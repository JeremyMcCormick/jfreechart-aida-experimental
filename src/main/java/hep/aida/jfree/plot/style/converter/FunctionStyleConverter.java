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
public class FunctionStyleConverter extends AbstractStyleConverter {
    public void applyStyle() {
        IPlotterStyle style = getStyle();
        int datasetIndex = this.getChartState().getDatasetIndex();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)this.getChartState().plot().getRenderer(datasetIndex);
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
