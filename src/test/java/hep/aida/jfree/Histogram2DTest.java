package hep.aida.jfree;

import hep.aida.IAnalysisFactory;
import hep.aida.IAxisStyle;
import hep.aida.IHistogram2D;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;
import hep.aida.IPlotterFactory;
import hep.aida.IPlotterStyle;
import hep.aida.ITextStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import junit.framework.TestCase;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Histogram2DTest extends TestCase {
    
    private IAnalysisFactory af;
    private IPlotterFactory pf;
    private IHistogramFactory hf;
    private JPanel panel = null;
        
    protected void setUp() {
        AnalysisFactory.register();
        af = IAnalysisFactory.create();
        pf = af.createPlotterFactory();
        hf = af.createHistogramFactory(null);
    }
    
    private final IHistogram2D histogram2D() {
        IHistogram2D h2d = hf.createHistogram2D("h2d", 50, -5.0, 5.0, 50, -5.0, 5.0);
        Random rand = new Random();
        for (int i = 0; i< 10000000; i++) {
            h2d.fill(rand.nextGaussian(), rand.nextGaussian());
        }        
        return h2d;
    }
        
    public void testHistogram2D() throws Exception {
                
        // Create plotter
        IPlotter plotter = pf.create();
                       
        // Create a 1D histo
        IHistogram2D h2d = histogram2D();
        
        // Set labels for axes automatically based on title
        h2d.annotation().addItem("xAxisLabel", h2d.title() + " X");
        h2d.annotation().addItem("yAxisLabel", h2d.title() + " Y");
        
        // Create region for showing plot
        plotter.createRegion();
        
        IPlotterStyle pstyle = plotter.style();
                
        // title style
        ITextStyle titleStyle = pstyle.titleStyle().textStyle();
        titleStyle.setBold(true);
        titleStyle.setFontSize(30.);
        titleStyle.setFont("Arial");
        titleStyle.setColor("black");
        
        // axis style
        List<IAxisStyle> axes = new ArrayList<IAxisStyle>();
        axes.add(pstyle.xAxisStyle());
        axes.add(pstyle.yAxisStyle());        
        for (IAxisStyle axisStyle : axes) {        
            axisStyle.labelStyle().setBold(true);
            axisStyle.labelStyle().setFont("Helvetica");
            axisStyle.labelStyle().setFontSize(15);
            axisStyle.labelStyle().setColor("black");    
            axisStyle.lineStyle().setColor("black");
            axisStyle.lineStyle().setThickness(2);        
            axisStyle.tickLabelStyle().setColor("black");
            axisStyle.tickLabelStyle().setFont("Helvetica");
            axisStyle.tickLabelStyle().setBold(true);
            axisStyle.tickLabelStyle().setFontSize(10);
        }
        
        // background color
        pstyle.regionBoxStyle().backgroundStyle().setColor("white");
        
        // Log scale.
        pstyle.zAxisStyle().setParameter("scale", "log");
        
        // Plot histogram into region.
        plotter.region(0).plot(h2d);
        
        // Show time
        plotter.show();
        panel = ((PlotterRegion)plotter.region(0)).getPanel();
        //Thread.sleep(5000); // Yeah, I know.
    }
    
    public void tearDown() {
        while (true) 
        { 
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    /*
    public static void main(String[] args) throws Exception
    {
        Histogram2DTest test = new Histogram2DTest();
        test.setUp();
        test.testHistogram2D();
    }
    */
}
