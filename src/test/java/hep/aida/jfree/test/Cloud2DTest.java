package hep.aida.jfree.test;

import hep.aida.ICloud2D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

/**
 * This is basically an integration test. It converts AIDA objects to JFreeChart
 * representations and plots them.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Cloud2DTest extends AbstractPlotTest {

    public void test() {

        ICloud2D c2d = histogramFactory.createCloud2D("c2d");
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            c2d.fill(Math.abs(rand.nextDouble()) * 100, Math.abs(rand.nextDouble()) * 100);
        }
        
        // Create 3x3 regions for showing plots
        plotter.createRegion();

        IPlotterStyle pstyle = plotter.style();

        pstyle.dataStyle().markerStyle().setVisible(true);
        pstyle.dataStyle().markerStyle().setShape("dot");
        pstyle.dataStyle().markerStyle().setSize(1);
        pstyle.dataStyle().markerStyle().setColor("green");

        // background color
        // pstyle.regionBoxStyle().backgroundStyle().setColor("white");

        // Plot histograms into regions
        plotter.region(0).plot(c2d);
        
        mode();
    }
}
