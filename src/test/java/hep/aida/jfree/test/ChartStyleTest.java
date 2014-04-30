package hep.aida.jfree.test;

import hep.aida.IBoxStyle;
import hep.aida.IFillStyle;
import hep.aida.IHistogram1D;
import hep.aida.IPlotterStyle;
import hep.aida.jfree.test.AbstractPlotTest;

import java.util.Random;

public class ChartStyleTest extends AbstractPlotTest {
        
    public void test() {
         
        IHistogram1D hist = histogramFactory.createHistogram1D("h1d", 50, 0, 50.0);
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            hist.fill(rand.nextInt(50));
        }
        
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
        
        //dataBoxStyle.setBackgroundStyle(arg0);
        //dataBoxStyle.setBorderStyle(arg0);
        //dataBoxStyle.setForegroundStyle(arg0);
        //dataBoxStyle.setHeight(arg0);
        //dataBoxStyle.setWidth(arg0);
        //dataBoxStyle.setX(arg0);
        //dataBoxStyle.setY(arg0);
        //dataBoxStyle.setVisible(arg0);
        
        mode();
        
    }
}