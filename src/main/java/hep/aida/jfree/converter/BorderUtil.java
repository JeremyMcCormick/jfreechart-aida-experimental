package hep.aida.jfree.converter;

import jas.util.border.ShadowBorder;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
final class BorderUtil {

    private BorderUtil() {
    }

    public final static int OTHER = -1;
    public final static int NONE = 0;
    public final static int BEVEL_IN = 1;
    public final static int BEVEL_OUT = 2;
    public final static int ETCHED = 3;
    public final static int LINE = 4;
    public final static int SHADOW = 5;

    static Border createBorder(int type) {
        switch (type) {
        case BEVEL_IN:
            return BorderFactory.createLoweredBevelBorder();
        case BEVEL_OUT:
            return BorderFactory.createRaisedBevelBorder();
        case ETCHED:
            return BorderFactory.createEtchedBorder();
        case SHADOW:
            return ShadowBorder.createShadowBorder();
        case LINE:
            return BorderFactory.createLineBorder(Color.black);
        case NONE:
        case OTHER:
        default:
            return null;
        }
    }

    /*
     * String dataAreaBorderType =
     * style.dataBoxStyle().borderStyle().borderType(); if ( dataAreaBorderType
     * != null ) { if ( dataAreaBorderType == "bevelIn" || dataAreaBorderType ==
     * "0" ) getPlot().setDataAreaBorderType(1); else if ( dataAreaBorderType ==
     * "bevelOut" || dataAreaBorderType == "1" )
     * getPlot().setDataAreaBorderType(2); else if ( dataAreaBorderType ==
     * "etched" || dataAreaBorderType == "2" )
     * getPlot().setDataAreaBorderType(3); else if ( dataAreaBorderType ==
     * "line" || dataAreaBorderType == "3" ) getPlot().setDataAreaBorderType(4);
     * else if ( dataAreaBorderType == "shadow" || dataAreaBorderType == "4" )
     * getPlot().setDataAreaBorderType(5); else
     * getPlot().setDataAreaBorderType(0); }
     */

}
