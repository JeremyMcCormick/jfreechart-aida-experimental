package hep.aida.jfree.converter;

import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.dataset.Histogram1DAdapter;
import static hep.aida.jfree.dataset.Histogram1DAdapter.*;

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
import org.jfree.data.RangeType;
import org.jfree.data.xy.XYDataset;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram1DConverter implements Converter<IHistogram1D> {

    public static final int STEP_DATA = 0;
    public static final int BAR_DATA = 1;
    public static final int POINT_DATA = 2;
    public static final int ERROR_DATA = 3;

    public Class<IHistogram1D> convertsType() {
        return IHistogram1D.class;
    }

    public JFreeChart convert(IHistogram1D histogram, IPlotterStyle style) {
        
        // Create the backing datasets which are just 
        // adapters to the AIDA 1D histogram type.
        XYDataset[] datasets = createDatasets(histogram);

        // Create array for the renderers.
        XYItemRenderer[] renderers = new XYItemRenderer[datasets.length];

        // Add bar renderer which draws bar chart.
        XYBarRenderer barRenderer = new XYBarRenderer();
        barRenderer.setDrawBarOutline(true);
        renderers[VALUES] = barRenderer;

        // Add error renderer that draws error bars and caps.
        XYErrorRenderer errorRenderer = new XYErrorRenderer();
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
                        
        // Set the axis labels.
        String[] labels = ConverterUtil.getAxisLabels(histogram);
        
        // Configure X axis.
        NumberAxis xAxis = new NumberAxis(labels[0]);
        xAxis.setLowerBound(histogram.axis().binLowerEdge(0));
        xAxis.setUpperBound(histogram.axis().binUpperEdge(histogram.axis().bins() - 1));
        xAxis.setAutoRange(false);
        
        // Configure Y axis.
        NumberAxis yAxis = new NumberAxis(labels[1]);
        yAxis.setAutoRange(true);
        yAxis.setAutoRangeIncludesZero(true);
        yAxis.setRangeType(RangeType.POSITIVE);

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
        JFreeChart chart = new JFreeChart(histogram.title(), JFreeChart.DEFAULT_TITLE_FONT, plot, false);

        // Apply the default theme.
        ChartFactory.getChartTheme().apply(chart);

        return chart;
    }

    private static XYDataset[] createDatasets(IHistogram1D h1d) {        
        XYDataset[] datasets = new XYDataset[4];                             
        Histogram1DAdapter adapter = new Histogram1DAdapter(h1d);
        for (int i=0; i<=3; i++) {
            datasets[i] = adapter;
        }
        return datasets;
    }
}
