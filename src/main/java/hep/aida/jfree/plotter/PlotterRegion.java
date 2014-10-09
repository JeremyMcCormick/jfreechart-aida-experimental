package hep.aida.jfree.plotter;

import hep.aida.IBaseHistogram;
import hep.aida.IDataPointSet;
import hep.aida.IFunction;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.converter.Converter;
import hep.aida.jfree.converter.ConverterFactory;
import hep.aida.jfree.plot.listener.PlotListener;
import hep.aida.jfree.plot.listener.PlotListenerFactory;
import hep.aida.jfree.plot.style.converter.StyleConverter;
import hep.aida.jfree.plot.style.converter.StyleConverterFactory;
import hep.aida.jfree.plot.style.util.LegendUtil;
import hep.aida.ref.event.IsObservable;
import hep.aida.ref.plotter.DummyPlotterRegion;
import jas.util.layout.PercentLayout;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;

/**
 * This class implements the plotting of AIDA data objects using a JFreeChart backend. Most of the
 * complex logic for converting from the internal AIDA data representation to JFreeChart classes is
 * contained in converter classes in the {@link hep.aida.jfree.converter} package that are called
 * from here. Each region has a <code>ChartPanel</code> associated with it, as well as a single
 * <code>JFreeChart</code> object onto which data is plotted. Histogram overlay is handled by appending 
 * datasets and renderers onto the chart's XYPlot object. Each histogram added to the region is assigned
 * a unique <code>PlotListener</code> which is notified when the histogram changes (e.g. fill is called).
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class PlotterRegion extends DummyPlotterRegion {

    // The position and dimensions.
    double x, y, w, h;
    
    // The style associated with the region, if any.
    IPlotterStyle regionStyle;
    
    // The title of the region.
    String title;       
    
    // The ChartPanel that represents this region.
    ChartPanel chartPanel;
    
    // The JFreeChart object with the visualization of the plots.
    JFreeChart baseChart;
    
    // The converter for applying IPlotterStyle to the created JFreeCharts.
    StyleConverter styleConverter;    
    
    // This object contains the non-visual state of this region.
    PlotterRegionState state = new PlotterRegionState();
                
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

    /**
     * Get the X position of this region in pixels.
     * @return The X position of this region in pixels.
     */
    double x() {
        return x;
    }

    /**
     * Get the Y position of this region in pixels.     
     * @return The Y position of this region in pixels.
     */
    public double y() {
        return y;
    }

    /**
     * Get the height of this region in pixels.
     * @return The height of this region in pixels.
     */
    public double height() {
        return h;
    }

    /**
     * Get the width of this region in pixels.
     * @return The width of this region in pixels.
     */
    public double width() {
        return w;
    }

    /**
     * Get the JPanel of this region which is a JFreeChart ChartPanel.
     * @return The JPanel of this region.
     */
    public JPanel getPanel() {
        return chartPanel;
    }

    /**
     * Get the JFreeChart XYPlot of this region.
     * @return The XYPlot of this region.
     */
    public XYPlot getPlot() {
        return baseChart.getXYPlot();
    }

    /**
     * Get the JFreeChart object of this region.
     * @return The JFreeChart of this region.
     */
    public JFreeChart getChart() {
        return baseChart;
    }
                
    /**
     * Plot an IBaseHistogam with the region's style.
     * @param histogram The IBaseHistogram to plot.
     */
    public void plot(IBaseHistogram histogram) {
        addObject(histogram, this.style(), null);
    }

    /**
     * Plot an IBaseHistogram with a user style.
     * @param histogram The IBaseHistogram to plot.
     * @param style The user's IPlotterStyle.
     */
    public void plot(IBaseHistogram histogram, IPlotterStyle style) {
        addObject(histogram, style, null);
    }

    /**
     * Plot an IFunction with the region's style.
     * @param function The IFunction to plot.     
     */
    public void plot(IFunction function) {
        addObject(function, this.style(), null);
    }

    /**
     * Plot an IFunction with a user style.
     * @param function The IFunction to plot.
     * @param style The user's IPlotterStyle.
     */
    public void plot(IFunction function, IPlotterStyle style) {
        addObject(function, style, null);
    }

    /**
     * Plot an IFunction with a user style and set of options.
     * @param function The IFunction to plot.
     * @param style The user's IPlotterStyle.
     * @param options The string options (currently ignored!).
     */
    public void plot(IFunction function, IPlotterStyle style, String options) {
        addObject(function, style, options);
    }
    
    /**
     * Plot an IDataPointSet into this region.
     * @param dps The IDataPointSet to plot.
     */
    public void plot(IDataPointSet dps) {
        addObject(dps, null, null);
    }
    
    /**
     * Plot an IDataPointSet into this region.
     * @param dps The IDataPointSet to plot.
     * @param style The IPlotterStyle to apply to the plot.
     */
    public void plot(IDataPointSet dps, IPlotterStyle style) {
        addObject(dps, style, null);
    }
    
    /**
     * Plot an IDataPointSet into this region.
     * @param dps The IDataPointSet to plot.
     * @param style The IPlotterStyle to apply to the plot.
     * @param options The user options (currently ignored).
     */
    public void plot(IDataPointSet dps, IPlotterStyle style, String options) {
        addObject(dps, style, options);
    }
    
    /**
     * Clear the region's contents, which will set the underlying JFreeChart to null
     * as well as clear the region's object state.  The chart will be rebuilt if the 
     * plot methods are called again.
     */
    public void clear() { 
        
        // Clear the current base chart.
        baseChart = null;
        
        // Clear reference to chart in panel.
        chartPanel.setChart(null);
        
        // Clear the backing object state.
        state.clear();
    }
    
    /**
     * Set the title of the region, as well as the backing JFreeChart if it has
     * already been created.
     * @param title The title text of the region.
     */
    public void setTitle(String title) {
        if (baseChart != null) {
            baseChart.setTitle(title);
        } 
        this.title = title;
    }
    
    /**
     * Get the title text of the region.
     * @return The title text of the region.
     */
    public String title() {
        return title;
    }
      
    /**
     * This is a convenience method for users to get the styles that are associated
     * with an object being plotted within this region.  This returns a list because 
     * the same object could be plotted more than once.
     * @param object
     * @return
     */
    public List<ObjectStyle> getObjectStyles(Object object) {
        List<ObjectStyle> foundObjectStyles = new ArrayList<ObjectStyle>();
        for (ObjectStyle objectStyle : state.getObjectStyles()) {
            if (objectStyle.object == object) {
                foundObjectStyles.add(objectStyle);
            }
        }
        return foundObjectStyles;
    }
    
    /**
     * Get a list of objects that are plotted in this region.
     * @return The list of objects plotted in this region.
     */
    public List<Object> getPlottedObjects() {
        return state.getObjects();
    }
       
    /* ---------------------------------------------------------------------------- */
    
    /**
     * Add this region's panel to its parent component using a percent layout,
     * based on the height and width dimensions and the x and y position of the region. 
     * @param parentPanel
     */
    void addToParentPanel(JPanel parentPanel) {
        if (parentPanel != null) {
            if (chartPanel != null) {
                parentPanel.add(chartPanel, 
                        new PercentLayout.Constraint(
                                x() * 100, 
                                y() * 100, 
                                width() * 100, 
                                height() * 100));
            }
        } else {
            throw new RuntimeException("The parent JPanel is null.");
        }
    }
        
    /**
     * This is the primary method for converting from AIDA to JFreeChart objects.
     * @param object The AIDA object to converter (e.g. an IHistogram1D etc.).
     * @param userStyle The PlotterStyle to apply to the plot.
     * @param options User options (currently ignored).
     */
    private void addObject(Object object, IPlotterStyle userStyle, String options) {     
                
        // Is this some kind of histogram?
        if (object instanceof IBaseHistogram) {
            // Add a histogram.
            createChart(IBaseHistogram.class, object, userStyle);
        // Is this a DataPointSet?
        } else if (object instanceof IDataPointSet) {     
            // Add a data point set.
            createChart(IDataPointSet.class, object, userStyle);            
        // Is this an IFunction object?
        } else if (object instanceof IFunction) {
            // Add a function to the region.
            createChart(IFunction.class, object, userStyle);
        } else {
            throw new IllegalArgumentException("The object type " + object.getClass().getCanonicalName() + " is not supported.");
        }                
    }
            
    /**
     * Set the base JFreeChart of the region.
     * @param histogram The histogram to plot into the chart.
     * @param newChart The JFreeChart object.
     */
    private void setBaseChart(JFreeChart newChart) {
               
        // The new chart becomes the base chart for this region.
        baseChart = newChart;

        // Is the current ChartPanel null?
        if (chartPanel == null) {
            // Create the JPanel for the region.
            chartPanel = new ChartPanel(baseChart);
            
            // Add a listener to activate callbacks when region is clicked.
            ChartPanelMouseListener mouseListener = new ChartPanelMouseListener(this);            
            mouseListener.addListeners(state.getRegionListeners());
            chartPanel.addMouseListener(mouseListener);
        } else { 
            // Reset the chart on the existing panel.
            chartPanel.setChart(baseChart);
        }
        
        // Apply region styles to the new panel. Only the base chart has its own JPanel.
        if (styleConverter != null) {
            styleConverter.applyStyle(chartPanel);
        }
    }

    /**
     * Create a JFreeChart from an IBaseHistogram and the given style.
     * @param histogram The IBaseHistogram to be converted.
     * @param style The IPlotterStyle to apply to the chart.
     * @param converter The object converter to use.
     * @return The new JFreeChart that was created.
     */
    @SuppressWarnings("unchecked")
    private <T> JFreeChart createChart(Class<T> type, Object object, IPlotterStyle style) {
        
        // Find the appropriate converter for the object.
        Converter<T> converter = null;
        if (type.equals(IBaseHistogram.class))
            // Need to look at object's specific type for IBaseHistogram.
            converter = ConverterFactory.instance().getConverter(object);
        else
            // We expect a specific class as the type argument.
            converter = ConverterFactory.instance().getConverter(type);
        
        // Was a converter found?
        if (converter == null)
            // There is not converter for this type or object!
            throw new RuntimeException("No converter found for type: "  + type.getCanonicalName());
                     
        // Create a new chart.
        JFreeChart newChart = converter.convert(type.cast(object), style);
        
        // Is there a PlotterStyle object?
        if (style != null) {            
            // Get a style converter for this object.
            styleConverter = StyleConverterFactory.create(object);
            // Was a style converter found?
            if (styleConverter != null) {
                // Apply visual style to the chart.
                styleConverter.applyStyle(newChart, object, style, null);                
            } 
        }
        
        // Get reference to the dataset for the PlotListener.
        XYDataset dataset = newChart.getXYPlot().getDataset(0);
        
        // Is the region's chart object not set?
        if (this.baseChart == null) {
            // Set the base chart, because this is the first object that was plotted.
            setBaseChart(newChart);
        } else {
            // Overlay the plot onto an existing chart.
            overlay(newChart.getXYPlot());
        }
        
        // Rebuild the chart's legend after the object has been added.
        LegendUtil.rebuildChartLegend(baseChart, state.getObjectStyles());
        
        // Add a listener which will handle updates to the histogram.
        addPlotListener(object, dataset);
        
        // Create a reference between the user style and its object.
        state.addObjectStyle(new ObjectStyle(object, style));        
        
        return newChart;
    }

    /**
     * Create a PlotListener and register it on the IBaseHistogram to get callbacks when it is updated.
     * @param hist The IBaseHistogram which will notify the listener.
     */
    private void addPlotListener(Object plot, XYDataset dataset) {
        PlotListener<?> listener = PlotListenerFactory.createListener(plot, baseChart, dataset);
        if (listener != null) {
            ((IsObservable) plot).addListener(listener);
            state.addPlotListener(listener);
        }                
    }

    /**
     * Overlay an IBaseHistogram onto an existing XYPlot.
     * @param histogram The IBaseHistogram to overlay.
     * @param overlayPlot The target XYPlot. 
     */
    private void overlay(XYPlot overlayPlot) {
        
        // The overlay plot's datasets will be appended to the current chart starting at this index.
        int datasetIndex = baseChart.getXYPlot().getDatasetCount();

        // Loop over all the datasets within the overlay plot.
        for (int i = 0, n = overlayPlot.getDatasetCount(); i < n; i++) {

            // Set the dataset and renderer for the overlay plot in the new chart.
            baseChart.getXYPlot().setDataset(datasetIndex, overlayPlot.getDataset(i));
            baseChart.getXYPlot().setRenderer(datasetIndex, overlayPlot.getRenderer(i));
            
            // Increment the index for the next dataset and renderer pair.
            ++datasetIndex;
        }
        
        // TODO: Should axes be resized here, too?
    }
        
    /*
    private void addFunctionListener(IFunction function, JFreeChart chart, int[] datasetIndices) {
        PlotListener<IFunction> listener = new FunctionListener(function, chart, datasetIndices);
        ((IsObservable) function).addListener(listener);
        this.listeners.add(listener);
    }
    */
    
    /**
     * Add an AIDA function object to this region.
     * @param function
     * @param style
     */
    // FIXME: I think this will fail if no histograms have been added to the region!
    /*
    private void createChart(IFunction function, IPlotterStyle style) {
        // Add function to JFreeChart object.
        FunctionConverter.addFunction(chart, function);

        // Apply styles to function.
        StyleConverter styleConverter = StyleConverterFactory.create(function);
        int[] datasetIndices = new int[] {chart.getXYPlot().getDatasetCount() - 1};
        styleConverter.applyStyle(chart, function, style, datasetIndices);
        
        // FIXME: Add this back once figure out how to connect with the object it is fitting.
        //addFunctionListener(function, chart, new int[] { chart.getXYPlot().getDatasetCount() - 1 });
    }
    */
    

}