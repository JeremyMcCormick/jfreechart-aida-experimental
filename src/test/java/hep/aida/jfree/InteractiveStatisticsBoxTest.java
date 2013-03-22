package hep.aida.jfree;


/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class InteractiveStatisticsBoxTest extends StatisticsBoxTest {
    public void test() {
        setBatchMode(false);
        plot();
        mode();
    }
}
