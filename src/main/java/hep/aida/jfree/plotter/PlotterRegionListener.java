package hep.aida.jfree.plotter;

/**
 * An interface for receiving a callback from a region.
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public interface PlotterRegionListener {    
    
    /**
     * Callback when region is selected (default is left click).
     * @param region The region that was selected.
     */
    public void regionSelected(PlotterRegion region);    
}
