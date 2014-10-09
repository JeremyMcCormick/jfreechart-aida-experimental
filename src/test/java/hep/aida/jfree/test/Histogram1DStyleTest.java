package hep.aida.jfree.test;

import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.plotter.PlotterFactory;

import java.util.Random;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram1DStyleTest extends AbstractPlotTest {
    
    static double mean = 0f;
    static double variance = 2f;
    
    public void test() {

        //IHistogram1D histogram = histogramFactory.createHistogram1D("h1d", 20, 45f, 55f);
        IHistogram1D histogram = histogramFactory.createHistogram1D("h1d", 20, -10f, 10f);
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {            
            histogram.fill(mean + rand.nextGaussian() * variance);
        }        
                
        // Set labels for axes.
        histogram.annotation().addItem("xAxisLabel", histogram.title() + " X");
        histogram.annotation().addItem("yAxisLabel", histogram.title() + " Y");

        // Create regions for showing plots.
        plotter.createRegions(3, 3, 0);

        // 0) Filled with bars.        
        IPlotterStyle pstyle = plotterFactory.createPlotterStyle();
        pstyle.gridStyle().setVisible(false);
        plotter.region(0).plot(histogram, pstyle);
        plotter.region(0).setTitle("0) default style");
        
        // 1) Filled plus outline.
        pstyle = plotterFactory.createPlotterStyle();
        pstyle.legendBoxStyle().setVisible(true);
        pstyle.gridStyle().setVisible(false);        
        pstyle.dataStyle().lineStyle().setVisible(false);
        pstyle.dataStyle().fillStyle().setColor("purple");        
        plotter.region(1).plot(histogram, pstyle);
        plotter.region(1).setTitle("1) no lines");

        // 2) No fill with bars drawn.
        pstyle = plotterFactory.createPlotterStyle();
        pstyle.legendBoxStyle().setVisible(true);
        pstyle.gridStyle().setVisible(false);
        pstyle.dataStyle().lineStyle().setVisible(true);
        pstyle.dataStyle().lineStyle().setColor("green");
        pstyle.dataStyle().fillStyle().setVisible(false);
        plotter.region(2).plot(histogram, pstyle);
        plotter.region(2).setTitle("2) no fill");

        // 3) No fill with outline only.
        pstyle = plotterFactory.createPlotterStyle();
        pstyle.gridStyle().setVisible(false);
        pstyle.dataStyle().lineStyle().setVisible(false); 
        pstyle.dataStyle().errorBarStyle().setVisible(false);
        plotter.region(3).plot(histogram, pstyle);
        plotter.region(3).setTitle("3) fill only");

        // 4) Show errors only.
        pstyle = plotterFactory.createPlotterStyle();
        pstyle.legendBoxStyle().setVisible(true);
        pstyle.gridStyle().setVisible(false);
        pstyle.dataStyle().errorBarStyle().setVisible(true);
        pstyle.dataStyle().outlineStyle().setVisible(false);
        pstyle.dataStyle().fillStyle().setVisible(false);
        pstyle.dataStyle().lineStyle().setVisible(false);
        plotter.region(4).plot(histogram, pstyle);
        plotter.region(4).setTitle("4) errors only");

        // 5) Show data only in outline style.
        pstyle = plotterFactory.createPlotterStyle();
        pstyle.legendBoxStyle().setVisible(true);
        pstyle.gridStyle().setVisible(false);
        pstyle.dataStyle().setVisible(true);
        pstyle.dataStyle().errorBarStyle().setVisible(false);                
        plotter.region(5).plot(histogram, pstyle);
        plotter.region(5).setTitle("5) data only");

        // 6) Show grid.
        pstyle = plotterFactory.createPlotterStyle();
        pstyle.legendBoxStyle().setVisible(true);
        pstyle.dataStyle().setVisible(false);
        pstyle.gridStyle().setVisible(true);
        pstyle.gridStyle().setLineType("dashed");
        pstyle.gridStyle().setColor("red");
        plotter.region(6).plot(histogram, pstyle);
        plotter.region(6).setTitle("6) grid only");
        
        // 7) Show data marker.
        pstyle = plotterFactory.createPlotterStyle();
        pstyle.legendBoxStyle().setVisible(true);
        pstyle.gridStyle().setVisible(false);
        pstyle.dataStyle().markerStyle().setVisible(true);
        pstyle.dataStyle().markerStyle().setShape("dot");
        pstyle.dataStyle().markerStyle().setSize(3);
        pstyle.dataStyle().errorBarStyle().setVisible(true);
        pstyle.dataStyle().fillStyle().setVisible(false);
        pstyle.dataStyle().outlineStyle().setVisible(false);
        pstyle.dataStyle().lineStyle().setVisible(false);
        plotter.region(7).plot(histogram, pstyle);
        plotter.region(7).setTitle("7) data marker and errors");

        // 8) Show lines between points.
        pstyle = plotterFactory.createPlotterStyle();
        pstyle.legendBoxStyle().setVisible(true);
        pstyle.gridStyle().setVisible(false);
        pstyle.dataStyle().fillStyle().setVisible(false);
        pstyle.dataStyle().errorBarStyle().setVisible(false);
        pstyle.dataStyle().markerStyle().setVisible(false);
        pstyle.dataStyle().lineStyle().setVisible(false);        
        pstyle.dataStyle().outlineStyle().setVisible(true);
        pstyle.dataStyle().outlineStyle().setColor("blue");
        pstyle.dataStyle().outlineStyle().setLineType("dotted");
        pstyle.dataStyle().outlineStyle().setThickness(5);        
        plotter.region(8).plot(histogram, pstyle);
        plotter.region(8).setTitle("8) lines between points");
                       
        mode();
    }
}

