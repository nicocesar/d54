package edu.mit.d54.plugins.gnu30;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import edu.mit.d54.Display2D;
import edu.mit.d54.DisplayPlugin;

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
	private GNU30Updater updater;
	private Thread updaterThread;
	private ArrayList<BufferedImage> frames;

	private int heightMax = 17;
	private int widthMax = 9;


	public GNU30Plugin(Display2D display, double framerate) {
		super(display, framerate);
		this.lastUpdateTime = 0;

		InputStream stream = GNU30Plugin.class.getResourceAsStream("/images/gnu30/GNU-Animation-1.gif");
		this.setGifObject(stream);

		this.updater = new GNU30Updater(this);
		this.updaterThread = new Thread(this.updater);
		this.updaterThread.start();
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
		return br;
	}

	public void setGifObject(InputStream is) {
		synchronized(this) {
			try {
				ArrayList<BufferedImage> localFrames = this.loadGifObject(is);
				if (localFrames.size() == 0) {
					System.out.println("The image sucks a whole lot");
				} else {
					this.frames = localFrames;
					this.frameCount = this.frames.size();
					this.currentFrame = 0;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void loop() {
		synchronized(this) {
			Display2D display = getDisplay();
			BufferedImage frame = this.frames.get(this.currentFrame);

			int width = frame.getWidth() > this.widthMax ? this.widthMax : frame.getWidth();
			int height = frame.getHeight() > this.heightMax ? this.heightMax : frame.getHeight();
			
			for (int ix = 0; ix < width; ++ix) {
				for (int iy = 0; iy < height; ++iy) {					
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
}
