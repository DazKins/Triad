package com.dazkins.triad.networking.server.update;

import com.dazkins.triad.networking.TriadConnection;
import com.dazkins.triad.networking.server.CameraState;

public class ServerUpdateCameraState extends ServerUpdate
{
	private TriadConnection con;
	private CameraState newState;
	
	public ServerUpdateCameraState(CameraState s, TriadConnection c)
	{
		this.newState = s;
		this.con = c;
	}
	
	public CameraState getCamState()
	{
		return newState;
	}
	
	public TriadConnection geTriadConnection()
	{
		return con;
	}
}
