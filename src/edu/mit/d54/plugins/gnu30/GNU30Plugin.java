package edu.mit.d54.plugins.gnu30;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import edu.mit.d54.Display2D;
import edu.mit.d54.DisplayPlugin;
import edu.mit.d54.plugins.erl30.Erl30Plugin;

/**
 * This plugin was developed by a member of GNU strikeforce delta.
 * under the BSD-3 license that the rest of the d54 project is under.
 *
 *  Contributors:
 *
 *    paultag@mit.edu
 *    nico@incocesar.com
 */
public class GNU30Plugin extends DisplayPlugin {

	int currentFrame;
	private long lastUpdateTime;
	private double secondDelay = 0.1;
	private int frameCount;
	

	public GNU30Plugin(Display2D display, double framerate) {
		super(display, framerate);
		this.currentFrame = 0;
		this.lastUpdateTime = 0;
		// this.secondDelay = 3;
	}

	public ArrayList<BufferedImage> loadGifObject(InputStream is) throws IOException {
		ImageInputStream image = ImageIO.createImageInputStream(is);
		ImageReader ir = (ImageReader) ImageIO.getImageReadersBySuffix("gif").next();
		ir.setInput(image, false);
		int frames = ir.getNumImages(true);
		ArrayList<BufferedImage> br = new ArrayList<BufferedImage>();
		for (int i = 0; i < frames; ++i) {
			BufferedImage frame = ir.read(i);
			br.add(frame);
		}
		this.frameCount = br.size();
		return br;
	}

	@Override
	protected void loop() {
		Display2D display = getDisplay();
		// Graphics2D g = display.getGraphics();

		// InputStream stream = GNU30Plugin.class.getResourceAsStream("/images/gnu30/gnu30.gif");
		InputStream stream = GNU30Plugin.class.getResourceAsStream("/images/gnu30/trippy3.gif");
		ArrayList<BufferedImage> frames = null;
		
		try {
			frames = this.loadGifObject(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedImage frame = frames.get(this.currentFrame);

		for (int ix = 0; ix < frame.getWidth(); ++ix) {
			for (int iy = 0; iy < frame.getHeight(); ++iy) {					
				int pixel = frame.getRGB(ix, iy);

				int red = (pixel >> 16) & 0xFF;
				int green = (pixel >> 8) & 0xFF;
				int blue = pixel & 0xFF;

				display.setPixelRGB(ix, iy, red, green, blue);
			}
		}
		
		if (this.lastUpdateTime + (this.secondDelay * 1000) < System.currentTimeMillis()) {
			this.currentFrame = (this.currentFrame + 1) % this.frameCount;
			this.lastUpdateTime = System.currentTimeMillis();
		}
	}
}
