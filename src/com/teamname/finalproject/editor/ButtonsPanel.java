package com.teamname.finalproject.editor;

import com.teamname.finalproject.Tabs;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ButtonsPanel extends JPanel {

	private FileBrowser browser;
	private MapHandler mapHandler;
	private int size;
	private int space;
	private Dimension localDimension;
	private int staBlockSize;
	private EditorPanel editorPanel;
	private int zoomNum;
	private boolean mapInExtendedSize;
	private boolean canZoomIn;
	private boolean canZoomOut;
	private Color buttonColor;
	private Tabs tabs;

	public ButtonsPanel(final EditorPanel editorPanel , final FileBrowser browser , final Tabs tabs) {
		this.editorPanel = editorPanel;
		this.browser = browser;
		this.tabs = tabs;
		this.mapHandler = editorPanel.getGameScreen().getMapHandler();
		localDimension = Toolkit.getDefaultToolkit().getScreenSize();
		staBlockSize = (int) (localDimension.getWidth() / 20);
		size = (int) (staBlockSize * 1.8 / 3);
		space = size / 3;
		zoomNum = 5;
		buttonColor = new Color(35 , 51 , 25);
		canZoomIn = true;
		canZoomOut = true;
		mapInExtendedSize = false;
		setLayout(null);
		setBackground(new Color(100 , 100 , 100));
		setSize(staBlockSize * 3 , (int) (staBlockSize * 8.75));
		final JButton newMap = new JButton();
		newMap.setToolTipText("New Map");
		newMap.setBackground(buttonColor);
		BufferedImage newMap1 = null;
		try {
			newMap1 = ImageIO.read(new File("res//newArray.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		newMap.setIcon(new ImageIcon(newMap1.getSubimage(0 , 0 , newMap1.getWidth() , newMap1.getHeight() / 2)));
		newMap.setRolloverIcon(new ImageIcon(newMap1.getSubimage(0 , newMap1.getHeight() / 2 , newMap1.getWidth() , newMap1.getHeight() / 2)));
		newMap.setSize(size , size);
		newMap.setLocation((int) (space * 4.5) , space / 2);
		newMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetMap();
			}
		});
		add(newMap);
		// newMap.repaint();

		JButton clearMap = new JButton();
		clearMap.setBackground(buttonColor);
		BufferedImage clearMap1 = null;
		try {
			clearMap1 = ImageIO.read(new File("res//deleteArray.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		clearMap.setIcon(new ImageIcon(clearMap1.getSubimage(0 , 0 , clearMap1.getWidth() , clearMap1.getHeight() / 2)));
		clearMap.setRolloverIcon(new ImageIcon(clearMap1.getSubimage(0 , clearMap1.getHeight() / 2 , clearMap1.getWidth() , clearMap1.getHeight() / 2)));
		clearMap.setToolTipText("Clear Map");
		clearMap.setSize(size , size);
		clearMap.setLocation(newMap.getX() + size + space , newMap.getY());
		clearMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteMap();
			}
		});
		add(clearMap);

		JButton mapSettings = new JButton();
		mapSettings.setBackground(buttonColor);
		BufferedImage mapSettings1 = null;
		try {
			mapSettings1 = ImageIO.read(new File("res//Settings.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		mapSettings.setIcon(new ImageIcon(mapSettings1.getSubimage(0 , 0 , mapSettings1.getWidth() , mapSettings1.getHeight() / 2)));
		mapSettings.setRolloverIcon(new ImageIcon(mapSettings1.getSubimage(0 , mapSettings1.getHeight() / 2 , mapSettings1.getWidth() , mapSettings1.getHeight() / 2)));
		mapSettings.setToolTipText("Map Settings");
		mapSettings.setSize(size , size);
		mapSettings.setLocation(newMap.getX() , newMap.getY() + size + space);
		mapSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LookAndFeel previousLF = UIManager.getLookAndFeel();
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					browser.changeArrayProperties(false);
					UIManager.setLookAndFeel(previousLF);
				} catch (ClassNotFoundException exception) {
					exception.printStackTrace();
				} catch (InstantiationException exception) {
					exception.printStackTrace();
				} catch (IllegalAccessException exception) {
					exception.printStackTrace();
				} catch (UnsupportedLookAndFeelException exception) {
					exception.printStackTrace();
				}
			}
		});
		add(mapSettings);

		JButton copyMapToCode = new JButton();
		copyMapToCode.setBackground(buttonColor);
		BufferedImage copyMapToCode1 = null;
		try {
			copyMapToCode1 = ImageIO.read(new File("res/copy.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		copyMapToCode.setIcon(new ImageIcon(copyMapToCode1.getSubimage(0 , 0 , copyMapToCode1.getWidth() , copyMapToCode1.getHeight() / 2)));
		copyMapToCode.setRolloverIcon(new ImageIcon(copyMapToCode1.getSubimage(0 , copyMapToCode1.getHeight() / 2 , copyMapToCode1.getWidth() , copyMapToCode1.getHeight() / 2)));
		copyMapToCode.setToolTipText("Copy Map To Code");
		copyMapToCode.setSize(size , size);
		copyMapToCode.setLocation(clearMap.getX() , clearMap.getY() + size + space);
		copyMapToCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LookAndFeel previousLF = UIManager.getLookAndFeel();
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					browser.launchCopyPaste();
					UIManager.setLookAndFeel(previousLF);
				} catch (ClassNotFoundException exception) {
					exception.printStackTrace();
				} catch (InstantiationException exception) {
					exception.printStackTrace();
				} catch (IllegalAccessException exception) {
					exception.printStackTrace();
				} catch (UnsupportedLookAndFeelException exception) {
					exception.printStackTrace();
				}
			}
		});
		add(copyMapToCode);

		JButton zoomIn = new JButton();
		zoomIn.setBackground(buttonColor);
		BufferedImage zoomIn1 = null;
		try {
			zoomIn1 = ImageIO.read(new File("res//zoomIn.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		zoomIn.setIcon(new ImageIcon(zoomIn1.getSubimage(0 , 0 , zoomIn1.getWidth() , zoomIn1.getHeight() / 2)));
		zoomIn.setRolloverIcon(new ImageIcon(zoomIn1.getSubimage(0 , zoomIn1.getHeight() / 2 , zoomIn1.getWidth() , zoomIn1.getHeight() / 2)));
		zoomIn.setToolTipText("Zoom In");
		zoomIn.setSize(size , size);
		zoomIn.setLocation(space + size / 2 , mapSettings.getY() + size + space * 2);
		zoomIn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				zoomIn();
			}
		});
		add(zoomIn);
		JButton MoveUp = new JButton();
		MoveUp.setBackground(buttonColor);
		BufferedImage MoveUp1 = null;
		try {
			MoveUp1 = ImageIO.read(new File("res//navUp.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		MoveUp.setIcon(new ImageIcon(MoveUp1.getSubimage(0 , 0 , MoveUp1.getWidth() , MoveUp1.getHeight() / 2)));
		MoveUp.setRolloverIcon(new ImageIcon(MoveUp1.getSubimage(0 , MoveUp1.getHeight() / 2 , MoveUp1.getWidth() , MoveUp1.getHeight() / 2)));
		MoveUp.setToolTipText("Move Up");
		MoveUp.setSize(size , size);
		MoveUp.setLocation(zoomIn.getX() + size + space , zoomIn.getY());
		MoveUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// editorPanel.getGameScreen().getMapHandler().setCol(editorPanel.getGameScreen().getMapHandler().getCol()-100);
				for (int i = 1 ; i <= 40 ; i++)
					if (editorPanel.getGameScreen().getMapHandler().getY() >= 5) {
						editorPanel.getGameScreen().getMapHandler().setY(editorPanel.getGameScreen().getMapHandler().getY() - 5);

					}
			}
		});
		add(MoveUp);

		JButton ZoomOut = new JButton();
		ZoomOut.setBackground(buttonColor);
		BufferedImage ZoomOut1 = null;
		try {
			ZoomOut1 = ImageIO.read(new File("res//zoomOut.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ZoomOut.setIcon(new ImageIcon(ZoomOut1.getSubimage(0 , 0 , ZoomOut1.getWidth() , ZoomOut1.getHeight() / 2)));
		ZoomOut.setRolloverIcon(new ImageIcon(ZoomOut1.getSubimage(0 , ZoomOut1.getHeight() / 2 , ZoomOut1.getWidth() , ZoomOut1.getHeight() / 2)));
		ZoomOut.setToolTipText("Zoom Out");
		ZoomOut.setSize(size , size);
		ZoomOut.setLocation(MoveUp.getX() + size + space , MoveUp.getY());
		ZoomOut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				zoomOut();
			}
		});
		add(ZoomOut);

		JButton moveLeft = new JButton();
		moveLeft.setBackground(buttonColor);
		BufferedImage moveLeft1 = null;
		try {
			moveLeft1 = ImageIO.read(new File("res//navLeft.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		moveLeft.setIcon(new ImageIcon(moveLeft1.getSubimage(0 , 0 , moveLeft1.getWidth() , moveLeft1.getHeight() / 2)));
		moveLeft.setRolloverIcon(new ImageIcon(moveLeft1.getSubimage(0 , moveLeft1.getHeight() / 2 , moveLeft1.getWidth() , moveLeft1.getHeight() / 2)));
		moveLeft.setToolTipText("Move Left");
		moveLeft.setSize(size , size);
		moveLeft.setLocation(zoomIn.getX() , zoomIn.getY() + size + space);
		moveLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 1 ; i <= 40 ; i++)
					if (editorPanel.getGameScreen().getMapHandler().getX() >= 5) {
						editorPanel.getGameScreen().getMapHandler().setX(editorPanel.getGameScreen().getMapHandler().getX() - 5);

					}

			}
		});
		add(moveLeft);

		JButton zoomFit = new JButton();
		zoomFit.setBackground(buttonColor);
		BufferedImage zoomFit1 = null;
		try {
			zoomFit1 = ImageIO.read(new File("res//zoomFit.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		zoomFit.setIcon(new ImageIcon(zoomFit1.getSubimage(0 , 0 , zoomFit1.getWidth() , zoomFit1.getHeight() / 2)));
		zoomFit.setRolloverIcon(new ImageIcon(zoomFit1.getSubimage(0 , zoomFit1.getHeight() / 2 , zoomFit1.getWidth() , zoomFit1.getHeight() / 2)));
		zoomFit.setToolTipText("Zoom Fit");
		zoomFit.setSize(size , size);
		zoomFit.setLocation(MoveUp.getX() , moveLeft.getY());
		zoomFit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// if(mapInExtendedSize)
				if (zoomNum > 5) {
					while (zoomNum != 5) {
						zoomOut();
					}
				} else if (zoomNum < 5) {
					while (zoomNum != 5) {
						zoomIn();
					}
				}
			}
		});
		add(zoomFit);

		JButton moveRight = new JButton();
		moveRight.setBackground(buttonColor);
		BufferedImage moveRight1 = null;
		try {
			moveRight1 = ImageIO.read(new File("res//navRight.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		moveRight.setIcon(new ImageIcon(moveRight1.getSubimage(0 , 0 , moveRight1.getWidth() , moveRight1.getHeight() / 2)));
		moveRight.setRolloverIcon(new ImageIcon(moveRight1.getSubimage(0 , moveRight1.getHeight() / 2 , moveRight1.getWidth() , moveRight1.getHeight() / 2)));
		moveRight.setToolTipText("Move Right");
		moveRight.setSize(size , size);
		moveRight.setLocation(ZoomOut.getX() , moveLeft.getY());
		moveRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 1 ; i <= 40 ; i++)
					if (editorPanel.getGameScreen().getMapHandler().getX() < editorPanel.getGameScreen().getMapHandler().getSeasons()[0].getWidth() - editorPanel.getGameScreen().getWidth() - 5) {
						editorPanel.getGameScreen().getMapHandler().setX(editorPanel.getGameScreen().getMapHandler().getX() + 5);

					}

			}
		});
		add(moveRight);
		JButton moveDown = new JButton();
		moveDown.setBackground(buttonColor);
		BufferedImage moveDown1 = null;
		try {
			moveDown1 = ImageIO.read(new File("res//navDown.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		moveDown.setIcon(new ImageIcon(moveDown1.getSubimage(0 , 0 , moveDown1.getWidth() , moveDown1.getHeight() / 2)));
		moveDown.setRolloverIcon(new ImageIcon(moveDown1.getSubimage(0 , moveDown1.getHeight() / 2 , moveDown1.getWidth() , moveDown1.getHeight() / 2)));
		moveDown.setToolTipText("Move Down");
		moveDown.setSize(size , size);
		moveDown.setLocation(MoveUp.getX() , moveRight.getY() + size + space);
		moveDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (int i = 1 ; i <= 40 ; i++)
					if (editorPanel.getGameScreen().getMapHandler().getY() < editorPanel.getGameScreen().getMapHandler().getSeasons()[0].getHeight() - editorPanel.getGameScreen().getHeight() - 5) {
						editorPanel.getGameScreen().getMapHandler().setY(editorPanel.getGameScreen().getMapHandler().getY() + 5);

					}
			}
		});
		add(moveDown);

		final JButton addRow = new JButton();
		addRow.setBackground(buttonColor);
		BufferedImage addRow1 = null;
		try {
			addRow1 = ImageIO.read(new File("res//addRow.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		addRow.setIcon(new ImageIcon(addRow1.getSubimage(0 , 0 , addRow1.getWidth() , addRow1.getHeight() / 2)));
		addRow.setRolloverIcon(new ImageIcon(addRow1.getSubimage(0 , addRow1.getHeight() / 2 , addRow1.getWidth() , addRow1.getHeight() / 2)));
		addRow.setToolTipText("Add Row");
		addRow.setSize(size , size);
		addRow.setLocation(newMap.getX() , (int) (moveDown.getY() + size + (space * 1.5)));
		addRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addRow();
			}
		});
		add(addRow);
		JButton removeRow = new JButton();
		removeRow.setBackground(buttonColor);
		BufferedImage removeRow1 = null;
		try {
			removeRow1 = ImageIO.read(new File("res//removeRow.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		removeRow.setIcon(new ImageIcon(removeRow1.getSubimage(0 , 0 , removeRow1.getWidth() , removeRow1.getHeight() / 2)));
		removeRow.setRolloverIcon(new ImageIcon(removeRow1.getSubimage(0 , removeRow1.getHeight() / 2 , removeRow1.getWidth() , removeRow1.getHeight() / 2)));
		removeRow.setToolTipText("Remove Row");
		removeRow.setSize(size , size);
		removeRow.setLocation(clearMap.getX() , addRow.getY());
		removeRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeLastRow();
			}
		});
		add(removeRow);

		JButton addCol = new JButton();
		addCol.setBackground(buttonColor);
		BufferedImage addCol1 = null;
		try {
			addCol1 = ImageIO.read(new File("res//addColumn.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		addCol.setIcon(new ImageIcon(addCol1.getSubimage(0 , 0 , addCol1.getWidth() , addCol1.getHeight() / 2)));
		addCol.setRolloverIcon(new ImageIcon(addCol1.getSubimage(0 , addCol1.getHeight() / 2 , addCol1.getWidth() , addCol1.getHeight() / 2)));
		addCol.setToolTipText("Add Column");
		addCol.setSize(size , size);
		addCol.setLocation(addRow.getX() , addRow.getY() + size + space);
		addCol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addCol();
			}
		});
		add(addCol);

		JButton removeCol = new JButton();
		removeCol.setBackground(buttonColor);
		BufferedImage removeCol1 = null;
		try {
			removeCol1 = ImageIO.read(new File("res//removeColumn.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		removeCol.setIcon(new ImageIcon(removeCol1.getSubimage(0 , 0 , removeCol1.getWidth() , removeCol1.getHeight() / 2)));
		removeCol.setRolloverIcon(new ImageIcon(removeCol1.getSubimage(0 , removeCol1.getHeight() / 2 , removeCol1.getWidth() , removeCol1.getHeight() / 2)));
		removeCol.setToolTipText("Remove Column");
		removeCol.setSize(size , size);
		removeCol.setLocation(removeRow.getX() , addCol.getY());
		removeCol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeLastCol();
			}
		});
		add(removeCol);

		JButton undo = new JButton();
		undo.setBackground(buttonColor);
		BufferedImage undo1 = null;
		try {
			undo1 = ImageIO.read(new File("res//navLeft.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		undo.setIcon(new ImageIcon(undo1.getSubimage(0 , 0 , undo1.getWidth() , undo1.getHeight() / 2)));
		undo.setRolloverIcon(new ImageIcon(undo1.getSubimage(0 , undo1.getHeight() / 2 , undo1.getWidth() , undo1.getHeight() / 2)));
		undo.setToolTipText("Undo");
		undo.setSize(size , size);
		undo.setLocation(newMap.getX() , (int) (addCol.getY() + size + (space * 1.5)));
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mapHandler.drawAgainU();
				undo();
			}
		});
		add(undo);

		JButton redo = new JButton();
		redo.setBackground(buttonColor);
		BufferedImage redo1 = null;
		try {
			redo1 = ImageIO.read(new File("res//navRight.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		redo.setIcon(new ImageIcon(redo1.getSubimage(0 , 0 , redo1.getWidth() , redo1.getHeight() / 2)));
		redo.setRolloverIcon(new ImageIcon(redo1.getSubimage(0 , redo1.getHeight() / 2 , redo1.getWidth() , redo1.getHeight() / 2)));
		redo.setToolTipText("redo");
		redo.setSize(size , size);
		redo.setLocation(clearMap.getX() , undo.getY());
		redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mapHandler.drawAgainR();
				redo();
			}
		});
		add(redo);

		JButton saveMap = new JButton();
		saveMap.setBackground(buttonColor);
		BufferedImage saveMap1 = null;
		try {
			saveMap1 = ImageIO.read(new File("res//saveArray.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		saveMap.setIcon(new ImageIcon(saveMap1.getSubimage(0 , 0 , saveMap1.getWidth() , saveMap1.getHeight() / 2)));
		saveMap.setRolloverIcon(new ImageIcon(saveMap1.getSubimage(0 , saveMap1.getHeight() / 2 , saveMap1.getWidth() , saveMap1.getHeight() / 2)));
		saveMap.setToolTipText("Save Map");
		saveMap.setSize(size , size);
		saveMap.setLocation(newMap.getX() , undo.getY() + size + space);
		saveMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browser.saveMap();
			}
		});
		add(saveMap);

		JButton openMap = new JButton();// same as load map
		openMap.setBackground(buttonColor);
		BufferedImage OpenMap1 = null;
		try {
			OpenMap1 = ImageIO.read(new File("res//openArray.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		openMap.setIcon(new ImageIcon(OpenMap1.getSubimage(0 , 0 , OpenMap1.getWidth() , OpenMap1.getHeight() / 2)));
		openMap.setRolloverIcon(new ImageIcon(OpenMap1.getSubimage(0 , OpenMap1.getHeight() / 2 , OpenMap1.getWidth() , OpenMap1.getHeight() / 2)));
		openMap.setToolTipText("Open Map");
		openMap.setSize(size , size);
		openMap.setLocation(clearMap.getX() , saveMap.getY());
		openMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browser.openMap();
			}
		});
		add(openMap);

		JButton about = new JButton();
		about.setBackground(buttonColor);
		BufferedImage about1 = null;
		try {
			about1 = ImageIO.read(new File("res//help.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		about.setIcon(new ImageIcon(about1.getSubimage(0 , 0 , about1.getWidth() , about1.getHeight() / 2)));
		about.setRolloverIcon(new ImageIcon(about1.getSubimage(0 , about1.getHeight() / 2 , about1.getWidth() , about1.getHeight() / 2)));
		about.setToolTipText("About");
		about.setSize(size , size);
		about.setLocation(space / 4 , (int) (saveMap.getY() + size + (space * 1.5)));
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LookAndFeel previousLF = UIManager.getLookAndFeel();
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					browser.help();
					UIManager.setLookAndFeel(previousLF);
				} catch (ClassNotFoundException exception) {
					exception.printStackTrace();
				} catch (InstantiationException exception) {
					exception.printStackTrace();
				} catch (IllegalAccessException exception) {
					exception.printStackTrace();
				} catch (UnsupportedLookAndFeelException exception) {
					exception.printStackTrace();
				}
			}
		});
		add(about);

		JButton preview = new JButton();
		preview.setBackground(buttonColor);
		BufferedImage preview1 = null;
		try {
			preview1 = ImageIO.read(new File("res//dragTool.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		preview.setIcon(new ImageIcon(preview1.getSubimage(0 , 0 , preview1.getWidth() , preview1.getHeight() / 2)));
		preview.setRolloverIcon(new ImageIcon(preview1.getSubimage(0 , preview1.getHeight() / 2 , preview1.getWidth() , preview1.getHeight() / 2)));
		preview.setToolTipText("Preview");
		preview.setSize(size , size);
		preview.setLocation(about.getX() + size + space , about.getY());
		preview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabs.setSelectedIndex(2);
			}
		});
		add(preview);

		JButton returnToGame = new JButton();
		returnToGame.setBackground(buttonColor);
		BufferedImage returnToGame1 = null;
		try {
			returnToGame1 = ImageIO.read(new File("res//navleft.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		returnToGame.setIcon(new ImageIcon(returnToGame1.getSubimage(0 , 0 , returnToGame1.getWidth() , returnToGame1.getHeight() / 2)));
		returnToGame.setRolloverIcon(new ImageIcon(returnToGame1.getSubimage(0 , returnToGame1.getHeight() / 2 , returnToGame1.getWidth() , returnToGame1.getHeight() / 2)));
		returnToGame.setToolTipText("Return To Game");
		returnToGame.setSize(size , size);
		returnToGame.setLocation(preview.getX() + size + space , preview.getY());
		returnToGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabs.setSelectedIndex(0);
			}
		});
		add(returnToGame);
		JButton exit = new JButton();
		exit.setBackground(buttonColor);
		BufferedImage exit1 = null;
		try {
			exit1 = ImageIO.read(new File("res//exit.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		exit.setIcon(new ImageIcon(exit1.getSubimage(0 , 0 , exit1.getWidth() , exit1.getHeight() / 2)));
		exit.setRolloverIcon(new ImageIcon(exit1.getSubimage(0 , exit1.getHeight() / 2 , exit1.getWidth() , exit1.getHeight() / 2)));
		exit.setToolTipText("Exit");
		exit.setSize(size , size);
		exit.setLocation(returnToGame.getX() + size + space , returnToGame.getY());
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LookAndFeel previousLF = UIManager.getLookAndFeel();
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					browser.exit(mapHandler.getCurrentMap());
					UIManager.setLookAndFeel(previousLF);
				} catch (ClassNotFoundException exception) {
					exception.printStackTrace();
				} catch (InstantiationException exception) {
					exception.printStackTrace();
				} catch (IllegalAccessException exception) {
					exception.printStackTrace();
				} catch (UnsupportedLookAndFeelException exception) {
					exception.printStackTrace();
				}
			}
		});
		add(exit);
	}

	public void zoomIn() {
		canZoomOut = true;
		if (canZoomIn) {
			zoomNum++;
			if (zoomNum == 7)
				canZoomIn = false;
			if (mapInExtendedSize) {
				setNormalSize();
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			decreaseDimension();
			updateSquareCenterZoomIn();

		}
	}

	private void updateSquareCenterZoomIn() {
		mapHandler.updateSquareCenterZoomIn();
	}

	private void decreaseDimension() {
		mapHandler.setWidth(mapHandler.getWidth() * 5 / 6);
		mapHandler.setHeight(mapHandler.getHeight() * 5 / 6);
	}

	private boolean getValidZoomOut() {
		if (mapHandler.getWidth() < (editorPanel.getGameScreen().getMapHandler().getSeasons()[0].getWidth() * 5.0 / 6.0 * 5.0 / 6.0) && mapHandler.getHeight() < (editorPanel.getGameScreen().getMapHandler().getSeasons()[0].getHeight() * 5.0 / 6.0 * 5.0 / 6.0)) {
			return true;
		}
		return false;
	}

	private boolean thisIsLastZoomOut() {
		if (!getValidZoomOut()) {
			canZoomOut = false;
			return true;
		}

		if (zoomNum == -Integer.MAX_VALUE) {
			canZoomOut = false;
			return true;

		}
		return false;
	}

	public void zoomOut() {
		canZoomIn = true;
		if (!canZoomOut)
			return;
		zoomNum--;
		if (thisIsLastZoomOut()) {
			setExtendedSize();
		}
		if (increaseDimension())
			updateSquareCenterZoomOut();
		editorPanel.getGameScreen().getMapHandler().clamp();

	}

	private boolean increaseDimension() {
		if (mapHandler.getWidth() * 6 / 5 <= mapHandler.getSeasons()[0].getWidth() && mapHandler.getHeight() * 6 / 5 <= mapHandler.getSeasons()[0].getHeight()) {
			mapHandler.setWidth(mapHandler.getWidth() * 6 / 5);
			mapHandler.setHeight(mapHandler.getHeight() * 6 / 5);
			return true;
		}
		return false;
	}

	private void updateSquareCenterZoomOut() {
		mapHandler.updateSquareCenterZoomOut();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		BufferedImage userInterface = new BufferedImage(getWidth() , getHeight() , BufferedImage.TYPE_INT_RGB);
		try {
			userInterface = ImageIO.read(new File("res/myUI.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(userInterface , 0 , 0 , getWidth() , getHeight() , null);
	}

	private void setNormalSize() {
		mapInExtendedSize = false;
		editorPanel.getDownPanel().setVisible(true);
		editorPanel.getGameScreen().setSize(editorPanel.getGameScreen().getWidth() , editorPanel.getGameScreen().getHeight() - staBlockSize * 2);
	}

	private void setExtendedSize() {
		mapInExtendedSize = true;

		editorPanel.getDownPanel().setVisible(false);
		editorPanel.getGameScreen().setSize(editorPanel.getGameScreen().getWidth() , editorPanel.getGameScreen().getHeight() + editorPanel.getDownPanel().getHeight());
	}

	public EditorPanel getEditorPanel() {
		return editorPanel;
	}

	public void setEditorPanel(EditorPanel editorPanel) {
		this.editorPanel = editorPanel;
	}

	public int getZoomNum() {
		return zoomNum;
	}

	public void setZoomNum(int zoomNum) {
		this.zoomNum = zoomNum;
	}

	public boolean isMapInExtendedSize() {
		return mapInExtendedSize;
	}

	public void setMapInExtendedSize(boolean mapInExtendedSize) {
		this.mapInExtendedSize = mapInExtendedSize;
	}

	public void addCol() {
		addColsToEntireMap();
		browser.setSavedState(false);
		mapHandler.addCol();
	}

	private BufferedImage makeEmptyCol(boolean isWinter) {
		BufferedImage colImg = new BufferedImage((staBlockSize * 1) , (mapHandler.getRows() * staBlockSize) , BufferedImage.TYPE_INT_RGB);
		Graphics g = colImg.createGraphics();
		for (int j = 1 ; j <= 1 ; j++) {
			for (int row = 0 ; row < mapHandler.getRows() ; row++) {
				if (!isWinter)
					g.drawImage(mapHandler.getImages().get(0).get(0) , staBlockSize * (j - 1) , (int) (mapHandler.getStaBlockSize() * row) , null);
				else
					g.drawImage(mapHandler.getImages().get(0).get(1) , staBlockSize * (j - 1) , (int) (mapHandler.getStaBlockSize() * row) , null);
			}
		}
		return colImg;
	}

	void addColsToEntireMap() {
		BufferedImage temp = new BufferedImage(((mapHandler.getCols() + 1) * staBlockSize) , (mapHandler.getRows()) * staBlockSize , BufferedImage.TYPE_INT_RGB);
		mapHandler.getSeasons()[0].createGraphics().drawImage(mapHandler.getSeasons()[0] , 0 , 0 , null);
		mapHandler.getSeasons()[0].createGraphics().drawImage(makeEmptyCol(false) , mapHandler.getSeasons()[0].getWidth() , 0 , null);
		mapHandler.setEntireMap(temp);

		BufferedImage temp1 = new BufferedImage(((mapHandler.getCols() + 1) * staBlockSize) , (mapHandler.getRows()) * staBlockSize , BufferedImage.TYPE_INT_RGB);
		mapHandler.getSeasons()[1].createGraphics().drawImage(mapHandler.getSeasons()[1] , 0 , 0 , null);
		mapHandler.getSeasons()[1].createGraphics().drawImage(makeEmptyCol(false) , mapHandler.getSeasons()[1].getWidth() , 0 , null);
		mapHandler.setSummer(temp1);

		BufferedImage temp2 = new BufferedImage(((mapHandler.getCols() + 1) * staBlockSize) , (mapHandler.getRows()) * staBlockSize , BufferedImage.TYPE_INT_RGB);
		mapHandler.getSeasons()[2].createGraphics().drawImage(mapHandler.getSeasons()[2] , 0 , 0 , null);
		mapHandler.getSeasons()[2].createGraphics().drawImage(makeEmptyCol(false) , mapHandler.getSeasons()[2].getWidth() , 0 , null);
		mapHandler.setFall(temp2);

		BufferedImage temp3 = new BufferedImage(((mapHandler.getCols() + 1) * staBlockSize) , (mapHandler.getRows()) * staBlockSize , BufferedImage.TYPE_INT_RGB);
		mapHandler.getSeasons()[1].createGraphics().drawImage(mapHandler.getSeasons()[3] , 0 , 0 , null);
		mapHandler.getSeasons()[3].createGraphics().drawImage(makeEmptyCol(true) , mapHandler.getSeasons()[3].getWidth() , 0 , null);
		mapHandler.setWinter(temp3);
	}

	public void addRowsToEntireMap() {
		BufferedImage temp = new BufferedImage((mapHandler.getCols() * staBlockSize) , (mapHandler.getRows() + 1) * staBlockSize , BufferedImage.TYPE_INT_RGB);
		mapHandler.getSeasons()[0].createGraphics().drawImage(mapHandler.getSeasons()[0] , 0 , 0 , null);
		mapHandler.getSeasons()[0].createGraphics().drawImage(makeEmptyRow(false) , 0 , mapHandler.getSeasons()[0].getHeight() , null);
		mapHandler.setEntireMap(temp);

		BufferedImage temp1 = new BufferedImage((mapHandler.getCols() * staBlockSize) , (mapHandler.getRows() + 1) * staBlockSize , BufferedImage.TYPE_INT_RGB);
		mapHandler.getSeasons()[1].createGraphics().drawImage(mapHandler.getSeasons()[1] , 0 , 0 , null);
		mapHandler.getSeasons()[1].createGraphics().drawImage(makeEmptyRow(false) , 0 , mapHandler.getSeasons()[1].getHeight() , null);
		mapHandler.setSummer(temp1);

		BufferedImage temp2 = new BufferedImage((mapHandler.getCols() * staBlockSize) , (mapHandler.getRows() + 1) * staBlockSize , BufferedImage.TYPE_INT_RGB);
		mapHandler.getSeasons()[2].createGraphics().drawImage(mapHandler.getSeasons()[2] , 0 , 0 , null);
		mapHandler.getSeasons()[2].createGraphics().drawImage(makeEmptyRow(false) , 0 , mapHandler.getSeasons()[2].getHeight() , null);
		mapHandler.setFall(temp2);

		BufferedImage temp3 = new BufferedImage((mapHandler.getCols() * staBlockSize) , (mapHandler.getRows() + 1) * staBlockSize , BufferedImage.TYPE_INT_RGB);
		mapHandler.getSeasons()[3].createGraphics().drawImage(mapHandler.getSeasons()[3] , 0 , 0 , null);
		mapHandler.getSeasons()[3].createGraphics().drawImage(makeEmptyRow(true) , 0 , mapHandler.getSeasons()[3].getHeight() , null);
		mapHandler.setSummer(temp3);
	}

	private BufferedImage makeEmptyRow(boolean isWinter) {
		BufferedImage rowImg = new BufferedImage((mapHandler.getCols() * staBlockSize) , (staBlockSize * 1) , BufferedImage.TYPE_INT_RGB);
		Graphics g = rowImg.createGraphics();
		for (int i = 1 ; i <= 1 ; i++) {
			for (int col = 0 ; col < mapHandler.getCols() ; col++) {
				if (!isWinter)
					g.drawImage(mapHandler.getImages().get(0).get(0) , (int) (mapHandler.getStaBlockSize() * col) , staBlockSize * (i - 1) , null);
				else
					g.drawImage(mapHandler.getImages().get(0).get(1) , (int) (mapHandler.getStaBlockSize() * col) , staBlockSize * (i - 1) , null);
			}
		}
		return rowImg;
	}

	public void addRow() {
		addRowsToEntireMap();
		browser.setSavedState(false);
		mapHandler.addRow();
	}

	public void removeLastRow() {
		browser.setSavedState(false);
		mapHandler.removeLastRow();
	}

	public void removeLastCol() {
		browser.setSavedState(false);
		mapHandler.removeLastCol();
	}

	public void setRowsAndCols(int rows , int cols , boolean clear) {
		browser.setSavedState(false);
		mapHandler.setRowsAndCols(rows , cols , clear);
	}

	public void newMap() {
		resetMap();
	}

	public void resetMap() {
		browser.setSavedState(false);
		browser.changeArrayProperties(true);
	}

	public void deleteMap() {
		clearMap();
	}

	public void clearMap() {
		browser.setSavedState(false);
		mapHandler.clearMap();
	}

	public void redo() {
		browser.setSavedState(false);
		mapHandler.redo();
	}

	public void undo() {
		browser.setSavedState(false);
		mapHandler.undo();
	}

	public FileBrowser getBrowser() {
		return browser;
	}
}
