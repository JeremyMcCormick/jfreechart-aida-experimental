package hep.aida.jfree;

import hep.aida.IPlotterFactory;
import hep.aida.jfree.plotter.PlotterFactory;
import hep.aida.jfree.plotter.style.registry.DefaultPlotterStyles;
import hep.aida.jfree.plotter.style.registry.InMemoryCloningStyleStore;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.renderer.xy.XYBarRenderer;

/**
 * JFreeChart AnalysisFactory
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class AnalysisFactory extends hep.aida.ref.AnalysisFactory {

    /**
     * Perform various static configuration.
     */
    public final static void configure() {
        // Static initialization of the style registry.
        InMemoryCloningStyleStore.initialize();
        DefaultPlotterStyles.registerDefaultStyles();
        
        // Static initialization of the default look and feel for JFreeChart.
        ChartFactory.setChartTheme(new DefaultChartTheme());
        XYBarRenderer.setDefaultShadowsVisible(false);
    }    
    
    /**
     * Register this class as the AIDA AnalysisFactory by setting the system property.
     */
    public final static void register() {        
        // Set the system property for this class to provide the AIDA factory implementation.
        System.setProperty("hep.aida.IAnalysisFactory", AnalysisFactory.class.getName());
    }

    /**
     * Create a named plotter factory.
     * @return The JFreeChart implementation of IPlotterFactory.
     */
    public IPlotterFactory createPlotterFactory(String name) {
        return new PlotterFactory(name);
    }

    /**
     * Create an unnamed plotter factory.
     */
    public IPlotterFactory createPlotterFactory() {
        return new PlotterFactory(null);
    }
}
