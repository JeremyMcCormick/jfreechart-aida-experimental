package hep.aida.jfree.plot.listener;

import hep.aida.IHistogram2D;
import hep.aida.jfree.converter.Histogram2DConverter;
import hep.aida.jfree.dataset.Bounds;
import hep.aida.jfree.dataset.DatasetConverter;
import hep.aida.jfree.dataset.Histogram2DAdapter;
import hep.aida.jfree.renderer.AbstractPaintScale;
import hep.aida.jfree.renderer.XYBoxRenderer;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.Range;
import org.jfree.data.xy.XYZDataset;

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
        XYPlot plot = (XYPlot)chart.getPlot();
        if (plot.getRenderer() instanceof XYBoxRenderer) {
            // Box plot
            // FIXME: Replace with adapter to avoid data copying.
            plot.setDataset(0, DatasetConverter.toXYZRangedDataset(h2d));
        } else {
            // Color map
            updateColorMap(plot);
        }
    }

    private void updateColorMap(XYPlot plot) {
        
        // Turn off notification while plot is being changed.
        chart.setNotify(false);
        
        Histogram2DAdapter adapter = (Histogram2DAdapter)plot.getDataset();         
        
        // Recalculate the Z bounds.        
        Bounds zBounds = adapter.recomputeZBounds();
        
        // Set the new Z bounds on the PaintScale.        
        PaintScale scale = ((XYBlockRenderer) plot.getRenderer()).getPaintScale();
        if (scale instanceof AbstractPaintScale) {
            ((AbstractPaintScale) scale).setBounds(zBounds.getMinimum(), zBounds.getMaximum());
        }
        
        // Rebuild the plot's legend.
        PaintScaleLegend legend = (PaintScaleLegend) chart.getSubtitle(Histogram2DConverter.COLOR_SCALE_LEGEND);
        legend.getAxis().setRange(new Range(scale.getLowerBound(), scale.getUpperBound()));

        // Fire notification for redraw.
        chart.setNotify(true);
        chart.fireChartChanged();
    }
}