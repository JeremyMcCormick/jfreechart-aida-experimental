package hep.aida.jfree;

import hep.aida.IPlotter;

/**
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
final class PlotterFactory extends hep.aida.ref.plotter.PlotterFactory
{

    String name;

    PlotterFactory()
    {
        super();
    }

    PlotterFactory(String name)
    {
        super();
        this.name = name;
    }

    public IPlotter create(String plotterName)
    {
        return new Plotter();
    }

    public IPlotter create()
    {
        return create((String) null);
    }
}
