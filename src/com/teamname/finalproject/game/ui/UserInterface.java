package com.teamname.finalproject.game.ui;

import com.teamname.finalproject.game.GameTab;
import com.teamname.finalproject.game.gameobject.GameObject;
import com.teamname.finalproject.game.gameobject.ObjectType;
import com.teamname.finalproject.game.gameobject.entities.Ferry;
import com.teamname.finalproject.game.gameobject.entities.FishingBoat;
import com.teamname.finalproject.game.gameobject.entities.Kingdom;
import com.teamname.finalproject.game.gameobject.entities.Peasant;
import com.teamname.finalproject.game.gameobject.entities.RangedFighter;
import com.teamname.finalproject.game.gameobject.entities.WarShip;
import com.teamname.finalproject.game.gameobject.resources.Resource;
import com.teamname.finalproject.game.gameobject.resources.Mine;
import com.teamname.finalproject.game.gameobject.resources.Tree;
import com.teamname.finalproject.game.gameobject.resources.WhiteFish;
import com.teamname.finalproject.game.gameobject.entities.King;
import com.teamname.finalproject.util.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;


/**
 * Created by Ali J on 5/22/2015.
 */
public class UserInterface extends Canvas {

    private int mx,my;

    private UIStates state;

    private UIListeners listeners;
    private Dimension dimension;
    private double blockSize;
    private BufferedImage myButton;
    public GameObject myFocus;
    public UserInterface() {

        blockSize = GameTab.getBlockSize();
        setBackground(new Color(0xffcacaca, true));
        setForeground(new Color(0xffcacaca, true));
        setSize((int) (3 * blockSize + 6), (int) (6 * blockSize));
        state = UIStates.VOID;
        listeners = new UIListeners(this, getWidth(), getHeight(), state, this);
        dimension = GameTab.getDimension();
        setLocation((int) dimension.getWidth() - getWidth() - 15, (int) (blockSize * 1.5));
        myFocus = null;

    }


    public void update() {

    }

    public void render() {
        try {
            BufferStrategy bs = getBufferStrategy();
            if (bs == null) {// if there is no bufferstrategy,we're going to create
                // one
                createBufferStrategy(3);
                return;
            }
            Graphics g = bs.getDrawGraphics();
            g.fillRect(0, 0, getWidth(), getHeight());
            ObjectType temp = ObjectType.VOID;

            g.drawImage(SpriteSheet.BACKGROUNDIMAGE.getSprites().get(0), 0, 0, getWidth(), getHeight(), null);
            if (myFocus == null) {
                setState(UIStates.VOID);
                setVisible(false);
            }


            if (myFocus != null) {
                temp = myFocus.getType();

                switch (temp) {

                    case KINGDOM://kingdom

                        setVisible(true);
                        setState(UIStates.KINGDOM);
                        drawKingdomUI(g);
                        break;
                    case KINGDOM_MP:
                        setVisible(true);
                        setState(UIStates.KINGDOM);
                        drawKingdomUI(g);
                        break;
                    case KING:
                        setVisible(true);
                        setState(UIStates.KING);
                        drawKingUI(g);
                        break;

                    case PEASANT://human
                        if (((Peasant) myFocus).getState() != Peasant.State.DEAD) {
                            setVisible(true);
                            setState(UIStates.PEASANT);
                            drawHumanUI(g);
                        } else {
                            setVisible(false);
                            setState(UIStates.VOID);
                        }
                        break;

                    case FERRY://ferry
                        setVisible(true);
                        setState(UIStates.Ferry);
                        drawFerryUI(g);
                        break;

                    case FISHINGBOAT://fishing boat
                        setVisible(true);
                        setState(UIStates.FISHINGBOAT);
                        drawFishingBoatUI(g);
                        break;

                    case WARSHIP://warship
                        setVisible(true);
                        setState(UIStates.WARSHIP);
                        drawWarshipUI(g);
                        break;

                    case TREE://tree
                        setVisible(true);
                        setState(UIStates.TREE);
                        drawTreeUI(g);
                        break;

                    case METAL://mine
                        setVisible(true);
                        setState(UIStates.MINE);
                        drawMineUI(g);
                        break;

                    case GOLDFISH://goldfish
                        setVisible(true);
                        setState(UIStates.GOLDFISH);
                        drawGoldFishUI(g);
                        break;

                    case WHITEFISH://whitefish
                        setVisible(true);
                        setState(UIStates.WHITEFISH);
                        drawWhiteFishUI(g);
                        break;

                    case VOID://void
                        setState(UIStates.VOID);
                        setVisible(false);
                        break;
                    case RANGEDFIGHTER:
                        setVisible(true);
                        setState(UIStates.RANGEDFIGHTER);
                        drawRangedFighter(g);
                        break;

                }
            }


            g.dispose();// we're disposing the graphics,because this will free up
            // the memory in the graphics and free up any resources that
            // the graphics object is using,because we wont be using it
            // anymore-->we've done all our drawings for this loop
            bs.show();
        } catch (IllegalStateException e) {

        }
    }

    private void drawRangedFighter(Graphics g) {
        try {

            Graphics2D g2d = (Graphics2D) g;
            Font font = new Font("Century Gothic", Font.BOLD, 20);
            g2d.setColor(new Color(121, 128, 39));
            g.setFont(font);

            g2d.drawImage(SpriteSheet.WIZARD.getSprites().get(0), 0, 2 * (getHeight() / 26), getWidth(), 4 * getHeight() / 13, null);


            String s = "lives: " + Integer.toString(((RangedFighter) myFocus).getHealth());
            int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 12 * (getHeight() / 26));

            s = "" + (((RangedFighter) myFocus).getState());
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 15 * (getHeight() / 26));

//	        s = ""+(((RangedFighter) myFocus).getEnemy());
//	        length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
//	        g2d.drawImage(SpriteSheet.BUTTON.getSprites().get(0), getWidth() / 8, 9 * (getHeight() / 26), 3 * (getWidth() / 4), getHeight() / 13, null);
//	        g2d.drawString(s, (getWidth() / 2) - length / 2, 9*(getHeight() / 26));

        } catch (NullPointerException e) {
        } catch (ClassCastException e) {
        }
    }

    private void drawKingUI(Graphics g) {

        try {

            Graphics2D g2d = (Graphics2D) g;
            Font font = new Font("Century Gothic", Font.BOLD, 20);
            g2d.setColor(new Color(121, 128, 39));
            g.setFont(font);

            g2d.drawImage(SpriteSheet.KING.getSprites().get(0), 0, 2 * (getHeight() / 26), getWidth(), 4 * getHeight() / 13, null);

            String s = "lives: " + Integer.toString(((King) myFocus).getHealth());
            int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 12 * (getHeight() / 26));

            s = "" + (((King) myFocus).getState());
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 15 * (getHeight() / 26));


        } catch (Exception e) {
        }


    }

    private void drawKingdomUI(Graphics g) {

        //wood ,  metal , fish ,human , fishingboat , ferry , warship


        try {
            Graphics2D g2d = (Graphics2D) g;
            Font font = new Font("Century Gothic", Font.BOLD, 20);
            g2d.setColor(new Color(121, 128, 39));
            g.setFont(font);

            g2d.drawImage(SpriteSheet.KINGDOM1.getSprites().get(0), 0, (int)(1.5 * (getHeight() / 26)), getWidth(), 4 * getHeight() / 13, null);


            String s = "metal: " + ((Kingdom) myFocus).getTotalMetal();
            int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 13 * (getHeight() / 26));

            s = "wood: " + ((Kingdom) myFocus).getTotalWood();
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 15 * (getHeight() / 26));

            s = "food: " + ((Kingdom) myFocus).getTotalFood();
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 17 * (getHeight() / 26));

            s = "NEW HUMAN ";
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawImage(SpriteSheet.BUTTON.getSprites().get(0), getWidth() / 8, (int) (17.5 * (getHeight() / 26)), 3 * (getWidth() / 4), getHeight() / 13, null);
            g2d.drawString(s, (getWidth() / 2) - length / 2, 19 * (getHeight() / 26));

            s = "NEW WARSHIP ";
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawImage(SpriteSheet.BUTTON.getSprites().get(0), getWidth() / 8, (int) (19.5 * (getHeight() / 26)), 3 * (getWidth() / 4), getHeight() / 13, null);
            g2d.drawString(s, (getWidth() / 2) - length / 2, 21 * (getHeight() / 26));

            s = "NEW FERRY ";
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawImage(SpriteSheet.BUTTON.getSprites().get(0), getWidth() / 8, (int) (21.5 * (getHeight() / 26)), 3 * (getWidth() / 4), getHeight() / 13, null);
            g2d.drawString(s, (getWidth() / 2) - length / 2, 23 * (getHeight() / 26));

            s = "NEW FISHINGBOAT ";
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawImage(SpriteSheet.BUTTON.getSprites().get(0), getWidth() / 8, (int) (23.5 * (getHeight() / 26)), 3 * (getWidth() / 4), getHeight() / 13, null);
            g2d.drawString(s, (getWidth() / 2) - length / 2, 25 * (getHeight() / 26));

            s = "NEW WIZARD ";
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawImage(SpriteSheet.BUTTON.getSprites().get(0), getWidth() / 8, (int) (9.5 * (getHeight() / 26)), 3 * (getWidth() / 4), getHeight() / 13, null);
            g2d.drawString(s, (getWidth() / 2) - length / 2, 11 * (getHeight() / 26));

        } catch (Exception e) {
        }


    }

    private void drawHumanUI(Graphics g) {
        // heal , wood , metal
        try {

            Graphics2D g2d = (Graphics2D) g;
            Font font = new Font("Century Gothic", Font.BOLD, 20);
            g2d.setColor(new Color(121, 128, 39));
            g.setFont(font);
            g2d.drawImage(SpriteSheet.PEASANT1.getSprites().get(0), 0, 2 * (getHeight() / 26), getWidth(), 4 * getHeight() / 13, null);


            String s = "wood: " + Integer.toString(((Peasant) myFocus).getWood());
            int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 21 * (getHeight() / 26));

            s = "metal: " + Integer.toString(((Peasant) myFocus).getMetal());
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 24 * (getHeight() / 26));

            s = "capacity: " + Integer.toString(((Peasant) myFocus).getCapacity());
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 18 * (getHeight() / 26));

            s = "lives: " + Integer.toString(((Peasant) myFocus).getHealth());
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 12 * (getHeight() / 26));

            s = "" + (((Peasant) myFocus).getState());
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 15 * (getHeight() / 26));


        } catch (Exception e) {
        }
    }

    private void drawWhiteFishUI(Graphics g) {

        try {

            Graphics2D g2d = (Graphics2D) g;
            Font font = new Font("Century Gothic", Font.BOLD, 20);
            g2d.setColor(new Color(121, 128, 39));
            g.setFont(font);

            g2d.drawImage(SpriteSheet.FISH.getSprites().get(0), 0, 2 * (getHeight() / 26), getWidth(), 4 * getHeight() / 13, null);

            String s = "capacity: " + ((WhiteFish) myFocus).getCapacity();
            int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 12 * (getHeight() / 26));

        } catch (Exception e) {
        }

    }

    private void drawGoldFishUI(Graphics g) {

        try {


            Graphics2D g2d = (Graphics2D) g;
            Font font = new Font("Century Gothic", Font.BOLD, 20);
            g2d.setColor(new Color(121, 128, 39));
            g.setFont(font);

            g2d.drawImage(SpriteSheet.FISH.getSprites().get(0), 0, 2 * (getHeight() / 26), getWidth(), 4 * getHeight() / 13, null);


            String s = "fish: " + ((Resource) myFocus).getCapacity();
            int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 12 * (getHeight() / 26));

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void drawMineUI(Graphics g) {

        try {


            Graphics2D g2d = (Graphics2D) g;
            Font font = new Font("Century Gothic", Font.BOLD, 20);
            g2d.setColor(new Color(121, 128, 39));
            g.setFont(font);

            g2d.drawImage(SpriteSheet.METAL.getSprites().get(0), 0, 2 * (getHeight() / 26), getWidth(), 4 * getHeight() / 13, null);


            String s = "metal : " + ((Mine) myFocus).getCapacity();
            int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 12 * (getHeight() / 26));

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void drawTreeUI(Graphics g) {

        try {

            Graphics2D g2d = (Graphics2D) g;
            Font font = new Font("Century Gothic", Font.BOLD, 20);
            g2d.setColor(new Color(121, 128, 39));
            g.setFont(font);

            g2d.drawImage(SpriteSheet.TREE.getSprites().get(0), 0, 2 * (getHeight() / 26), getWidth(), 4 * getHeight() / 13, null);

            String s = "wood: " + ((Tree) myFocus).getCapacity();
            int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 12 * (getHeight() / 26));

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void drawWarshipUI(Graphics g) {
        // heal , repair
        try {

            Graphics2D g2d = (Graphics2D) g;
            Font font = new Font("Century Gothic", Font.BOLD, 20);
            g2d.setColor(new Color(121, 128, 39));
            g.setFont(font);


            g2d.drawImage(SpriteSheet.warship3.getSprites().get(0), 0, 2 * (getHeight() / 26), getWidth(), 4 * getHeight() / 13, null);


            String s = "lives: " + ((WarShip) myFocus).getHealth();
            int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 12 * (getHeight() / 26));

            s = "REPAIR WARSHIP";
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawImage(SpriteSheet.BUTTON.getSprites().get(0), getWidth() / 8, (int) (13.5 * (getHeight() / 26)), 3 * (getWidth() / 4), getHeight() / 13, null);
//        g2d.fillRect(getWidth() / 8, 15 * (getHeight() / 26), 3 * (getWidth() / 4), getHeight() / 13);
            g2d.drawString(s, (getWidth() / 2) - length / 2, 15 * (getHeight() / 26));


            s = "" + (((WarShip) myFocus).getState());
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawImage(SpriteSheet.BUTTON.getSprites().get(0), getWidth() / 8, (int) (16.5 * (getHeight() / 26)), 3 * (getWidth() / 4), getHeight() / 13, null);
            g2d.drawString(s, (getWidth() / 2) - length / 2, 18 * (getHeight() / 26));

        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    private void drawFishingBoatUI(Graphics g) {
        //repair ,heal ,  goldfish
        try {

            Graphics2D g2d = (Graphics2D) g;
            Font font = new Font("Century Gothic", Font.BOLD, 20);
            g2d.setColor(new Color(121, 128, 39));
            g.setFont(font);

            g2d.drawImage(SpriteSheet.FISHINGBOAT1.getSprites().get(0), 0, 2 * (getHeight() / 26), getWidth(), 3 * getHeight() / 13, null);
            // g2d.fillRect((int)(getWidth() / 8), 9 * (int)(getHeight() / 26)-(int)(getHeight() / 13), 3 * (int)(getWidth() / 4), (int)(getHeight() / 13));

            String s = "lives: " + ((FishingBoat) myFocus).getHealth();
            int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 9 * (getHeight() / 26));

            s = "capacity: " + ((FishingBoat) myFocus).getFood();
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 12 * (getHeight() / 26));


            s = "REPAIR FISHINGBOAT";
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawImage(SpriteSheet.BUTTON.getSprites().get(0), getWidth() / 8, (int) (13.5 * (getHeight() / 26)), 3 * (getWidth() / 4) + 10, getHeight() / 13, null);
            g2d.drawString(s, (getWidth() / 2) - length / 2, 15 * (getHeight() / 26));

            s = "" + (((FishingBoat) myFocus).getState());
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 18 * (getHeight() / 26));


        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void drawFerryUI(Graphics g) {
        //repair , heal , capacity
        try {


            Graphics2D g2d = (Graphics2D) g;
            Font font = new Font("Century Gothic", Font.BOLD, 20);
            g2d.setColor(new Color(121, 128, 39));
            g.setFont(font);

            g2d.drawImage(SpriteSheet.FISHINGBOAT2.getSprites().get(0), 0, (getHeight() / 26), getWidth(), 6 * getHeight() / 26, null);


            String s = "lives: " + ((Ferry) myFocus).getHealth();
            int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 8 * (getHeight() / 26));

            s = "capacity: " + ((Ferry) myFocus).getCapacity();
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 10 * (getHeight() / 26));

            s = "wood: " + ((Ferry) myFocus).getWood();
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 12 * (getHeight() / 26));

            s = "metal: " + ((Ferry) myFocus).getMetal();
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 14 * (getHeight() / 26));


            s = "REPAIR FERRY";
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawImage(SpriteSheet.BUTTON.getSprites().get(0), getWidth() / 8, (int) (14.5 * (getHeight() / 26)), 3 * (getWidth() / 4), getHeight() / 13, null);
            g2d.drawString(s, (getWidth() / 2) - length / 2, 16 * (getHeight() / 26));

            s = "" + (((Ferry) myFocus).getState());
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawString(s, (getWidth() / 2) - length / 2, 18 * (getHeight() / 26));


            s = "DISEMBARC KING";
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawImage(myButton, getWidth() / 8, (int) (16.5 * (getHeight() / 26)), 3 * (getWidth() / 4), getHeight() / 13, null);
            g2d.drawString(s, (getWidth() / 2) - length / 2, 20 * (getHeight() / 26));

            s = "DISEMBARK MEMBER";
            length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g2d.drawImage(myButton, getWidth() / 8, (int) (18.5 * (getHeight() / 26)), 3 * (getWidth() / 4), getHeight() / 13, null);
            g2d.drawString(s, (getWidth() / 2) - length / 2, 22 * (getHeight() / 26));

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static UIStates lookUpState(int id) {
        for (UIStates s : UIStates.values()) {
            if (s.getStateID() == id) {
                return s;
            }
        }
        return UIStates.VOID;
    }

    public UIStates getState() {
        return state;
    }

    public void setState(UIStates state) {
        this.state = state;
        listeners.setState(state);
    }

    public GameObject getMyFocus() {
        return myFocus;
    }

    public void setMyFocus(GameObject myFocus) {
        this.myFocus = myFocus;


    }

    public int getMy() {
        return my;
    }

    public void setMy(int my) {
        this.my = my;
    }

    public int getMx() {
        return mx;
    }

    public void setMx(int mx) {
        this.mx = mx;
    }

}