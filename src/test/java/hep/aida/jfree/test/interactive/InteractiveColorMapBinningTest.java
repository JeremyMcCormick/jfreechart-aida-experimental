package hep.aida.jfree.test.interactive;

import hep.aida.IHistogram2D;
import hep.aida.jfree.test.AbstractPlotTest;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class InteractiveColorMapBinningTest extends AbstractPlotTest {
    
    public void testRainbow() {
        
        // Create a 2D histogram.
        IHistogram2D rainbowColorMap = histogramFactory.createHistogram2D("rainbow", 5, -2.5, 2.5, 7, -3.5, 3.5);
        
        // Set style.
        rainbowColorMap.annotation().addItem("xAxisLabel", rainbowColorMap.title() + " X");
        rainbowColorMap.annotation().addItem("yAxisLabel", rainbowColorMap.title() + " Y");                
        
        style.dataStyle().fillStyle().setParameter("colorMapScheme", "COLORMAP_RAINBOW");
        
        // Plot histogram into region.
        plotter.createRegion();
        plotter.region(0).plot(rainbowColorMap);
        plotter.show();
                
        double xValue = -2;
        double yValue = -3;
        for (int x=0; x<5; x++) {
            for (int y=0; y<7; y++) {
                System.out.println("fill(x,y) = " + xValue + "," + yValue);
                rainbowColorMap.fill(xValue, yValue);
                yValue += 1.;
            }            
            yValue = -3;
            xValue += 1.;
        }
        
        pause(5000);        
    }
}