package com.Broders.mygdxgame;
import java.util.HashMap;

import com.Broders.Logic.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

	private static HashMap<String, Sound> sounds;

	private static Music music;
	private static Sound muzak;
	private static long muzakId;
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
			music.play();
			music.setLooping(true);
		} else {
			muzak = Gdx.audio.newSound(Gdx.files.internal("data/broderoids.mp3"));
			muzakId = muzak.loop();
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
		if (android) {
			music.stop();
			music.dispose();
		} else { 
			muzak.stop();
			muzak.dispose();
		}
		sounds = null;
	}
	
	public static void setMusicVolume(float volume) {
		update();
			
		if (android) {
			music.setVolume(volume);
		} else {
			muzak.setVolume(muzakId, volume);
		}
		
		System.out.println("Volume parameter = " + volume);
	}
	
	public static void setMusicPitch(float pitch) {
		update();
		if (!android);
			muzak.setPitch(muzakId, pitch);
	}

	public static void update() {
		sv = Settings.getSoundVol() * 0.1f;
	}
}
