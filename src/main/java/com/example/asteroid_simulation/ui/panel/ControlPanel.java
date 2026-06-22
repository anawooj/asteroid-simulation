package com.example.asteroid_simulation.ui.panel;

import com.example.asteroid_simulation.model.Composition;
import com.example.asteroid_simulation.ui.common.SectionLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlPanel extends JPanel {

    public JButton btnSimulate;

    private JSlider sliderDiameter;
    private JSlider sliderVelocity;
    private JSlider sliderAngle;
    private JComboBox<Composition> cmbComposition;

    /** Wywolywane po kliknieciu "SYMULUJ IMPAKT". Ustawiane z zewnatrz
     *  (przez WindowInit) poprzez setOnSimulate, bo w momencie budowania
     *  tego panelu Simulation jeszcze nie istnieje (potrzebuje takze
     *  CenterPanel/ResultPanel/ImpactCalculator). */
    private Runnable onSimulate;

    // ── Panel kontrolny (lewy) ───────────────────────────────────────────────
    public JPanel buildControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(22, 28, 48));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Średnica
        JLabel lblDiameter = paramLabel("Średnica: 100 m");
        sliderDiameter = makeSlider(10, 2000, 100);
        sliderDiameter.addChangeListener(e -> lblDiameter.setText(
                "Średnica: " + sliderDiameter.getValue() + " m"));
        panel.add(lblDiameter);
        panel.add(sliderDiameter);
        panel.add(Box.createVerticalStrut(12));

        // Prędkość
        JLabel lblVelocity = paramLabel("Prędkość: 20 km/s");
        sliderVelocity = makeSlider(10, 70, 20);
        sliderVelocity.addChangeListener(e -> lblVelocity.setText(
                "Prędkość: " + sliderVelocity.getValue() + " km/s"));
        panel.add(lblVelocity);
        panel.add(sliderVelocity);
        panel.add(Box.createVerticalStrut(12));

        // Kąt wejścia
        JLabel lblAngle = paramLabel("Kąt wejścia: 45°");
        sliderAngle = makeSlider(5, 90, 45);
        sliderAngle.addChangeListener(e -> lblAngle.setText(
                "Kąt wejścia: " + sliderAngle.getValue() + "°"));
        panel.add(lblAngle);
        panel.add(sliderAngle);
        panel.add(Box.createVerticalStrut(16));

        // Skład
        panel.add(paramLabel("Skład:"));
        cmbComposition = new JComboBox<>(Composition.values());
        styleCombo(cmbComposition);
        panel.add(cmbComposition);
        panel.add(Box.createVerticalStrut(12));

        // Przycisk
        btnSimulate = new JButton("SYMULUJ IMPAKT");
        btnSimulate.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSimulate.setBackground(new Color(200, 60, 40));
        btnSimulate.setForeground(Color.WHITE);
        btnSimulate.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSimulate.setFocusPainted(false);
        btnSimulate.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnSimulate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSimulate.setMaximumSize(new Dimension(240, 44));
        btnSimulate.addActionListener(e -> {
            if (onSimulate != null) {
                onSimulate.run();
            }
        });
        btnSimulate.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnSimulate.setBackground(new Color(230, 80, 55));
            }

            public void mouseExited(MouseEvent e) {
                btnSimulate.setBackground(new Color(200, 60, 40));
            }
        });
        panel.add(btnSimulate);
        panel.add(Box.createVerticalGlue());

        // Legenda
        panel.add(SectionLabel.sectionLabel("LEGENDA"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(legendRow(new Color(180, 0, 0), "Krater / całk. zniszczenie"));
        panel.add(legendRow(new Color(220, 80, 0), "Ciężkie uszkodzenia"));
        panel.add(legendRow(new Color(220, 190, 0), "Wybite szyby"));
        panel.add(legendRow(new Color(40, 160, 60), "Odczuwalna fala uderzeniowa"));
        panel.add(Box.createVerticalStrut(12));

        return panel;
    }

    /** Ustawia akcje wywolywana po kliknieciu przycisku symulacji.
     *  Wywolaj PO buildControlPanel(). */
    public void setOnSimulate(Runnable onSimulate) {
        this.onSimulate = onSimulate;
    }

    public double getDiameter() {
        return sliderDiameter.getValue();
    }

    public double getVelocity() {
        return sliderVelocity.getValue();
    }

    public double getAngle() {
        return sliderAngle.getValue();
    }

    public Composition getComposition() {
        return (Composition) cmbComposition.getSelectedItem();
    }

    private JSlider makeSlider(int min, int max, int val) {
        JSlider s = new JSlider(min, max, val);
        s.setBackground(new Color(22, 28, 48));
        s.setForeground(new Color(140, 170, 255));
        s.setMaximumSize(new Dimension(250, 36));
        return s;
    }

    private JLabel paramLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(180, 200, 255));
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(new EmptyBorder(0, 4, 2, 0));
        return l;
    }

    private void styleCombo(JComboBox<?> c) {
        c.setBackground(new Color(30, 40, 70));
        c.setForeground(new Color(200, 220, 255));
        c.setFont(new Font("SansSerif", Font.PLAIN, 12));
        c.setMaximumSize(new Dimension(250, 30));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JPanel legendRow(Color color, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        row.setBackground(new Color(22, 28, 48));
        row.setMaximumSize(new Dimension(260, 24));
        JLabel dot = new JLabel("●");
        dot.setForeground(color);
        dot.setFont(new Font("SansSerif", Font.BOLD, 14));
        JLabel txt = new JLabel(text);
        txt.setForeground(new Color(180, 200, 255));
        txt.setFont(new Font("SansSerif", Font.PLAIN, 11));
        row.add(dot);
        row.add(txt);
        return row;
    }

    public void setSimulationRunning(boolean running) {

        btnSimulate.setEnabled(!running);

        if (running) {
            btnSimulate.setText("Obliczam...");
        } else {
            btnSimulate.setText("SYMULUJ IMPAKT");
        }
    }
}