package hep.aida.jfree.converter;


/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class LineType
{
    public static final LineType SOLID = new LineType(null);
    public static final LineType DOTTED = new LineType(new float[] {1, 4});
    public static final LineType DASHED = new LineType(new float[] {10, 10});
    public static final LineType DOTDASH = new LineType(new float[] {10, 5, 2, 5});

    private final float[] dashArray;

    private LineType(float[] dashArray)
    {
        this.dashArray = dashArray;
    }

    public final float[] getDashArray()
    {
        return dashArray;
    }

    public static LineType getLineType(String lineType)
    {
        if (lineType == null || lineType.toLowerCase().equals("solid") || lineType.equals("0"))
            return SOLID;
        if (lineType.toLowerCase().equals("dotted") || lineType.equals("1"))
            return DOTTED;
        if (lineType.toLowerCase().equals("dashed") || lineType.equals("2"))
            return DASHED;
        if (lineType.toLowerCase().equals("dotdash") || lineType.equals("3"))
            return DOTDASH;
        else
            return SOLID;
    }
}
