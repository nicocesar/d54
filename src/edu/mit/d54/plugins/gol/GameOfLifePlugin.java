package edu.mit.d54.plugins.gol;

import edu.mit.d54.Display2D;
import edu.mit.d54.DisplayPlugin;

public class GameOfLifePlugin extends DisplayPlugin {

	protected int width = 9;
	protected int height = 17;
	protected boolean life[][];
	protected boolean oldLife[][];

	public GameOfLifePlugin(Display2D display, double framerate) {
		super(display, framerate);
		this.life = new boolean[this.width][this.height];
		this.oldLife = life;
	}

	protected void golRender() {
		Display2D display = getDisplay();
		for (int ix = 0; ix < this.width; ++ix) {
			for (int iy = 0; iy < this.height; ++iy) {
				boolean alive = life[ix][iy];
				int rgb = 1200394;

				if (alive) {
					display.setPixelRGB(ix, iy, rgb);
				} else {
					display.setPixelRGB(ix, iy, 0);
				}
			}
		}

	}

	protected boolean isAlive(boolean[][] life, int x, int y) {
		boolean current = life[x][y];
		int neighborCount = 0;

		/*  Up (y - 1)    */
		if (y > 0 && life[x][y - 1]) {
			neighborCount += 1;
		}

		/*  Down (y + 1)  */
		if (y < (this.height - 1) && life[x][y + 1]) {
			neighborCount += 1;
		}
		
		/*  Left (x - 1)  */
		if (x > 0 && life[x - 1][y]) {
			neighborCount += 1;
		}
		
		/*  Right (x + 1) */
		if (x < (this.width - 1) && life[x + 1][y]) {
			neighborCount += 1;
		}

		if (current && neighborCount < 2) {
			return false; /* Forever alone */
		} else if (current && neighborCount > 3) {
			return false; /* Overcrowding */
		} else if (current) {
			return true; /* Nice! */
		} else if (!current && neighborCount == 3) {
			return true;
		}
		
		return current;
	}

	protected void doGameOfLife() {
		boolean[][] life = this.life;
		boolean next[][] = new boolean[this.width][this.height];

		for (int ix = 0; ix < this.width; ++ix) {
			for (int iy = 0; iy < this.height; ++iy) {
				next[ix][iy] = this.isAlive(life, ix, iy);
			}
		}
		
		this.oldLife = this.life;
		this.life = next;
	}

	
	protected void messItUp() {
		for (int ix = 0; ix < this.width; ++ix) {
			for (int iy = 0; iy < this.height; ++iy) {
				this.life[ix][iy] = (Math.random() > 0.5);
			}
		}
	}
	
	protected void contaminate() {
		for (int ix = 0; ix < this.width; ++ix) {
			for (int iy = 0; iy < this.height; ++iy) {
				if (this.life[ix][iy] != this.oldLife[ix][iy]) {
					return;
				}
			}
		}
		this.messItUp();
	}
	
	@Override
	public void loop() {
		this.contaminate();
		doGameOfLife();
		this.golRender();
	}
}
