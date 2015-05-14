package hep.aida.jfree.plotter;

import hep.aida.ref.plotter.PlotterStyle;

/**
 * This is essentially just a wrapper to the <code>PlotterStyle</code> class defined in FreeHEP. It is extended to allow
 * setting of a parent style, which uses a protected constructor of the super class.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
class DefaultPlotterStyle extends PlotterStyle {

    public DefaultPlotterStyle(PlotterStyle parentStyle) {
        super(parentStyle);

        applyDefaults(this);
    }

    /**
     * Use given style as parent.
     * 
     * @param parentStyle
     */
    public DefaultPlotterStyle(String name, PlotterStyle parentStyle) {
        super(parentStyle);
        setName(name);
    }

    /**
     * No parent style. It should be set manually.
     */
    public DefaultPlotterStyle(String name) {
        setName(name);
    }

    /**
     * No parent style. It should be set manually.
     */
    public DefaultPlotterStyle() {
        setName("DefaultPlotterStyle");
    }

    /**
     * Apply a few defaults to avoid annoying behavior like a border showing around the region.
     * 
     * @param style the style to apply the defaults
     */
    static void applyDefaults(final PlotterStyle style) {
        style.regionBoxStyle().borderStyle().setVisible(false);
    }
}
