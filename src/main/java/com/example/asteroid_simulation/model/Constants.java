package com.example.asteroid_simulation.model;

public class Constants {

    // Stała grawitacyjna Newtona m³ / (kg · s²)
    public static final double G = 6.674e-11;

    // Masa Ziemi. kg
    public static final double EARTH_MASS = 5.972e24;

    // Średni promień Ziemi. m
    public static final double EARTH_R = 6.371e6;


    // Umowna granica atmosfery (linia Kármána). m
    public static final double ATM_HEIGHT = 100_000;

    // Gęstość powietrza na poziomie morza. kg / m³
    public static final double RHO_AIR_0 = 1.225;

    /**
     * Wysokość skali atmosfery używana w modelu barometrycznym:
     * <p>
     * ρ(h) = ρ₀ · exp(-h / H_SCALE)
     * <p>
     * Jednostka:
     * m
     */
    public static final double H_SCALE = 8500;

    /**
     * Współczynnik oporu aerodynamicznego.
     * <p>
     * Wielkość bezwymiarowa.
     */
    public static final double C_DRAG = 1.0;

    /**
     * Energia odpowiadająca wybuchowi 1 tony TNT.
     * <p>
     * Jednostka:
     * J (dżule)
     * <p>
     * 1 t TNT = 4.184 × 10⁹ J
     */
    public static final double YIELD_TNT = 4.184e9;
}