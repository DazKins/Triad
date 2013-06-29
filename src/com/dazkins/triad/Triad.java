package com.dazkins.triad;

import java.awt.Canvas;

import javax.swing.JFrame;

public class Triad extends Canvas {
	private final String title = "Triad Pre-Alpha";
	private final int WIDTH = 1280;
	private final int HEIGHT = 720;
	
	public static void main(String args[]) {
		Triad mc = new Triad();
		JFrame frame = new JFrame(mc.title);
		
		frame.add(mc);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.pack();
		
		mc.start();
	}

	private void start() {
		
	}
}