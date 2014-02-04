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

    protected void plot() {

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
        
        //pstyle.dataBoxStyle().backgroundStyle().setColor("gray");
        //plotter.region(1).plot(h1d, pstyle);
    }
    
    /*
    protected void plot() {

        // Create a simple histogram.
        IHistogram1D h1d = histogramFactory.createHistogram1D("h1d", 11, 0., 11.0);
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            int value = rand.nextInt(10);
            if (value % 2 == 0 || value == 3)
                h1d.fill(value);
        }

        h1d.annotation().addItem("xAxisLabel", h1d.title() + " X");
        h1d.annotation().addItem("yAxisLabel", h1d.title() + " Y");
        
        IPlotterStyle pstyle = new DefaultHistogram1DStyle();
        
        plotter.createRegions(1, 2, 0);
        
        plotter.region(0).plot(h1d, pstyle);
        
        pstyle.dataBoxStyle().backgroundStyle().setColor("gray");
        plotter.region(1).plot(h1d, pstyle);
    }
    */
}

