package com.dazkins.triad.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class TriadLogger
{
	private Logger logger;
	private String name;
	
	private String padName;
	
	private static TriadLogger LOG;
	
	public static void initClientLog()
	{
		LOG = new TriadLogger("client.log", "Triad Client");
	}
	
	public static void initServerLog()
	{
		LOG = new TriadLogger("server.log", "Triad Server");
	}
	
	//p = path to log file
	public TriadLogger(String p, String n)
	{
		name = n;
		try
		{
			logger = Logger.getLogger(name);
			logger.setUseParentHandlers(false);
			FileHandler fh;
			fh = new FileHandler(p);
			logger.addHandler(fh);
			fh.setFormatter(new LoggerFormatter());
		} catch (SecurityException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		padName = String.format("%1$-" + 15 + "s", name);
		writeToLog("Logger started!", false);
	}
	
	public static void log(String s, boolean err)
	{
		LOG.writeToLog(s, err);
	}
	
	public void writeToLog(String s, boolean err)
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String pre = "[" + padName + sdf.format(cal.getTime()) + "]  ";
		if (err)
			safePrintErr(pre + s + StringUtil.LINE_SEPERATOR);
		else
			safePrint(pre + s + StringUtil.LINE_SEPERATOR);
		logger.info(pre + s + StringUtil.LINE_SEPERATOR);
	}
	
	public static synchronized void safePrint(String s)
	{
		System.out.print(s);
		System.out.flush();
	}
	
	public static synchronized void safePrintErr(String s)
	{
		System.err.print(s);
		System.err.flush();
	}
	
	private static class LoggerFormatter extends Formatter
	{
		public String format(LogRecord l)
		{
			return l.getMessage();
		}
	}
}