package com.dazkins.triad.gfx;

//Respresents color with component values between 0 and 255
public class Color {
	private int c;
	
	public Color(int c) {
		this.c = c;
	}
	
	public Color(int r, int g, int b) {
		c += r << 16;
		c += g << 8;
		c += b;
	}
	
	public void setR(int r) {
		c &= 0x00FFFF;
		c += r << 16;
	}
	
	public void setG(int g) {
		c &= 0xFF00FF;
		c += g << 8;
	}
	
	public void setB(int b) {
		c &= 0xFFFF00;
		c += b;
	}
	
	public int getR() {
		return (c >> 16) & 0xFF;
	}
	
	public int getG() {
		return (c >> 8) & 0xFF;
	}
	
	public int getB() {
		return c & 0xFF;
	}
	
	public float getDR() {
		return getR() / 255.0f;
	}
	
	public float getDG() {
		return getG() / 255.0f;
	}
	
	public float getDB() {
		return getB() / 255.0f;
	}
	
	public static Color averageColors(Color[] c) {
		int r = 0, g = 0, b = 0;
		for (int i = 0; i < c.length; i++) {
			r += c[i].getR();
			g += c[i].getG();
			b += c[i].getB();
		}
		r /= c.length;
		g /= c.length;
		b /= c.length;
		return new Color(r, g, b);
	}
	
	public void clip(int c) {
		if (getR() < c)
			setR(c);
		if (getG() < c)
			setG(c);
		if (getB() < c)
			setB(c);
	}
	
	public void blend(Color c) {
		if (getR() < c.getR())
			setR(c.getR());
		if (getG() < c.getG())
			setG(c.getG());
		if (getB() < c.getB())
			setB(c.getB());
	}
	
	public static Color lerp(Color c1, Color c2, float l) {
		int r = (int) (((c2.getR() - c1.getR()) * l) + c1.getR());
		int g = (int) (((c2.getG() - c1.getG()) * l) + c1.getG());
		int b = (int) (((c2.getB() - c1.getB()) * l) + c1.getB());
		return new Color(r, g, b);
	}
	
	public String toString() {
		return "Red: " + getR() + " Green: " + getG() + " Blue: " + getB();
	}
	 
	public boolean equals(Color c) {
		if (c == null)
			return false;
		return c.c == this.c;
	}
	
	public Color copyOf() {
		return new Color(c);
	}
}