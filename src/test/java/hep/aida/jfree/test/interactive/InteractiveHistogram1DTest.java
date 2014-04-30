package hep.aida.jfree.test.interactive;

import hep.aida.IHistogram1D;
import hep.aida.IPlotter;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class InteractiveHistogram1DTest extends AbstractPlotTest {
        
    public void testHistogram1D() {

        IHistogram1D histogram = histogramFactory.createHistogram1D("h1d", 50, -3, 6);

        histogram.annotation().addItem("xAxisLabel", "Value");
        histogram.annotation().addItem("yAxisLabel", "Entries");
        
        //IPlotterStyle pstyle = new DefaultHistogram1DStyle();
        
        //plotter.createRegions(1, 2, 0);
        plotter.createRegion();
        //plotter.region(0).plot(h1d, pstyle);
        plotter.region(0).plot(histogram);
        
        // Call show() here to see interactive updating of plot.
        plotter.show();
        
        Random rand = new Random();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            histogram.fill(rand.nextGaussian());
        }        
        
        mode();
    }
    
    public void testVariableBin() throws Exception {
        
        double[] binEdges = { 0.0, 5.0, 10.0, 20.0, 40.0, 80.0, 100.0 };
        IHistogram1D histogram = histogramFactory.createHistogram1D(
                "/Hist1DVariableBinWidths", 
                "Variable Bin Histogram 1D",
                binEdges);
                        
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            histogram.fill(rand.nextDouble() * 100);
        }

        IPlotter plotter = analysisFactory.createPlotterFactory().create();
        plotter.createRegion();
        plotter.region(0).plot(histogram);
        
        plotter.style().statisticsBoxStyle().setVisible(true);
        
        mode();
    }
}

