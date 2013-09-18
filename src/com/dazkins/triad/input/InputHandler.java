package com.dazkins.triad.input;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputHandler  {
	private boolean[] keys = new boolean[65536];
	public boolean mouse1;
	public int mouseX, mouseY;
	public int mWheel;
	
	public boolean isKeyDown(int k) {
		return keys[k];
	}
	
	public void tick() {
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				keys[Keyboard.getEventKey()] = true;

			} else {
				keys[Keyboard.getEventKey()] = false;
			}
		}
		
		mouse1 = Mouse.isButtonDown(0);
		
		mouseX = Mouse.getX();
		mouseY = Mouse.getY();
		mWheel = Mouse.getDWheel();
	}
}