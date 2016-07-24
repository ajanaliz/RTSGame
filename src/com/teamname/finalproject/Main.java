package com.teamname.finalproject;

import com.teamname.finalproject.util.SoundEffect;
import com.teamname.finalproject.util.SpriteSheet;

import java.awt.*;
import java.util.Collections;

public class Main {
	public static void main(String[] args) {
        KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kfm.setDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
        kfm.setDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
		SpriteSheet.init();
		SoundEffect.init();
		new Window();
		SoundEffect.MAINMENU.play();
	}
}