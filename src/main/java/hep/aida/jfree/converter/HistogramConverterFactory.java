package hep.aida.jfree.converter;

import hep.aida.IBaseHistogram;

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
public class HistogramConverterFactory {

    List<HistogramConverter> converters = new ArrayList<HistogramConverter>();

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

    public HistogramConverter getConverter(IBaseHistogram hist) {
        for (HistogramConverter cnv : converters) {
            if (cnv.convertsType().isInstance(hist)) {
                return cnv;
            }
        }
        return null;
    }
}