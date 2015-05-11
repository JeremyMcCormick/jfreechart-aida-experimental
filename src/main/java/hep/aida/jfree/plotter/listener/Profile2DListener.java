package hep.aida.jfree.plotter.listener;

import hep.aida.IHistogram2D;
import hep.aida.IProfile2D;
import hep.aida.jfree.converter.Profile2DAdapter;
import hep.aida.ref.event.IsObservable;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;


public class Profile2DListener extends PlotListener<IProfile2D> {

    Histogram2DListener listener;
    
    Profile2DListener(IProfile2D plot, JFreeChart chart, XYDataset dataset) {
        super(plot, chart, dataset);
        
        // Wrap the Profile2D with a Histogram2D adapter.
        // This should be okay, because the backing Histogram2D is not actually used directly when updating.
        IHistogram2D adapter = new Profile2DAdapter(plot);
        listener = new Histogram2DListener(adapter, chart, dataset);
        ((IsObservable)adapter).removeListener(listener);
    }    
}
