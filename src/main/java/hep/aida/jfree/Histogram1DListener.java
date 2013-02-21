package hep.aida.jfree;

import hep.aida.IBaseHistogram;
import hep.aida.IHistogram1D;
import hep.aida.jfree.converter.Dataset;
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
public class Histogram1DListener extends PlotListener
{
    IHistogram1D h1d;

    Histogram1DListener(IBaseHistogram hist, JFreeChart chart, int[] datasetIndices)
    {
        super(hist, chart, datasetIndices);
        if (!(hist instanceof IHistogram1D)) {
            throw new IllegalArgumentException("hist is not an instance of IHistogram1D.");
        }
        h1d = (Histogram1D) hist;
    }

    synchronized void update()
    {
        XYPlot plot = (XYPlot) chart.getPlot();
        XYDataset[] datasets = Dataset.convertForStep(h1d);
        plot.setDataset(datasetIndices[0], datasets[0]);
        plot.setDataset(datasetIndices[1], datasets[1]);
    }
}
