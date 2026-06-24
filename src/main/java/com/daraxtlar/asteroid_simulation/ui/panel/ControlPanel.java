package com.daraxtlar.asteroid_simulation.ui.panel;

import com.daraxtlar.asteroid_simulation.model.Composition;
import com.daraxtlar.asteroid_simulation.ui.common.RoundedBorder;
import com.daraxtlar.asteroid_simulation.ui.common.SectionLabel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlPanel extends JPanel {

    public JButton btnSimulate;
    public JButton btnReset;

    private JSlider sliderDiameter;
    private JSlider sliderVelocity;
    private JSlider sliderAngle;
    private JComboBox<Composition> cmbComposition;

    private Runnable onSimulate;
    private Runnable onReset;

    public JPanel buildControlPanel() {
        // Zewnętrzny wrapper z BorderLayout – tytuł na górze, reszta poniżej.
        // Identyczna struktura jak w ResultPanel.
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(22, 28, 48));
        wrapper.setBorder(new CompoundBorder(
                new EmptyBorder(12, 12, 12, 8),
                new RoundedBorder(new Color(60, 80, 140), 10)
        ));
        wrapper.setPreferredSize(new Dimension(280, 600));

        JLabel title = new JLabel("PARAMETRY SYMULACJI", SwingConstants.CENTER);
        title.setForeground(new Color(180, 200, 255));
        title.setFont(new Font("SansSerif", Font.BOLD, 13));
        title.setBorder(new EmptyBorder(8, 0, 8, 0));
        wrapper.add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(22, 28, 48));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0;

        int row = 0;

        // ── Średnica ──────────────────────────────────────────────────────────
        JLabel lblDiameter = paramLabel("Średnica: 100 m");
        sliderDiameter = makeSlider(10, 2000, 100);
        sliderDiameter.addChangeListener(e ->
                lblDiameter.setText("Średnica: " + sliderDiameter.getValue() + " m"));

        gbc.gridy = row++; gbc.insets = new Insets(10, 8, 0, 8);
        panel.add(lblDiameter, gbc);
        gbc.gridy = row++; gbc.insets = new Insets(2, 8, 0, 8);
        panel.add(sliderDiameter, gbc);

        // ── Prędkość ──────────────────────────────────────────────────────────
        JLabel lblVelocity = paramLabel("Prędkość: 20 km/s");
        sliderVelocity = makeSlider(10, 70, 20);
        sliderVelocity.addChangeListener(e ->
                lblVelocity.setText("Prędkość: " + sliderVelocity.getValue() + " km/s"));

        gbc.gridy = row++; gbc.insets = new Insets(10, 8, 0, 8);
        panel.add(lblVelocity, gbc);
        gbc.gridy = row++; gbc.insets = new Insets(2, 8, 0, 8);
        panel.add(sliderVelocity, gbc);

        // ── Kąt wejścia ───────────────────────────────────────────────────────
        JLabel lblAngle = paramLabel("Kąt wejścia: 45°");
        sliderAngle = makeSlider(5, 90, 45);
        sliderAngle.addChangeListener(e ->
                lblAngle.setText("Kąt wejścia: " + sliderAngle.getValue() + "°"));

        gbc.gridy = row++; gbc.insets = new Insets(10, 8, 0, 8);
        panel.add(lblAngle, gbc);
        gbc.gridy = row++; gbc.insets = new Insets(2, 8, 0, 8);
        panel.add(sliderAngle, gbc);

        // ── Skład ─────────────────────────────────────────────────────────────
        gbc.gridy = row++; gbc.insets = new Insets(14, 8, 0, 8);
        panel.add(paramLabel("Skład:"), gbc);

        cmbComposition = new JComboBox<>(Composition.values());
        styleCombo(cmbComposition);
        gbc.gridy = row++; gbc.insets = new Insets(2, 8, 0, 8);
        panel.add(cmbComposition, gbc);

        // ── Przyciski ─────────────────────────────────────────────────────────
        gbc.gridy = row++; gbc.insets = new Insets(18, 8, 0, 8);
        panel.add(buildSimulateButton(), gbc);

        gbc.gridy = row++; gbc.insets = new Insets(6, 8, 0, 8);
        panel.add(buildResetButton(), gbc);

        // ── Wypełniacz (pcha legendę na dół) ─────────────────────────────────
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        GridBagConstraints gbcSpacer = new GridBagConstraints();
        gbcSpacer.gridx = 0; gbcSpacer.gridy = row++;
        gbcSpacer.weightx = 1.0; gbcSpacer.weighty = 1.0;
        gbcSpacer.fill = GridBagConstraints.BOTH;
        panel.add(spacer, gbcSpacer);

        // ── Legenda ───────────────────────────────────────────────────────────
        gbc.gridy = row++; gbc.weighty = 0; gbc.insets = new Insets(4, 8, 2, 8);
        panel.add(SectionLabel.sectionLabel("LEGENDA"), gbc);

        gbc.gridy = row++; gbc.insets = new Insets(2, 8, 0, 8);
        panel.add(legendRow(new Color(180, 0, 0),   "Krater / całk. zniszczenie"), gbc);
        gbc.gridy = row++;
        panel.add(legendRow(new Color(220, 80, 0),  "Ciężkie uszkodzenia"), gbc);
        gbc.gridy = row++;
        panel.add(legendRow(new Color(220, 190, 0), "Wybite szyby"), gbc);
        gbc.gridy = row++; gbc.insets = new Insets(2, 8, 10, 8);
        panel.add(legendRow(new Color(40, 160, 60), "Odczuwalna fala uderzeniowa"), gbc);

        wrapper.add(panel, BorderLayout.CENTER);
        return wrapper;
    }

    private JButton buildSimulateButton() {
        btnSimulate = new JButton("SYMULUJ IMPAKT");
        btnSimulate.setBackground(new Color(200, 60, 40));
        btnSimulate.setForeground(Color.WHITE);
        btnSimulate.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSimulate.setFocusPainted(false);
        btnSimulate.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnSimulate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSimulate.addActionListener(e -> { if (onSimulate != null) onSimulate.run(); });
        btnSimulate.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnSimulate.setBackground(new Color(230, 80, 55)); }
            public void mouseExited(MouseEvent e)  { btnSimulate.setBackground(new Color(200, 60, 40)); }
        });
        return btnSimulate;
    }

    private JButton buildResetButton() {
        btnReset = new JButton("RESETUJ SYMULACJĘ");
        btnReset.setBackground(new Color(60, 80, 140));
        btnReset.setForeground(Color.WHITE);
        btnReset.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnReset.setFocusPainted(false);
        btnReset.setBorder(new EmptyBorder(8, 20, 8, 20));
        btnReset.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnReset.addActionListener(e -> { if (onReset != null) onReset.run(); });
        btnReset.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnReset.setBackground(new Color(80, 100, 165)); }
            public void mouseExited(MouseEvent e)  { btnReset.setBackground(new Color(60, 80, 140)); }
        });
        return btnReset;
    }

    public void setOnSimulate(Runnable onSimulate) { this.onSimulate = onSimulate; }
    public void setOnReset(Runnable onReset)       { this.onReset = onReset; }

    public double getDiameter()         { return sliderDiameter.getValue(); }
    public double getVelocity()         { return sliderVelocity.getValue(); }
    public double getAngle()            { return sliderAngle.getValue(); }
    public Composition getComposition() { return (Composition) cmbComposition.getSelectedItem(); }

    public void setSimulationRunning(boolean running) {
        btnSimulate.setEnabled(!running);
        btnSimulate.setText(running ? "Obliczam..." : "SYMULUJ IMPAKT");
    }

    private JSlider makeSlider(int min, int max, int val) {
        JSlider s = new JSlider(min, max, val);
        s.setBackground(new Color(22, 28, 48));
        s.setForeground(new Color(140, 170, 255));
        return s;
    }

    private JLabel paramLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(180, 200, 255));
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return l;
    }

    private void styleCombo(JComboBox<?> c) {
        c.setBackground(new Color(30, 40, 70));
        c.setForeground(new Color(200, 220, 255));
        c.setFont(new Font("SansSerif", Font.PLAIN, 12));
    }

    private JPanel legendRow(Color color, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        row.setBackground(new Color(22, 28, 48));
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
}