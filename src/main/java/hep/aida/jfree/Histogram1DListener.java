package hep.aida.jfree;

import hep.aida.IBaseHistogram;
import hep.aida.IHistogram1D;
import hep.aida.jfree.converter.Histogram1DConverter;
import hep.aida.ref.histogram.Histogram1D;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;

/**
 * This is a listener to update display of a 1D histogram in JFreeChart.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Histogram1DListener extends PlotListener {

    IHistogram1D h1d;

    Histogram1DListener(IBaseHistogram hist, JFreeChart chart, int[] datasetIndices) {
        super(hist, chart, datasetIndices);
        if (!(hist instanceof IHistogram1D)) {
            throw new IllegalArgumentException("hist is not an instance of IHistogram1D.");
        }
        h1d = (Histogram1D) hist;
    }

    synchronized void update() {
        // long startTime = System.nanoTime();
        XYPlot plot = (XYPlot) chart.getPlot();
        XYDataset[] datasets = Histogram1DConverter.createDatasets(h1d);
        for (int i = 0; i < datasetIndices.length; i++) {
            // System.out.println("updating ds @ " + datasetIndices[i]);
            plot.setDataset(datasetIndices[i], datasets[i]);
        }
        // long endTime = System.nanoTime() - startTime;
        // System.out.println("updated plot in " + endTime/1e6 + " ms");
    }
}
