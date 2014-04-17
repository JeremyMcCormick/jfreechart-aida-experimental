package hep.aida.jfree.test;

import hep.aida.IHistogram2D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class ColorMapTest extends AbstractPlotTest {

    protected void plot() {

        // Create a 1D histo
        IHistogram2D h2d = histogramFactory.createHistogram2D("h2d", 100, 0., 100., 100, 0., 100.);

        // Set labels for axes.
        h2d.annotation().addItem("xAxisLabel", h2d.title() + " X");
        h2d.annotation().addItem("yAxisLabel", h2d.title() + " Y");

        IPlotterStyle pstyle = plotter.style();
        
        style.setParameter("hist2DStyle", "colorMap");

        pstyle.xAxisStyle().setParameter("allowZeroSuppression", "false");
        pstyle.yAxisStyle().setParameter("allowZeroSuppression", "false");
        pstyle.zAxisStyle().setParameter("scale", "log");

        // Plot histogram into region.
        plotter.createRegion();
        plotter.region(0).plot(h2d, pstyle);
                
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            h2d.fill(rand.nextInt(100), rand.nextInt(100));
        }
    }
}
