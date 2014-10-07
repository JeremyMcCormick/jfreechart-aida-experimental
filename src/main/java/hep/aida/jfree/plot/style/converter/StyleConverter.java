package hep.aida.jfree.plot.style.converter;

import hep.aida.IPlotterStyle;
import hep.aida.jfree.plotter.ChartState;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * This is the interface for applying AIDA style settings to a chart.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public interface StyleConverter {

    public static final Paint TRANSPARENT = new Color(0f, 0f, 0f, 0f);
    public static final Color DEFAULT_LINE_COLOR = Color.black;
    public static final Color DEFAULT_SHAPE_COLOR = Color.blue;
    public static final Color DEFAULT_FILL_COLOR = Color.blue;
    public static final Color DEFAULT_GRID_COLOR = Color.gray;
    public static final Stroke DEFAULT_STROKE = new BasicStroke(1.0f);

    /**
     * Apply styles to current chart state.
     */
    void applyStyle();
    
    /**
     * Set the chart state to which styles will be applied.
     * @param state The chart state.
     */
    void setChartState(ChartState state);
    
    void setStyle(IPlotterStyle style);
}