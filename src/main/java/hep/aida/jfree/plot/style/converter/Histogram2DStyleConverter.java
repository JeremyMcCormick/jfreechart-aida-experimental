package hep.aida.jfree.plot.style.converter;

import hep.aida.IBaseHistogram;
import hep.aida.ILineStyle;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.plot.style.util.ColorUtil;
import hep.aida.jfree.plot.style.util.StrokeUtil;
import hep.aida.jfree.renderer.XYBoxRenderer;

import java.awt.Color;
import java.awt.Stroke;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Histogram2DStyleConverter extends AbstractStyleConverter implements StyleConverter {
    
    void applyStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style) {
        
        // Apply styles to the chart, NOT directly having to do with the data.
        applyNonDataStyle(chart, hist, style);

        if (style.isVisible()) {
            // Apply styles to chart having to do with data visibility and appearance.
            applyDataStyle(chart, hist, style);

        } else {
            makeDataInvisible(chart);
        }
    }
   
    protected void applyDataFillStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style) {
        
        XYItemRenderer renderer = chart.getXYPlot().getRenderer();

        // Set fill style for box plot.
        if (renderer instanceof XYBoxRenderer) {
            if (style.dataStyle().fillStyle().isVisible()) {
                Color color = ColorUtil.toColor(style.dataStyle().fillStyle(), null);
                if (color != null) {
                    ((XYBoxRenderer) renderer).setSeriesFillPaint(0, color);
                }
            } else {
                ((XYBoxRenderer) renderer).setSeriesFillPaint(0, null);
            }
        }
    }

    protected void applyDataLineStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style) {
        ILineStyle lineStyle = style.dataStyle().lineStyle();

        XYPlot plot = chart.getXYPlot();

        // The renderer for the data.
        XYItemRenderer renderer = plot.getRenderer();

        // These styles only apply if the plot is being rendered as boxes and
        // not a color map, in which case they are ignored.
        if (renderer instanceof XYBoxRenderer) {

            // Color of the data lines.
            Color color = ColorUtil.toColor(lineStyle, DEFAULT_LINE_COLOR);
            renderer.setSeriesOutlinePaint(0, color);

            // Stroke of the data lines.
            Stroke stroke = StrokeUtil.toStroke(lineStyle);
            if (stroke != null) {
                renderer.setSeriesStroke(0, stroke);
            }
        }
    }

    /**
     * Turn off data visibility for the given chart.
     * @param chart the JFreeChart object
     */
    protected void makeDataInvisible(JFreeChart chart) {
        chart.getXYPlot().getRenderer().setSeriesVisible(0, false);
        chart.getSubtitle(1).setVisible(false);
    }
}
