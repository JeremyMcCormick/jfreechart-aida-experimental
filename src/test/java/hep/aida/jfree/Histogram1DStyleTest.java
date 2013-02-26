package hep.aida.jfree;

import hep.aida.IAnalysisFactory;
import hep.aida.IHistogram1D;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;
import hep.aida.IPlotterFactory;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.converter.Style;

import java.awt.Color;
import java.util.Random;

import junit.framework.TestCase;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Histogram1DStyleTest extends TestCase
{
    IAnalysisFactory af;
    IPlotterFactory pf;
    IHistogramFactory hf;
    
    int nfills = 10000;
    int range = 50;

    protected void setUp()
    {
        AnalysisFactory.register();
        af = IAnalysisFactory.create();
        pf = af.createPlotterFactory();
        hf = af.createHistogramFactory(null);
    }

    // Create a 1D histogram with random Gaussian distribution
    private final IHistogram1D histogram1D()
    {
        IHistogram1D h1d = hf.createHistogram1D("h1d", 50, 0, 50.0);
        Random rand = new Random();
        for (int i = 0; i < nfills; i++) {
            h1d.fill(rand.nextInt(range));
        }
        return h1d;
    }

    public void test() throws Exception
    {

        // Create plotter
        IPlotter plotter = pf.create();

        // Create a test histogram which will be used to show
        // various style options.
        IHistogram1D h = histogram1D();

        // Set labels for axes automatically based on title
        h.annotation().addItem("xAxisLabel", h.title() + " X");
        h.annotation().addItem("yAxisLabel", h.title() + " Y");

        // Create 3x3 regions for showing plots
        plotter.createRegions(3, 3, 0);

        // Get the plotter style.
        IPlotterStyle pstyle = plotter.style();
        
        pstyle.xAxisStyle().labelStyle().setBold(true);
        pstyle.yAxisStyle().labelStyle().setBold(true);
        pstyle.xAxisStyle().tickLabelStyle().setBold(true);
        pstyle.yAxisStyle().tickLabelStyle().setBold(true);
        pstyle.xAxisStyle().lineStyle().setColor("black");
        pstyle.yAxisStyle().lineStyle().setColor("black");
        /*
        pstyle.xAxisStyle().lineStyle().setThickness(4);
        pstyle.yAxisStyle().lineStyle().setThickness(4);
        */
        
        // Title style.
        pstyle.titleStyle().textStyle().setFontSize(20);

        // Draw caps on error bars.
        pstyle.dataStyle().errorBarStyle().setParameter("errorBarDecoration", (new Float(1.0f)).toString());

        // Turn off grid lines until explicitly enabled.
        pstyle.gridStyle().setVisible(false);
        
        // 0) Default style.        
        plotter.region(0).plot(h, pstyle);
        plotter.region(0).setTitle("0) filled and bars");

        // 1) Filled plus outline.
        pstyle.dataStyle().lineStyle().setVisible(false);
        pstyle.dataStyle().fillStyle().setColor("purple");
        plotter.region(1).plot(h, pstyle);
        plotter.region(1).setTitle("1) filled with no bars");

        // 2) No fill with bars drawn.
        pstyle.dataStyle().lineStyle().setVisible(true);
        pstyle.dataStyle().fillStyle().setVisible(false);
        plotter.region(2).plot(h, pstyle);
        plotter.region(2).setTitle("2) bars with no fill");
        
        // 3) No fill with outline only.
        pstyle.dataStyle().lineStyle().setVisible(false);
        plotter.region(3).plot(h, pstyle);
        plotter.region(3).setTitle("3) contour of histogram only");

        // 4) Show errors only.
        pstyle.dataStyle().setVisible(false);
        plotter.region(4).plot(h, pstyle);
        plotter.region(4).setTitle("4) errors only");

        // 5) Show data only in outline style.
        pstyle.dataStyle().setVisible(true);
        pstyle.dataStyle().errorBarStyle().setVisible(false);
        plotter.region(5).plot(h, pstyle);
        plotter.region(5).setTitle("5) data only");

        // 6) Show grid.
        pstyle.setVisible(false);
        pstyle.gridStyle().setVisible(true);
        pstyle.gridStyle().setLineType("dashed");
        pstyle.gridStyle().setColor("red");
        plotter.region(6).plot(h, pstyle);
        plotter.region(6).setTitle("6) grid");        
        
        // 7) Show data marker.
        pstyle.gridStyle().setVisible(false);
        pstyle.setVisible(true);
        pstyle.dataStyle().setVisible(false);
        pstyle.dataStyle().markerStyle().setVisible(true);
        pstyle.dataStyle().markerStyle().setShape("dot");
        pstyle.dataStyle().markerStyle().setSize(5);
        plotter.region(7).plot(h, pstyle);
        plotter.region(7).setTitle("7) data marker");
        
        // 8) Show lines between points.
        pstyle.dataStyle().markerStyle().setVisible(false);
        pstyle.dataStyle().outlineStyle().setVisible(true);
        pstyle.dataStyle().outlineStyle().setColor("blue");
        pstyle.dataStyle().outlineStyle().setLineType("dotted");
        pstyle.dataStyle().outlineStyle().setThickness(5);
        plotter.region(8).plot(h, pstyle);
        plotter.region(8).setTitle("8) lines between points");
          
        // Show time.
        plotter.show();
    }

    public void tearDown()
    {
        System.out.println("Hit Ctrl + C to exit.");
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
