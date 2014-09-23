package hep.aida.jfree.test.interactive;

import hep.aida.IHistogram2D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class InteractiveHistogram2DTest extends AbstractPlotTest {

    public InteractiveHistogram2DTest() {
        setBatchMode(false);
        setWaitTime(30000);
    }

    public void testBoxPlot() {
        
        // Create a 2D histogram.
        IHistogram2D h2d = histogramFactory.createHistogram2D("h2d", 2, 0., 2., 2, 0., 2.);

        // Set labels for the axes.
        h2d.annotation().addItem("xAxisLabel", h2d.title() + " X");
        h2d.annotation().addItem("yAxisLabel", h2d.title() + " Y");

        IPlotterStyle pstyle = plotter.style();
        
        pstyle.gridStyle().setVisible(false);

        pstyle.xAxisStyle().setParameter("allowZeroSuppression", "false");
        pstyle.yAxisStyle().setParameter("allowZeroSuppression", "false");

        // background color
        // pstyle.regionBoxStyle().backgroundStyle().setColor("white");

        // Log scale.
        // pstyle.zAxisStyle().setParameter("scale", "log");

        // Plot histogram into region.
        // plotter.createRegions(1, 2, 0);

        // Display as color map.
        // plotter.region(0).plot(h2d, pstyle);

        // Display as box plot.
        pstyle.setParameter("hist2DStyle", "box");
        pstyle.dataStyle().lineStyle().setVisible(true);
        pstyle.dataStyle().lineStyle().setColor("blue");
        pstyle.dataStyle().fillStyle().setVisible(false);
        pstyle.dataStyle().fillStyle().setColor("blue");

        plotter.createRegion();
        System.out.println("doing plot ...");
        plotter.region(0).plot(h2d, pstyle);
        
        // Fill the histogram with random data.
        
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            h2d.fill(rand.nextDouble() * 2.0, rand.nextDouble() * 2.0);            
        }
        
        mode();
    }
    
    public void testVariableBinWidth() {
        
        double[] xBinEdges = { 0.0, 1.0, 3.0, 6.0, 10.0 };
        double[] yBinEdges = { 0.0, 5.0, 15.0, 30.0 };
        
        // Create a 2D histogram.
        IHistogram2D histogram = histogramFactory.createHistogram2D(
                "/H2DVariableBinWidth", 
                "H2D Variable Bin Width",
                xBinEdges,
                yBinEdges);

        IPlotterStyle pstyle = plotter.style();        
        pstyle.gridStyle().setVisible(true);

        pstyle.xAxisStyle().setParameter("allowZeroSuppression", "false");
        pstyle.yAxisStyle().setParameter("allowZeroSuppression", "false");

        // background color
        // pstyle.regionBoxStyle().backgroundStyle().setColor("white");

        // Log scale.
        // pstyle.zAxisStyle().setParameter("scale", "log");

        // Plot histogram into region.
        // plotter.createRegions(1, 2, 0);

        // Display as color map.
        // plotter.region(0).plot(h2d, pstyle);

        // Display as box plot.
        pstyle.setParameter("hist2DStyle", "box");
        pstyle.dataStyle().lineStyle().setVisible(true);
        pstyle.dataStyle().lineStyle().setColor("blue");
        pstyle.dataStyle().fillStyle().setVisible(false);
        pstyle.dataStyle().fillStyle().setColor("blue");

        plotter.createRegion();
        plotter.region(0).plot(histogram, pstyle);
        
        // Fill the histogram with random data.
        Random rand = new Random();
        for (int i = 0; i < 100000; i++) {
            histogram.fill(rand.nextDouble() * 10, rand.nextDouble() * 30);
        }
        
        mode();
    }

}
