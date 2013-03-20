package hep.aida.jfree;

import hep.aida.IHistogram2D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram2DTest extends AbstractPlotTest {

    private void histogramExample() {

        // Create a 1D histo
        IHistogram2D h2d = histogramFactory.createHistogram2D("h2d", 100, 0., 100., 100, 0., 100.);

        // Set labels for axes.
        h2d.annotation().addItem("xAxisLabel", h2d.title() + " X");
        h2d.annotation().addItem("yAxisLabel", h2d.title() + " Y");

        IPlotterStyle pstyle = plotter.style();

        pstyle.xAxisStyle().setParameter("allowZeroSuppression", "false");
        pstyle.yAxisStyle().setParameter("allowZeroSuppression", "false");

        // background color
        pstyle.regionBoxStyle().backgroundStyle().setColor("white");

        // Log scale.
        // pstyle.zAxisStyle().setParameter("scale", "log");

        // Plot histogram into region.
        plotter.createRegions(1, 2, 0);

        // Display as color map.
        plotter.region(0).plot(h2d, pstyle);

        // Display as box plot.
        pstyle.setParameter("hist2DStyle", "box");
        pstyle.dataStyle().lineStyle().setVisible(true);
        pstyle.dataStyle().lineStyle().setColor("blue");
        pstyle.dataStyle().fillStyle().setVisible(true);
        pstyle.dataStyle().fillStyle().setColor("blue");
        plotter.region(1).plot(h2d, pstyle);

        Random rand = new Random();
        for (int i = 0; i < 100000000; i++) {
            h2d.fill(rand.nextInt(100), rand.nextInt(100));
        }
    }
    
    public void testHistogram() {
        histogramExample();
        mode();
    }
}
