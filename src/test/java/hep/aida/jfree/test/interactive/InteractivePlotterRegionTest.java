package hep.aida.jfree.test.interactive;

import hep.aida.IHistogram1D;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;


public class InteractivePlotterRegionTest extends AbstractPlotTest {
    
    public void testClear() {
        IHistogram1D histogram = histogramFactory.createHistogram1D("h1d", 10, 0., 10);
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            histogram.fill(random.nextDouble() * 10.0);
        }
        
        plotter.createRegion();
        plotter.region(0).plot(histogram);
        plotter.show();
        
        _wait(2000);
                
        plotter.region(0).clear();
        
        _wait(2000);
        
        plotter.region(0).plot(histogram);
        
        _wait(5000);        
    }
    
    public void _wait(int waitTime) {
        Boolean object = new Boolean(true);
        synchronized (object) {
            try {
                object.wait(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }

}
