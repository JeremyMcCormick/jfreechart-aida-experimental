package hep.aida.jfree.converter;

import hep.aida.IBaseHistogram;
import hep.aida.IPlotterStyle;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.JFreeChart;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public interface StyleConverter
{
    static final Paint TRANSPARENT = new Color(0f, 0f, 0f, 0f);
    static final Color DEFAULT_LINE_COLOR = Color.black;
    static final Color DEFAULT_SHAPE_COLOR = Color.blue;
    static final Color DEFAULT_FILL_COLOR = Color.blue;
    
    void applyStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style);
}
