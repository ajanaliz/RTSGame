package com.teamname.finalproject.mainmenu;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Ali J on 5/16/2015.
 */
public class MenuComponents {


    private BufferedImage labelImage;
    private BufferedImage buttonImage;
    private Rectangle singlePlayer;
    private Rectangle multiPlayer;
    private Rectangle mapEditor;
    private Rectangle options;
    private Rectangle aboutUs;
    private Rectangle exit;
    private Dimension dimension;
    private int standardGap;
    private int standardButtonX;
    private int standardButtonY;

    public MenuComponents(){
        dimension = Toolkit.getDefaultToolkit().getScreenSize();
        standardButtonX = (int) dimension.getWidth()/2;
        standardButtonY = (int) dimension.getHeight()/2;
        standardGap = (int) (dimension.getHeight() / 15);
        singlePlayer = new Rectangle( standardButtonX - (int) dimension.getWidth() / 10, standardButtonY - 5 * standardGap, (int) dimension.getWidth() / 5, (int) dimension.getHeight() / 28);
        multiPlayer = new Rectangle( standardButtonX - (int) dimension.getWidth() / 10, standardButtonY - 3 * standardGap, (int) dimension.getWidth() / 5, (int) dimension.getHeight() / 28 );
        mapEditor = new Rectangle( standardButtonX - (int) dimension.getWidth() / 10, standardButtonY - standardGap, (int) dimension.getWidth() / 5, (int) dimension.getHeight() / 28 );
        options = new Rectangle(standardButtonX - (int) dimension.getWidth() / 10, standardButtonY +  standardGap, (int) dimension.getWidth() / 5, (int) dimension.getHeight() / 28);
        aboutUs = new Rectangle( standardButtonX - (int) dimension.getWidth() / 10, standardButtonY + 3 * standardGap, (int) dimension.getWidth() / 5, (int) dimension.getHeight() / 28 );
        exit = new Rectangle( standardButtonX - (int) dimension.getWidth() / 10, standardButtonY + 5 * standardGap, (int) dimension.getWidth() / 5, (int) dimension.getHeight() / 28 );
        try {
            labelImage = ImageIO.read(new File("res/mainmenu/components/label.png"));
            buttonImage = ImageIO.read(new File("res/mainmenu/components/menubutton.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Font font = new Font("Century Gothic", Font.BOLD,20);
        g.setFont(font);
        g2d.setColor(new Color(121, 128, 39));
        g2d.drawImage(buttonImage, standardButtonX - (int) dimension.getWidth() / 10, standardButtonY - 5 * standardGap, (int) dimension.getWidth() / 5, (int) dimension.getHeight() / 28, null);
        String s = "Single Player";
        int length = (int) g.getFontMetrics().getStringBounds(s , g).getWidth();
        g2d.drawString(s, standardButtonX - length / 2, standardButtonY - 5 * standardGap + 25);
        g2d.drawImage(buttonImage, standardButtonX - (int) dimension.getWidth() / 10, standardButtonY - 3 * standardGap, (int) dimension.getWidth() / 5, (int) dimension.getHeight() / 28, null);
        s = "Multi Player";
        length = (int) g.getFontMetrics().getStringBounds(s , g).getWidth();
        g2d.drawString(s, standardButtonX - length / 2, standardButtonY - 3 * standardGap + 25);
        g2d.drawImage(buttonImage, standardButtonX - (int) dimension.getWidth() / 10, standardButtonY - standardGap, (int) dimension.getWidth() / 5, (int) dimension.getHeight() / 28, null);
        s = "Map Editor";
        length = (int) g.getFontMetrics().getStringBounds(s , g).getWidth();
        g2d.drawString(s, standardButtonX - length / 2, standardButtonY - standardGap + 25);
        g2d.drawImage(buttonImage,standardButtonX - (int) dimension.getWidth() / 10,standardButtonY + standardGap,(int) dimension.getWidth() / 5 ,(int) dimension.getHeight() / 28,null);
        s = "Preview";
        length = (int) g.getFontMetrics().getStringBounds(s , g).getWidth();
        g2d.drawString(s, standardButtonX - length / 2, standardButtonY + standardGap + 25);
        g2d.drawImage(buttonImage,standardButtonX - (int) dimension.getWidth() / 10,standardButtonY + 3 * standardGap,(int) dimension.getWidth() / 5 ,(int) dimension.getHeight() / 28,null);
        s = "About Us";
        length = (int) g.getFontMetrics().getStringBounds(s , g).getWidth();
        g2d.drawString(s, standardButtonX - length / 2, standardButtonY + 3 * standardGap + 25);
        g2d.drawImage(buttonImage,standardButtonX - (int) dimension.getWidth() / 10,standardButtonY + 5 * standardGap,(int) dimension.getWidth() / 5 ,(int) dimension.getHeight() / 28,null);
        s = "Exit";
        length = (int) g.getFontMetrics().getStringBounds(s , g).getWidth();
        g2d.drawString(s ,standardButtonX - length / 2 , standardButtonY + 5 * standardGap + 25);
    }

}
