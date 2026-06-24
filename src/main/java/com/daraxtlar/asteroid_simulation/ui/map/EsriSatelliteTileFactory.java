package com.daraxtlar.asteroid_simulation.ui.map;

import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;

public class EsriSatelliteTileFactory extends DefaultTileFactory {

    public EsriSatelliteTileFactory() {
        super(INFO);

        setThreadPoolSize(8);
    }

    private static final TileFactoryInfo INFO =
            new TileFactoryInfo(
                    1,
                    19,
                    19,
                    256,
                    true,
                    true,
                    "",
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
                            z,
                            y,
                            x
                    );
                }
            };
}
