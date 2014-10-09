package hep.aida.jfree.plotter.style.util;

import hep.aida.IBorderStyle;
import jas.util.border.ShadowBorder;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public final class BorderUtil {

    private BorderUtil() {
    }

    public final static int OTHER = -1;
    public final static int NONE = 0;
    public final static int BEVEL_IN = 1;
    public final static int BEVEL_OUT = 2;
    public final static int ETCHED = 3;
    public final static int LINE = 4;
    public final static int SHADOW = 5;

    static Border createBorder(int type, Color color) {
        switch (type) {
        case BEVEL_IN:
            return BorderFactory.createLoweredBevelBorder();
        case BEVEL_OUT:
            return BorderFactory.createRaisedBevelBorder();
        case ETCHED:
            return BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, color, Color.black);
        case SHADOW:
            return ShadowBorder.createShadowBorder();
        case LINE:
            return BorderFactory.createLineBorder(color);
        case NONE:
        case OTHER:
        default:
            return null;
        }
    }
    
    public static Border toBorder(IBorderStyle borderStyle) {
        Border border = null;
        String borderType = borderStyle.borderType();
        if (borderType != null) {
            Color color = ColorUtil.toColor(borderStyle, Color.black);
            if (borderType == "bevelIn" || borderType == "0")
                border = createBorder(1, color);
            else if (borderType == "bevelOut" || borderType == "1")
                border = createBorder(2, color);
            else if (borderType == "etched" || borderType == "2")
                border = createBorder(3, color);
            else if (borderType == "line" || borderType == "3")
                createBorder(4, color);
            else if (borderType == "shadow" || borderType == "4")
                createBorder(5, color);
            else
                createBorder(0, color);
        }
        return border;
    }
}
