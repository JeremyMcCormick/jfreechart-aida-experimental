package hep.aida.jfree.test;

import hep.aida.IHistogram2D;
import hep.aida.IPlotterStyle;

import java.util.Random;


public class Histogram2DOverlayTest extends AbstractPlotTest {
    
    public void testHistogram2DOverlay() {
        
        this.setBatchMode(false);
        
        // Create a 2D histogram.
        IHistogram2D histogram1 = histogramFactory.createHistogram2D("Histogram2D", 10, 0., 10., 10, 0., 10.);

        // Fill the histogram with random data.
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            histogram1.fill(rand.nextInt(10), rand.nextInt(10));
        }
        
        // Create another 2D histogram.
        IHistogram2D histogram2 = histogramFactory.createHistogram2D("Another Histogram2D", 10, 0., 10., 10, 0., 10.);

        // Fill the histogram with random data.
        for (int i = 0; i < 2000; i++) {
            histogram2.fill(rand.nextInt(10), rand.nextInt(10));
        }

        plotter.createRegion();
        
        IPlotterStyle style = plotterFactory.createPlotterStyle();
        style.setParameter("hist2DStyle", "box");
        style.dataStyle().lineStyle().setVisible(true);
        style.dataStyle().lineStyle().setColor("green");        
        plotter.region(0).plot(histogram1, style);
        style = plotterFactory.createPlotterStyle();
        style.setParameter("hist2DStyle", "box");
        style.dataStyle().lineStyle().setVisible(true);
        style.dataStyle().lineStyle().setColor("red");
        //plotter.region(0).plot(histogram2, style);
                
        mode();
    }

}
