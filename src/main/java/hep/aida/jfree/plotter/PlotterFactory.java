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

    /**
     * Create an unnamed plotter factory.
     */
    public PlotterFactory() {
        super();
    }

    /**
     * Create a named plotter factory.
     * @param name The name of the plotter factory.
     */
    public PlotterFactory(String name) {
        super();
        this.name = name;
    }
    
    /**
     * Add a listener which will be registered with the regions of all plotters.
     * @param listener The region listener to register.
     */
    public void addPlotterRegionListener(PlotterRegionListener listener) {
        regionListeners.add(listener);
    }

    /**
     * Create a named plotter with the given title.
     * @param plotterName The name or title of the plotter.
     */
    public IPlotter create(String plotterName) {
        
        Plotter plotter;
        
        // Is embedded behavior enabled?
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

    /**
     * Create an unnamed plotter.
     */
    public IPlotter create() {
        return create((String) null);
    }
    
    /**
     * Enable embedded behavior on the created plotters, which means that a JFrame
     * will not be created and used automatically.
     * @param embedded True to enable embedded behavior.
     */
    protected void setIsEmbedded(boolean embedded) {
        this.isEmbedded = embedded;
    }
    
    /**
     * Create a plotter style with the given parent.
     * @param style The parent style.
     */
    public IPlotterStyle createPlotterStyle(IPlotterStyle style) {
        return new DefaultPlotterStyle((PlotterStyle)style);
    }
    
    /**
     * Create a default style for an IHistogram1D.
     * @return The default IHistogram1D style.
     */
    public IPlotterStyle createDefaultHistogram1DStyle() {
        return new DefaultHistogram1DStyle();
    }
}