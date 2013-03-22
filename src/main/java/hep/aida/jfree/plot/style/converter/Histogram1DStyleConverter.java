package hep.aida.jfree.plot.style.converter;

import static hep.aida.ref.plotter.Style.ERRORBAR_DECORATION;
import hep.aida.IBaseHistogram;
import hep.aida.IDataStyle;
import hep.aida.IFillStyle;
import hep.aida.IHistogram1D;
import hep.aida.ILineStyle;
import hep.aida.IMarkerStyle;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.converter.Histogram1DConverter;
import hep.aida.jfree.plot.style.util.ColorUtil;
import hep.aida.jfree.plot.style.util.MarkerUtil;
import hep.aida.jfree.plot.style.util.StrokeUtil;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Histogram1DStyleConverter extends AbstractStyleConverter {

    /**
     * 
     * @param chart
     */
    protected void makeDataInvisible(JFreeChart chart) {
        chart.getXYPlot().getRenderer(Histogram1DConverter.BAR_DATA).setSeriesVisible(0, false);
    }

    /**
     * 
     * @param chart
     */
    protected void makeErrorsInvisible(JFreeChart chart) {
        chart.getXYPlot().getRenderer(Histogram1DConverter.ERROR_DATA).setSeriesVisible(0, false);
    }

    /**
     * 
     * @param chart
     * @param hist
     * @param style
     */
    protected void applyDataMarkerStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style) {
        IMarkerStyle markerStyle = style.dataStyle().markerStyle();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) chart.getXYPlot().getRenderer(Histogram1DConverter.POINT_DATA);
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

    /**
     * 
     * @param chart
     * @param hist
     * @param style
     */
    protected void applyDataOutlineStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style) {
        ILineStyle outlineStyle = style.dataStyle().outlineStyle();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) chart.getXYPlot().getRenderer(Histogram1DConverter.POINT_DATA);
        if (outlineStyle.isVisible()) {
            renderer.setSeriesVisible(0, true);
            Color color = ColorUtil.toColor(outlineStyle, DEFAULT_LINE_COLOR);
            renderer.setSeriesPaint(0, color);
            Stroke stroke = StrokeUtil.toStroke(outlineStyle);
            renderer.setSeriesStroke(0, stroke);
        } else {
            renderer.setSeriesLinesVisible(0, false);
        }
    }

    /**
     * 
     * @param chart
     * @param hist
     * @param style
     */
    protected void applyDataLineStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style) {
        
        XYPlot plot = chart.getXYPlot();
        ILineStyle lineStyle = style.dataStyle().lineStyle();
        Color color = ColorUtil.toColor(lineStyle, DEFAULT_LINE_COLOR);
        Stroke stroke = StrokeUtil.toStroke(lineStyle);
        
        if (hist instanceof IHistogram1D) {
            if (lineStyle.isVisible()) {
                // Set the outline color of the bars.
                XYItemRenderer barRenderer = plot.getRenderer(Histogram1DConverter.BAR_DATA); 
                barRenderer.setSeriesOutlineStroke(0, stroke);
            } else {
                if (!style.dataStyle().fillStyle().isVisible()) {

                    // If lines nor fill are visible, then turn off the bar chart renderer.
                    plot.getRenderer(Histogram1DConverter.BAR_DATA).setSeriesVisible(0, false);

                    // Turn on the step renderer.
                    XYItemRenderer stepRenderer = plot.getRenderer(Histogram1DConverter.STEP_DATA);
                    stepRenderer.setSeriesVisible(0, true);
                    stepRenderer.setSeriesPaint(0, color);
                    stepRenderer.setSeriesStroke(0, stroke);
                }
            }
            //XYItemRenderer renderer = chart.getXYPlot().getRenderer(0);

            // Stroke of the data lines.
            //Stroke stroke = StrokeUtil.toStroke(lineStyle);
            //if (stroke != null)
            //    renderer.setSeriesStroke(0, stroke);
        }
    }

    /**
     * 
     * @param chart
     * @param hist
     * @param style
     */
    protected void applyDataFillStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style) {
        XYPlot plot = chart.getXYPlot();
        IDataStyle dataStyle = style.dataStyle();
        IFillStyle dataFillStyle = dataStyle.fillStyle();

        XYItemRenderer renderer = plot.getRenderer(Histogram1DConverter.BAR_DATA);

        if (dataFillStyle.isVisible()) {
            Color color = ColorUtil.toColor(dataFillStyle, DEFAULT_FILL_COLOR);
            renderer.setSeriesPaint(0, color);
            renderer.setSeriesOutlinePaint(0, color);
        } else {
            renderer.setSeriesPaint(0, TRANSPARENT);
        }
    }

    /**
     * 
     * @param chart
     * @param style
     */
    protected void applyErrorBarStyle(JFreeChart chart, IPlotterStyle style) {
        // Get the error renderer.
        XYErrorRenderer renderer = (XYErrorRenderer) chart.getXYPlot().getRenderer(Histogram1DConverter.ERROR_DATA);

        // It looks like there are no errors defined.
        if (renderer == null) {
            return;
        }

        // Style for error bars.
        ILineStyle errorStyle = style.dataStyle().errorBarStyle();

        // Set invisible if selected.
        if (errorStyle.isVisible()) {

            // Set the line color.
            Color errorColor = ColorUtil.toColor(errorStyle, DEFAULT_LINE_COLOR);
            if (errorColor != null)
                renderer.setSeriesPaint(0, errorColor);

            // Create the stroke from the error line style.
            Stroke stroke = StrokeUtil.toStroke(errorStyle);

            // Set the stroke on the renderer.
            renderer.setSeriesStroke(0, stroke);

            // Default cap length.
            // renderer.setCapLength(4.0f);
            // Error bar decoration.
            String decoration = errorStyle.parameterValue(ERRORBAR_DECORATION);
            if (decoration != null) {
                float capLength = Float.parseFloat(decoration);
                if (capLength == 0.f) {
                    renderer.setCapLength(capLength);
                }
            }
            // } else {
            // renderer.setCapLength(capLength);
            // }
            // }
        } else {
            renderer.setSeriesVisible(0, false);
        }
    }
}
