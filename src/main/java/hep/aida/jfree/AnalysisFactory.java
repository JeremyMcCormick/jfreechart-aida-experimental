package hep.aida.jfree;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.renderer.xy.XYBarRenderer;

import hep.aida.IPlotterFactory;
import hep.aida.jfree.chart.DefaultChartTheme;
import hep.aida.jfree.plotter.PlotterFactory;

/**
 * JFreeChart AnalysisFactory
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class AnalysisFactory extends hep.aida.ref.AnalysisFactory {

    public static void configure() {
        ChartFactory.setChartTheme(new DefaultChartTheme());
        XYBarRenderer.setDefaultShadowsVisible(false);
    }

    /**
     * Register this class as the AIDA AnalysisFactory by setting the system property.
     */
    public final static void register() {        
        // Set the system property for this class to provide the AIDA factory implementation.
        System.setProperty("hep.aida.IAnalysisFactory", AnalysisFactory.class.getName());

        // Setup JFreeChart default configuration.
        configure();
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
