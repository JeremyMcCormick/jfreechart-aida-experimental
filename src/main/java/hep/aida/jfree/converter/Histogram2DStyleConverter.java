package hep.aida.jfree.converter;

import static hep.aida.jfree.converter.Histogram2DConverter.BOX_DATA;
import static hep.aida.jfree.converter.Histogram2DConverter.COLOR_DATA;
import hep.aida.IBaseHistogram;
import hep.aida.IHistogram2D;
import hep.aida.ILineStyle;
import hep.aida.IPlotterStyle;

import java.awt.Color;
import java.awt.Stroke;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYZDataset;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Histogram2DStyleConverter extends AbstractStyleConverter implements StyleConverter 
{    
    
    public void applyStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style)
    {        
        // Apply styles to the chart, NOT directly having to do with data, 
        // e.g. title, background colors, etc.
        applyNonDataStyle(chart, hist, style);
        
        // Apply styles to chart having to do with data visibility and appearance.
        applyDataStyle(chart, hist, style);
        
        // Apply Histogram 2D styles.
        applyHistogram2DStyle(chart, (IHistogram2D)hist, style);
    }

    public void applyHistogram2DStyle(JFreeChart chart, IHistogram2D h2d, IPlotterStyle style) 
    {
        //System.out.println("applyhistogram2DStyle");
        //System.out.println("renderers = " + chart.getXYPlot().getRendererCount());
        //System.out.println("colorMap renderer = " + chart.getXYPlot().getRenderer(COLOR_DATA).getClass().getCanonicalName());
        //System.out.println("box renderer = " + chart.getXYPlot().getRenderer(BOX_DATA).getClass().getCanonicalName());        
        //System.out.println("ds 0 = " + chart.getXYPlot().getDataset(0).getClass().getCanonicalName());        
        //Comparable key = chart.getXYPlot().getDataset(0).getSeriesKey(0);
        //System.out.println("ds 0 key = " + key.toString());        
        //System.out.println("ds 1 = " + chart.getXYPlot().getDataset(1).getClass().getCanonicalName());
        
        //List subtitles = chart.getSubtitles();
        //int n = chart.getSubtitles().size();
        //for (int i=0; i<n; i++) {
        //    System.out.println("subtitle["+i+"] = " + chart.getSubtitle(i).toString());
        //}            
        
        String histStyle = style.parameterValue("hist2DStyle");
        //System.out.println("hist2DStyle = " + histStyle);
        XYPlot plot = chart.getXYPlot();
        if (histStyle != null) {
            if (histStyle.equals("box")) {                                
                plot.getRenderer(BOX_DATA).setSeriesVisible(0, true);
                // FIXME: Setting renderer to null seems to be the only thing that works!
                //        Series visibility set to false is not working correctly.
                plot.setRenderer(COLOR_DATA, null);
                chart.getSubtitle(1).setVisible(false); // Turn scale legend off.
            } else if (histStyle.equals("ellipse")) {
                throw new RuntimeException("The ellipse style is not implemented yet.");
            } else if (histStyle.equals("colorMap")) {
                //System.out.println("colorMap");
                plot.getRenderer(BOX_DATA).setSeriesVisible(0, false);
                // Recreate the renderer for the color map if it was set to null.
                if (plot.getRenderer(COLOR_DATA) == null) {
                    plot.setRenderer(COLOR_DATA, 
                            Histogram2DConverter.createColorMapRenderer(
                                    (XYZDataset)chart.getXYPlot().getDataset(COLOR_DATA), h2d, style));
                }
                plot.getRenderer(COLOR_DATA).setSeriesVisible(0, true);
                chart.getSubtitle(1).setVisible(true); // Turn scale legend back on.
            } else {
                throw new RuntimeException("Unknown hist2DStyle: " + histStyle);
            }
        }
    }
              
    protected void applyDataLineStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style) 
    {
        ILineStyle lineStyle = style.dataStyle().lineStyle();
        
        XYPlot plot = chart.getXYPlot();

        // The box renderer for the data.
        XYItemRenderer renderer = plot.getRenderer(Histogram2DConverter.BOX_DATA);

        // Color of the data lines.
        Color color = DEFAULT_LINE_COLOR;
        try {
            color = ColorUtil.toColor(lineStyle);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        plot.getRenderer(Histogram2DConverter.BOX_DATA).setSeriesOutlinePaint(0, color);

        // Stroke of the data lines.
        Stroke stroke = StrokeUtil.toStroke(lineStyle);
        if (stroke != null) {
            renderer.setSeriesStroke(0, stroke);
        }
    }
    
    /*     
    Color map styles:    
    -warm
    -cool
    -thermal
    -rainbow
    -grayscale
    -userdefined
    */
}
