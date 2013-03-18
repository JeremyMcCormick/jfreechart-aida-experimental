package hep.aida.jfree;

import hep.aida.IAnalysisFactory;
import hep.aida.IHistogram1D;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;
import hep.aida.IPlotterFactory;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.converter.MarkerUtil;

import java.util.Random;

import junit.framework.TestCase;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class MarkerTest extends TestCase {

    IAnalysisFactory af;
    IPlotterFactory pf;
    IHistogramFactory hf;

    int nfills = 100;
    int range = 10;

    protected void setUp() {
        AnalysisFactory.register();
        af = IAnalysisFactory.create();
        pf = af.createPlotterFactory();
        hf = af.createHistogramFactory(null);
    }

    // Create a 1D histogram with random Gaussian distribution
    private final IHistogram1D histogram1D() {
        IHistogram1D h1d = hf.createHistogram1D("h1d", 10, 0, 10.0);
        Random rand = new Random();
        for (int i = 0; i < nfills; i++) {
            h1d.fill(rand.nextInt(range));
        }
        return h1d;
    }

    public void test() throws Exception {
        // Create plotter
        IPlotter plotter = pf.create();

        // Create a test histogram which will be used to show various style
        // options.
        IHistogram1D h = histogram1D();

        // Create regions for showing plots
        plotter.createRegions(5, 2, 0);

        // Get the plotter style.
        IPlotterStyle pstyle = plotter.style();

        pstyle.titleStyle().textStyle().setFontSize(20);

        // Show data marker only.
        pstyle.dataStyle().setVisible(false);
        pstyle.dataStyle().errorBarStyle().setVisible(false);
        pstyle.dataStyle().markerStyle().setVisible(true);
        pstyle.dataStyle().markerStyle().setSize(6);
        pstyle.dataStyle().markerStyle().setColor("blue");

        // Display plots of all marker types.
        for (int i = 0; i < MarkerUtil.availableShapes.length; i++) {
            pstyle.dataStyle().markerStyle().setShape(MarkerUtil.availableShapes[i]);
            plotter.region(i).plot(h, pstyle);
            plotter.region(i).setTitle(MarkerUtil.availableShapes[i]);
        }

        // Show time.
        plotter.show();
    }

    public void tearDown() {
        System.out.println("Hit Ctrl + C to exit.");
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
