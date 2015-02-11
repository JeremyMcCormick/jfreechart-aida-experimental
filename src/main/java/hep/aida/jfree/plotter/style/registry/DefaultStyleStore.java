package hep.aida.jfree.plotter.style.registry;

import hep.aida.IPlotterStyle;
import hep.aida.jfree.plotter.style.registry.DefaultPlotterStyles.DefaultPlotterStyle;
import hep.aida.ref.plotter.PlotterStyle;
import hep.aida.ref.plotter.style.registry.BaseStyleStore;
import hep.aida.ref.plotter.style.registry.StyleRegistry;

public class DefaultStyleStore extends BaseStyleStore {

    static boolean initialized;
    
    static final String STORE_NAME = "DefaultStyleStore";
    
    private DefaultStyleStore() {
        super(STORE_NAME);
    }
    
    public static void initialize() {
        if (StyleRegistry.getStyleRegistry().getStore(STORE_NAME) == null) {
            StyleRegistry.getStyleRegistry().addStore(new DefaultStyleStore());
            DefaultPlotterStyles.registerDefaultStyles();
        }
    }
    
    public void commit() {
        throw new UnsupportedOperationException("The commit method is not implemented.");
    }
    
    public void addStyle(IPlotterStyle style) { 
        if (this.getStyle(style.name()) != null)
                throw new IllegalArgumentException("There is already a style named " + style.name() + " in this store.");
        addStyle(style.name(), style);
    }
    
    public IPlotterStyle getStyle(String styleName) {
                
        IPlotterStyle style = super.getStyle(styleName);
                
        // Clone the style instead of just returning it.
        if (style != null) {
            return new DefaultPlotterStyle((PlotterStyle)style);
        } else {
            return null;
        }
    }
}