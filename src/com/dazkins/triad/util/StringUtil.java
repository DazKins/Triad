package com.dazkins.triad.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public class StringUtil
{
	public static final String LINE_SEPERATOR = System.getProperty("line.separator");
	public static final String RANDOM_SPACE_SEPERATOR = "ב";
	
	public static String loadFileAsString(String p)
	{
		StringBuilder res = new StringBuilder();
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(StringUtil.class.getResourceAsStream(p), "UTF-8"));
			String buffer = "";
			while ((buffer = reader.readLine()) != null)
			{
				res.append(buffer);
				res.append(LINE_SEPERATOR);
			}
			reader.close();
		} catch (Exception e)
		{
			TriadLogger.log(e.getMessage(), true);
		}
		return res.toString();
	}
}