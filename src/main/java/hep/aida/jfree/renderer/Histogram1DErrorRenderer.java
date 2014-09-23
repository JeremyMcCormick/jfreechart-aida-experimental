package hep.aida.jfree.renderer;

import hep.aida.jfree.dataset.Histogram1DAdapter;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;


public class Histogram1DErrorRenderer extends XYErrorRenderer {

    Number zero = new Double(0);
    
    /**
     * Override the default implement to suppress drawing of zero value items 
     * in 1D histograms, which have no error values.
     */
    public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, 
            PlotRenderingInfo info, XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, 
            XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
        if (dataset instanceof Histogram1DAdapter) {
            if (series == Histogram1DAdapter.ERRORS) {
                if (!dataset.getY(series, item).equals(zero)) {                    
                    super.drawItem(g2, state, dataArea, info, plot, 
                            domainAxis, rangeAxis, dataset, series, 
                            item, crosshairState, pass);
                }
            }
        }
    }
}
