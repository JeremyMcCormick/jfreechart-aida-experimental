package hep.aida.jfree.converter;

import hep.aida.IAxis;
import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.dataset.DatasetConverter;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram1DConverter implements HistogramConverter<IHistogram1D> {

    public static final int STEP_DATA = 0;
    public static final int BAR_DATA = 1;
    public static final int POINT_DATA = 2;
    public static final int ERROR_DATA = 3;

    public Class<IHistogram1D> convertsType() {
        return IHistogram1D.class;
    }

    public JFreeChart convert(IHistogram1D h1d, IPlotterStyle style) {
        return convertFullDatasets(h1d);
    }

    // Convert 1D histogram to chart
    static JFreeChart convertToBarChart(IHistogram1D h1d) {
        // Create datasets
        XYIntervalSeriesCollection[] datasets = DatasetConverter.forBarChart(h1d);
        XYIntervalSeriesCollection valuesDataset = datasets[0];
        XYIntervalSeriesCollection errorsDataset = datasets[1];

        // create the chart
        JFreeChart chart = ChartFactory.createHistogram(h1d.title(), null, null, valuesDataset, PlotOrientation.VERTICAL, true, true, false);

        // Set X axis display range
        IAxis axis = h1d.axis();
        NumberAxis domain = (NumberAxis) chart.getXYPlot().getDomainAxis();
        domain.setRange(axis.lowerEdge(), axis.upperEdge());

        // Set the Renderer for the values dataset, so it displays as bars.
        XYBarRenderer barRenderer = new XYBarRenderer();
        barRenderer.setDrawBarOutline(true);
        chart.getXYPlot().setRenderer(0, barRenderer);

        // Create a new Renderer for the errors
        XYErrorRenderer errorRenderer = new XYErrorRenderer();

        // Turn off display of shape at error mid-point
        errorRenderer.setBaseShapesVisible(false);

        // Set default error line color to black
        errorRenderer.setSeriesPaint(0, Color.black);

        XYPlot plot = chart.getXYPlot();

        // Set the values dataset as the second one in the plot
        plot.setDataset(1, errorsDataset);

        // Set the Renderer for the errors
        plot.setRenderer(1, errorRenderer);

        // Set display order so errors are on top
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        return chart;
    }

    static JFreeChart convertToStepChart(IHistogram1D h1d) {
        // Create two datasets, one for values, and one for errors.
        XYSeries values = new XYSeries("values");
        XYIntervalSeries errors = new XYIntervalSeries("errors");
        IAxis axis = h1d.axis();
        int nbins = axis.bins();
        for (int i = 0; i < nbins; i++) {
            values.add(axis.binLowerEdge(i), h1d.binHeight(i));
            double error = h1d.binError(i);
            errors.add(axis.binCenter(i), axis.binCenter(i), axis.binCenter(i), h1d.binHeight(i), h1d.binHeight(i) - error, h1d.binHeight(i) + error);
            if (i == (nbins - 1)) {
                values.add(axis.binUpperEdge(i), h1d.binHeight(i));
            }
        }
        XYSeriesCollection valuesDataset = new XYSeriesCollection();
        valuesDataset.addSeries(values);
        XYIntervalSeriesCollection errorsDataset = new XYIntervalSeriesCollection();
        errorsDataset.addSeries(errors);

        // Axis labels.
        String xLabel = "";
        if (h1d.annotation().hasKey("xAxisLabel")) {
            xLabel = h1d.annotation().value("xAxisLabel");
        }
        String yLabel = "";
        if (h1d.annotation().hasKey("yAxisLabel")) {
            yLabel = h1d.annotation().value("yAxisLabel");
        }

        // Create the chart.
        JFreeChart chart = ChartFactory.createHistogram("histo", xLabel, yLabel, valuesDataset, PlotOrientation.VERTICAL, true, true, false);

        // Set X axis range.
        NumberAxis domain = (NumberAxis) chart.getXYPlot().getDomainAxis();
        domain.setRange(axis.lowerEdge(), axis.upperEdge());

        // Display as steps instead of bars.
        chart.getXYPlot().setRenderer(new XYStepRenderer());

        // Display error bars.
        chart.getXYPlot().setDataset(1, errorsDataset);
        XYErrorRenderer errorRenderer = new XYErrorRenderer();
        errorRenderer.setBaseShapesVisible(false);
        errorRenderer.setSeriesPaint(0, Color.black);
        chart.getXYPlot().setRenderer(1, errorRenderer);

        return chart;
    }

    public static JFreeChart convertFullDatasets(IHistogram1D h1d) {
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

        // xAxis.setTickUnit(new NumberTickUnit(h1d.axis().binWidth(0)*10));
        // xAxis.setMinorTickCount(10);

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
