package hep.aida.jfree.dataset;

import hep.aida.IHistogram2D;

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.xy.AbstractXYZDataset;
import org.jfree.data.xy.XYZDataset;

/**
 * This adapter implements JFreeChart's XYZDataset interface 
 * from a list of backing AIDA IHistogram2D objects.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram2DAdapter extends AbstractXYZDataset implements XYZDataset  {

    IHistogram2D histogram;
        
    Histogram2DAdapter(IHistogram2D histogram) {
        this.histogram = histogram;
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

    @Override
    public Number getY(int series, int item) {
        int y = item % histogram.xAxis().bins();
        return histogram.yAxis().binCenter(y);
    }

    @Override
    public Number getZ(int series, int item) {
        int x = getX(series, item).intValue();
        int y = getY(series, item).intValue();
        return histogram.binHeight(x, y);
    }

    @Override
    public int getSeriesCount() {
        return 1;
    }

    @Override
    public Comparable getSeriesKey(int series) {
        return histogram.title();
    }    
}
