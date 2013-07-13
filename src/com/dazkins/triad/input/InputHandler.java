package com.dazkins.triad.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputHandler  {
	private boolean[] keys = new boolean[65536];
	private int mouseX, mouseY;
	
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
		
		mouseX = Mouse.getX();
		mouseY = Mouse.getY();
	}
}