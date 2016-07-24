package com.teamname.finalproject;

import com.teamname.finalproject.editor.EditorPanel;
import com.teamname.finalproject.editor.preview.PreviewPanel;
import com.teamname.finalproject.game.GameTab;
import com.teamname.finalproject.mainmenu.MainMenuState;
import com.teamname.finalproject.mainmenu.MainMenuTab;
import com.teamname.finalproject.util.Messages;
import com.teamname.finalproject.util.SoundEffect;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by Ali J on 5/13/2015.
 */
public class Tabs extends JTabbedPane {

	private EditorPanel editorTab;
	private PreviewPanel previewTab;
	private GameTab gameTab;
	private MainMenuTab mainMenuTab;

	public Tabs() {
		gameTab = new GameTab();
		editorTab = new EditorPanel(this);
		previewTab = new PreviewPanel(editorTab.getGameScreen().getMapHandler().getMap() , this);
		mainMenuTab = new MainMenuTab(this);
		addTab("Main Menu" , null , mainMenuTab , null);
		addTab("Map Editor" , null , editorTab , null);
		addTab("Preview" , null , previewTab , null);
		addTab("Game" , null , gameTab , null);

		this.setModel(new DefaultSingleSelectionModel() {
			public void setSelectedIndex(int index) {
				if (index == 0) {
					mainMenuTab.getMenuCanvas().start();
					editorTab.getGameScreen().pause();
					previewTab.getPreview().pause();
					gameTab.getGame().pause();
				} else if (index == 1) {
					editorTab.getGameScreen().start();
					mainMenuTab.getMenuCanvas().pause();
					previewTab.getPreview().pause();
					gameTab.getGame().pause();
				} else if (index == 2) {
					previewTab.getPreview().start(editorTab.getGameScreen().getMapHandler().copyImages());
					mainMenuTab.getMenuCanvas().pause();
					editorTab.getGameScreen().pause();
					gameTab.getGame().pause();
                } else if (index == 3) {
					gameTab.getGame().start();
					mainMenuTab.getMenuCanvas().pause();
					editorTab.getGameScreen().pause();
					previewTab.getPreview().pause();
				}
				super.setSelectedIndex(index);
			}
		});
		addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				if (index == 0) {
					mainMenuTab.getMenuCanvas().setMenuState(MainMenuState.MAIN_MENU_STATE);
					mainMenuTab.getMenuCanvas().setBackGroundFrame(0);
					mainMenuTab.getMenuCanvas().setSinglePlayerBackGroundFrame(0);
					SoundEffect.HARMONY.pause();
                    SoundEffect.PREVIEW.pause();
                    SoundEffect.GAME.pause();
                    SoundEffect.MAINMENU.play();
                } else if (index == 1) {
					SoundEffect.MAINMENU.pause();
                    SoundEffect.PREVIEW.pause();
                    SoundEffect.GAME.pause();
                    SoundEffect.HARMONY.play();
                } else if (index == 2) {
                    SoundEffect.HARMONY.pause();
                    SoundEffect.MAINMENU.pause();
                    SoundEffect.GAME.pause();
                    SoundEffect.PREVIEW.play();
                }else if (index == 3){
                    SoundEffect.PREVIEW.pause();
                    SoundEffect.HARMONY.pause();
                    SoundEffect.MAINMENU.pause();
                    SoundEffect.GAME.play();
                }
			}
		});
	}

	public synchronized void stopEverything() {
		mainMenuTab.getMenuCanvas().stop();
		editorTab.getGameScreen().stop();
		previewTab.getPreview().stop();
	}

    @Override
    protected void processEvent(AWTEvent e) {
        if (e.getID() == Messages.LOSE_GAME_MESSAGE){
            LookAndFeel previousLF = UIManager.getLookAndFeel();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                JOptionPane.showMessageDialog(null,"YOU LOSE!!!");
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
            setSelectedIndex(0);
        }else if (e.getID() == Messages.LOSE_GAME_MESSAGE){
            LookAndFeel previousLF = UIManager.getLookAndFeel();
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                JOptionPane.showMessageDialog(null,"YOU WON!!!");
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
            setSelectedIndex(0);
        }
        super.processEvent(e);
    }

    public EditorPanel getEditorTab() {
		return editorTab;
	}

	public void setEditorTab(EditorPanel editorTab) {
		this.editorTab = editorTab;
	}
}
