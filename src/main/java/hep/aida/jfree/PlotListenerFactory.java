package hep.aida.jfree;

import hep.aida.IBaseHistogram;
import hep.aida.ICloud2D;
import hep.aida.IHistogram1D;
import hep.aida.IHistogram2D;

import org.jfree.chart.JFreeChart;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class PlotListenerFactory {

    public static PlotListener createListener(IBaseHistogram hist, JFreeChart chart, int[] datasetIndices) {
        if (hist instanceof IHistogram1D) {
            return new Histogram1DListener(hist, chart, datasetIndices);
        } else if (hist instanceof ICloud2D) {
            return new Cloud2DListener(hist, chart, datasetIndices);
        } else if (hist instanceof IHistogram2D) {
            return new Histogram2DListener(hist, chart, datasetIndices);
        } else {
            return null;
        }
    }
}
