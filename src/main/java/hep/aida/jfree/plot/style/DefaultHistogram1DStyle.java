package hep.aida.jfree.plot.style;

import hep.aida.ref.plotter.PlotterStyle;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class DefaultHistogram1DStyle extends PlotterStyle {
    
    public DefaultHistogram1DStyle() {
        //initializeBaseStyle();
        applyDefaultSettings();
    }
    
    private void applyDefaultSettings() {
        this.gridStyle().setVisible(false);
        this.setParameter("allowZeroSuppression", "false");
        this.dataStyle().markerStyle().setVisible(false);
        this.dataStyle().lineStyle().setVisible(false);
        this.dataStyle().lineStyle().setThickness(4);
        this.dataStyle().fillStyle().setVisible(false);
        this.dataStyle().outlineStyle().setVisible(false);
        this.dataStyle().fillStyle().setOpacity(0.0);
    }
}
