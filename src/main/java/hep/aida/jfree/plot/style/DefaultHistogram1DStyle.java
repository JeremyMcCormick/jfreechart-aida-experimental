package hep.aida.jfree.plot.style;

import hep.aida.ref.plotter.PlotterStyle;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class DefaultHistogram1DStyle extends PlotterStyle {
    
    public DefaultHistogram1DStyle() {
        applyDefaultSettings();
    }
    
    private void applyDefaultSettings() {
        this.gridStyle().setVisible(false);
        this.setParameter("allowZeroSuppression", "false");
        this.dataStyle().markerStyle().setVisible(false);
        this.dataStyle().lineStyle().setVisible(false);
        this.dataStyle().lineStyle().setThickness(2);
        this.dataStyle().fillStyle().setVisible(false);
        this.dataStyle().outlineStyle().setVisible(false);
        this.dataBoxStyle().setVisible(true);
        this.dataBoxStyle().borderStyle().setVisible(true);
        this.dataBoxStyle().borderStyle().setThickness(2);
        this.dataBoxStyle().borderStyle().setColor("black");
        this.dataBoxStyle().backgroundStyle().setVisible(true);
        this.dataBoxStyle().backgroundStyle().setColor("white");
        this.xAxisStyle().lineStyle().setThickness(4);
        this.yAxisStyle().lineStyle().setThickness(4);
    }
}
