package com.teamname.finalproject.game;

import com.teamname.finalproject.game.minimap.MiniMapInterface;
import com.teamname.finalproject.game.ui.ResourceInterface;
import com.teamname.finalproject.game.ui.UserInterface;

import javax.swing.*;
import java.awt.*;

public class GameTab extends JPanel {

    private static Game game;
    private static UserInterface userInterface;
    private MiniMapInterface miniMap;

    private static ResourceInterface resourceInterface;

    private static double blockSize;
    private static Dimension dimension;
    public GameTab() {
        setLayout(null);
        dimension = Toolkit.getDefaultToolkit().getScreenSize();
        blockSize = dimension.getWidth() / 20;
        userInterface = new UserInterface();
        game = new Game();
        miniMap = new MiniMapInterface();
        resourceInterface = new ResourceInterface();
        game.setMiniMap(miniMap);
        add(miniMap);// aval bayad chizi add shavad ke mikhahim roo bashad
        add(userInterface);
        add(resourceInterface);
        add(game);
    }

    public static UserInterface getUserInterface() {
        return userInterface;
    }

    public static double getBlockSize() {
        return blockSize;
    }

    public static Dimension getDimension() {
        return dimension;
    }

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        GameTab.game = game;
    }

    public static ResourceInterface getResourceInterface() {
        return resourceInterface;
    }

    public static void setResourceInterface(ResourceInterface resourceInterface) {
        GameTab.resourceInterface = resourceInterface;
    }

}
