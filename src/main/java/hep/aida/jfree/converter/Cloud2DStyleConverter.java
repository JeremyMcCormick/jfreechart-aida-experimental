package hep.aida.jfree.converter;

import hep.aida.IBaseHistogram;
import hep.aida.IDataStyle;
import hep.aida.IFillStyle;
import hep.aida.IMarkerStyle;
import hep.aida.IPlotterStyle;

import java.awt.Color;
import java.awt.Shape;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Cloud2DStyleConverter extends AbstractStyleConverter
{
    protected void applyDataStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style)
    {
        // Check if the plot is visible before continuing.
        if (style.isVisible()) {

            // Set the data styling or turn it off if invisible.
            if (isDataVisible(style)) {

                // data fill style
                applyDataFillStyle(chart, hist, style);

            // Turn off display of histogram data.
            } else {
                makeDataInvisible(chart);
            }

            // Set marker and line styling which may still be visible even if data style is off.
            if (style.isVisible()) {
                applyDataMarkerStyle(chart, hist, style);
            }

        // Turn off data.
        } else {
            makeDataInvisible(chart);
        }
    }
    
    protected void applyDataFillStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style)
    {
        XYPlot plot = chart.getXYPlot();
        IDataStyle dataStyle = style.dataStyle();
        IFillStyle dataFillStyle = dataStyle.fillStyle();

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)plot.getRenderer(0);
        
        if (dataFillStyle.isVisible()) {
            renderer.setSeriesShapesFilled(0, true);
            if (dataFillStyle.color() != null) {
                Color color = DEFAULT_FILL_COLOR;
                try {
                    color = ColorUtil.toColor(dataFillStyle);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                renderer.setSeriesFillPaint(0, color);
            }
        } else {
            renderer.setSeriesShapesFilled(0, false);
        } 
    }
    
    protected void applyDataMarkerStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style)
    {
        IMarkerStyle markerStyle = style.dataStyle().markerStyle();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)chart.getXYPlot().getRenderer(0);
        if (markerStyle.isVisible()) {
            renderer.setSeriesVisible(0, true);
            Shape shape = MarkerUtil.getMarkerShape(markerStyle.shape(), markerStyle.size());
            renderer.setSeriesShape(0, shape); 
            Color color = ColorUtil.toColor(markerStyle, DEFAULT_SHAPE_COLOR);
            renderer.setSeriesPaint(0, color);                
        } else {
            renderer.setSeriesShapesVisible(0, false);
        }
    }
    
    protected void makeDataInvisible(JFreeChart chart)
    {
        chart.getXYPlot().getRenderer(0).setSeriesVisible(0, false);
    }   
}
