package hep.aida.jfree;

import hep.aida.IAnalysisFactory;
import hep.aida.IHistogram1D;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;
import hep.aida.IPlotterFactory;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.converter.Style;

import java.util.Random;

import junit.framework.TestCase;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Histogram1DStyleTest extends TestCase
{
    IAnalysisFactory af;
    IPlotterFactory pf;
    IHistogramFactory hf;
    
    int nfills = 1;
    int range = 5;

    protected void setUp()
    {
        AnalysisFactory.register();
        af = IAnalysisFactory.create();
        pf = af.createPlotterFactory();
        hf = af.createHistogramFactory(null);
    }

    // Create a 1D histogram with random Gaussian distribution
    private final IHistogram1D histogram1D()
    {
        IHistogram1D h1d = hf.createHistogram1D("h1d", 5, 0, 5.0);
        Random rand = new Random();
        for (int i = 0; i < nfills; i++) {
            h1d.fill(rand.nextInt(range));
        }
        return h1d;
    }

    public void test() throws Exception
    {

        // Create plotter
        IPlotter plotter = pf.create();

        // Create a test histogram which will be used to show
        // various style options.
        IHistogram1D h = histogram1D();

        // Set labels for axes automatically based on title
        h.annotation().addItem("xAxisLabel", h.title() + " X");
        h.annotation().addItem("yAxisLabel", h.title() + " Y");

        // Create 3x3 regions for showing plots
        plotter.createRegions(3, 3, 0);

        // Get the plotter style.
        IPlotterStyle pstyle = plotter.style();

        // Caps on error bars.
        pstyle.dataStyle().errorBarStyle().setParameter("errorBarDecoration", (new Float(1.0f)).toString());

        // Default style.
        plotter.region(0).plot(h, pstyle);

        // Default plus visible outline.
        pstyle.dataStyle().outlineStyle().setVisible(true);
        plotter.region(1).plot(h, pstyle);

        // No fill with bars drawn.
        pstyle.dataStyle().fillStyle().setVisible(false);
        pstyle.dataStyle().outlineStyle().setVisible(true);
        pstyle.dataStyle().outlineStyle().setColor("blue");
        plotter.region(2).plot(h, pstyle);

        // No fill with outline only.
        pstyle.dataStyle().lineStyle().setVisible(false);
        plotter.region(3).plot(h, pstyle);

        // Show errors only.
        pstyle.dataStyle().setVisible(false);
        plotter.region(4).plot(h, pstyle);

        // Show data only in outline style.
        pstyle.dataStyle().setVisible(true);
        pstyle.dataStyle().errorBarStyle().setVisible(false);
        plotter.region(5).plot(h, pstyle);

        // Show nothing at all.
        pstyle.dataStyle().setVisible(false);
        pstyle.dataStyle().errorBarStyle().setVisible(false);
        plotter.region(6).plot(h, pstyle);

        // Show data marker.
        pstyle.dataStyle().setVisible(true);
        pstyle.dataStyle().markerStyle().setVisible(true);
        pstyle.dataStyle().markerStyle().setShape("dot");
        pstyle.dataStyle().markerStyle().setSize(2);
        plotter.region(7).plot(h, pstyle);       

        // Show time.
        plotter.show();
    }

    public void tearDown()
    {
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
