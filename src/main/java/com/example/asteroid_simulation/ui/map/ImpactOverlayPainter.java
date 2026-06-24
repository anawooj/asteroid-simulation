package com.example.asteroid_simulation.ui.map;

import com.example.asteroid_simulation.model.ImpactResult;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.geom.Point2D;

public class ImpactOverlayPainter {

    public void paint(
            Graphics2D g2,
            JXMapViewer map,
            GeoPosition impactLocation,
            ImpactResult result,
            boolean showMarker) {

        if (impactLocation == null)
            return;

        if (showMarker) {
            drawImpactMarker(
                    g2,
                    map,
                    impactLocation
            );
        }

        if (result != null) {

            drawRadius(
                    g2,
                    map,
                    impactLocation,
                    result.getR_wave(),
                    new Color(40, 160, 60, 40)
            );

            drawRadius(
                    g2,
                    map,
                    impactLocation,
                    result.getR_glass(),
                    new Color(220, 190, 0, 50)
            );

            drawRadius(
                    g2,
                    map,
                    impactLocation,
                    result.getR_heavy(),
                    new Color(220, 80, 0, 70)
            );

            drawRadius(
                    g2,
                    map,
                    impactLocation,
                    result.getR_total(),
                    new Color(180, 0, 0, 100)
            );
        }
    }

    private double metersPerPixel(
            JXMapViewer map,
            GeoPosition impactLocation) {

        double lat = impactLocation.getLatitude();

        int realZoom =
                19 - map.getZoom();

        return 156543.03392
                * Math.cos(Math.toRadians(lat))
                / Math.pow(2, realZoom);
    }

    private void drawRadius(
            Graphics2D g2,
            JXMapViewer map,
            GeoPosition impactLocation,
            double radiusKm,
            Color color) {

        if (radiusKm <= 0)
            return;

        Point2D pt =
                map.getTileFactory().geoToPixel(
                        impactLocation,
                        map.getZoom()
                );

        Rectangle viewport = map.getViewportBounds();

        int centerX =
                (int) (pt.getX() - viewport.getX());

        int centerY =
                (int) (pt.getY() - viewport.getY());

        GeoPosition edge =
                new GeoPosition(
                        impactLocation.getLatitude(),
                        impactLocation.getLongitude()
                                + radiusKm / (111.32 * Math.cos(Math.toRadians(impactLocation.getLatitude())))
                );

        Point2D center =
                map.getTileFactory()
                        .geoToPixel(
                                impactLocation,
                                map.getZoom());

        Point2D edgePoint =
                map.getTileFactory()
                        .geoToPixel(
                                edge,
                                map.getZoom());

        double radiusPixels = center.distance(edgePoint);

        int r = (int) radiusPixels;

        g2.setColor(color);

        g2.fillOval(
                centerX - r,
                centerY - r,
                r * 2,
                r * 2
        );
    }

    private void drawImpactMarker(
            Graphics2D g2,
            JXMapViewer map,
            GeoPosition impactLocation) {

        Point2D pt =
                map.getTileFactory().geoToPixel(
                        impactLocation,
                        map.getZoom()
                );

        Rectangle viewport =
                map.getViewportBounds();

        int x = (int) (pt.getX() - viewport.getX());
        int y = (int) (pt.getY() - viewport.getY());

        g2.setColor(Color.RED);
        g2.fillOval(x - 8, y - 8, 16, 16);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x - 8, y - 8, 16, 16);

        g2.setColor(Color.WHITE);
        g2.drawString(
                String.format(
                        "%.4f, %.4f",
                        impactLocation.getLatitude(),
                        impactLocation.getLongitude()
                ),
                x + 12,
                y - 12
        );
    }
}