package hep.aida.jfree.plotter;

import hep.aida.IBaseHistogram;
import hep.aida.IDataPointSet;
import hep.aida.IFunction;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.converter.Converter;
import hep.aida.jfree.converter.ConverterFactory;
import hep.aida.jfree.plotter.listener.PlotListener;
import hep.aida.jfree.plotter.listener.PlotListenerFactory;
import hep.aida.jfree.plotter.style.converter.StyleConverter;
import hep.aida.jfree.plotter.style.converter.StyleConverterFactory;
import hep.aida.jfree.plotter.style.util.LegendUtil;
import hep.aida.jfree.plotter.style.util.RegionUtil;
import hep.aida.ref.plotter.DummyPlotterRegion;
import jas.util.layout.PercentLayout;

import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;

/**
 * This class implements the plotting of AIDA data objects using a JFreeChart backend. Most of the complex logic for
 * converting from the internal AIDA data representation to JFreeChart classes is contained in converter classes in the
 * {@link hep.aida.jfree.converter} package that are called from here. Each region has a <code>ChartPanel</code>
 * associated with it, as well as a single <code>JFreeChart</code> object onto which data is plotted. Histogram overlay
 * is handled by appending datasets and renderers onto the chart's XYPlot object. Each histogram added to the region is
 * assigned a unique <code>PlotListener</code> which is notified when the histogram changes (e.g. fill is called).
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class PlotterRegion extends DummyPlotterRegion {

    // The position and dimensions.
    private double x, y, w, h;

    // The style associated with the region, if any.
    private IPlotterStyle regionStyle;

    // The title of the region.
    private String title;

    // The ChartPanel that represents this region.
    private ChartPanel chartPanel;

    // The JFreeChart object with the visualization of the plots.
    private JFreeChart baseChart;

    // The converter for applying IPlotterStyle to the created JFreeCharts.
    private StyleConverter styleConverter;

    // This object contains the non-visual state of this region.
    private PlotterRegionState state = new PlotterRegionState();

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
     * This is implemented to only apply style to the region itself and not the scene objects.
     * 
     * @param the style to apply
     */
    public void applyStyle(IPlotterStyle style) {

        if (this.baseChart == null) {
            throw new IllegalStateException("The chart has not been created yet.");
        }

        this.regionStyle = style;

        // Style is only applied here to the region itself and not scene objects.
        RegionUtil.applyRegionStyle(this.baseChart, this.regionStyle);
    }

    /**
     * Return the <tt>IPlotterStyle<tt> of this region.
     * 
     * @return The <tt>IPlotterStyle</tt> of this region.
     */
    public IPlotterStyle style() {
        return regionStyle;
    }

    /**
     * Set the <tt>IPlotterStyle</tt> of this region.
     * 
     * @param The <tt>IPlotterStyle</tt> of this region.
     */
    public void setStyle(IPlotterStyle style) {
        this.regionStyle = style;
    }

    /**
     * Get the X position of this region in pixels.
     * 
     * @return The X position of this region in pixels.
     */
    public double x() {
        return x;
    }

    /**
     * Get the Y position of this region in pixels.
     * 
     * @return The Y position of this region in pixels.
     */
    public double y() {
        return y;
    }

    /**
     * Get the height of this region in pixels.
     * 
     * @return The height of this region in pixels.
     */
    public double height() {
        return h;
    }

    /**
     * Get the width of this region in pixels.
     * 
     * @return The width of this region in pixels.
     */
    public double width() {
        return w;
    }

    /**
     * Get the JPanel of this region which is a JFreeChart ChartPanel.
     * 
     * @return The JPanel of this region.
     */
    public JPanel getPanel() {
        return chartPanel;
    }

    /**
     * Plot an IBaseHistogam with the region's style.
     * 
     * @param histogram The IBaseHistogram to plot.
     */
    public void plot(IBaseHistogram histogram) {
        addObject(histogram, this.style(), null);
    }

    /**
     * Plot an IBaseHistogram with a user style.
     * 
     * @param histogram The IBaseHistogram to plot.
     * @param style The user's IPlotterStyle.
     */
    public void plot(IBaseHistogram histogram, IPlotterStyle style) {
        addObject(histogram, style, null);
    }

    /**
     * Plot an IFunction with the region's style.
     * 
     * @param function The IFunction to plot.
     */
    public void plot(IFunction function) {
        addObject(function, this.style(), null);
    }

    /**
     * Plot an IFunction with a user style.
     * 
     * @param function The IFunction to plot.
     * @param style The user's IPlotterStyle.
     */
    public void plot(IFunction function, IPlotterStyle style) {
        addObject(function, style, null);
    }

    /**
     * Plot an IFunction with a user style and set of options.
     * 
     * @param function The IFunction to plot.
     * @param style The user's IPlotterStyle.
     * @param options The string options (currently ignored!).
     */
    public void plot(IFunction function, IPlotterStyle style, String options) {
        addObject(function, style, options);
    }

    /**
     * Plot an IDataPointSet into this region.
     * 
     * @param dps The IDataPointSet to plot.
     */
    public void plot(IDataPointSet dps) {
        addObject(dps, null, null);
    }

    /**
     * Plot an IDataPointSet into this region.
     * 
     * @param dps The IDataPointSet to plot.
     * @param style The IPlotterStyle to apply to the plot.
     */
    public void plot(IDataPointSet dps, IPlotterStyle style) {
        addObject(dps, style, null);
    }

    /**
     * Plot an IDataPointSet into this region.
     * 
     * @param dps The IDataPointSet to plot.
     * @param style The IPlotterStyle to apply to the plot.
     * @param options The user options (currently ignored).
     */
    public void plot(IDataPointSet dps, IPlotterStyle style, String options) {
        addObject(dps, style, options);
    }

    /**
     * Clear the region's contents, which will set the underlying JFreeChart to null as well as clear the region's
     * object state. The chart will be rebuilt if the plot methods are called again.
     */
    public void clear() {

        // Clear the current base chart.
        baseChart = null;

        // Clear reference to chart in panel.
        if (chartPanel != null) {
            chartPanel.setChart(null);
        }

        // Clear the backing object state.
        state.clear();
    }

    /**
     * Set the title of the region, as well as the backing JFreeChart if it has already been created.
     * 
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
     * 
     * @return The title text of the region.
     */
    public String title() {
        return title;
    }

    /**
     * Get a list of objects that are plotted in this region.
     * 
     * @return The list of objects plotted in this region.
     */
    public List<Object> getPlottedObjects() {
        return state.getObjects();
    }

    /* ---------------------------------------------------------------------------- */

    /**
     * Add this region's panel to its parent component using a percent layout, based on the height and width dimensions
     * and the x and y position of the region.
     * 
     * @param parentPanel
     */
    void addToParentPanel(JPanel parentPanel) {
        if (parentPanel != null) {
            if (chartPanel != null) {
                parentPanel.add(chartPanel, new PercentLayout.Constraint(x() * 100, y() * 100, width() * 100,
                        height() * 100));
            }
        } else {
            throw new RuntimeException("The parent JPanel is null.");
        }
    }

    /**
     * This is the primary method for converting from AIDA to JFreeChart objects.
     * 
     * @param object The AIDA object to convert (e.g. an IHistogram1D etc.).
     * @param userStyle The PlotterStyle to apply.
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
            // The object type is not supported.
            throw new IllegalArgumentException("The object type " + object.getClass().getCanonicalName()
                    + " is not supported.");
        }
    }

    /**
     * Set the base JFreeChart of the region.
     * 
     * @param histogram The histogram to plot into the chart.
     * @param newChart The JFreeChart object.
     */
    private void setBaseChart(JFreeChart newChart) {

        // This chart becomes the base chart for the region.
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
     * Create an JFreeChart from an AIDA object and the given style, or if the base chart is already set, then overlay
     * the plot onto it.
     * 
     * @param type The AIDA object's class.
     * @param object The AIDA object to convert.
     * @param style The PlotterStyle to apply.
     * @return The new JFreeChart that was created.
     */
    @SuppressWarnings("unchecked")
    private <T> void createChart(Class<T> type, Object object, IPlotterStyle style) {

        // Find the appropriate converter for the object.
        Converter<T> converter = null;
        if (type.equals(IBaseHistogram.class)) {
            // Need to look at object's specific type for IBaseHistogram.
            converter = ConverterFactory.instance().getConverter(object);
        } else {
            // We expect a specific class as the type argument.
            converter = ConverterFactory.instance().getConverter(type);
        }

        // Was a converter found?
        if (converter == null)
            // There is no converter for this type or object.
            throw new RuntimeException("No converter found for object with type: "
                    + object.getClass().getCanonicalName());

        // Create a chart from the AIDA object.
        // In a few cases, this might not create a new chart (e.g. for an IFunction).
        JFreeChart newChart = converter.convert(baseChart, type.cast(object), style);

        // The FunctionStyleConverter will need the dataset indices.
        int[] datasetIndices = null;
        if (object instanceof IFunction) {
            datasetIndices = new int[] {baseChart.getXYPlot().getDatasetCount() - 1};
        }

        // Is there a PlotterStyle object?
        if (style != null) {
            // Get a style converter for this object.
            styleConverter = StyleConverterFactory.create(object);
            // Was a style converter found?
            if (styleConverter != null) {
                // Apply visual style to the chart.
                styleConverter.applyStyle(newChart, object, style, datasetIndices);
            }
        }

        // Get reference to the dataset for the PlotListener. It should be the last one in the list.
        XYDataset dataset = newChart.getXYPlot().getDataset(newChart.getXYPlot().getDatasetCount() - 1);

        // Is the region's chart object not set?
        if (this.baseChart == null) {
            // Set the base chart, because this is the first object that was plotted.
            setBaseChart(newChart);
        } else {
            // Did the converter actually create a new chart or just modify the current one?
            if (this.baseChart != newChart)
                // Overlay the new chart onto the existing base chart.
                overlay(newChart.getXYPlot());
        }

        // Add a listener which will handle updates to the histogram.
        addPlotListener(object, dataset);

        // Create a reference between the user style and its object.
        state.addObjectStyle(new ObjectStyle(object, style));

        // Rebuild the chart's legend after the object has been added.
        LegendUtil.rebuildChartLegend(baseChart, state.getObjectStyles());
    }

    /**
     * Create a PlotListener and register it on the IBaseHistogram to get callbacks when it is updated.
     * 
     * @param hist The IBaseHistogram which will notify the listener.
     */
    private void addPlotListener(Object plot, XYDataset dataset) {
        PlotListener<?> listener = PlotListenerFactory.createListener(plot, baseChart, dataset);
        if (listener != null) {
            state.addPlotListener(listener);
        }
    }

    /**
     * Overlay an IBaseHistogram onto an existing XYPlot.
     * 
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
    }

    public void refresh() {
        if (this.chartPanel != null && this.chartPanel.getChart() != null) {
            for (PlotListener<?> listener : state.plotListeners) {
                // Call the listener's update method.
                listener.update();
            }
            getChart().fireChartChanged();
        }
    }

    public JFreeChart getChart() {
        return this.chartPanel.getChart();
    }

    public PlotterRegionState getState() {
        return this.state;
    }
}