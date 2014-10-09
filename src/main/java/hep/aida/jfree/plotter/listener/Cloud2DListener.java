package hep.aida.jfree.plotter.listener;

import hep.aida.ICloud2D;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Cloud2DListener extends PlotListener<ICloud2D> {

    Cloud2DListener(ICloud2D cloud, JFreeChart chart, XYDataset dataset) {
        super(cloud, chart, dataset);
    }
    
    public synchronized void update() {
        chart.setNotify(false);        
        chart.getXYPlot().configureDomainAxes();
        chart.getXYPlot().configureRangeAxes();
        chart.setNotify(true);
        chart.fireChartChanged();
    }
}