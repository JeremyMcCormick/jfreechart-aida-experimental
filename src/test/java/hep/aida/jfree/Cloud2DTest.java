package hep.aida.jfree;

import hep.aida.IAnalysisFactory;
import hep.aida.IAxisStyle;
import hep.aida.ICloud2D;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;
import hep.aida.IPlotterFactory;
import hep.aida.IPlotterStyle;
import hep.aida.ITextStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

/**
 * This is basically an integration test.  It converts AIDA objects
 * to JFreeChart representations and plots them.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Cloud2DTest extends TestCase {
    
    IAnalysisFactory af;
    IPlotterFactory pf;
    IHistogramFactory hf;
        
    protected void setUp() {
        AnalysisFactory.register();
        af = IAnalysisFactory.create();
        pf = af.createPlotterFactory();
        hf = af.createHistogramFactory(null);
    }
    
    // Create a 2D cloud with randomly distributed points
    private final ICloud2D cloud2D() {              
        ICloud2D c2d = hf.createCloud2D("c2d");
        Random rand = new Random();
        for (int i = 0; i< 10000; i++) {
            c2d.fill(Math.abs(rand.nextDouble())*100, Math.abs(rand.nextDouble())*100);
        }        
        return c2d;
    }
        
    public void testCloud2D() throws Exception {
                
        // Create plotter
        IPlotter plotter = pf.create();
                       
        // Create a list with various types of histograms
        ICloud2D c2d = cloud2D();
        
        // Set labels for axes automatically based on title
        c2d.annotation().addItem("xAxisLabel", c2d.title() + " X");
        c2d.annotation().addItem("yAxisLabel", c2d.title() + " Y");        
        
        // Create 3x3 regions for showing plots
        plotter.createRegion();
        
        IPlotterStyle pstyle = plotter.style();
        
        pstyle.dataStyle().fillStyle().setColor("blue");
             
        // title style
        ITextStyle titleStyle = pstyle.titleStyle().textStyle();
        //titleStyle.setBold(true);
        titleStyle.setFontSize(30.);
        titleStyle.setFont("Times New Roman");
        titleStyle.setColor("black");
        
        // axis style
        List<IAxisStyle> axes = new ArrayList<IAxisStyle>();
        axes.add(pstyle.xAxisStyle());
        axes.add(pstyle.yAxisStyle());        
        for (IAxisStyle axisStyle : axes) {        
            axisStyle.labelStyle().setBold(true);
            axisStyle.labelStyle().setFont("Times New Roman");
            axisStyle.labelStyle().setFontSize(20);
            axisStyle.labelStyle().setColor("black");
            axisStyle.lineStyle().setColor("black");
            axisStyle.lineStyle().setThickness(2);        
            axisStyle.tickLabelStyle().setColor("black");
            axisStyle.tickLabelStyle().setFont("Times New Roman");
            axisStyle.tickLabelStyle().setBold(true);
            axisStyle.tickLabelStyle().setFontSize(10);
        }
        
        // background color
        pstyle.regionBoxStyle().backgroundStyle().setColor("white");
        
        // Plot histograms into regions
        plotter.region(0).plot(c2d);       
        
        // Show time
        plotter.show();
        Thread.sleep(100000); // Yeah, I know.
    }
}
