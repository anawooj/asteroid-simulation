package com.example.asteroid_simulation.physics;

import com.example.asteroid_simulation.model.Composition;
import com.example.asteroid_simulation.model.ImpactResult;
import org.jxmapviewer.viewer.GeoPosition;

import static com.example.asteroid_simulation.model.Constants.*;

public class ImpactCalculator {

    private static double computeCraterRadius(double Ek_J, double density) {
        double scaleFactor = density < 1500 ? 0.011 : density < 5000 ? 0.0133 : 0.016;
        // Numeryczna całka trapezów dla weryfikacji (skalowanie energetyczne)
        double R_est = scaleFactor * Math.pow(Ek_J, 0.294); // metry

        // Całkowanie numeryczne ciśnienia w gruncie (metoda prostokątów, 1000 kroków)
        // P(r) = P0 * (R/r)^3,  P0 = Ek / (2π R³/3)
        int N = 1000;
        double dr = R_est / N;
        double P0 = Ek_J / (2 * Math.PI * R_est * R_est * R_est / 3.0);
        double P_threshold = 1e9; // 1 GPa – próg zniszczenia skały
        double R_crater = R_est;

        for (int i = 1; i <= N; i++) {
            double r = i * dr;
            double P = P0 * Math.pow(R_est / r, 3);
            if (P < P_threshold) {
                R_crater = r;
                break;
            }
        }
        return R_crater;
    }

    public ImpactResult simulate(
            double diameterM,
            double velKms,
            double angleDeg,
            Composition composition,
            GeoPosition impactLocation) {

        double latitude = impactLocation.getLatitude();
        double longitude = impactLocation.getLongitude();

        double density = composition.getDensity();

        // Masa początkowa
        double radius0 = diameterM / 2.0;
        double volume0 = (4.0 / 3.0) * Math.PI * radius0 * radius0 * radius0;
        double mass0 = density * volume0;

        // Prędkość wejścia i kąt w radianach
        double v0 = velKms * 1000.0;       // m/s
        double angleRad = Math.toRadians(angleDeg);
        double sinTheta = Math.sin(angleRad);

        // Energia kinetyczna przed atmosferą
        double Ek0 = 0.5 * mass0 * v0 * v0;

        // ── Całkowanie RK4 przez atmosferę ───────────────────────────────────
        // Stan: [h (wysokość m), v (prędkość m/s), m (masa kg)]
        // Równania ruchu:
        //   dh/dt = -v * sin(theta)
        //   dv/dt = -F_drag/m - g*sin(theta)
        //   dm/dt = -ablacja (uproszczona)

        double h = ATM_HEIGHT;
        double v = v0;
        double m = mass0;
        double dt = 0.5; // krok całkowania [s]

        double g = G * EARTH_MASS / (EARTH_R * EARTH_R); // ~9.81 m/s²

        // Akumulujemy pracę siły oporu (całka ∫F·ds) – energia zdeponowana w atmosferze
        double energyDepositedInAtm = 0.0;
        double maxPressure = 0.0; // maksymalne ciśnienie dynamiczne [Pa]

        while (h > 0 && v > 100) {
            // Gęstość powietrza na wysokości h (model barometryczny)
            double rhoAir = RHO_AIR_0 * Math.exp(-h / H_SCALE);

            // Przekrój poprzeczny (zakładamy sferyczny kształt)
            double r = Math.cbrt(m / (density * (4.0 / 3.0) * Math.PI)); // promień [m]
            double A = Math.PI * r * r;

            // Siła oporu: F_d = ½ ρ v² Cd A
            double Fd = 0.5 * rhoAir * v * v * C_DRAG * A;

            // Ciśnienie dynamiczne
            double q = 0.5 * rhoAir * v * v;
            if (q > maxPressure) maxPressure = q;

            // Ablacja: uproszczona – asteroida traci masę proporcjonalnie do strumienia energii
            double CH = 0.1;   // wsp. przejmowania ciepła
            double Qab = 8e6;   // ciepło ablacji [J/kg]
            double dmdt = -CH * rhoAir * v * v * v * A / Qab;

            // Pochodne (układ równań)
            double dhdt = -v * sinTheta;
            double dvdt = -(Fd / m) - g * sinTheta;

            // RK4 – uproszczony (jeden krok dla h i v, ablacja osobno)
            // k1
            double k1h = dhdt;
            double k1v = dvdt;
            // k2
            double h2 = h + 0.5 * dt * k1h;
            double v2 = v + 0.5 * dt * k1v;
            double rhoAir2 = RHO_AIR_0 * Math.exp(-h2 / H_SCALE);
            double Fd2 = 0.5 * rhoAir2 * v2 * v2 * C_DRAG * A;
            double k2h = -v2 * sinTheta;
            double k2v = -(Fd2 / m) - g * sinTheta;
            // k3
            double h3 = h + 0.5 * dt * k2h;
            double v3 = v + 0.5 * dt * k2v;
            double rhoAir3 = RHO_AIR_0 * Math.exp(-h3 / H_SCALE);
            double Fd3 = 0.5 * rhoAir3 * v3 * v3 * C_DRAG * A;
            double k3h = -v3 * sinTheta;
            double k3v = -(Fd3 / m) - g * sinTheta;
            // k4
            double h4 = h + dt * k3h;
            double v4 = v + dt * k3v;
            double rhoAir4 = RHO_AIR_0 * Math.exp(-h4 / H_SCALE);
            double Fd4 = 0.5 * rhoAir4 * v4 * v4 * C_DRAG * A;
            double k4h = -v4 * sinTheta;
            double k4v = -(Fd4 / m) - g * sinTheta;

            // Aktualizacja stanu
            double hNew = h + (dt / 6.0) * (k1h + 2 * k2h + 2 * k3h + k4h);
            double vNew = v + (dt / 6.0) * (k1v + 2 * k2v + 2 * k3v + k4v);
            double mNew = Math.max(m + dmdt * dt, 1.0);

            // Energia zdeponowana w atmosferze = praca siły oporu
            energyDepositedInAtm += Fd * v * dt;

            h = hNew;
            v = Math.max(vNew, 0);
            m = mNew;
        }

        // ── Energia kinetyczna przy impakcie ─────────────────────────────────
        double Ek_impact = 0.5 * m * v * v;

        // Całkujemy energię uwalnianą w glebie metodą trapezów (model Holsapple'a)
        double craterRadius = computeCraterRadius(Ek_impact, density);

        // ── Strefy zniszczeń (skalowanie wg energii) ─────────────────────────
        // NAPRAWIONO: 1 megaton TNT = YIELD_TNT [J/t] * 1 000 000 [t],
        // a nie * 1000 (to dawalo wynik w kilotonach podpisany jako "Mt",
        // czyli wartosci 1000x za duze).
        double E_MT = Ek_impact / (YIELD_TNT * 1_000_000); // energia w megaton TNT
        double Ek0_MT = Ek0 / (YIELD_TNT * 1_000_000);

        // Promienie stref [km]
        double r_total = 0.28 * Math.pow(E_MT, 0.33);   // całk. zniszczenie (>138 kPa)
        double r_heavy = 0.80 * Math.pow(E_MT, 0.33);   // ciężkie uszkodz. (>35 kPa)
        double r_glass = 5.0 * Math.pow(E_MT, 0.33);   // wybite szyby     (>3 kPa)
        double r_wave = 12.0 * Math.pow(E_MT, 0.33);   // fala odczuw.     (>1 kPa)

        // Kategoria widoku mapy
        double maxR = r_wave;
        String mapView;
        if (maxR < 5) mapView = "centrum miasta";
        else if (maxR < 50) mapView = "całe miasto";
        else if (maxR < 200) mapView = "województwo";
        else if (maxR < 1000) mapView = "kraj";
        else mapView = "Europa";

        double massLost = (mass0 - m) / mass0 * 100;
        double energyAtmPct = energyDepositedInAtm / Ek0 * 100;

        System.out.printf(
                "E=%.3f Mt  total=%.2f heavy=%.2f glass=%.2f wave=%.2f%n",
                E_MT,
                r_total,
                r_heavy,
                r_glass,
                r_wave
        );

        return new ImpactResult(
                diameterM,
                velKms,
                angleDeg,
                composition,
                latitude,
                longitude,
                mass0,
                m,
                massLost,
                Ek0_MT,
                E_MT,
                craterRadius / 1000.0,
                r_total,
                r_heavy,
                r_glass,
                r_wave,
                mapView,
                energyAtmPct,
                maxPressure / 1000.0
        );
    }
}