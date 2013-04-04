package hep.aida.jfree.test.interactive;

import hep.aida.jfree.test.Histogram2DTest;


/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class InteractiveHistogram2DTest extends Histogram2DTest {
    public void test() {
        setBatchMode(false);
        plot();
        mode();
    }

}
