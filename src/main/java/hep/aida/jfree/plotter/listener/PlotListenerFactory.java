package hep.aida.jfree.plotter.listener;

import hep.aida.ICloud1D;
import hep.aida.ICloud2D;
import hep.aida.IDataPointSet;
import hep.aida.IHistogram1D;
import hep.aida.IHistogram2D;
import hep.aida.IProfile1D;
import hep.aida.IProfile2D;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

/**
 * This factory creates {@link PlotListener} objects depending on the type of AIDA object.
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class PlotListenerFactory {

    private PlotListenerFactory() {        
    }
    
    public static PlotListener<?> createListener(Object plot, JFreeChart chart, XYDataset dataset) {
        if (plot instanceof IHistogram1D) {            
            return new Histogram1DListener((IHistogram1D)plot, chart, dataset);
        } else if (plot instanceof ICloud1D) {
            return new Cloud1DListener((ICloud1D)plot, chart, dataset);
        } else if (plot instanceof ICloud2D) {
            return new Cloud2DListener((ICloud2D)plot, chart, dataset);
        } else if (plot instanceof IHistogram2D) {
            return new Histogram2DListener((IHistogram2D)plot, chart, dataset);
        } else if (plot instanceof IDataPointSet) {
            return new DataPointSetListener((IDataPointSet)plot, chart, dataset);
        } else if (plot instanceof IProfile1D) {
            return new Profile1DListener((IProfile1D)plot, chart, dataset);
        } else if (plot instanceof IProfile2D) {
            return new Profile2DListener((IProfile2D)plot, chart, dataset);
        } else {
            return null;
        }        
    }
}
