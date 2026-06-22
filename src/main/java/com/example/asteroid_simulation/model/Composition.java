package com.example.asteroid_simulation.model;

public enum Composition {

    KAMIENNA("Kamienna", 2700),
    ZELAZNA("Żelazna", 7800),
    LODOWA("Lodowa", 900);

    private final String displayName;
    private final double density;

    Composition(String displayName, double density) {
        this.displayName = displayName;
        this.density = density;
    }

    public double getDensity() {
        return density;
    }

    @Override
    public String toString() {
        return displayName;
    }
}