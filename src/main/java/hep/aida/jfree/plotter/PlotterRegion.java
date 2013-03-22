package hep.aida.jfree.plotter;

import hep.aida.IBaseHistogram;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.converter.HistogramConverter;
import hep.aida.jfree.converter.HistogramConverterFactory;
import hep.aida.jfree.plot.listener.PlotListener;
import hep.aida.jfree.plot.listener.PlotListenerFactory;
import hep.aida.jfree.plot.style.converter.AbstractStyleConverter;
import hep.aida.jfree.plot.style.converter.StyleConverterFactory;
import hep.aida.ref.event.IsObservable;
import hep.aida.ref.plotter.DummyPlotterRegion;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;

/**
 * This class implements the plotting of AIDA data objects using a JFreeChart
 * backend. Most of the complex logic for converting from the internal AIDA data
 * representation to JFreeChart classes is contained in converter classes in
 * another package that are called from here. Each region has a ChartPanel
 * associated with it, as well as a single JFreeChart object onto which data is
 * plotted. Histograms overlay is handled by appending data sets onto the chart's XYPlot. 
 * Each histogram added onto the region is assigned a unique listener so that updates to it, 
 * e.g. calls to the fill method, will result in the displayed plot being updated on the fly.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class PlotterRegion extends DummyPlotterRegion {

    double x, y, w, h;
    IPlotterStyle style;
    
    ChartPanel panel;
    JFreeChart chart;
    AbstractStyleConverter styleConverter;
    List<PlotListener> listeners = new ArrayList<PlotListener>();

    /**
     * Create a new plotter region.
     * 
     * @param x The position as percentage in X.
     * @param y The position as percentage in Y.
     * @param w The width as percentage.
     * @param h The height as percentage.
     */
    PlotterRegion(IPlotterStyle style, double x, double y, double w, double h) {
        this.style = style;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    public IPlotterStyle style() {
        return style;
    }
    
    public void setStyle(IPlotterStyle style) {
        this.style = style;
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
    
    public JPanel getPanel() {
        return panel;
    }
    
    public XYPlot getPlot() {
        return chart.getXYPlot();
    }
    
    public JFreeChart getChart() {
        return chart;
    }

    public void plot(IBaseHistogram hist) {
        plot(hist, this.style());
    }

    public void plot(IBaseHistogram hist, IPlotterStyle style) {
        // System.out.println("PlotterRegion.plot - " + hist.title());

        // Find the appropriate converter for this AIDA data type.
        HistogramConverter converter = HistogramConverterFactory.instance().getConverter(hist);
        
        // The plot will only show if there is a converter registered.
        if (converter != null) {
            // Create a new chart.
            JFreeChart newChart = createChart(hist, style, converter);
            if (chart == null) {
                // This is the first plot onto the region.
                setBaseChart(hist, newChart);            
            } else {
                // Overlay onto an existing chart.
                overlay(hist, newChart.getXYPlot());
            }
        } else {
            // Cannot display this type of plot.
            System.err.println("WARNING: Histogram type " + hist.getClass().getCanonicalName() + " is not supported yet.");
        }
    }

    private void setBaseChart(IBaseHistogram hist, JFreeChart newChart) {
        
        // The new chart becomes the base chart for this region.
        chart = newChart;

        // Create the JPanel for the region.
        panel = new ChartPanel(chart);
        
        // Apply region styles to the new panel.  Only the base chart has its own JPanel.
        if (styleConverter != null) {
            styleConverter.getChartState().setPanel(panel);
            styleConverter.applyPanelStyle();
        }

        // Add a listener for receiving callbacks when the underlying histogram is updated,
        // so that it can be redrawn.
        int[] datasetIndices = getDatasetIndices(chart.getXYPlot(), 0);
        addListener(hist, datasetIndices);
    }

    private JFreeChart createChart(IBaseHistogram hist, IPlotterStyle style, HistogramConverter converter) {

        // Create a new chart.
        JFreeChart newChart = converter.convert(hist, style);

        // Apply AIDA styles to the chart.
        if (style != null) {
            styleConverter = (AbstractStyleConverter)StyleConverterFactory.getStyleConverter(hist);
            if (styleConverter != null) {
                ChartState state = new ChartState(null, newChart, hist);
                styleConverter.setChartState(state);
                styleConverter.setStyle(style);
                styleConverter.applyStyle();
            } else {
                System.err.println("WARNING: No style converter found for " + hist.title() + " with type " + hist.getClass().getCanonicalName());
            }
        } else {
            System.out.println("WARNING: The style object points to null!");
        }
        return newChart;
    }

    private void addListener(IBaseHistogram hist, int[] datasetIndices) {
        PlotListener listener = PlotListenerFactory.createListener(hist, chart, datasetIndices);
        if (listener != null) {
            ((IsObservable) hist).addListener(listener);
            this.listeners.add(listener);
        } else
            System.out.println("WARNING: No listener defined for plot " + hist.title() + " with type " + hist.getClass().getCanonicalName());
    }

    private int[] getDatasetIndices(XYPlot plot, int offset) {
        int[] datasetIndices = new int[plot.getDatasetCount()];
        for (int i = 0, n = plot.getDatasetCount(); i < n; i++) {
            datasetIndices[i] = i + offset;
        }
        return datasetIndices;
    }

    private void overlay(IBaseHistogram hist, XYPlot overlayPlot) {
        // This is the array of dataset indices for the listener.
        int[] datasetIndices = getDatasetIndices(overlayPlot, chart.getXYPlot().getDatasetCount());

        // Get the current count of datasets in the existing chart for the
        // region. The overlay plot's data will be appended to the current chart 
        // starting at this index.
        int datasetIndex = chart.getXYPlot().getDatasetCount();

        // Loop over the datasets for the overlay plot.
        for (int i = 0, n = overlayPlot.getDatasetCount(); i < n; i++) {

            // Set the dataset and renderer in the chart for the overlay.
            chart.getXYPlot().setDataset(datasetIndex, overlayPlot.getDataset(i));
            chart.getXYPlot().setRenderer(datasetIndex, overlayPlot.getRenderer(i));

            // Increment the index for next dataset and renderer in the overlay plot.
            ++datasetIndex;
        }

        // Add a listener which will handle updates to the overlay histogram.
        addListener(hist, datasetIndices);
    }

    public void setTitle(String title) {
        chart.setTitle(title);
    }
    
    public synchronized void update() {
        for (PlotListener listener : listeners) {
            listener.update();
        }
    }
}