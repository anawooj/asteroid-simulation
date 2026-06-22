package com.example.asteroid_simulation.model;

public class ImpactResult {

    public final double diameterM;
    public final double velKms;
    public final double angleDeg;
    public final Composition composition;
    public final double E_MT;
    public final double craterKm;
    public final double r_total;
    public final double r_heavy;
    public final double r_glass;
    public final double r_wave;
    public final String mapView;
    final double latitude;
    final double longitude;
    final double mass0, massImpact, massLostPct;
    final double Ek0_MT;
    final double energyAtmPct, maxPressureKPa;

    public ImpactResult(
            double diameterM,
            double velKms,
            double angleDeg,
            Composition composition,
            double latitude,
            double longitude,
            double mass0,
            double massImpact,
            double massLostPct,
            double Ek0_MT,
            double E_MT,
            double craterKm,
            double r_total,
            double r_heavy,
            double r_glass,
            double r_wave,
            String mapView,
            double energyAtmPct,
            double maxPressureKPa) {

        this.diameterM = diameterM;
        this.velKms = velKms;
        this.angleDeg = angleDeg;

        this.composition = composition;

        this.latitude = latitude;
        this.longitude = longitude;

        this.mass0 = mass0;
        this.massImpact = massImpact;
        this.massLostPct = massLostPct;

        this.Ek0_MT = Ek0_MT;
        this.E_MT = E_MT;

        this.craterKm = craterKm;

        this.r_total = r_total;
        this.r_heavy = r_heavy;
        this.r_glass = r_glass;
        this.r_wave = r_wave;

        this.mapView = mapView;

        this.energyAtmPct = energyAtmPct;
        this.maxPressureKPa = maxPressureKPa;
    }

    public String toReport() {

        return String.format("""
                        ══════════════════════
                          RAPORT IMPAKTU
                        ══════════════════════
                        
                        📍 Punkt impaktu
                        
                        Szerokość:
                        %.4f
                        
                        Długość:
                        %.4f
                        
                        ── ASTEROID ──────────
                        Średnica:    %.0f m
                        Prędkość:    %.0f km/s
                        Kąt wejścia: %.0f°
                        Skład:       %s
                        Masa pocz.:  %s
                        Masa przy ud.:%s
                        Utrata masy: %.1f%%
                        
                        ── ENERGIA ───────────
                        E₀ (przed atm.):
                          %.3f Mt TNT
                        E (przy uderzeniu):
                          %.3f Mt TNT
                        Zatrzymane w atm.:
                          %.1f%%
                        Max ciśn. dyn.:
                          %.0f kPa
                        
                        ── KRATER ────────────
                        Promień krateru:
                          %.2f km
                        
                        ── STREFY SZKÓD ──────
                        🔴 Całk. zniszczenie:
                           r = %.1f km
                        
                        🟠 Ciężkie uszkodz.:
                           r = %.1f km
                        
                        🟡 Wybite szyby:
                           r = %.1f km
                        
                        🟢 Odczuw. fala:
                           r = %.1f km
                        
                        ── WIDOK MAPY ────────
                        %s
                        """,
                latitude,
                longitude,

                diameterM,
                velKms,
                angleDeg,
                composition,

                humanMass(mass0),
                humanMass(massImpact),
                massLostPct,

                Ek0_MT,
                E_MT,
                energyAtmPct,
                maxPressureKPa,

                craterKm,

                r_total,
                r_heavy,
                r_glass,
                r_wave,

                mapView.toUpperCase()
        );
    }

    private String humanMass(double kg) {
        if (kg < 1e3) return String.format("%.1f kg", kg);
        if (kg < 1e6) return String.format("%.2f t", kg / 1e3);
        if (kg < 1e9) return String.format("%.2f kt", kg / 1e6);
        if (kg < 1e12) return String.format("%.2f Mt", kg / 1e9);
        return String.format("%.3e kg", kg);
    }

    public double getDiameterM() {
        return diameterM;
    }

    public double getVelKms() {
        return velKms;
    }

    public double getAngleDeg() {
        return angleDeg;
    }

    public Composition getComposition() {
        return composition;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getMass0() {
        return mass0;
    }

    public double getMassImpact() {
        return massImpact;
    }

    public double getMassLostPct() {
        return massLostPct;
    }

    public double getEk0_MT() {
        return Ek0_MT;
    }

    public double getE_MT() {
        return E_MT;
    }

    public double getCraterKm() {
        return craterKm;
    }

    public double getR_total() {
        return r_total;
    }

    public double getR_heavy() {
        return r_heavy;
    }

    public double getR_glass() {
        return r_glass;
    }

    public double getR_wave() {
        return r_wave;
    }

    public String getMapView() {
        return mapView;
    }

    public double getEnergyAtmPct() {
        return energyAtmPct;
    }

    public double getMaxPressureKPa() {
        return maxPressureKPa;
    }
}