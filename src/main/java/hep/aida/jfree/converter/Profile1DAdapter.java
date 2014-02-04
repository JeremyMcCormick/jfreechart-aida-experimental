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

    IProfile1D profile = null;

    Profile1DAdapter(IProfile1D profile) {
        this.profile = profile;
    }

    public int allEntries() {
        return profile.allEntries();
    }

    public double equivalentBinEntries() {
        return profile.entries();
    }

    public int extraEntries() {
        return profile.extraEntries();
    }

    public double maxBinHeight() {
        return profile.maxBinHeight();
    }

    public double minBinHeight() {
        return profile.maxBinHeight();
    }

    public void scale(double arg0) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method not supported.");
    }

    public double sumAllBinHeights() {
        return profile.sumAllBinHeights();
    }

    public double sumBinHeights() {
        return profile.sumBinHeights();
    }

    public double sumExtraBinHeights() {
        return profile.sumExtraBinHeights();
    }

    public IAnnotation annotation() {
        return profile.annotation();
    }

    public int dimension() {
        return profile.dimension();
    }

    public int entries() {
        return profile.entries();
    }

    public int nanEntries() {
        return profile.nanEntries();
    }

    public void reset() throws RuntimeException {
        profile.reset();
    }

    public void setTitle(String arg0) throws IllegalArgumentException {
        profile.setTitle(arg0);
    }

    public String title() {
        return profile.title();
    }

    public void add(IHistogram1D arg0) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method not supported.");
    }

    public IAxis axis() {
        return profile.axis();
    }

    public int binEntries(int arg0) throws IllegalArgumentException {
        return profile.binEntries(arg0);
    }

    public double binError(int arg0) throws IllegalArgumentException {
        return profile.binError(arg0);
    }

    public double binHeight(int arg0) throws IllegalArgumentException {
        return profile.binHeight(arg0);
    }

    public double binMean(int arg0) throws IllegalArgumentException {
        return profile.binMean(arg0);
    }

    public int coordToIndex(double arg0) {
        return profile.coordToIndex(arg0);
    }

    public void fill(double arg0) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method not supported.");
    }

    public void fill(double arg0, double arg1) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method not supported.");
    }

    public double mean() {
        return profile.mean();
    }

    public double rms() {
        return profile.rms();
    }
}