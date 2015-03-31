package hep.aida.jfree.test.interactive;

import hep.aida.jfree.test.Histogram2DOverlayTest;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class InteractiveHistogram2DOverlayTest extends Histogram2DOverlayTest {
    
    public void test() {
        setBatchMode(false);
        setWaitTime(1000000);
        super.test();
        mode();
    }

}