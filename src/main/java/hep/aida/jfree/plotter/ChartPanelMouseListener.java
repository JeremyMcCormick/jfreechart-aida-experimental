package hep.aida.jfree.plotter;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A <code>MouseListener</code> that will activate the {@link PlotterRegionListener}s
 * for a JFreeChart <code>ChartPanel</code>.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class ChartPanelMouseListener implements MouseListener {

    PlotterRegion plotterRegion;
    List<PlotterRegionListener> listeners = new ArrayList<PlotterRegionListener>();
    
    static final String REGION_SELECTED = "regionSelected";
    
    public ChartPanelMouseListener(PlotterRegion plotterRegion) {
        this.plotterRegion = plotterRegion;
    }
    
    public void addListener(PlotterRegionListener listener) {
        listeners.add(listener);
    }
    
    public void addListeners(Collection<PlotterRegionListener> listeners) {
        this.listeners.addAll(listeners);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        for (PlotterRegionListener listener : listeners) {
            listener.regionSelected(plotterRegion);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {        
    }

    @Override
    public void mouseEntered(MouseEvent e) {        
    }

    @Override
    public void mouseExited(MouseEvent e) {        
    }    
}