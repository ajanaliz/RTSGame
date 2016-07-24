package com.teamname.finalproject.editor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DownPanel extends JPanel {

    GameScreen gameScreen;
    JButton ocean , deepOcean , goldFish , whiteFish , mine , tree , land;

    JPanel myButtonPanel;
    private int width;
    private int height;
    private int buttonheight;
    private int buttonwidth;
    private JLabel image;
    private BufferedImage bgImage;
    private Color buttonColor;

    public DownPanel(GameScreen gameScreen) {
        buttonColor = new Color(35 , 51 , 25);
        try {
            bgImage = ImageIO.read(new File("res/mydownUI.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.gameScreen = gameScreen;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        width = (int) (d.getWidth() / 20) * 17;
        height = (int) ((int) (d.getWidth() / 20) * 2.15);

        setSize(width , height);

        buttonheight = (int) ((d.getHeight() / 33) * 2);
        buttonwidth = (int) (d.getWidth() / 20);

        setBackground(Color.BLACK);
        setLayout(null);

        land = new JButton();
        land.setBackground(buttonColor);
        BufferedImage land1 = null;
        try {
            land1 = ImageIO.read(new File("land.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        land.setIcon(new ImageIcon(land1.getSubimage(0 , 0 , land1.getWidth() , land1.getHeight() / 2)));
        land.setRolloverIcon(new ImageIcon(land1.getSubimage(0 , land1.getHeight() / 2 , land1.getWidth() , land1.getHeight() / 2)));
        land.setToolTipText("Land");
        land.setSize((int) (d.getWidth() / 60) * 4 , (int) d.getHeight() / 11);
        land.setLocation(buttonwidth , buttonheight);

        this.add(land);

        ocean = new JButton();
        ocean.setBackground(buttonColor);
        BufferedImage ocean1 = null;
        try {
            ocean1 = ImageIO.read(new File("wave2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ocean.setIcon(new ImageIcon(ocean1.getSubimage(0 , 0 , ocean1.getWidth() , ocean1.getHeight() / 2)));
        ocean.setRolloverIcon(new ImageIcon(ocean1.getSubimage(0 , ocean1.getHeight() / 2 , ocean1.getWidth() , ocean1.getHeight() / 2)));
        ocean.setToolTipText("Shallow Waters");
        ocean.setSize((int) (d.getWidth() / 60) * 4 , (int) d.getHeight() / 11);
        ocean.setLocation(buttonwidth * 3, buttonheight);

        this.add(ocean);

        deepOcean = new JButton();
        deepOcean.setBackground(buttonColor);
        BufferedImage deepOcean1 = null;
        try {
            deepOcean1 = ImageIO.read(new File("waves.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        deepOcean.setIcon(new ImageIcon(deepOcean1.getSubimage(0 , 0 , deepOcean1.getWidth() , deepOcean1.getHeight() / 2)));
        deepOcean.setRolloverIcon(new ImageIcon(deepOcean1.getSubimage(0 , deepOcean1.getHeight() / 2 , deepOcean1.getWidth() , deepOcean1.getHeight() / 2)));
        deepOcean.setToolTipText("Deep Waters");
        deepOcean.setSize((int) (d.getWidth() / 60) * 4 , (int) d.getHeight() / 11);
        deepOcean.setLocation(buttonwidth * 5 , buttonheight);
        this.add(deepOcean);

        whiteFish = new JButton();
        whiteFish.setBackground(buttonColor);
        BufferedImage whiteFish1 = null;
        try {
            whiteFish1 = ImageIO.read(new File("fish1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        whiteFish.setIcon(new ImageIcon(whiteFish1.getSubimage(0 , 0 , whiteFish1.getWidth() , whiteFish1.getHeight() / 2)));
        whiteFish.setRolloverIcon(new ImageIcon(whiteFish1.getSubimage(0 , whiteFish1.getHeight() / 2 , whiteFish1.getWidth() , whiteFish1.getHeight() / 2)));
        whiteFish.setToolTipText("White Fish");
        whiteFish.setSize((int) (d.getWidth() / 60) * 4 , (int) d.getHeight() / 11);
        whiteFish.setLocation(buttonwidth * 9 , buttonheight);
        this.add(whiteFish);

        mine = new JButton();
        mine.setBackground(buttonColor);
        BufferedImage mine1 = null;
        try {
            mine1 = ImageIO.read(new File("miner.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mine.setIcon(new ImageIcon(mine1.getSubimage(0 , 0 , mine1.getWidth() , mine1.getHeight() / 2)));
        mine.setRolloverIcon(new ImageIcon(mine1.getSubimage(0 , mine1.getHeight() / 2 , mine1.getWidth() , mine1.getHeight() / 2)));
        mine.setToolTipText("Mine");
        mine.setSize((int) (d.getWidth() / 60) * 4 , (int) d.getHeight() / 11);
        mine.setLocation(buttonwidth * 11 , buttonheight);
        this.add(mine);

        goldFish = new JButton();
        goldFish.setBackground(buttonColor);
        BufferedImage goldFish1 = null;
        try {
            goldFish1 = ImageIO.read(new File("fish222.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        goldFish.setIcon(new ImageIcon(goldFish1.getSubimage(0 , 0 , goldFish1.getWidth() , goldFish1.getHeight() / 2)));
        goldFish.setRolloverIcon(new ImageIcon(goldFish1.getSubimage(0 , goldFish1.getHeight() / 2 , goldFish1.getWidth() , goldFish1.getHeight() / 2)));
        goldFish.setToolTipText("Gold Fish");
        goldFish.setSize((int) (d.getWidth() / 60) * 4 , (int) d.getHeight() / 11);
        goldFish.setLocation(buttonwidth *7, buttonheight);

        this.add(goldFish);


        tree = new JButton();
        tree.setBackground(buttonColor);
        BufferedImage tree1 = null;
        try {
            tree1 = ImageIO.read(new File("treeeeee.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        tree.setIcon(new ImageIcon(tree1.getSubimage(0 , 0 , tree1.getWidth() , tree1.getHeight() / 2)));
        tree.setRolloverIcon(new ImageIcon(tree1.getSubimage(0 , tree1.getHeight() / 2 , tree1.getWidth() , tree1.getHeight() / 2)));
        tree.setToolTipText("Tree");
        tree.setSize((int) (d.getWidth() / 60) * 4 , (int) d.getHeight() / 11);
        tree.setLocation(buttonwidth * 13 , buttonheight);
        this.add(tree);

        addListeners();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage , 0 , 0 , getWidth() , getHeight() , null);
    }

    private void addListeners() {

        land.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage a = new BufferedImage(96 / 2 , 96 / 2 , BufferedImage.TYPE_INT_ARGB);
                try {
                    a = ImageIO.read(new File("res/transparentland.png"));
                    // Image c = a.getImage(a);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                BufferedImage out = new BufferedImage(96 / 2 , 96 / 2 , BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = out.createGraphics();
                float opacity = 0.1f;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER , opacity));

                Toolkit b = Toolkit.getDefaultToolkit();
                Cursor d = b.createCustomCursor(a , new Point(10 , 10) , " ");
                gameScreen.setCursor(d);
                gameScreen.setId(1);

            }
        });

        ocean.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Toolkit a = Toolkit.getDefaultToolkit();
                Image c = a.getImage("res/0.png");
                Cursor d = a.createCustomCursor(c , new Point(10 , 10) , " ");
                gameScreen.setCursor(d);
                gameScreen.setId(0);
            }
        });

        deepOcean.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                Toolkit a = Toolkit.getDefaultToolkit();
                Image c = a.getImage("res/new deep waters/" + 0 + ".png");
                Cursor d = a.createCustomCursor(c , new Point(10 , 10) , " ");
                gameScreen.setCursor(d);
                gameScreen.setId(2);

            }
        });
        goldFish.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Toolkit a = Toolkit.getDefaultToolkit();
                Image c = a.getImage("fish22.png");
                Cursor d = a.createCustomCursor(c , new Point(10 , 10) , " ");
                gameScreen.setCursor(d);
                gameScreen.setId(6);

            }
        });
        whiteFish.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Toolkit a = Toolkit.getDefaultToolkit();
                Image c = a.getImage("fish.png");
                Cursor d = a.createCustomCursor(c , new Point(10 , 10) , " ");
                gameScreen.setCursor(d);
                gameScreen.setId(4);

            }
        });
        mine.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Toolkit a = Toolkit.getDefaultToolkit();
                Image c = a.getImage("ore.png");
                Cursor d = a.createCustomCursor(c , new Point(10 , 10) , " ");
                gameScreen.setCursor(d);
                gameScreen.setId(5);

            }
        });
        tree.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Toolkit a = Toolkit.getDefaultToolkit();
                Image c = a.getImage("splat-tree-1");
                Cursor d = a.createCustomCursor(c , new Point(10 , 10) , " ");
                gameScreen.setCursor(Cursor.getDefaultCursor());
                gameScreen.setId(3);

            }
        });
    }
}
