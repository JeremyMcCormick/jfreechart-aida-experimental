package hep.aida.jfree;

import java.awt.Color;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleInsets;

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
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.CENTER);
        chart.setPadding(new RectangleInsets(5, 5, 5, 5));
    }

    public void applyToXYPlot(XYPlot plot) {
        // White is default background color.
        plot.setBackgroundPaint(Color.white);
               
        // By default do not show outline around chart.
        plot.setOutlineVisible(false);

        // Turn off crosshair.
        plot.setDomainCrosshairVisible(false);
        plot.setDomainZeroBaselineVisible(false);
        plot.setRangeCrosshairVisible(false);
        plot.setRangeZeroBaselineVisible(false);

        // Configure domain tick marks.
        plot.getDomainAxis().setTickMarkInsideLength(10.0f);
        plot.getDomainAxis().setTickMarkOutsideLength(0.0f);
        plot.getDomainAxis().setMinorTickMarkInsideLength(5.0f);
        plot.getDomainAxis().setMinorTickMarkOutsideLength(0.0f);
        plot.getDomainAxis().setMinorTickMarksVisible(true);
        
        // Configure range tick marks.
        plot.getRangeAxis().setTickMarkInsideLength(10.0f);
        plot.getRangeAxis().setTickMarkOutsideLength(0.0f);
        plot.getRangeAxis().setMinorTickMarkInsideLength(5.0f);
        plot.getRangeAxis().setMinorTickMarkOutsideLength(0.0f);
        plot.getDomainAxis().setMinorTickMarksVisible(true);
    }
}