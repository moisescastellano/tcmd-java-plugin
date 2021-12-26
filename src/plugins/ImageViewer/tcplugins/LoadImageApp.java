/*
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */
package tcplugins;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * This class demonstrates how to load an Image from an external file
 */
public class LoadImageApp extends Component {

	BufferedImage img;

	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}

	public LoadImageApp(String filename) {
		try {
			ImageIO.setUseCache(false);
			img = ImageIO.read(new FileImageInputStream(new File(filename)));
		} catch (IOException e) {
		}

	}

	public Dimension getPreferredSize() {
		if (img == null) {
			return new Dimension(100, 100);
		} else {
			return new Dimension(img.getWidth(null), img.getHeight(null));
		}
	}

	public static void main(String[] args) {

		JFrame f = new JFrame("Load Image Sample");

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		f.setLayout(new GridLayout(2,2));
		LoadImageApp app = new LoadImageApp(args[0]);
		f.add(new JScrollPane(app));

		app = new LoadImageApp(args[0]);
		f.add(new JScrollPane(app));

		f.pack();
		f.setVisible(true);
	}
	
	@Override
	protected void finalize() throws Throwable {
		img.flush();
		super.finalize();
	}
}
