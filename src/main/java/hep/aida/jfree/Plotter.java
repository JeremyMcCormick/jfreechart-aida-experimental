package hep.aida.jfree;

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
final class Plotter extends DummyPlotter {
    
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
    
    public void show() {
        frame = new JFrame();
        plotRegions();
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);        
    }
    
    public void hide() {
        frame.setVisible(false);
    }
    
    public void destroyRegions() {        
    }

    public void writeToFile(String file, String type) throws IOException {
    }
    
    private void plotRegions() {        
        for (int i=0; i<numberOfRegions(); i++) {
            PlotterRegion r = (PlotterRegion)region(i);
            JPanel rp = r.getPanel();
            if (rp == null) {
                continue;
            } 
            // Place and scale the region into the plotter's JPanel
            //System.out.println("plotting region: " + i);
            //System.out.println("x=" + r.x()*100 + ", y=" + r.y()*100 + ", w=" + r.width()*100 + ", h=" + r.height()*100);
            //System.out.println();
            rootPanel.add(rp, new PercentLayout.Constraint(r.x()*100, r.y()*100, r.width()*100, r.height()*100));
        }
    }
     
    /**
     * Overridden from DummyPlotter to use the JFree implementation of IPlotterRegion.
     */
    protected IPlotterRegion justCreateRegion(double x, double y, double width, double height) {
        //System.out.println(this.getClass().getSimpleName() +
        //        ".justCreateRegion => x = " + x + ", y = " + y + ", w = " + width + ", " + ", h = " + height);
        PlotterRegion region = new PlotterRegion(x, y, width, height);    
        region.setStyle(this.style());
        regions.add(region);
        return region;
    }
              
    /**
     * This needs to be overridden because there is no access to the list of regions in order to add one ourselves.     
     */
    public IPlotterRegion region(int index) {
        if (index < 0 || index >= regions.size()) throw new IllegalArgumentException("Invalid index for region: " + index);
        return (IPlotterRegion)regions.get(index);
    } 
}