package hep.aida.jfree.converter;

import hep.aida.IBaseHistogram;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class HistogramConverterFactory
{
    List<HistogramConverter> converters = new ArrayList<HistogramConverter>();

    static private HistogramConverterFactory instance = null;
        
    public final static HistogramConverterFactory instance()
    {
        if (instance == null)
            instance = new HistogramConverterFactory();
        return instance;
    }

    private HistogramConverterFactory()
    {
        converters.add(new Histogram1DConverter());
        converters.add(new Histogram2DConverter());
        converters.add(new Cloud1DConverter());
        converters.add(new Cloud2DConverter());
    }

    public HistogramConverter getConverter(IBaseHistogram hist)
    {
        for (HistogramConverter cnv : converters) {
            if (cnv.convertsType().isInstance(hist)) {
                return cnv;
            }
        }
        return null;
    }
}