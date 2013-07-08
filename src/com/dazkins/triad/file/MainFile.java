package com.dazkins.triad.file;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import sun.misc.BASE64Decoder;

public abstract class MainFile {
	protected String path;
	
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
		System.out.println(s);
		return s;
	}
	
	protected static String loadFileContents(String p) throws IOException {
		String rValue;
		File f = new File(p);
		FileReader fr = new FileReader(f);
		char chars[] = new char[(int) f.length()];
		fr.read(chars);
		rValue = new String(chars);
		return rValue;
	}
}