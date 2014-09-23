package hep.aida.jfree.plotter;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        //System.out.println("ChartPanelMouseListener.mouseClicked");
        for (PlotterRegionListener listener : listeners) {
            //System.out.println("activating listener " + listener.getClass().getCanonicalName() + " on region " + plotterRegion.title());
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