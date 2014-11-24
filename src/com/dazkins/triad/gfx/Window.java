package com.dazkins.triad.gfx;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.glfw.ErrorCallback;
import org.lwjgl.system.glfw.GLFW;
import org.lwjgl.system.glfw.GLFWvidmode;
import org.lwjgl.system.glfw.WindowCallback;
import org.lwjgl.system.glfw.WindowCallbackAdapter;

public class Window {
	private int w, h;
	
	private boolean resized;
	
	private long winRef;
	
	public Window(int w, int h) {
		this.w = w;
		this.h = h;
	}
	
	public long getWindowReference() {
		return winRef;
	}
	
	public void setup() {
		GLFW.glfwSetErrorCallback(ErrorCallback.Util.getDefault());
		 
        if (GLFW.glfwInit() != GL11.GL_TRUE)
            throw new IllegalStateException("Unable to initialize GLFW");
        
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 16);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
 
        winRef =  GLFW.glfwCreateWindow(w, h, "Triad", MemoryUtil.NULL, MemoryUtil.NULL);
        if (winRef == MemoryUtil.NULL)
            throw new RuntimeException("Failed to create the GLFW window");
        
        ByteBuffer vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(winRef, (GLFWvidmode.width(vidmode) - w) / 2, (GLFWvidmode.height(vidmode) - h) / 2);
 
        GLFW.glfwMakeContextCurrent(winRef);
 
        GLFW.glfwShowWindow(winRef);
	}
	
	public void update() {
		GLFW.glfwSwapBuffers(winRef);
        GLFW.glfwPollEvents();
	}
	
	public void destroy() {
		GLFW.glfwDestroyWindow(winRef);
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}

	public void setW(int w) {
		this.w = w;
		resized = true;
	}

	public void setH(int h) {
		this.h = h;
		resized = true;
	}
	
	public boolean wasResized() {
		return resized;
	}
	
	public boolean wasCloseRequested() {
		return GLFW.glfwWindowShouldClose(winRef) == GL11.GL_TRUE;
	}
	
	public void tickState() {
		resized = false;
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowSize(winRef, width, height);
		setH(height.get(0));
		setW(width.get(0));
	}
}