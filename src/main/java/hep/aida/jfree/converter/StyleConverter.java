package hep.aida.jfree.converter;

import hep.aida.IBaseHistogram;
import hep.aida.IPlotterStyle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import org.jfree.chart.JFreeChart;

/**
 * This is the interface for applying AIDA style settings to a chart.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public interface StyleConverter {

    static final Paint TRANSPARENT = new Color(0f, 0f, 0f, 0f);
    static final Color DEFAULT_LINE_COLOR = Color.black;
    static final Color DEFAULT_SHAPE_COLOR = Color.blue;
    static final Color DEFAULT_FILL_COLOR = Color.blue;
    static final Color DEFAULT_GRID_COLOR = Color.gray;
    static final Stroke DEFAULT_STROKE = new BasicStroke(1.0f);

    void applyStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style);
}
