package hep.aida.jfree.plotter;

import hep.aida.IPlotter;

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
        plotter.setEmbedded(embedded);
        return plotter;
    }

    public IPlotter create() {
        return create((String) null);
    }
    
    protected void setEmbedded(boolean embedded) {
        this.embedded = embedded;
    }
}