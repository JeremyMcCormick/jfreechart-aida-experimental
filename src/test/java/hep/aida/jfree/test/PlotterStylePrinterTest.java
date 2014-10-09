package hep.aida.jfree.test;

import hep.aida.jfree.plotter.style.util.PlotterStylePrinter;
import hep.aida.jfree.test.AbstractPlotTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class PlotterStylePrinterTest extends AbstractPlotTest {
    
    public void test() {
        PrintStream ps = null;
        File outputFile = new File("target/" + this.getClass().getSimpleName() + ".txt");
        try {
            ps = new PrintStream(outputFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        PlotterStylePrinter printer = new PlotterStylePrinter(style, ps);
        printer.print();
        System.out.println("Wrote test output to " + outputFile.getPath());
    }
}
