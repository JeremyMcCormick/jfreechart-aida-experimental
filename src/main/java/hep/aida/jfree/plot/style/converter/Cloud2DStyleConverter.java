package hep.aida.jfree.plot.style.converter;

import static hep.aida.jfree.plot.style.util.StyleConstants.DEFAULT_FILL_COLOR;
import static hep.aida.jfree.plot.style.util.StyleConstants.DEFAULT_SHAPE_COLOR;
import hep.aida.IDataStyle;
import hep.aida.IFillStyle;
import hep.aida.IMarkerStyle;
import hep.aida.jfree.plot.style.util.ColorUtil;
import hep.aida.jfree.plot.style.util.MarkerUtil;

import java.awt.Color;
import java.awt.Shape;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Cloud2DStyleConverter extends BaseStyleConverter {

    void applyDataStyle() {
        // Check if the plot is visible before continuing.
        if (state.getPlotterStyle().isVisible()) {

            // Set the data styling or turn it off if invisible.
            if (isDataVisible()) {

                // data fill style
                applyDataFillStyle();

                // Turn off display of histogram data.
            } else {
                makeDataInvisible();
            }

            // Set marker and line styling which may still be visible even if
            // data style is off.
            if (state.getPlotterStyle().isVisible()) {
                applyDataMarkerStyle();
            }

            // Turn off data.
        } else {
            makeDataInvisible();
        }
    }

    void applyDataFillStyle() {
        
        XYPlot plot = state.getChart().getXYPlot();
        IDataStyle dataStyle = state.getPlotterStyle().dataStyle();
        IFillStyle dataFillStyle = dataStyle.fillStyle();

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer(0);

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

    void applyDataMarkerStyle() {
        IMarkerStyle markerStyle = state.getPlotterStyle().dataStyle().markerStyle();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) state.getChart().getXYPlot().getRenderer(0);
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

    void makeDataInvisible() {
        state.getChart().getXYPlot().getRenderer(0).setSeriesVisible(0, false);
    }
}
