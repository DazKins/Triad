package com.dazkins.triad.input;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.glfw.GLFW;
import org.lwjgl.system.glfw.WindowCallback;

import com.dazkins.triad.gfx.Window;

public class InputHandler extends WindowCallback {
	private Window window;
	
	private boolean[] keys = new boolean[400];
	private boolean[] justDownedKeys = new boolean[400];
	
	public boolean mouse1;
	public boolean mouse1JustDown;
	public boolean mouse2;
	public boolean mouse2JustDown;
	public boolean mouse3;
	public boolean mouse3JustDown;
	
	public int mouseX, mouseY;
	public int mWheel;
	
	public InputHandler(Window w) {
		window = w;
		WindowCallback.set(window.getWindowReference(), this);
	}
	
	public boolean isKeyDown(int k) {
		return keys[k];
	}
	
	public boolean isKeyJustDown(int k) {
		return justDownedKeys[k];
	}
	
	public void tick() {
		mouse1 = GLFW.glfwGetMouseButton(window.getWindowReference(), GLFW.GLFW_MOUSE_BUTTON_1) == GLFW.GLFW_PRESS;
		mouse2 = GLFW.glfwGetMouseButton(window.getWindowReference(), GLFW.GLFW_MOUSE_BUTTON_2) == GLFW.GLFW_PRESS;
		mouse3 = GLFW.glfwGetMouseButton(window.getWindowReference(), GLFW.GLFW_MOUSE_BUTTON_3) == GLFW.GLFW_PRESS;
		
		for (int i = 0; i < justDownedKeys.length; i++) {
			justDownedKeys[i] = false;
		}
		
		mouse1JustDown = false;
		mouse2JustDown = false;
		mouse3JustDown = false;
		
		mWheel = 0;
	}

	public void charMods(long window, int codepoint, int mods) { }

	public void character(long window, int codepoint) { }

	public void cursorEnter(long window, int entered) { }

	public void cursorPos(long window, double xpos, double ypos) {
		mouseX = (int) xpos;
		mouseY = (int) (this.window.getH() - ypos);
	}

	public void drop(long window, int count, long names) { }

	public void framebufferSize(long window, int width, int height) { }

	public void key(long window, int key, int scancode, int action, int mods) {
		if (action == GLFW.GLFW_PRESS) {
			if (key < 400 && key >= 0) {
				keys[key] = true;
				justDownedKeys[key] = true;
			}
		} else if (action == GLFW.GLFW_RELEASE) {
			if (key < 400 && key >= 0) {
				keys[key] = false;
			}
		}
	}

	public void mouseButton(long window, int button, int action, int mods) {
		if (action == GLFW.GLFW_PRESS) {
			if (button == GLFW.GLFW_MOUSE_BUTTON_1)
				mouse1JustDown = true;
			else if (button == GLFW.GLFW_MOUSE_BUTTON_2)
				mouse2JustDown = true;
			else if (button == GLFW.GLFW_MOUSE_BUTTON_3)
				mouse3JustDown = true;
		}
	}

	public void scroll(long window, double xoffset, double yoffset) {
		mWheel = (int) yoffset;
	}

	public void windowClose(long window) { }

	public void windowFocus(long window, int focused) { }

	public void windowIconify(long window, int iconified) { }

	public void windowPos(long window, int xpos, int ypos) { }

	public void windowRefresh(long window) { }

	public void windowSize(long window, int width, int height) { }
}