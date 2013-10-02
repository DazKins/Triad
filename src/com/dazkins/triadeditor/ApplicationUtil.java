package com.dazkins.triadeditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.dazkins.fileencryptor.FileEncrypter;
import com.dazkins.triad.game.world.Chunk;
import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.WorldInfo;

public class ApplicationUtil {
	public static void requestWorldSave(World w, JFrame frame) throws FileNotFoundException {
		JFileChooser jfc = new JFileChooser();
		int r = jfc.showSaveDialog(frame);
		if (r == JFileChooser.APPROVE_OPTION) {
			File f = jfc.getSelectedFile();
			
			WorldInfo wi = w.getInfo();
			
			StringBuilder s = new StringBuilder();
			
			for (int x = 0; x < wi.nChunksX * Chunk.chunkW; x++) {
				for (int y = 0; y < wi.nChunksY * Chunk.chunkH; y++) {
					s.append(w.getTile(x, y).getID() + " ");
				}
			}
			
			String contents = s.toString();
			
			contents = FileEncrypter.encryptString(contents);
			
			PrintWriter pr = new PrintWriter(f);
			pr.write(contents);
			pr.close();
		}
	}
}