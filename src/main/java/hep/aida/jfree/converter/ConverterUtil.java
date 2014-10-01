package hep.aida.jfree.converter;

import static hep.aida.jfree.dataset.Histogram1DAdapter.ERRORS;
import static hep.aida.jfree.dataset.Histogram1DAdapter.POINTS;
import static hep.aida.jfree.dataset.Histogram1DAdapter.STEPS;
import static hep.aida.jfree.dataset.Histogram1DAdapter.VALUES;
import hep.aida.IBaseHistogram;
import hep.aida.ICloud1D;
import hep.aida.IHistogram1D;
import hep.aida.jfree.dataset.Cloud1DAdapter;
import hep.aida.jfree.dataset.Histogram1DAdapter;
import hep.aida.jfree.renderer.Histogram1DErrorRenderer;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.xy.XYDataset;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class ConverterUtil {

    static final double DEFAULT_X_AXIS_MARGIN = 0.05;
    static final double DEFAULT_Y_AXIS_MARGIN = 0.25;
    
    public static final String[] getAxisLabels(IBaseHistogram h) {
        
        String[] labels = new String[] { "", "" };
        
        // X label.
        if (h.annotation().hasKey("xAxisLabel")) {
            labels[0] = h.annotation().value("xAxisLabel");
        }
        // Y label.
        if (h.annotation().hasKey("yAxisLabel")) {
            labels[1] = h.annotation().value("yAxisLabel");
        }
        return labels;
    }
    
    static XYItemRenderer[] createHistogramRenderers(XYDataset[] datasets) {
        
        // Create array for the renderers.
        XYItemRenderer[] renderers = new XYItemRenderer[datasets.length];
        
        // Add bar renderer which draws bar chart.
        XYBarRenderer barRenderer = new XYBarRenderer();
        barRenderer.setDrawBarOutline(true);
        renderers[VALUES] = barRenderer;

        // Add error renderer that draws error bars and caps.
        XYErrorRenderer errorRenderer = new Histogram1DErrorRenderer();
        errorRenderer.setBaseShapesVisible(false);
        errorRenderer.setSeriesPaint(ERRORS, Color.black);
        renderers[ERRORS] = errorRenderer;
        
        // Add step renderer which draws the histogram step contour.
        XYItemRenderer stepRenderer = new XYStepRenderer();
        renderers[STEPS] = stepRenderer;
        
        // Add line and shape renderer for lines between points and markers.
        XYLineAndShapeRenderer pointRenderer = new XYLineAndShapeRenderer();
        renderers[POINTS] = pointRenderer;
        
        // Turn off the renderering of all series.  
        // These will be activated later in the style code.
        for (int i=0; i<=3; i++) {
            for (int j=0; j<=3; j++) {
                renderers[i].setSeriesVisible(j, false);
            }
        }
        
        return renderers;
    }
    
    static JFreeChart createHistogramChart(String title, NumberAxis xAxis, NumberAxis yAxis, 
            XYDataset[] datasets, XYItemRenderer[] renderers) {
        
        if (datasets.length != renderers.length) {
            throw new IllegalArgumentException("The datasets and renderers must be the same size.");
        }
        
        // Create the plot without any data.
        XYPlot plot = new XYPlot(null, xAxis, yAxis, null);

        // Add datasets and their associated renderers to the plot.
        for (int i = 0, n = datasets.length; i < n; i++) {
            plot.setDataset(i, datasets[i]);
            plot.setRenderer(i, renderers[i]);
        }

        // Set proper rendering order.
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        // Create the chart.
        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, false);

        // Apply the default theme.
        ChartFactory.getChartTheme().apply(chart);
        
        return chart;
    }
    
    static XYDataset[] createDatasets(ICloud1D cloud) {
        XYDataset[] datasets = new XYDataset[4];
        Cloud1DAdapter adapter = new Cloud1DAdapter(cloud);
        for (int i=0; i<=3; i++) {
            datasets[i] = adapter;
        }
        return datasets;
    }
    
    static XYDataset[] createDatasets(IHistogram1D h1d) {        
        XYDataset[] datasets = new XYDataset[4];                             
        Histogram1DAdapter adapter = new Histogram1DAdapter(h1d);
        for (int i=0; i<=3; i++) {
            datasets[i] = adapter;
        }
        return datasets;
    }       
}
