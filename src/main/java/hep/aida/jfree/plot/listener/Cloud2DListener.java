package hep.aida.jfree.plot.listener;

import hep.aida.IBaseHistogram;
import hep.aida.ICloud2D;
import hep.aida.jfree.dataset.DatasetConverter;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Cloud2DListener extends PlotListener {

    ICloud2D cloud;

    Cloud2DListener(IBaseHistogram cloud, JFreeChart chart, int[] datasetIndices) {
        super(cloud, chart, datasetIndices);
        if (!(hist instanceof ICloud2D)) {
            throw new IllegalArgumentException("hist is not an instance of ICloud2D");
        }
        this.cloud = (ICloud2D) cloud;
    }

    public synchronized void update() {
        chart.setNotify(false);
        XYPlot plot = (XYPlot) chart.getPlot();
        XYSeriesCollection dataset = DatasetConverter.convert(cloud);
        plot.setDataset(datasetIndices[0], dataset);
        chart.setNotify(true);
        chart.fireChartChanged();
     }
}