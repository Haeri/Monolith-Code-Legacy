package Code.Components.Swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ScrollBarPlus extends BasicScrollBarUI  {

    private Image imageThumb, imageTrack;
    private JButton b = new JButton() {
		private static final long serialVersionUID = 1L;

		@Override
        public Dimension getPreferredSize() {
            return new Dimension(0, 0);
        }

    };

    public ScrollBarPlus(Color bg) {
    	Color thumb;
    	
    	float luminance = (bg.getRed() * 0.2126f + bg.getGreen() * 0.7152f + bg.getBlue() * 0.0722f) / 255;

		if (luminance >= 0.5f) {
			thumb =  bg.darker();
		} else {
			thumb =  bg.brighter();
		}
		
		imageThumb = FauxImage.create(16, 16, thumb);
	    imageTrack = FauxImage.create(16, 16, bg);
     }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        ((Graphics2D) g).drawImage(imageThumb,
            r.x, r.y, r.width, r.height, null);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        ((Graphics2D) g).drawImage(imageTrack,
            r.x, r.y, r.width, r.height, null);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return b;
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return b;
    }
    
    private static class FauxImage {

        static public Image create(int w, int h, Color c) {
            BufferedImage bi = new BufferedImage(
                w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bi.createGraphics();
            g2d.setPaint(c);
            g2d.fillRect(0, 0, w, h);
            g2d.dispose();
            return bi;
        }
    }
}