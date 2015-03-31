package hep.aida.jfree.plotter.style.registry;

import hep.aida.jfree.plotter.style.util.StyleConstants;
import hep.aida.ref.plotter.PlotterStyle;
import hep.aida.ref.plotter.style.registry.StyleRegistry;

/**
 * This is a set of default <code>IPlotterStyle</code> implementations that can be
 * registered in the global AIDA <code>StyleRegistry</code> so that they are accessible
 * to user plotting code.
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public final class DefaultPlotterStyles {
    
    static final String STORE_NAME = "DefaultStyleStore";
    
    static final PlotterStyle basePlotterStyle = new BasePlotterStyle();
    
    private DefaultPlotterStyles() {        
    }
    
    public static void registerDefaultStyles() {
        DefaultStyleStore store = (DefaultStyleStore)StyleRegistry.getStyleRegistry().getStore(STORE_NAME);
        store.addStyle(new DefaultHistogram1DStyle());
        store.addStyle(new DefaultCloud2DStyle());
        store.addStyle(new DefaultBoxPlotStyle());
        store.addStyle(new DefaultColorMapStyle());
        store.addStyle(new DefaultFunctionStyle());
        store.addStyle(new DefaultDataPointSetStyle());
        store.addStyle(new ROOTHistogram1DStyle());
    }
    
    public static class DefaultPlotterStyle extends PlotterStyle {        
        DefaultPlotterStyle(PlotterStyle plotterStyle) {
            super(plotterStyle);
            setName(getClass().getSimpleName());
        }                
    }
   
    public static class BasePlotterStyle extends PlotterStyle {        
        BasePlotterStyle() {
            setName(getClass().getSimpleName());
        }                
    }
        
    public static class DefaultHistogram1DStyle extends PlotterStyle {
        DefaultHistogram1DStyle() {
            super(basePlotterStyle);
            setName(getClass().getSimpleName());
            
            gridStyle().setVisible(false);
            dataStyle().fillStyle().setVisible(true);
            dataStyle().lineStyle().setVisible(true);
            dataStyle().outlineStyle().setVisible(false);
            dataStyle().markerStyle().setVisible(false);
            legendBoxStyle().setVisible(false);
        }
    }    
    
    public static class ROOTHistogram1DStyle extends DefaultHistogram1DStyle {
        ROOTHistogram1DStyle() {
            setName(getClass().getSimpleName());
            
            dataStyle().lineStyle().setColor("black");
            dataStyle().lineStyle().setVisible(false);
            dataStyle().errorBarStyle().setVisible(false);
            dataStyle().fillStyle().setVisible(false);            
            
            titleStyle().textStyle().setFontSize(20);
            titleStyle().textStyle().setFont("Arial");
            titleStyle().textStyle().setBold(true);
            
            xAxisStyle().lineStyle().setThickness(1);
            xAxisStyle().labelStyle().setBold(true);
            xAxisStyle().labelStyle().setFont("Arial");
            xAxisStyle().labelStyle().setFontSize(16);
            yAxisStyle().lineStyle().setThickness(1);
            yAxisStyle().labelStyle().setBold(true);
            yAxisStyle().labelStyle().setFont("Arial");
            yAxisStyle().labelStyle().setFontSize(16);
            
            titleStyle().boxStyle().borderStyle().setVisible(true);
            titleStyle().boxStyle().borderStyle().setThickness(1);

            legendBoxStyle().setVisible(false);                        
        }
    }
    
    public static class DefaultCloud2DStyle extends PlotterStyle {
        DefaultCloud2DStyle() {
            super(basePlotterStyle);
            setName(getClass().getSimpleName());      
            
            dataStyle().markerStyle().setVisible(true);
            dataStyle().markerStyle().setShape("circle");
            dataStyle().markerStyle().setSize(2);
            legendBoxStyle().setVisible(false);
        }
    }
    
    public static class DefaultDataPointSetStyle extends PlotterStyle {
        DefaultDataPointSetStyle() {
            super(basePlotterStyle);
            setName(getClass().getSimpleName());
            
            dataStyle().markerStyle().setShape("diamond");
            dataStyle().outlineStyle().setVisible(true);
            dataStyle().outlineStyle().setThickness(1);
            dataStyle().outlineStyle().setColor("black");
            
            legendBoxStyle().setVisible(false);
        }                
    }
    
    public static class DefaultBoxPlotStyle extends PlotterStyle {
        DefaultBoxPlotStyle() {
            super(basePlotterStyle);
            setName(getClass().getSimpleName());
            
            dataStyle().fillStyle().setVisible(false);
            dataStyle().lineStyle().setColor("blue");
            dataStyle().lineStyle().setThickness(1);
            
            legendBoxStyle().setVisible(false);
        }
    }
    
    public static class DefaultColorMapStyle extends PlotterStyle {
        DefaultColorMapStyle() {
            super(basePlotterStyle);
            this.setName(getClass().getSimpleName());          
            
            setParameter(StyleConstants.HIST2DSTYLE, "colorMap");
            dataStyle().fillStyle().setParameter(StyleConstants.COLOR_MAP_SCHEME, "rainbow");
            dataStyle().fillStyle().setParameter(StyleConstants.SHOW_ZERO_HEIGHT_BINS, "false");
            
            legendBoxStyle().setVisible(true);
        }
    }
    
    public static class DefaultFunctionStyle extends PlotterStyle {
        DefaultFunctionStyle() {
            super(basePlotterStyle);
            
            setName(getClass().getSimpleName());
            dataStyle().lineStyle().setColor("red");
            dataStyle().lineStyle().setThickness(2);
            
            legendBoxStyle().setVisible(false);
        }
    }
}
