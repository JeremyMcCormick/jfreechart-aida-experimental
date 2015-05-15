package hep.aida.jfree.plotter;

import javax.swing.JFrame;

/**
 * This is an <code>IPlotter</code> implementation which will show plots in a <code>JFrame</code>.
 * 
 * @author Jeremy McCormick <jeremym@slac.stanford.edu>
 */
public class StandalonePlotter extends Plotter {

    private JFrame frame;

    /**
     * Class constructor.
     */
    StandalonePlotter() {
        super();
    }

    /**
     * Show the regions of this plotter in a standalone JFrame.
     */
    public void show() {
        super.show();
        if (this.frame == null) {
            createFrame();
        }
        if (this.title() != null) {
            frame.setTitle(title());
        }
        if (!frame.isVisible()) {
            frame.pack();
            frame.setVisible(true);
        }
    }

    /**
     * This will hide the plotter frame.
     */
    public void hide() {
        super.hide();
        if (frame != null) {
            frame.setVisible(false);
        }
    }

    /**
     * Create the <tt>JFrame</tt> for the plotter. This will only have an effect if the plotter is not embedded.
     */
    private void createFrame() {
        frame = new JFrame();
        frame.setContentPane(panel());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
