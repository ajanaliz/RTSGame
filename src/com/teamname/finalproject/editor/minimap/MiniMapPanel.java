package com.teamname.finalproject.editor.minimap;

import com.teamname.finalproject.editor.GameScreen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MiniMapPanel extends JPanel {
	private GameScreen gaScreen;
	private BufferedImage bg;
	private int blocksize;
	private MiniMap miniMap;

	public MiniMapPanel(GameScreen gameScreen) {
		this.gaScreen = gameScreen;
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setLayout(null);
		blocksize = (int) d.getWidth() / 20;
		miniMap = new MiniMap(this.gaScreen);
		add(miniMap);
		try {
			bg = ImageIO.read(new File("res//minimap//minimapbg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(bg , 0 , 0 , this.getWidth() , this.getHeight() , null);
	}

	public GameScreen getGaScreen() {
		return gaScreen;
	}

	public void setGaScreen(GameScreen gaScreen) {
		this.gaScreen = gaScreen;
	}

	public int getBlocksize() {
		return blocksize;
	}

	public void setBlocksize(int blocksize) {
		this.blocksize = blocksize;
	}

	public MiniMap getMiniMap() {
		return miniMap;
	}

	public void setMiniMap(MiniMap miniMap) {
		this.miniMap = miniMap;
	}

}
