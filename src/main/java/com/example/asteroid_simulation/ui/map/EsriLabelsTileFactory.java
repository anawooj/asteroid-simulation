package com.example.asteroid_simulation.ui.map;

import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;

public class EsriLabelsTileFactory extends DefaultTileFactory {

    public EsriLabelsTileFactory() {
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
                            "https://services.arcgisonline.com/ArcGIS/rest/services/" +
                                    "Reference/World_Boundaries_and_Places/MapServer/tile/%d/%d/%d",
                            z,
                            y,
                            x
                    );
                }
            };
}
