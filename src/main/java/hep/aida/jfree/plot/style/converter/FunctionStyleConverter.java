package hep.aida.jfree.plot.style.converter;

import hep.aida.ILineStyle;
import hep.aida.IMarkerStyle;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.plot.style.util.ColorUtil;
import hep.aida.jfree.plot.style.util.MarkerUtil;
import hep.aida.jfree.plot.style.util.StrokeUtil;

import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;

import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
// FIXME: The shape color is always the same as the line.  This is probably because there is one paint object.
// This can be fixed by adding 2 series in the adapter, one for the line and the other for the shapes.
public class FunctionStyleConverter extends AbstractStyleConverter {
    
    public void applyStyle() {
        IPlotterStyle style = state.getPlotterStyle();
        int datasetIndex = state.getDatasetIndices()[0];
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)state.getChart().getXYPlot().getRenderer(datasetIndex);
        if (style.isVisible()) { 
            // Apply line style.
            ILineStyle lineStyle = style.dataStyle().lineStyle();
            if (lineStyle.isVisible()) {
                Color color = ColorUtil.toColor(lineStyle, DEFAULT_LINE_COLOR);
                Stroke stroke = StrokeUtil.toStroke(lineStyle);            
                renderer.setSeriesPaint(0, color);
                renderer.setSeriesStroke(0, stroke);
            } else {
                renderer.setSeriesLinesVisible(0, false);
            }

            // Apply marker style.
            IMarkerStyle markerStyle = style.dataStyle().markerStyle();
            if (markerStyle.isVisible()) {
                Shape markerShape = MarkerUtil.getShape(markerStyle);
                renderer.setSeriesShape(0, markerShape);
            } else {
                renderer.setSeriesShapesVisible(0, false);
            }

        } else{
            renderer.setSeriesVisible(datasetIndex, false);
        }
    }       
}
