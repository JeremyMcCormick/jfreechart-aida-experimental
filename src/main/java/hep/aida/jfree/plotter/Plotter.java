package hep.aida.jfree.plotter;

import hep.aida.IPlotterRegion;
import hep.aida.ref.plotter.DummyPlotter;
import hep.aida.ref.plotter.PlotterStyle;
import jas.util.layout.PercentLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * <p>
 * This class implements a JFreeChart <code>IPlotter</code> by extending AIDA's <code>DummyPlotter</code> class.
 * <p>
 * By default, it has behavior which is suitable for embedding into external Swing components such as JPanels. 
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Plotter extends DummyPlotter {

    List<PlotterRegion> regions = new ArrayList<PlotterRegion>();
    JPanel rootPanel;
    boolean isSetup = false;
    
    List<PlotterRegionListener> plotterRegionListeners = new ArrayList<PlotterRegionListener>();

    /**
     * Class constructor.
     */
    Plotter() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new PercentLayout());
        rootPanel.setOpaque(false);
    }
    
    /**
     * Add a PlotterRegionListener which will be registered with all regions created by this plotter.
     * @param listener The PlotterRegionListener to register with created regions.
     */
    public void addPlotterRegionListener(PlotterRegionListener listener) {
        plotterRegionListeners.add(listener);
    }
        
    /**
     * Show the regions of this plotter, first setting them up if necessary.
     */
    public void show() {
        plotRegions();
    }

    /**
     * This will set the Plotter's JPanel to invisible. 
     */
    public void hide() {
        rootPanel.setVisible(false);
    }

    /**
     * Clear all the regions of this plotter.
     */
    public void destroyRegions() {
        regions.clear();
        this.isSetup = false;
    }

    /**
     * Get the panel containing the graphics for all of this plotter's regions.
     * @return The panel with the plots.
     */
    public JPanel panel() {
        return this.rootPanel;
    }

    /**
     * Write the graphics to a file.
     * @param file The file name, from which the type is inferred from the extension.
     */
    public void writeToFile(String file) throws IOException {
        String extension = "";
        int i = file.lastIndexOf('.');
        if (i > 0) {
            extension = file.substring(i + 1);
        } else {
            throw new IllegalArgumentException("File name has no extension: " + file);
        }
        writeToFile(file, extension);
    }

    /**
     * Write the graphics to a file.
     * @param file The file name.
     * @param type The file type (such as "PNG").
     */
    public void writeToFile(String file, String type) throws IOException {
        if (!isSetup) { 
            plotRegions();
        }
        if (!file.endsWith(type))
            file = file + "." + type;
        System.out.println("Saving plots to " + file);
        super.writeToFile(file, type, null);
    }
    
    /**
     * This needs to be overridden because there is no access to the list of regions 
     * from the parent class.
     */
    public IPlotterRegion region(int index) {
        if (index < 0 || index >= regions.size())
            throw new IllegalArgumentException("Invalid index for region: " + index);
        return (IPlotterRegion) regions.get(index);
    }
    
    /* ------------------------------------------------------------------------------------ */

    /**
     * Overridden from DummyPlotter to use the JFree implementation of IPlotterRegion.
     */
    protected IPlotterRegion justCreateRegion(double x, double y, double width, double height) {
                
        if (width <= 0)
            throw new IllegalArgumentException("The width parameter must be > 0.");
        
        if (height <= 0)
            throw new IllegalArgumentException("The height parameter must be > 0.");
        
        // Create a new region with full width, height, x position and y position parameters.
        PlotterRegion region = new PlotterRegion(this.style(), x, y, width, height);
        
        for (PlotterRegionListener listener : this.plotterRegionListeners) {            
            region.state.addRegionListener(listener);
        }                  
        
        // This makes sure the region by default has a style object that chains back to the plotter 
        // as its parent.  It can be overridden by setting a custom style on the region.
        region.setStyle(new DefaultPlotterStyle("region", (PlotterStyle)style()));
        
        // Add the region to the list of regions.       
        regions.add(region);
        
        return region;
    }

    /**
     * Plot all the regions into the root panel.
     */
    void plotRegions() {
        if (!isSetup) {
            for (int i = 0; i < numberOfRegions(); i++) {
                
                // Get a region by index.
                PlotterRegion region = (PlotterRegion) region(i);
                
                // Add the region's panel to the root panel.
                region.addToParentPanel(rootPanel);
            }
            isSetup = true;
        }
    }           
}