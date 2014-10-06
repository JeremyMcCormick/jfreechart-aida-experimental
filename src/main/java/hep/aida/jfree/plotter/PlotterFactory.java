package hep.aida.jfree.plotter;

import hep.aida.IPlotter;
import hep.aida.IPlotterStyle;
import hep.aida.ref.plotter.PlotterStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of <code>IPlotterFactory</code> for the JFreeChart backend.
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class PlotterFactory extends hep.aida.ref.plotter.PlotterFactory {

    String name;
    boolean isEmbedded = false;
    List<PlotterRegionListener> regionListeners = new ArrayList<PlotterRegionListener>();

    public PlotterFactory() {
        super();
    }

    public PlotterFactory(String name) {
        super();
        this.name = name;
    }
    
    public void addPlotterRegionListener(PlotterRegionListener listener) {
        regionListeners.add(listener);
    }

    public IPlotter create(String plotterName) {
        
        Plotter plotter;
        
        // Create a plotter based on whether embedded behavior is enabled.
        if (isEmbedded)
            // This is the default embeddable plotter.
            plotter = new Plotter();        
        else
            // This is a standalone plotter that will use its own JFrame.
            plotter = new StandalonePlotter();
        
        // Set the name of the plotter, which is the same as its title.
        plotter.setTitle(plotterName);
        
        // Add region listeners. 
        for (PlotterRegionListener listener : regionListeners) {
            plotter.addPlotterRegionListener(listener);
        }
        
        return plotter;
    }

    public IPlotter create() {
        return create((String) null);
    }
    
    protected void setIsEmbedded(boolean embedded) {
        this.isEmbedded = embedded;
    }
    
    public IPlotterStyle createPlotterStyle(IPlotterStyle style) {
        return new DefaultPlotterStyle((PlotterStyle)style);
    }
    
    public IPlotterStyle createDefaultHistogram1DStyle() {
        return new DefaultHistogram1DStyle();
    }
}