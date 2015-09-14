package com.dazkins.triad.networking.packet;

public class Packet003ChunkData extends Packet {
	private int x;
	private int y;
	private byte[] light;
	private byte[] tiles;
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public byte[] getLight() {
		return light;
	}
	
	public void setLight(byte[] light) {
		this.light = light;
	}
	
	public byte[] getTiles() {
		return tiles;
	}
	
	public void setTiles(byte[] tiles) {
		this.tiles = tiles;
	}
}