package com.example.asteroid_simulation.ui;

import com.example.asteroid_simulation.ui.common.RoundedBorder;
import com.example.asteroid_simulation.ui.common.SectionLabel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * UWAGA: ta klasa nie jest juz uzywana w WindowInit (poprzednio byla
 * tworzona i natychmiast odrzucana - "PanelInit pi = new PanelInit();"
 * bez dodania jej gdziekolwiek). Zostawiona w projekcie na wypadek,
 * gdyby byla potrzebna do dalszej rozbudowy; mozna ja bezpiecznie usunac.
 */
public class PanelInit extends JPanel {

    public PanelInit() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(22, 28, 48));

        setBorder(new CompoundBorder(
                new EmptyBorder(12, 12, 12, 8),
                new RoundedBorder(new Color(60, 80, 140), 10)
        ));

        setPreferredSize(new Dimension(280, 600));

        add(SectionLabel.sectionLabel("PARAMETRY ASTEROIDY"));
        add(Box.createVerticalStrut(8));
    }
}