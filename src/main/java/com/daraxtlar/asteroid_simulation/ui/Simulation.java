package com.daraxtlar.asteroid_simulation.ui;

import com.daraxtlar.asteroid_simulation.model.ImpactResult;
import com.daraxtlar.asteroid_simulation.physics.ImpactCalculator;
import com.daraxtlar.asteroid_simulation.ui.panel.CenterPanel;
import com.daraxtlar.asteroid_simulation.ui.panel.ControlPanel;
import com.daraxtlar.asteroid_simulation.ui.panel.ResultPanel;

import javax.swing.*;

public class Simulation {

    private final ControlPanel controlPanel;
    private final CenterPanel centerPanel;
    private final ResultPanel resultPanel;
    private final ImpactCalculator calculator;

    private ImpactResult lastResult;

    public Simulation(
            ControlPanel controlPanel,
            CenterPanel centerPanel,
            ResultPanel resultPanel,
            ImpactCalculator calculator) {

        this.controlPanel = controlPanel;
        this.centerPanel = centerPanel;
        this.resultPanel = resultPanel;
        this.calculator = calculator;
    }

    public void runSimulation() {

        controlPanel.setSimulationRunning(true);
        centerPanel.lockImpactPoint();

        SwingWorker<ImpactResult, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected ImpactResult doInBackground() {

                        return calculator.simulate(
                                controlPanel.getDiameter(),
                                controlPanel.getVelocity(),
                                controlPanel.getAngle(),
                                controlPanel.getComposition(),
                                centerPanel.getImpactLocation()
                        );
                    }

                    @Override
                    protected void done() {

                        try {

                            lastResult = get();

                            centerPanel.setResult(lastResult);

                            resultPanel.setResult(lastResult);

                        } catch (Exception ex) {
                            ex.printStackTrace();

                        } finally {

                            controlPanel.setSimulationRunning(false);
                        }
                    }
                };

        worker.execute();
    }
}