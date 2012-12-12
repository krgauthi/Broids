package com.Broders.mygdxgame;
import java.util.HashMap;

import com.Broders.Logic.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public class SoundManager {

	private static HashMap<String, Sound> sounds;

	private static Disposable music;
	private static long musicId;
	private static float mv;
	private static float sv;
	private static boolean android;

		

	public static void init() {
		
		android = Gdx.app.getVersion() > 0;
		sounds = new HashMap<String, Sound>();

		String[][] defaultSounds = {
				{"death","data/shipboom.mp3"},
				{"roidBreak1", "data/asteroidboom1.wav"},
				{"roidBreak2", "data/asteroidboom2.wav"},
				{"roidBreak3", "data/asteroidboom3.wav"},
				{"pew","data/shot.mp3"},
				{"zoom", "data/thrust.mp3"},
				{"click", "data/menushift.mp3"},
				{"respawn", "data/shiprespawn.mp3"},
				{"start", "data/roundstart.wav"},
				{"error", "data/error.wav"}
		};
		

		for (String[] noise : defaultSounds) {
			Sound temp = Gdx.audio.newSound(Gdx.files.internal(noise[1]));
			sounds.put(noise[0], temp);
		}
		
		//if android
		if (android) {
			music = Gdx.audio.newMusic(Gdx.files.internal("data/broderoids.mp3"));
			((Music) music).play();
			((Music) music).setLooping(true);
		} else {
			music = Gdx.audio.newSound(Gdx.files.internal("data/broderoids.mp3"));
			((Sound) music).play();
			((Sound) music).loop();
		}

	}

	public static Sound get(String key) {
		return sounds.get(key);
	}

	public static long play(String key) {
		update();
		return sounds.get(key).play(sv);
	}

	public static long play(String key, float volume) {
		update();
		return sounds.get(key).play(sv * volume);
	}

	public static long play(String key, float volume, float pitch) {
		update();
		Sound clip = sounds.get(key);

		long id = clip.play(sv * volume);
		clip.setPitch(id, pitch);
		
		return id;
	}

	public static void setPitch(String key, long id, float pitch) {
		sounds.get(key).setPitch(id, pitch);
	}

	public static void setVolume(String key, long id, float volume) {
		update();
		sounds.get(key).setVolume(id, volume);
	}

	public static void dispose() {
		for (Sound noise : sounds.values()) {
			noise.stop();
			noise.dispose();
		}
		if (android)
			((Music) music).stop();
		else 
			((Sound) music).stop();
		music.dispose();
		sounds = null;
	}
	
	public static void setMusicVolume(float volume) {
		update();
		if (android)
			((Music) music).setVolume(mv);
		else
			((Sound) music).setVolume(musicId, mv);
	}
	
	public static void setMusicPitch(float pitch) {
		if (!android);
			((Sound) music).setPitch(musicId, pitch);
	}

	public static void update() {
		mv = Settings.getMusicVol() * 0.1f;
		sv = Settings.getSoundVol() * 0.1f;
	}
}
