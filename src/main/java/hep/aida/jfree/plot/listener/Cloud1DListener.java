package hep.aida.jfree.plot.listener;

import hep.aida.ICloud1D;

import org.jfree.chart.JFreeChart;

public class Cloud1DListener extends PlotListener<ICloud1D> {

    ICloud1D cloud;
    boolean configuredRange = false;

    Cloud1DListener(ICloud1D cloud, JFreeChart chart) {
        super(cloud, chart);
        this.cloud = (ICloud1D) cloud;
    }

    public void update() {
        chart.getXYPlot().getDomainAxis().configure();
        chart.getXYPlot().getRangeAxis().configure();
        super.update();
    }
}
