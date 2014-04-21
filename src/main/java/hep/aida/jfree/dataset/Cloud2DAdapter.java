package hep.aida.jfree.dataset;

import hep.aida.ICloud2D;

import org.jfree.data.xy.AbstractXYDataset;

public class Cloud2DAdapter extends AbstractXYDataset {
    
    ICloud2D cloud;
    
    public Cloud2DAdapter(ICloud2D cloud) {
        this.cloud = cloud;
    }
        
    public int getItemCount(int series) {
        return cloud.entries();
    }

    public int getSeriesCount() {
        return 1;
    }
    
    public String getSeriesKey(int series) {
        return cloud.title();
    }
    
    public Number getX(int series, int item) {
        return cloud.valueX(item);
    }
    
    public Number getY(int series, int item) {
        return cloud.valueY(item);
    }
    
    public double getXValue(int series, int item) {
        return getX(series, item).doubleValue();
    }
    
    public double getYValue(int series, int item) {
        return getY(series, item).doubleValue();
    }    
}
