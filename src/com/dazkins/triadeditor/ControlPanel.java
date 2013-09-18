package com.dazkins.triadeditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ControlPanel {
	private static int WIDTH = 400;
	private static int HEIGHT = 720;
	
	public JFrame frame;
	
	public JButton addButton(FunctionParser fp, String name, int x0, int y0, int x1, int y1) {
		final TriadButton tb = new TriadButton(name, fp);
		frame.add(tb);
		tb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tb.onPress();
			}
		});
		tb.setBounds(x0, y0, x1, y1);
		return tb;
	}

	public ControlPanel(int x, int y) {
		frame = new JFrame();
		frame.setLocation(x, y);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLayout(null);
		frame.pack();

		Dimension d = new Dimension(WIDTH, HEIGHT);
		
		frame.setPreferredSize(d);
		frame.setMinimumSize(d);
		frame.setMaximumSize(d);
	}
}