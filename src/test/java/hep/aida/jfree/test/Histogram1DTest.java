package hep.aida.jfree.test;

import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.plot.style.DefaultHistogram1DStyle;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram1DTest extends AbstractPlotTest {

    public void test() {

        // Create a simple histogram.
        IHistogram1D h1d = histogramFactory.createHistogram1D("h1d", 50, -3, 6);
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            h1d.fill(rand.nextGaussian());
        }

        h1d.annotation().addItem("xAxisLabel", "Value");
        h1d.annotation().addItem("yAxisLabel", "Entries");
        
        IPlotterStyle pstyle = new DefaultHistogram1DStyle();
        
        //plotter.createRegions(1, 2, 0);
        plotter.createRegion();
        plotter.region(0).plot(h1d, pstyle);
        
        mode();
        
        //pstyle.dataBoxStyle().backgroundStyle().setColor("gray");
        //plotter.region(1).plot(h1d, pstyle);
    }    
}

