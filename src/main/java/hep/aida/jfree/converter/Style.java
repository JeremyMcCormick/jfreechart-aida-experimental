package hep.aida.jfree.converter;

import static hep.aida.ref.plotter.Style.ERRORBAR_DECORATION;
import hep.aida.IAxisStyle;
import hep.aida.IBaseHistogram;
import hep.aida.IDataStyle;
import hep.aida.IFillStyle;
import hep.aida.IHistogram1D;
import hep.aida.ILineStyle;
import hep.aida.IMarkerStyle;
import hep.aida.IPlotterStyle;
import hep.aida.ref.plotter.BaseStyle;
import hep.aida.ref.plotter.PlotterFontUtil;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.freehep.swing.ColorConverter;
import org.freehep.swing.ColorConverter.ColorConversionException;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.util.ShapeUtilities;

/**
 * This class applies styles to JFreeChart objects.
 * 
 * See this method for how to apply styles from AIDA using the JAS3 plotter:
 * 
 * freehep-jaida: 
 * hep.aida.ref.plotter.PlotterRegion.applyStyle(JASHistData jasHistData, IPlotterStyle style);
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public final class Style
{
    
    private Style()
    {
    }

    // Set labels on chart axes based on plot annotations.
    public static void setAxisLabels(IBaseHistogram hist, XYPlot plot)
    {
        if (hist.annotation().hasKey("xAxisLabel")) {
            plot.getDomainAxis().setLabel(hist.annotation().value("xAxisLabel"));
        }
        if (hist.annotation().hasKey("yAxisLabel")) {
            plot.getRangeAxis().setLabel(hist.annotation().value("yAxisLabel"));
        }
    }

    public static void applyStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style)
    {

        // System.out.println("applying style to chart: " + chart.getTitle());
        // System.out.println("chart type: " + chart.getXYPlot().getClass().getCanonicalName());
        // for (int i=0; i<chart.getXYPlot().getRendererCount(); i++) {
        // System.out.println("renderer["+i+"]: "+chart.getXYPlot().getRenderer(i).getClass().getCanonicalName());
        // }
        // System.out.println();

        // get the plot
        XYPlot plot = chart.getXYPlot();
        
        // Set log scale on axes if selected.
        setLogAxis(chart, style);

        // Set position of axes.
        setAxisLocation(chart, style);

        // Set axis labels on the plot
        setAxisLabels(hist, chart.getXYPlot());
        
        // X Axis style such as label fonts.
        setAxisStyle(plot.getDomainAxis(), style.xAxisStyle());

        // Y Axis style such as label fonts.
        setAxisStyle(plot.getRangeAxis(), style.yAxisStyle());
        
        // title style
        setTitleStyle(chart, style);

        // background color
        setBackgroundColor(plot, style);

        // Set data styling or turn off if invisible.
        if (isDataVisible(style)) {
        
            // data fill style
            setDataFillStyle(chart, style);

            // data outline style
            setDataOutlineStyle(plot, style);

            // data line style (histogram bars)
            setDataLineStyle(chart, style);
            
            // data marker style
            setDataMarkerStyle(chart, hist, style);
        } else {
            makeDataInvisible(chart);
        }

        // Set error styling or turn off if invisible.
        if (areErrorsVisible(style)) {
            setErrorBarStyle(chart, style);
        } else {
            makeErrorsInvisible(chart);
        }

        // foreground color => What is this supposed to paint? Which components? Overrides more granular styles?

        // data area colors

        // data area border type

        // 1D histograms (a lot of stuff!)

        // 2D histograms (ditto)

        // color map

        // functions
    }

    /**
     * Sets log style on X and Y axes.  This MUST come before other axis styling, 
     * because this sets a new axis object on the plot.
     * @param chart The chart to style.
     * @param style The AIDA plotter style.
     */
    private static void setLogAxis(JFreeChart chart, IPlotterStyle style)
    {
        setLogAxis(chart.getXYPlot(), style.xAxisStyle(), true);
        setLogAxis(chart.getXYPlot(), style.yAxisStyle(), false);
    }

    /**
     * Set the location of the axis. Currently only handles placement of Y axis on left or right side. AIDA
     * does not appear to support placing the X axis on the top of the plot. (JFreeChart does support this.)
     * 
     * @param chart The chart to set axis location.
     * @param style The AIDA plot style.
     */
    private static void setAxisLocation(JFreeChart chart, IPlotterStyle style)
    {
        IAxisStyle yAxisStyle = style.yAxisStyle();
        String yAxisValue = yAxisStyle.parameterValue("yAxis");
        if (yAxisValue != null) {
            try {
                AxisLocation axisLocation = (yAxisValue.equalsIgnoreCase("Y1")) ? AxisLocation.BOTTOM_OR_RIGHT : AxisLocation.BOTTOM_OR_LEFT;
                chart.getXYPlot().setDomainAxisLocation(axisLocation);
            } catch (Exception cce) {
                throw new RuntimeException(cce);
            }
        }

        // TODO: Can AIDA put X axis at top of plot?
    }

    /**
     * Set log axis if selected.
     * 
     * @param plot The plot with the axes.
     * @param axisStyle The AIDA axis style settings.
     * @param domain True if axis is domain; false if range.
     */
    private static void setLogAxis(XYPlot plot, IAxisStyle axisStyle, boolean domain)
    {
        String scale = axisStyle.scaling();
        if (scale != null) {
            if (scale.startsWith("log")) {
                LogAxis axis = new LogAxis("");
                axis.setBase(10);
                axis.setSmallestValue(0.01);
                if (domain) {
                    plot.setDomainAxis(axis);
                } else {
                    plot.setRangeAxis(axis);
                }
            }
        }
    }

    private static void setBackgroundColor(XYPlot plot, IPlotterStyle style)
    {
        String backgroundColor = style.regionBoxStyle().backgroundStyle().color();
        if (backgroundColor != null) {
            try {
                Color color = ColorConverter.get(backgroundColor);
                plot.setBackgroundPaint(color);
            } catch (Exception cce) {
                throw new RuntimeException(cce);
            }
        }
    }

    private static void setTitleStyle(JFreeChart chart, IPlotterStyle style)
    {
        Font titleFont = PlotterFontUtil.getFont(style.titleStyle().textStyle());
        chart.getTitle().setFont(titleFont);

        String colorStr = style.titleStyle().textStyle().color();
        if (colorStr != null) {
            try {
                Color titleColor = ColorConverter.get(colorStr);
                titleColor = getTransparentColor(titleColor, style.titleStyle().textStyle().opacity());
                chart.getTitle().setPaint(titleColor);
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        }
    }

    private static void setDataFillStyle(JFreeChart chart, IPlotterStyle style)
    {
        XYPlot plot = chart.getXYPlot();
        IDataStyle dataStyle = style.dataStyle();
        IFillStyle dataFillStyle = dataStyle.fillStyle();
        if (dataFillStyle.isVisible()) {
            Color color = null;
            try {
                color = ColorConverter.get(dataFillStyle.color());
            } catch (ColorConversionException x) {
            } catch (NullPointerException x) {
            }
            plot.getRenderer().setSeriesPaint(0, color);
        } else {
            plot.getRenderer().setSeriesPaint(0, chart.getBackgroundPaint());
        }
    }

    private static void setDataOutlineStyle(XYPlot plot, IPlotterStyle style)
    {
        ILineStyle lineStyle = style.dataStyle().outlineStyle();
        if (lineStyle.isVisible()) {
            Color color = Color.black;
            try {
                color = ColorConverter.get(lineStyle.color());
            } catch (ColorConversionException x) {
            } catch (NullPointerException x) {
            }
            if (plot.getRenderer() instanceof XYBarRenderer) {
                ((XYBarRenderer) plot.getRenderer()).setDrawBarOutline(true);
                plot.getRenderer().setSeriesOutlinePaint(0, color);
            } else if (plot.getRenderer() instanceof XYStepRenderer) {
                ((XYStepRenderer) plot.getRenderer()).setSeriesPaint(0, color);
            }
        } else {
            plot.getRenderer().setSeriesVisible(0, false);
        }
    }

    private static void setErrorBarStyle(JFreeChart chart, IPlotterStyle style)
    {

        // Assumes errors are drawn by the 2nd renderer in the plot.
        XYItemRenderer renderer = chart.getXYPlot().getRenderer(1);

        // It looks like there are no errors defined so we bail!
        if (renderer == null) {
            return;
        }

        // Style for error bars.
        ILineStyle errorStyle = style.dataStyle().errorBarStyle();

        // Set invisible if selected.
        if (!errorStyle.isVisible())
            renderer.setSeriesVisible(0, false);

        // Set the line color.
        Color errorColor = Style.colorFromLineStyle(errorStyle);        
        if (errorColor != null)
            renderer.setSeriesPaint(0, errorColor);
        
        // Create the stroke from the error line style.
        Stroke stroke = Style.strokeFromLineStyle(errorStyle);

        // Set the stroke on the renderer.
        renderer.setSeriesStroke(0, stroke);
        
        // Error bar decoration.
        // FIXME: Right now this just handles turning caps on or off.  Proper handling
        //        of this setting, which is a percentage of the bin width, will require
        //        code to figure out the width of the bin in Java 2D units.  This will
        //        also not work for histograms with variable X bin sizes.
        String decoration = errorStyle.parameterValue(ERRORBAR_DECORATION);
        //System.out.println("decoration = " + decoration);
        if (decoration != null) {
            float decVal = Float.parseFloat(decoration);
            if (decVal <= 0.f) {
                if (renderer instanceof XYErrorRenderer) {
                    //System.out.println("setting cap length to zero");
                    ((XYErrorRenderer)renderer).setCapLength(0.);
                }
            } else {
                // TODO: Correctly set error bar width based on style setting.
            }
        }
    }

    
      private static void setDataLineStyle(JFreeChart chart, IPlotterStyle style) 
      { 
          ILineStyle lineStyle = style.dataStyle().lineStyle();
          XYItemRenderer renderer = chart.getXYPlot().getRenderer(0);
          if (style.isVisible() == false) {
              renderer.setSeriesVisible(0, false);
          } 
          
          // Color of data lines.
          Color color = Style.colorFromLineStyle(lineStyle);
          if (color != null)
              renderer.setSeriesPaint(0, color);
          
          // Stroke of data lines.
          Stroke stroke = Style.strokeFromLineStyle(lineStyle);
          if (stroke != null)
              renderer.setSeriesStroke(0, stroke);
      }
     

    private static Color getTransparentColor(Color c, double alpha)
    {
        if (alpha == -1 || alpha < 0 || alpha > 1)
            return c;
        int t = (int) (255 * alpha);
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), t);
    }

    private static void setAxisStyle(ValueAxis axis, IAxisStyle axisStyle)
    {

        String xAxisLabel = axisStyle.label();
        boolean setlabel = xAxisLabel != null && ((BaseStyle) axisStyle).isParameterSet(hep.aida.ref.plotter.Style.AXIS_LABEL);
        if (setlabel)
            axis.setLabel(xAxisLabel);

        // boolean xAllowZeroSuppression =
        // Boolean.valueOf(axisStyle.parameterValue("allowZeroSuppression")).booleanValue();
        // axis.setAllowSuppressedZero( xAllowZeroSuppression );

        // axis label font
        axis.setLabelFont(PlotterFontUtil.getFont(axisStyle.labelStyle()));

        // axis label color
        String axisLabelColor = axisStyle.labelStyle().color();
        if (axisLabelColor != null) {
            try {
                Color color = ColorConverter.get(axisLabelColor);
                color = getTransparentColor(color, axisStyle.labelStyle().opacity());
                axis.setLabelPaint(color);
            } catch (Exception cce) {
                throw new RuntimeException(cce);
            }

            // vertical label
            // boolean verticalLabel =
            // Boolean.valueOf(axisStyle.parameterValue(Style.AXIS_VERTICAL_LABEL)).booleanValue();
            // label.setRotated(verticalLabel);
        }

        // tick labels color
        String axisTickLabelColor = axisStyle.tickLabelStyle().color();
        if (axisTickLabelColor != null) {
            try {
                Color color = ColorConverter.get(axisTickLabelColor);
                color = getTransparentColor(color, axisStyle.tickLabelStyle().opacity());
                axis.setTickLabelPaint(color);
            } catch (Exception cce) {
                throw new RuntimeException(cce);
            }
        }

        // tick labels font
        axis.setTickLabelFont(PlotterFontUtil.getFont(axisStyle.tickLabelStyle()));

        // axis line color
        String axisLineColor = axisStyle.lineStyle().color();
        if (axisLineColor != null) {
            try {
                Color color = ColorConverter.get(axisLineColor);
                color = getTransparentColor(color, axisStyle.lineStyle().opacity());
                axis.setAxisLinePaint(color);
            } catch (Exception cce) {
                throw new RuntimeException(cce);
            }
        }

        // axis line sidth
        float axisLineWidth = lineThickness(axisStyle.lineStyle().thickness());
        if (axisLineWidth >= 0) {
            try {
                Stroke stroke = new BasicStroke(axisLineWidth);
                axis.setAxisLineStroke(stroke);
            } catch (Exception cce) {
                throw new RuntimeException(cce);
            }
        }
    }
    
    private static void setDataMarkerStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style)
    {
        IMarkerStyle markerStyle = style.dataStyle().markerStyle();
        if (markerStyle.isVisible()) {
            // Marker for 1D histogram.
            if (hist instanceof IHistogram1D) {
                Shape shape = getMarkerShape(markerStyle.shape(), markerStyle.size());
                XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false, true);
                renderer.setSeriesShape(0, shape);       
                XYDataset ds = Dataset.convertToPoints((IHistogram1D)hist);
                chart.getXYPlot().setDataset(2, ds);
                chart.getXYPlot().setRenderer(2, renderer);
            }
        }
    }
    
    private static boolean isDataVisible(IPlotterStyle style) 
    {
        boolean visible = true;
        if (!style.dataStyle().isVisible()) {
            visible = false;
        }
        return visible;
    }
    
    private static boolean areErrorsVisible(IPlotterStyle style)
    {
        boolean visible = true;
        if (!style.dataStyle().errorBarStyle().isVisible()) {
            visible = false;
        } 
        return visible;
    }
       
    private static void makeDataInvisible(JFreeChart chart)
    {
        chart.getXYPlot().getRenderer(0).setSeriesVisible(0, false);
    }
    
    private static void makeErrorsInvisible(JFreeChart chart)
    {
        chart.getXYPlot().getRenderer(1).setSeriesVisible(0, false);
    }

    private static void setHistogram1DStyle(XYPlot plot, IPlotterStyle style)
    {

        IDataStyle dataStyle = style.dataStyle();
        boolean vis = dataStyle.fillStyle().isVisible();
        boolean set = ((BaseStyle) dataStyle.fillStyle()).isParameterSet("isVisible");

        if (!set) {
            plot.setBackgroundPaint(null);
        } else {

        }

        //
        // Huge amount of possible styles for 1D histograms below here !!!! 
        //
        
        /*
                  
        JASHist1DHistogramStyle hs = (JASHist1DHistogramStyle) histStyle;
        
        // if (customOverlay != null && customOverlay instanceof CanSetStyle)
        //    ((CanSetStyle) customOverlay).setStyle(hs);

        hs.setShowHistogramBars(dataStyle.lineStyle().isVisible());

        hs.setHistogramFill(dataStyle.fillStyle().isVisible());

        hs.setShowDataPoints(dataStyle.markerStyle().isVisible());

        hs.setShowLinesBetweenPoints(dataStyle.outlineStyle().isVisible());

        IFillStyle dataFillStyle = dataStyle.fillStyle();
        ILineStyle dataLineStyle = dataStyle.lineStyle();
        IMarkerStyle dataMarkerStyle = dataStyle.markerStyle();

        // By default the histogram's fill color, the contour of the histogram bar and
        // the line between the points are taken from the line color. If the fill color is set
        // and the histogram's bars are filled, the different color is used. Same for the
        // line between the points.

        // Set Colors here, including defaults

        String dataLineColor = dataLineStyle.color();
        String dataFillColor = dataFillStyle.color();
        String dataLineBetweenPointsColor = dataStyle.outlineStyle().color();
        String dataMarkerColor = dataMarkerStyle.color();
        String errorBarsColor = dataStyle.errorBarStyle().color();

        if (dataFillStyle instanceof BrushStyle) {
            dataFillColor = ((BrushStyle) dataFillStyle).color(globalIndex, overlayIndex);
            if (dataMarkerColor == null || !((BrushStyle) dataMarkerStyle).isParameterSet(Style.BRUSH_COLOR, true))
                dataMarkerColor = dataFillColor;
            if (dataLineBetweenPointsColor == null || !((BrushStyle) dataStyle.outlineStyle()).isParameterSet(Style.BRUSH_COLOR, true))
                dataLineBetweenPointsColor = dataFillColor;
            if (dataLineColor == null || !((BrushStyle) dataLineStyle).isParameterSet(Style.BRUSH_COLOR, true)) {
                dataLineColor = null;
            }

            // By default the error bars have the same color as the plot's line.
            if (errorBarsColor == null || !((BrushStyle) dataStyle.errorBarStyle()).isParameterSet(Style.BRUSH_COLOR, true)) {
                errorBarsColor = dataLineColor;
            }

        }

        if (dataFillColor != null && hs.getHistogramFill())
            if (dataFillColor != null)
                try {
                    Color color = ColorConverter.get(dataFillColor);
                    color = getTransparentColor(color, dataFillStyle.opacity());
                    hs.setHistogramBarColor(color);
                } catch (Exception cce) {
                    throw new RuntimeException(cce);
                }

        if (dataLineColor != null)
            try {
                Color color = ColorConverter.get(dataLineColor);
                color = getTransparentColor(color, dataLineStyle.opacity());
                hs.setHistogramBarLineColor(color);
            } catch (Exception cce) {
                throw new RuntimeException(cce);
            }

        if (dataLineBetweenPointsColor != null)
            try {
                Color color = ColorConverter.get(dataLineBetweenPointsColor);
                hs.setLineColor(color);
            } catch (Exception cce) {
                throw new RuntimeException(cce);
            }

        if (dataMarkerColor != null)
            try {
                Color color = ColorConverter.get(dataMarkerColor);
                color = getTransparentColor(color, dataMarkerStyle.opacity());
                hs.setDataPointColor(color);
                hs.setErrorBarColor(color);
            } catch (Exception cce) {
                throw new RuntimeException(cce);
            }

        try {
            if (errorBarsColor != null) {
                Color color = ColorConverter.get(errorBarsColor);
                color = getTransparentColor(color, dataStyle.errorBarStyle().opacity());
                hs.setErrorBarColor(color);
            }
        } catch (Exception cce) {
            throw new RuntimeException(cce);
        }

        // Set the line types String dataLineType = dataLineStyle.lineType();
        if (dataLineStyle instanceof LineStyle)
            dataLineType = ((LineStyle) dataLineStyle).lineType(globalIndex, overlayIndex);
        hs.setHistogramBarLineStyle(lineType(dataLineType));
        hs.setHistogramBarLineWidth(lineThickness(dataLineStyle.thickness()));

        String lineType = dataStyle.outlineStyle().lineType();
        if (dataStyle.outlineStyle() instanceof LineStyle)
            lineType = ((LineStyle) dataStyle.outlineStyle()).lineType(globalIndex, overlayIndex);
        hs.setLinesBetweenPointsStyle(lineType(lineType));
        hs.setLinesBetweenPointsWidth(lineThickness(dataStyle.outlineStyle().thickness()));

        String dataMarkerShape = dataMarkerStyle.shape();
        if (dataMarkerStyle instanceof MarkerStyle)
            dataMarkerShape = ((MarkerStyle) dataMarkerStyle).shape(globalIndex, overlayIndex);
        if (dataMarkerShape != null)
            hs.setDataPointStyle(markerShape(dataMarkerShape));

        hs.setDataPointSize(dataMarkerStyle.size());
         */
    }

    /*
    private static float lineThickness(String thickness)
    {
        return Float.parseFloat(thickness) / (float) 2.;
    }
    */

    private static float lineThickness(int thickness)
    {
        return ((float) thickness) / (float) 2.;
    }
    
    private static Stroke strokeFromLineStyle(ILineStyle style)
    {        
        // Line thickness.
        float lineThickness = Style.lineThickness(style.thickness());

        // Error bar line type.
        LineType lineType = LineType.getLineType(style.lineType());

        // Create the stroke for the line.
        BasicStroke stroke = new BasicStroke(lineThickness, 
                                             BasicStroke.CAP_SQUARE, 
                                             BasicStroke.JOIN_MITER, 
                                             10.0f, 
                                             lineType.getDashArray(), 
                                             0.0f);
        
        return stroke;
    }
    
    private static Color colorFromLineStyle(ILineStyle style)
    {
        // Color.
        Color color = null;
        
        try {
            // Get the basic color.
            color = ColorConverter.get(style.color());
            
            // Apply opacity setting.
            color = getTransparentColor(color, style.opacity());
            
        } catch (ColorConversionException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            //e.printStackTrace();
        }        
        
        return color;
    }
    
    public static final String[] availableShapes = new String[] {
            "dot",
            "box",
            "triangle",
            "diamond",
            "star",
            "verticalLine",
            "horizontalLine",
            "cross",
            "circle",
            "square"
    };
        
    /**
     * Create an AWT shape from a name and a size.
     * @param markerShape The name of the shape or AIDA index.
     * @param size The size of the shape. 
     * @return The AWT shape.
     */
    private static Shape getMarkerShape(String markerShape, float size)
    {
        if (markerShape.equals(availableShapes[0]) || markerShape.equals("0")) 
            return new Ellipse2D.Float(0-size/2, 0-size/2, size, size);
        else if (markerShape.equals(availableShapes[1]) || markerShape.equals("1"))
            return new Rectangle2D.Double(0-size/2, 0-size/2, size, size);
        else if (markerShape.equals(availableShapes[2]) || markerShape.equals("2"))
            return ShapeUtilities.createDownTriangle(size);
            //return createUpTriangle(size);
        else if (markerShape.equals(availableShapes[3]) || markerShape.equals("3"))
            return ShapeUtilities.createDiamond(size);
        else if (markerShape.equals(availableShapes[4]) || markerShape.equals("4"))
            return createStar(size);                       
        else if (markerShape.equals(availableShapes[5]) || markerShape.equals("5"))
            return new Line2D.Float(0, size, 0, -size);
        else if (markerShape.equals(availableShapes[6]) || markerShape.equals("6"))
            return new Line2D.Float(-size, 0, size, 0);
        else if (markerShape.equals(availableShapes[7]) || markerShape.equals("7"))
            return ShapeUtilities.createRegularCross(size, 1);
        else if (markerShape.equals(availableShapes[8]) || markerShape.equals("8"))
            return new Ellipse2D.Float(0-size/2, 0-size/2, size, size);
        else if (markerShape.equals(availableShapes[9]) || markerShape.equals("9"))
            return new Rectangle2D.Double(0-size, 0-size, size, size);
        else
            return null;
    }
    
    // Taken from:
    // jas.plot.java2.PlotGraphics12
    private static Shape createStar(float size)
    {
        GeneralPath path = new GeneralPath();
        
        // half size
        float ss = (float) size/2;
        
        // I think these are used for 2D placement within the plot,
        // so set to (0,0) and hope JFreeChart moves shape to the right place!
        float xx = 0;
        float yy = 0;
        
        // This code is straight copy from Tony's method.
        path.moveTo(xx, yy+ss);
        path.lineTo(xx, yy-ss);
        path.moveTo(xx-ss, yy);
        path.lineTo(xx+ss, yy);
        path.moveTo(xx-ss, yy-ss);
        path.lineTo(xx+ss, yy+ss);
        path.moveTo(xx+ss, yy-ss);
        path.lineTo(xx-ss, yy+ss);
        
        return path;
    } 
    
    /*
    private static Shape createUpTriangle(float size)
    {
        GeneralPath path = new GeneralPath();
        path.moveTo(0f, -size);
        path.lineTo(size, size);
        path.lineTo(-size, size);
        path.closePath();        
        return path;
    }
    */
}
