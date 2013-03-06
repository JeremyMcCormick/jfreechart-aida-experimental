package hep.aida.jfree;

import hep.aida.IAnalysisFactory;
import hep.aida.IHistogram2D;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;
import hep.aida.IPlotterFactory;
import hep.aida.jfree.converter.Histogram2DConverter;

import java.awt.Color;
import java.util.Random;

import javax.swing.JFrame;

import junit.framework.TestCase;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class XYBoxRendererTest extends TestCase
{
    public void test()
    {
        AnalysisFactory.register();
        IAnalysisFactory af = IAnalysisFactory.create();
        IPlotterFactory pf = af.createPlotterFactory();
        IHistogramFactory hf = af.createHistogramFactory(null);

        // Create plotter
        IPlotter plotter = pf.create();

        // Create a 2D histo
        IHistogram2D h2d = hf.createHistogram2D("h2d", 10, 0., 10., 10, 0., 10.);
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            h2d.fill(rand.nextDouble() * 10, rand.nextDouble() * 10);
        }

        JFreeChart chart = new Histogram2DConverter().toBoxPlot(h2d, plotter.style());
        chart.getXYPlot().setDomainGridlinesVisible(true);
        chart.getXYPlot().setRangeGridlinesVisible(true);
        chart.getXYPlot().getRenderer(0).setSeriesOutlinePaint(0, Color.red);
                
        chart.fireChartChanged();
        
        JFrame frame = new JFrame();
        frame.setContentPane(new ChartPanel(chart));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
