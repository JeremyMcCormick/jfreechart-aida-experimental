package hep.aida.jfree.plot.style.converter;

import static hep.aida.jfree.dataset.Histogram1DAdapter.ERRORS;
import static hep.aida.jfree.dataset.Histogram1DAdapter.POINTS;
import static hep.aida.jfree.dataset.Histogram1DAdapter.VALUES;
import static hep.aida.jfree.plot.style.util.StyleConstants.DEFAULT_FILL_COLOR;
import static hep.aida.jfree.plot.style.util.StyleConstants.DEFAULT_LINE_COLOR;
import static hep.aida.jfree.plot.style.util.StyleConstants.DEFAULT_SHAPE_COLOR;
import static hep.aida.jfree.plot.style.util.StyleConstants.TRANSPARENT_COLOR;
import hep.aida.IDataStyle;
import hep.aida.IFillStyle;
import hep.aida.ILineStyle;
import hep.aida.IMarkerStyle;
import hep.aida.jfree.plot.style.util.ColorUtil;
import hep.aida.jfree.plot.style.util.MarkerUtil;
import hep.aida.jfree.plot.style.util.StrokeUtil;
import hep.aida.ref.plotter.Style;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram1DStyleConverter extends BaseStyleConverter {

    /**
     * 
     * @param baseChart
     */
    void makeDataInvisible() {
        state.getChart().getXYPlot().getRenderer(VALUES).setSeriesVisible(VALUES, false);
    }

    /**
     * 
     * @param baseChart
     */
    void makeErrorsInvisible() {
        state.getChart().getXYPlot().getRenderer(ERRORS).setSeriesVisible(ERRORS, false);
    }

    /**
     * 
     * @param baseChart
     * @param hist
     * @param style
     */
    void applyDataMarkerStyle() {
        IMarkerStyle markerStyle = state.getPlotterStyle().dataStyle().markerStyle();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) state.getChart().getXYPlot().getRenderer(POINTS);
        if (markerStyle.isVisible()) {
            renderer.setSeriesVisible(POINTS, true);
            Shape shape = MarkerUtil.getMarkerShape(markerStyle.shape(), markerStyle.size());
            renderer.setSeriesShape(POINTS, shape);
            Color color = ColorUtil.toColor(markerStyle, DEFAULT_SHAPE_COLOR);
            renderer.setSeriesPaint(POINTS, color);
        } else {
            renderer.setSeriesShapesVisible(POINTS, false);
        }
    }

    /**
     * 
     * @param baseChart
     * @param hist
     * @param style
     */
    void applyDataOutlineStyle() {
        ILineStyle outlineStyle = state.getPlotterStyle().dataStyle().outlineStyle();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) state.getChart().getXYPlot().getRenderer(POINTS);
        if (outlineStyle.isVisible()) {
            renderer.setSeriesVisible(POINTS, true);
            Color color = ColorUtil.toColor(outlineStyle, DEFAULT_LINE_COLOR);
            renderer.setSeriesPaint(POINTS, color);
            Stroke stroke = StrokeUtil.toStroke(outlineStyle);
            renderer.setSeriesStroke(POINTS, stroke);
        } else {
            renderer.setSeriesLinesVisible(POINTS, false);
        }
    }

    /**
     * 
     * @param baseChart
     * @param hist
     * @param style
     */
    void applyDataLineStyle() {
                
        XYPlot plot = state.getChart().getXYPlot();
        ILineStyle lineStyle = state.getPlotterStyle().dataStyle().lineStyle();
        Color color = ColorUtil.toColor(lineStyle, DEFAULT_LINE_COLOR);
        Stroke stroke = StrokeUtil.toStroke(lineStyle);

        if (lineStyle.isVisible()) {
            // Set the outline color of the bars in the renderer.
            XYItemRenderer barRenderer = plot.getRenderer(VALUES);
            barRenderer.setSeriesVisible(VALUES, true);
            barRenderer.setSeriesOutlineStroke(VALUES, stroke);
            barRenderer.setSeriesOutlinePaint(VALUES, color);
        } else {
            if (!state.getPlotterStyle().dataStyle().fillStyle().isVisible()) {

                // If lines and fill are both turned off, then turn off the bar chart renderer entirely.
                plot.getRenderer(VALUES).setSeriesVisible(VALUES, false);

                // FIXME: Determine if this should actually happen here!           
                // Turn on the step renderer by default.
                // XYItemRenderer stepRenderer = plot.getRenderer(STEPS);
                // stepRenderer.setSeriesVisible(STEPS, true);
                // stepRenderer.setSeriesPaint(STEPS, color);
                // stepRenderer.setSeriesStroke(STEPS, stroke);
            }
        }
    }

    /**
     * 
     * @param baseChart
     * @param hist
     * @param style
     */
    void applyDataFillStyle() {                        
        XYPlot plot = state.getChart().getXYPlot();
        IDataStyle dataStyle = state.getPlotterStyle().dataStyle();
        IFillStyle dataFillStyle = dataStyle.fillStyle();
        XYItemRenderer renderer = plot.getRenderer(VALUES);
        if (dataFillStyle.isVisible()) {
            Color color = ColorUtil.toColor(dataFillStyle, DEFAULT_FILL_COLOR);
            renderer.setSeriesVisible(VALUES, true);
            renderer.setSeriesPaint(VALUES, color);
            renderer.setSeriesOutlinePaint(VALUES, color);
        } else {
            renderer.setSeriesPaint(VALUES, TRANSPARENT_COLOR);
        }
    }

    /**
     * 
     * @param baseChart
     * @param style
     */
    void applyErrorBarStyle() {
                        
        // Get the error renderer.
        XYErrorRenderer renderer = (XYErrorRenderer) state.getChart().getXYPlot().getRenderer(ERRORS);

        // Style for error bars.
        ILineStyle errorStyle = state.getPlotterStyle().dataStyle().errorBarStyle();

        // Set invisible if selected.
        if (errorStyle.isVisible()) {               
            
            // Turn on display of errors in the renderer.
            renderer.setSeriesVisible(ERRORS, true);
            
            // Set the renderer's line color.
            Color errorColor = ColorUtil.toColor(errorStyle, DEFAULT_LINE_COLOR);
            if (errorColor != null) {
                renderer.setSeriesPaint(ERRORS, errorColor);
            }

            // Create the stroke from the error line style.
            Stroke stroke = StrokeUtil.toStroke(errorStyle);

            // Set the stroke on the renderer.
            renderer.setSeriesStroke(ERRORS, stroke);

            //
            // FIXME: Need to make sure cap length is handled correctly here...
            //
            
            // Default cap length.
            // renderer.setCapLength(4.0f);
            // Error bar decoration.
            String decoration = errorStyle.parameterValue(Style.ERRORBAR_DECORATION);
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
            renderer.setSeriesVisible(ERRORS, false);
        }
    }           
}
