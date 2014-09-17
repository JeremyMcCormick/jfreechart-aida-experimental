package hep.aida.jfree.plotter;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
class DefaultHistogram1DStyle extends DefaultPlotterStyle { 
   
    public DefaultHistogram1DStyle() {
        super("DefaultHistogram1DStyle");
        applyDefaultSettings();
    }
    
    private void applyDefaultSettings() {
        gridStyle().setVisible(false);
        setParameter("allowZeroSuppression", "false");
        dataStyle().markerStyle().setVisible(false);
        dataStyle().lineStyle().setVisible(true);
        dataStyle().lineStyle().setThickness(2);
        dataStyle().fillStyle().setVisible(true);
        dataStyle().outlineStyle().setVisible(false);
        //dataBoxStyle().setVisible(true);
        //dataBoxStyle().borderStyle().setVisible(true);
        //dataBoxStyle().borderStyle().setThickness(2);
        //dataBoxStyle().borderStyle().setColor("black");
        //dataBoxStyle().backgroundStyle().setVisible(true);
        //dataBoxStyle().backgroundStyle().setColor("white");
        xAxisStyle().lineStyle().setThickness(4);
        yAxisStyle().lineStyle().setThickness(4);
    }
}
