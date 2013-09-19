package edu.mit.d54.plugins.gnu30;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


/**
 * This plugin support code was developed by a member of GNU strikeforce delta.
 * under the BSD-3 license that the rest of the d54 project is under.
 *
 *  Contributors:
 *
 *    paultag@mit.edu
 */
public class GNU30Updater implements Runnable {
	private GNU30Plugin plugin;
	private boolean alive = false;
	private int heartbeat = 10000;
	private String remoteIndex = "http://public.pault.ag/gnu/current.gif";

	public GNU30Updater(GNU30Plugin plugin) {
		this.plugin = plugin;
	}

	public long wget(String where, String path) throws IOException {
		URL website = new URL(where);
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream(path);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
		
		File f = new File(path);
		return f.length();
	}

	public long update(String path) {
		try {
			return this.wget(this.remoteIndex, path);
		} catch (IOException e1) {
			System.out.println("Network or filesystem did something bad.");
			System.out.println(e1.getLocalizedMessage());
			return -1;	
		}
	}

	@Override
	public void run() {
		this.alive = true;
		long lastFile = 0;

		while (this.alive) {
			String path = "current.gif";
			long thisFile = this.update(path);
			
			if (thisFile < 0) {
				System.out.println("  Something bad happened. Not updating graphics.");
			} else if (thisFile != lastFile) {
				lastFile = thisFile;
				System.out.println("  Graphics switch!.");
				
				synchronized(this.plugin) {
					InputStream stream;
					try {
						stream = new FileInputStream(path);
						this.plugin.setGifObject(stream);
					} catch (FileNotFoundException e) {
						System.out.println("Filesystem really screwed us here.");
					}
				}
			} else {
				System.out.println("  No graphics update.");
			}
			
			try {
				Thread.sleep(this.heartbeat);
			} catch (InterruptedException e) {}


		
		}
	}
}
