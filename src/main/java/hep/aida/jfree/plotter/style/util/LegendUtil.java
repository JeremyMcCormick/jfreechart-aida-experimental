package hep.aida.jfree.plotter.style.util;

import static hep.aida.jfree.plotter.style.util.StyleConstants.DEFAULT_FILL_COLOR;
import hep.aida.IBaseHistogram;
import hep.aida.ICloud1D;
import hep.aida.ICloud2D;
import hep.aida.IFunction;
import hep.aida.IHistogram1D;
import hep.aida.IHistogram2D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.converter.Histogram2DConverter;
import hep.aida.jfree.plotter.ObjectStyle;
import hep.aida.ref.function.BaseModelFunction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.title.LegendTitle;

/**
 * Utility methods for creating plot legend items.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class LegendUtil {

    private LegendUtil() {
    }

    /**
     * Build the legend for the chart given the list of plotted objects.
     * @param chart The chart containing the plots.
     * @param objectStyles The list of plotting objects with their styles.
     */
    public static void rebuildChartLegend(JFreeChart chart, List<ObjectStyle> objectStyles) {

        if (chart == null) {
            throw new IllegalArgumentException("The chart argument is null.");
        }

        //System.out.println("rebuildChartLegend - " + chart.getTitle().getText());

        List<LegendItemSource> legendItemSources = new ArrayList<LegendItemSource>();
        for (ObjectStyle objectStyle : objectStyles) {
            //System.out.println("objectStyle - " + objectStyle.object());

            Object object = objectStyle.object();
            IPlotterStyle style = objectStyle.style();
            LegendItem legendItem = null;
            
            // Get the label.
            String label = getLabel(object);
            
            // Create the legend item.
            legendItem = createLegendItem(object, label, style);

            // Add legend item to the chart if one was created.
            if (legendItem != null) {
                final LegendItemCollection legendItemCollection = new LegendItemCollection();
                legendItemCollection.add(legendItem);
                LegendItemSource legendItemSource = new LegendItemSource() {
                    public LegendItemCollection getLegendItems() {
                        return legendItemCollection;
                    }
                };
                legendItemSources.add(legendItemSource);
            }
        }
        
        // Add legend to chart if at least one item was created.
        if (legendItemSources.size() > 0) {
            if (chart.getLegend() == null) {
                chart.addLegend(new LegendTitle(null));
            }
            chart.getLegend().setSources(legendItemSources.toArray(new LegendItemSource[] {}));
            chart.getLegend().setVisible(true);
        } else {
            chart.getLegend().setVisible(false);
        }
        
        // DEBUG
        //System.out.println();
    }
    
    static String getLabel(Object object) {
        if (object instanceof IBaseHistogram) {
            return ((IBaseHistogram)object).title();                
        } else if (object instanceof IFunction) {
            return ((IFunction)object).title();
        } else {        
            return object.toString();
        }
    }

    /**
     * 
     * @param histogram
     * @param style
     * @return
     */
    static LegendItem createLegendItem(Object object, String label, IPlotterStyle style) {
        if (object instanceof IHistogram1D || object instanceof ICloud1D) {
            return createLegendItem(label, style);
        } else if (object instanceof IHistogram2D) {
            return createHistogram2DLegendItem(label, style);
        } else if (object instanceof ICloud2D) {
            if (!((ICloud2D)object).isConverted()) {
                return createCloud2DLegendItem(label, style);
            } else {
                return createHistogram2DLegendItem(label, style);
            }
        } else if (object instanceof IFunction) {
            return createFunctionLegendItem(label, style);
        } else {
            return null;
        }        
    }

    /**
     * 
     * @param label
     * @param style
     * @return
     */
    static LegendItem createHistogram1DLegendItem(String label, IPlotterStyle style) {
        return createLegendItem(label, style);
    }

    /**
     * 
     * @param label
     * @param style
     * @return
     */
    static LegendItem createHistogram2DLegendItem(String label, IPlotterStyle style) {
        LegendItem legendItem = null;
        if (style.legendBoxStyle().isVisible()) {

            // Get the histogram 2D style setting.
            String hist2DStyle = Histogram2DConverter.getHist2DStyle(style);

            // Only create legend item if color map is not being used.
            if (!hist2DStyle.equals(StyleConstants.COLOR_MAP)) {
               legendItem = createLegendItem(label, style);
            }
        }
        return legendItem;
    }
    
    /**
     * 
     */
    static LegendItem createCloud2DLegendItem(String label, IPlotterStyle style) {
        LegendItem legendItem = null;
        if (style.legendBoxStyle().isVisible()) {

            Color color = ColorUtil.toColor(style.dataStyle().markerStyle(), DEFAULT_FILL_COLOR);
            
            // Is there a valid color?
            if (color != null) {
                //System.out.println("adding new legend item " + label + " with color " + color);                    
                // Add a legend item for this histogram.
                legendItem = new LegendItem(label, color);
            }            
        }
        return legendItem;
    }
    
    /**
     * Default conversion of style info to legend color.
     * This is mostly useful for 1D histograms.
     * @param style The style object.
     * @return The color for the legend from the style info.
     */
    static LegendItem createLegendItem(String label, IPlotterStyle style) {
        Color color = null;
        LegendItem item = null;
        if (style.legendBoxStyle().isVisible()) {
        if (style.dataStyle().isVisible()) {
            if (style.dataStyle().fillStyle().isVisible()) {
                //System.out.println("using fill style");
                // Use the fill color.
                color = ColorUtil.toColor(style.dataStyle().fillStyle(), null);
            }             
            if (color == null && style.dataStyle().lineStyle().isVisible()) {
                //System.out.println("using line style");
                // Use the line color.
                color = ColorUtil.toColor(style.dataStyle().lineStyle(), null);
            }            
            if (color == null && style.dataStyle().outlineStyle().isVisible()) {
                //System.out.println("using outline style");
                // Use the outline color (lines between points).
                color = ColorUtil.toColor(style.dataStyle().outlineStyle(), null);
            }             
            if (color == null && style.dataStyle().markerStyle().isVisible()) {
                //System.out.println("using marker style");
                // Use the marker or shape color.
                color = ColorUtil.toColor(style.dataStyle().markerStyle(), null);
            }
            if (color != null) {
                //System.out.println("adding new legend item " + label + " with color " + color);                    
                // Add a legend item for this histogram.
                item = new LegendItem(label, color);
            }
        }
        }
        return item;
    }      
    
    /**
     * 
     */
    static LegendItem createFunctionLegendItem(String label, IPlotterStyle style) {
        LegendItem legendItem = null;
        if (style.legendBoxStyle().isVisible()) {
            
            Color color = ColorUtil.toColor(style.dataStyle().lineStyle(), StyleConstants.DEFAULT_LINE_COLOR);
            
            // Is there a valid color?
            if (color != null) {
                //System.out.println("adding new legend item " + label + " with color " + color);                    
                // Add a legend item for this histogram.
                legendItem = new LegendItem(label, color);
            }            
        }
        return legendItem;
    }
    
}
