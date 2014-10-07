package hep.aida.jfree.plot.style.converter;

import hep.aida.IPlotterStyle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

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
     * Apply style, setting up the current ChartState with the given parameters.
     * @param chart The JFreeChart that represents the plot.
     * @param plotObject The AIDA object that backs the chart.
     * @param style The visual style to apply.
     * @param datasetIndices The applicable indices of the datasets in the chart (may be null).
     */
    void applyStyle(JFreeChart chart, Object plotObject, IPlotterStyle style, int[] datasetIndices);
            
    /**
     * Apply style to panel containing the plot.
     * This is a separate method because the chart's panel may not be immediately available.  
     * @param panel The chart's JPanel.
     */
    void applyStyle(ChartPanel panel);
}