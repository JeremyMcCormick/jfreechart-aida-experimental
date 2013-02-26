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
public class MarkerTest extends TestCase
{
    IAnalysisFactory af;
    IPlotterFactory pf;
    IHistogramFactory hf;
    
    int nfills = 100;
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

        // Create a test histogram which will be used to show various style options.
        IHistogram1D h = histogram1D();

        // Create 3x3 regions for showing plots
        plotter.createRegions(5, 2, 0);

        // Get the plotter style.
        IPlotterStyle pstyle = plotter.style();

        // Show data marker only.
        pstyle.dataStyle().errorBarStyle().setVisible(false);
        pstyle.dataStyle().lineStyle().setVisible(false);
        pstyle.dataStyle().outlineStyle().setVisible(false);
        //pstyle.dataStyle().setVisible(true);
        pstyle.dataStyle().markerStyle().setVisible(true);
        pstyle.dataStyle().markerStyle().setSize(5);
        
        for (int i=0; i<Style.availableShapes.length; i++)
        {
            //System.out.println("making plot for marker " + Style.availableShapes[i] + " in region " + i);
            pstyle.dataStyle().markerStyle().setShape(Style.availableShapes[i]);
            plotter.region(i).plot(h, pstyle);
            plotter.region(i).setTitle(Style.availableShapes[i]);
        }        
        
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
