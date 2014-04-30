package hep.aida.jfree.test.interactive;

import hep.aida.jfree.test.StatisticsBoxTest;


/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class InteractiveStatisticsBoxTest extends StatisticsBoxTest {
    
    public void testInteractiveStatisticsBox() {
        setBatchMode(false);
        super.testStatisticsBox();
        mode();
    }
    
}
