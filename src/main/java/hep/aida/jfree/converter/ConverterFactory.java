package hep.aida.jfree.converter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class ConverterFactory {

    List<Converter<?>> converters = new ArrayList<Converter<?>>();

    static private ConverterFactory instance = null;

    public final static ConverterFactory instance() {
        if (instance == null)
            instance = new ConverterFactory();
        return instance;
    }

    private ConverterFactory() {
        converters.add(new Histogram1DConverter());
        converters.add(new Histogram2DConverter());
        converters.add(new Cloud1DConverter());
        converters.add(new Cloud2DConverter());
        converters.add(new Profile1DConverter());
        converters.add(new DataPointSetConverter());
    }

    public Converter getConverter(Object object) {
        for (Converter cnv : converters) {
            if (cnv.convertsType().isInstance(object)) {
                return cnv;
            }
        }
        throw new RuntimeException("No converter found for object with type: " + object.getClass().getCanonicalName());
    }

    public <T> Converter<T> getConverter(Class<T> type) {
        for (Converter<?> cnv : converters) {
            if (cnv.convertsType().isAssignableFrom(type)) {
                return (Converter<T>)cnv;
            }
        }
        throw new RuntimeException("No converter found for type: " + type.getCanonicalName());
    }         
}