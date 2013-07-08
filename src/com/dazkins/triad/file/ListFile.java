package com.dazkins.triad.file;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sun.misc.BASE64Decoder;

public class ListFile extends MainFile {
	private String splitter;
	
	public List<Object> objs = new ArrayList<Object>();
	
	public ListFile(String p, String s) throws IOException {
		path = p;
		loadFile();
		splitter = s;
	}
	
	private void loadFile() throws IOException {
		String content = super.loadFileContents(path);
		content = super.decryptContents(content);
		String[] data = content.split(splitter);
		for (int i = 0; i < data.length; i++) {
			objs.add(data[i]);
		}
	}
}