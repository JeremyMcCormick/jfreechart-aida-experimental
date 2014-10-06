package hep.aida.jfree.plotter;

import hep.aida.IPlotterStyle;

/**
 * A class for associating a plotter object with its style, as a pair.
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class ObjectStyle {
    
    Object object;
    IPlotterStyle style;
    
    public ObjectStyle(Object object, IPlotterStyle style) {
        this.object = object;
        this.style = style;
    }
    
    public Object object() {
        return object;
    }
    
    public IPlotterStyle style() {
        return style;
    }
}
