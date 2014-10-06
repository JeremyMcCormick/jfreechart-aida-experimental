package hep.aida.jfree.dataset;

import hep.aida.ICloud1D;

public class Cloud1DAdapter extends Histogram1DAdapter {
    
    ICloud1D cloud;
    
    public Cloud1DAdapter(ICloud1D cloud) {        
        super(null);
        this.cloud = cloud;      
        checkConverted();
    }
        
    public int getItemCount(int series) {
        if (checkConverted()) {
            return super.getItemCount(series);
        } else {
            return 0;
        }
    }
    
    boolean checkConverted() {
        if (cloud.isConverted()) {
            if (this.histogram == null)
                this.histogram = cloud.histogram();
            return true;
        } else {
            return false;
        }        
    }    
}
