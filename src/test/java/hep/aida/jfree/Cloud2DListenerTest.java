package hep.aida.jfree;

import hep.aida.IAnalysisFactory;
import hep.aida.ICloud2D;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;
import hep.aida.IPlotterFactory;
import hep.aida.IPlotterStyle;

import java.util.Random;

import junit.framework.TestCase;

/**
 * This is basically an integration test. It converts AIDA objects to JFreeChart
 * representations and plots them.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Cloud2DListenerTest extends TestCase {

    IAnalysisFactory af;
    IPlotterFactory pf;
    IHistogramFactory hf;

    protected void setUp() {
        AnalysisFactory.register();
        af = IAnalysisFactory.create();
        pf = af.createPlotterFactory();
        hf = af.createHistogramFactory(null);
    }

    // Create a 2D cloud with randomly distributed points
    private final ICloud2D cloud2D() {
        ICloud2D c2d = hf.createCloud2D("c2d");
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            c2d.fill(Math.abs(rand.nextDouble()) * 100, Math.abs(rand.nextDouble()) * 100);
        }
        return c2d;
    }

    public void testCloud2D() throws Exception {

        // Create plotter
        IPlotter plotter = pf.create();

        // Create a list with various types of histograms
        ICloud2D c2d = cloud2D();

        // Set labels for axes automatically based on title
        c2d.annotation().addItem("xAxisLabel", c2d.title() + " X");
        c2d.annotation().addItem("yAxisLabel", c2d.title() + " Y");

        // Create 3x3 regions for showing plots
        plotter.createRegion();

        IPlotterStyle pstyle = plotter.style();

        pstyle.dataStyle().markerStyle().setVisible(true);

        // Plot histograms into regions
        plotter.region(0).plot(c2d, pstyle);

        // Show time
        plotter.show();

        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            c2d.fill(Math.abs(rand.nextDouble()) * 100, Math.abs(rand.nextDouble()) * 100);
            Thread.sleep(500);
        }
    }

    public void tearDown() {
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
