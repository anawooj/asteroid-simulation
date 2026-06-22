package com.example.asteroid_simulation.ui.panel;

import com.example.asteroid_simulation.model.ImpactResult;
import com.example.asteroid_simulation.ui.map.ImpactMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CenterPanel {

    // NAPRAWIONO: wczesniej "mapPanel" byl zmienna lokalna w
    // buildCenterPanel() - nie dalo sie do niego dotrzec z getImpactLocation()
    // / setResult(), bo te metody w ogole nie istnialy.
    private ImpactMapViewer mapPanel;

    public JPanel buildCenterPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 6));
        wrapper.setBackground(new Color(15, 20, 35));
        wrapper.setBorder(new EmptyBorder(12, 4, 12, 4));

        JLabel title = new JLabel("MAPA ZAGROŻENIA", SwingConstants.CENTER);
        title.setForeground(new Color(180, 200, 255));
        title.setFont(new Font("SansSerif", Font.BOLD, 13));
        title.setBorder(new EmptyBorder(0, 0, 6, 0));
        wrapper.add(title, BorderLayout.NORTH);

        mapPanel = new ImpactMapViewer();
        wrapper.add(mapPanel, BorderLayout.CENTER);
        return wrapper;
    }

    /** Aktualne miejsce uderzenia wybrane na mapie (klikniecie) lub domyslne. */
    public GeoPosition getImpactLocation() {
        return mapPanel.getImpactLocation();
    }

    /** Przekazuje wynik symulacji do komponentu mapy, ktory rysuje strefy. */
    public void setResult(ImpactResult result) {
        mapPanel.setResult(result);
    }
}