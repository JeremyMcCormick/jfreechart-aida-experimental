package hep.aida.jfree.test.interactive;

import hep.aida.jfree.test.Histogram1DTest;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class InteractiveHistogram1DTest extends Histogram1DTest {
    
    public void test() {        
        setBatchMode(false);
        plot();
        mode();
    }
}

