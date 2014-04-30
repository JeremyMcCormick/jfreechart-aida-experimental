package hep.aida.jfree.test.interactive;

import hep.aida.ICloud2D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class InteractiveCloud2DTest extends AbstractPlotTest {

    protected void plot() {

        ICloud2D c2d = histogramFactory.createCloud2D("/", "c2d", 0, "autoconvert=false");

        // Set labels for axes automatically based on title.
        c2d.annotation().addItem("xAxisLabel", "X Axis");
        c2d.annotation().addItem("yAxisLabel", "Y Axis");

        // Create 3x3 regions for showing plots.
        plotter.createRegion();

        IPlotterStyle pstyle = plotter.style();

        pstyle.dataStyle().markerStyle().setVisible(true);
        pstyle.dataStyle().markerStyle().setSize(1);

        // Plot histograms into regions.
        plotter.region(0).plot(c2d, pstyle);

        // Show time.
        plotter.show();

        Random rand = new Random();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            Object object = new Boolean(true);
            synchronized(object) {
                try {
                    object.wait(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            c2d.fill(Math.abs(rand.nextDouble()) * 1000000, Math.abs(rand.nextDouble()) * 1000000);
        }
        
        mode();
    }    
}