package hep.aida.jfree.converter;

import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.dataset.DatasetConverter;

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
 */
public class Histogram1DConverter implements Converter<IHistogram1D> {

    public static final int STEP_DATA = 0;
    public static final int BAR_DATA = 1;
    public static final int POINT_DATA = 2;
    public static final int ERROR_DATA = 3;

    public Class<IHistogram1D> convertsType() {
        return IHistogram1D.class;
    }

    public JFreeChart convert(IHistogram1D h1d, IPlotterStyle style) {
        
        // Create all the datasets that might be needed later for supprting
        // various style settings.
        XYDataset[] datasets = createDatasets(h1d);

        // Array for the renderers.
        XYItemRenderer[] renderers = new XYItemRenderer[datasets.length];

        // Step renderer which draws the histogram step contour.
        XYItemRenderer renderer = new XYStepRenderer();
        renderer.setSeriesVisible(0, false);
        renderers[STEP_DATA] = renderer;

        // Bar renderer which draws bar chart.
        XYBarRenderer barRenderer = new XYBarRenderer();
        barRenderer.setDrawBarOutline(true);
        renderers[BAR_DATA] = barRenderer;

        // Line and shape renderer for lines between points and markers.
        renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesVisible(0, false);
        renderers[POINT_DATA] = renderer;

        // Error renderer that draws error bars and caps.
        XYErrorRenderer errorRenderer = new XYErrorRenderer();
        errorRenderer.setBaseShapesVisible(false);
        errorRenderer.setSeriesPaint(0, Color.black);
        renderers[ERROR_DATA] = errorRenderer;

        // Set the axis labels.
        String[] labels = ConverterUtil.getAxisLabels(h1d);
        NumberAxis xAxis = new NumberAxis(labels[0]);
        NumberAxis yAxis = new NumberAxis(labels[1]);

        // Create the plot without any data.
        XYPlot plot = new XYPlot(null, xAxis, yAxis, null);

        // Add datasets and associated renderers to the plot.
        for (int i = 0, n = datasets.length; i < n; i++) {
            plot.setDataset(i, datasets[i]);
            plot.setRenderer(i, renderers[i]);
        }

        // Set proper rendering order.
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        // Create the chart.
        JFreeChart chart = new JFreeChart(h1d.title(), JFreeChart.DEFAULT_TITLE_FONT, plot, false);

        // Apply the default theme.
        ChartFactory.getChartTheme().apply(chart);

        return chart;
    }

    public static XYDataset[] createDatasets(IHistogram1D h1d) {
        
        XYDataset[] datasets = new XYDataset[4];
        XYDataset[] stepData = DatasetConverter.forStepChart(h1d);
        XYDataset[] barData = DatasetConverter.forBarChart(h1d);
        XYDataset points = DatasetConverter.forPoints(h1d);

        // step data
        datasets[STEP_DATA] = stepData[0];

        // bar data
        datasets[BAR_DATA] = barData[0];

        // center points of bins for lines and markers
        datasets[POINT_DATA] = points;

        // errors
        datasets[ERROR_DATA] = stepData[1];

        return datasets;
    }
}
