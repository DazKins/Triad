package com.dazkins.triad.file;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class FileEncrypter {
	public static void main(String args[]) throws Exception {
		JFrame frame = new JFrame("Encrypter");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    JPanel mainPanel = new JPanel();
        
        final JFileChooser jfc = new JFileChooser();
        jfc.setBounds(0, 0, 1280, 720);
        jfc.setControlButtonsAreShown(false);
        jfc.setCurrentDirectory(new File("C:/dev/java/Triad/res"));
        mainPanel.add(jfc);
	    
	    JButton btn0;
        btn0 = new JButton("ENCRYPT");
        btn0.setBounds(0, 0, 1000, 500);
        btn0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					encrypt(jfc.getSelectedFile());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
        });
        mainPanel.add(btn0);
        
        JButton btn1;
        btn1 = new JButton("DECRYPT");
        btn1.setBounds(0, 0, 1000, 500);
        btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					decrypt(jfc.getSelectedFile());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
        });
        mainPanel.add(btn1);

	    frame.setContentPane(mainPanel);

	    frame.setSize(520,400);
	    frame.setVisible(true);
	    frame.setResizable(false);
	}

	private static void decrypt(File f) throws IOException {
		char chars[] = new char[(int)f.length()];
		FileReader fr = new FileReader(f);
		fr.read(chars);
		String content = new String(chars);
		content = content.replace("enc ", "");
		
		BASE64Decoder be = new BASE64Decoder();
		String str = new String(be.decodeBuffer(content));
		
		PrintWriter pr = new PrintWriter(f);
		pr.write(str);
		pr.close();
	}
	
	private static void encrypt(File f) throws IOException {
		char chars[] = new char[(int)f.length()];
		FileReader fr = new FileReader(f);
		fr.read(chars);
		String content = new String(chars);
		
		BASE64Encoder be = new BASE64Encoder();
		String str = new String(be.encodeBuffer(content.getBytes()));
		str = "enc " + str;
		
		PrintWriter pr = new PrintWriter(f);
		pr.write(str);
		pr.close();
	}
}