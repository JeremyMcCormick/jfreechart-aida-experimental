package hep.aida.jfree.test.interactive;

import hep.aida.IAnalysisFactory;
import hep.aida.IHistogram2D;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;
import hep.aida.IPlotterFactory;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.AnalysisFactory;

import java.util.Random;

import junit.framework.TestCase;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class InteractiveColorMapTest extends TestCase {

    protected IAnalysisFactory analysisFactory;
    protected IPlotterFactory plotterFactory;
    protected IHistogramFactory histogramFactory;
    protected IPlotter plotter;
    protected IPlotterStyle style;
    
    protected void setUp() {
        AnalysisFactory.register();
        analysisFactory = IAnalysisFactory.create();
        plotterFactory = analysisFactory.createPlotterFactory();
        histogramFactory = analysisFactory.createHistogramFactory(null);
        plotter = plotterFactory.create();
        
        style = plotter.style();
        style.gridStyle().setVisible(false);
        style.setParameter("hist2DStyle", "colorMap");
    }
    
    public void testRainbow() {

        // Create a 2D histogram.
        IHistogram2D rainbowColorMap = histogramFactory.createHistogram2D("rainbow", 100, 0., 100., 100, 0., 100.);
        
        // Set style.
        rainbowColorMap.annotation().addItem("xAxisLabel", rainbowColorMap.title() + " X");
        rainbowColorMap.annotation().addItem("yAxisLabel", rainbowColorMap.title() + " Y");                
        
        style.dataStyle().fillStyle().setParameter("colorMapScheme", "COLORMAP_RAINBOW");
        
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
            double x = rand.nextInt(100);
            double y = rand.nextInt(100);
            rainbowColorMap.fill(x, y);
            if (i % 1000 == 0)
                System.out.println("fill #" + i);
        }                
    }    
    
    public void testGreyScale() {
        IHistogram2D greyScaleColorMap = histogramFactory.createHistogram2D("greyscale", 100, 0., 100., 100, 0., 100.);
        
        // Set axis labels.
        greyScaleColorMap.annotation().addItem("xAxisLabel", greyScaleColorMap.title() + " X");
        greyScaleColorMap.annotation().addItem("yAxisLabel", greyScaleColorMap.title() + " Y");
        
        // Set greyscale color map style.
        style.dataStyle().fillStyle().setParameter("colorMapScheme", "COLORMAP_GRAYSCALE");
        
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
        IHistogram2D greyScaleColorMap = histogramFactory.createHistogram2D("greyscale", 100, 0., 100., 100, 0., 100.);
        
        // Set axis labels.
        greyScaleColorMap.annotation().addItem("xAxisLabel", greyScaleColorMap.title() + " X");
        greyScaleColorMap.annotation().addItem("yAxisLabel", greyScaleColorMap.title() + " Y");
        
        // Set greyscale color map style.
        style.dataStyle().fillStyle().setParameter("colorMapScheme", "COLORMAP_USERDEFINED");
        style.dataStyle().fillStyle().setParameter("startColor", "BLUE");
        style.dataStyle().fillStyle().setParameter("endColor", "RED");
        
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