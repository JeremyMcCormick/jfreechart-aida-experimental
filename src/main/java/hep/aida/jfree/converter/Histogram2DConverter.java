package hep.aida.jfree.converter;

import hep.aida.IHistogram2D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.dataset.DatasetConverter;
import hep.aida.jfree.renderer.AbstractPaintScale;
import hep.aida.jfree.renderer.GreyPaintScale;
import hep.aida.jfree.renderer.RainbowPaintScale;
import hep.aida.jfree.renderer.XYBoxRenderer;

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
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.Range;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Histogram2DConverter implements Converter<IHistogram2D> {

    static public final int COLOR_DATA = 0;
    static public final int COLOR_SCALE_LEGEND = 1;

    public Class<IHistogram2D> convertsType() {
        return IHistogram2D.class;
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
        
        // Check if using a log scale.
        boolean logScale = false;
        if (style.zAxisStyle().parameterValue("scale").startsWith("log")) {
            logScale = true;
        }

        // Setup the renderer.
        XYBlockRenderer renderer = createColorMapRenderer(dataset, h2d, style);

        // Create the plot.
        XYPlot plot = new XYPlot(dataset, null, null, renderer);
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
     * This is the primary method for creating and configuring a Renderer
     * that will display XYZ data as a color map.
     * 
     * @param dataset
     * @param h2d
     * @param style
     * @return The renderer for the color map.
     */
    static XYBlockRenderer createColorMapRenderer(XYZDataset dataset, IHistogram2D h2d, IPlotterStyle style) {
                
        // Setup the Renderer.
        // FIXME: Assumes all bin widths are the same!
        XYBlockRenderer renderer = new XYBlockRenderer();
        renderer.setBlockHeight(h2d.yAxis().binWidth(0));
        renderer.setBlockWidth(h2d.xAxis().binWidth(0));
        
        // Calculate the lower and upper Z value bounds for the Dataset.
        double[] zlimits = calculateZBounds(dataset);         
        
        // Get the color map style if it exists.
        String colorMapScheme = null;
        try {
            colorMapScheme = style.dataStyle().fillStyle().parameterValue("colorMapScheme");
        } catch (IllegalArgumentException e) {            
        }
        if (colorMapScheme == null || colorMapScheme.equals("none"))
            colorMapScheme = "rainbow";
                
        // Create the PaintScale based on the color map setting.
        PaintScale paintScale = null;
        if (colorMapScheme.equals("rainbow"))
            paintScale = new RainbowPaintScale(zlimits[0], zlimits[1]);
        else if (colorMapScheme.equals("greyscale"))
            paintScale = new GreyPaintScale(zlimits[0], zlimits[1]);
        
        // Set log scale if selected.q
        String logScale = null;
        try {
            logScale = style.zAxisStyle().parameterValue("scale");
        } catch (IllegalArgumentException e) {            
        }               
        if (logScale != null) {
            if (logScale.startsWith("log")) {
                ((AbstractPaintScale)paintScale).setLogScale();
            }
        }
        
        // Register the PaintScale with the Renderer.
        renderer.setPaintScale(paintScale);
        
        return renderer;
    }

    public JFreeChart toBoxPlot(IHistogram2D h2d, IPlotterStyle style) {
        // Create dataset.
        XYZDataset dataset = DatasetConverter.toXYZRangedDataset(h2d);

        // Create plot
        NumberAxis xAxis = new NumberAxis(null);

        // Y axis.
        NumberAxis yAxis = new NumberAxis(null);

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

    private static void configureAxes(IHistogram2D h2d, JFreeChart chart, double margin) {

        String[] labels = ConverterUtil.getAxisLabels(h2d);
        
        NumberAxis xAxis = new NumberAxis(labels[0]);
        NumberAxis yAxis = new NumberAxis(labels[1]);
        
        chart.getXYPlot().setDomainAxis(xAxis);
        chart.getXYPlot().setRangeAxis(yAxis);
    }

    /**
     * Replace existing chart dataset and renderer with a box plot.
     * @param h2d The backing histogram.
     * @param chart The chart into which the box plot will be drawn.
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

    public static void replaceWithColorMap(IHistogram2D h2d, JFreeChart chart, IPlotterStyle style) {
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
     * Inspired by 
     * <a href="http://www.jfree.org/phpBB2/viewtopic.php?f=3&t=29588#p81629">this post</a>
     * 
     * FIXME: The display of the tick labels still needs some work, 
     * and the legend looks weird when the log scale is used.
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
    
    public static double[] calculateZBounds(XYZDataset ds) {
        double zmin = Double.POSITIVE_INFINITY;
        double zmax = Double.NEGATIVE_INFINITY;
        for (int i = 0, n = ds.getItemCount(0); i < n; i++) {
            double value = ds.getZValue(0, i);
            //System.out.println("value: " + value);
            if (value != 0) {
                if (value < zmin)
                    zmin = value;
                if (value > zmax)
                    zmax = value;
            }
        }
        if (zmin == Double.POSITIVE_INFINITY)
            zmin = 0;
        if (zmax == Double.NEGATIVE_INFINITY)
            zmax = 1.0;
        //System.out.println("calculateZBounds: " + zmin + "," + zmax);
        //if (true) throw new RuntimeException("bork");
        return new double[] {zmin, zmax};
    }
}