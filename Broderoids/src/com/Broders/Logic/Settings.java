package com.Broders.Logic;

import com.Broders.mygdxgame.BaseGame;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;

/* Settings Object class to be used as a modular way to
 * access/set/read settings.
 * 
 * A lot of the settings in here aren't currently used, and
 * are just implemented as a boolean. Many of these options don't
 * have a corresponding boolean/enum/etc in the base game yet,
 * as we haven't quite figured out how to implement them, or
 * if we're even going to implement them. Maybe I shouldn't
 * have made them in the first place if we we'r'en't sure we were
 * going to use them... whoops.
 * 
 * @TimeAnnotation("O(n)") lol just kidding
 */
public class Settings {

	private static BaseGame game;
	public static String data_path = "data/";
	private static Preferences prefs;
	
	private static String username;
	private static Color shipColor;
	private static Color worldColor;
	private static int soundVol;
	private static int musicVol;
	// TODO: Resolution?
	private static boolean debug;
	private static int difficulty;
	private static boolean epileptic;
	private static boolean retro;
	
	private static String[] defaultUsernames = {
		"Bro",
		"Broski",
		"Bromo",
		"Brotien",
		"Brodeo",
		"Brohan",
		"Brochill",
		"Brosicle",
		"Broseph",
		"Brocean",
		"Brotastic",
		"Brofessor",
		"Han Brolo",
		"Brosideon",
		"Broba Fett",
		"Brohemian",
		"Brotato Chip",
		"Brohammad",
		"Brohan Solo",
		"Bro Montana",
		"Bromosexual",
		"Bromosapien",
		"Brotien Shake",
		"Brorontosaurus",
		"Broseph Stalin",
		"Abroham Lincoln",
		"Brorack Brobama",
		"Broseph Goebbels",
		"Brorannasarus Rex",
		"Edgar Allen Bro",
	};
	
	public static void init(BaseGame mygame) {
		game = mygame;
		prefs = Gdx.app.getPreferences("broids-prefs");
	}
	
	public static void load() {
		int bro = (int) (defaultUsernames.length * Math.random());
		username = prefs.getString("username", defaultUsernames[bro]);
		// TODO: Load ShipColor
		// TODO: Load WorldColor
		// TODO: Load resolution
		soundVol = prefs.getInteger("soundVol", 5);
		musicVol = prefs.getInteger("musicVol", 5);
		difficulty = prefs.getInteger("difficulty", 0);
		debug = prefs.getBoolean("debug", false);
		epileptic = prefs.getBoolean("epileptic", false);
		retro = prefs.getBoolean("retro", false);
	}

	public static boolean getRetro() {
		return retro;
	}

	public static void setRetro(boolean retro) {
		prefs.putBoolean("retro", retro);
		prefs.flush();
		load();
	}
	
	public static boolean getDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		prefs.putBoolean("debug", debug);
		prefs.flush();
		load();
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		prefs.putString("username", username);
		prefs.flush();
		load();
	}

	public static boolean getEpileptic() {
		return epileptic;
	}

	public static void setEpileptic(boolean epileptic) {
		prefs.putBoolean("epileptic", epileptic);
		prefs.flush();
		load();
	}

	public static int getSoundVol() {
		return soundVol;
	}

	public static void setSoundVol(int soundVol) {
		prefs.putInteger("soundVol", soundVol);
		prefs.flush();
		load();
	}

	public static int getMusicVol() {
		return musicVol;
	}

	public static void setMusicVol(int musicVol) {
		prefs.putInteger("musicVol", musicVol);
		prefs.flush();
		load();
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		prefs.putInteger("difficulty", difficulty);
		prefs.flush();
		load();
	}

	public void loadShipColor(String value) {
		value = value.replaceAll("#", "").trim();
		value = "FF" + value;
		game.playerColor.set(colorFromHexString(value));
	}
	
	public void loadWorldColor(String value) {
		value = value.replaceAll("#", "").trim();
		value = "FF" + value;
		game.gameColor.set(colorFromHexString(value));
	}
		
	public void loadSoundVolume(String value) {
		int vol = 0;
		try {
			vol = Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			vol = 5;
		}
		if (vol > 10) {
			vol = 10;
		} else if (vol < 0) {
			vol = 0;
		}
		game.soundVolume = vol;
		System.out.printf("Loaded volume setting [%d] from config file%n", vol);
	}
		
	public void loadMusicSetting(String value) {

		int vol = 0;
		try {
			vol = Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			vol = 5;
		}
		if (vol > 10) {
			vol = 10;
		} else if (vol < 0) {
			vol = 0;
		}
		game.musicVolume = vol;
		System.out.printf("Loaded music setting [%d] from config file%n", vol);
	}
	
	public void loadResolution(String value) {
		String[] sizeStrings = value.split("x");
		int[] res = {
			Integer.parseInt(sizeStrings[0].trim()),
			Integer.parseInt(sizeStrings[1].trim())
		};
		if (!(game.screenWidth == res[0] && game.screenHeight == res[1])) {
			game.getScreen().resize(res[0], res[1]);
			game.screenWidth = res[0];
			game.screenHeight = res[1];
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
		try {
			game.difficulty = Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
			game.difficulty = 0;
		}
		System.out.printf("Loaded SP Difficulty setting [%d] from config" +
				" file%n", game.difficulty);
	}
	
	public void loadEpilepticModeSetting(String value) {
		game.epileptic = Boolean.parseBoolean(value) ? true : false;
		System.out.printf("Loaded epileptic mode setting [%s] from config file%n",
				Boolean.toString(game.epileptic));
	}
	
	public static String swapHex(String hex) {
		String bgr = hex.substring(2); // Why libgdx???
		String red = bgr.substring(4);
		String green = bgr.substring(2, 4);
		String blue = bgr.substring(0, 2);
		return red + green + blue;
	}
	
	// Following 2 methods totally stolen from:
	// http://code.google.com/p/libgdx-users/wiki/ColorHexConverter
	// Author: Michael.Lowfyr (Michael.Lowfyr@gmail.com)
	
    // Expects a hex value as integer and returns the appropriate Color object.
    // @param hex
    //            Must be of the form 0xAARRGGBB
    // @return the generated Color object
   private Color colorFromHex(long hex)
   {
           float a = (hex & 0xFF000000L) >> 24;
           float r = (hex & 0xFF0000L) >> 16;
           float g = (hex & 0xFF00L) >> 8;
           float b = (hex & 0xFFL);
                           
           return new Color(r/255f, g/255f, b/255f, a/255f);
   }

  
    // Expects a hex value as String and returns the appropriate Color object
    // @param s The hex string to create the Color object from
    // @return
   
   public Color colorFromHexString(String s)
   {               
           if(s.startsWith("0x"))
                   s = s.substring(2);
           
           if(s.length() != 8) // AARRGGBB
                   throw new IllegalArgumentException("String must have the form AARRGGBB");
                   
           return colorFromHex(Long.parseLong(s, 16));
   }
}
