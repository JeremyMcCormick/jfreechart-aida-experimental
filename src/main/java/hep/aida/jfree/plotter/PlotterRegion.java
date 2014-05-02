package hep.aida.jfree.plotter;

import hep.aida.IBaseHistogram;
import hep.aida.IFunction;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.converter.Converter;
import hep.aida.jfree.converter.FunctionConverter;
import hep.aida.jfree.converter.HistogramConverterFactory;
import hep.aida.jfree.plot.listener.FunctionListener;
import hep.aida.jfree.plot.listener.PlotListener;
import hep.aida.jfree.plot.listener.PlotListenerFactory;
import hep.aida.jfree.plot.style.converter.AbstractStyleConverter;
import hep.aida.jfree.plot.style.converter.StyleConverter;
import hep.aida.jfree.plot.style.converter.StyleConverterFactory;
import hep.aida.ref.event.IsObservable;
import hep.aida.ref.plotter.DummyPlotterRegion;
import jas.util.layout.PercentLayout;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;

/**
 * This class implements the plotting of AIDA data objects using a JFreeChart backend. Most of the
 * complex logic for converting from the internal AIDA data representation to JFreeChart classes is
 * contained in converter classes in the {@link hep.aida.jfree.converter} package that are called
 * from here. Each region has a <code>ChartPanel</code> associated with it, as well as a single
 * JFreeChart object onto which data is plotted. Histogram overlay is handled by appending data
 * sets onto the chart's XYPlot object. Each histogram added onto the region is assigned a unique
 * listener so that updates to it, e.g. calls to the fill method, will result in the displayed plot
 * being updated on the fly.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
// FIXME: The region should have a default JFreeChart object instead of requiring it to be set externally.
public class PlotterRegion extends DummyPlotterRegion {

    double x, y, w, h;
    IPlotterStyle style;

    ChartPanel chartPanel;
    JFreeChart chart;
    AbstractStyleConverter styleConverter;
    List<PlotListener<?>> listeners = new ArrayList<PlotListener<?>>();

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

    /**
     * Return the <tt>IPlotterStyle<tt> of this region.
     * @return The <tt>IPlotterStyle</tt> of this region.
     */
    public IPlotterStyle style() {
        return style;
    }

    /**
     * Set the <tt>IPlotterStyle</tt> of this region.
     * @param The <tt>IPlotterStyle</tt> of this region.
     */
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
        return chartPanel;
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
        
        // Find the appropriate converter for this AIDA data type.
        Converter<?> converter = HistogramConverterFactory.instance().getConverter(hist);

        // The plot will only show if there is a converter registered.
        if (converter != null) {
            // Create a new chart
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

    public void plot(IFunction function) {
        plot(function, this.style, null);
    }

    public void plot(IFunction function, IPlotterStyle style) {
        plot(function, style, null);
    }

    public void plot(IFunction function, IPlotterStyle style, String options) {

        // Add function to JFreeChart object.
        FunctionConverter.addFunction(chart, function);

        // Apply styles to function.
        StyleConverter styleConverter = StyleConverterFactory.getStyleConverter(function);
        styleConverter.setStyle(style);
        styleConverter.setChartState(new ChartState(chartPanel, chart, function, chart.getXYPlot().getDatasetCount() - 1));
        styleConverter.applyStyle();

        // FIXME: Add this back once figure out how to connect with object it is fitting.
        //addFunctionListener(function, chart, new int[] { chart.getXYPlot().getDatasetCount() - 1 });
    }
    
    /**
     * Clear the region's contents, which will set the underlying JFreeChart to null.
     * The chart will be rebuilt if the plot methods are called again.
     */
    public void clear() { 
        
        // Clear the current base chart.
        chart = null;
        
        // Clear reference to chart in panel.
        chartPanel.setChart(null);
        
        // Clear the list of listeners.
        listeners.clear();
    }

    /**
     * Set the base JFreeChart of the region.
     * @param histogram The histogram to plot into the chart.
     * @param newChart The JFreeChart object.
     */
    // FIXME: This should not conflate adding a histogram with creating the backing chart for the region.
    private void setBaseChart(IBaseHistogram histogram, JFreeChart newChart) {
               
        // The new chart becomes the base chart for this region.
        chart = newChart;

        if (chartPanel == null)
            // Create the JPanel for the region.
            chartPanel = new ChartPanel(chart);
        else 
            // Reset the chart on the existing panel.
            chartPanel.setChart(chart);

        // Apply region styles to the new panel. Only the base chart has its own JPanel.
        if (styleConverter != null) {
            styleConverter.getChartState().setPanel(chartPanel);
            styleConverter.applyPanelStyle();
        }

        // Add a listener for receiving callbacks when the underlying histogram is updated,
        // so that it can be redrawn.
        int[] datasetIndices = getDatasetIndices(chart.getXYPlot(), 0);
        addHistogramListener(histogram, datasetIndices);
    }

    private JFreeChart createChart(IBaseHistogram hist, IPlotterStyle style, Converter converter) {

        // Create a new chart.
        JFreeChart newChart = converter.convert(hist, style);

        // Apply AIDA styles to the chart.
        if (style != null) {
            styleConverter = (AbstractStyleConverter) StyleConverterFactory.getStyleConverter(hist);
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

    private void addHistogramListener(IBaseHistogram hist, int[] datasetIndices) {
        PlotListener<?> listener = PlotListenerFactory.createListener(hist, chart, datasetIndices);
        if (listener != null) {
            ((IsObservable) hist).addListener(listener);
            this.listeners.add(listener);
        } else
            System.out.println("WARNING: No listener defined for plot " + hist.title() + " with type " + hist.getClass().getCanonicalName());               
    }

    private void addFunctionListener(IFunction function, JFreeChart chart, int[] datasetIndices) {
        PlotListener<IFunction> listener = new FunctionListener(function, chart, datasetIndices);
        ((IsObservable) function).addListener(listener);
        this.listeners.add(listener);
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
        addHistogramListener(hist, datasetIndices);
    }

    /*
     * FIXME: This should be fixed so it can be called at any time, not just when chart is non-null
     */
    public void setTitle(String title) {
        if (chart != null) {
            chart.setTitle(title);
        } else {
            System.err.println("Cannot set title.  No chart exists for this region yet.");
        }
    }

    public synchronized void update() {
        for (PlotListener<?> listener : listeners) {
            listener.update();
        }
    }
        
    void addToParentPanel(JPanel parentPanel) {
        if (parentPanel != null)
            parentPanel.add(
                    chartPanel, 
                    new PercentLayout.Constraint(
                            x() * 100,
                            y() * 100,
                            width() * 100,
                            height() * 100));
        else
            throw new RuntimeException("The parent JPanel points to null.");
    }    
}