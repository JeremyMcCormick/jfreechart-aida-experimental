package hep.aida.jfree.converter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
/*
 * TODO:
 * 
 * Still need converters for...
 * 
 * -IProfile2D 
 * -IDataPointSet (w/ dimension <= 2) 
 * -IFunction
 * 
 * Probably these will be handled with a separate package like JZY3D:
 * 
 * -ICloud3D 
 * -IHistogram3D 
 * -IDatapointSet (w/ dim > 2) 
 * -IHistogram2D (as 3D lego plot)
 */
// FIXME: This is more like ObjectConverterFactory because it should handle all AIDA objects, not just histograms.
public class HistogramConverterFactory {

    List<Converter<?>> converters = new ArrayList<Converter<?>>();

    static private HistogramConverterFactory instance = null;

    public final static HistogramConverterFactory instance() {
        if (instance == null)
            instance = new HistogramConverterFactory();
        return instance;
    }

    private HistogramConverterFactory() {
        converters.add(new Histogram1DConverter());
        converters.add(new Histogram2DConverter());
        converters.add(new Cloud1DConverter());
        converters.add(new Cloud2DConverter());
        converters.add(new Profile1DConverter());
    }

    public Converter getConverter(Object object) {
        for (Converter cnv : converters) {
            if (cnv.convertsType().isInstance(object)) {
                return cnv;
            }
        }
        return null;
    }
}