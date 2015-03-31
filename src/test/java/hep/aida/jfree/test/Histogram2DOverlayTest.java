package hep.aida.jfree.test;

import hep.aida.IHistogram2D;
import hep.aida.IPlotterStyle;

import java.util.Random;


public class Histogram2DOverlayTest extends AbstractPlotTest {
    
    public void test() {
        
        this.setBatchMode(false);
        this.setWaitTime(10);
        
        // Create a 2D histogram.
        IHistogram2D histogram1 = histogramFactory.createHistogram2D("Histogram2D", 10, 0., 10., 10, 0., 10.);

        // Create another 2D histogram.
        IHistogram2D histogram2 = histogramFactory.createHistogram2D("Another Histogram2D", 10, 0., 10., 10, 0., 10.);

        plotter.createRegion();
        
        IPlotterStyle style = plotterFactory.createPlotterStyle();
        style.setParameter("hist2DStyle", "box");
        style.dataStyle().fillStyle().setVisible(false);
        style.dataStyle().lineStyle().setVisible(true);
        style.dataStyle().lineStyle().setColor("green");        
        plotter.region(0).plot(histogram1, style);
        style = plotterFactory.createPlotterStyle();
        style.setParameter("hist2DStyle", "box");
        style.dataStyle().fillStyle().setVisible(false);
        style.dataStyle().lineStyle().setVisible(true);
        style.dataStyle().lineStyle().setColor("red");
        plotter.region(0).plot(histogram2, style);
        plotter.show();
                
        // Fill the histogram with random data.
        Random rand = new Random();
        for (int i = 0; i < 100000; i++) {            
            if (i % 2 == 0)
                histogram1.fill(rand.nextDouble() * 10, rand.nextDouble() * 10);
            else 
                histogram2.fill(rand.nextDouble() * 10, rand.nextDouble() * 10);
            try {
                synchronized (Thread.currentThread()) {
                    Thread.currentThread().wait(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
