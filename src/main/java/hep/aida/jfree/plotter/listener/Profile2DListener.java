package hep.aida.jfree.plotter.listener;

import hep.aida.IProfile2D;
import hep.aida.jfree.converter.Profile2DAdapter;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;


public class Profile2DListener extends PlotListener<IProfile2D> {

    Histogram2DListener listener;
    
    Profile2DListener(IProfile2D plot, JFreeChart chart, XYDataset dataset) {
        super(plot, chart, dataset);
        
        // This should be okay, because the backing Histogram2D is not actually used directly when updating.
        listener = new Histogram2DListener(new Profile2DAdapter(plot), chart, dataset);
    }
    
    public synchronized void update() {
        listener.update();
    }

}
