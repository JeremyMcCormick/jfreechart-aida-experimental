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
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.Range;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Histogram2DConverter implements HistogramConverter<IHistogram2D>
{   
    static public final int COLOR_DATA = 0;
    static public final int BOX_DATA = 1;

    public Class<IHistogram2D> convertsType()
    {
        return IHistogram2D.class;
    }

    static double[] calculateZLimits(XYZDataset ds)
    {
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
        return new double[] {zMin, zMax, zLogMin};
    }
       
    // Convert 2D histogram to color map chart.
    public JFreeChart toColorMap(IHistogram2D h2d, IPlotterStyle style)
    {
        // Create dataset.
        XYZDataset dataset = DatasetConverter.convert(h2d);

        // Calculate Z limits from the dataset.
        double[] zlimits = calculateZLimits(dataset);

        // Create plot
        NumberAxis xAxis = new NumberAxis(null);
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        xAxis.setLowerMargin(0.0);
        xAxis.setUpperMargin(0.0);

        // Y axis.
        NumberAxis yAxis = new NumberAxis(null);
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        yAxis.setLowerMargin(0.0);
        yAxis.setUpperMargin(0.0);

        // Set the renderer.
        XYBlockRenderer renderer = new XYBlockRenderer();
        renderer.setBlockHeight(h2d.yAxis().binWidth(0));
        renderer.setBlockWidth(h2d.xAxis().binWidth(0));

        // Old JFreeChart class
        // PaintScale scale = new GrayPaintScale(0., h2d.maxBinHeight());

        // Check if using a log scale.
        boolean logScale = false;
        if (style.zAxisStyle().parameterValue("scale").startsWith("log")) {
            logScale = true;
        }

        // Use custom rainbow paint scale.
        RainbowPaintScale scale = new RainbowPaintScale(zlimits[0], zlimits[1], zlimits[2], logScale);
        renderer.setPaintScale(scale);

        // Create the plot.
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        JFreeChart chart = new JFreeChart(h2d.title(), plot);

        // Add paint scale color legend.
        createPaintScaleLegend(chart, scale, logScale);

        // Apply default styles.
        ChartFactory.getChartTheme().apply(chart);
        
        chart.getLegend().setVisible(false);

        return chart;
    }
    
    // FIXME: not used in above method so some code is duplicated 
    static final XYItemRenderer createColorMapRenderer(JFreeChart chart, IHistogram2D h2d, IPlotterStyle style)
    {
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
        double[] zlimits = Histogram2DConverter.calculateZLimits((XYZDataset)chart.getXYPlot().getDataset(Histogram2DConverter.COLOR_DATA));

        // Use custom rainbow paint scale.
        RainbowPaintScale scale = new RainbowPaintScale(zlimits[0], zlimits[1], zlimits[2], logScale);
        renderer.setPaintScale(scale);
        
        return renderer;
    }
    
    // FIXME: Only used in test right now.       
    public JFreeChart toBoxPlot(IHistogram2D h2d, IPlotterStyle style)
    {
        // Create dataset.
        XYZDataset dataset = DatasetConverter.toXYZRangedDataset(h2d);

        // Create plot
        NumberAxis xAxis = new NumberAxis(null);
        xAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        xAxis.setLowerMargin(0.0);
        xAxis.setUpperMargin(0.0);

        // Y axis.
        NumberAxis yAxis = new NumberAxis(null);
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        yAxis.setLowerMargin(0.0);
        yAxis.setUpperMargin(0.0);

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

    // Convert 2D histogram to color map chart.
    public JFreeChart convert(IHistogram2D h2d, IPlotterStyle style)
    {        
        // Create color map chart as primary.
        JFreeChart chart = toColorMap(h2d, style);
        
        // Add box plot with separate renderer and dataset, which is off by default.
        addBoxPlot(h2d, chart);
        
        return chart;
    }
    
    private void addBoxPlot(IHistogram2D h2d, JFreeChart chart)
    {
        XYZDataset dataset = DatasetConverter.toXYZRangedDataset(h2d);
        XYBoxRenderer renderer = new XYBoxRenderer(h2d.xAxis().binWidth(0), h2d.yAxis().binWidth(0));
        chart.getXYPlot().setRenderer(BOX_DATA, renderer);
        chart.getXYPlot().setDataset(BOX_DATA, dataset);
        renderer.setSeriesVisible(0, false);
    }

    //
    // Inspired by this thread:
    //
    // http://www.jfree.org/phpBB2/viewtopic.php?f=3&t=29588
    //
    // Post by badera => Thu Dec 17, 2009 12:32 pm
    //
    private static void createPaintScaleLegend(JFreeChart chart, PaintScale scale, boolean logScale)
    {
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
            // TODO: Set tick unit source here to force display of label for the max value of the axis.
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