package com.example.asteroid_simulation.ui.map;

import com.example.asteroid_simulation.model.ImpactResult;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class ImpactMapViewer extends JXMapViewer {

    private GeoPosition impactLocation;
    private ImpactResult result;

    public ImpactMapViewer() {

        TileFactoryInfo info = new TileFactoryInfo(
                1,
                19,
                19,
                256,
                true,
                true,
                "https://server.arcgisonline.com",
                "x",
                "y",
                "z"
        ) {
            @Override
            public String getTileUrl(int x, int y, int zoom) {

                int z = getMaximumZoomLevel() - zoom;

                return String.format(
                        "https://server.arcgisonline.com/ArcGIS/rest/services/" +
                                "World_Imagery/MapServer/tile/%d/%d/%d",
                        z, y, x
                );
            }
        };

        DefaultTileFactory tileFactory =
                new DefaultTileFactory(info);

        TileFactoryInfo labelsInfo = new TileFactoryInfo(
                1, 19, 19, 256,
                true, true,
                "",
                "x", "y", "z"
        ) {
            @Override
            public String getTileUrl(int x, int y, int zoom) {

                int z = getMaximumZoomLevel() - zoom;

                return String.format(
                        "https://services.arcgisonline.com/ArcGIS/rest/services/" +
                                "Reference/World_Boundaries_and_Places/MapServer/tile/%d/%d/%d",
                        z, y, x
                );
            }
        };

        DefaultTileFactory labelsFactory =
                new DefaultTileFactory(labelsInfo);

        tileFactory.setThreadPoolSize(8);

        setTileFactory(tileFactory);

        impactLocation =
                new GeoPosition(52.2297, 21.0122);

        setAddressLocation(impactLocation);

        setZoom(6);

        MouseInputListener mia =
                new PanMouseInputListener(this);

        addMouseListener(mia);
        addMouseMotionListener(mia);

        addMouseWheelListener(
                new ZoomMouseWheelListenerCenter(this)
        );

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                impactLocation =
                        pointToGeoPosition(e.getPoint());

                repaint();
            }
        });
    }

    public GeoPosition getImpactLocation() {
        return impactLocation;
    }

    public void setImpactLocation(GeoPosition position) {
        this.impactLocation = position;
        repaint();
    }

    private GeoPosition pointToGeoPosition(Point point) {

        Rectangle viewport =
                getViewportBounds();

        Point2D worldPoint =
                new Point2D.Double(
                        point.getX() + viewport.getX(),
                        point.getY() + viewport.getY()
                );

        return getTileFactory()
                .pixelToGeo(worldPoint, getZoom());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (impactLocation == null)
            return;

        Graphics2D g2 = (Graphics2D) g.create();

        Point2D pt =
                getTileFactory().geoToPixel(
                        impactLocation,
                        getZoom()
                );

        Rectangle viewport =
                getViewportBounds();

        int x = (int) (pt.getX() - viewport.getX());
        int y = (int) (pt.getY() - viewport.getY());

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

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

        if (result != null) {

            drawRadius(
                    g2,
                    result.getR_wave(),
                    new Color(40, 160, 60, 40)
            );

            drawRadius(
                    g2,
                    result.getR_glass(),
                    new Color(220, 190, 0, 50)
            );

            drawRadius(
                    g2,
                    result.getR_heavy(),
                    new Color(220, 80, 0, 70)
            );

            drawRadius(
                    g2,
                    result.getR_total(),
                    new Color(180, 0, 0, 100)
            );

            g2.dispose();
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int W = getWidth(), H = getHeight();

        if (result == null) {
            return;
        }
        drawMap(g2, W, H);
        g2.dispose();
    }


    public void setResult(ImpactResult result) {
        this.result = result;
        repaint();
    }

    private double metersPerPixel() {

        double lat =
                impactLocation.getLatitude();

        return 156543.03392
                * Math.cos(Math.toRadians(lat))
                / Math.pow(2, getZoom());
    }

    private void drawRadius(
            Graphics2D g2,
            double radiusKm,
            Color color) {

        if (radiusKm <= 0)
            return;

        Point2D pt =
                getTileFactory().geoToPixel(
                        impactLocation,
                        getZoom()
                );

        Rectangle viewport =
                getViewportBounds();

        int centerX =
                (int) (pt.getX() - viewport.getX());

        int centerY =
                (int) (pt.getY() - viewport.getY());

        double radiusPixels =
                (radiusKm * 1000.0)
                        / metersPerPixel();

        int r = (int) radiusPixels;

        g2.setColor(color);

        g2.fillOval(
                centerX - r,
                centerY - r,
                r * 2,
                r * 2
        );
    }

    private static void drawTerrain(Graphics2D g, int W, int H, String mapView) {
        // Gradient tła zależny od widoku
        Color c1, c2;
        switch (mapView) {
            case "centrum miasta" -> { c1 = new Color(70, 90, 70); c2 = new Color(50, 70, 55); }
            case "całe miasto"    -> { c1 = new Color(60, 85, 65); c2 = new Color(45, 65, 50); }
            case "województwo"    -> { c1 = new Color(55, 80, 60); c2 = new Color(40, 60, 45); }
            case "kraj"           -> { c1 = new Color(50, 75, 55); c2 = new Color(35, 55, 40); }
            default               -> { c1 = new Color(45, 70, 50); c2 = new Color(30, 50, 35); }
        }
        GradientPaint gp = new GradientPaint(0, 0, c1, W, H, c2);
        g.setPaint(gp);
        g.fillRect(0, 0, W, H);

        // Schematyczne drogi / siatka
        g.setColor(new Color(255, 255, 255, 20));
        g.setStroke(new BasicStroke(1));
        for (int x = 0; x < W; x += W / 8)
            g.drawLine(x, 0, x, H);
        for (int y = 0; y < H; y += H / 8)
            g.drawLine(0, y, W, y);

        // "Rzeka"
        g.setColor(new Color(60, 100, 160, 80));
        g.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int[] rx = {0, W/5, W*2/5, W*3/5, W*4/5, W};
        int[] ry = {H*2/3, H*3/5, H/2+20, H*3/5, H*2/3, H*3/5};
        for (int i = 0; i < rx.length - 1; i++)
            g.drawLine(rx[i], ry[i], rx[i+1], ry[i+1]);
    }

    private static void drawZone(Graphics2D g, int cx, int cy, double r_km, double scale,
                                 Color fill, Color border) {
        int r = (int)(r_km * scale);
        g.setColor(fill);
        g.fillOval(cx - r, cy - r, r*2, r*2);
        g.setColor(border);
        g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                0, new float[]{8, 4}, 0));
        g.drawOval(cx - r, cy - r, r*2, r*2);
        g.setStroke(new BasicStroke(1));
    }

    private static void drawRadiusLabel(Graphics2D g, int cx, int cy,
                                        double r_km, double scale, String text) {
        int r = (int)(r_km * scale);
        g.setFont(new Font("SansSerif", Font.BOLD, 11));
        g.setColor(new Color(255, 255, 255, 200));
        FontMetrics fm = g.getFontMetrics();
        int tw = fm.stringWidth(text);
        // Pozycja: prawo od okręgu, trochę powyżej środka
        int lx = cx + r - tw / 2;
        int ly = cy - r - 4;
        if (ly < 16) ly = cy + r + 14;
        // Tło dla czytelności
        g.setColor(new Color(0, 0, 0, 120));
        g.fillRoundRect(lx - 2, ly - 12, tw + 4, 16, 4, 4);
        g.setColor(Color.WHITE);
        g.drawString(text, lx, ly);
    }

    private void drawScale(Graphics2D g, int W, int H, double scale) {
        // Dobieramy „ładną" odległość dla skali
        double[] niceDistances = {1, 2, 5, 10, 20, 50, 100, 200, 500, 1000};
        double targetPx = 80;
        double niceDist = 10;
        for (double d : niceDistances) {
            if (d * scale >= targetPx) { niceDist = d; break; }
        }
        int scaleLen = (int)(niceDist * scale);
        int sx = 20, sy = H - 30;
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));
        g.drawLine(sx, sy, sx + scaleLen, sy);
        g.drawLine(sx, sy - 4, sx, sy + 4);
        g.drawLine(sx + scaleLen, sy - 4, sx + scaleLen, sy + 4);
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        String label = niceDist < 1 ? (int)(niceDist*1000) + " m"
                : niceDist < 10 ? String.format("%.0f km", niceDist)
                  : (int)niceDist + " km";
        g.drawString(label, sx + scaleLen / 2 - 15, sy - 6);
    }

    private void drawInfoBar(Graphics2D g, int W, int H) {
        if (result == null) return;
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(0, H - 22, W, 22);
        g.setColor(new Color(180, 200, 255));
        g.setFont(new Font("SansSerif", Font.PLAIN, 11));
        String info = String.format("Ø %.0f m | %.0f km/s | %d° | %s | E=%.2f Mt TNT",
                result.diameterM, result.velKms, (int)result.angleDeg,
                result.composition, result.E_MT);
        g.drawString(info, 8, H - 6);
    }

    private static String fmt1(double v) {
        return v < 10 ? String.format("%.1f", v) : String.format("%.0f", v);
    }

    public void drawMap(Graphics2D g, int W, int H) {
        int cx = W / 2, cy = H / 2;
        double maxR_km = result.r_wave;
        double scale   = Math.min(W, H) * 0.44 / maxR_km; // px/km

        // ── Tło mapy (schematyczny teren) ──────────────────────────────
        drawTerrain(g, W, H, result.mapView);

        // ── Strefy zniszczeń (kolorowe okręgi z przezroczystością) ─────
        drawZone(g, cx, cy, result.r_wave,  scale, new Color(40, 160, 60, 70),  new Color(40, 160, 60, 140));
        drawZone(g, cx, cy, result.r_glass, scale, new Color(220, 200, 0, 90),  new Color(220, 190, 0, 160));
        drawZone(g, cx, cy, result.r_heavy, scale, new Color(220, 100, 0, 110), new Color(220, 80, 0, 180));
        drawZone(g, cx, cy, result.r_total, scale, new Color(180, 0, 0, 130),   new Color(180, 0, 0, 200));

        // ── Krater (wypełniony) ─────────────────────────────────────────
        double craterPx = result.craterKm * scale;
        if (craterPx < 4) craterPx = 4;
        g.setColor(new Color(30, 20, 10));
        g.fillOval(cx - (int)craterPx, cy - (int)craterPx,
                (int)(craterPx*2), (int)(craterPx*2));
        // Obramowanie krateru
        g.setColor(new Color(100, 60, 20));
        g.setStroke(new BasicStroke(2));
        g.drawOval(cx - (int)craterPx, cy - (int)craterPx,
                (int)(craterPx*2), (int)(craterPx*2));

        // ── Etykiety promieni ───────────────────────────────────────────
        g.setStroke(new BasicStroke(1));
        drawRadiusLabel(g, cx, cy, result.r_wave,  scale, "🟢 " + fmt1(result.r_wave)  + " km");
        drawRadiusLabel(g, cx, cy, result.r_glass, scale, "🟡 " + fmt1(result.r_glass) + " km");
        drawRadiusLabel(g, cx, cy, result.r_heavy, scale, "🟠 " + fmt1(result.r_heavy) + " km");
        drawRadiusLabel(g, cx, cy, result.r_total, scale, "🔴 " + fmt1(result.r_total) + " km");

        // ── Znacznik miasta ─────────────────────────────────────────────
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 12));
//            g.drawString("★ " + result.city, cx + 10, cy - 12);

        // ── Tytuł mapy ──────────────────────────────────────────────────
        g.setColor(new Color(220, 220, 255));
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        String title = "Widok: " + result.mapView.toUpperCase();
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, (W - fm.stringWidth(title)) / 2, 24);

        // ── Skala ───────────────────────────────────────────────────────
        drawScale(g, W, H, scale);

        // ── Pasek informacyjny na dole ──────────────────────────────────
        drawInfoBar(g, W, H);
    }
}