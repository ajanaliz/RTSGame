package com.teamname.finalproject.editor.preview;

import com.teamname.finalproject.Tabs;

import java.awt.event.*;

/**
 * Created by Ali J on 5/15/2015.
 */
public class PreviewEventListener implements KeyListener , MouseMotionListener , MouseWheelListener , MouseListener {

	private Preview preview;
    private Tabs tabs;

	public PreviewEventListener(Preview preview, Tabs tabs) {
        // for function calls
		this.preview = preview;
	    this.tabs = tabs;
        //add listeners
		preview.addKeyListener(this);
		preview.addMouseListener(this);
		preview.addMouseMotionListener(this);
		preview.addMouseWheelListener(this);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_Q) {
			// zoomout
            preview.zoomOut();
		}
		if (e.getKeyCode() == KeyEvent.VK_E) {
            preview.zoomIn();
			// zoomin
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
		if (e.getKeyCode() == KeyEvent.VK_PLUS) {
			// seasonDelay - 1000 ----> increase speed
		}
		if (e.getKeyCode() == KeyEvent.VK_MINUS) {
			// seasonDelay + 1000 ---> decrease speed
		}
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            tabs.setSelectedIndex(1);
        }
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

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

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public synchronized void mouseWheelMoved(MouseWheelEvent e) {
		int notches = e.getWheelRotation();
		if (notches >= 0) {
            preview.zoomOut();
			// zoomout
		} else {
            preview.zoomIn();
			// zoomin
		}
	}
}
