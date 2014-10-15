package hep.aida.jfree.test.interactive;

import hep.aida.IProfile1D;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

public class InteractiveProfile1DTest extends AbstractPlotTest {

    public void test() {

        setBatchMode(false);
        setWaitTime(100000);
        
        // Create 1D and 2D IProfile with fixed bin width
        IProfile1D prof1DFixedBinWidth = histogramFactory.createProfile1D("prof1DFixedBinWidth", "Fixed bin width 1D", 10, 0, 1);

        double[] xBinEdges = { 0, 0.1, 0.21, 0.35, 0.48, 0.52, 0.65, 0.75, 0.83, 0.94, 1.0 };

        // Create 1D IProfile with variable bin width
        IProfile1D prof1DVariableBinWidth = histogramFactory.createProfile1D("prof1DVariableBinWidth", "Variable bin width 1D", xBinEdges);

        // Display the results
        plotter.createRegions(2, 1);
        plotter.region(0).plot(prof1DFixedBinWidth);
        plotter.region(1).plot(prof1DVariableBinWidth);
        plotter.show();
        
        Random r = new Random();

        for (int i = 0; i < 1000; i++) {
            double x = r.nextDouble();
            double y = x + 0.1 * r.nextGaussian();

            // Fill the IProfiles with default weight.
            prof1DFixedBinWidth.fill(x, y);
            prof1DVariableBinWidth.fill(x, y);
            
            synchronized (Thread.currentThread()) {
                try {
                    Thread.currentThread().wait(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }            
        }
        plotter.show();
        
        mode();
    }
}