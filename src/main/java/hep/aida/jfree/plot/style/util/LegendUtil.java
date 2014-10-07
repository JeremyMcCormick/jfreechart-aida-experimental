package hep.aida.jfree.plot.style.util;

import static hep.aida.jfree.plot.style.converter.StyleConverter.DEFAULT_FILL_COLOR;
import static hep.aida.jfree.plot.style.converter.StyleConverter.DEFAULT_LINE_COLOR;
import static hep.aida.jfree.plot.style.converter.StyleConverter.DEFAULT_SHAPE_COLOR;
import hep.aida.IBaseHistogram;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.plotter.ObjectStyle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.title.LegendTitle;

public class LegendUtil {
    
    private LegendUtil() {
    }

    public static void rebuildChartLegend(JFreeChart chart, List<ObjectStyle> objectStyles) {
        List<LegendItemSource> legendItemSources = new ArrayList<LegendItemSource>(); 
        for (ObjectStyle objectStyle : objectStyles) {
            Object object = objectStyle.object();
            LegendItem legendItem = null;
            if (object instanceof IBaseHistogram) {
                legendItem = createLegendItem((IBaseHistogram)object, objectStyle.style());
            }
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
        if (chart.getLegend() == null) {
            chart.addLegend(new LegendTitle(null));
        }
        chart.getLegend().setSources(legendItemSources.toArray(new LegendItemSource[]{}));
    }
    
    static LegendItem createLegendItem(IBaseHistogram histogram, IPlotterStyle style) {
        LegendItem legendItem = null;
        if (style.legendBoxStyle().isVisible()) {                       
            String label = histogram.title();
            if (label == null) {
                label = histogram.toString();
            }            
            Color color = null;
            if (style.dataStyle().isVisible()) {
                if (style.dataStyle().fillStyle().isVisible()) {
                    // Use the fill color.
                    color = ColorUtil.toColor(style.dataStyle().fillStyle(), DEFAULT_FILL_COLOR);
                } else if (style.dataStyle().lineStyle().isVisible()) {
                    // Use the line color.
                    color = ColorUtil.toColor(style.dataStyle().lineStyle(), DEFAULT_LINE_COLOR);
                } else if (style.dataStyle().outlineStyle().isVisible()) {
                    // Use the outline color (lines between points).
                    color = ColorUtil.toColor(style.dataStyle().outlineStyle(), DEFAULT_LINE_COLOR);
                } else if (style.dataStyle().markerStyle().isVisible()) {
                    // Use the marker or shape color.
                    color = ColorUtil.toColor(style.dataStyle().markerStyle(), DEFAULT_SHAPE_COLOR);
                }                
            }
            // Is there a valid color?
            if (color != null) {
                // Add a legend item for this histogram.
                legendItem = new LegendItem(label, color);
            }
        }          
        return legendItem;
    }        
}
