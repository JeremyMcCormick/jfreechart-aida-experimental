package hep.aida.jfree.plotter;

import hep.aida.IPlotterStyle;


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
