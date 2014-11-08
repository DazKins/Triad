package com.dazkins.triad.input;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputHandler  {
	private boolean[] keys = new boolean[65536];
	private boolean[] justDownedKeys = new boolean[65536];
	
	public boolean mouse1;
	public boolean mouse1JustDown;
	public boolean mouse2;
	public boolean mouse2JustDown;
	
	public int mouseX, mouseY;
	public int mWheel;
	
	public boolean isKeyDown(int k) {
		return keys[k];
	}
	
	public boolean isKeyJustDown(int k) {
		return justDownedKeys[k];
	}
	
	public void tick() {
		for (int i = 0; i < 65536; i++) {
			justDownedKeys[i] = false;
		}
		mouse1JustDown = false;
		mouse2JustDown = false;
		
		while(Keyboard.next()) {
			if(Keyboard.getEventKeyState()) {
				keys[Keyboard.getEventKey()] = true;
				justDownedKeys[Keyboard.getEventKey()] = true;

			} else {
				keys[Keyboard.getEventKey()] = false;
			}
		}
		
		mouse1 = Mouse.isButtonDown(0);
		mouse2 = Mouse.isButtonDown(1);
		
		while(Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				mouse1JustDown = Mouse.isButtonDown(0);
				mouse2JustDown = Mouse.isButtonDown(1);
			}
		}
		
		mouseX = Mouse.getX();
		mouseY = Mouse.getY();
		mWheel = Mouse.getDWheel();
	}
}