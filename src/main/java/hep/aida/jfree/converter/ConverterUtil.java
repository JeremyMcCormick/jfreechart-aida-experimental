package hep.aida.jfree.converter;

import org.jfree.chart.axis.NumberAxis;

import hep.aida.IBaseHistogram;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
class ConverterUtil {

    public static final String[] getAxisLabels(IBaseHistogram h) {
        String[] labels = new String[] { "", "" };
        // X label.
        if (h.annotation().hasKey("xAxisLabel")) {
            labels[0] = h.annotation().value("xAxisLabel");
        }
        // Y label.
        if (h.annotation().hasKey("yAxisLabel")) {
            labels[1] = h.annotation().value("yAxisLabel");
        }
        return labels;
    }
    
    // TODO: Set tick unit source here to force display of label for the
    // max value of the axis.
    // http://www.jfree.org/phpBB2/viewtopic.php?f=3&t=28597&p=79591&hilit=NumberAxis+tick#p79591

}
