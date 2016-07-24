package com.teamname.finalproject.mainmenu;

import com.teamname.finalproject.Window;
import com.teamname.finalproject.game.Game;
import com.teamname.finalproject.game.level.MapBuffer;
import com.teamname.finalproject.util.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Created by Ali J on 5/21/2015.
 */
public class HostGameMenu {

	private BufferedImage myButton;
	private LinkedList<BufferedImage> backGroundImages;
	private int backGroundFrame;
	private int standardButtonX;
	private int standardButtonY;
	private Dimension dimension;
	private int standardGap;
	private Rectangle load;
	private Rectangle back;
	private BufferedImage buttonImage;
	private Dimension buttonDimension;
	private MapBuffer map;
	private Rectangle startGame;
	private Rectangle startServer;

	public HostGameMenu() {
		map = Game.getMap();
		dimension = Window.getLocalDimension();
		standardButtonX = (int) dimension.getWidth() / 2;
		standardButtonY = (int) dimension.getHeight() / 2;
		standardGap = (int) (dimension.getHeight() / 15);
		backGroundImages = new LinkedList<BufferedImage>();
		backGroundFrame = 29;
		buttonDimension = new Dimension((int) dimension.getWidth() / 9 , (int) dimension.getHeight() / 17);
		startGame = new Rectangle(standardButtonX - standardGap * 11 , standardButtonY - standardGap * 6 , (int) buttonDimension.getWidth() + standardGap , (int) buttonDimension.getHeight());
		startServer = new Rectangle(standardButtonX - standardGap * 11 , (int) (standardButtonY - standardGap * 4.75) , (int) buttonDimension.getWidth() + standardGap , (int) buttonDimension.getHeight());
		load = new Rectangle(standardButtonX - standardGap * 11 , (int) (standardButtonY - standardGap * 3.5) , (int) buttonDimension.getWidth() + standardGap , (int) buttonDimension.getHeight());
		back = new Rectangle(standardButtonX - standardGap * 12 , standardButtonY + standardGap * 4 , (int) buttonDimension.getWidth() , (int) buttonDimension.getHeight());
		buttonImage = MainMenuCanvas.getButton();
	}


	public void render(Graphics g , int width , int height) {
        g.drawImage(SpriteSheet.UIBACKGROUND.getSprites().get(0), (standardGap * 7), standardGap, standardGap * 17, (int) (standardGap * 12.5), null);
		g.drawImage(map.getSeasons()[map.getSeasonID()] , (standardGap * 7) , standardGap , standardGap * 17 , (int) (standardGap * 12.5) , null);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(17 , 0 , 70));
		g2d.setStroke(new BasicStroke(5));
		g2d.drawRect((standardGap * 7) - 3 , standardGap - 3 , standardGap * 17 + 5 , (int) (standardGap * 12.5) + 5);
		Font font = new Font("Century Gothic" , Font.BOLD , 20);
		g.setFont(font);
		g2d.setColor(new Color(121 , 128 , 39));
		g2d.drawImage(buttonImage , (int) startGame.getX() , (int) startGame.getY() , (int) startGame.getWidth() , (int) startGame.getHeight() , null);
		String caption = "Start Game";
		int length = (int) g.getFontMetrics().getStringBounds(caption , g).getWidth();
		g2d.drawImage(buttonImage , (int) startGame.getX() , (int) startGame.getY() , (int) startGame.getWidth() , (int) startGame.getHeight() , null);
		g2d.drawString(caption , (int) (startGame.getX() + startGame.getWidth() / 2 - length / 2) , (int) startGame.getY() + standardGap / 2);
		g2d.drawImage(buttonImage , (int) load.getX() , (int) load.getY() , (int) load.getWidth() , (int) load.getHeight() , null);
		caption = "Load";
		length = (int) g.getFontMetrics().getStringBounds(caption , g).getWidth();
		g2d.drawString(caption , (int) (load.getX() + load.getWidth() / 2 - length / 2) , (int) load.getY() + standardGap / 2);
		caption = "Start Server";
		length = (int) g.getFontMetrics().getStringBounds(caption , g).getWidth();
		g2d.drawImage(buttonImage , (int) startServer.getX() , (int) startServer.getY() , (int) startServer.getWidth() , (int) startServer.getHeight() , null);
		g2d.drawString(caption , (int) (startServer.getX() + startServer.getWidth() / 2 - length / 2) , (int) startServer.getY() + standardGap / 2);
		caption = "Back";
		length = (int) g.getFontMetrics().getStringBounds(caption , g).getWidth();
		g2d.drawImage(buttonImage , (int) back.getX() , (int) back.getY() , (int) back.getWidth() , (int) back.getHeight() , null);
		g2d.drawString(caption , (int) (back.getX() + back.getWidth() / 2 - length / 2) , (int) back.getY() + standardGap / 2);
	}

	public void update() {
		backGroundFrame++;
		if (backGroundFrame == backGroundImages.size() - 1) {
			backGroundFrame = 0;
		}
	}
}
