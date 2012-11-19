package com.Broders.Logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.Broders.mygdxgame.BaseGame;

/* Settings Object class to be used as a modular way to
 * access/set/read settings.
 * 
 * A lot of the settings in here aren't currently used, and
 * are just implemented as a boolean. Many of these options don't
 * have a corresponding boolean/enum/etc in the base game yet,
 * as we haven't quite figured out how to implement them, or
 * if we're even going to implement them. Maybe I shouldn't
 * have made them in the first place if we we'rent sure we were
 * going to use them... whoops.
 * 
 * @TimeAnnotation("O(n)") lol just kidding
 */
public class Settings {

	BaseGame game;
	public static String data_path = "data/";
	
	public Settings(BaseGame game) {
		this.game = game;
	}
	
	// Main method for loading settings from settings file
	// Broken up into individual methods for maintainability
	public void loadSettings() throws FileNotFoundException {
		Scanner s = new Scanner(new File("config/broids.cfg"));
		while (s.hasNextLine()) {
			String line = s.nextLine();
			String[] tokens = line.split(":");
			String option = tokens[0].toLowerCase().trim();
			String value = tokens[1].toLowerCase().trim();

			// checks option to see which option is being read
			// and then sets that option to the value.
			checkSettings(option, value);
		}
	}
	
	private void checkSettings(String option, String value) {
		if ( option.equals("username")) {
			loadUsername(value);
		} else if (option.equals("ship color")) {
			loadShipColor(value);
		} else if (option.equals("background")) {
			loadBackgroundSetting(value);
		} else if (option.equals("volume")) {
			loadVolumeSetting(value);
		} else if (option.equals("sounds")) {
			loadSoundSetting(value);
		} else if (option.equals("music")) {
			loadMusicSetting(value);
		} else if (option.equals("resolution")) {
			loadResolution(value);
		} else if (option.equals("debug")) {
			loadDebugSetting(value);
		} else if (option.equals("sp difficulty")) {
			loadSPDiffSetting(value);
		} else if (option.equals("epileptic")) {
			loadEpilepticModeSetting(value);
		}
	}

	public void loadUsername(String value) {
		System.out.printf("Loaded username [%s] from config file%n", value);
	}
	
	public void loadShipColor(String value) {
		value = value.replaceAll("#", "").trim();
		int r = Integer.parseInt(value.substring(0, 2), 16);
		int g = Integer.parseInt(value.substring(2, 4), 16);
		int b = Integer.parseInt(value.substring(4, 6), 16);
		int a = 0xFF;
		game.playerColor.set((float) r, (float) g, (float) b, (float) a);
		System.out.printf("Loaded ship color [%02x%02x%02x] from config file%n",
				r, g, b);
	}
	
	// still need to figure out what we're doing for this, if anything
    // maybe density of stars, no stars, etc..
	public void loadBackgroundSetting(String value) {
		System.out.printf("Loaded background setting [%s] from config file%n", 
				Boolean.toString(Boolean.parseBoolean(value)));
	}
	
	public void loadVolumeSetting(String value) {
		int vol = Integer.parseInt(value);
		System.out.printf("Loaded volume setting [%d] from config file%n", vol);
	}
	
	// perhaps different sound packs?
	public void loadSoundSetting(String value) {
		System.out.printf("Loaded sound setting [%s] from config file%n", 
				Boolean.toString(Boolean.parseBoolean(value)));
	}
	
	public void loadMusicSetting(String value) {
		System.out.printf("Loaded music setting [%s] from config file%n", 
				Boolean.toString(Boolean.parseBoolean(value)));
	}
	
	public void loadResolution(String value) {
		String[] sizeStrings = value.split("x");
		int[] res = {
			Integer.parseInt(sizeStrings[0].trim()),
			Integer.parseInt(sizeStrings[1].trim())
		};
		if (!(game.screenWidth == res[0] && game.screenHeight == res[1])) {
			game.getScreen().resize(res[0], res[1]);
			System.out.println(res[0]);
			System.out.println(res[1]);
			System.out.printf("Set Screen size to %d x %d%n", res[0], res[1]);
		} else {
			System.out.printf("Screen size is already set to [%d x %d]," +
					" leaving alone%n", res[0], res[1]);
		}
	}
	
	public void loadDebugSetting(String value) {
		game.debugMode = Boolean.parseBoolean(value) ? true : false;
		System.out.printf("Loaded debug setting [%s] from config file%n",
				Boolean.toString(game.debugMode));
	}
	
	public void loadSPDiffSetting(String value) {
		System.out.printf("Loaded SP Difficulty setting [%d] from config" +
				" file%n", Integer.parseInt(value));
	}
	
	public void loadEpilepticModeSetting(String value) {
		game.epileptic = Boolean.parseBoolean(value) ? true : false;
		System.out.printf("Loaded epileptic mode setting [%s] from config file%n",
				Boolean.toString(game.epileptic));
	}
}
