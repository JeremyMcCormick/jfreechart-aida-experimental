package hep.aida.jfree.test.interactive;

import hep.aida.jfree.test.ColorMapTest;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class InteractiveColorMapTest extends ColorMapTest {

    public void test() {
        setBatchMode(false);
        plot();
        mode();
    }
}
