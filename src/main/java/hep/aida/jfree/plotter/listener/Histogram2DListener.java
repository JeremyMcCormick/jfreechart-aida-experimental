package hep.aida.jfree.plotter.listener;

import hep.aida.IHistogram2D;
import hep.aida.jfree.converter.Histogram2DConverter;
import hep.aida.jfree.dataset.Bounds;
import hep.aida.jfree.dataset.Histogram2DAdapter;
import hep.aida.jfree.renderer.AbstractPaintScale;
import hep.aida.jfree.renderer.XYVariableBinWidthBlockRenderer;
import hep.aida.jfree.renderer.XYVariableBinWidthBoxRenderer;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;

/**
 * This is a listener to update display of a 2D histogram in JFreeChart.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Histogram2DListener extends PlotListener<IHistogram2D> {
    
    // Update interval is every 1 second.
    static private int updateInterval = 1000;

    Histogram2DListener(IHistogram2D histogram, JFreeChart chart, XYDataset dataset) {
        super(histogram, chart, dataset, updateInterval); 
    }

    public synchronized void update() {
        chart.setNotify(false);        
        XYPlot plot = chart.getXYPlot();        
        Histogram2DAdapter adapter = (Histogram2DAdapter)dataset;
        Bounds zBounds = adapter.recomputeZBounds();
        if (zBounds.isValid()) {        
            if (plot.getRendererForDataset(dataset) instanceof XYVariableBinWidthBlockRenderer) {
                updateColorMap(zBounds);
            } else if (plot.getRendererForDataset(dataset) instanceof XYVariableBinWidthBoxRenderer) {
                updateBoxPlot(zBounds);
            }
        }    
        chart.setNotify(true);
        chart.fireChartChanged();
    }

    private void updateBoxPlot(Bounds zBounds) {
        // Update box plot bounds for the given renderer.
        ((XYVariableBinWidthBoxRenderer)chart.getXYPlot().getRendererForDataset(dataset)).setMaximumValue(zBounds.getMaximum());
    }

    private void updateColorMap(Bounds zBounds) {       
        // Set the new Z bounds on the PaintScale.        
        PaintScale scale = ((XYVariableBinWidthBlockRenderer)chart.getXYPlot().getRendererForDataset(dataset)).getPaintScale();
        if (scale instanceof AbstractPaintScale) {
            ((AbstractPaintScale) scale).setBounds(0., zBounds.getMaximum());
        }
        
        // Rebuild the plot's legend.
        PaintScaleLegend legend = (PaintScaleLegend)chart.getSubtitle(Histogram2DConverter.COLOR_SCALE_LEGEND);
        legend.getAxis().setRange(new Range(scale.getLowerBound(), scale.getUpperBound()));
    }
}