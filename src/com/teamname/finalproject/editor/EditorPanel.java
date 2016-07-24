package com.teamname.finalproject.editor;

import com.teamname.finalproject.Tabs;
import com.teamname.finalproject.editor.minimap.MiniMapPanel;
import com.teamname.finalproject.game.Game;

import javax.swing.*;
import java.awt.*;

public class EditorPanel extends JPanel {

	private static FileBrowser browser;
	private GameScreen gameScreen;
	private MiniMapPanel miniMapPanel;
	private ButtonsPanel buttonsPanel;
	private EventListener eventListener;
	private DownPanel downPanel;

	private int staBlockSize;
	private Dimension d;

	public EditorPanel(Tabs tabs) {
		gameScreen = new GameScreen();
		browser = new FileBrowser(System.getProperty("user.dir") , gameScreen.getMapHandler() , Game.getMap());
		buttonsPanel = new ButtonsPanel(this , browser , tabs);
		miniMapPanel = new MiniMapPanel(this.gameScreen);
		eventListener = new EventListener(gameScreen , buttonsPanel);
		downPanel = new DownPanel(gameScreen);
		gameScreen.getMapHandler().setButtonsPanel(buttonsPanel);
		setLayout(null);
		d = Toolkit.getDefaultToolkit().getScreenSize();
		staBlockSize = (int) (d.getWidth() / 20);
		getDownPanel().setLocation(0 , gameScreen.getHeight());
		add(getDownPanel());
		add(buttonsPanel);
		Dimension localDimension;
		localDimension = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(localDimension);
		setLocation(0 , 0);
		staBlockSize = (int) localDimension.getWidth() / 20;
		gameScreen.setLocation(0 , 0);
		buttonsPanel.setLocation(gameScreen.getWidth() , 0);
		add(miniMapPanel);
		add(gameScreen);
		gameScreen.setMiniMap(miniMapPanel.getMiniMap());
		miniMapPanel.setLocation(gameScreen.getWidth() , gameScreen.getHeight());
		miniMapPanel.setSize(buttonsPanel.getWidth() , downPanel.getHeight());
		miniMapPanel.getMiniMap().setLocation((miniMapPanel.getWidth() - miniMapPanel.getMiniMap().getWidth()) / 2 , (miniMapPanel.getHeight() - miniMapPanel.getMiniMap().getHeight()) / 2);
	}

	private void buildWindow() {
		getGameScreen().setLocation(0 , 0);
		add(getGameScreen());
	}

	private void addListeners() {
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	public MiniMapPanel getMinMapPanel() {
		return miniMapPanel;
	}

	public void setMinMapPanel(MiniMapPanel minMapPanel) {
		this.miniMapPanel = minMapPanel;
	}

	public int getBlocksize() {
		return staBlockSize;
	}

	public void setBlocksize(int blocksize) {
		this.staBlockSize = blocksize;
	}

	public DownPanel getDownPanel() {
		return downPanel;
	}

	public void setDownPanel(DownPanel downPanel) {
		this.downPanel = downPanel;
	}

	static public FileBrowser getBrowser() {
		return browser;
	}

}
