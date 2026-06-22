package com.example.asteroid_simulation.ui.common;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SectionLabel {

    public static JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(120, 150, 220));
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(new EmptyBorder(6, 4, 2, 0));
        return l;
    }
}
