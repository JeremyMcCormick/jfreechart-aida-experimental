package hep.aida.jfree.test.interactive;

import hep.aida.IHistogram2D;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class InteractiveColorMapTest extends AbstractPlotTest {
    
    public void testNoData() {

        // Create a 2D histogram.
        IHistogram2D rainbowColorMap = histogramFactory.createHistogram2D("no data", 10, 0., 10., 20, 0., 20.);
        
        // Set style.
        rainbowColorMap.annotation().addItem("xAxisLabel", rainbowColorMap.title() + " X");
        rainbowColorMap.annotation().addItem("yAxisLabel", rainbowColorMap.title() + " Y");                
        
        style.dataStyle().fillStyle().setParameter("colorMapScheme", "rainbow");
        
        style.dataStyle().fillStyle().setParameter("showZeroHeightBins", Boolean.FALSE.toString());
        
        // Plot histogram into region.
        plotter.createRegion();
        plotter.region(0).plot(rainbowColorMap);
        plotter.show();
        
        Object object = new Boolean(true);
        synchronized(object) {
            try {
                object.wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }
    }        
    
    public void testRainbow() {

        // Create a 2D histogram.
        IHistogram2D rainbowColorMap = histogramFactory.createHistogram2D("rainbow", 10, 0., 10., 20, 0., 20.);
        
        // Set style.
        rainbowColorMap.annotation().addItem("xAxisLabel", rainbowColorMap.title() + " X");
        rainbowColorMap.annotation().addItem("yAxisLabel", rainbowColorMap.title() + " Y");                
        
        style.dataStyle().fillStyle().setParameter("colorMapScheme", "rainbow");
        
        style.dataStyle().fillStyle().setParameter("showZeroHeightBins", Boolean.FALSE.toString());
        
        // Plot histogram into region.
        plotter.createRegion();
        plotter.region(0).plot(rainbowColorMap);
        plotter.show();
                
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            Object object = new Boolean(true);
            synchronized(object) {
                try {
                    object.wait(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            double x = rand.nextInt(10);
            double y = rand.nextInt(20);
            rainbowColorMap.fill(x, y);
            if (i % 1000 == 0)
                System.out.println("fill #" + i);
        }                
    }    
    
    public void testGreyScale() {
        IHistogram2D greyScaleColorMap = histogramFactory.createHistogram2D("grayscale", 100, 0., 100., 100, 0., 100.);
        
        // Set axis labels.
        greyScaleColorMap.annotation().addItem("xAxisLabel", greyScaleColorMap.title() + " X");
        greyScaleColorMap.annotation().addItem("yAxisLabel", greyScaleColorMap.title() + " Y");
        
        // Set greyscale color map style.
        style.dataStyle().fillStyle().setParameter("colorMapScheme", "grayscale");
        
        // Plot histogram into region.
        plotter.createRegion();
        plotter.region(0).plot(greyScaleColorMap);
        plotter.show();
                
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            Object object = new Boolean(true);
            synchronized(object) {
                try {
                    object.wait(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            double x = rand.nextInt(100);
            double y = rand.nextInt(100);
            greyScaleColorMap.fill(x, y);
            if (i % 1000 == 0)
                System.out.println("fill #" + i);
        }
    }
    
    public void testCustomColorMap() {
        IHistogram2D greyScaleColorMap = histogramFactory.createHistogram2D("userdefined", 100, 0., 100., 100, 0., 100.);
        
        // Set axis labels.
        greyScaleColorMap.annotation().addItem("xAxisLabel", greyScaleColorMap.title() + " X");
        greyScaleColorMap.annotation().addItem("yAxisLabel", greyScaleColorMap.title() + " Y");
        
        // Set greyscale color map style.
        style.dataStyle().fillStyle().setParameter("colorMapScheme", "userdefined");
        style.dataStyle().fillStyle().setParameter("startColor", "BLUE");
        style.dataStyle().fillStyle().setParameter("endColor", "RED");
        
        style.dataStyle().fillStyle().setParameter("showZeroHeightBins", Boolean.TRUE.toString());
        
        // Plot histogram into region.
        plotter.createRegion();
        plotter.region(0).plot(greyScaleColorMap);
        plotter.show();
                
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            Object object = new Boolean(true);
            synchronized(object) {
                try {
                    object.wait(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            double x = rand.nextInt(100);
            double y = rand.nextInt(100);
            greyScaleColorMap.fill(x, y);
            if (i % 1000 == 0)
                System.out.println("fill #" + i);
        }
    }    
}