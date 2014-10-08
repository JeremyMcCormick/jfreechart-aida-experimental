package hep.aida.example;

import hep.aida.IAnalysisFactory;
import hep.aida.IPlotterFactory;
import hep.aida.IPlotterStyle;
import hep.aida.ref.plotter.PlotterStyle;
import hep.aida.ref.plotter.style.registry.BaseStyleStore;
import hep.aida.ref.plotter.style.registry.StyleRegistry;
import junit.framework.TestCase;

/**
 * Toy example of using the StyleRegistry to create and register global styles.
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class StyleRegistryTest extends TestCase {
    
    static IPlotterFactory plotterFactory = IAnalysisFactory.create().createPlotterFactory();
    
    public void testStyleRegistry() {       
        StyleRegistry styleRegistry = StyleRegistry.getStyleRegistry();
        InMemoryCloningStyleStore styleStore = new InMemoryCloningStyleStore();
        
        styleStore.addStyle(new DefaultHistogram1DStyle());
        styleStore.addStyle(new DefaultCloud2DStyle());
        
        styleRegistry.addStore(styleStore);

        IPlotterStyle foundStyle = styleRegistry.getStore("DefaultStyleStore").getStyle("DefaultHistogram1DStyle");        
        System.out.println("found style " + foundStyle.name() + " with type " + foundStyle.getClass().getCanonicalName());
        System.out.println("  gridStyle.isVisible: " + foundStyle.gridStyle().isVisible());        
        
        foundStyle = styleRegistry.getStore("DefaultStyleStore").getStyle("DefaultCloud2DStyle");        
        System.out.println("found style " + foundStyle.name() + " with type " + foundStyle.getClass().getCanonicalName());
        System.out.println("  markerStyle.isVisible: " + foundStyle.dataStyle().markerStyle().isVisible());
    }
    
    static class InMemoryCloningStyleStore extends BaseStyleStore {

        InMemoryCloningStyleStore() {
            super("DefaultStyleStore");
        }
        
        public void commit() {
            throw new UnsupportedOperationException("The commit method is not implemented.");
        }
        
        public void addStyle(IPlotterStyle style) {
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
    
    static class DefaultPlotterStyle extends PlotterStyle {        
        DefaultPlotterStyle(PlotterStyle parent) {
            super(parent);
        }        
    }
            
    static class DefaultHistogram1DStyle extends PlotterStyle {
        DefaultHistogram1DStyle(PlotterStyle style) {
            super(style);
            this.setName("DefaultHistogram1DStyle");
            setDefaults();
        }        
        
        DefaultHistogram1DStyle() {
            this.setName("DefaultHistogram1DStyle");
            setDefaults();
        }
        
        private void setDefaults() {
            this.gridStyle().setVisible(false);
            this.dataStyle().fillStyle().setVisible(true);
            this.dataStyle().lineStyle().setVisible(true);
            this.dataStyle().outlineStyle().setVisible(false);
            this.dataStyle().markerStyle().setVisible(false);
        }
    }    
    
    static class DefaultCloud2DStyle extends PlotterStyle {
        DefaultCloud2DStyle(PlotterStyle style) {
            super(style);
            this.setName("DefaultCloud2DStyle");
            setDefaults();
        }        
        
        DefaultCloud2DStyle() {
            this.setName("DefaultCloud2DStyle");
            setDefaults();
        }
        
        private void setDefaults() {
            this.dataStyle().markerStyle().setVisible(true);
            this.dataStyle().markerStyle().setShape("diamond");
        }
    }        
}
