package hep.aida.jfree.plot.listener;

import hep.aida.ICloud1D;

import org.jfree.chart.JFreeChart;

public class Cloud1DListener extends PlotListener<ICloud1D> {
    
    ICloud1D c1d;
        
    Cloud1DListener(ICloud1D hist, JFreeChart chart, int[] datasetIndices) {
        super(hist, chart, datasetIndices);
        if (!(hist instanceof ICloud1D)) {
            throw new IllegalArgumentException("hist is not an instance of ICloud1D.");
        }
        c1d = (ICloud1D) hist;
    }
}
