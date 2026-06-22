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

        // WAZNE: build*Panel() musi byc wywolane PRZED utworzeniem Simulation,
        // bo to wlasnie te metody inicjalizuja wewnetrzne pola (slidery,
        // mapPanel, pole tekstowe wynikow), z ktorych Simulation pozniej
        // korzysta poprzez gettery. Wczesniej Simulation byl tworzony w
        // ControlPanel (przed zbudowaniem pozostalych paneli), co w ogole
        // sie nie kompilowalo.
        JPanel controlUi = controlPanel.buildControlPanel();
        JPanel centerUi  = centerPanel.buildCenterPanel();
        JPanel resultUi  = resultPanel.buildResultPanel();

        ImpactCalculator calculator = new ImpactCalculator();
        Simulation simulation = new Simulation(controlPanel, centerPanel, resultPanel, calculator);
        controlPanel.setOnSimulate(simulation::runSimulation);

        add(controlUi, BorderLayout.WEST);
        add(centerUi, BorderLayout.CENTER);
        add(resultUi, BorderLayout.EAST);

        pack();
        setVisible(true);
    }

    private void initializeFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(new Color(15, 20, 35));

        setMinimumSize(new Dimension(1200, 720));
        setLocationRelativeTo(null);
    }
}