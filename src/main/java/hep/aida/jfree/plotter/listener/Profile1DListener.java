package hep.aida.jfree.plotter.listener;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

import hep.aida.IProfile1D;

/**
 * This is a listener to update display of an IProfile1D in JFreeChart.
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Profile1DListener extends PlotListener<IProfile1D> {
    
    Profile1DListener(IProfile1D p1D, JFreeChart chart, XYDataset dataset) {
        super(p1D, chart, dataset);
    }
    
    @Override
    public void update() {       
        chart.getXYPlot().getRangeAxis().setAutoRangeMinimumSize(plot.maxBinHeight() + findMaxError(plot));
        chart.getXYPlot().getRangeAxis().configure();
        super.update();
    }
    
    private double findMaxError(IProfile1D p1D) {
        double maxBinHeight = p1D.maxBinHeight();
        double maxError = 0;
        for (int i = 0; i < p1D.axis().bins(); i++) {
            if (p1D.binHeight(i) == maxBinHeight) {
                if (p1D.binError(i) > maxError)
                    maxError = p1D.binError(i); 
            }
        }
        return maxError;
    }
}
