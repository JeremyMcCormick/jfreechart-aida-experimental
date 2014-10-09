package hep.aida.jfree.test;

import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.plotter.style.util.MarkerUtil;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class MarkerTest extends AbstractPlotTest {

    public void test() {

        // Create a test histogram which will be used to show various style
        // options.
        IHistogram1D h1d = histogramFactory.createHistogram1D("h1d", 10, 0, 10.0);
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            h1d.fill(rand.nextInt(10));
        }

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
            plotter.region(i).plot(h1d, pstyle);
            plotter.region(i).setTitle(MarkerUtil.availableShapes[i]);
        }
        
        mode();
    }
}
