package com.example.asteroid_simulation.ui;

import com.example.asteroid_simulation.physics.ImpactCalculator;
import com.example.asteroid_simulation.ui.panel.CenterPanel;
import com.example.asteroid_simulation.ui.panel.ControlPanel;
import com.example.asteroid_simulation.ui.panel.ResultPanel;

import javax.swing.*;
import java.awt.*;

public class WindowInit extends JFrame {

    public WindowInit() {
        initializeFrame();
        initializeComponents();
    }

    private void initializeComponents() {

        ResultPanel resultPanel = new ResultPanel();
        ControlPanel controlPanel = new ControlPanel();
        CenterPanel centerPanel = new CenterPanel();

        JPanel controlUi = controlPanel.buildControlPanel();
        JPanel centerUi = centerPanel.buildCenterPanel();
        JPanel resultUi = resultPanel.buildResultPanel();

        ImpactCalculator calculator = new ImpactCalculator();
        Simulation simulation = new Simulation(controlPanel, centerPanel, resultPanel, calculator);
        controlPanel.setOnSimulate(simulation::runSimulation);

        controlPanel.setOnReset(() -> {
            centerPanel.resetSimulation();
            resultPanel.reset();
        });

        add(controlUi, BorderLayout.WEST);
        add(centerUi, BorderLayout.CENTER);
        add(resultUi, BorderLayout.EAST);

        pack();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Symulator Uderzenia Asteroidy");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(new Color(15, 20, 35));

        setMinimumSize(new Dimension(1200, 720));
        setLocationRelativeTo(null);
    }
}