package hep.aida.jfree.test;

import hep.aida.IHistogram2D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram2DTest extends AbstractPlotTest {

    protected void plot() {

        // Create a 2D histogram.
        IHistogram2D h2d = histogramFactory.createHistogram2D("h2d", 50, 0., 500., 50, 0., 500.);

        // Fill the histogram with random data.
        Random rand = new Random();
        for (int i = 0; i < 20000; i++) {
            h2d.fill(rand.nextInt(500), rand.nextInt(500));
        }

        // Set labels for the axes.
        h2d.annotation().addItem("xAxisLabel", h2d.title() + " X");
        h2d.annotation().addItem("yAxisLabel", h2d.title() + " Y");

        IPlotterStyle pstyle = plotter.style();

        pstyle.xAxisStyle().setParameter("allowZeroSuppression", "false");
        pstyle.yAxisStyle().setParameter("allowZeroSuppression", "false");

        // background color
        //pstyle.regionBoxStyle().backgroundStyle().setColor("white");

        // Log scale.
        // pstyle.zAxisStyle().setParameter("scale", "log");

        // Plot histogram into region.
        //plotter.createRegions(1, 2, 0);

        // Display as color map.
        //plotter.region(0).plot(h2d, pstyle);

        // Display as box plot.
        pstyle.setParameter("hist2DStyle", "box");
        pstyle.dataStyle().lineStyle().setVisible(true);
        pstyle.dataStyle().lineStyle().setColor("blue");
        pstyle.dataStyle().fillStyle().setVisible(true);
        pstyle.dataStyle().fillStyle().setColor("blue");
                 
        plotter.createRegion();
        plotter.region(0).plot(h2d, pstyle);
    }
}
