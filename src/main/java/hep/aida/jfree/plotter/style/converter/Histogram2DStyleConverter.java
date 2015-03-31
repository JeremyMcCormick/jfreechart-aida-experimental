package hep.aida.jfree.plotter.style.converter;

import hep.aida.ILineStyle;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.plotter.style.util.ColorUtil;
import hep.aida.jfree.plotter.style.util.StrokeUtil;
import hep.aida.jfree.plotter.style.util.StyleConstants;
import hep.aida.jfree.renderer.HasPaintScale;

import java.awt.Color;
import java.awt.Stroke;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Histogram2DStyleConverter extends BaseStyleConverter {
    
    public void applyStyle() {

        // Apply styles to the chart, NOT directly having to do with the data.
        applyNonDataStyle();

        if (state.getPlotterStyle().isVisible()) {
            // Apply styles to chart having to do with data visibility and appearance.
            applyDataStyle();

        } else {
            makeDataInvisible();
        }
    }
    
    private String getHist2DStyle() {
        String hist2DStyle = null;
        try {
            hist2DStyle = state.getPlotterStyle().parameterValue(StyleConstants.HIST2DSTYLE);
        } catch (Exception e) {   
            e.printStackTrace();
        }        
        if (hist2DStyle == null)
            hist2DStyle = StyleConstants.DEFAULT_HIST2DSTYLE;
        return hist2DStyle;
    }

    void applyDataFillStyle() {
        
        // Get the renderer for the plot.
        XYItemRenderer renderer = state.getChart().getXYPlot().getRenderer();
        
        // Get the Histogram2D display style.
        String hist2DStyle = getHist2DStyle();

        // Is this a box plot?
        if (hist2DStyle.equals(StyleConstants.BOX_PLOT)) {            
            // Is the fill style visible?
            if (state.getPlotterStyle().dataStyle().fillStyle().isVisible()) {
                Color color = ColorUtil.toColor(state.getPlotterStyle().dataStyle().fillStyle(), StyleConstants.DEFAULT_FILL_COLOR);
                //if (color != null) {
                ((AbstractXYItemRenderer)renderer).setSeriesFillPaint(0, color);
                //}
            } else {
                // The fill will not be visible.
                ((AbstractXYItemRenderer)renderer).setSeriesFillPaint(0, null);
            }
        // Is this a color map?
        } else if (hist2DStyle.equals(StyleConstants.COLOR_MAP)) {                            
            // Set whether bins of height 0 are drawn or if the background will show instead.
            boolean showZeroHeightBins = getShowZeroHeight(state.getPlotterStyle());
            ((HasPaintScale)renderer).getPaintScale().setShowZeroHeightBins(showZeroHeightBins);
        } else {
            // The hist2DStyle parameter setting is invalid or unimplemented.
            throw new RuntimeException("Invalid hist2DStyle setting in PlotterStyle: " + hist2DStyle);
        }
    }

    void applyDataLineStyle() {
               
        ILineStyle lineStyle = state.getPlotterStyle().dataStyle().lineStyle();

        XYPlot plot = state.getChart().getXYPlot();

        // The renderer for the data.
        XYItemRenderer renderer = plot.getRenderer();

        // These styles only apply if the plot is being rendered as boxes and
        // not a color map, in which case they are ignored.
        if (this.getHist2DStyle().equals(StyleConstants.BOX_PLOT)) {
            
            // Color of the data lines.
            Color color = ColorUtil.toColor(lineStyle, StyleConstants.DEFAULT_LINE_COLOR);
            //System.out.println("  color: " + color.toString());
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
     * @param baseChart the JFreeChart object
     */
    void makeDataInvisible() {
        state.getChart().getXYPlot().getRenderer().setSeriesVisible(0, false);
        state.getChart().getSubtitle(1).setVisible(false);
    }
                
    private static boolean getShowZeroHeight(IPlotterStyle style) {
        return Boolean.parseBoolean(style.dataStyle().fillStyle().parameterValue(StyleConstants.SHOW_ZERO_HEIGHT_BINS));
    }
}
