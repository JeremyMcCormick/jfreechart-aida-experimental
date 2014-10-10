package hep.aida.jfree.converter;

import hep.aida.IBaseHistogram;
import hep.aida.IDataPointSet;
import hep.aida.IHistogram1D;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class ConverterUtil {
    
    private ConverterUtil() {        
    }
    
    /**
     * Get the axis labels for a histogram if the annotation is set.
     * @param h The histogram.
     * @return The axis labels.
     */
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
    
    /**
     * Get the axis labels for an IDataPointSet if the annotation is set.
     * @param d The IDataPointSet.
     * @return The axis labels.
     */
    public static final String[] getAxisLabels(IDataPointSet d) {
        
        String[] labels = new String[] { "", "" };
        
        // X label.
        if (d.annotation().hasKey("xAxisLabel")) {
            labels[0] = d.annotation().value("xAxisLabel");
        }
        // Y label.
        if (d.annotation().hasKey("yAxisLabel")) {
            labels[1] = d.annotation().value("yAxisLabel");
        }
        return labels;
    }
    
    /**
     * Get all the renderers from a plot.
     * @param plot
     * @return
     */
    static XYItemRenderer[] getRenderers(XYPlot plot) {
        XYItemRenderer[] renderers = new XYItemRenderer[plot.getRendererCount()];
        for (int i = 0; i < plot.getRendererCount(); i++) {
            renderers[i] = plot.getRenderer(i);
        }
        return renderers;
    }
    
    /**
     * Get all the datasets from a plot.
     * @param plot
     * @return
     */
    static XYDataset[] getDatasets(XYPlot plot) {
        XYDataset[] datasets = new XYDataset[plot.getDatasetCount()];
        for (int i = 0; i < plot.getDatasetCount(); i++) {
            datasets[i] = plot.getDataset(i);
        }
        return datasets;
    }
    
    /**
     * Overlay an XYPlot onto an existing chart.
     * @param chart The target JFreeChart.
     * @param plot The plot with the overlay data.
     * @return The indices of the overlaid datasets in the target chart.
     */
    static int[] overlay(JFreeChart chart, XYPlot plot) {                     
        return overlay(chart, getDatasets(plot), getRenderers(plot));        
    }
    
    /**
     * Overlay datasets and renderers onto an existing chart. 
     * @param chart The target chart.
     * @param datasets The datasets to overlay.
     * @param renderers The renderers to overlay.
     * @return The indices of the overlaid datasets in the new chart.
     * @throw IllegalArgumentException If datasets and renderers have different lengths.
     */
    static int[] overlay(JFreeChart chart, XYDataset[] datasets, XYItemRenderer[] renderers) {
        
        if (datasets.length != renderers.length) {
            throw new IllegalArgumentException("The datasets and renderers have different lengths.");
        }
        
        int[] datasetIndices = new int[datasets.length];
        
        // The overlay plot's datasets will be appended to the current chart starting at this index.
        int datasetIndex = chart.getXYPlot().getDatasetCount();

        // Loop over all the datasets within the overlay plot.
        for (int i = 0, n = datasets.length; i < n; i++) {

            // Set the dataset and renderer for the overlay plot in the new chart.
            chart.getXYPlot().setDataset(datasetIndex, datasets[i]);
            chart.getXYPlot().setRenderer(datasetIndex, renderers[i]);
            
            datasetIndices[i] = datasetIndex;
            
            // Increment the index for the next dataset and renderer pair.
            ++datasetIndex;
        }
        
        return datasetIndices;
    }       
}
