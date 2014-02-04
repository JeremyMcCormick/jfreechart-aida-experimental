package hep.aida.jfree.plotter;

import hep.aida.IPlotterRegion;
import hep.aida.ref.plotter.DummyPlotter;
import jas.util.layout.PercentLayout;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This class implements a JFreeChart <tt>IPlotter</tt> by extending AIDA's
 * <tt>DummyPlotter</tt> class.  It can be run in standalone mode (the default)
 * or embedded in another Swing component.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Plotter extends DummyPlotter {

    List<PlotterRegion> regions = new ArrayList<PlotterRegion>();
    JPanel rootPanel;
    JFrame frame;
    boolean embedded = false;
    boolean setup = false;

    /**
     * Class constructor.
     */
    Plotter() {
        configureRootPanel();
    }

    /**
     * Configure the root panel by creating a new JPanel and settings its layout.
     */
    private void configureRootPanel() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new PercentLayout());
        rootPanel.setOpaque(false);
    }

    /**
     * Setup the regions, which includes JFrame configuration and redrawing
     * the root component.
     */
    private void setupRegions() {
        if (!setup) {
            createFrame();
            plotRegions();
            configureFrame();
            redraw();         
            setup = true;
        }
    }

    /**
     * Configure the <tt>JFrame</tt> for the plotter.
     * This will only have an effect if the plotter is not embedded.
     */
    private void configureFrame() {
        if (!embedded) {
            frame.setContentPane(rootPanel);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
        }
    }

    /**
     * Create the <tt>JFrame</tt> for the plotter.
     * This will only have an effect if the plotter is not embedded.
     */
    private void createFrame() {
        if (!embedded) {
            frame = new JFrame();
        }
    }
    
    /**
     * This will redraw the frame in which the the plotter's regions are embedded,
     * whether or not it is being run embedded or standalone.
     */
    private void redraw() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    Component c = SwingUtilities.getRoot(rootPanel);
                    if (c != null) {
                        c.invalidate();
                        c.validate();
                        c.repaint();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Set whether or not to use a standalone JFrame to display this plotter's regions.
     * If <tt>embedded</tt> is set to true, then the user framework will need to manually
     * embed it into their application by calling {@link #panel()} to get the <tt>JPanel</tt>.
     * @param embedded Set to true to run in embedded mode without a JFrame.  
     */
    public void setEmbedded(boolean embedded) {
        this.embedded = embedded;
    }

    /**
     * Show the regions of this plotter, first setting them up if necessary.
     */
    public void show() {
        setupRegions();
        if (!embedded) {
            frame.setVisible(true);
        }
    }

    /**
     * This will hide the plotter's regions and set its frame to null, 
     * but only if it is being run standalone and not embedded. 
     */
    public void hide() {
        if (frame != null) {
            frame.setVisible(false);
            frame = null;
        }
        setup = false;
    }

    /**
     * Clear all the regions of this plotter.
     */
    public void destroyRegions() {
        regions.clear();
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
        if (!setup) { // used to check frame == null
            setupRegions();
        }
        if (!file.endsWith(type))
            file = file + "." + type;
        System.out.println("Saving to " + file + " with type " + type);
        super.writeToFile(file, type, null);
    }

    /**
     * Plot all the regions into the root panel.
     */
    private void plotRegions() {
        for (int i = 0; i < numberOfRegions(); i++) {
            PlotterRegion region = (PlotterRegion) region(i);
            JPanel regionPanel = region.getPanel();
            if (regionPanel == null) {
                System.out.println("WARNING: Skipping region " + i + " with null JPanel!");
                continue;
            }
            rootPanel.add(regionPanel, 
                    new PercentLayout.Constraint(
                            region.x() * 100, 
                            region.y() * 100, 
                            region.width() * 100, 
                            region.height() * 100));
        }
    }

    /**
     * Overridden from DummyPlotter to use the JFree implementation of IPlotterRegion.
     */
    protected IPlotterRegion justCreateRegion(double x, double y, double width, double height) {
        PlotterRegion region = new PlotterRegion(this.style(), x, y, width, height);
        regions.add(region);
        return region;
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
}