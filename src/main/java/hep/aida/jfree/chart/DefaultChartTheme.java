package hep.aida.jfree.chart;

import java.awt.BasicStroke;
import java.awt.Color;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.XYPlot;

/**
 * This is the default theme for JFreeChart when plotting AIDA objects.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class DefaultChartTheme extends StandardChartTheme {

    public DefaultChartTheme() {
        super("Legacy");
    }

    public void apply(JFreeChart chart) {
        super.apply(chart);
    }

    public void applyToXYPlot(XYPlot plot) {
        // White is default background color.
        plot.setBackgroundPaint(Color.white);

        // By default do not show outline around chart.
        plot.setOutlineVisible(false);

        // Turn off grid lines.
        plot.setDomainGridlinesVisible(false);
        plot.setDomainMinorGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);
        plot.setRangeMinorGridlinesVisible(false);

        // Turn off crosshair.
        plot.setDomainCrosshairVisible(false);
        plot.setDomainZeroBaselineVisible(false);
        plot.setRangeCrosshairVisible(false);
        plot.setRangeZeroBaselineVisible(false);

        // Turn on auto range.
        plot.getDomainAxis().setAutoRange(true);
        plot.getRangeAxis().setAutoRange(true);

        // Turn on minor tick marks.
        plot.getDomainAxis().setMinorTickMarksVisible(true);
        plot.getRangeAxis().setMinorTickMarksVisible(true);

        // Configure domain tick marks.
        plot.getDomainAxis().setTickMarkInsideLength(4.0f);
        plot.getDomainAxis().setTickMarkOutsideLength(0.0f);
        plot.getDomainAxis().setMinorTickMarkInsideLength(2.0f);
        plot.getDomainAxis().setMinorTickMarkOutsideLength(0.0f);
        plot.getDomainAxis().setTickMarkPaint(Color.black);
        plot.getDomainAxis().setAxisLinePaint(Color.black);
        plot.getDomainAxis().setAxisLineStroke(new BasicStroke(1.0f));

        // Configure range tick marks.
        plot.getRangeAxis().setTickMarkInsideLength(4.0f);
        plot.getRangeAxis().setTickMarkOutsideLength(0.0f);
        plot.getRangeAxis().setMinorTickMarkInsideLength(2.0f);
        plot.getRangeAxis().setMinorTickMarkOutsideLength(0.0f);
        plot.getRangeAxis().setTickMarkPaint(Color.black);
        plot.getRangeAxis().setAxisLinePaint(Color.black);
        plot.getRangeAxis().setAxisLineStroke(new BasicStroke(1.0f));
    }
}