package hep.aida.jfree.converter;

import hep.aida.IHistogram2D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.dataset.Bounds;
import hep.aida.jfree.dataset.Histogram2DAdapter;
import hep.aida.jfree.plotter.style.util.StyleConstants;
import hep.aida.jfree.renderer.AbstractPaintScale;
import hep.aida.jfree.renderer.CustomPaintScale;
import hep.aida.jfree.renderer.GreyPaintScale;
import hep.aida.jfree.renderer.RainbowPaintScale;
import hep.aida.jfree.renderer.XYVariableBinWidthBlockRenderer;
import hep.aida.jfree.renderer.XYVariableBinWidthBoxRenderer;

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

    //public static final int COLOR_DATA = 0;
    public static final int COLOR_SCALE_LEGEND = 1;
        
    // These are changed from the AIDA values to be more user friendly. 
    public static final String COLORMAP_RAINBOW = "rainbow";
    public static final String COLORMAP_GRAYSCALE = "grayscale";
    public static final String COLORMAP_USERDEFINED = "userdefined";
    
    public Class<IHistogram2D> convertsType() {
        return IHistogram2D.class;
    }
    
    /**
     * Convert an AIDA 2D histogram to a <code>JFreeChart</code>.
     * 
     * @param histogram
     * @param style
     */
    public JFreeChart convert(JFreeChart chart, IHistogram2D histogram, IPlotterStyle style) {
        
        // Create the Dataset adapter.
        Histogram2DAdapter adapter = new Histogram2DAdapter(histogram);
        
        // Get display style with default as color map.
        String hist2DStyle = null;
        try {
            hist2DStyle = style.parameterValue(StyleConstants.HIST2DSTYLE);
        } catch (Exception e) {            
        }        
        if (hist2DStyle == null)
            hist2DStyle = StyleConstants.COLOR_MAP;
        
        JFreeChart newChart = null;
                
        if (hist2DStyle.equals(StyleConstants.COLOR_MAP)) {
            // color map
            newChart = createColorMap(adapter, style);
        } else if (hist2DStyle.equals(StyleConstants.BOX_PLOT)) {
            // box plot
            //System.out.println("creating box plot");
            newChart = createBoxPlot(adapter, style);
        } else if (hist2DStyle.equals(StyleConstants.ELLIPSE_PLOT)) {
            // ellipse style is not implemented yet!
            throw new IllegalArgumentException("The ellipse style is not implemented yet.");
        } else {
            throw new IllegalArgumentException("Unknown hist2DStyle: " + hist2DStyle);
        }

        return newChart;
    }
    
    /**
     * Create a box plot to display 2D histogram data.
     * 
     * @param adapter
     * @param style
     * @return
     */
    JFreeChart createBoxPlot(Histogram2DAdapter adapter, IPlotterStyle style) {
                
        IHistogram2D histogram = adapter.getHistogram();

        // Setup the renderer.
        XYVariableBinWidthBoxRenderer renderer = new XYVariableBinWidthBoxRenderer();
        if (histogram.entries() > 0) {
            adapter.recomputeZBounds();
            renderer.setMaximumValue(adapter.getZBounds(0).getMaximum());
        }
                                   
        // Create the plot.        
        XYPlot plot = new XYPlot(adapter, null, null, renderer);
        configureAxes(plot, histogram);
        JFreeChart chart = new JFreeChart(adapter.getHistogram().title(), plot);        
        //chart.getXYPlot().configureDomainAxes();
        //chart.getXYPlot().configureRangeAxes();
                
        // Apply default styles.
        ChartFactory.getChartTheme().apply(chart);

        // Turn off the default chart legend unless it is activated by style code later.
        chart.getLegend().setVisible(false);

        chart.fireChartChanged();
        
        return chart;
    }

    /**
     * Convert 2D histogram to color map chart.
     * 
     * @param h2d
     * @param style
     * @return
     */
    JFreeChart createColorMap(Histogram2DAdapter adapter, IPlotterStyle style) {
        
        IHistogram2D histogram = adapter.getHistogram();
        
        // Check if using a log scale.
        boolean logScale = false;
        if (style.zAxisStyle().parameterValue(StyleConstants.SCALE).startsWith(StyleConstants.LOG)) {
            logScale = true;
        }

        // Setup the renderer.
        XYVariableBinWidthBlockRenderer renderer = createColorMapRenderer(adapter, style);

        // Create the plot.
        XYPlot plot = new XYPlot(adapter, null, null, renderer);
        JFreeChart chart = new JFreeChart(adapter.getHistogram().title(), plot);

        configureAxes(plot, histogram);
        
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
    XYVariableBinWidthBlockRenderer createColorMapRenderer(Histogram2DAdapter adapter, IPlotterStyle style) {
                
        // Setup the renderer.
        XYVariableBinWidthBlockRenderer renderer = new XYVariableBinWidthBlockRenderer();
        
        // Calculate the lower and upper Z value bounds for the Dataset.
        Bounds bounds = adapter.recomputeZBounds();

        double minimum = 0.0;
        double maximum = 1.0;
        if (bounds.isValid()) {
            // FIXME: Doesn't use lower bound right now!!!
            //minimum = bounds.getMinimum();
            maximum = bounds.getMaximum();
        }
        
        // Get the color map style if it exists.
        String colorMapScheme = null;
        try {
            colorMapScheme = style.dataStyle().fillStyle().parameterValue(StyleConstants.COLOR_MAP_SCHEME);
        } catch (IllegalArgumentException e) {            
        }
        if (colorMapScheme == null || colorMapScheme.equals("none"))
            colorMapScheme = COLORMAP_RAINBOW;
                
        // Create the PaintScale based on the color map setting.
        AbstractPaintScale paintScale = null;
        if (colorMapScheme.equals(COLORMAP_RAINBOW)) {
            paintScale = new RainbowPaintScale(minimum, maximum);
        } else if (colorMapScheme.equals(COLORMAP_GRAYSCALE)) {
            paintScale = new GreyPaintScale(minimum, maximum);
        } else if (colorMapScheme.equals(COLORMAP_USERDEFINED)) {
            try {
                Color startColor = ColorConverter.get(style.dataStyle().fillStyle().parameterValue(StyleConstants.START_COLOR));
                Color endColor = ColorConverter.get(style.dataStyle().fillStyle().parameterValue(StyleConstants.END_COLOR));
                paintScale = new CustomPaintScale(startColor, endColor, 0, maximum);
            } catch (Exception e) {                
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Unknown color map scheme: " + colorMapScheme);
        }
        
        // Enable log scale if selected in style parameter.
        String logScale = null;
        try {
            logScale = style.zAxisStyle().parameterValue(StyleConstants.SCALE);
        } catch (IllegalArgumentException e) {            
        }               
        if (logScale != null) {
            if (logScale.startsWith("log")) {
                paintScale.setLogScale();
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
    private void createPaintScaleLegend(JFreeChart chart, PaintScale scale, boolean logScale) {
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
        
    private void configureAxes(XYPlot plot, IHistogram2D histogram) {
        
        String[] labels = ConverterUtil.getAxisLabels(histogram);
        
        // Configure the x axis.
        NumberAxis xAxis = new NumberAxis(labels[0]);
        xAxis.setUpperMargin(0.2);
        xAxis.setLowerMargin(0.2);
        double xAxisSize = Math.abs(histogram.xAxis().lowerEdge()) + Math.abs(histogram.xAxis().upperEdge());
        if (xAxisSize <= 1.0) {
            xAxis.setUpperMargin(0.5);
            xAxis.setLowerMargin(0.5);
        }
        xAxis.setDefaultAutoRange(new Range(histogram.xAxis().lowerEdge(), histogram.xAxis().upperEdge()));
        
        // Configure the y axis.
        NumberAxis yAxis = new NumberAxis(labels[0]);
        yAxis.setUpperMargin(0.2);
        yAxis.setLowerMargin(0.2);
        double yAxisSize = Math.abs(histogram.yAxis().lowerEdge()) + Math.abs(histogram.yAxis().upperEdge());
        if (yAxisSize <= 1.0) {
            yAxis.setUpperMargin(0.5);
            yAxis.setLowerMargin(0.5);
        }
        yAxis.setDefaultAutoRange(new Range(histogram.yAxis().lowerEdge(), histogram.yAxis().upperEdge()));  
        
        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);
    }
}