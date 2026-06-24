package com.daraxtlar.asteroid_simulation;

import com.daraxtlar.asteroid_simulation.ui.WindowInit;

import javax.swing.*;

public class AsteroidImpactSimulator extends JFrame {

    public AsteroidImpactSimulator() {
        new WindowInit();
    }

    static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new AsteroidImpactSimulator();
        });
    }
}