package com.teamname.finalproject.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ali J on 6/7/2015.
 */


/*the constructor of this class takes 2 parameters,first is the path to the image and the second is the number of sprites(frames) in the image*/
public enum SpriteSheet {


    KINGIDEAL("res//KingIdle_368.png", 368),
    KINGWALKING("res//KingWalking_280.png", 280),
    KINGFIGHTING("res//KingFighting_256.png", 256),
    KINGWALKTOFIGHT("res//KingWalkingToFight_272.png", 272),
    KINGDOM1("res//Kingdom1.png", 1),
    KINGDOM2("res//Kingdom2.png", 1),
    KINGDOM3("res//Kingdom3.png", 1),
    KINGDOM4("res//Kingdom4.png", 1),
    WHITEFISH("res//whitefish.png", 1),

    PEASANTIDLE("res//workerIDLE.png", 8),
    PEASANTWALKING("res//workerwalk.png", 120),
    PEASANTHAMMERING("res//BlackSmithHammering_256.png", 256),
    PEASANTWALKTOWORK("res//BlackSmithMoveMent_256.png", 256),
    UIBACKGROUND("res//resuibg.png",1),
    PEASANTWALKTOFIGHTT1("res//TemplarCharging_176.png", 176),
    BUTTON("res//button.png",1),

    PEASANTFIGHTINGT1("res//TemplarFighting_280.png", 280),

    FERRY("res//Ferry_8.png", 8),
    FISHINGBOAT("res//FishingBoat_8.png", 8),
    BACKGROUNDIMAGE("res//website background.png",1),
    FISHINGBOAT1("res//manowar.png", 1),
    FISHINGBOAT2("res//merchantman.png", 1),
    warship3("res//frigate.png", 1),
    TREE("res//Lumber.png", 1),
    METAL("res//Rock.png", 1),
    FISH("res//fish.png", 1),
    PEASANT1("res//peasant1.png", 1),
    KING("res//peasant1.png", 1),
    WIZARD("res//Wizard.png", 1),
    WAVE1("res//wave.png",1),
    WAVE2("res//wave2.png",1),
    WAVE3("res//wave3.png",1),
    WIZARDIDEAL("res//WizardIDLE_168.png", 168),
    WIZARDFIGHTING("res//WizardSpellCasting_280.png", 280),
    WIZARDWALKING("res//WizardWalking_400.png", 400),
    WARSHIPSAILINGT1("res//WarShpSailing_8.png",8),
    WARSHIPSHOOTINGT1("res//WarShpShooting_8.png",8),
    WARSHIPIDEAL("res//WarShpIdle_8.png",8);

    private BufferedImage spriteSheet;
    private ArrayList<BufferedImage> sprites;
    private int width, height;

    private SpriteSheet(String spritePath, int frames) {
        try {
            spriteSheet = ImageIO.read(new File(spritePath));
            sprites = new ArrayList<BufferedImage>();
            width = spriteSheet.getWidth();
            height = spriteSheet.getHeight() / frames;
            for (int i = 0; i < frames; i++) {
                BufferedImage temp = spriteSheet.getSubimage(0, i * height, width, height);
                sprites.add(temp);
            }
            System.out.println( spritePath + " loaded successfully..");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BufferedImage> getSprites() {
        return sprites;
    }

    public int getFramesPerDir() {
        return sprites.size() / 8;
    }

    public static void init() {
        values(); // calls the constructor for all the elements,the good thing about this is all the values will be initialized,therefor if we call on this function at the beginning of our program,we wont need to load the images again :D
    }
}
