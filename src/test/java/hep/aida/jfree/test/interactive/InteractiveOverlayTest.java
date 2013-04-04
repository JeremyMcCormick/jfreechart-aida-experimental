package hep.aida.jfree.test.interactive;

import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class InteractiveOverlayTest extends AbstractPlotTest {

    protected void plot() {

        // Create a list with various types of histograms
        IHistogram1D h1d = histogramFactory.createHistogram1D("h1d", 50, -5.0, 5.0);
        
        IHistogram1D h1dOverlay = histogramFactory.createHistogram1D("h1d", 50, -5.0, 5.0);
        
        h1d.annotation().addItem("xAxisLabel", h1d.title() + " X");
        h1d.annotation().addItem("yAxisLabel", h1d.title() + " Y");

        // Create 3x3 regions for showing plots
        // plotter.createRegions(3, 3, 0);
        plotter.createRegion();

        IPlotterStyle pstyle = plotter.style();

        pstyle.dataStyle().errorBarStyle().setVisible(true);
        pstyle.dataStyle().fillStyle().setVisible(false);
        pstyle.dataStyle().lineStyle().setVisible(true);
        pstyle.gridStyle().setVisible(false);

        // Plot first histogram.
        pstyle.dataStyle().lineStyle().setColor("blue");
        plotter.region(0).plot(h1d, pstyle);

        pstyle.dataStyle().lineStyle().setColor("red");
        plotter.region(0).plot(h1dOverlay, pstyle);

        // Overlay histograms in real time.
        
        if (!this.batchMode) {
            plotter.show();
        }
        
        Random rand = new Random();
        for (int i = 0; i < 1000000000; i++) {
            h1d.fill(rand.nextGaussian());      
            h1dOverlay.fill(rand.nextGaussian());
            //try {
            //    Thread.sleep(1000);
            //} catch (InterruptedException e) {                
            //}
        }
    }
    
    public void test() {
        setBatchMode(false);
        plot();
    }
}
