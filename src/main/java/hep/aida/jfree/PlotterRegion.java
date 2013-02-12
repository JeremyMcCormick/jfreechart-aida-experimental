package hep.aida.jfree;

import hep.aida.IBaseHistogram;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.converter.Factory;
import hep.aida.jfree.converter.Histogram;
import hep.aida.jfree.converter.Style;
import hep.aida.ref.event.IsObservable;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;

/**
 * This class implements the plotting of AIDA data objects using a JFreeChart backend.
 * Most of the complex logic for converting from the internal AIDA data representation
 * to JFreeChart classes is contained in converter classes in another package that
 * are called from here.  Each region has a JPanel associated with it, as well as a
 * single JFreeChart object onto which data is plotted.  Histograms will be automatically
 * overlayed onto each other by appending data sets onto the chart's XYPlot.  Each 
 * histogram added onto the region is assigned a unique listener so that updates to
 * it, e.g. calls to the fill method, will result in the displayed plot being updated on 
 * the fly.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public final class PlotterRegion extends DummyPlotterRegion {
    
    JPanel panel;    
    JFreeChart chart;
    double x, y, w, h;
    
    /**
     * Create a new plotter region with JFree backend.
     * @param x The position as percentage in X.
     * @param y The position as percentage in Y.
     * @param w The width as percentage.
     * @param h The heigh as percentage.
     */
    PlotterRegion(double x, double y, double w, double h) {
        super(x, y, w, h);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    public JPanel getPanel() {
        return panel;
    }
    
    public JFreeChart getChart() {
        return chart;
    }
    
    double x() {
        return x;
    }
    
    public double y() {
        return y;
    }
    
    public double height() {
        return h;
    }
    
    public double width() {
        return w;
    }
    
    public void plot(IBaseHistogram hist) {
        plot(hist, this.style());
    }
        
    public void plot(IBaseHistogram hist, IPlotterStyle style) {
        
        // Find the appropriate converter for this AIDA data type.
        Histogram converter = Factory.instance().getConverter(hist);

        // The plot will only show if there is a converter registered.
        if (converter != null) {
            
            // Create a new chart.
            JFreeChart newChart = createChart(hist, style, converter);
            
            // This is the first plot onto the region.
            if (chart == null) {                
                setBaseChart(hist, newChart);
            // Overlay onto an existing chart.
            } else {                              
                overlay(hist, newChart.getXYPlot());
            }
        // Cannot display this type of plot.
        } else {
            System.err.println("WARNING: Histgram type " + hist.getClass().getCanonicalName() + " is not supported.");
        }
    }

    private void setBaseChart(IBaseHistogram hist, JFreeChart newChart)
    {
        // The new chart becomes the base chart for this region.
        chart = newChart;
                        
        // Create the JPanel for the region.
        panel = new ChartPanel(chart);

        // Add a listener to get callbacks when the underlying histogram is updated.
        int[] datasetIndices = getDatasetIndices(chart.getXYPlot(), 0);
        addListener(hist, datasetIndices);
    }

    private JFreeChart createChart(IBaseHistogram hist, IPlotterStyle style, Histogram converter)
    {
        // Create a new chart.
        JFreeChart newChart = converter.convert(hist, style); 
        
        // Apply AIDA styles to the chart.
        if (style != null) {
            Style.applyStyle(newChart, hist, style);
        }
        return newChart;
    }

    private void addListener(IBaseHistogram hist, int[] datasetIndices)
    {
        ((IsObservable) hist).addListener(PlotListenerFactory.createListener(hist, chart, datasetIndices));
    }
    
    private int[] getDatasetIndices(XYPlot plot, int offset) {
        int[] datasetIndices = new int[plot.getDatasetCount()];
        for (int i=0, n=plot.getDatasetCount(); i<n; i++) {
            datasetIndices[i] = i + offset; 
        }
        return datasetIndices;
    }
    
    private void overlay(IBaseHistogram hist, XYPlot overlayPlot)
    {
        // This is the array of dataset indices for the listener.
        int[] datasetIndices = getDatasetIndices(overlayPlot, chart.getXYPlot().getDatasetCount());
        
        // Get the current count of datasets in the existing chart for the region.  The overlay plot's
        // data will be appended to the current chart starting at this index.
        int datasetIndex = chart.getXYPlot().getDatasetCount();
        
        // Loop over the datasets for the overlay plot.
        for (int i=0, n=overlayPlot.getDatasetCount(); i<n; i++) {
            
            // Set the dataset and its renderer in the region's chart, copying from the overlay plot.
            chart.getXYPlot().setDataset(datasetIndex, overlayPlot.getDataset(i));
            chart.getXYPlot().setRenderer(datasetIndex, overlayPlot.getRenderer(i));
            
            // Increment the index for next dataset and renderer in the overlay plot.
            ++datasetIndex;
        }
        
        // Add a listener which will handle updates to the overlayed histogram.
        addListener(hist, datasetIndices);        
    }           
}