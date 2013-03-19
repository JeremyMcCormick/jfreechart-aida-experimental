package hep.aida.jfree.plot.style.converter;

import hep.aida.IBaseHistogram;
import hep.aida.IHistogram2D;
import hep.aida.ILineStyle;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.converter.Histogram2DConverter;
import hep.aida.jfree.plot.style.util.ColorUtil;
import hep.aida.jfree.plot.style.util.StrokeUtil;
import hep.aida.jfree.renderer.XYBoxRenderer;

import java.awt.Color;
import java.awt.Stroke;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Histogram2DStyleConverter extends AbstractStyleConverter implements StyleConverter {

    public void applyStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style) {

        // Apply styles to the chart, NOT directly having to do with data,
        // e.g. title, background colors, etc.
        applyNonDataStyle(chart, hist, style);

        if (style.isVisible()) {

            // Apply Histogram 2D styles.
            applyHistogram2DStyle(chart, (IHistogram2D) hist, style);

            // Apply styles to chart having to do with data visibility and
            // appearance.
            applyDataStyle(chart, hist, style);

        } else {
            makeDataInvisible(chart);
        }
    }

    /*
     * TODO:
     * 
     * Color map styles to implement:
     * 
     * -warm -cool -thermal -grayscale -userdefined
     * 
     * e.g.
     * 
     * style.dataStyle().fillStyle().setParameter("colorMapScheme", "rainbow");
     * 
     * Old JFreeChart class PaintScale scale = new GrayPaintScale(0.,
     * h2d.maxBinHeight());
     */

    // TODO: implement style.dataStyle().fillStyle() to fill boxes if selected
    public void applyHistogram2DStyle(JFreeChart chart, IHistogram2D h2d, IPlotterStyle style) {

        String histStyle = style.parameterValue("hist2DStyle");

        if (histStyle != null) {
            if (histStyle.equals("box")) {
                // Replace the existing chart with a box plot.
                Histogram2DConverter.replaceWithBoxPlot(h2d, chart);
            } else if (histStyle.equals("ellipse")) {
                throw new RuntimeException("The ellipse style is not implemented yet.");
            } else if (histStyle.equals("colorMap")) {
                // Replace the existing chart with a color map if the plot is
                // not already displayed as one.
                if (!(chart.getXYPlot().getRenderer() instanceof XYBlockRenderer)) {
                    Histogram2DConverter.replaceWithColorMap(h2d, chart, style);
                }
            } else {
                throw new RuntimeException("Unknown hist2DStyle: " + histStyle);
            }
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
     * 
     * @param chart
     *            The chart.
     */
    protected void makeDataInvisible(JFreeChart chart) {
        chart.getXYPlot().getRenderer().setSeriesVisible(0, false);
        chart.getSubtitle(1).setVisible(false);
    }
}
