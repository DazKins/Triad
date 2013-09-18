package com.dazkins.triad.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import sun.misc.BASE64Decoder;

public abstract class MainFile {
	protected String path;
	
	public MainFile(String p) {
		path = p;
	}
	
	public void writeToFile(String c) {
		try {
			PrintWriter pr = new PrintWriter(new File(path));
			decryptContents(c);
			pr.write(c);
			pr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static void validateFileType(String s, String ext) {
		if (!s.endsWith(ext)) {
			System.out.println("Warning! File: " + s + " does not end with file extension \"" + ext + "\"");
		}
	}
	
	protected static String decryptContents(String s) throws IOException {
		if (!s.startsWith("enc ")) {
			throw new RuntimeException("File not correctly encrypted, please use the provided encrypter!");
		}
		s = s.replace("enc ", "");
		BASE64Decoder be = new BASE64Decoder();
		s = new String(be.decodeBuffer(s));
		return s;
	}
	
	protected static String loadFileContents(String p) throws IOException {
		String rValue;
		File f = new File(p);
		FileReader fr = new FileReader(f);
		char chars[] = new char[(int) f.length()];
		fr.read(chars);
		fr.close();
		rValue = new String(chars);
		return rValue;
	}
}