package hep.aida.jfree.test;

import hep.aida.IAnalysisFactory;
import hep.aida.IHistogram1D;
import hep.aida.IPlotter;
import hep.aida.IPlotterFactory;
import hep.aida.IPlotterRegion;
import hep.aida.jfree.AnalysisFactory;
import hep.aida.jfree.plotter.Plotter;

import java.io.IOException;
import java.util.Random;

import junit.framework.TestCase;

/**
 * Basic tests of all the methods in the <code>IPlotter</code> interface.
 */
public final class PlotterTest extends TestCase {
    
    private IAnalysisFactory analysisFactory;
    private IPlotterFactory plotterFactory;
    
    public void setUp() {
        AnalysisFactory.register();
        AnalysisFactory.configure();
        analysisFactory = AnalysisFactory.create();
        plotterFactory = analysisFactory.createPlotterFactory();        
    }
    
    public void testPlotterType() {
        IPlotter plotter = plotterFactory.create();
        assertTrue("IPlotter does not have right type.", (plotter instanceof Plotter));
    }
    
    public void testCreateRegion() {
        IPlotter plotter = plotterFactory.create();
        plotter.createRegion();
        plotter.region(0).plot(createHistogram());
        showAndReset(plotter);        
    }
    
    public void testCreateRegionX() {        
        IPlotter plotter = plotterFactory.create();        
        plotter.createRegion(0.5);
        plotter.region(0).plot(createHistogram());
        showAndReset(plotter);
    }
    
    public void testCreateRegionXY() {
        IPlotter plotter = plotterFactory.create();        
        plotter.createRegion(0.5, 0.5);
        plotter.region(0).plot(createHistogram());
        showAndReset(plotter);
    }
    
    public void testCreateRegionXYW() {
        IPlotter plotter = plotterFactory.create();        
        plotter.createRegion(0.5, 0.5, 0.5);
        plotter.region(0).plot(createHistogram());
        showAndReset(plotter);
    }
    
    public void testCreateRegionXYWH() {
        IPlotter plotter = plotterFactory.create();        
        plotter.createRegion(0.5, 0.5, 0.5, 0.5);
        plotter.region(0).plot(createHistogram());
        showAndReset(plotter);
    }
    
    public void testCreateRegions() {
        IPlotter plotter = plotterFactory.create();        
        plotter.createRegions();
        plotter.region(0).plot(createHistogram());
        showAndReset(plotter);
    }
    
    public void testCreateRegionsColumns() {
        IPlotter plotter = plotterFactory.create();        
        plotter.createRegions(2);
        plotter.region(0).plot(createHistogram());
        plotter.region(1).plot(createHistogram());
        showAndReset(plotter);
    }
    
    public void testCreateRegionsColumnsRows() {
        IPlotter plotter = plotterFactory.create();
        plotter.createRegions(2, 2);
        plotter.region(0).plot(createHistogram());
        plotter.region(1).plot(createHistogram());
        plotter.region(2).plot(createHistogram());
        plotter.region(3).plot(createHistogram());
        showAndReset(plotter);
    }
    
    // FIXME: How is this actually supposed to work and how to test it?
    public void testCreateRegionsColumnsRowsStart() {
        IPlotter plotter = plotterFactory.create();
        plotter.createRegions(2, 2, 0);
        plotter.region(0).plot(createHistogram());
        plotter.region(1).plot(createHistogram());
        plotter.region(2).plot(createHistogram());
        plotter.region(3).plot(createHistogram());
        showAndReset(plotter);
    }
    
    public void testCurrentRegion() {
        IPlotter plotter = plotterFactory.create();
        IPlotterRegion region1 = plotter.createRegion();
        IPlotterRegion region2 = plotter.createRegion();
        assertEquals("Current region number not correct.", plotter.currentRegionNumber(), 0);
        IPlotterRegion currentRegion = plotter.next();
        assertEquals("Current region not correct.", region2, currentRegion);
        assertEquals("Current region number not correct.", plotter.currentRegionNumber(), 1);
        plotter.setCurrentRegionNumber(0);
        assertEquals("Current region number not correct.", plotter.currentRegionNumber(), 0);
        currentRegion = plotter.currentRegion();
        assertEquals("Current region not correct.", region1, currentRegion);
    }
    
    public void testDestroyRegions() {
        IPlotter plotter = plotterFactory.create();
        plotter.createRegion();
        plotter.createRegion();
        plotter.destroyRegions();
        assertEquals("Wrong number of regions after destroyRegions.", 0, plotter.numberOfRegions());
    }
    
    public void testHide() {
        IPlotter plotter = plotterFactory.create();
        
        plotter.hide();
        plotter.createRegion();
        
        plotter.hide();
        plotter.region(0).plot(createHistogram());
        
        plotter.show();
        pause(2000);
        plotter.hide();
        pause(2000);
        plotter.show();
        pause(2000);
        plotter.hide();
        plotter.destroyRegions();
        plotter.hide();
    }
    
    public void testInteract() {
        IPlotter plotter = plotterFactory.create();
        plotter.createRegion();
        plotter.interact();        
        plotter.region(0).plot(createHistogram());
        plotter.interact();
        plotter.show();
        plotter.interact();
        plotter.hide();
        plotter.interact();
        plotter.destroyRegions();        
    }
    
    public void testRegion() {
                
        IPlotter plotter = plotterFactory.create();
        IHistogram1D histogram = createHistogram();
        try {
            // Trying to plot into a non-existent region should fail.
            plotter.region(0).plot(histogram);
            TestCase.assertFalse("The plot operation should not have worked before region created.", true);
        } catch (IllegalArgumentException e) {
        }        
        plotter.createRegion();
        plotter.region(0).plot(histogram);
        showAndReset(plotter);
        try {
            // Trying to plot into a non-existent region after destroy should fail.
            plotter.region(0).plot(histogram);
            TestCase.assertFalse("The plot operation should not have worked after regions destroyed.", true);
        } catch (IllegalArgumentException e) {
        }
    }
    
    public void testNumberOfRegions() {
        IPlotter plotter = plotterFactory.create();
        plotter.createRegion();
        assertEquals("Wrong number of regions.", 1, plotter.numberOfRegions());
        plotter.destroyRegions();
        plotter.createRegion();
        plotter.createRegion();
        assertEquals("Wrong number of regions.", 2, plotter.numberOfRegions());
        plotter.destroyRegions();
        plotter.createRegions(4);
        assertEquals("Wrong number of regions.", 4, plotter.numberOfRegions());
        plotter.destroyRegions();
        plotter.createRegions(3, 3);
        assertEquals("Wrong number of regions.", 9, plotter.numberOfRegions());
        plotter.destroyRegions();
    }    
    
    public void testRefresh() {
        IPlotter plotter = plotterFactory.create();
        plotter.createRegion();
        plotter.refresh();
        plotter.region(0).plot(createHistogram());
        plotter.refresh();
        plotter.show();
        plotter.refresh();
        plotter.hide();
        plotter.refresh();        
        plotter.destroyRegions();
        plotter.refresh();        
    }
    
    public void testSetParameter() {
        IPlotter plotter = plotterFactory.create();
        plotter.setParameter("plotterWidth", "400");
        plotter.setParameter("plotterHeight", "600");
        assertEquals("Wrong parameter value.", plotter.parameterValue("plotterWidth"), "400");
        assertEquals("Wrong parameter value.", plotter.parameterValue("plotterHeight"), "600");
    }
    
    public void testPlotterWidthHeight() {
        
        IHistogram1D histogram = createHistogram();
        
        // Default width and height.
        IPlotter plotter = plotterFactory.create();
        plotter.createRegion();
        plotter.region(0).plot(histogram);
        showAndReset(plotter);
        
        // Custom width and height.
        plotter.setParameter("plotterWidth", "400");
        plotter.setParameter("plotterHeight", "600");
        plotter.createRegion();
        plotter.region(0).plot(histogram);
        showAndReset(plotter);
    }
    
    public void testSetTitle() {
        IPlotter plotter = plotterFactory.create();
        plotter.setTitle("My Special Plotter");
        plotter.createRegion();
        plotter.region(0).plot(createHistogram());
        showAndReset(plotter);
    }
    
    public void testWriteToFile() throws IOException {
        IPlotter plotter = plotterFactory.create();
        plotter.setTitle("My Special Plotter");
        plotter.createRegion();
        plotter.region(0).plot(createHistogram());
        plotter.writeToFile("target/test-output/PlotterTest");
    }
    
    public void testWriteToFileFormat() throws IOException {
        IPlotter plotter = plotterFactory.create();
        plotter.setTitle("My Special Plotter");
        plotter.createRegion();
        plotter.region(0).plot(createHistogram());
        plotter.writeToFile("target/test-output/PlotterTestFormat", "png");
        plotter.writeToFile("target/test-output/PlotterTestFormat", "jpeg");
        plotter.writeToFile("target/test-output/PlotterTestFormat", "gif");
    }
                               
    // This is actually not supported when using the standalone plotter.
    /*
    public void testTitleStyle() throws IOException {
        plotter.setTitleStyle(pf.createPlotterStyle().titleStyle());        
    }
    */
    
    public void testParameters() {
        IPlotter plotter = plotterFactory.create();
        System.out.println("printing parameter options ...");
        for (String parameter : plotter.availableParameters()) {
            System.out.println(parameter + " = " + plotter.parameterValue(parameter));
            for (String option : plotter.availableParameterOptions(parameter)) {
                System.out.println("  " + option);
            }
        }
    }
    
    IHistogram1D createHistogram() {
        // Create a test histogram.
        IHistogram1D histogram = analysisFactory.createHistogramFactory(null).createHistogram1D("h1d", 50, 0, 50.0);
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            histogram.fill(rand.nextInt(50));
        }
        return histogram;
    }
           
    private void showAndReset(IPlotter plotter) {
        plotter.show();
        this.pause(4000);
        plotter.hide();
        plotter.destroyRegions();
    }
    
    private void pause(int millis) {
        
        Object lock = new Object();
        synchronized(lock) {
            try {
                lock.wait(millis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }        
    }
}
