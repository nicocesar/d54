package edu.mit.d54.plugins.gnu30;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import edu.mit.d54.Display2D;
import edu.mit.d54.DisplayPlugin;

/**
 * This plugin was developed by a member of GNU strikeforce delta.
 *
 *  Contributors:
 *
 *    paultag@mit.edu
 */
public class GNU30Plugin extends DisplayPlugin {

	public GNU30Plugin(Display2D display, double framerate) {
		super(display, framerate);
	}

	@Override
	protected void loop() {
		Display2D display = getDisplay();
		Graphics2D g = display.getGraphics();
	}
}
