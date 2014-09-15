package hep.aida.jfree.dataset;

import hep.aida.IHistogram2D;

import org.jfree.data.xy.AbstractXYZDataset;
import org.jfree.data.xy.IntervalXYZDataset;

/**
 * This adapter implements JFreeChart's XYZDataset interface 
 * from a backing AIDA IHistogram2D object.  It is hard-coded 
 * to have only a single data series so the <code>series</code>
 * arguments are always ignored.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram2DAdapter extends AbstractXYZDataset implements IntervalXYZDataset, HasZBounds  {

    IHistogram2D histogram;
    Bounds bounds = new Bounds();
        
    public Histogram2DAdapter(IHistogram2D histogram) {
        this.histogram = histogram;
    }
    
    public IHistogram2D getHistogram() {
        return histogram;
    }
        
    @Override
    public int getItemCount(int series) {
        return histogram.xAxis().bins() * histogram.yAxis().bins();
    }

    @Override
    public Number getX(int series, int item) {
        int x = item % histogram.xAxis().bins();
        return histogram.xAxis().binCenter(x);
    }
    
    @Override
    public double getXValue(int series, int item) {
        return getX(series, item).doubleValue();
    }

    @Override
    public Number getY(int series, int item) {
        int y = item / histogram.xAxis().bins();
        return histogram.yAxis().binCenter(y);
    }

    @Override
    public Number getZ(int series, int item) {
        return histogram.binHeight(getXBin(item), getYBin(item));
    }

    @Override
    public int getSeriesCount() {
        // This dataset may only have one series.
        return 1;
    }

    @Override
    public Comparable getSeriesKey(int series) {
        return histogram.title();
    }    
           
    @Override
    public Bounds getZBounds(int series) {
        return bounds;
    }
    
    public Bounds recomputeZBounds() {
        if (histogram.entries() != 0)
            bounds.computeZBounds(this, 0);
        return bounds;
    }

    @Override
    public Number getStartXValue(int series, int item) {
        return histogram.xAxis().binLowerEdge(getXBin(item));
    }

    @Override
    public Number getEndXValue(int series, int item) {
        return histogram.xAxis().binUpperEdge(getXBin(item));
    }

    @Override
    public Number getStartYValue(int series, int item) {
        return histogram.yAxis().binLowerEdge(getYBin(item));
    }

    @Override
    public Number getEndYValue(int series, int item) {
        return histogram.yAxis().binUpperEdge(getYBin(item));
    }

    @Override
    public Number getStartZValue(int series, int item) {
        // The Z values always start at zero.
        return 0;
    }

    @Override
    public Number getEndZValue(int series, int item) {
        return histogram.binHeight(getXBin(item), getYBin(item));
    }
    
    private int getXBin(int item) {
        return item % histogram.xAxis().bins();
    }
    
    private int getYBin(int item) {
        return item / histogram.xAxis().bins();
    }
}