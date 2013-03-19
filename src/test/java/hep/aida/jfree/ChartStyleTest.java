package hep.aida.jfree;

import java.io.IOException;
import java.util.Random;

import hep.aida.IAnalysisFactory;
import hep.aida.IBoxStyle;
import hep.aida.IFillStyle;
import hep.aida.IHistogram1D;
import hep.aida.IHistogramFactory;
import hep.aida.IPlotter;
import hep.aida.IPlotterFactory;
import hep.aida.IPlotterStyle;
import junit.framework.TestCase;

public class ChartStyleTest extends TestCase {
    
    IAnalysisFactory af;
    IPlotterFactory pf;
    IHistogramFactory hf;
    

    protected void setUp() {
        AnalysisFactory.register();
        af = IAnalysisFactory.create();
        pf = af.createPlotterFactory();
        hf = af.createHistogramFactory(null);
    }
    
    // Create a 1D histogram with random Gaussian distribution
    private final IHistogram1D histogram1D() {
        IHistogram1D h1d = hf.createHistogram1D("h1d", 50, 0, 50.0);
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            h1d.fill(rand.nextInt(50));
        }
        return h1d;
    }
    
    public void test() {
        IHistogram1D hist = histogram1D();
        
        IPlotter plotter = pf.create();
        IPlotterStyle style = plotter.style();
        
        style.gridStyle().setVisible(false);
        
        // Data box style.
        IBoxStyle dataBoxStyle = style.dataBoxStyle();
        IFillStyle fillStyle = dataBoxStyle.backgroundStyle();
        fillStyle.setColor("gray");
        
        // Region box style.
        IBoxStyle regionBoxStyle = style.regionBoxStyle();
        IFillStyle regionFillStyle = regionBoxStyle.backgroundStyle();
        regionFillStyle.setColor("pink");
        regionBoxStyle.borderStyle().setBorderType("bevelIn");
        
        // Create regions.
        plotter.createRegions(3, 2);
        
        // Plot 1 with bevelIn border.
        plotter.region(0).plot(hist);
        
        // Plot 2 with bevelOut border.
        regionBoxStyle.borderStyle().setBorderType("bevelOut");
        plotter.region(1).plot(hist);
        
        regionBoxStyle.borderStyle().setBorderType("etched");
        plotter.region(2).plot(hist);
        
        regionBoxStyle.borderStyle().setBorderType("line");
        plotter.region(3).plot(hist);
        
        regionBoxStyle.borderStyle().setBorderType("shadow");
        plotter.region(4).plot(hist);
        
        regionBoxStyle.borderStyle().setBorderType(null);
        plotter.region(5).plot(hist);
        
        //try {
        //    plotter.writeToFile(this.getClass().getSimpleName() + ".png");
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
        //}
        
        plotter.show();
        
        //dataBoxStyle.setBackgroundStyle(arg0);
        //dataBoxStyle.setBorderStyle(arg0);
        //dataBoxStyle.setForegroundStyle(arg0);
        //dataBoxStyle.setHeight(arg0);
        //dataBoxStyle.setWidth(arg0);
        //dataBoxStyle.setVisible(arg0);
        //dataBoxStyle.setX(arg0);
        //dataBoxStyle.setY(arg0);
    }

    public void tearDown() {
        System.out.println("Hit Ctrl + C to exit.");
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}