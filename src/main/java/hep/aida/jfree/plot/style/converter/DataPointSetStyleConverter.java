package hep.aida.jfree.plot.style.converter;

import static hep.aida.jfree.dataset.DataPointSetAdapter.ERRORS;
import static hep.aida.jfree.dataset.DataPointSetAdapter.VALUES;
import static hep.aida.jfree.dataset.DataPointSetAdapter.LINES;
import static hep.aida.jfree.plot.style.util.StyleConstants.DEFAULT_LINE_COLOR;
import static hep.aida.jfree.plot.style.util.StyleConstants.DEFAULT_SHAPE_COLOR;
import hep.aida.IDataPointSet;
import hep.aida.ILineStyle;
import hep.aida.IMarkerStyle;
import hep.aida.jfree.plot.style.util.ColorUtil;
import hep.aida.jfree.plot.style.util.MarkerUtil;
import hep.aida.jfree.plot.style.util.StrokeUtil;
import hep.aida.ref.plotter.Style;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;

import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;


public class DataPointSetStyleConverter extends BaseStyleConverter {
        
    void applyDataStyle() {
        // Is plot visible?
        if (state.getPlotterStyle().isVisible()) {
                       
            // Set the data styling or turn it off if invisible.
            if (isDataVisible()) {
                
                // Apply marker style which draws markers such as circles at data points.                
                applyDataMarkerStyle();
                
                // Apply data outline style which draws lines between data points.
                applyDataOutlineStyle();                
                                
                // Are errors visible?
                if (areErrorsVisible()) {
                    // Apply error bar style, which will only show if the data itself is set to visible.
                    applyErrorBarStyle();
                } else {
                    // Turn off display of error values.
                    makeErrorsInvisible();
                }                                               
            } else {
                
                // Turn off display of histogram data.
                makeDataInvisible();
                
                // Turn off display of errors.
                makeErrorsInvisible();
            }                   
        } 
    }
    
    void applyDataMarkerStyle() {
        IMarkerStyle markerStyle = state.getPlotterStyle().dataStyle().markerStyle();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) state.getChart().getXYPlot().getRenderer(VALUES);
        if (markerStyle.isVisible()) {
            renderer.setSeriesVisible(VALUES, true);
            Shape shape = MarkerUtil.getMarkerShape(markerStyle.shape(), markerStyle.size());
            renderer.setSeriesShape(VALUES, shape);
            Color color = ColorUtil.toColor(markerStyle, DEFAULT_SHAPE_COLOR);
            renderer.setSeriesPaint(VALUES, color);
        } else {
            renderer.setSeriesShapesVisible(VALUES, false);
        }
    }
    
    void applyDataOutlineStyle() {
        ILineStyle outlineStyle = state.getPlotterStyle().dataStyle().outlineStyle();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) state.getChart().getXYPlot().getRenderer(LINES);
        if (outlineStyle.isVisible()) {
            renderer.setSeriesVisible(LINES, true);
            Color color = ColorUtil.toColor(outlineStyle, DEFAULT_LINE_COLOR);
            renderer.setSeriesPaint(LINES, color);
            Stroke stroke = StrokeUtil.toStroke(outlineStyle);
            renderer.setSeriesStroke(LINES, stroke);
        } else {
            renderer.setSeriesLinesVisible(LINES, false);
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
            
            // Error bar decoration.
            String decoration = errorStyle.parameterValue(Style.ERRORBAR_DECORATION);
            if (decoration != null) {
                float capLength = Float.parseFloat(decoration);
                if (capLength == 0.f) {
                    renderer.setCapLength(capLength);
                }
            }
        } else {
            renderer.setSeriesVisible(ERRORS, false);
        }
    }
    
    void applyAxisLabels() {
        if (((IDataPointSet)state.getPlotObject()).annotation().hasKey("xAxisLabel")) {
            state.getChart().getXYPlot().getDomainAxis().setLabel(state.getHistogram().annotation().value("xAxisLabel"));
        }
        if (((IDataPointSet)state.getPlotObject()).annotation().hasKey("yAxisLabel")) {
            state.getChart().getXYPlot().getRangeAxis().setLabel(state.getHistogram().annotation().value("yAxisLabel"));
        }
    }    
}
