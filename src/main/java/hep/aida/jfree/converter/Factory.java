package hep.aida.jfree.converter;

import hep.aida.IBaseHistogram;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Factory
{
    List<Histogram> converters = new ArrayList<Histogram>();

    static private Factory instance = null;
        
    public final static Factory instance()
    {
        if (instance == null)
            instance = new Factory();
        return instance;
    }

    private Factory()
    {
        converters.add(new Histogram1D());
        converters.add(new Histogram2D());
        converters.add(new Cloud1D());
        converters.add(new Cloud2D());
    }

    public Histogram getConverter(IBaseHistogram hist)
    {
        for (Histogram cnv : converters) {
            if (cnv.convertsType().isInstance(hist)) {
                return cnv;
            }
        }
        return null;
    }
}