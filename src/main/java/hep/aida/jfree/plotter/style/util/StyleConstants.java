package hep.aida.jfree.plotter.style.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

public final class StyleConstants {
    
    private StyleConstants() {        
    }

    public static final Paint TRANSPARENT_COLOR = new Color(0f, 0f, 0f, 0f);
    public static final Color DEFAULT_LINE_COLOR = Color.black;
    public static final Color DEFAULT_SHAPE_COLOR = Color.blue;
    public static final Color DEFAULT_FILL_COLOR = Color.blue;
    public static final Color DEFAULT_GRID_COLOR = Color.gray;
    public static final Stroke DEFAULT_STROKE = new BasicStroke(1.0f);
    
    public static final String COLOR_MAP = "colorMap";
    public static final String BOX_PLOT = "box";
    public static final String ELLIPSE_PLOT = "ellipse";
    public static final String DEFAULT_HIST2DSTYLE = COLOR_MAP;
    public static final String HIST2DSTYLE = "hist2DStyle";
    public static final String COLOR_MAP_SCHEME = "colorMapScheme";
    public static final String START_COLOR = "startColor";
    public static final String END_COLOR = "endColor";
    
    public static final String SHOW_ZERO_HEIGHT_BINS = "showZeroHeightBins";
    
    public static final String SCALE = "scale";
    public static final String LOG = "log";
}
