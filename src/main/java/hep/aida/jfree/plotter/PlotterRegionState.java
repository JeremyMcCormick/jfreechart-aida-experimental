package hep.aida.jfree.plotter;

import hep.aida.jfree.plotter.listener.PlotListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * This class encapsulates the state of a PlotterRegion apart from its visual representation.
 * <p>
 * The state includes separate lists of:
 * <ul>
 * <li>PlotListeners that are used to update the chart dynamically</li>
 * <li>ObjectStyles which provide a link between plotted objects and their specific IPlotterStyles</li>
 * <li>PlotterRegionListeners that are used to update listeners on the plotted regions (e.g. on mouse click, etc.)</li>
 * <li>Objects that are are plotted by the region (returned as unmodifiable Collection)</li>
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
class PlotterRegionState {

    List<PlotListener<?>> plotListeners = new ArrayList<PlotListener<?>>();
    List<ObjectStyle> objectStyles = new ArrayList<ObjectStyle>();
    List<PlotterRegionListener> regionListeners = new ArrayList<PlotterRegionListener>();
    
    void addPlotListener(PlotListener<?> plotListener) {
        plotListeners.add(plotListener);
    }
    
    void addObjectStyle(ObjectStyle objectStyle) {
        objectStyles.add(objectStyle);
    }
    
    void addRegionListener(PlotterRegionListener regionListener) {
        regionListeners.add(regionListener);
    }
    
    List<PlotListener<?>> getPlotListeners() {
        return plotListeners;
    }
    
    List<ObjectStyle> getObjectStyles() {
        return objectStyles;
    }
    
    List<PlotterRegionListener> getRegionListeners() {
        return regionListeners;
    }
    
    List<Object> getObjects() {
        List<Object> objects = new ArrayList<Object>();
        for (ObjectStyle objectStyle : objectStyles) {
            Object object = objectStyle.object();
            objects.add(object);
        }        
        return Collections.unmodifiableList(objects);
    }
    
    void clear() {
        plotListeners.clear();
        objectStyles.clear();
        regionListeners.clear();
    }
}
