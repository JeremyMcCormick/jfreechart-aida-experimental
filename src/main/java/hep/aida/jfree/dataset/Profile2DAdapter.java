package hep.aida.jfree.dataset;

import hep.aida.IAnnotation;
import hep.aida.IAxis;
import hep.aida.IHistogram2D;
import hep.aida.IProfile2D;
import hep.aida.ref.event.IsObservable;
import hep.aida.ref.histogram.Histogram2D;

/**
 * This is a read only adapter to adapt an <code>IProfile2D</code>
 * to the <code>IHistogram2D</code> interface for display in the plotter.
 * If the backing object needs to be updated, the user should call
 * directly the <code>IProfile2D</code> methods. 
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class Profile2DAdapter extends Histogram2D implements IHistogram2D, IsObservable {

    IProfile2D p2D;
    
    public Profile2DAdapter(IProfile2D p2D) {
        this.p2D = p2D;
    }
    
    @Override
    public int allEntries() {
        return p2D.allEntries();
    }

    @Override
    public double equivalentBinEntries() {        
        throw new UnsupportedOperationException("Method is not implemented.");
    }

    @Override
    public int extraEntries() {
        return p2D.extraEntries();
    }

    @Override
    public double maxBinHeight() {
        return p2D.maxBinHeight();
    }

    @Override
    public double minBinHeight() {
        return p2D.minBinHeight();
    }

    @Override
    public void scale(double arg0) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method is not implemented."); 
    }

    @Override
    public double sumAllBinHeights() {
        return p2D.sumAllBinHeights();
    }

    @Override
    public double sumBinHeights() {
        return p2D.sumBinHeights();
    }

    @Override
    public double sumExtraBinHeights() {
        return p2D.sumExtraBinHeights();
    }

    @Override
    public IAnnotation annotation() {
        return p2D.annotation();
    }

    @Override
    public int dimension() {
        return p2D.dimension();
    }

    @Override
    public int entries() {
        return p2D.entries();
    }

    @Override
    public int nanEntries() {
        return p2D.nanEntries();
    }

    @Override
    public void reset() throws RuntimeException {
        p2D.reset();        
    }

    @Override
    public void setTitle(String title) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method is not implemented.");
    }

    @Override
    public String title() {
        return p2D.title();
    }

    @Override
    public void add(IHistogram2D histogram) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method is not implemented.");
    }

    @Override
    public int binEntries(int x, int y) throws IllegalArgumentException {
        return p2D.binEntries(x, y);
    }

    @Override
    public int binEntriesX(int x) throws IllegalArgumentException {
        return p2D.binEntriesX(x);
    }

    @Override
    public int binEntriesY(int y) throws IllegalArgumentException {
        return p2D.binEntriesY(y);
    }

    @Override
    public double binError(int x, int y) throws IllegalArgumentException {
        return p2D.binError(x, y);
    }

    @Override
    public double binHeight(int x, int y) throws IllegalArgumentException {
        return p2D.binHeight(x, y);
    }

    @Override
    public double binHeightX(int x) throws IllegalArgumentException {
        return p2D.binHeightX(x);
    }

    @Override
    public double binHeightY(int y) throws IllegalArgumentException {
        return p2D.binHeightY(y);
    }

    @Override
    public double binMeanX(int x, int y) throws IllegalArgumentException {        
        return p2D.binMeanX(x, y);
    }

    @Override
    public double binMeanY(int x, int y) throws IllegalArgumentException {
        return p2D.binMeanY(x, y);
    }

    @Override
    public int coordToIndexX(double coord) {
        return p2D.coordToIndexX(coord);
    }

    @Override
    public int coordToIndexY(double coord) {
        return p2D.coordToIndexY(coord);
    }

    @Override
    public void fill(double x, double y) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method is not implemented.");         
    }

    @Override
    public void fill(double x, double y, double weight) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Method is not implemented.");          
    }

    @Override
    public double meanX() {
        return p2D.meanX();
    }

    @Override
    public double meanY() {
        return p2D.meanY();
    }

    @Override
    public double rmsX() {
        return p2D.rmsX();
    }

    @Override
    public double rmsY() {
        return p2D.rmsY();
    }

    @Override
    public IAxis xAxis() {
        return p2D.xAxis();
    }

    @Override
    public IAxis yAxis() {
        return p2D.yAxis();
    }       
}
