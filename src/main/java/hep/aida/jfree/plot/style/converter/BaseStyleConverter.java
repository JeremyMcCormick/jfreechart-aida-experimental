package hep.aida.jfree.plot.style.converter;

import hep.aida.IAxisStyle;
import hep.aida.IBoxStyle;
import hep.aida.IGridStyle;
import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;
import hep.aida.IStatisticsBoxStyle;
import hep.aida.jfree.annotations.BasicMultiLineXYTextAnnotation;
import hep.aida.jfree.plot.style.util.BorderUtil;
import hep.aida.jfree.plot.style.util.ColorUtil;
import hep.aida.jfree.plot.style.util.StrokeUtil;
import hep.aida.jfree.plot.style.util.StyleConstants;
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
class BaseStyleConverter implements StyleConverter {
    
    protected ChartState state = null;
            
    BaseStyleConverter() {
    }
            
    public void applyStyle(JFreeChart chart, Object plotObject, IPlotterStyle style, int[] datasetIndices) {
        state = new ChartState();
        state.setChart(chart);
        state.setPlotObject(plotObject);
        state.setPlotterStyle(style);
        state.setDatasetIndices(datasetIndices);
        applyStyle();
    }
    
        
    /**
     * This is the primary method for modifying a JFreeChart plot based on AIDA
     * styles.
     * 
     * @param baseChart The chart to which styles should be applied.
     * @param hist The backing histogram for the chart.
     * @param style The styles to apply.
     */
    void applyStyle() {
        // Apply styles to the chart, NOT directly having to do with data, 
        // e.g. title, background colors, etc.
        applyNonDataStyle();

        // Apply styles to chart having to do with data visibility and appearance.
        applyDataStyle();
    }

    /**
     * This is the default implementation to apply all styles to data and
     * non-data elements of the chart. Implementations of this method for specific
     * types should probably override this to avoid strange default behavior and
     * implement the most efficient way of applying the styles.
     * 
     * @param baseChart The chart to which styles should be applied.
     * @param hist The backing histogram.
     * @param style The plotter style.
     */
    void applyDataStyle() {
        // Is plot visible?
        if (state.getPlotterStyle().isVisible()) {
                       
            // Set the data styling or turn it off if invisible.
            if (isDataVisible()) {
                
                // Apply data fill style which fills histograms.
                applyDataFillStyle();

                // Apply data line style which draws histogram bars. 
                applyDataLineStyle();

                // Apply marker style which draws markers such as circles at data points.                
                applyDataMarkerStyle();
                
                // Apply data outline style which draws lines between data points.
                applyDataOutlineStyle();                
                                
                // Are errors visible?
                if (areErrorsVisible()) {
                    // Apply error bar style, which will only show if the data itself is set to visible.
                    applyErrorBarStyle();
                } else {
                    // Turn off display of error values.
                    makeErrorsInvisible();
                }                                               
            } else {
                
                // Turn off display of histogram data.
                makeDataInvisible();
                
                // Turn off display of errors.
                makeErrorsInvisible();
            }       
            
            // Draw the statistics box, which only really makes sense if data is visible.
            drawStatisticsBox();                
        } 
    }

    /**
     * This method applies styles to non-data elements of the chart, such as the
     * title, axes, and background.
     * 
     * @param baseChart The chart to which styles should be applied.
     * @param hist The backing histogram.
     * @param style The plotter style.
     */
    void applyNonDataStyle() {
        // Set the title style.
        applyTitleStyle();

        // Set the region's background color.
        applyRegionStyle();

        // Set the plot's background color.
        applyDataBoxStyle();

        // Set the grid style.
        applyGridStyle();

        // Apply all axis styles.
        applyAxisStyles();
    }

    /**
     * This is a helper method to call all methods that apply axis styles of
     * various kinds.
     * 
     * @param baseChart
     * @param hist
     * @param style
     */
    void applyAxisStyles() {
        // Set log scale on axes.
        applyLogAxis();

        // Set position of axes.
        applyAxisLocation();

        // Set axis labels.
        applyAxisLabels();

        // Set the X axis drawing style, such as label fonts.
        applyAxisStyle(state.getChart().getXYPlot().getDomainAxis(), state.getPlotterStyle().xAxisStyle());

        // Set the Y axis drawing style, such as label fonts.
        applyAxisStyle(state.getChart().getXYPlot().getRangeAxis(), state.getPlotterStyle().yAxisStyle());

        // Set the X axis limits.
        applyAxisLimits(state.getChart().getXYPlot().getDomainAxis(), state.getPlotterStyle().xAxisStyle());

        // Set the Y axis limits.
        applyAxisLimits(state.getChart().getXYPlot().getRangeAxis(), state.getPlotterStyle().yAxisStyle());
    }

    /**
     * Apply axil labels to the chart from the hist
     * @param baseChart
     * @param hist
     */
    void applyAxisLabels() {
        if (state.getHistogram().annotation().hasKey("xAxisLabel")) {
            state.getChart().getXYPlot().getDomainAxis().setLabel(state.getHistogram().annotation().value("xAxisLabel"));
        }
        if (state.getHistogram().annotation().hasKey("yAxisLabel")) {
            state.getChart().getXYPlot().getRangeAxis().setLabel(state.getHistogram().annotation().value("yAxisLabel"));
        }
    }

    /**
     * Sets log style on X and Y axes. This MUST come before other axis styling,
     * because it sets a new axis object on the plot.
     * 
     * @param baseChart The chart to style.
     * @param style The AIDA plotter style.
     */
    void applyLogAxis() {
        applyLogAxis(state.getPlotterStyle().xAxisStyle(), true);
        applyLogAxis(state.getPlotterStyle().yAxisStyle(), false);
    }

    /**
     * Set the location of the axis. Currently only handles placement of Y axis
     * on left or right side. AIDA does not appear to support placing the X axis
     * on the top of the plot. (JFreeChart does support this.)
     * 
     * @param baseChart The chart to set axis location.
     * @param style The AIDA plot style.
     */
    void applyAxisLocation() {
        IAxisStyle yAxisStyle = state.getPlotterStyle().yAxisStyle();
        String yAxisValue = yAxisStyle.parameterValue("yAxis");
        if (yAxisValue != null) {
            try {
                AxisLocation axisLocation = (yAxisValue.equalsIgnoreCase("Y1")) ? AxisLocation.BOTTOM_OR_RIGHT : AxisLocation.BOTTOM_OR_LEFT;
                state.getChart().getXYPlot().setDomainAxisLocation(axisLocation);
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
    void applyLogAxis(IAxisStyle axisStyle, boolean domain) {
        String scale = axisStyle.scaling();
        if (scale != null) {
            if (scale.startsWith("log")) {
                LogAxis axis = new LogAxis("");
                axis.setBase(10);
                axis.setSmallestValue(0.01);
                if (domain) {
                    state.getChart().getXYPlot().setDomainAxis(axis);
                } else {
                    state.getChart().getXYPlot().setRangeAxis(axis);
                }
            }
        }
    }

    /**
     * 
     * @param baseChart
     * @param style
     */
    void applyDataBoxStyle() {        
        IBoxStyle boxStyle = state.getPlotterStyle().dataBoxStyle();        
        if (boxStyle.isVisible()) {            
            if (boxStyle.backgroundStyle().isVisible()) {
                Color color = ColorUtil.toColor(state.getPlotterStyle().dataBoxStyle().backgroundStyle(), Color.white);
                state.getChart().getXYPlot().setBackgroundPaint(color);
            }
            if (boxStyle.borderStyle().isVisible()) {
                state.getChart().getXYPlot().setOutlineVisible(true);
                Stroke stroke = StrokeUtil.toStroke(boxStyle.borderStyle());
                state.getChart().getXYPlot().setOutlineStroke(stroke);
                Color color = ColorUtil.toColor(boxStyle.borderStyle(), Color.black);
                state.getChart().getXYPlot().setOutlinePaint(color);
                
            }
        }
    }

    /**
     * 
     * @param baseChart
     * @param style
     */
    void applyRegionStyle() {
        Color color = ColorUtil.toColor(state.getPlotterStyle().regionBoxStyle().backgroundStyle(), Color.white);
        state.getChart().setBackgroundPaint(color);
        
        //Border border = BorderUtil.toBorder(style.regionBoxStyle().borderStyle());
        //if (border != null)
        //    System.out.println("created border: " + border.getClass().getCanonicalName());

        // TODO: set border styling here
        //style.regionBoxStyle().borderStyle().borderType();
    }

    public void applyStyle(ChartPanel panel) {
        if (this.state == null)
            throw new RuntimeException("The ChartState was never set.");
        this.state.setPanel(panel);
        applyPanelStyle();
    }
    
    /**
     * Apply panel style.
     * @param panel The ChartPanel.
     * @param style The plotter style.
     */
    void applyPanelStyle() {
        Border border = BorderUtil.toBorder(state.getPlotterStyle().regionBoxStyle().borderStyle());
        state.getPanel().setBorder(border);
    }
    
    /**
     * 
     * @param baseChart
     * @param style
     */
    void applyTitleStyle() {
        Font titleFont = PlotterFontUtil.getFont(state.getPlotterStyle().titleStyle().textStyle());
        state.getChart().getTitle().setFont(titleFont);

        String colorStr = state.getPlotterStyle().titleStyle().textStyle().color();
        if (colorStr != null) {
            try {
                Color titleColor = ColorUtil.toColor(state.getPlotterStyle().titleStyle().textStyle());
                state.getChart().getTitle().setPaint(titleColor);
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
    void applyAxisLimits(ValueAxis axis, IAxisStyle axisStyle) {
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
    void applyAxisStyle(ValueAxis axis, IAxisStyle axisStyle) {
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
     * @param baseChart
     * @param style
     */
    void applyGridStyle() {

        // System.out.println("applyGridStyle - " + chart.getTitle().getText());

        IGridStyle gridStyle = state.getPlotterStyle().gridStyle();
        boolean visible = gridStyle.isVisible();

        if (visible) {

            XYPlot plot = state.getChart().getXYPlot();

            // System.out.println("grid is visible");

            plot.setDomainGridlinesVisible(true);
            plot.setRangeGridlinesVisible(true);
            // plot.setDomainMinorGridlinesVisible(true);
            // plot.setRangeMinorGridlinesVisible(true);

            Color color = ColorUtil.toColor(gridStyle, StyleConstants.DEFAULT_GRID_COLOR);
            plot.setDomainGridlinePaint(color);
            // plot.setDomainMinorGridlinePaint(color);
            plot.setRangeGridlinePaint(color);
            // plot.setRangeMinorGridlinePaint(color);

            Stroke stroke = StrokeUtil.toStroke(gridStyle);
            if (stroke != null) {
                // System.out.println("setting grid stroke");
                state.getChart().getXYPlot().setDomainGridlineStroke(stroke);
                // chart.getXYPlot().setDomainMinorGridlineStroke(stroke);
                state.getChart().getXYPlot().setRangeGridlineStroke(stroke);
                // chart.getXYPlot().setRangeMinorGridlineStroke(stroke);
            } /*
               * else { System.out.println("using default stroke"); }
               */

            // chart.fireChartChanged();

            // Not sure JFree can do this.
            // double cellSize = gridStyle.cellSize();
        } else {
            // System.out.println("grid is NOT visible");
            state.getChart().getXYPlot().setDomainGridlinesVisible(false);
            // chart.getXYPlot().setDomainMinorGridlinesVisible(false);
            state.getChart().getXYPlot().setRangeGridlinesVisible(false);
            // chart.getXYPlot().setRangeMinorGridlinesVisible(false);
        }

    }

    /**
     * Helper method to check if the data is visible in the style.
     * 
     * @param style
     * @return True if data is visible; false if not.
     */
    boolean isDataVisible() {
        boolean visible = true;
        if (!state.getPlotterStyle().dataStyle().isVisible()) {
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
    boolean areErrorsVisible() {
        boolean visible = true;
        if (!state.getPlotterStyle().dataStyle().errorBarStyle().isVisible()) {
            visible = false;
        }
        return visible;
    }
    
    // FIXME: Only works for 1D histograms.
    void drawStatisticsBox() {
        IStatisticsBoxStyle statStyle = state.getPlotterStyle().statisticsBoxStyle();
        if (statStyle.isVisible()) {
            if (state.getHistogram() instanceof IHistogram1D) {
                
                XYPlot plot = state.getChart().getXYPlot();
                IHistogram1D hist = (IHistogram1D)state.getHistogram();
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
     * @param baseChart
     * @param style
     */
    void applyErrorBarStyle() {
    }

    /**
     * 
     * @param baseChart
     * @param hist
     * @param style
     */
    void applyDataFillStyle() {
    }

    /**
     * 
     * @param baseChart
     * @param hist
     * @param style
     */
    void applyDataLineStyle() {
    }

    /**
     * 
     * @param baseChart
     * @param hist
     * @param style
     */
    void applyDataOutlineStyle() {
    }

    /**
     * 
     * @param baseChart
     * @param hist
     * @param style
     */
    void applyDataMarkerStyle() {
    }

    /**
     * 
     * @param baseChart
     */
    void makeDataInvisible() {
    }

    /**
     * 
     * @param baseChart
     */
    void makeErrorsInvisible() {
    }    
}
