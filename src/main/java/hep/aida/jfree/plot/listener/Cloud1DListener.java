package hep.aida.jfree.plot.listener;

import hep.aida.ICloud1D;
import hep.aida.jfree.converter.Histogram1DConverter;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;

public class Cloud1DListener extends PlotListener<ICloud1D> {
    
    ICloud1D c1d;
        
    Cloud1DListener(ICloud1D hist, JFreeChart chart, int[] datasetIndices) {
        super(hist, chart, datasetIndices);
        if (!(hist instanceof ICloud1D)) {
            throw new IllegalArgumentException("hist is not an instance of ICloud1D.");
        }
        c1d = (ICloud1D) hist;
    }

    public synchronized void update() {
        XYPlot plot = (XYPlot) chart.getPlot();
        XYDataset[] datasets = Histogram1DConverter.createDatasets(c1d.histogram());
        for (int i = 0; i < datasetIndices.length; i++) {
            plot.setDataset(datasetIndices[i], datasets[i]);
        }
    }
}
