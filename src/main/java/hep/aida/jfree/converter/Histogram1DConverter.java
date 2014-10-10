package hep.aida.jfree.converter;

import static hep.aida.jfree.dataset.Histogram1DAdapter.ERRORS;
import static hep.aida.jfree.dataset.Histogram1DAdapter.POINTS;
import static hep.aida.jfree.dataset.Histogram1DAdapter.STEPS;
import static hep.aida.jfree.dataset.Histogram1DAdapter.VALUES;
import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;
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
import org.jfree.data.Range;
import org.jfree.data.RangeType;
import org.jfree.data.xy.XYDataset;

/**
 * Convert an AIDA <code>IHistogram1D</code> to a <code>JFreeChart</code> object.
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram1DConverter implements Converter<IHistogram1D> {

    static final double DEFAULT_X_AXIS_MARGIN = 0.05;
    static final double DEFAULT_Y_AXIS_MARGIN = 0.2;
    
    public Class<IHistogram1D> convertsType() {
        return IHistogram1D.class;
    }

    public JFreeChart convert(JFreeChart chart, IHistogram1D histogram, IPlotterStyle style) {
        
        // Create the backing datasets, which are actually adapters.
        XYDataset[] datasets = createDatasets(histogram);

        // Create the renderers.
        XYItemRenderer[] renderers = createHistogramRenderers(datasets);
                        
        // Set the axis labels.
        String[] labels = ConverterUtil.getAxisLabels(histogram);
        
        // Configure the X axis.
        NumberAxis xAxis = new NumberAxis(labels[0]);
        xAxis.setAutoRange(true);
        xAxis.setDefaultAutoRange(new Range(histogram.axis().lowerEdge(), histogram.axis().upperEdge()));
        xAxis.setUpperMargin(DEFAULT_X_AXIS_MARGIN);
        
        // Configure the Y axis.
        NumberAxis yAxis = new NumberAxis(labels[1]);
        yAxis.setAutoRange(true);
        yAxis.setAutoRangeIncludesZero(true);
        yAxis.setUpperMargin(DEFAULT_Y_AXIS_MARGIN);
        // If there is data here already then size the y axis correctly.
        if (histogram.maxBinHeight() > 0.0) {
            yAxis.setAutoRangeMinimumSize(histogram.maxBinHeight() + findMaxError(histogram));
        }
        yAxis.setRangeType(RangeType.POSITIVE);

        return createHistogramChart(histogram.title(), xAxis, yAxis, datasets, renderers);
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
        JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);        

        // Apply the default theme.
        ChartFactory.getChartTheme().apply(chart);
        
        return chart;
    }
    
    static XYDataset[] createDatasets(IHistogram1D h1d) {        
        XYDataset[] datasets = new XYDataset[4];                             
        Histogram1DAdapter adapter = new Histogram1DAdapter(h1d);
        for (int i=0; i<=3; i++) {
            datasets[i] = adapter;
        }
        return datasets;
    }        
    
    /**
     * Get the error value for the highest bin value.
     * @param h1D The IHistogram1D.
     * @return The error value for the highest bin value.
     */
    public static double findMaxError(IHistogram1D h1D) {
        double maxBinHeight = h1D.maxBinHeight();
        double maxError = 0;
        for (int i = 0; i < h1D.axis().bins(); i++) {
            if (h1D.binHeight(i) == maxBinHeight) {
                if (h1D.binError(i) > maxError)
                    maxError = h1D.binError(i); 
            }
        }
        return maxError;
    }    
}
