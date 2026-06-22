package com.example.asteroid_simulation.ui.panel;

import com.example.asteroid_simulation.model.ImpactResult;
import com.example.asteroid_simulation.ui.common.RoundedBorder;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ResultPanel {

    private final JTextArea txtResults = new JTextArea();

    // ── Panel wyników (prawy) ────────────────────────────────────────────────
    public JPanel buildResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(22, 28, 48));
        panel.setBorder(new CompoundBorder(
                new EmptyBorder(12, 8, 12, 12),
                new RoundedBorder(new Color(60, 80, 140), 10)
        ));
        panel.setPreferredSize(new Dimension(280, 600));

        JLabel title = new JLabel("WYNIKI SYMULACJI", SwingConstants.CENTER);
        title.setForeground(new Color(180, 200, 255));
        title.setFont(new Font("SansSerif", Font.BOLD, 13));
        title.setBorder(new EmptyBorder(8, 0, 8, 0));
        panel.add(title, BorderLayout.NORTH);

        JTextArea txtResults = getJTextArea();

        JScrollPane scroll = new JScrollPane(txtResults);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(new Color(15, 20, 35));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JTextArea getJTextArea() {
        txtResults.setEditable(false);
        txtResults.setBackground(new Color(15, 20, 35));
        txtResults.setForeground(new Color(200, 220, 255));
        txtResults.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtResults.setBorder(new EmptyBorder(8, 8, 8, 8));
        txtResults.setLineWrap(true);
        txtResults.setWrapStyleWord(true);
        txtResults.setText("\n  Ustaw parametry i kliknij\n  [SYMULUJ IMPAKT]\n\n  Program obliczy:\n  • energię kinetyczną\n  • siłę uderzenia\n  • rozmiary stref szkód\n  • ekwiwalent w megaton");
        return txtResults;
    }

    public void setResult(ImpactResult result) {
        txtResults.setText(result.toReport());
        txtResults.setCaretPosition(0);
    }
}
