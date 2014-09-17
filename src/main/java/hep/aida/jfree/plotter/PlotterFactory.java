package hep.aida.jfree.plotter;

import hep.aida.IPlotter;
import hep.aida.IPlotterStyle;
import hep.aida.ref.plotter.PlotterStyle;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class PlotterFactory extends hep.aida.ref.plotter.PlotterFactory {

    String name;
    boolean embedded = false;

    public PlotterFactory() {
        super();
    }

    public PlotterFactory(String name) {
        super();
        this.name = name;
    }

    public IPlotter create(String plotterName) {
        Plotter plotter = new Plotter();
        plotter.setIsEmbedded(embedded);
        return plotter;
    }

    public IPlotter create() {
        return create((String) null);
    }
    
    protected void setEmbedded(boolean embedded) {
        this.embedded = embedded;
    }
    
    public IPlotterStyle createPlotterStyle(IPlotterStyle style) {
        return new DefaultPlotterStyle((PlotterStyle)style);
    }
    
    public IPlotterStyle createDefaultHistogram1DStyle() {
        return new DefaultHistogram1DStyle();
    }
}