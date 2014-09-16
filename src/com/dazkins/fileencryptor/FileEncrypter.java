package com.dazkins.fileencryptor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.dazkins.triad.file.MainFile;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class FileEncrypter {
	public static JFrame frame = new JFrame("Encrypter");
	private static String path;
    
    private static JTextArea textArea = new JTextArea(20, 20);
	
	public static void main(String args[]) throws Exception {
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    JPanel mainPanel = new JPanel();
	    mainPanel.setLayout(null);
	    
	    textArea.setBounds(0, 0, 1280, 600);
	    textArea.setLineWrap(true);
	    mainPanel.add(textArea);
	    
	    JButton open = new JButton("Open");
	    open.setBounds(0, 605, 200, 85);
	    open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				    textArea.setText("");
					File f = requestOpen();
					path = f.getPath();
					FileReader fr;
					fr = new FileReader(f);
					char chars[] = new char[(int) f.length()];
					fr.read(chars);
					fr.close();
					String text = new String(chars);
					text = decrypt(text);
				    textArea.setText(text);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
	    });
	    mainPanel.add(open);
	    
	    JButton save = new JButton("Save");
	    save.setBounds(1280 - 200, 605, 200, 85);
	    save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestSave();
			}
	    });
	    mainPanel.add(save);

	    frame.setContentPane(mainPanel);

	    frame.setSize(1280, 720);
	    frame.setVisible(true);
	    frame.setResizable(false);
	}
	
	private static File requestOpen() {
		File f = null;
		JFileChooser jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File("C:\\dev\\Triad\\res\\data"));
		int r = jfc.showOpenDialog(frame);
		if (r == JFileChooser.APPROVE_OPTION) {
			f = jfc.getSelectedFile();
		}
		return f;
	}
	
	private static void requestSave() {
		if (path == null) {
			JFileChooser jfc = new JFileChooser();
			int r = jfc.showSaveDialog(frame);
			if (r == JFileChooser.APPROVE_OPTION) {
				try {
					File f = jfc.getSelectedFile();
					PrintWriter pr = new PrintWriter(f);
					pr.write(encryptString(textArea.getText()));
					pr.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				File f = new File(path);
				PrintWriter pr;
				pr = new PrintWriter(f);
				pr.write(encryptString(textArea.getText()));
				pr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static String decrypt(String s) throws IOException {
		if (!s.startsWith("enc ")) {
			System.err.println("File not correctly encrypted, please use the provided encrypter!");
		}
		s = s.replace("enc ", "");
		BASE64Decoder be = new BASE64Decoder();
		s = new String(be.decodeBuffer(s));
		return s;
	}
	
	public static String encryptString(String s) {
		BASE64Encoder be = new BASE64Encoder();
		String str = new String(be.encodeBuffer(s.getBytes()));
		str = "enc " + str;
		return str;
	}
}