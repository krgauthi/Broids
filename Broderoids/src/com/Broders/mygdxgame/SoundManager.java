package com.Broders.mygdxgame;

import java.util.HashMap;

import com.Broders.Logic.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * This is a static class that loads and mangages all of the sounds that play in
 * the game. Playing a sound means calling a method from this class, which then plays
 * the sound itself. This helps keep memory usage down. The volume of all Sounds are
 * modified by the volume settings set by the user.
 * 
 */
public class SoundManager {

	// Store all of the sounds of the game.
	private static HashMap<String, Sound> sounds;

	// The background music of the game. Is stored as a Music object or a
	// Sound object depending upon the device being played on.
	private static Music music;
	private static Sound muzak;
	
	private static long muzakId;
	private static float sv; // Sounds volume
	private static float mv; // Music volume
	private static boolean android;

	public static void init() {
		
		if (Gdx.app.getVersion() > 0)
			android = true;

		sounds = new HashMap<String, Sound>();

		// All of the sounds, plus the strings used to id them.
		String[][] defaultSounds = {
			{ "death", "data/shipboom.mp3" },
			{ "roidBreak1", "data/asteroidboom1.wav" },
			{ "roidBreak2", "data/asteroidboom2.wav" },
			{ "roidBreak3", "data/asteroidboom3.wav" },
			{ "pew", "data/shot.mp3" },
			{ "zoom", "data/thrust.mp3" },
			{ "click", "data/menushift.mp3" },
			{ "respawn", "data/shiprespawn.mp3" },
			{ "start", "data/roundstart.wav" },
			{ "error", "data/error.wav" }
		};

		// Loading all of the sounds into the HashMap.
		for (String[] noise : defaultSounds) {
			Sound temp = Gdx.audio.newSound(Gdx.files.internal(noise[1]));
			sounds.put(noise[0], temp);
		}

		update();

		/* Android devices can't handle the larger Sound format, which gives a few play 
		 * options that we take advantage of, so it is stored as a smaller Music object on
		 * those devices.
		 */ 
		if (android) {
			music = Gdx.audio.newMusic(Gdx.files.internal("data/broderoids.mp3"));
			music.play();
			music.setLooping(true);
		} else {
			muzak = Gdx.audio.newSound(Gdx.files.internal("data/broderoids.mp3"));
			muzakId = muzak.loop(mv);
		}
	}

	/**
	 * Returns the Sound that is identified by the String argument.
	 * 
	 * @param key
	 * 			A unique string key
	 * @return the corresponding Sound
	 */
	public static Sound get(String key) {
		return sounds.get(key);
	}

	/**
	 * Plays the Sound identified by the String argument at normal volume.
	 * 
	 * @param key
	 * 			A unique string key
	 * @return the unique id of the instance of the Sound being played
	 */
	public static long play(String key) {
		update();
		return sounds.get(key).play(sv);
	}

	/**
	 * Plays the Sound identified by the String argument at the volume specified.
	 * 
	 * @param key
	 * 			A unique string key
	 * @param volume
	 * 			The Sound volume (between 0f and 1f)
	 * @return the unique id of the instance of the Sound being played
	 */
	public static long play(String key, float volume) {
		update();
		return sounds.get(key).play(sv * volume);
	}

	/**
	 * Plays the Sound identified by the String argument at the volume specified,
	 * modulated by the pitch specified.
	 * 
	 * @param key
	 * 			A unique string key
	 * @param volume
	 * 			The Sound volume (between 0f and 1f)
	 * @param pitch
	 * 			The Sound pitch (between 0.5f and 2f)
	 * @return the unique id of the instance of the Sound being played
	 */
	public static long play(String key, float volume, float pitch) {
		update();
		Sound clip = sounds.get(key);

		long id = clip.play(sv * volume);
		clip.setPitch(id, pitch);

		return id;
	}

	/**
	 * Sets the pitch of the Sound instance identified by the key string and ID 
	 * number. 
	 * 
	 * @param key
	 * 			A unique string key
	 * @param id
	 * 			The unique id of the instance of the Sound.
	 * @param pitch
	 * 			The Sound pitch (between 0.5f and 2f)
	 */
	public static void setPitch(String key, long id, float pitch) {
		sounds.get(key).setPitch(id, pitch);
	}

	/**
	 * Sets the volume of the Sound instance identified by the key string and ID 
	 * number. 
	 * 
	 * @param key
	 * 			A unique string key
	 * @param id
	 * 			The unique id of the instance of the Sound.
	 * @param pitch
	 * 			The Sound pitch (between 0f and 1f)
	 */
	public static void setVolume(String key, long id, float volume) {
		update();
		sounds.get(key).setVolume(id, volume * sv);
	}

	/**
	 * Stops all Sounds being played. Releases all allocated memory. Called to
	 * clean up at close.
	 */
	public static void dispose() {
		for (Sound noise : sounds.values()) {
			noise.stop();
			noise.dispose();
		}
		if (android) {
			music.stop();
			music.dispose();
		} else {
			muzak.stop();
			muzak.dispose();
		}
		sounds = null;
	}

	/**
	 * Sets the volume of the background music.
	 * 
	 * @param volume
	 *			The music volume (between 0f and 1f)
	 */
	public static void setMusicVolume(float volume) {
		update();

		if (android) {
			music.setVolume(volume);
		} else {
			muzak.setVolume(muzakId, volume * 0.1f);
		}

		System.out.println("Volume parameter = " + volume);
	}

	/**
	 * Sets the pitch of the background music.
	 * 
	 * @param pitch
	 * 			The music pitch (between 0.5f and 2f)
	 */
	public static void setMusicPitch(float pitch) {
		update();
		if (!android) {
			muzak.setPitch(muzakId, pitch);
		}
	}

	/**
	 * Makes sure the overall sound and music volumes are up-to-date with the user's
	 * settings.
	 */
	public static void update() {
		mv = Settings.getMusicVol() * 0.1f;
		sv = Settings.getSoundVol() * 0.1f;
	}
}
