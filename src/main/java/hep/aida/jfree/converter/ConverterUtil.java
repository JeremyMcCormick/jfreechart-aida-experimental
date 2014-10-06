package hep.aida.jfree.converter;

import hep.aida.IBaseHistogram;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class ConverterUtil {
    
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
}
