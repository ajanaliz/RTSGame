package com.teamname.finalproject.util;

/**
 * Created by Ali J on 4/29/2015.
 */

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public enum SoundEffect {
    THEME("sounds/PeaceTime.wav"),
    HARMONY("sounds/Harmony.wav"),
    MAINMENU("sounds/mainmenu.wav"),
    PREVIEW("sounds/preview.wav"),
    GAME("sounds/medmusic.wav"),
    GREETINGSADVENTURER("sounds/greetings_adventurer.wav"),
    NEW_SEASON("sounds/new_season.wav"),
    FAMINE("sounds/our_land.wav"),
    NPC_ALRIGHT("sounds/alright.wav"),
    NPC_HURRYUP("sounds/hurry_up.wav"),
    NPC_NEED_SOME_SLEEP("sounds/any_sleep.wav"),
    NPC_WHISTLE("sounds/whistle.wav"),
    NPC_WHINE("sounds/arghhh.wav"),
    NPC_WHAT_ARE_YOU_WAITING_FOR("sounds/what_are_you_waiting_for.wav"),
    NPC_WHINE2("sounds/know_what_its_like_here.wav"),
    NPC_WHINE3("sounds/when_i_was.wav"),
    NPC_WHINE4("sounds/when_will_it_end.wav"),
    NPC_WELL_WELL_WELL("sounds/well_well_well.wav"),
    NPC_Ill_HELP("sounds/ill_help_you.wav"),
    NPC_POOR_CRYING("sounds/every_night_every_day.wav"),
    NPC_COUGH("sounds/cough.wav"),
    NPC_IM_HERE_TO_SERVE("sounds/im_here_to_serve_you.wav"),
    NPC_GREETING("sounds/greetings_sir.wav"),
    NPC_CONFIRMATION("sounds/oh_i_see.wav"),
    NPC_CONFIRMATION2("sounds/so_much_to_do.wav"),
    NPC_CONFIRMATION3("sounds/time_of_day.wav"),
    NPC_CONFIRMATION4("sounds/yipee.wav"),
    NPC_CONFIRMATION5("sounds/your_eager.wav"),
    NPC_GOODDAY("sounds/good_day_to_you_sir.wav"),
    NPC_SOLDIER_SPEECH1("sounds/crying.wav"),
    NPC_SOLDIER_SPEECH2("sounds/danger_is_ahead.wav"),
    NPC_SOLDIER_SPEECH3("sounds/dont_think_we_have_a_chanse.wav"),
    NPC_SOLDIER_SPEECH4("sounds/dymiss_fear.wav"),
    NPC_SOLDIER_SPEECH5("sounds/raise_to_the_kingdom.wav"),
    NPC_SOLDIER_SPEECH6("sounds/seem_very_charming.wav"),
    NPC_WARCRY1("sounds/by_deaths_door.wav"),
    NPC_WARCRY2("sounds/you_expect_riches.wav"),
    NPC_WARCRY3("sounds/execution.wav"),
    NPC_WARCRY4("sounds/fear_my_be_within_us.wav"),
    NPC_WARCRY5("sounds/find_your_win.wav"),
    NPC_WARCRY6("sounds/in_our_freedom.wav"),
    NPC_WARCRY7("sounds/in_our_hearts.wav"),
    NPC_WARCRY8("sounds/off_with_his_head.wav"),
    NPC_WARCRY9("sounds/stand_tall.wav"),
    NPC_WARCRY10("sounds/surrender_is_not_what_we_are_about.wav"),
    NPC_WARCRY11("sounds/we_must_battle.wav");

    // Nested class for specifying volume
    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.LOW;
    // Each sound effect has its own clip, loaded with its own sound file.
    private Clip clip;
    private Random random = new Random();

    // Constructor to construct each element of the enum with its own sound
    // file.
    private SoundEffect(String soundFileName) {
        try {
            File dot = new File(".");
            System.out.println("current dir is: " + dot.getCanonicalPath());

            String soundFileLocation = dot.getCanonicalPath() + "/"
                    + soundFileName;
            File soundFile = new File(soundFileLocation);
            System.out
                    .println("Sound file is: " + soundFile.getCanonicalPath());
            if (soundFile.exists()) {
                System.out.println("File exists");
                if (soundFile.canRead())
                    System.out.println("Can Read...");
            }

            Class cl = this.getClass();
            System.out.println("Class toString is:" + cl.toString());
            System.out.println("Class name is:" + cl.getCanonicalName());

            // Use URL (instead of File) to read from disk and JAR.
            URL url = this.getClass().getResource(soundFileName);
            // URL url =
            // this.getClass().getClassLoader().getResource(soundFileLocation);

            System.out.println(" URL = " + url.getPath());

            // Set up an audio input stream piped from the sound file.
            AudioInputStream audioInputStream = AudioSystem
                    .getAudioInputStream(url);
            // AudioInputStream audioInputStream =
            // AudioSystem.getAudioInputStream( soundFile );

            System.out.println(" input stream is: "
                    + audioInputStream.toString());

            // Get a clip resource.
            clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("ERROR!! " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
    }

    // Play or Re-play the sound effect from the beginning, by rewinding.
    public synchronized void play() {
        if (volume != Volume.MUTE) {
            if (clip.isRunning())
                clip.stop(); // Stop the player if it is still running
            clip.setFramePosition(0); // rewind to the beginning
            clip.start(); // Start playing
        }
    }

    public synchronized void stop() {
        if (volume != Volume.MUTE) {
            if (clip.isRunning())
                clip.stop(); // Stop the player if it is still running
            clip.setFramePosition(0); // rewind to the beginning
        }
    }

    public synchronized void continuePlaying() {
        if (volume != Volume.MUTE) {
            if (clip.isRunning())
                clip.stop(); // Stop the player if it is still running
            if (clip.getFrameLength() - 5 <= clip.getFramePosition()) {
                clip.setFramePosition(0);
            }
            clip.start(); // Start playing
        }
    }

    public synchronized void pause() {
        if (clip.isRunning()) {
            clip.stop();
        }
    }


    public synchronized void npcConfirmation() {
        npcSilence();
        switch (random.nextInt(16)) {
            case 0:
                NPC_WHINE.play();
                break;
            case 1:
                NPC_WHINE2.play();
                break;
            case 2:
                NPC_WHINE3.play();
                break;
            case 3:
                NPC_WHINE4.play();
                break;
            case 4:
                NPC_CONFIRMATION.play();
                break;
            case 5:
                NPC_CONFIRMATION2.play();
                break;
            case 6:
                NPC_CONFIRMATION3.play();
                break;
            case 7:
                NPC_CONFIRMATION4.play();
                break;
            case 8:
                NPC_CONFIRMATION5.play();
                break;
            case 9:
                NPC_ALRIGHT.play();
                break;
            case 10:
                NPC_WHAT_ARE_YOU_WAITING_FOR.play();
                break;
            case 11:
                NPC_NEED_SOME_SLEEP.play();
                break;
            case 12:
                NPC_HURRYUP.play();
                break;
            case 13:
                NPC_WHISTLE.play();
                break;
            case 14:
                NPC_Ill_HELP.play();
                break;
            case 15:
                NPC_WELL_WELL_WELL.play();
                break;
            case 16:
                NPC_COUGH.play();
                break;
        }
    }

    public void npcSilence() {
        NPC_IM_HERE_TO_SERVE.stop();
        NPC_GREETING.stop();
        NPC_GOODDAY.stop();
        NPC_POOR_CRYING.stop();
        NPC_WHINE.stop();
        NPC_WHINE2.stop();
        NPC_WHINE3.stop();
        NPC_WHINE4.stop();
        NPC_WHISTLE.stop();
        NPC_WARCRY1.stop();
        NPC_WARCRY2.stop();
        NPC_WARCRY3.stop();
        NPC_WARCRY4.stop();
        NPC_WARCRY5.stop();
        NPC_WARCRY6.stop();
        NPC_WARCRY7.stop();
        NPC_WARCRY8.stop();
        NPC_WARCRY9.stop();
        NPC_WARCRY10.stop();
        NPC_WARCRY11.stop();
        NPC_SOLDIER_SPEECH1.stop();
        NPC_SOLDIER_SPEECH2.stop();
        NPC_SOLDIER_SPEECH3.stop();
        NPC_SOLDIER_SPEECH4.stop();
        NPC_SOLDIER_SPEECH5.stop();
        NPC_SOLDIER_SPEECH6.stop();
        NPC_CONFIRMATION.stop();
        NPC_CONFIRMATION2.stop();
        NPC_CONFIRMATION3.stop();
        NPC_CONFIRMATION4.stop();
        NPC_CONFIRMATION5.stop();
        NPC_ALRIGHT.stop();
        NPC_WHAT_ARE_YOU_WAITING_FOR.stop();
        NPC_NEED_SOME_SLEEP.stop();
        NPC_HURRYUP.stop();
        NPC_Ill_HELP.stop();
        NPC_WELL_WELL_WELL.stop();
        NPC_COUGH.stop();
        FAMINE.stop();
        GREETINGSADVENTURER.stop();
        NEW_SEASON.stop();
    }

    public void npcSelection() {
        npcSilence();
        switch (random.nextInt()) {
            case 0:
                NPC_IM_HERE_TO_SERVE.play();
                break;
            case 1:
                NPC_GREETING.play();
                break;
            case 2:
                NPC_GOODDAY.play();
                break;
            case 3:
                NPC_POOR_CRYING.play();
                break;
            case 4:
                NPC_WHINE.play();
                break;
            case 5:
                NPC_WHINE2.play();
                break;
            case 6:
                NPC_WHINE3.play();
                break;
            case 7:
                NPC_WHINE4.play();
                break;
            case 8:
                NPC_WHISTLE.play();
                break;
        }
    }


    public void npcAttack() {
        npcSilence();
        switch (random.nextInt(12)) {
            case 0:
                NPC_WARCRY1.play();
                break;
            case 1:
                NPC_WARCRY2.play();
                break;
            case 2:
                NPC_WARCRY3.play();
                break;
            case 3:
                NPC_WARCRY4.play();
                break;
            case 4:
                NPC_WARCRY5.play();
                break;
            case 5:
                NPC_WARCRY6.play();
                break;
            case 6:
                NPC_WARCRY7.play();
                break;
            case 7:
                NPC_WARCRY8.play();
                break;
            case 8:
                NPC_WARCRY9.play();
                break;
            case 9:
                NPC_WARCRY10.play();
                break;
            case 10:
                NPC_WARCRY11.play();
                break;
        }
    }

    // Optional static method to pre-load all the sound files.
    public static void init() {
    	 values(); // calls the constructor for all the elements
    	 }
}
