package hep.aida.jfree;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.renderer.xy.XYBarRenderer;

import hep.aida.IPlotterFactory;

/**
 * JFreeChart AnalysisFactory
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public final class AnalysisFactory extends hep.aida.ref.AnalysisFactory
{

    static void configure()
    {
        ChartFactory.setChartTheme(new DefaultChartTheme());
        XYBarRenderer.setDefaultShadowsVisible(false);
    }

    /**
     * Register this class as the AIDA AnalysisFactory by setting the magic system property
     */
    final static void register()
    {
        // Set the system property that will cause this class to be the default AIDA factory implementation.
        System.setProperty("hep.aida.IAnalysisFactory", AnalysisFactory.class.getName());

        // Setup JFreeChart default configuration.
        configure();
    }

    /**
     * Create a named plotter factory
     */
    public IPlotterFactory createPlotterFactory(String name)
    {
        return new PlotterFactory(name);
    }

    /**
     * Create an unnamed plotter factory
     */
    public IPlotterFactory createPlotterFactory()
    {
        return new PlotterFactory(null);
    }
}