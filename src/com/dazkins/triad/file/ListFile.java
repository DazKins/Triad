package com.dazkins.triad.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListFile extends MainFile {
	private String splitter;	
	private List<String> objs = new ArrayList<String>();
	private int size;
	
	public ListFile(String path, String splitter) throws IOException {
		super(path);
		validateFileType(path, ".lt");
		this.splitter = splitter;
		loadFile();
	}
	
	public ListFile(File f) throws IOException {
		super(f.getPath());
		validateFileType(path, ".lt");
		loadFile();
	}
	
	public String getString(int index) {
		return (String) (objs.get(index));
	}
	
	public int getInt(int index) {
		return Integer.parseInt(getString(index));
	}
	
	private void loadFile() throws IOException {
		String content = super.loadFileContents(path);
		content = super.decryptContents(content);
		String[] data = content.split(splitter);
		for (int i = 0; i < data.length; i++) {
			String s = data[i].replace(splitter, "");
			objs.add(s);
		}
		size = data.length;
	}

	public int getSize() {
		return size;
	}
}