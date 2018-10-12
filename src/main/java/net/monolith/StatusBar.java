package net.monolith;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

public class StatusBar extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private final Color CONFIRM = new Color(0, 224, 15);
	private final Color WARN = new Color(255, 152, 0);
	private final Color ERROR = new Color(249, 54, 47);
	private final Color PROCEDE = new Color(9, 67, 224);
	
	int width;
	int height;
	boolean bLoad, bFlash;
	Timer timer;
	Color bgColor, currentColor;
	int xPosLoad, alpha;
	float cntFlash;
	float cntLoad;
	int barLength;
	float speed = 0.5f;

	long last_time;
	float delta_time;

	public StatusBar() {
		super();
		width = getWidth();
		height = getHeight();
		xPosLoad = 0;
		alpha = 0;
		currentColor = new Color(0, 0, 0, 0);

		timer = new Timer(15, this);

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent event) {
				width = getWidth();
				height = getHeight();
			}
		});
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// loading
		g.setColor(PROCEDE);

		// g.fillRect(width * y / 900, 0, barLength, height);
		g.fillRect(xPosLoad, 0, barLength, height);

		// Color Flash
		g.setColor(new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), alpha));
		g.fillRect(0, 0, width, height);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// step increase for all active components
		boolean toggle = false;
		// Flash Color
		if (bFlash) {
			if (cntFlash < 1) {
				cntFlash += 0.01f;
				alpha = lerp(cntFlash, 255, true);
			} else {
				bFlash = false;
				cntFlash = 0;
				alpha = 0;
			}
			toggle = true;
		}

		// Show loading bar
		if (bLoad) {

			long time = System.nanoTime();
			delta_time = ((time - last_time) / 10000000.0f);
			last_time = time;

			if (cntLoad < 100) {
				cntLoad += (speed * delta_time);
				barLength = (int) ((-16.0f * Math.pow((cntLoad / 100) - 0.5, 4.0f) + 1.0f) * (width/6.0f));
				xPosLoad = doubleRamp(cntLoad / 100.0f, barLength);
			} else {
				cntLoad = 0;
				xPosLoad = 0;
			}
			toggle = true;
		}

		repaint();
		if (!toggle) {
			timer.stop();
		}
	}

	private void startTimer() {
		if (!timer.isRunning()) {
			timer.start();
			last_time = System.nanoTime();
		}
	}

	public void setBG(Color bg) {
		bgColor = bg;
		setBackground(bgColor);
		repaint();
	}

	private void refresh() {
		bFlash = true;
		alpha = 255;
		cntFlash = 0;
	}

	// Green flash
	public void confirm() {
		// set values
		refresh();
		currentColor = CONFIRM;

		startTimer();
	}

	// Yellow flash
	public void warning() {
		// restart values
		refresh();
		currentColor = WARN;

		startTimer();
	}

	// Red Flash
	public void error() {
		// restart values
		refresh();
		currentColor = ERROR;

		startTimer();
	}

	// Blue load
	public void loadStart() {
		// restart values
		bLoad = true;
		xPosLoad = 0;
		cntLoad = 0;

		startTimer();
	}

	public void loadEnd() {
		bLoad = false;
		xPosLoad = -barLength;
	}

	private int lerp(float progress, int max, boolean inverse) {
		float x = progress;

		float x2 = (float) Math.pow(x, 4);
		float g = x + (1 - x);
		float y = (float) ((float) x2 / (float) (Math.pow(g, 4)));

		y = Math.min(y, 1);
		y = Math.max(y, 0);

		int res = (int) (y * max);

		if (inverse) {
			res = max - res;
		}
		return res;
	}

	
	
	private float easeInQuad(float t) {
		return t*t; 
	}
	
	private float easeOutQuad(float t) {
		return t*(2-t); 
	}
	
	private int doubleRamp(float t, int offset) {
		float lerp = (float) (t < 0.5f ? easeOutQuad(2f * t) * 0.5f : easeInQuad((2.0f * (t - 0.5f))) * 0.5 + 0.5f) ;
		return (int) ((width - offset) * lerp);
	}
}