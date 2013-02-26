package hep.aida.jfree.converter;

import hep.aida.IAxis;
import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.xy.XYIntervalSeries;
import org.jfree.data.xy.XYIntervalSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram1D implements Histogram<IHistogram1D>
{

    public Class<IHistogram1D> convertsType()
    {
        return IHistogram1D.class;
    }

    public JFreeChart convert(IHistogram1D h1d, IPlotterStyle style)
    {
        // First case will draw the histogram contour only and no bars.
        if (style != null && !style.dataStyle().lineStyle().isVisible() && !style.dataStyle().fillStyle().isVisible()) {
            //System.out.println("converting outline only");
            return convertToStepChart(h1d);
        // This will draw the default histogram with style options for fill and bars.
        } else {
            //System.out.println("convert default");
            return convertToBarChart(h1d);
        }
    }

    // Convert 1D histogram to chart
    static JFreeChart convertToBarChart(IHistogram1D h1d)
    {

        // Create datasets
        XYIntervalSeriesCollection[] datasets = Dataset.convert(h1d);
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

    static JFreeChart convertToStepChart(IHistogram1D h1d)
    {

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

    static JFreeChart convertXYPlot(IHistogram1D h1d)
    {

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
}
