package hep.aida.jfree.plot.style.converter;

import hep.aida.IAxisStyle;
import hep.aida.IBaseHistogram;
import hep.aida.IBoxStyle;
import hep.aida.IGridStyle;
import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;
import hep.aida.IStatisticsBoxStyle;
import hep.aida.jfree.annotations.BasicMultiLineXYTextAnnotation;
import hep.aida.jfree.plot.style.util.BorderUtil;
import hep.aida.jfree.plot.style.util.ColorUtil;
import hep.aida.jfree.plot.style.util.StrokeUtil;
import hep.aida.jfree.plotter.ChartState;
import hep.aida.ref.plotter.BaseStyle;
import hep.aida.ref.plotter.PlotterFontUtil;
import hep.aida.ref.plotter.Style;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;

import javax.swing.border.Border;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.TextAnchor;

/**
 * This class converts from AIDA styles to JFreeChart.
 * 
 * See this method for how to apply styles from AIDA using the JAS3 plotter,
 * which was used as a reference:
 * 
 * freehep-jaida: hep.aida.ref.plotter.PlotterRegion.applyStyle(JASHistData jasHistData, IPlotterStyle style);
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */

//
// TODO:
//
// Some remaining style tasks:
//
// -foreground color
//   -What is this supposed to paint? Which components? Overrides other styles?  See freehep-jaida for details.
// -data area border type
//   -Borders are difficult to draw generically in JFreeChart because plots are not built with internal Swing JComponents.
// -2D histograms (see JAIDA code)
//   -different color map types
// -functions
public abstract class AbstractStyleConverter implements StyleConverter {

    private static final boolean DEBUG = false;
    
    private ChartState state = null;
    private IPlotterStyle style = null;
    
    protected AbstractStyleConverter() {
    }
    
    protected AbstractStyleConverter(ChartState state) {
        this.state = state;
    }
    
    public void setChartState(ChartState state) {
        this.state = state;
    }
    
    public ChartState getChartState() {
        return this.state;
    }
    
    public void setStyle(IPlotterStyle style) {
        this.style = style;
    }
    
    public IPlotterStyle getStyle() {
        return this.style;
    }
    
    /**
     * This method will apply styles to the objects from the current ChartState,
     * but will not apply panel styles, which much be done in a separate call,
     * as the panel is not always immediately available.
     */
    public void applyStyle() {
        applyStyle(state.chart(), state.histogram(), style);
    }
    
    /**
     * This is the primary method for modifying a JFreeChart plot based on AIDA
     * styles.
     * 
     * @param chart The chart to which styles should be applied.
     * @param hist The backing histogram for the chart.
     * @param style The styles to apply.
     */
    void applyStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style) {
        if (DEBUG) {
            System.out.println("applying style to chart: " + chart.getTitle().getText());
            System.out.println("chart type: " + chart.getXYPlot().getClass().getCanonicalName());
            for (int i = 0; i < chart.getXYPlot().getRendererCount(); i++) {
                System.out.println("renderer[" + i + "]: " + chart.getXYPlot().getRenderer(i).getClass().getCanonicalName());
            }
        }

        // Apply styles to the chart, NOT directly having to do with data, e.g.
        // title, background colors, etc.
        applyNonDataStyle(chart, hist, style);

        // Apply styles to chart having to do with data visibility and
        // appearance.
        applyDataStyle(chart, hist, style);
    }

    /**
     * This is the default implementation to apply all styles to data and
     * non-data elements of the chart. Implementations of styles for specific
     * types should probably override this to avoid strange default behavior and
     * implement the most efficient way of applying the styles to that type.
     * 
     * @param chart The chart to which styles should be applied.
     * @param hist The backing histogram.
     * @param style The plotter style.
     */
    protected void applyDataStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style) {
        // Check if the plot is visible before continuing.
        if (style.isVisible()) {

            // Set the data styling or turn it off if invisible.
            if (isDataVisible(style)) {

                // data fill style
                applyDataFillStyle(chart, hist, style);

                // data line style (histogram bars)
                applyDataLineStyle(chart, hist, style);

            } else {
                // Turn off display of histogram data.
                makeDataInvisible(chart);
            }

            // Set marker and line styling which may still be visible even if
            // data style is off.
            if (style.isVisible()) {
                applyDataMarkerStyle(chart, hist, style);
                applyDataOutlineStyle(chart, hist, style);
            }

            // Set error style.
            if (areErrorsVisible(style)) {
                applyErrorBarStyle(chart, style);
            // Turn off display of error values.
            } else {
                makeErrorsInvisible(chart);
            }
        
            // Draw the statistics box.
            drawStatisticsBox();

        // Turn off both data and errors as style is set to invisible.
        } else {
            makeDataInvisible(chart);
            makeErrorsInvisible(chart);
        }
    }

    /**
     * This method applies styles to non-data elements of the chart, such as the
     * title, axes, and background.
     * 
     * @param chart The chart to which styles should be applied.
     * @param hist The backing histogram.
     * @param style The plotter style.
     */
    protected void applyNonDataStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style) {
        // Set the title style.
        applyTitleStyle(chart, style);

        // Set the region's background color.
        applyRegionStyle(chart, style);

        // Set the plot's background color.
        applyDataBoxStyle(chart, style);

        // Set the grid style.
        applyGridStyle(chart, style);

        // Apply all axis styles.
        applyAllAxisStyles(chart, hist, style);
    }

    /**
     * This is a helper method to call all methods that apply axis styles of
     * various kinds.
     * 
     * @param chart
     * @param hist
     * @param style
     */
    private void applyAllAxisStyles(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style) {
        // Set log scale on axes.
        applyLogAxis(chart, style);

        // Set position of axes.
        applyAxisLocation(chart, style);

        // Set axis labels.
        applyAxisLabels(chart, hist);

        // Set the X axis drawing style, such as label fonts.
        applyAxisStyle(chart.getXYPlot().getDomainAxis(), style.xAxisStyle());

        // Set the Y axis drawing style, such as label fonts.
        applyAxisStyle(chart.getXYPlot().getRangeAxis(), style.yAxisStyle());

        // Set the X axis limits.
        applyAxisLimits(chart.getXYPlot().getDomainAxis(), style.xAxisStyle());

        // Set the Y axis limits.
        applyAxisLimits(chart.getXYPlot().getRangeAxis(), style.yAxisStyle());
    }

    /**
     * Apply axil labels to the chart from the hist
     * @param chart
     * @param hist
     */
    private static void applyAxisLabels(JFreeChart chart, IBaseHistogram hist) {
        if (hist.annotation().hasKey("xAxisLabel")) {
            chart.getXYPlot().getDomainAxis().setLabel(hist.annotation().value("xAxisLabel"));
        }
        if (hist.annotation().hasKey("yAxisLabel")) {
            chart.getXYPlot().getRangeAxis().setLabel(hist.annotation().value("yAxisLabel"));
        }
    }

    /**
     * Sets log style on X and Y axes. This MUST come before other axis styling,
     * because it sets a new axis object on the plot.
     * 
     * @param chart The chart to style.
     * @param style The AIDA plotter style.
     */
    private static void applyLogAxis(JFreeChart chart, IPlotterStyle style) {
        applyLogAxis(chart.getXYPlot(), style.xAxisStyle(), true);
        applyLogAxis(chart.getXYPlot(), style.yAxisStyle(), false);
    }

    /**
     * Set the location of the axis. Currently only handles placement of Y axis
     * on left or right side. AIDA does not appear to support placing the X axis
     * on the top of the plot. (JFreeChart does support this.)
     * 
     * @param chart The chart to set axis location.
     * @param style The AIDA plot style.
     */
    private static void applyAxisLocation(JFreeChart chart, IPlotterStyle style) {
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
    }

    /**
     * Set log axis if selected.
     * @param plot The plot with the axes.
     * @param axisStyle The AIDA axis style settings.
     * @param domain True if axis is domain; false if range.
     */
    private static void applyLogAxis(XYPlot plot, IAxisStyle axisStyle, boolean domain) {
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

    /**
     * 
     * @param chart
     * @param style
     */
    private static void applyDataBoxStyle(JFreeChart chart, IPlotterStyle style) {        
        IBoxStyle boxStyle = style.dataBoxStyle();        
        if (boxStyle.isVisible()) {            
            if (boxStyle.backgroundStyle().isVisible()) {
                Color color = ColorUtil.toColor(style.dataBoxStyle().backgroundStyle(), Color.white);
                chart.getXYPlot().setBackgroundPaint(color);
            }
            if (boxStyle.borderStyle().isVisible()) {
                chart.getXYPlot().setOutlineVisible(true);
                Stroke stroke = StrokeUtil.toStroke(boxStyle.borderStyle());
                chart.getXYPlot().setOutlineStroke(stroke);
                Color color = ColorUtil.toColor(boxStyle.borderStyle(), Color.black);
                chart.getXYPlot().setOutlinePaint(color);
                
            }
        }
    }

    /**
     * 
     * @param chart
     * @param style
     */
    private static void applyRegionStyle(JFreeChart chart, IPlotterStyle style) {
        Color color = ColorUtil.toColor(style.regionBoxStyle().backgroundStyle(), Color.white);
        chart.setBackgroundPaint(color);
        
        //Border border = BorderUtil.toBorder(style.regionBoxStyle().borderStyle());
        //if (border != null)
        //    System.out.println("created border: " + border.getClass().getCanonicalName());

        // TODO: set border styling here
        //style.regionBoxStyle().borderStyle().borderType();
    }

    /**
     * Apply panel style.
     * @param panel The ChartPanel.
     * @param style The plotter style.
     */
    private static void applyPanelStyle(ChartPanel panel, IPlotterStyle style) {
        Border border = BorderUtil.toBorder(style.regionBoxStyle().borderStyle());
        //if (border != null)
        //    System.out.println("created border: " + border.getClass().getCanonicalName());
        panel.setBorder(border);
    }
    
    /**
     * Apply panel style to current chart state.
     */
    public void applyPanelStyle() 
    {
        applyPanelStyle(state.panel(), style);
    }

    /**
     * 
     * @param chart
     * @param style
     */
    private static void applyTitleStyle(JFreeChart chart, IPlotterStyle style) {
        Font titleFont = PlotterFontUtil.getFont(style.titleStyle().textStyle());
        chart.getTitle().setFont(titleFont);

        String colorStr = style.titleStyle().textStyle().color();
        if (colorStr != null) {
            try {
                Color titleColor = ColorUtil.toColor(style.titleStyle().textStyle());
                chart.getTitle().setPaint(titleColor);
            } catch (Exception x) {
                throw new RuntimeException(x);
            }
        }
    }

    /**
     * 
     * @param axis
     * @param axisStyle
     */
    private static void applyAxisLimits(ValueAxis axis, IAxisStyle axisStyle) {
        if (axisStyle.parameterValue(Style.AXIS_LOWER_LIMIT) != null) {
            double lowerLimit = Double.parseDouble(axisStyle.parameterValue(Style.AXIS_LOWER_LIMIT));
            axis.setLowerBound(lowerLimit);
        }

        if (axisStyle.parameterValue(Style.AXIS_UPPER_LIMIT) != null) {
            double upperLimit = Double.parseDouble(axisStyle.parameterValue(Style.AXIS_UPPER_LIMIT));
            axis.setUpperBound(upperLimit);
        }
    }

    /**
     * This method applies these styles to the axis:
     * 
     * <ul>
     * <li>axis labels
     * <li>tick labels
     * <li>axis and tick line appearence
     * <li>zero suppression
     * </ul>
     * 
     * @param axis The JFreeChart axis.
     * @param axisStyle The AIDA axis style settings.
     */
    private static void applyAxisStyle(ValueAxis axis, IAxisStyle axisStyle) {
        // Axis label.
        String axisLabel = axisStyle.label();
        boolean setlabel = axisLabel != null && ((BaseStyle) axisStyle).isParameterSet(hep.aida.ref.plotter.Style.AXIS_LABEL);
        if (setlabel)
            axis.setLabel(axisLabel);

        // axis label font
        axis.setLabelFont(PlotterFontUtil.getFont(axisStyle.labelStyle()));

        // axis label color
        String axisLabelColor = axisStyle.labelStyle().color();
        if (axisLabelColor != null) {
            try {
                Color color = ColorUtil.toColor(axisStyle.labelStyle());
                if (color != null)
                    axis.setLabelPaint(color);
            } catch (Exception cce) {
                throw new RuntimeException(cce);
            }
        }

        // tick labels color
        String axisTickLabelColor = axisStyle.tickLabelStyle().color();
        if (axisTickLabelColor != null) {
            try {
                Color color = ColorUtil.toColor(axisStyle.tickLabelStyle());
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
                Color color = ColorUtil.toColor(axisStyle.lineStyle());
                axis.setAxisLinePaint(color);
            } catch (Exception cce) {
                throw new RuntimeException(cce);
            }
        }

        // axis line width
        float axisLineWidth = StrokeUtil.lineThickness(axisStyle.lineStyle().thickness());
        if (axisLineWidth >= 0) {
            try {
                Stroke stroke = new BasicStroke(axisLineWidth);
                axis.setAxisLineStroke(stroke);
                axis.setTickMarkStroke(stroke);
            } catch (Exception cce) {
                throw new RuntimeException(cce);
            }
        }

        // Suppress zero on the axis range.
        boolean allowZeroSuppression = Boolean.valueOf(axisStyle.parameterValue("allowZeroSuppression")).booleanValue();
        if (allowZeroSuppression) {
            // System.out.println("allowZeroSuppression = " +
            // allowZeroSuppression);
            
            // FIXME: Fixed if using a LogAxis.
            if (axis instanceof NumberAxis)
                ((NumberAxis) axis).setAutoRangeIncludesZero(false);
        }

        //
        // Not sure about how to use this, because JFreeChart's labels on the Y
        // axis are aligned vertically by default. The default style in JAIDA is a horizontal
        // label placed at the top of the axis.
        //

        // Set vertical axis label.
        // boolean verticalLabel =
        // Boolean.valueOf(axisStyle.parameterValue(Style.AXIS_VERTICAL_LABEL)).booleanValue();
        // if (verticalLabel)
        // axis.setLabelAngle(Math.PI/2);
        // label.setRotated(verticalLabel);
    }

    /**
     * 
     * @param chart
     * @param style
     */
    private static void applyGridStyle(JFreeChart chart, IPlotterStyle style) {

        // System.out.println("applyGridStyle - " + chart.getTitle().getText());

        IGridStyle gridStyle = style.gridStyle();
        boolean visible = gridStyle.isVisible();

        if (visible) {

            XYPlot plot = chart.getXYPlot();

            // System.out.println("grid is visible");

            plot.setDomainGridlinesVisible(true);
            plot.setRangeGridlinesVisible(true);
            // plot.setDomainMinorGridlinesVisible(true);
            // plot.setRangeMinorGridlinesVisible(true);

            Color color = ColorUtil.toColor(gridStyle, DEFAULT_GRID_COLOR);
            plot.setDomainGridlinePaint(color);
            // plot.setDomainMinorGridlinePaint(color);
            plot.setRangeGridlinePaint(color);
            // plot.setRangeMinorGridlinePaint(color);

            Stroke stroke = StrokeUtil.toStroke(gridStyle);
            if (stroke != null) {
                // System.out.println("setting grid stroke");
                chart.getXYPlot().setDomainGridlineStroke(stroke);
                // chart.getXYPlot().setDomainMinorGridlineStroke(stroke);
                chart.getXYPlot().setRangeGridlineStroke(stroke);
                // chart.getXYPlot().setRangeMinorGridlineStroke(stroke);
            } /*
               * else { System.out.println("using default stroke"); }
               */

            // chart.fireChartChanged();

            // Not sure JFree can do this.
            // double cellSize = gridStyle.cellSize();
        } else {
            // System.out.println("grid is NOT visible");
            chart.getXYPlot().setDomainGridlinesVisible(false);
            // chart.getXYPlot().setDomainMinorGridlinesVisible(false);
            chart.getXYPlot().setRangeGridlinesVisible(false);
            // chart.getXYPlot().setRangeMinorGridlinesVisible(false);
        }

    }

    /**
     * Helper method to check if the data is visible in the style.
     * 
     * @param style
     * @return True if data is visible; false if not.
     */
    protected static boolean isDataVisible(IPlotterStyle style) {
        boolean visible = true;
        if (!style.dataStyle().isVisible()) {
            visible = false;
        }
        return visible;
    }

    /**
     * Helper method to check if the errors are visible in the style.
     * 
     * @param style
     * @return True if data is visible; false if not.
     */
    protected static boolean areErrorsVisible(IPlotterStyle style) {
        boolean visible = true;
        if (!style.dataStyle().errorBarStyle().isVisible()) {
            visible = false;
        }
        return visible;
    }
    
    // FIXME: Only works for 1D histograms.
    protected void drawStatisticsBox() {
        IStatisticsBoxStyle statStyle = style.statisticsBoxStyle();
        if (statStyle.isVisible()) {
            if (state.histogram() instanceof IHistogram1D) {
                
                XYPlot plot = state.plot();
                IHistogram1D hist = (IHistogram1D)state.histogram();
                int entries = hist.allEntries();
                double mean = hist.mean();
                double rms = hist.rms();
                String stats = "entries: " + entries + "\n" + "mean: " + mean + "\n" + "rms: " + rms;
                double x = statStyle.boxStyle().x();
                double y = statStyle.boxStyle().y();
                //System.out.println("drawing @ x, y = " + x + " " + y);
                BasicMultiLineXYTextAnnotation annotation = new BasicMultiLineXYTextAnnotation(stats, x, y);
                annotation.setTextAnchor(TextAnchor.TOP_LEFT);
                annotation.setOutlineStroke(new BasicStroke(1.0f));
                annotation.setOutlineVisible(true);
                Paint borderPaint = ColorUtil.toColor(statStyle.boxStyle().borderStyle());
                if (borderPaint != null) {
                    annotation.setOutlinePaint(borderPaint);
                }
                Font font = PlotterFontUtil.getFont(statStyle.textStyle());
                annotation.setFont(font);
                plot.addAnnotation(annotation, true);
            }
        }
    }

    /**
     * 
     * @param chart
     * @param style
     */
    protected void applyErrorBarStyle(JFreeChart chart, IPlotterStyle style) {
    }

    /**
     * 
     * @param chart
     * @param hist
     * @param style
     */
    protected void applyDataFillStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style) {
    }

    /**
     * 
     * @param chart
     * @param hist
     * @param style
     */
    protected void applyDataLineStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style) {
    }

    /**
     * 
     * @param chart
     * @param hist
     * @param style
     */
    protected void applyDataOutlineStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style) {
    }

    /**
     * 
     * @param chart
     * @param hist
     * @param style
     */
    protected void applyDataMarkerStyle(JFreeChart chart, IBaseHistogram hist, IPlotterStyle style) {
    }

    /**
     * 
     * @param chart
     */
    protected void makeDataInvisible(JFreeChart chart) {
    }

    /**
     * 
     * @param chart
     */
    protected void makeErrorsInvisible(JFreeChart chart) {
    }
}
