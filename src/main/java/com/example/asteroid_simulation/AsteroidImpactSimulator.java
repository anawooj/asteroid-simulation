package com.example.asteroid_simulation;

import com.example.asteroid_simulation.ui.WindowInit;

import javax.swing.*;

public class AsteroidImpactSimulator extends JFrame {

    public AsteroidImpactSimulator() {
        super("Symulator Uderzenia Asteroidy");

        WindowInit wi = new WindowInit();
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