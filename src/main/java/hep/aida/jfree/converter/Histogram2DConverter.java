package hep.aida.jfree.converter;

import hep.aida.IHistogram2D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.XYBoxRenderer;

import java.awt.Color;
import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.Range;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Histogram2DConverter implements HistogramConverter<IHistogram2D> {

    static public final int COLOR_DATA = 0;
    static public final int COLOR_SCALE_LEGEND = 1;
    static public final int DEFAULT_MINOR_TIC_COUNT = 2;

    public Class<IHistogram2D> convertsType() {
        return IHistogram2D.class;
    }

    public static double[] calculateZLimits(XYZDataset ds) {
        double zLogMin;
        double zMin;
        double zMax;
        if (ds.getItemCount(0) == 0) {
            zMin = 0;
            zLogMin = Double.POSITIVE_INFINITY;
            zMax = 1;
        } else {
            zLogMin = Double.POSITIVE_INFINITY;
            zMin = Double.POSITIVE_INFINITY;
            zMax = Double.NEGATIVE_INFINITY;

            for (int i = 0, n = ds.getItemCount(0); i < n; i++) {
                double d = ds.getZValue(0, i);
                zMin = Math.min(zMin, d);
                if (d > 0.0000000000001) // was zero???
                    zLogMin = Math.min(zLogMin, d);
                zMax = Math.max(zMax, d);
            }
        }
        return new double[] { zMin, zMax, zLogMin };
    }

    /**
     * Convert 2D histogram to color map chart.
     * 
     * @param h2d
     * @param style
     * @return
     */
    static JFreeChart toColorMap(IHistogram2D h2d, IPlotterStyle style) {
        // Create dataset.
        XYZDataset dataset = DatasetConverter.convert(h2d);

        // Create X axis.
        NumberAxis xAxis = new NumberAxis(null);

        // Create Y axis.
        NumberAxis yAxis = new NumberAxis(null);

        // Check if using a log scale.
        boolean logScale = false;
        if (style.zAxisStyle().parameterValue("scale").startsWith("log")) {
            logScale = true;
        }

        // Setup the renderer.
        XYBlockRenderer renderer = createColorMapRenderer(dataset, h2d, style);

        // Create the plot.
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        JFreeChart chart = new JFreeChart(h2d.title(), plot);

        // Configure the axes;
        configureAxes(h2d, chart, 0);

        // Add paint scale color legend.
        createPaintScaleLegend(chart, renderer.getPaintScale(), logScale);

        chart.getLegend().setVisible(false);

        ChartFactory.getChartTheme().apply(chart);

        return chart;
    }

    /**
     * Create the renderer for the color map.
     * 
     * @param dataset
     * @param h2d
     * @param style
     * @return The renderer for the color map.
     */
    static XYBlockRenderer createColorMapRenderer(XYZDataset dataset, IHistogram2D h2d, IPlotterStyle style) {
        // Set the renderer.
        XYBlockRenderer renderer = new XYBlockRenderer();
        renderer.setBlockHeight(h2d.yAxis().binWidth(0));
        renderer.setBlockWidth(h2d.xAxis().binWidth(0));

        // Check if using a log scale.
        boolean logScale = false;
        if (style.zAxisStyle().parameterValue("scale").startsWith("log")) {
            logScale = true;
        }

        // Calculate Z limits from the dataset.
        double[] zlimits = Histogram2DConverter.calculateZLimits(dataset);

        // Use custom rainbow paint scale.
        RainbowPaintScale scale = new RainbowPaintScale(zlimits[0], zlimits[1], zlimits[2], logScale);
        renderer.setPaintScale(scale);

        return renderer;
    }

    // FIXME: Only used in test right now.
    public JFreeChart toBoxPlot(IHistogram2D h2d, IPlotterStyle style) {
        // Create dataset.
        XYZDataset dataset = DatasetConverter.toXYZRangedDataset(h2d);

        // Create plot
        NumberAxis xAxis = new NumberAxis(null);
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        xAxis.setLowerMargin(0.0);
        xAxis.setUpperMargin(0.05);

        // Y axis.
        NumberAxis yAxis = new NumberAxis(null);
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        yAxis.setLowerMargin(0.0);
        yAxis.setUpperMargin(0.05);

        // Set the renderer.
        XYBoxRenderer renderer = new XYBoxRenderer(h2d.xAxis().binWidth(0), h2d.yAxis().binWidth(0));

        // Create the plot.
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        JFreeChart chart = new JFreeChart(h2d.title(), plot);

        // Apply default styles.
        ChartFactory.getChartTheme().apply(chart);

        chart.getLegend().setVisible(false);

        return chart;
    }

    /**
     * Convert 2D histogram to chart that includes a color map plus data
     * suitable for display as scaled boxes.
     * 
     * @param h2d
     * @param style
     */
    public JFreeChart convert(IHistogram2D h2d, IPlotterStyle style) {
        // Create color map chart as primary.
        JFreeChart chart = toColorMap(h2d, style);

        // Add box plot with separate renderer and dataset, which is off by
        // default.
        // addBoxPlot(h2d, chart);

        return chart;
    }

    // FIXME: More work needed here for small bin sizes in which case way too
    // many tic labels are displayed.
    private static void configureAxes(IHistogram2D h2d, JFreeChart chart, double margin) {
        // Setup X axis.
        NumberAxis xAxis = new NumberAxis(chart.getXYPlot().getDomainAxis().getLabel());
        xAxis.setUpperBound(h2d.xAxis().binUpperEdge(h2d.yAxis().bins() - 1));
        xAxis.setUpperMargin(margin);
        xAxis.setTickUnit(new NumberTickUnit(h2d.xAxis().binWidth(0)));
        xAxis.setMinorTickCount(DEFAULT_MINOR_TIC_COUNT);
        xAxis.setMinorTickMarksVisible(true);
        chart.getXYPlot().setDomainAxis(xAxis);

        // Setup Y axis.
        NumberAxis yAxis = new NumberAxis(chart.getXYPlot().getRangeAxis().getLabel());
        yAxis.setUpperBound(h2d.yAxis().binUpperEdge(h2d.yAxis().bins() - 1));
        yAxis.setUpperMargin(margin);
        yAxis.setTickUnit(new NumberTickUnit(h2d.yAxis().binWidth(0)));
        yAxis.setMinorTickCount(DEFAULT_MINOR_TIC_COUNT);
        yAxis.setMinorTickMarksVisible(true);
        chart.getXYPlot().setRangeAxis(yAxis);
    }

    /**
     * Replace existing chart dataset and renderer with a box plot.
     * 
     * @param h2d
     *            The backing histogram.
     * @param chart
     *            The chart into which the box plot will be drawn.
     */
    public static void replaceWithBoxPlot(IHistogram2D h2d, JFreeChart chart) {
        // Create the dataset.
        XYZDataset dataset = DatasetConverter.toXYZRangedDataset(h2d);

        // Create the renderer.
        XYBoxRenderer renderer = new XYBoxRenderer(h2d.xAxis().binWidth(0), h2d.yAxis().binWidth(0));

        // Set the renderer and dataset and make visible.
        chart.getXYPlot().setRenderer(0, renderer);
        chart.getXYPlot().setDataset(0, dataset);
        renderer.setSeriesVisible(0, true);

        // Configure the axes;
        configureAxes(h2d, chart, 0.1);

        // Add padding to right hand side of plot.
        RectangleInsets padding = new RectangleInsets(0, 0, 0, 10);
        chart.setPadding(padding);

        // Turn off pre-existing color map legend.
        chart.getSubtitle(COLOR_SCALE_LEGEND).setVisible(false);

        ChartFactory.getChartTheme().apply(chart);
    }

    static void replaceWithColorMap(IHistogram2D h2d, JFreeChart chart, IPlotterStyle style) {
        // Create the dataset.
        XYZDataset dataset = DatasetConverter.convert(h2d);

        // Setup the renderer.
        XYBlockRenderer renderer = createColorMapRenderer(dataset, h2d, style);
        chart.getXYPlot().setRenderer(renderer);

        // Turn the legend back on.
        chart.getSubtitle(COLOR_SCALE_LEGEND).setVisible(true);
    }

    /**
     * This method creates a legend for the paint scale used by the block
     * renderer.
     * 
     * Inspired by this post:
     * 
     * <a href="http://www.jfree.org/phpBB2/viewtopic.php?f=3&t=29588#p81629">
     * Tooltips not shown in XYZPlot</a>
     * 
     * FIXME: The display of tic labels still needs some work, and the legend
     * gets a bit weird when the log scale is used.
     * 
     * @param chart
     * @param scale
     * @param logScale
     */
    private static void createPaintScaleLegend(JFreeChart chart, PaintScale scale, boolean logScale) {
        NumberAxis legendAxis = null;
        if (logScale) {
            legendAxis = new LogarithmicAxis("scale");
        } else {
            legendAxis = new NumberAxis("scale");
        }
        legendAxis.setAxisLinePaint(Color.white);
        legendAxis.setTickMarkPaint(Color.white);
        legendAxis.setTickLabelFont(new Font("Dialog", 0, 7));
        legendAxis.setRange(new Range(scale.getLowerBound(), scale.getUpperBound()));

        PaintScaleLegend legend = new PaintScaleLegend(scale, legendAxis);
        if (logScale) {
            legend.setSubdivisionCount(50000);
            // TODO: Set tick unit source here to force display of label for the
            // max value of the axis.
            // http://www.jfree.org/phpBB2/viewtopic.php?f=3&t=28597&p=79591&hilit=NumberAxis+tick#p79591
        } else {
            legend.setSubdivisionCount(50);
        }
        legend.setAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        legend.setAxisOffset(5D);
        legend.setMargin(new RectangleInsets(5D, 5D, 5D, 5D));
        legend.setFrame(new BlockBorder(Color.black));
        legend.setPadding(new RectangleInsets(10D, 10D, 10D, 10D));
        legend.setStripWidth(20D);
        legend.setPosition(RectangleEdge.RIGHT);
        chart.addSubtitle(legend);
    }
}