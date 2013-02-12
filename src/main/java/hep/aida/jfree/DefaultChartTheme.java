package hep.aida.jfree;

import java.awt.Color;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.XYPlot;

/**
 * This is the default theme for JFreeChart when plotting AIDA objects.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
class DefaultChartTheme extends StandardChartTheme {

    public DefaultChartTheme() {
        super("Default");
    }
    
    public void apply(JFreeChart chart) {
        super.apply(chart);
        chart.setBackgroundPaint(Color.white);
        chart.removeLegend();
    }
    
    public void applyToXYPlot(XYPlot plot) {
        
        // White is default background color.
        plot.setBackgroundPaint(Color.white);
    
        // Turn off domain grid lines.
        plot.setDomainGridlinesVisible(false);
        plot.setDomainMinorGridlinesVisible(false);
        plot.setDomainCrosshairVisible(false);
        plot.setDomainZeroBaselineVisible(false);
    
        // Turn off range grid lines.
        plot.setRangeGridlinesVisible(false);      
        plot.setRangeMinorGridlinesVisible(false);
        plot.setRangeCrosshairVisible(false);
        plot.setRangeZeroBaselineVisible(false);
                
        // Turn on auto range.
        plot.getDomainAxis().setAutoRange(true);
        plot.getRangeAxis().setAutoRange(true);
                        
        // Tick unit frequency and labels.  
        // FIXME: This doesn't work generically!  To do it right, would need
        //        to look at the upper bounds for both axes and set accordingly.
        //TickUnits units = new TickUnits();
        //units.add(new NumberTickUnit(10.));        
        //plot.getRangeAxis().setStandardTickUnits(units);
        //plot.getRangeAxis().setMinorTickMarksVisible(true);
        //plot.getRangeAxis().setMinorTickCount(10);        
        //plot.getDomainAxis().setStandardTickUnits(units);
        //plot.getDomainAxis().setMinorTickMarksVisible(true);
        //plot.getDomainAxis().setMinorTickCount(10);
        
        // Turn on minor tick marks.
        plot.getDomainAxis().setMinorTickMarksVisible(true);        
        plot.getRangeAxis().setMinorTickMarksVisible(true);
        
        // Configure domain tick marks.
        plot.getDomainAxis().setTickMarkInsideLength(8.0f);
        plot.getDomainAxis().setTickMarkOutsideLength(0.0f);
        plot.getDomainAxis().setMinorTickMarkInsideLength(2.0f);
        plot.getDomainAxis().setMinorTickMarkOutsideLength(0.0f);
        plot.getDomainAxis().setTickMarkPaint(Color.black);        
        
        // Configure range tick marks.
        plot.getRangeAxis().setTickMarkInsideLength(8.0f);
        plot.getRangeAxis().setTickMarkOutsideLength(0.0f);        
        plot.getRangeAxis().setMinorTickMarkInsideLength(2.0f);
        plot.getRangeAxis().setMinorTickMarkOutsideLength(0.0f);
        plot.getRangeAxis().setTickMarkPaint(Color.black);
        
        //double upper = plot.getRangeAxis().getUpperBound();
        //System.out.println("upper="+upper);
                
        // Set margins.  
        //plot.getDomainAxis().setUpperMargin(0.10);
        //plot.getRangeAxis().setUpperMargin(0.05);       
    }    
}