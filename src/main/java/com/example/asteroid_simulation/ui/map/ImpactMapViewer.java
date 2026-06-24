package com.example.asteroid_simulation.ui.map;

import com.example.asteroid_simulation.model.ImpactResult;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class ImpactMapViewer extends JLayeredPane {

    private GeoPosition impactLocation;
    private ImpactResult result;
    private final ImpactOverlayPainter overlayPainter = new ImpactOverlayPainter();
    private final JXMapViewer imageryViewer;
    private final JXMapViewer labelsViewer;

    // NOWE: po kliknieciu "SYMULUJ" mapa zostaje zablokowana - nie da sie
    // klikniecem ustawic nowego punktu uderzenia, a marker punktu impaktu
    // jest chwilowo skryty (zostaja tylko narysowane strefy skutkow), dopoki
    // uzytkownik nie kliknie "RESETUJ SYMULACJE".
    private boolean locked = false;

    public ImpactMapViewer() {

        imageryViewer = new JXMapViewer();
        labelsViewer  = new JXMapViewer();

        imageryViewer.setTileFactory(new EsriSatelliteTileFactory());

        labelsViewer.setTileFactory(new EsriLabelsTileFactory());

        labelsViewer.setOpaque(false);

        add(imageryViewer, Integer.valueOf(0));
        add(labelsViewer, Integer.valueOf(1));

        imageryViewer.setAddressLocation(new GeoPosition(52.2297, 21.0122));
        labelsViewer.setAddressLocation(new GeoPosition(52.2297, 21.0122));

        imageryViewer.setZoom(6);
        labelsViewer.setZoom(6);

        MouseInputListener mia = new PanMouseInputListener(imageryViewer);

        ZoomMouseWheelListenerCenter zoomListener =
                new ZoomMouseWheelListenerCenter(imageryViewer);

        imageryViewer.addMouseWheelListener(zoomListener);
        labelsViewer.addMouseWheelListener(zoomListener);

        imageryViewer.addPropertyChangeListener(evt -> {

            String name = evt.getPropertyName();

            if ("zoom".equals(name)
                    || "center".equals(name)
                    || "centerPosition".equals(name)) {

                syncViewers();
            }
        });

        labelsViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (locked) {
                    // blokada: dopoki symulacja nie zostanie zresetowana,
                    // klikanie na mapie nie zmienia miejsca uderzenia
                    return;
                }

                impactLocation = pointToGeoPosition(e.getPoint());

                repaint();
            }
        });

        labelsViewer.addMouseListener(mia);
        labelsViewer.addMouseMotionListener(mia);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g.create();

        overlayPainter.paint(
                g2,
                imageryViewer,
                impactLocation,
                result,
                !locked
        );

        g2.dispose();
    }

    public GeoPosition getImpactLocation() {
        return impactLocation;
    }

    private GeoPosition pointToGeoPosition(Point point) {

        Rectangle viewport = imageryViewer.getViewportBounds();

        Point2D worldPoint =
                new Point2D.Double(
                        point.getX() + viewport.getX(),
                        point.getY() + viewport.getY()
                );

        return imageryViewer.getTileFactory()
                .pixelToGeo(
                        worldPoint,
                        imageryViewer.getZoom()
                );
    }

    public void setResult(ImpactResult result) {
        this.result = result;
        repaint();
    }

    /** Wywolywane po kliknieciu "SYMULUJ" - chowa marker punktu uderzenia
     *  i blokuje wybieranie nowego miejsca klikniciem na mapie. */
    public void lockImpactPoint() {
        locked = true;
        repaint();
    }

    /** Wywolywane po kliknieciu "RESETUJ SYMULACJE" - usuwa narysowane
     *  strefy skutkow i odblokowuje wybieranie nowego miejsca uderzenia. */
    public void resetSimulation() {
        locked = false;
        result = null;
        repaint();
    }

    @Override
    public void doLayout() {

        imageryViewer.setBounds(
                0, 0,
                getWidth(),
                getHeight());

        labelsViewer.setBounds(
                0, 0,
                getWidth(),
                getHeight());
    }

    private void syncViewers() {

        labelsViewer.setZoom(
                imageryViewer.getZoom());

        labelsViewer.setCenterPosition(
                imageryViewer.getCenterPosition());
    }
}