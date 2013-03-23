package hep.aida.jfree.plotter;

import hep.aida.IPlotterRegion;
import hep.aida.ref.plotter.DummyPlotter;
import jas.util.layout.PercentLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
class Plotter extends DummyPlotter {

    List<PlotterRegion> regions = new ArrayList<PlotterRegion>();
    JPanel rootPanel;
    JFrame frame;

    Plotter() {
        configureRootPanel();
    }

    private void configureRootPanel() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new PercentLayout());
        rootPanel.setOpaque(false);
    }

    private void setupRegions() {
    	if (frame == null) {
    	    frame = new JFrame();
    	    plotRegions();
    	    frame.setContentPane(rootPanel);
    	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	    frame.pack();
    	}
    }

    public void show() {
        setupRegions();
        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }

    public void destroyRegions() {
    }

    public JPanel panel() {
        return this.rootPanel;
    }
    
    public void writeToFile(String file) throws IOException {
        String extension = "";
        int i = file.lastIndexOf('.');
        if (i > 0) {
            extension = file.substring(i+1);
        } else {
            throw new IllegalArgumentException("File name has no extension: " + file);
        }
        writeToFile(file, extension);
    }
   
    public void writeToFile(String file, String type) throws IOException {
        if (frame == null) {
            setupRegions();
        }
        if (!file.endsWith(type))
            file = file + "." + type;
        System.out.println("Saving to " + file + " with type " + type);
        super.writeToFile(file, type, null);
    }

    private void plotRegions() {
        for (int i = 0; i < numberOfRegions(); i++) {
            // System.out.println("plotting region = " + i);
            PlotterRegion r = (PlotterRegion) region(i);
            JPanel rp = r.getPanel();
            if (rp == null) {
                System.out.println("WARNING: skipping region; panel is null!");
                continue;
            }
            // Place and scale the region into the plotter's JPanel
            // System.out.println("plotting region: " + i);
            // System.out.println("x=" + r.x()*100 + ", y=" + r.y()*100 + ", w="
            // + r.width()*100 + ", h=" + r.height()*100);
            // System.out.println();
            rootPanel.add(rp, new PercentLayout.Constraint(r.x() * 100, r.y() * 100, r.width() * 100, r.height() * 100));
        }
    }

    /**
     * Overridden from DummyPlotter to use the JFree implementation of
     * IPlotterRegion.
     */
    protected IPlotterRegion justCreateRegion(double x, double y, double width, double height) {
        // System.out.println(this.getClass().getSimpleName() +
        // ".justCreateRegion => x = " + x + ", y = " + y + ", w = " + width +
        // ", " + ", h = " + height);
        PlotterRegion region = new PlotterRegion(this.style(), x, y, width, height);
        //region.setStyle(this.style());
        regions.add(region);
        return region;
    }

    /**
     * This needs to be overridden because there is no access to the list of
     * regions in order to add one ourselves.
     */
    public IPlotterRegion region(int index) {
        if (index < 0 || index >= regions.size())
            throw new IllegalArgumentException("Invalid index for region: " + index);
        return (IPlotterRegion) regions.get(index);
    }
}