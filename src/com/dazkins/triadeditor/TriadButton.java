package com.dazkins.triadeditor;

import javax.swing.JButton;

public class TriadButton extends JButton {
	private static final long serialVersionUID = -4277475306310414132L;
	
	private FunctionParser funcp;
	
	public TriadButton(String name, FunctionParser fp) {
		super(name);
		funcp = fp;
	}
	
	public void onPress() {
		funcp.func();
	}
}