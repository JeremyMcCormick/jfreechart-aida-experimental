package hep.aida.jfree.test.interactive;

import hep.aida.IProfile2D;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

public class InteractiveProfile2DTest extends AbstractPlotTest {

    public void testProfile2D() {
                       
        // Create 1D and 2D IProfile with fixed bin width
        IProfile2D prof2DFixedBinWidth = histogramFactory.createProfile2D(
                "prof2DFixedBinWidth",
                "Fixed bin width 2D", 
                10, 0, 1, 10, -5, 5);

        double[] xBinEdges = {0,0.1,0.21,0.35,0.48,0.52,0.65,0.75,0.83,0.94,1.0};
        double[] yBinEdges = {-5.0,-4.1,-3.2,-2.0,-1.1,-0.4,1.2,2.3,3.5,4.2,5.0};

        // Create 1D and 2D IProfile with variable bin width
        IProfile2D prof2DVariableBinWidth = 
                histogramFactory.createProfile2D("prof2DVariableBinWidth", "Variable bin width 2D", xBinEdges, yBinEdges);
        
        plotter.createRegions(2);
        plotter.region(0).plot(prof2DFixedBinWidth);
        plotter.region(1).plot(prof2DVariableBinWidth);
        plotter.show();
        
        Random r = new Random();
        for ( int i = 0; i < 1000; i++ ) {
          double x = r.nextDouble();
          double y = x + 0.1*r.nextGaussian();

          // Fill the IProfiles with default weight.
          prof2DFixedBinWidth.fill(x,y, r.nextDouble());
          prof2DVariableBinWidth.fill(x,y, r.nextDouble());
          
          synchronized (Thread.currentThread()) {
              try {
                Thread.currentThread().wait(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
          }
        }
    }   
}
