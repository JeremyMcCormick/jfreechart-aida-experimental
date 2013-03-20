package hep.aida.jfree.plot.listener;

import hep.aida.IBaseHistogram;
import hep.aida.IHistogram2D;
import hep.aida.jfree.converter.Histogram2DConverter;
import hep.aida.jfree.dataset.DatasetConverter;
import hep.aida.jfree.renderer.RainbowPaintScale;
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
public class Histogram2DListener extends PlotListener {

    private IHistogram2D h2d;
    static private int updateInterval = 1000;

    Histogram2DListener(IBaseHistogram hist, JFreeChart chart, int[] datasetIndices) {
        super(hist, chart, datasetIndices, updateInterval);
        if (!(hist instanceof IHistogram2D)) {
            throw new IllegalArgumentException("hist is not an instance of IHistogram2D.");
        }
        h2d = (IHistogram2D) hist;
    }

    public synchronized void update() {
        XYPlot plot = (XYPlot) chart.getPlot();
        if (plot.getRenderer() instanceof XYBoxRenderer) {
            plot.setDataset(0, DatasetConverter.toXYZRangedDataset(h2d));
        } else {
            rebuildColorMap(plot);
        }
    }

    private void rebuildColorMap(XYPlot plot) {

        chart.setNotify(false);

        XYZDataset dataset = DatasetConverter.convert(h2d);
        double[] zlimits = Histogram2DConverter.calculateZLimits(dataset);
        PaintScale scale = ((XYBlockRenderer) plot.getRenderer()).getPaintScale();
        if (scale instanceof RainbowPaintScale) {
            ((RainbowPaintScale) scale).setZParameters(zlimits);
        }
        // TODO: Handle other types of paint scales here.

        plot.setDataset(0, dataset);
        PaintScaleLegend legend = (PaintScaleLegend) chart.getSubtitle(Histogram2DConverter.COLOR_SCALE_LEGEND);
        legend.getAxis().setRange(new Range(scale.getLowerBound(), scale.getUpperBound()));

        chart.setNotify(true);
        chart.fireChartChanged();
    }
}