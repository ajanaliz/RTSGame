package com.teamname.finalproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Ali J on 5/8/2015.
 */
public class Window extends JFrame {

    private static Tabs tabs;

    private static Dimension localDimension;

    private static double blockSize;

	public Window() {
		localDimension = Toolkit.getDefaultToolkit().getScreenSize();
	    blockSize = (int)(localDimension.getWidth() / 20);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setUndecorated(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setResizable(false);
		setLayout(null);
		tabs = new Tabs();
		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		tabs.setBounds(0 , 0 , (int) localDimension.getWidth() , (int) localDimension.getHeight());
		getContentPane().add(tabs);
		setFocusable(true);
		requestFocus();
		exit();
		setVisible(true);
	}

	private void exit() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
                //have to make the instance of the Game class here so we can Control the Users connection/disconnection
//                DisconnectPacket packet = new DisconnectPacket(this.game.getUser().getPlayerID());
//                packet.writeData(this.game.getSocketClient());
				if (!tabs.getEditorTab().getBrowser().isSaved()) {
					LookAndFeel previousLF = UIManager.getLookAndFeel();
					try {
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        if (tabs.getSelectedIndex() == 1) {
                            if (JOptionPane.showConfirmDialog(null, "Would you like to save first?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                                tabs.getEditorTab().getBrowser().quickSave(false);
                                System.exit(0);
                            }
                        }else{
                            if (JOptionPane.showConfirmDialog(null, "dont you want to save the map you designed?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                                tabs.getEditorTab().getBrowser().quickSave(false);
                                System.exit(0);
                            }
                        }
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
				} else
					System.exit(0);
			}
		});
	}

    public static Dimension getLocalDimension() {
        return localDimension;
    }

    public static double getBlockSize() {
        return blockSize;
    }

    public static Tabs getTabs() {
        return tabs;
    }

    public static void setTabs(Tabs tabs) {
        Window.tabs = tabs;
    }

}
