package com.teamname.finalproject.mainmenu;

import com.teamname.finalproject.*;
import com.teamname.finalproject.editor.EditorPanel;
import com.teamname.finalproject.editor.FileBrowser;
import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.GameTab;
import com.teamname.finalproject.game.PlayerID;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.entities.KingdomMP;
import com.teamname.finalproject.game.network.GameServer;
import com.teamname.finalproject.game.network.packets.LoginPacket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

//import com.teamname.finalproject.game.gameobject.entities.KingdomMP;
//import com.teamname.finalproject.game.network.packets.MapConfirmationPacket;
//import com.teamname.finalproject.game.network.packets.MapInitPacket;
//import com.teamname.finalproject.game.network.packets.TilePacket;

/**
 * Created by Ali J on 5/16/2015.
 */
public class MouseInput implements MouseListener {
    private MainMenuCanvas menuCanvas;
    private int standardGap;
    private int standardButtonX;
    private int standardButtonY;
    private int returnButtonX;
    private int returnButtonY;
    private Rectangle singlePlayer;
    private Rectangle multiPlayer;
    private Rectangle mapEditor;
    private Rectangle options;
    private Rectangle aboutUs;
    private Rectangle exit;
    private Rectangle aboutUsReturn;
    private Dimension dimension;
    private Tabs tabs;
    private Rectangle joinGame;
    private Rectangle back;
    private Rectangle hostGame;
    private Dimension buttonDimension;
    private Rectangle loadInHostGame;
    private Rectangle loadInSP;
    private Rectangle startServer;
    private Rectangle startGame;
    private KingdomMP player;
    private FileBrowser browser;


    public MouseInput(MainMenuCanvas menuCanvas, Tabs tabs) {
        dimension = Toolkit.getDefaultToolkit().getScreenSize();
        browser = EditorPanel.getBrowser();
        buttonDimension = new Dimension((int) dimension.getWidth() / 9, (int) dimension.getHeight() / 17);
        this.tabs = tabs;
        standardButtonX = (int) dimension.getWidth() / 2;
        standardButtonY = (int) dimension.getHeight() / 2;
        standardGap = (int) (dimension.getHeight() / 15);
        returnButtonX = (int) dimension.getHeight() / 15 * 3;
        returnButtonY = (int) dimension.getHeight() - (int) dimension.getHeight() / 15 * 2;
        singlePlayer = new Rectangle(standardButtonX - (int) dimension.getWidth() / 10, standardButtonY - 5 * standardGap, (int) dimension.getWidth() / 5, (int) dimension.getHeight() / 28);
        multiPlayer = new Rectangle(standardButtonX - (int) dimension.getWidth() / 10, standardButtonY - 3 * standardGap, (int) dimension.getWidth() / 5, (int) dimension.getHeight() / 28);
        mapEditor = new Rectangle(standardButtonX - (int) dimension.getWidth() / 10, standardButtonY - standardGap, (int) dimension.getWidth() / 5, (int) dimension.getHeight() / 28);
        options = new Rectangle(standardButtonX - (int) dimension.getWidth() / 10, standardButtonY + standardGap, (int) dimension.getWidth() / 5, (int) dimension.getHeight() / 28);
        aboutUs = new Rectangle(standardButtonX - (int) dimension.getWidth() / 10, standardButtonY + 3 * standardGap, (int) dimension.getWidth() / 5, (int) dimension.getHeight() / 28);
        exit = new Rectangle(standardButtonX - (int) dimension.getWidth() / 10, standardButtonY + 5 * standardGap, (int) dimension.getWidth() / 5, (int) dimension.getHeight() / 28);
        aboutUsReturn = new Rectangle(returnButtonX, returnButtonY, 150, 35);
        this.menuCanvas = menuCanvas;
        menuCanvas.addMouseListener(this);
        hostGame = new Rectangle((int) (standardButtonX - standardGap * 2.5), standardButtonY - standardGap * 5, (int) buttonDimension.getWidth() + standardGap * 2, (int) buttonDimension.getHeight());
        joinGame = new Rectangle((int) (standardButtonX - standardGap * 15), (int) (standardButtonY - standardGap * 3), (int) buttonDimension.getWidth() + standardGap * 2, (int) buttonDimension.getHeight());
        startGame = new Rectangle(standardButtonX - standardGap * 11, standardButtonY - standardGap * 6, (int) buttonDimension.getWidth() + standardGap, (int) buttonDimension.getHeight());
        startServer = new Rectangle(standardButtonX - standardGap * 11, (int) (standardButtonY - standardGap * 4.75), (int) buttonDimension.getWidth() + standardGap, (int) buttonDimension.getHeight());
        loadInHostGame = new Rectangle(standardButtonX - standardGap * 11, (int) (standardButtonY - standardGap * 3.5), (int) buttonDimension.getWidth() + standardGap, (int) buttonDimension.getHeight());
        loadInSP = new Rectangle(standardButtonX - standardGap * 10, (int) (standardButtonY - standardGap * 4.75), (int) buttonDimension.getWidth(), (int) buttonDimension.getHeight());
        back = new Rectangle(standardButtonX - standardGap * 12, standardButtonY + standardGap * 4, (int) buttonDimension.getWidth(), (int) buttonDimension.getHeight());
        // spMouseInput = new SPMouseInput(menuCanvas);
        // menuCanvas.addMouseListener(spMouseInput);
        // mpMouseInput = new MPMouseInput(menuCanvas);
        // menuCanvas.addMouseListener(mpMouseInput);
        // hostGameMouseInput = new HostGameMouseInput(menuCanvas);
        // menuCanvas.addMouseListener(hostGameMouseInput);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        Rectangle myMouse = new Rectangle(mx, my, 1, 1);
        if (myMouse.intersects(singlePlayer) && menuCanvas.getMenuState() == MainMenuState.MAIN_MENU_STATE) {// playbutton
            System.out.println("Singleer");
            menuCanvas.setMenuState(MainMenuState.SINGLE_PLAYER_STATE);

        } else if (myMouse.intersects(multiPlayer) && menuCanvas.getMenuState() == MainMenuState.MAIN_MENU_STATE) {// multiplayer
            System.out.println("MultiPlayer");
            menuCanvas.setMenuState(MainMenuState.MULTI_PLAYER_STATE);
            MultiPlayerMenu.setState(MPState.MULTIPLAYERMENU);
        } else if (myMouse.intersects(mapEditor) && menuCanvas.getMenuState() == MainMenuState.MAIN_MENU_STATE) {// mapeditor
            tabs.setSelectedIndex(1);
        } else if (myMouse.intersects(options) && menuCanvas.getMenuState() == MainMenuState.MAIN_MENU_STATE) {// options
            System.out.println("preview");
            tabs.setSelectedIndex(2);
        } else if (myMouse.intersects(aboutUs) && menuCanvas.getMenuState() == MainMenuState.MAIN_MENU_STATE) {// aboutus
            menuCanvas.setMenuState(MainMenuState.ABOUT_US_STATE);
        } else if (myMouse.intersects(exit) && menuCanvas.getMenuState() == MainMenuState.MAIN_MENU_STATE) {// exit
            System.exit(0);
        } else if (myMouse.intersects(aboutUsReturn) && menuCanvas.getMenuState() == MainMenuState.ABOUT_US_STATE) {
            menuCanvas.setMenuState(MainMenuState.MAIN_MENU_STATE);
        }

        if (menuCanvas.getMenuState() == MainMenuState.MULTI_PLAYER_STATE && MultiPlayerMenu.getState() == MPState.MULTIPLAYERMENU) {
            if (myMouse.intersects(hostGame)) {
                System.out.println("HostGame");
                MultiPlayerMenu.setState(MPState.HOSTGAME);
            } else if (myMouse.intersects(joinGame)) {
                System.out.println("JoinGame");
                joinGame();
            } else if (myMouse.intersects(back)) {
                menuCanvas.setMenuState(MainMenuState.MAIN_MENU_STATE);
                System.out.println("Back From MultiPlayeMenu");
            }
        }
        if (menuCanvas.getMenuState() == MainMenuState.SINGLE_PLAYER_STATE) {
            if (myMouse.intersects(startGame)) {
                System.out.println("Start In SP");
                com.teamname.finalproject.Window.getTabs().setSelectedIndex(3);
            } else if (myMouse.intersects(loadInSP)) {
                System.out.println("Load In SP");
                browser.openGameMap();
            } else if (myMouse.intersects(back)) {
                menuCanvas.setMenuState(MainMenuState.MAIN_MENU_STATE);
                System.out.println("Back From SP");
            }
        }
        if (menuCanvas.getMenuState() == MainMenuState.MULTI_PLAYER_STATE && MultiPlayerMenu.getState() == MPState.HOSTGAME) {
            if (myMouse.intersects(startGame)) {
                System.out.println("Startgame In HostGame");
                joinGame();
            } else if (myMouse.intersects(startServer)) {
                System.out.println("start server in host game");
                LookAndFeel previousLF = UIManager.getLookAndFeel();
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    Game.setNumberOfPlayers(Integer.parseInt(JOptionPane.showInputDialog(this, "How Many Players do You Wish To Host?")));
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
                Game.setSocketServer(new GameServer(GameTab.getGame()));
                Game.getSocketServer().start();
            } else if (myMouse.intersects(loadInHostGame)) {
                System.out.println("Load In HostGame");
                browser.openGameMap();
            } else if (myMouse.intersects(back)) {
                MultiPlayerMenu.setState(MPState.MULTIPLAYERMENU);
                System.out.println("Back From HostGame");
            }
        }

    }

    private void joinGame() {
        Game.getSocketClient().start();
        LookAndFeel previousLF = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            String username = JOptionPane.showInputDialog(this, "Please Enter a UserName");
            Game.setUsername(username);
//            if (Game.getSocketServer() == null) {
//            } else {
//                player = new KingdomMP(Game.getSocketServer().getLevel().getKingdoms().get(0).getX(), Game.getSocketServer().getLevel().getKingdoms().get(0).getY(), ObjectType.KINGDOM_MP, PlayerID.PLAYER1, username, null, -1);
//            }
            player = new KingdomMP(0, 0, ObjectType.KINGDOM_MP, PlayerID.PLAYER1, username, null, -1);

            Game.getLevel().getKingdoms().add(player);

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
//				now we have to add this player to the linkedList of players we have
        LoginPacket loginPacket = new LoginPacket(player.getUserName(), (int) player.getX(), (int) player.getY());
        if (Game.getSocketServer() != null) {
            Game.getSocketServer().addConnection(player, loginPacket);
        }
        loginPacket.writeData(Game.getSocketClient());
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
}
