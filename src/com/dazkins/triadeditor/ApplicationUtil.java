package com.dazkins.triadeditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.dazkins.fileencryptor.FileEncrypter;
import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.WorldInfo;

public class ApplicationUtil {
	public static void requestWorldSave(final World w) throws FileNotFoundException {
		final JFrame frame = new JFrame();
		frame.setLocation(0, 0);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLayout(null);
		frame.pack();

		Dimension d = new Dimension(400, 400);

		frame.setPreferredSize(d);
		frame.setMaximumSize(d);
		frame.setMinimumSize(d);
		
		JFileChooser jfc = new JFileChooser();
		int r = jfc.showSaveDialog(frame);
		if (r == JFileChooser.APPROVE_OPTION) {
			File f = jfc.getSelectedFile();
			String contents = "";
			
			WorldInfo wi = w.getInfo();
			
			for (int x = 0; x < wi.nChunksX * Chunk.chunkW; x++) {
				for (int y = 0; y < wi.nChunksY * Chunk.chunkH; y++) {
					contents += w.getTile(x, y).getID() + " ";
				}
			}
			
			contents = FileEncrypter.encryptString(contents);
			
			PrintWriter pr = new PrintWriter(f);
			pr.write(contents);
			pr.close();
			
			f = null;
			contents = null;
			pr = null;
			
			System.gc();
		}
	}
}