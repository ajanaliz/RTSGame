package com.teamname.finalproject.editor;

import com.teamname.finalproject.util.Listeners;

import java.awt.event.*;

public class EventListener implements Listeners {

	private GameScreen gui;
	private boolean isCtrlDown;
	private ButtonsPanel buttonsPanel;

	public EventListener(GameScreen gui , ButtonsPanel buttonsPanel) {
		this.gui = gui;
		this.buttonsPanel = buttonsPanel;
		gui.addKeyListener(this);
		gui.addMouseMotionListener(this);
		gui.addMouseListener(this);
		gui.addMouseWheelListener(this);
		isCtrlDown = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_Q) {
			// zoomout
			buttonsPanel.zoomOut();
		}
		if (e.getKeyCode() == KeyEvent.VK_E) {
			// zoomin
			buttonsPanel.zoomIn();
		}
		if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
			// move up -- > nav up
		}
		if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
			// move right ---> nav right
		}
		if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
			// move down --> nav down
		}
		if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
			// move left -- > nav left
		}
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			isCtrlDown = !isCtrlDown;
		}
		if (e.getKeyCode() == KeyEvent.VK_S && isCtrlDown) {
			// save as the map
			buttonsPanel.getBrowser().saveMap();
		}
		if (e.getKeyCode() == KeyEvent.VK_O && isCtrlDown) {
			// open/load map
			buttonsPanel.getBrowser().openMap();
		}
		if (e.getKeyCode() == KeyEvent.VK_Z && isCtrlDown) {
			// undo
			gui.getMapHandler().drawAgainU();
			buttonsPanel.undo();
		}
		if (e.getKeyCode() == KeyEvent.VK_Y && isCtrlDown) {
			// redo
			gui.getMapHandler().drawAgainR();
			buttonsPanel.redo();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			isCtrlDown = !isCtrlDown;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!buttonsPanel.isMapInExtendedSize()) {
			gui.getMapHandler().setCol((int) (gui.getMapHandler().getX() / gui.getMapHandler().getStaBlockSize() + e.getX() * gui.getMapHandler().getWidth() / gui.getMapHandler().getStaWidth() / gui.getMapHandler().getStaBlockSize()));
			gui.getMapHandler().setRow((int) (gui.getMapHandler().getY() / gui.getMapHandler().getStaBlockSize() + e.getY() * gui.getMapHandler().getHeight() / gui.getMapHandler().getStaHeight() / gui.getMapHandler().getStaBlockSize()));
			int mycol = gui.getMapHandler().getCol();
			int myrow = gui.getMapHandler().getRow();
			if (gui.getId() == 1 || gui.getId() == 2 || gui.getId() == 0) {
				if (!(gui.getId() == gui.getMapHandler().getMap().getCurrentMap().get(myrow).get(mycol).getType() && gui.getMapHandler().getMap().getCurrentMap().get(myrow).get(mycol).getImage() == -1)) {
					gui.getMapHandler().saveMap();
					gui.getMapHandler().getMap().getCurrentMap().get(myrow).get(mycol).setImage(-1);
					gui.getMapHandler().setTile(myrow , mycol , gui.getId());
					if (myrow > 0) {
						gui.getMapHandler().setTile(myrow - 1 , mycol , gui.getMapHandler().getMap().getCurrentMap().get(myrow - 1).get(mycol).getType());
					}

					if (gui.getMapHandler().getMap().getRows() - 1 > myrow) {
						gui.getMapHandler().setTile(myrow + 1 , mycol , gui.getMapHandler().getMap().getCurrentMap().get(myrow + 1).get(mycol).getType());
					}

					if (gui.getMapHandler().getMap().getCols() - 1 > mycol) {
						gui.getMapHandler().setTile(myrow , mycol + 1 , gui.getMapHandler().getMap().getCurrentMap().get(myrow).get(mycol + 1).getType());
					}

					if (mycol > 0) {
						gui.getMapHandler().setTile(myrow , mycol - 1 , gui.getMapHandler().getMap().getCurrentMap().get(myrow).get(mycol - 1).getType());
					}

				}
			} else {
				if (!(gui.getMapHandler().getMap().getCurrentMap().get(myrow).get(mycol).getImage() == gui.getId())) {
					gui.getMapHandler().saveMap();
					gui.getMapHandler().getMap().setIcon(myrow , mycol , gui.getId());
				}
			}

		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!buttonsPanel.isMapInExtendedSize()) {
			gui.getMapHandler().setCol((int) (gui.getMapHandler().getX() / gui.getMapHandler().getStaBlockSize() + e.getX() * gui.getMapHandler().getWidth() / gui.getMapHandler().getStaWidth() / gui.getMapHandler().getStaBlockSize()));
			gui.getMapHandler().setRow((int) (gui.getMapHandler().getY() / gui.getMapHandler().getStaBlockSize() + e.getY() * gui.getMapHandler().getHeight() / gui.getMapHandler().getStaHeight() / gui.getMapHandler().getStaBlockSize()));
			int mycol = gui.getMapHandler().getCol();
			int myrow = gui.getMapHandler().getRow();
			if (gui.getId() == 1 || gui.getId() == 2 || gui.getId() == 0) {
				if (!(gui.getId() == gui.getMapHandler().getMap().getCurrentMap().get(myrow).get(mycol).getType() && gui.getMapHandler().getMap().getCurrentMap().get(myrow).get(mycol).getImage() == -1)) {
					gui.getMapHandler().saveMap();
					gui.getMapHandler().getMap().getCurrentMap().get(myrow).get(mycol).setImage(-1);
					gui.getMapHandler().setTile(myrow , mycol , gui.getId());
					if (myrow > 0) {
						gui.getMapHandler().setTile(myrow - 1 , mycol , gui.getMapHandler().getMap().getCurrentMap().get(myrow - 1).get(mycol).getType());
					}

					if (gui.getMapHandler().getMap().getRows() - 1 > myrow) {
						gui.getMapHandler().setTile(myrow + 1 , mycol , gui.getMapHandler().getMap().getCurrentMap().get(myrow + 1).get(mycol).getType());
					}

					if (gui.getMapHandler().getMap().getCols() - 1 > mycol) {
						gui.getMapHandler().setTile(myrow , mycol + 1 , gui.getMapHandler().getMap().getCurrentMap().get(myrow).get(mycol + 1).getType());
					}

					if (mycol > 0) {
						gui.getMapHandler().setTile(myrow , mycol - 1 , gui.getMapHandler().getMap().getCurrentMap().get(myrow).get(mycol - 1).getType());
					}

				}
			} else {
				if (!(gui.getMapHandler().getMap().getCurrentMap().get(myrow).get(mycol).getImage() == gui.getId())) {
					gui.getMapHandler().saveMap();
					gui.getMapHandler().getMap().setIcon(myrow , mycol , gui.getId());
				}
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int notches = e.getWheelRotation();
		if (notches >= 0) {
			// zoomout
			buttonsPanel.zoomOut();
		} else {
			// zoomin
			buttonsPanel.zoomIn();
		}
	}
}