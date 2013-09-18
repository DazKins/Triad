package com.dazkins.triadeditor;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.dazkins.triad.game.world.World;
import com.dazkins.triad.game.world.tile.Tile;
import com.dazkins.triad.gfx.Camera;
import com.dazkins.triad.gfx.ViewportInfo;
import com.dazkins.triad.input.InputHandler;

public class Application {
	private ControlPanel controlPanel;
	private World world;
	private Camera cam;
	private InputHandler input;
	private ViewportInfo viewport;
	
	private int selectedTile;
	private boolean displayGrid;
	
	public Application(InputHandler i, ViewportInfo v) {
		world = World.getWorldFromName("TestingMap");
		input = i;
		viewport = v;
		cam = new Camera(i, viewport, 0, 0);
		cam.lockZoom(0.7f, 1.3f);
	}

	public void initControlPanel(ControlPanel cp) {
		controlPanel = cp;

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
		jc.setBounds(0, 450, 200, 50);
		jc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayGrid = !displayGrid;
			}
		});
	}
	
	private void renderTileSelector() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		int x = ((int) ((input.mouseX / cam.getZoom()) + cam.getX()) >> 5) << 5;
		int y = ((int) ((input.mouseY / cam.getZoom()) + cam.getY()) >> 5) << 5;
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4f(1, 0, 0, 0.5f);
			GL11.glVertex3f(x, y, 5.0f);
			GL11.glVertex3f(x + Tile.tileSize, y, 5.0f);
			GL11.glVertex3f(x + Tile.tileSize, y + Tile.tileSize, 5.0f);
			GL11.glVertex3f(x, y + Tile.tileSize, 5.0f);
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
				if (t != null)
					world.setTile(t, sx, sy);
			}
		}
		
		if (input.isKeyDown(Keyboard.KEY_S)) {
			try {
				world.saveWorldToFile("res/testWorld.lt");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		cam.tick();
		
		cam.moveWithKeys(10f, Keyboard.KEY_UP, Keyboard.KEY_DOWN, Keyboard.KEY_RIGHT, Keyboard.KEY_LEFT);
	}
	
	public void render() {
		GL11.glPushMatrix();
		cam.attachTranslation();
		
		if(displayGrid)
			world.renderGrid(cam);
		
		world.render(cam);
		
		renderTileSelector();
		
		GL11.glPopMatrix();
	}
}