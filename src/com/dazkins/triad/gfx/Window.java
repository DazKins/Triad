package com.dazkins.triad.gfx;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import com.dazkins.triad.util.TriadLogger;

public class Window
{
	private int w, h;

	private boolean resized;
	private boolean fullscreen;

	private long winRef;

	public Window(int w, int h, boolean f)
	{
		this.w = w;
		this.h = h;
		this.fullscreen = f;
	}

	public long getWindowReference()
	{
		return winRef;
	}

	public void setup()
	{
//		GLFW.glfwSetErrorCallback(ErrorCallback.Util.getDefault());
		GLFWErrorCallback.createPrint(System.err).set();

		if (!GLFW.glfwInit())
		{
			TriadLogger.log("GLFW did not initialize!", true);
			System.exit(-1);
		}

		GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 16);

		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);

		if (fullscreen)
			winRef = GLFW.glfwCreateWindow(w, h, "Triad", GLFW.glfwGetPrimaryMonitor(), MemoryUtil.NULL);
		else
			winRef = GLFW.glfwCreateWindow(w, h, "Triad", MemoryUtil.NULL, MemoryUtil.NULL);

		if (winRef == MemoryUtil.NULL)
		{
			TriadLogger.log("Failed to create the GLFW window", true);
			System.exit(-1);
		}

		if (!fullscreen)
		{
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			GLFW.glfwSetWindowPos(winRef, (vidmode.width() - w) / 2, (vidmode.height() - h) / 2);
		}

		GLFW.glfwMakeContextCurrent(winRef);

		GLFW.glfwSwapInterval(1);

		GLFW.glfwShowWindow(winRef);
	}

	public void update()
	{
		GLFW.glfwSwapBuffers(winRef);
		GLFW.glfwPollEvents();
	}

	public void destroy()
	{
		GLFW.glfwDestroyWindow(winRef);
	}

	public int getW()
	{
		return w;
	}

	public int getH()
	{
		return h;
	}

	public void setW(int w)
	{
		if (w != this.w)
		{
			this.w = w;
			resized = true;
		}
	}

	public void setH(int h)
	{
		if (h != this.h)
		{
			this.h = h;
			resized = true;
		}
	}

	public boolean wasResized()
	{
		return resized;
	}

	public boolean wasCloseRequested()
	{
		return GLFW.glfwWindowShouldClose(winRef);
	}

	public void tickState()
	{
		resized = false;
		IntBuffer widthB = BufferUtils.createIntBuffer(1);
		IntBuffer heightB = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowSize(winRef, widthB, heightB);
		int width = widthB.get(0);
		int height = heightB.get(0);
		if (w != width)
		{
			resized = true;
			w = width;
		}
		if (h != height)
		{
			resized = true;
			h = height;
		}
	}
}