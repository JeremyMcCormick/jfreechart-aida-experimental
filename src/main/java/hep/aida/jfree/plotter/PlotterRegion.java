package hep.aida.jfree.plotter;

import hep.aida.IBaseHistogram;
import hep.aida.IFunction;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.converter.Converter;
import hep.aida.jfree.converter.FunctionConverter;
import hep.aida.jfree.converter.HistogramConverterFactory;
import hep.aida.jfree.plot.listener.PlotListener;
import hep.aida.jfree.plot.listener.PlotListenerFactory;
import hep.aida.jfree.plot.style.converter.AbstractStyleConverter;
import hep.aida.jfree.plot.style.converter.StyleConverter;
import hep.aida.jfree.plot.style.converter.StyleConverterFactory;
import hep.aida.ref.event.IsObservable;
import hep.aida.ref.plotter.BaseStyle;
import hep.aida.ref.plotter.DummyPlotterRegion;
import jas.util.layout.PercentLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
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
 */
// TODO: Add implementations for remaining plot methods from IPlotterRegion interface.
// TODO: Add connection between functions and data they are fitting for dynamic updating (code may not belong here).
public class PlotterRegion extends DummyPlotterRegion {

    double x, y, w, h;
    IPlotterStyle regionStyle;
    String title;       
    ChartPanel chartPanel;
    JFreeChart chart;
            
    List<PlotListener<?>> plotListeners = new ArrayList<PlotListener<?>>();
    List<ObjectStyle> objectStyles = new ArrayList<ObjectStyle>();
    List<PlotterRegionListener> plotterRegionListeners = new ArrayList<PlotterRegionListener>();
    
    // FIXME: Can this be initialized when the region is created?  Or can a static object be used?
    AbstractStyleConverter styleConverter;

    /**
     * Create a new plotter region.
     * 
     * @param x The position as percentage in X.
     * @param y The position as percentage in Y.
     * @param w The width as percentage.
     * @param h The height as percentage.
     */
    PlotterRegion(IPlotterStyle style, double x, double y, double w, double h) {
        this.regionStyle = style;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    public void addListener(PlotterRegionListener listener) {
        plotterRegionListeners.add(listener);
    }

    /**
     * Return the <tt>IPlotterStyle<tt> of this region.
     * @return The <tt>IPlotterStyle</tt> of this region.
     */
    public IPlotterStyle style() {
        return regionStyle;
    }

    /**
     * Set the <tt>IPlotterStyle</tt> of this region.
     * @param The <tt>IPlotterStyle</tt> of this region.
     */
    public void setStyle(IPlotterStyle style) {
        this.regionStyle = style;
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
                
    public void plot(IBaseHistogram histogram) {
        plotObject(histogram, this.style(), null);
    }

    public void plot(IBaseHistogram histogram, IPlotterStyle style) {
        plotObject(histogram, style, null);
    }

    public void plot(IFunction function) {
        plotObject(function, regionStyle, null);
    }

    public void plot(IFunction function, IPlotterStyle style) {
        plotObject(function, style, null);
    }

    public void plot(IFunction function, IPlotterStyle style, String options) {
        plotObject(function, style, options);
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
        plotListeners.clear();
    }
    
    public void setTitle(String title) {
        if (chart != null) {
            chart.setTitle(title);
        } 
        this.title = title;
    }
    
    public String title() {
        return title;
    }

    public synchronized void update() {
        for (PlotListener<?> listener : plotListeners) {
            listener.update();
        }
    }
    
    /**
     * This is a convenience method for users to get the styles that are associated
     * with the objects being plotted within this region.
     * @param object
     * @return
     */
    public List<ObjectStyle> getObjectStyles(Object object) {
        List<ObjectStyle> foundObjectStyles = new ArrayList<ObjectStyle>();
        for (ObjectStyle objectStyle : objectStyles) {
            if (objectStyle.object == object) {
                foundObjectStyles.add(objectStyle);
            }
        }
        return foundObjectStyles;
    }
    
    public List<ObjectStyle> getObjectStyles() {
        return Collections.unmodifiableList(objectStyles);
    }
    
    /* ---------------------------------------------------------------------------- */
    
    /**
     * Add this region's panel to its parent component using a percent layout
     * based on height, width, x and y. 
     * @param parentPanel
     */
    void addToParentPanel(JPanel parentPanel) {
        if (parentPanel != null) {
            if (chartPanel != null) {
                parentPanel.add(chartPanel, 
                        new PercentLayout.Constraint(x() * 100, y() * 100, width() * 100, height() * 100));
            }
        } else {
            throw new RuntimeException("The parent JPanel is null.");
        }
    }

    /**
     * Add a new histogram to the chart, which will either create the chart for the 
     * entire region or overlay the histogram onto an existing chart.
     * @param histogram
     * @param style
     * @param converter
     * @return
     */
    private JFreeChart add(IBaseHistogram histogram, IPlotterStyle style, Converter<?> converter) {
        
        // Create a new chart object.
        JFreeChart chart = createChart(histogram, style, converter);
        
        // Is the region's chart object not set?
        if (this.chart == null) {
            // Set the base chart, because this is the first object plotted.
            setBaseChart(histogram, chart);
        } else {
            // Overlay the histogram onto an existing chart from its XYPlot.
            overlay(histogram, chart.getXYPlot());
        }
        return chart;
    }
    
    /**
     * Add an AIDA function object to this region.
     * @param function
     * @param style
     */
    private void add(IFunction function, IPlotterStyle style) {
        // Add function to JFreeChart object.
        FunctionConverter.addFunction(chart, function);

        // Apply styles to function.
        StyleConverter styleConverter = StyleConverterFactory.getStyleConverter(function);
        styleConverter.setStyle(style);
        styleConverter.setChartState(new ChartState(chartPanel, chart, function, chart.getXYPlot().getDatasetCount() - 1));
        styleConverter.applyStyle();
        
        // FIXME: Add this back once figure out how to connect with the object it is fitting.
        //addFunctionListener(function, chart, new int[] { chart.getXYPlot().getDatasetCount() - 1 });
    }
    
    /**
     * This is the primary method for converting from AIDA to JFreeChart objects.
     * @param object
     * @param userStyle
     * @param options
     */
    // TODO: Handle options string.
    private void plotObject(Object object, IPlotterStyle userStyle, String options) {

        // Is this an AIDA base histogram?
        if (object instanceof IBaseHistogram) {

            // Find the appropriate converter for the histogram.
            Converter<?> converter = HistogramConverterFactory.instance().getConverter(object);

            // Found a converter for this histogram type?
            if (converter != null) {

                // Create a reference between the user style and its object.
                objectStyles.add(new ObjectStyle(object, userStyle));

                // Does the user style not have a parent set?
                if (((BaseStyle) userStyle).parentList().size() == 0) {
                    // Is the user style the same as the region style?
                    if (userStyle != regionStyle)
                        // Set the parent style to the region so that cascading occurs.
                        ((BaseStyle) userStyle).setParent(regionStyle);
                }

                // Create a new chart, or overlay a histogram onto the existing chart.
                add((IBaseHistogram) object, userStyle, converter);
            } else {
                // Cannot display this type of histogram.  Converter was not written yet!
                System.err.println("WARNING: IBaseHistogram of type " + object.getClass().getCanonicalName() + " is not supported yet!");
            }
        // Is this an IFunction object?
        } else if (object instanceof IFunction) {
            // Add a function to the region.
            // FIXME: I think this will fail if no histograms have been added to the region!
            add((IFunction) object, userStyle);
        } else {
            // Cannot display this type of object at all.
            // FIXME: Should this throw a runtime exception?
            System.err.println("WARNING: Object of type " + object.getClass().getCanonicalName() + " is not supported!");
        }        
    }
    
    /**
     * Set the base JFreeChart of the region.
     * @param histogram The histogram to plot into the chart.
     * @param newChart The JFreeChart object.
     */
    private void setBaseChart(IBaseHistogram histogram, JFreeChart newChart) {
               
        // The new chart becomes the base chart for this region.
        chart = newChart;

        if (chartPanel == null) {
            // Create the JPanel for the region.
            chartPanel = new ChartPanel(chart);
            ChartPanelMouseListener mouseListener = new ChartPanelMouseListener(this);            
            mouseListener.addListeners(this.plotterRegionListeners);
            chartPanel.addMouseListener(mouseListener);
        } else { 
            // Reset the chart on the existing panel.
            chartPanel.setChart(chart);
        }
        
        // Apply region styles to the new panel. Only the base chart has its own JPanel.
        if (styleConverter != null) {
            styleConverter.getChartState().setPanel(chartPanel);
            styleConverter.applyPanelStyle();
        }

        // Add a listener for receiving callbacks when the underlying histogram is updated,
        // so that it can be redrawn on the fly.
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
                System.err.println("WARNING: No style converter found for " + hist.title() + " with type " + hist.getClass().getCanonicalName() + ".");
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
            this.plotListeners.add(listener);
        } else
            System.out.println("WARNING: No listener defined for plot " + hist.title() + " with type " + hist.getClass().getCanonicalName());               
    }

    /*
    private void addFunctionListener(IFunction function, JFreeChart chart, int[] datasetIndices) {
        PlotListener<IFunction> listener = new FunctionListener(function, chart, datasetIndices);
        ((IsObservable) function).addListener(listener);
        this.listeners.add(listener);
    }
    */

    private int[] getDatasetIndices(XYPlot plot, int offset) {
        int[] datasetIndices = new int[plot.getDatasetCount()];
        for (int i = 0, n = plot.getDatasetCount(); i < n; i++) {
            datasetIndices[i] = i + offset;
        }
        return datasetIndices;
    }

    private void overlay(IBaseHistogram hist, XYPlot overlayPlot) {

        // Get array containing indices of datasets which will be used by the listener.
        int[] datasetIndices = getDatasetIndices(overlayPlot, chart.getXYPlot().getDatasetCount());

        // The overlay plot's datasets will be appended to the current chart starting at this index.
        int datasetIndex = chart.getXYPlot().getDatasetCount();

        // Loop over all the datasets within the overlay plot.
        for (int i = 0, n = overlayPlot.getDatasetCount(); i < n; i++) {

            // Set the dataset and renderer for the overlay plot using the .
            chart.getXYPlot().setDataset(datasetIndex, overlayPlot.getDataset(i));
            chart.getXYPlot().setRenderer(datasetIndex, overlayPlot.getRenderer(i));

            // Increment the index for the next dataset and renderer pair.
            ++datasetIndex;
        }

        // Add a listener which will handle updates to the overlay histogram.
        addHistogramListener(hist, datasetIndices);
    }
}