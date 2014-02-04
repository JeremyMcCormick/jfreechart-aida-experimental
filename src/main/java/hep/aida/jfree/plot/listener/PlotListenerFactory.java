package hep.aida.jfree.plot.listener;

//import hep.aida.IBaseHistogram;
import hep.aida.ICloud1D;
import hep.aida.ICloud2D;
import hep.aida.IHistogram1D;
import hep.aida.IHistogram2D;

import org.jfree.chart.JFreeChart;

/**
 * This factory creates {@link PlotListener} objects depending on the type of AIDA object.
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class PlotListenerFactory {

    public static PlotListener createListener(Object plot, JFreeChart chart, int[] datasetIndices) {
        if (plot instanceof IHistogram1D) {
            return new Histogram1DListener((IHistogram1D)plot, chart, datasetIndices);
        } else if (plot instanceof ICloud1D) {
            return new Cloud1DListener((ICloud1D)plot, chart, datasetIndices);
        } else if (plot instanceof ICloud2D) {
            return new Cloud2DListener((ICloud2D)plot, chart, datasetIndices);
        } else if (plot instanceof IHistogram2D) {
            return new Histogram2DListener((IHistogram2D)plot, chart, datasetIndices);
        } else {
            return null;
        }
    }
}
