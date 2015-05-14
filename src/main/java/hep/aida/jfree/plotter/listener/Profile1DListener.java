package hep.aida.jfree.plotter.listener;

import hep.aida.IProfile1D;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;

/**
 * This is a listener to update display of an IProfile1D in JFreeChart.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Profile1DListener extends PlotListener<IProfile1D> {

    Profile1DListener(IProfile1D p1D, JFreeChart chart, XYDataset dataset) {
        super(p1D, chart, dataset);
    }
}
