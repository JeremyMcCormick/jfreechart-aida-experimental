package hep.aida.jfree.plot.listener;

//import hep.aida.IBaseHistogram;
import hep.aida.IHistogram1D;
import hep.aida.ref.histogram.Histogram1D;

import org.jfree.chart.JFreeChart;

/**
 * This is a listener to update display of a 1D histogram in JFreeChart.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Histogram1DListener extends PlotListener<IHistogram1D> {

    IHistogram1D histogram;

    Histogram1DListener(IHistogram1D histogram, JFreeChart chart, int[] datasetIndices) {
        super(histogram, chart, datasetIndices);
        if (!(histogram instanceof IHistogram1D)) {
            throw new IllegalArgumentException("histogram is not an instance of IHistogram1D.");
        }
        this.histogram = (Histogram1D) histogram;
    }
    
    public synchronized void update() {
        chart.getXYPlot().getRangeAxis().configure();
        super.update();
    }
}
