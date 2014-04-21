package hep.aida.jfree.converter;

import hep.aida.IHistogram2D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.dataset.Bounds;
import hep.aida.jfree.dataset.Histogram2DAdapter;
import hep.aida.jfree.renderer.AbstractPaintScale;
import hep.aida.jfree.renderer.CustomPaintScale;
import hep.aida.jfree.renderer.GreyPaintScale;
import hep.aida.jfree.renderer.RainbowPaintScale;
import hep.aida.jfree.renderer.XYBoxRenderer;

import java.awt.Color;
import java.awt.Font;

import org.freehep.swing.ColorConverter;
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
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
// FIXME: This class does not support variable bin sizes.
public class Histogram2DConverter implements Converter<IHistogram2D> {

    public static final int COLOR_DATA = 0;
    public static final int COLOR_SCALE_LEGEND = 1;
    
    // copied from JASHist2DHistogramStyle
    public static final String COLORMAP_RAINBOW = "COLORMAP_RAINBOW";
    public static final String COLORMAP_GRAYSCALE = "COLORMAP_GRAYSCALE";
    public static final String COLORMAP_USERDEFINED = "COLORMAP_USERDEFINED";

    public Class<IHistogram2D> convertsType() {
        return IHistogram2D.class;
    }
    
    /**
     * Convert an AIDA 2D histogram to a <code>JFreeChart</code>.
     * 
     * @param histogram
     * @param style
     */
    public JFreeChart convert(IHistogram2D histogram, IPlotterStyle style) {
        
        // Create the Dataset adapter.
        Histogram2DAdapter adapter = new Histogram2DAdapter(histogram);
        
        // Get display style.
        String hist2DStyle = "colorMap";
        try {
            hist2DStyle = style.parameterValue("hist2DStyle");
        } catch (Exception e) {
            
        }
        
        JFreeChart chart = null;
                
        if (hist2DStyle.equals("colorMap")) {
            // color map
            chart = createColorMap(adapter, style);
        } else if (hist2DStyle.equals("box")) {
            // box plot
            chart = this.createBoxPlot(adapter, style);
        } else if (hist2DStyle.equals("ellipse")) {
            // ellipse => not implemented!
            throw new IllegalArgumentException("The ellipse style is not implemented yet.");
        } else {
            throw new IllegalArgumentException("Unknown hist2DStyle: " + hist2DStyle);
        }

        return chart;
    }

    /**
     * Convert 2D histogram to color map chart.
     * 
     * @param h2d
     * @param style
     * @return
     */
    static JFreeChart createColorMap(Histogram2DAdapter adapter, IPlotterStyle style) {
                        
        // Check if using a log scale.
        boolean logScale = false;
        if (style.zAxisStyle().parameterValue("scale").startsWith("log")) {
            logScale = true;
        }

        // Setup the renderer.
        XYBlockRenderer renderer = createColorMapRenderer(adapter, style);

        // Create the plot.
        XYPlot plot = new XYPlot(adapter, null, null, renderer);
        JFreeChart chart = new JFreeChart(adapter.getHistogram().title(), plot);

        // Configure the axes;
        configureAxes(adapter.getHistogram(), chart, 0);

        // Add paint scale color legend.
        createPaintScaleLegend(chart, renderer.getPaintScale(), logScale);
        
        // Turn off the default legend.
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
    static XYBlockRenderer createColorMapRenderer(Histogram2DAdapter adapter, IPlotterStyle style) {
        
        IHistogram2D histogram = adapter.getHistogram();
        
        // Setup the Renderer.
        XYBlockRenderer renderer = new XYBlockRenderer();
        renderer.setBlockHeight(histogram.yAxis().binWidth(0));
        renderer.setBlockWidth(histogram.xAxis().binWidth(0));
        
        // Calculate the lower and upper Z value bounds for the Dataset.
        Bounds bounds = adapter.recomputeZBounds();

        double minimum = 0.0;
        double maximum = 1.0;
        if (bounds.isValid()) {
            minimum = bounds.getMinimum();
            maximum = bounds.getMaximum();
        }
        
        // Get the color map style if it exists.
        String colorMapScheme = null;
        try {
            colorMapScheme = style.dataStyle().fillStyle().parameterValue("colorMapScheme");
        } catch (IllegalArgumentException e) {            
        }
        if (colorMapScheme == null || colorMapScheme.equals("none"))
            colorMapScheme = COLORMAP_RAINBOW;
                
        // Create the PaintScale based on the color map setting.
        PaintScale paintScale = null;
        if (colorMapScheme.equals(COLORMAP_RAINBOW)) {
            paintScale = new RainbowPaintScale(minimum, maximum);
        } else if (colorMapScheme.equals(COLORMAP_GRAYSCALE)) {
            paintScale = new GreyPaintScale(minimum, maximum);
        } else if (colorMapScheme.equals(COLORMAP_USERDEFINED)) {
            try {
                Color startColor = ColorConverter.get(style.dataStyle().fillStyle().parameterValue("startColor"));
                Color endColor = ColorConverter.get(style.dataStyle().fillStyle().parameterValue("endColor"));
                paintScale = new CustomPaintScale(startColor, endColor, minimum, maximum);
            } catch (Exception e) {                
                throw new RuntimeException(e);
            }
        }
        
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
    
    /**
     * Create a data legend for the <code>PaintScale</code> used by the <code>XYBlockRenderer</code>.
     * 
     * It is based on ideas from
     * <a href="http://www.jfree.org/phpBB2/viewtopic.php?f=3&t=29588#p81629">this post</a>.
     * 
     * FIXME: The display of the tick labels still needs some work, and the legend looks weird when the 
     * log scale is used.
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

    /**
     * Create a box plot to display 2D histogram data.
     * 
     * @param adapter
     * @param style
     * @return
     */
    public JFreeChart createBoxPlot(Histogram2DAdapter adapter, IPlotterStyle style) {
                
        IHistogram2D histogram = adapter.getHistogram();

        // Setup the renderer.
        XYBoxRenderer renderer = new XYBoxRenderer(histogram.xAxis().binWidth(0), histogram.yAxis().binWidth(0));
        if (histogram.entries() > 0)
            renderer.setMaximumValue(adapter.getZBounds(0).getMaximum());

        // Create the plot.
        XYPlot plot = new XYPlot(adapter, null, null, renderer);        
        JFreeChart chart = new JFreeChart(adapter.getHistogram().title(), plot);
        configureAxes(histogram, chart, 0);

        // Apply default styles.
        ChartFactory.getChartTheme().apply(chart);

        // Turn off the default chart legend.
        chart.getLegend().setVisible(false);

        return chart;
    }

    private static void configureAxes(IHistogram2D h2d, JFreeChart chart, double margin) {

        String[] labels = ConverterUtil.getAxisLabels(h2d);
        
        NumberAxis xAxis = new NumberAxis(labels[0]);
        NumberAxis yAxis = new NumberAxis(labels[1]);
        
        chart.getXYPlot().setDomainAxis(xAxis);
        chart.getXYPlot().setRangeAxis(yAxis);
    }

}