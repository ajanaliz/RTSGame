package com.teamname.finalproject.mainmenu;

import com.teamname.finalproject.Window;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

/**
 * Created by Ali J on 5/21/2015.
 */
public class MultiPlayerMenu {

    private BufferedImage myButton;
    private LinkedList<BufferedImage> backGroundImages;
    private int backGroundFrame;
    private int standardButtonX;
    private int standardButtonY;
    private Dimension dimension;
    private int standardGap;
    private Rectangle joinGame;
    private Rectangle back;
    private Rectangle hostGame;
    private MultiPlayerMenu menu;
    private BufferedImage buttonImage;
    private Dimension buttonDimension;
    private static MPState state;
    private HostGameMenu hostGameMenu;

    public MultiPlayerMenu() {
        state = MPState.MULTIPLAYERMENU;
        dimension = Window.getLocalDimension();
        standardButtonX = (int) dimension.getWidth() / 2;
        standardButtonY = (int) dimension.getHeight() / 2;
        standardGap = (int) (dimension.getHeight() / 15);
        backGroundImages = new LinkedList<BufferedImage>();
        backGroundFrame = 29;
        init();
        buttonDimension = new Dimension((int) dimension.getWidth() / 9, (int) dimension.getHeight() / 17);
        hostGame = new Rectangle((int) (standardButtonX - standardGap * 2.5), standardButtonY - standardGap * 5, (int) buttonDimension.getWidth() + standardGap * 2, (int) buttonDimension.getHeight());
        joinGame = new Rectangle((int) (standardButtonX - standardGap * 2.5), (int) (standardButtonY - standardGap * 3), (int) buttonDimension.getWidth() + standardGap * 2, (int) buttonDimension.getHeight());
        back = new Rectangle(standardButtonX - standardGap * 12, standardButtonY + standardGap * 4, (int) buttonDimension.getWidth(), (int) buttonDimension.getHeight());
        buttonImage = MainMenuCanvas.getButton();
        hostGameMenu = new HostGameMenu();
    }

    private void init() {
        myButton = MainMenuCanvas.getButton();
        try {
            for (int i = 1; i < 3; i++) {
                BufferedImage temp = ImageIO.read(new File("res//mainmenu//singleplayer//(" + i + ").jpg"));
                backGroundImages.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g, int width, int height) {
        if (state == MPState.HOSTGAME) {
            hostGameMenu.render(g, width, height);
        } else {
            Graphics2D g2d = (Graphics2D) g;
            Font font = new Font("Century Gothic", Font.BOLD, 20);
            g.setFont(font);
            g2d.setColor(new Color(121, 128, 39));
            g2d.drawImage(buttonImage, (int) hostGame.getX(), (int) hostGame.getY(), (int) hostGame.getWidth(), (int) hostGame.getHeight(), null);
            String caption = "Host Game";
            int length = (int) g.getFontMetrics().getStringBounds(caption, g).getWidth();
            g2d.drawString(caption, (int) (hostGame.getX() + hostGame.getWidth() / 2 - length / 2), (int) hostGame.getY() + standardGap / 2);
            g2d.drawImage(buttonImage, (int) joinGame.getX(), (int) joinGame.getY(), (int) joinGame.getWidth(), (int) joinGame.getHeight(), null);
            caption = "Join Game";
            length = (int) g.getFontMetrics().getStringBounds(caption, g).getWidth();
            g2d.drawString(caption, (int) (joinGame.getX() + joinGame.getWidth() / 2 - length / 2), (int) joinGame.getY() + standardGap / 2);
            g2d.drawImage(buttonImage, (int) back.getX(), (int) back.getY(), (int) back.getWidth(), (int) back.getHeight(), null);
            caption = "Back";
            length = (int) g.getFontMetrics().getStringBounds(caption, g).getWidth();
            g2d.drawString(caption, (int) (back.getX() + back.getWidth() / 2 - length / 2), (int) back.getY() + standardGap / 2);
        }
    }

    public void update() {
        backGroundFrame++;
        if (backGroundFrame == backGroundImages.size() - 1) {
            backGroundFrame = 0;
        }
    }

    public static MPState getState() {
        return state;
    }

    public static void setState(MPState state) {
        MultiPlayerMenu.state = state;
    }
}
