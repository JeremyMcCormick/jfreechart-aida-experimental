package hep.aida.jfree.plotter.listener;

import hep.aida.ICloud1D;
import hep.aida.IHistogram1D;
import hep.aida.jfree.converter.Histogram1DConverter;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

public class Cloud1DListener extends PlotListener<ICloud1D> {

    Cloud1DListener(ICloud1D cloud, JFreeChart chart, XYDataset dataset) {
        super(cloud, chart, dataset);
    }

    public void update() {
        if (this.plot.isConverted()) {
            IHistogram1D histogram = this.plot.histogram();
            this.chart.setNotify(false);
            Histogram1DConverter.configureAxes(this.chart, histogram);
            this.chart.setNotify(true);
        }
        super.update();
    }
}
