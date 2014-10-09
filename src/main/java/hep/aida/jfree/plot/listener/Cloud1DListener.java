package hep.aida.jfree.plot.listener;

import hep.aida.ICloud1D;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

public class Cloud1DListener extends PlotListener<ICloud1D> {

    Cloud1DListener(ICloud1D cloud, JFreeChart chart, XYDataset dataset) {
        super(cloud, chart, dataset);
    }

    public void update() {
        chart.getXYPlot().getDomainAxis().configure();
        chart.getXYPlot().getRangeAxis().configure();
        super.update();
    }
}
