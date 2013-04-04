package hep.aida.jfree.test.interactive;

import hep.aida.jfree.test.Histogram1DStyleTest;


/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class InteractiveHistogram1DStyleTest extends Histogram1DStyleTest {
    public void test() {
        setBatchMode(false);
        plot();
        mode();
    }

}
