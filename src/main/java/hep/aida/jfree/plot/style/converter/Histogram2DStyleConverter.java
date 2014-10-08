package hep.aida.jfree.plot.style.converter;

import hep.aida.ILineStyle;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.plot.style.util.ColorUtil;
import hep.aida.jfree.plot.style.util.StrokeUtil;
import hep.aida.jfree.renderer.AbstractPaintScale;
import hep.aida.jfree.renderer.XYBoxRenderer;
import hep.aida.jfree.renderer.XYVariableBinWidthBoxRenderer;

import java.awt.Color;
import java.awt.Stroke;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Histogram2DStyleConverter extends AbstractStyleConverter implements StyleConverter {
    
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

    void applyDataFillStyle() {
        
        XYItemRenderer renderer = state.getChart().getXYPlot().getRenderer();
        
        // Set the default Paint to the chart background.
        //System.out.println("setting base paint: " + chart.getBackgroundPaint().toString());
        //renderer.setBasePaint(chart.getBackgroundPaint());

        // Set data fill style for box plot.
        if (isBoxPlotRenderer(renderer)) {            
            if (state.getPlotterStyle().dataStyle().fillStyle().isVisible()) {
                Color color = ColorUtil.toColor(state.getPlotterStyle().dataStyle().fillStyle(), null);
                if (color != null) {
                    getBoxPlotRenderer(renderer).setSeriesFillPaint(0, color);
                }
            } else {
                getBoxPlotRenderer(renderer).setSeriesFillPaint(0, null);
            }
        // Set data fill style for color map.
        } else if (isColorMapRenderer(renderer)) {     
            
            // By default zero height bins are drawn using the lowest color value.
            boolean showZeroHeightBins = true;
            
            try {
                // Set whether bins of height 0 are not drawn so that the background show
                // or whether they use the lowest color value.
                showZeroHeightBins = getShowZeroHeightBinsSetting(state.getPlotterStyle());
                getAbstractPaintScale(renderer).setShowZeroHeightBins(showZeroHeightBins);                
            } catch (Exception e) {
                // Not sure what Exceptions are actually thrown from the try block 
                // but catch everything here just in case.
                e.printStackTrace(System.err);
            }                        
        }
    }

    void applyDataLineStyle() {
        
        //System.out.println("Histogram2DStyleConverter.applyDataLineStyle");
        
        ILineStyle lineStyle = state.getPlotterStyle().dataStyle().lineStyle();

        XYPlot plot = state.getChart().getXYPlot();

        // The renderer for the data.
        XYItemRenderer renderer = plot.getRenderer();

        // These styles only apply if the plot is being rendered as boxes and
        // not a color map, in which case they are ignored.
        if (isBoxPlotRenderer(renderer)) {
            
            //System.out.println("  isBoxPlotRenderer");

            // Color of the data lines.
            Color color = ColorUtil.toColor(lineStyle, DEFAULT_LINE_COLOR);
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
     * @param chart the JFreeChart object
     */
    void makeDataInvisible() {
        state.getChart().getXYPlot().getRenderer().setSeriesVisible(0, false);
        state.getChart().getSubtitle(1).setVisible(false);
    }
    
    private static boolean isColorMapRenderer(XYItemRenderer renderer) {
        return renderer instanceof XYBlockRenderer;
    }
    
    private static boolean isBoxPlotRenderer(XYItemRenderer renderer) {
        return renderer instanceof XYVariableBinWidthBoxRenderer || renderer instanceof XYBoxRenderer;
    }
    
    private static XYBlockRenderer getColorMapRenderer(XYItemRenderer renderer) {
        return (XYBlockRenderer)renderer;
    }
    
    private static XYBoxRenderer getBoxPlotRenderer(XYItemRenderer renderer) {
        return (XYBoxRenderer)renderer;
    }
    
    private static AbstractPaintScale getAbstractPaintScale(XYItemRenderer renderer) {
        if (!isColorMapRenderer(renderer)) {
            throw new IllegalArgumentException("The renderer has the wrong type.");
        }
        return (AbstractPaintScale)getColorMapRenderer(renderer).getPaintScale();
    }   
    
    private static boolean getShowZeroHeightBinsSetting(IPlotterStyle style) {
        return Boolean.parseBoolean(style.dataStyle().fillStyle().parameterValue("showZeroHeightBins"));
    }
}
