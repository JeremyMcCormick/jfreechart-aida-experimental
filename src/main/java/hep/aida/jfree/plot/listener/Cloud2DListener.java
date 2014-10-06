package hep.aida.jfree.plot.listener;

import hep.aida.ICloud2D;

import org.jfree.chart.JFreeChart;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Cloud2DListener extends PlotListener<ICloud2D> {

    ICloud2D cloud;

    Cloud2DListener(ICloud2D cloud, JFreeChart chart) {
        super(cloud, chart);
        this.cloud = (ICloud2D) cloud;
    }
    
    public synchronized void update() {
        chart.setNotify(false);        
        chart.getXYPlot().configureDomainAxes();
        chart.getXYPlot().configureRangeAxes();
        chart.setNotify(true);
        chart.fireChartChanged();
    }
}