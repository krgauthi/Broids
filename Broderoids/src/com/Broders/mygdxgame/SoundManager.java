package com.Broders.mygdxgame;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	
	private static HashMap<String, Sound> sounds;
	private static long muzakId;
	private static BaseGame g;
	private static float mv;
	private static float sv;
	
	public static void init(BaseGame game) {
		
		g = game;
		setMuzakId(0);
		
		sounds = new HashMap<String, Sound>();
		String[][] defaultSounds = {
				{"death","data/ShipBoom.mp3"},
				{"roidBreak1", "data/AsteroidBoom1.wav"},
				{"roidBreak2", "data/AsteroidBoom2.wav"},
				{"roidBreak3", "data/AsteroidBoom3.wav"},
				{"pew","data/Shot.mp3"},
				{"muzak", "data/Broderoids.mp3"},
				{"zoom", "data/Thrust.mp3"},
				{"click", "data/menuShift.mp3"},
				{"respawn", "data/ShipRespawn.mp3"},
				{"start", "data/RoundStart.wav"},
				{"error", "data/error.wav"}
		};
		
		for (String[] noise : defaultSounds) {
			Sound temp = Gdx.audio.newSound(Gdx.files.internal(noise[1]));
			sounds.put(noise[0], temp);
		}
	}
	
	public static Sound get(String key) {
		return sounds.get(key);
	}
	
	public static long play(String key) {
		update();
		if (key.equals("muzak")) {
			muzakId = sounds.get(key).play(mv);
			return muzakId;
		} else
			return sounds.get(key).play(sv);
	}
	
	public static long play(String key, float volume) {
		update();
		if (key.equals("muzak")) {
			muzakId = sounds.get(key).play(mv * volume);
			return muzakId;
		} else
			return sounds.get(key).play(sv * volume);
	}
	
	public static long play(String key, float volume, float pitch) {
		update();
		Sound clip = sounds.get(key);
		long id = 0;
		update();
		
		if (key.equals("muzak")) {
			muzakId = sounds.get(key).play(mv * volume);
			return muzakId;
		} else
			id = sounds.get(key).play(sv * volume);

		clip.setPitch(id, pitch);
		return id;
	}

	public static void setPitch(String key, long id, float pitch) {
		sounds.get(key).setPitch(id, pitch);
	}
	
	public static void setVolume(String key, long id, float volume) {
		update();
		if (key.equals("muzak")) {
			sounds.get(key).setVolume(id, volume * mv);
		} else
			sounds.get(key).setVolume(id, volume * sv);
	}
	
	public static void dispose() {
		for (Sound noise : sounds.values()) {
			noise.dispose();
		}
		sounds = null;
	}

	public static long getMuzakId() {
		return muzakId;
	}

	public static void setMuzakId(long muzakId) {
		SoundManager.muzakId = muzakId;
	}
	
	private static void update() {
		mv = g.musicVolume * 0.1f;
		sv = g.soundVolume * 0.1f;
	}
}
