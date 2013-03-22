package hep.aida.jfree;

import hep.aida.IHistogram1D;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;


/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class StatisticsBoxTest extends AbstractPlotTest {
    
    public void plot() {
        
        IHistogram1D hist = histogramFactory.createHistogram1D("histogram", 10, 0., 10.);
        Random rand = new Random();
        for (int i=0; i<1000; i++) {
            hist.fill(rand.nextInt(10));
        }
        
        plotter.createRegion();
        plotter.region(0).plot(hist);
        
        style.statisticsBoxStyle().setVisible(true);
        
        //plotter.show();        
    }
}
