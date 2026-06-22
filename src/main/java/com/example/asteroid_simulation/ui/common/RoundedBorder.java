package com.example.asteroid_simulation.ui.common;

import javax.swing.border.AbstractBorder;
import java.awt.*;

// ── Zaokrąglona ramka ────────────────────────────────────────────────────
public class RoundedBorder extends AbstractBorder {
    private final Color color;
    private final int radius;

    public RoundedBorder(Color c, int r) {
        color = c;
        radius = r;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
    }
}

