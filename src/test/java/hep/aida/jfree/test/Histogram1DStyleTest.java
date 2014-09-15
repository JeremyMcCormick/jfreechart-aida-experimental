package hep.aida.jfree.test;

import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;

import java.util.Random;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram1DStyleTest extends AbstractPlotTest {
    
    /**
     * Create a default style to be used in each of the histogram regions.
     */
    public IPlotterStyle createDefaultStyle() {
        
        IPlotterStyle pstyle = this.plotterFactory.createPlotterStyle();
              
        // Axis appearence.
        pstyle.xAxisStyle().labelStyle().setBold(true);
        pstyle.yAxisStyle().labelStyle().setBold(true);
        pstyle.xAxisStyle().tickLabelStyle().setBold(true);
        pstyle.yAxisStyle().tickLabelStyle().setBold(true);
        pstyle.xAxisStyle().lineStyle().setColor("black");
        pstyle.yAxisStyle().lineStyle().setColor("black");
        pstyle.xAxisStyle().lineStyle().setThickness(2);
        pstyle.yAxisStyle().lineStyle().setThickness(2);

        // Force auto range to zero.
        pstyle.yAxisStyle().setParameter("allowZeroSuppression", "false");
        pstyle.xAxisStyle().setParameter("allowZeroSuppression", "false");

        // Title style.
        pstyle.titleStyle().textStyle().setFontSize(20);

        // Draw caps on error bars.
        pstyle.dataStyle().errorBarStyle().setParameter("errorBarDecoration", (new Float(1.0f)).toString());

        // Turn off grid lines until explicitly enabled.
        pstyle.gridStyle().setVisible(false);
        
        return pstyle;
    }
    
    public void test() {

        IHistogram1D h = histogramFactory.createHistogram1D("h1d", 50, 0, 50.0);
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            h.fill(rand.nextInt(50));
        }
        
        // Set labels for axes automatically based on title
        //h.annotation().addItem("xAxisLabel", h.title() + " X");
        //h.annotation().addItem("yAxisLabel", h.title() + " Y");

        // Create 3x3 regions for showing plots
        plotter.createRegions(3, 3, 0);

        // 0) Filled with bars.        
        IPlotterStyle pstyle = this.createDefaultStyle();        
        pstyle.xAxisStyle().setLabel("0) filled and bars");        
        plotter.region(0).plot(h, pstyle);
        plotter.region(0).setTitle("0) filled and bars");
        
        // 1) Filled plus outline.
        pstyle = this.createDefaultStyle();
        pstyle.xAxisStyle().setLabel("1) filled with no bars");
        pstyle.dataStyle().lineStyle().setVisible(false);
        pstyle.dataStyle().fillStyle().setColor("purple");        
        plotter.region(1).plot(h, pstyle);
        plotter.region(1).setTitle("1) filled with no bars");

        // 2) No fill with bars drawn.
        pstyle = this.createDefaultStyle();
        pstyle.dataStyle().lineStyle().setVisible(true);
        pstyle.dataStyle().lineStyle().setColor("green");
        pstyle.dataStyle().fillStyle().setVisible(false);                
        plotter.region(2).style().xAxisStyle().setLabel("2) filled with no bars");
        plotter.region(2).plot(h, pstyle);
        plotter.region(2).setTitle("2) bars with no fill");

        // 3) No fill with outline only.
        pstyle = this.createDefaultStyle();
        pstyle.dataStyle().lineStyle().setColor("blue");
        pstyle.dataStyle().lineStyle().setVisible(false);                
        pstyle.xAxisStyle().setLabel("3) contour of histogram only");        
        plotter.region(3).plot(h, pstyle);
        plotter.region(3).setTitle("3) contour of histogram only");

        // 4) Show errors only.
        pstyle = this.createDefaultStyle();
        pstyle.dataStyle().setVisible(false);        
        pstyle.xAxisStyle().setLabel("4) contour of histogram only");
        plotter.region(4).plot(h, pstyle);
        plotter.region(4).setTitle("4) errors only");

        // 5) Show data only in outline style.
        pstyle = this.createDefaultStyle();
        pstyle.dataStyle().setVisible(true);
        pstyle.dataStyle().errorBarStyle().setVisible(false);                
        pstyle.xAxisStyle().setLabel("5) data only");
        plotter.region(5).plot(h, pstyle);
        plotter.region(5).setTitle("5) data only");

        // 6) Show grid.
        pstyle = this.createDefaultStyle();
        pstyle.setVisible(false);
        pstyle.gridStyle().setVisible(true);
        pstyle.gridStyle().setLineType("dashed");
        pstyle.gridStyle().setColor("red");                        
        pstyle.xAxisStyle().setLabel("6) grid");
        plotter.region(6).plot(h, pstyle);
        plotter.region(6).setTitle("6) grid");
        
        // 7) Show data marker.
        pstyle = this.createDefaultStyle();
        pstyle.gridStyle().setVisible(false);
        pstyle.setVisible(true);
        pstyle.dataStyle().setVisible(false);
        pstyle.dataStyle().markerStyle().setVisible(true);
        pstyle.dataStyle().markerStyle().setShape("dot");
        pstyle.dataStyle().markerStyle().setSize(5);
        pstyle.dataStyle().errorBarStyle().setVisible(true);                
        pstyle.xAxisStyle().setLabel("7) data only");
        plotter.region(7).plot(h, pstyle);
        plotter.region(7).setTitle("7) data marker and errors");

        // 8) Show lines between points.
        pstyle = this.createDefaultStyle();
        pstyle.dataStyle().errorBarStyle().setVisible(false);
        pstyle.dataStyle().markerStyle().setVisible(false);
        pstyle.dataStyle().outlineStyle().setVisible(true);
        pstyle.dataStyle().outlineStyle().setColor("blue");
        pstyle.dataStyle().outlineStyle().setLineType("dotted");
        pstyle.dataStyle().outlineStyle().setThickness(5);                
        pstyle.xAxisStyle().setLabel("8) data only");
        plotter.region(8).plot(h, pstyle);
        plotter.region(8).setTitle("8) lines between points");
        
        // pstyle.xAxisStyle().setParameter(Style.AXIS_LOWER_LIMIT, "15.0");
        // pstyle.xAxisStyle().setParameter(Style.AXIS_UPPER_LIMIT, "40.0");
        // pstyle.yAxisStyle().setParameter(Style.AXIS_LOWER_LIMIT, "100.0");
        // pstyle.yAxisStyle().setParameter(Style.AXIS_UPPER_LIMIT, "300.0");
        
        for (int i=0; i<plotter.numberOfRegions(); i++) {            
            System.out.println(i + " : " + plotter.region(i).title());
        }
        
        mode();
    }
}

