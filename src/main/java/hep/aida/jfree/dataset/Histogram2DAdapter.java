package hep.aida.jfree.dataset;

import hep.aida.IHistogram2D;

import org.jfree.data.xy.AbstractXYZDataset;
import org.jfree.data.xy.XYZDataset;

/**
 * This adapter implements JFreeChart's XYZDataset interface 
 * from a backing AIDA IHistogram2D object.  It is hard-coded 
 * to have only a single data series so the <code>series</code>
 * arguments are always ignored.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram2DAdapter extends AbstractXYZDataset implements XYZDataset, HasZBounds  {

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
        int x = item / histogram.xAxis().bins();
        return histogram.xAxis().binCenter(x);
    }
    
    public double getXValue(int series, int item) {
        return getX(series, item).doubleValue();
    }

    @Override
    public Number getY(int series, int item) {
        int y = item % histogram.xAxis().bins();
        return histogram.yAxis().binCenter(y);
    }

    @Override
    public Number getZ(int series, int item) {
        int x = getX(series, item).intValue();
        int y = getY(series, item).intValue();
        int xBin = histogram.coordToIndexX(x);
        int yBin = histogram.coordToIndexY(y);
        return histogram.binHeight(xBin, yBin);
    }

    @Override
    public int getSeriesCount() {
        return 1;
    }

    @Override
    public Comparable getSeriesKey(int series) {
        return histogram.title();
    }    
           
    public Bounds getZBounds(int series) {
        return bounds;
    }
    
    public Bounds recomputeZBounds() {
        if (histogram.entries() != 0)
            bounds.computeZBounds(this, 0);
        return bounds;
    }
}