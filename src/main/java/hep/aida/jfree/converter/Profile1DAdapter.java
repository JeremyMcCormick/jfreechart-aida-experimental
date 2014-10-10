package hep.aida.jfree.converter;

import hep.aida.IAnnotation;
import hep.aida.IAxis;
import hep.aida.IHistogram1D;
import hep.aida.IProfile1D;

/**
 * This is a read-only adapter to implement the IHistogram1D interface using a
 * backing IProfile1D.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 * @version $Id: $
 */
public class Profile1DAdapter implements IHistogram1D {

    IProfile1D p1D = null;

    Profile1DAdapter(IProfile1D profile) {
        this.p1D = profile;
    }

    public int allEntries() {
        return p1D.allEntries();
    }

    public double equivalentBinEntries() {
        return p1D.entries();
    }

    public int extraEntries() {
        return p1D.extraEntries();
    }

    public double maxBinHeight() {
        return p1D.maxBinHeight();
    }

    public double minBinHeight() {
        return p1D.maxBinHeight();
    }

    public void scale(double arg0) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method not supported.");
    }

    public double sumAllBinHeights() {
        return p1D.sumAllBinHeights();
    }

    public double sumBinHeights() {
        return p1D.sumBinHeights();
    }

    public double sumExtraBinHeights() {
        return p1D.sumExtraBinHeights();
    }

    public IAnnotation annotation() {
        return p1D.annotation();
    }

    public int dimension() {
        return p1D.dimension();
    }

    public int entries() {
        return p1D.entries();
    }

    public int nanEntries() {
        return p1D.nanEntries();
    }

    public void reset() throws RuntimeException {
        p1D.reset();
    }

    public void setTitle(String title) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method not supported."); 
    }

    public String title() {
        return p1D.title();
    }

    public void add(IHistogram1D arg0) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method not supported.");
    }

    public IAxis axis() {
        return p1D.axis();
    }

    public int binEntries(int arg0) throws IllegalArgumentException {
        return p1D.binEntries(arg0);
    }

    public double binError(int arg0) throws IllegalArgumentException {
        return p1D.binError(arg0);
    }

    public double binHeight(int arg0) throws IllegalArgumentException {
        return p1D.binHeight(arg0);
    }

    public double binMean(int arg0) throws IllegalArgumentException {
        return p1D.binMean(arg0);
    }

    public int coordToIndex(double arg0) {
        return p1D.coordToIndex(arg0);
    }

    public void fill(double arg0) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method not supported.");
    }

    public void fill(double arg0, double arg1) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method not supported.");
    }

    public double mean() {
        return p1D.mean();
    }

    public double rms() {
        return p1D.rms();
    }
}