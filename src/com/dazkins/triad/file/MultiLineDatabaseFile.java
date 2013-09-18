package com.dazkins.triad.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiLineDatabaseFile extends MainFile {
	private List<Map<String, String>> tags = new ArrayList<Map<String, String>>();
	private int lineCount;

	public MultiLineDatabaseFile(String p) throws IOException {
		super(p);
		validateFileType(p, ".db");
		loadDatabaseFile();
	}
	
	public String getString(String tag, int line) {
		return (String) (tags.get(line).get(tag));
	}
	
	public int getInt(String tag, int line) {
		return Integer.parseInt(getString(tag, line));
	}
	
	public byte getByte(String tag, int line) {
		return Byte.parseByte(getString(tag, line));
	}
	
	public boolean getBoolean(String tag, int line) {
		return Boolean.parseBoolean(getString(tag, line));
	}
	
	public int getLineCount() {
		return lineCount;
	}

	public void loadDatabaseFile() throws IOException {
		String content = super.loadFileContents(path);
		content = super.decryptContents(content);
		String[] lines = content.split(System.getProperty("line.separator"));
		lineCount = lines.length;
		for (int i = 0; i < lines.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			String[] nodes = lines[i].split(" ");
			for(int u =0; u < nodes.length; u++) {
				String node = nodes[u].replace(" ", "");
				if(node.startsWith("[")) {
					String tag = node.replace("[", "").replace("]", "");
					String nodeValue = "";
					for (int o = 1; o < nodes.length - u; o++) {
						String tmpNodeValue = nodes[u + o];
						if(tmpNodeValue.startsWith("[")) {
							break;
						} else {
							nodeValue += tmpNodeValue;
						}
					}
					map.put(tag, nodeValue);
				}
			}
			tags.add(map);
		}
	}
}