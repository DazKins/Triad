package com.dazkins.triad.file;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SingleLineDatabaseFile extends MainFile {
	private Map<String, String> map = new HashMap<String, String>();
	
	public SingleLineDatabaseFile(String p) throws IOException {
		super(p);
		validateFileType(p, ".db");
		loadFile();
	}
	
	public String getString(String t) {
		return map.get(t);
	}
	
	public int getInt(String i) {
		return Integer.parseInt(getString(i));
	}
	
	public byte getByte(String i) {
		return Byte.parseByte(getString(i));
	}
	
	public boolean getBoolean(String i) {
		return Boolean.parseBoolean(getString(i));
	}
	
	private void loadFile() throws IOException {
		String content = loadFileContents(path);
		content = decryptContents(content);
		
		String[] nodes = content.split(" ");
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
	}
}