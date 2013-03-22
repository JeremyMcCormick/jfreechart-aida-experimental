package hep.aida.jfree;

import hep.aida.IHistogram1D;
import hep.aida.IStatisticsBoxStyle;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;


/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class StatisticsBoxTest extends AbstractPlotTest {
    
    public void plot() {
        
        IHistogram1D hist = histogramFactory.createHistogram1D("histogram", 10, 0., 10.);
        for (int i=0; i<10; i++) {
            hist.fill(5);
        }
        
        style.yAxisStyle().setParameter("allowZeroSuppression", "false");
        
        IStatisticsBoxStyle statStyle = style.statisticsBoxStyle();
        
        statStyle.setVisible(true);
        statStyle.boxStyle().setX(7.);
        statStyle.boxStyle().setY(8.);
        
        statStyle.boxStyle().borderStyle().setBorderType("shadow");
        
        plotter.createRegion();
        plotter.region(0).plot(hist);
        
        //plotter.show();        
    }
}
