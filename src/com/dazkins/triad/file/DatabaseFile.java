package com.dazkins.triad.file;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseFile {
	public List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();;
	private String path;

	public DatabaseFile(String p) throws IOException {
		if (!p.endsWith(".db"))
			System.out.println("Warning, \".db\" is the native filetype for databases");
		path = p;
		loadDatabaseFile();
	}

	private static String dataType(String s) {
		if (s.startsWith("\""))
			return "string";
		else if (s.startsWith("$"))
			return "bool";
		else
			return "int";
	}

	public void loadDatabaseFile() throws IOException {
		File f = new File(path);
		FileReader fr = new FileReader(f);
		char[] chars = new char[(int) f.length()];
		fr.read(chars);
		String content = new String(chars);
		String[] lines = content.split(System.getProperty("line.separator"));
		for (int i = 0; i < lines.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String[] nodes = lines[i].split(" ");
			for(int u =0; u < nodes.length; u++) {
				String node = nodes[u].replace(" ", "");
				if(node.startsWith("[")) {
					String tag = node.replace("[", "").replace("]", "");
					String nodeValue = nodes[u + 1];
					String dt = dataType(nodeValue);
					if (dt == "int")
						map.put(tag, Integer.parseInt(nodeValue));
					else if (dt == "string")
						map.put(tag, nodeValue.replace("\"", ""));
					else if (dt == "bool")
						map.put(tag, Boolean.parseBoolean(nodeValue.replace("$", "")));
				}
			}
			tags.add(map);
		}
	}
}