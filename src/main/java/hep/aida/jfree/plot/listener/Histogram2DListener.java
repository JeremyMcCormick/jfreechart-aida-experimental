package hep.aida.jfree.plot.listener;

import hep.aida.IHistogram2D;
import hep.aida.jfree.converter.Histogram2DConverter;
import hep.aida.jfree.dataset.Bounds;
import hep.aida.jfree.dataset.Histogram2DAdapter;
import hep.aida.jfree.renderer.AbstractPaintScale;
import hep.aida.jfree.renderer.XYBoxRenderer;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.Range;

/**
 * This is a listener to update display of a 2D histogram in JFreeChart.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Histogram2DListener extends PlotListener<IHistogram2D> {

    private IHistogram2D h2d;
    
    static private int updateInterval = 1000;

    Histogram2DListener(IHistogram2D hist, JFreeChart chart, int[] datasetIndices) {
        super(hist, chart, datasetIndices, updateInterval);
        if (!(hist instanceof IHistogram2D)) {
            throw new IllegalArgumentException("hist is not an instance of IHistogram2D.");
        }
        h2d = (IHistogram2D) hist;
    }

    public synchronized void update() {
        
        chart.setNotify(false);
        
        XYPlot plot = (XYPlot)chart.getPlot();        
        Histogram2DAdapter adapter = (Histogram2DAdapter)plot.getDataset();                 
        Bounds zBounds = adapter.recomputeZBounds();
        
        if (plot.getRenderer() instanceof XYBlockRenderer) {
            updateColorMap(plot, zBounds);
        } else if (plot.getRenderer() instanceof XYBoxRenderer) {
            updateBoxPlot(plot, zBounds);
        }
        
        chart.setNotify(true);
        chart.fireChartChanged();        
    }

    private void updateBoxPlot(XYPlot plot, Bounds zBounds) {
        // Update box plot bounds.
        ((XYBoxRenderer)plot.getRenderer()).setMaximumValue(zBounds.getMaximum());
    }

    private void updateColorMap(XYPlot plot, Bounds zBounds) {
                        
        // Set the new Z bounds on the PaintScale.        
        PaintScale scale = ((XYBlockRenderer) plot.getRenderer()).getPaintScale();
        if (scale instanceof AbstractPaintScale) {
            ((AbstractPaintScale) scale).setBounds(zBounds.getMinimum(), zBounds.getMaximum());
        }
        
        // Rebuild the plot's legend.
        PaintScaleLegend legend = (PaintScaleLegend) chart.getSubtitle(Histogram2DConverter.COLOR_SCALE_LEGEND);
        legend.getAxis().setRange(new Range(scale.getLowerBound(), scale.getUpperBound()));
    }
}