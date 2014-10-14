package com.dazkins.triadeditor;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.WindowInfo;
import com.dazkins.triad.input.InputHandler;

public class Application {
	private ControlPanel controlPanel;
	private World world;
	private Camera cam;
	private InputHandler input;
	private WindowInfo winInfo;
	
	private int selectedTile;
	private int brushSize = 1;
	private boolean displayGrid;
	
	public Application(InputHandler i, WindowInfo w) {
		world = World.getWorldFromName("TestingMap");
		input = i;
		winInfo = w;
		cam = new Camera(i, winInfo, 0, 0);
		cam.lockZoom(0.001f, 10000f);
	}

	public void initControlPanel(ControlPanel cp) {
		controlPanel = cp;
		
		controlPanel.addButton(new FunctionParser() {
			public void func() {
				try {
					ApplicationUtil.requestWorldSave(world, controlPanel.frame);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}, "Save", 300, 100, 100, 50);

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(0, 3));
		for (int i = 0; i < Tile.tiles.length; i++) {
			final int id = i;
			ImageIcon image = Tile.getTileImageIcon(i, 64);
			JButton jb = new JButton();
			p.add(jb);
			if (image != null) {
				jb.setIcon(image);
				jb.setBorder(null);
			} else {
				jb.setBackground(Color.PINK);
				jb.setBorder(null);
			}
			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent a) {
					Tile t = Tile.tiles[id];
					if (t != null)
						selectedTile = Tile.tiles[id].getID();
					else
						selectedTile = -1;
				}
			});
		}
		JScrollPane js = new JScrollPane(p, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		js.setViewportView(p);
		controlPanel.frame.add(js);
		js.setBounds(0, 0, 212, 300);
		
		JCheckBox jc = new JCheckBox("Display Grid");
		controlPanel.frame.add(jc);
		jc.setBounds(0, 450, 200, 19);
		jc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayGrid = !displayGrid;
			}
		});
		
		final String[] options = new String[10];
		for (int i = 0; i < options.length; i++) {
			options[i] = i + "";
		}
		controlPanel.addButton(new FunctionParser() {
			public void func() {
				brushSize = Integer.parseInt((String) JOptionPane.showInputDialog(null, "Choose brush size", "Brush Size", JOptionPane.PLAIN_MESSAGE, null, options, options[0]));
			}
		}, "Change brush size", 0, 600, 200, 50);
		
		JLabel hl = new JLabel("hit");
		controlPanel.frame.add(hl);
		hl.setBounds(0, 400, 100, 100);
		
		JTextArea jt = new JTextArea();
		controlPanel.frame.add(jt);
		jt.setBounds(0, 300, 100, 100);
		jt.setLineWrap(true);
	}
	
	private void renderTileSelector() {
		int mX = ((int) ((input.mouseX / cam.getZoom()) + cam.getX()) >> 5) << 5;
		int mY = ((int) ((input.mouseY / cam.getZoom()) + cam.getY()) >> 5) << 5;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(1, 0, 0, 0.5f);
		
		for (int x = mX - brushSize * 32; x < mX + (brushSize + 1) * 32; x += 32) {
			for (int y = mY - brushSize * 32; y < mY + (brushSize + 1) * 32; y += 32) {
				float xD = Math.abs(mX - x) / 32.0f;
				float yD = Math.abs(mY - y) / 32.0f;
				float l = (float) Math.sqrt(xD * xD + yD * yD);
				if (l <= brushSize) {
					GL11.glVertex3f(x, y, 5.0f);
					GL11.glVertex3f(x + Tile.tileSize, y, 5.0f);
					GL11.glVertex3f(x + Tile.tileSize, y + Tile.tileSize, 5.0f);
					GL11.glVertex3f(x, y + Tile.tileSize, 5.0f);
				}
			}
		}
		
		GL11.glColor3f(1, 1, 1);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public void tick() {
		if (input.mouse1) {
			if (selectedTile >= 0) {
				Tile t = Tile.tiles[selectedTile];
				int sx = (int) ((input.mouseX / cam.getZoom()) + cam.getX()) >> 5;
				int sy = (int) ((input.mouseY / cam.getZoom()) + cam.getY()) >> 5;
				for (int x = sx - brushSize; x < sx + (brushSize + 1) ; x++) {
					for (int y = sy - brushSize; y < sy + (brushSize + 1); y++) {
						float xD = Math.abs(sx - x);
						float yD = Math.abs(sy - y);
						float l = (float) Math.sqrt(xD * xD + yD * yD);
						if (l <= brushSize) {
							if (t != null)
								world.setTile(t, x, y);
						}
					}
				}
			}
		}
		
		cam.tick();
		
		cam.moveWithKeys(10f, Keyboard.KEY_UP, Keyboard.KEY_DOWN, Keyboard.KEY_RIGHT, Keyboard.KEY_LEFT);
	}
	
	public void render() {
		GL11.glPushMatrix();
		cam.attachTranslation();
		
		if(displayGrid)
			world.renderGrid();
		
		world.render();
		
		renderTileSelector();
		
		GL11.glPopMatrix();
	}
}