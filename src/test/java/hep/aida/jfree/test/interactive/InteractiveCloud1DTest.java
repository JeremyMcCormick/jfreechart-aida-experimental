package hep.aida.jfree.test.interactive;

import hep.aida.jfree.test.Cloud1DTest;

public class InteractiveCloud1DTest extends Cloud1DTest {   

    public void test() {
        setBatchMode(false);
        plot();
        mode();
    }
    
}