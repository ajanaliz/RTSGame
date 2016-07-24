package com.teamname.finalproject.mainmenu;

import com.teamname.finalproject.util.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Ali J on 5/16/2015.
 */
public class AboutUsMenu {

    private Font font;
    private Rectangle rectangle;
    private Dimension dimension;
    private int standardX, standardY, returnButtonX, returnButtonY;
    private String aboutUsTextl1;
    private String aboutUsTextl2;
    private String aboutUsTextl3;
    private String aboutUsTextl4;
    private String aboutUsTextl5;
    private String aboutUsTextl6;
    private String aboutUsTextl7;
    private String aboutUsTextl8;
    private String aboutUsTextl9;
    private String aboutUsTextl10;
    private String aboutUsTextl11;
    private String aboutUsTextl12;
    private String aboutUsTextl13;
    private String aboutUsTextl14;
    private String aboutUsTextl15;
    private String aboutUsTextl16;
    private BufferedImage returnButton;

    public AboutUsMenu() {
        returnButton = MainMenuCanvas.getButton();
        font = new Font("Century Gothic", Font.BOLD, 20);
        dimension = Toolkit.getDefaultToolkit().getScreenSize();
        standardX = (int) dimension.getWidth() / 2 - (int) dimension.getWidth() / 15 * 4;
        standardY = (int) dimension.getHeight() / 2 - (int) dimension.getHeight() / 15 * 4;
        returnButtonX = (int) dimension.getHeight() / 15 * 3;
        returnButtonY = (int) dimension.getHeight() - (int) dimension.getHeight() / 15 * 2;
        rectangle = new Rectangle(standardX, standardY, (int) dimension.getWidth() / 5 * 2 + 15, (int) dimension.getHeight() / 5 * 2);
        aboutUsTextl1 = "in rooz ha ke hame donyashun computerie'vo tafriheshun bazi!! ma ham";
        aboutUsTextl2 = "ke dige daneshjooye in reshte'im!se tayi baham in bazio neveshtim!!";
        aboutUsTextl3 = "dar morede sabegheye derakhshane khodemunam,hamin bas ke shagerde";
        aboutUsTextl4 = "shoma'imo daneshjooye narmo IT!!az unja'i ke sha'er mige: dar rahe";
        aboutUsTextl5 = "manzele leyli ke khatar hast,dar 'An sharte aval ghadam 'An ast ke";
        aboutUsTextl6 = "majnun bashi!! ma ham ke setayimun majnune code zadan!!(mikham begam";
        aboutUsTextl7 = "ke kheili talash kardim!!hameye daneshkade shahedan!!sajjad midune ke";
        aboutUsTextl8 = "ma shaba key miraftim khune!!) talashemuno kardim! dooste khubemun";
        aboutUsTextl9 = "aghaye gilani kollan hallale moshkelat budan!!(debuger) aghaye ";
        aboutUsTextl10 = "janalizadeh ham ke dasteshun dard nakone!! vazifeye khatireye peyda";
        aboutUsTextl11 = "kardane aks va photoshop ro mardune be dush keshidan!!khanume ";
        aboutUsTextl12 = "Abbasianam yademun nare,mese ye sarparast baese hamkarie beyne";
        aboutUsTextl13 = "in 2 mojoode sarsakht mishodan!!nagofte namune bedune ishun in 2ta";
        aboutUsTextl14 = "bishtar baham da'va mioftadan ta inke code bezanan!hameye talashemuno";
        aboutUsTextl15 = "kardim!haselesham shode in bazi!omidvaram nomre'i ke be ma midin ";
        aboutUsTextl16 = "khastegi ro ba'de chand mah ke mirim be diaremun az tanemun dar biare!!";
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(SpriteSheet.UIBACKGROUND.getSprites().get(0), standardX, standardY, (int) dimension.getWidth() / 5 * 2 + 15, (int) dimension.getHeight() / 5 * 2, null);
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(new Color(128, 23, 13));
        g2d.draw(rectangle);
        g2d.setFont(font);
        g2d.setColor(new Color(0, 0, 0));
        g2d.drawString(aboutUsTextl1, standardX + 10, standardY + (int) (dimension.getHeight() / 43));
        g2d.drawString(aboutUsTextl2, standardX + 10, standardY + 2 * (int) (dimension.getHeight() / 43));
        g2d.drawString(aboutUsTextl3, standardX + 10, standardY + 3 * (int) (dimension.getHeight() / 43));
        g2d.drawString(aboutUsTextl4, standardX + 10, standardY + 4 * (int) (dimension.getHeight() / 43));
        g2d.drawString(aboutUsTextl5, standardX + 10, standardY + 5 * (int) (dimension.getHeight() / 43));
        g2d.drawString(aboutUsTextl6, standardX + 10, standardY + 6 * (int) (dimension.getHeight() / 43));
        g2d.drawString(aboutUsTextl7, standardX + 10, standardY + 7 * (int) (dimension.getHeight() / 43));
        g2d.drawString(aboutUsTextl8, standardX + 10, standardY + 8 * (int) (dimension.getHeight() / 43));
        g2d.drawString(aboutUsTextl9, standardX + 10, standardY + 9 * (int) (dimension.getHeight() / 43));
        g2d.drawString(aboutUsTextl10, standardX + 10, standardY + 10 * (int) (dimension.getHeight() / 43));
        g2d.drawString(aboutUsTextl11, standardX + 10, standardY + 11 * (int) (dimension.getHeight() / 43));
        g2d.drawString(aboutUsTextl12, standardX + 10, standardY + 12 * (int) (dimension.getHeight() / 43));
        g2d.drawString(aboutUsTextl13, standardX + 10, standardY + 13 * (int) (dimension.getHeight() / 43));
        g2d.drawString(aboutUsTextl14, standardX + 10, standardY + 14 * (int) (dimension.getHeight() / 43));
        g2d.drawString(aboutUsTextl15, standardX + 10, standardY + 15 * (int) (dimension.getHeight() / 43));
        g2d.drawString(aboutUsTextl16, standardX + 10, standardY + 16 * (int) (dimension.getHeight() / 43));
        g2d.drawImage(returnButton, returnButtonX, returnButtonY, 150, 35, null);
        g2d.setColor(new Color(121, 128, 39));
        g2d.drawString("Back", returnButtonX + 50, returnButtonY + 23);
    }
}
