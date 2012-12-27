package com.Broders.Logic;

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
	public static String data_path = "data/";
	private static Preferences prefs;

	private static String username;
	private static Color shipColor;
	private static Color worldColor;
	private static int soundVol;
	private static int musicVol;
	private static int width;
	private static int height;
	private static boolean debug;
	private static int difficulty;
	private static boolean epileptic;
	private static boolean retro;
	private static boolean nameDis; 		//TODO option to change

	private static String[] defaultUsernames = {
	"Bro", "Broski", "Bromo", "Brotien", "Brodeo", "Broku", "Brohan", "Brochill", "Brosicle", "Broseph", "Brocean", "Brotastic", "Brofessor", "Han Brolo", "Brosideon", "Broba Fett", "Brohemian", "Brotato Chip", "Brohammad", "Brohan Solo", "Bro Montana", "Bromosexual", "Bromosapien", "Brotien Shake", "Brorontosaurus", "Broseph Stalin", "Abroham Lincoln", "Brorack Brobama", "Broseph Goebbels", "Brorannasarus Rex", "Edgar Allen Bro", "Luke Browalker", "Brolbo Baggins", "Brodo Baggins", "Tron: Brogacy", "Doctor Bro", "Darth Broder", "BroNotFoundException", };

	public static void init() {
		prefs = Gdx.app.getPreferences("broids-prefs");
		load();

	}

	public static void load() {
		int bro = (int) (defaultUsernames.length * Math.random());
		username = prefs.getString("username", defaultUsernames[bro]);
		shipColor = colorFromHexString("FF" + prefs.getString("shipColor", "00FF00"));
		worldColor = colorFromHexString("FF" + prefs.getString("worldColor", "44DDEE"));
		if (Gdx.app.getVersion() > 0) {
			width = prefs.getInteger("width", Gdx.graphics.getWidth());
			height = prefs.getInteger("height", Gdx.graphics.getHeight());
		} else {
			width = prefs.getInteger("width", 1024);
			height = prefs.getInteger("height", 576);
		}
		soundVol = prefs.getInteger("soundVol", 5);
		musicVol = prefs.getInteger("musicVol", 5);
		difficulty = prefs.getInteger("difficulty", 1);
		debug = prefs.getBoolean("debug", false);
		epileptic = prefs.getBoolean("epileptic", false);
		retro = prefs.getBoolean("retro", false);
		
		setNameDis(true);

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

	public static int getDifficulty() {
		return difficulty;
	}

	public static void setDifficulty(int difficulty) {
		prefs.putInteger("difficulty", difficulty);
		prefs.flush();
		load();
	}

	public static Color getWorldColor() {
		return worldColor;
	}

	public static Color getShipColor() {
		return shipColor;
	}

	public static void setShipColor(String color) {
		// TODO: Better safety checks
		if (color.length() == 6) {
			prefs.putString("shipColor", color);
			prefs.flush();
			load();
		}
	}

	public static void setWorldColor(String color) {
		// TODO: Better safety checks
		if (color.length() == 6) {
			prefs.putString("worldColor", color);
			prefs.flush();
			load();
		}
	}

	public static void setWidth(int width) {
		prefs.putInteger("width", width);
		prefs.flush();
		load();
	}

	public static void setHeight(int height) {
		prefs.putInteger("height", height);
		prefs.flush();
		load();
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	// Mikes stuff (OLD)

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
	// Must be of the form 0xAARRGGBB
	// @return the generated Color object
	private static Color colorFromHex(long hex) {
		float a = (hex & 0xFF000000L) >> 24;
		float r = (hex & 0xFF0000L) >> 16;
		float g = (hex & 0xFF00L) >> 8;
		float b = (hex & 0xFFL);

		return new Color(r / 255f, g / 255f, b / 255f, a / 255f);
	}

	// Expects a hex value as String and returns the appropriate Color object
	// @param s The hex string to create the Color object from
	// @return

	public static Color colorFromHexString(String s) {
		if (s.startsWith("0x")) {
			s = s.substring(2);
		}

		if (s.length() != 8) { // AARRGGBB
			throw new IllegalArgumentException("String must have the form AARRGGBB");
		}

		return colorFromHex(Long.parseLong(s, 16));
	}

	public static boolean isNameDis() {
		return nameDis;
	}

	public static void setNameDis(boolean nameDis) {
		Settings.nameDis = nameDis;
	}

}
