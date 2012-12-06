package com.Broders.mygdxgame;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	
	private static HashMap<String, Sound> sounds;
	
	public static void init() {
		sounds = new HashMap<String, Sound>();
		String[][] defaultSounds = {
				{"death","data/ShipBoom.mp3"},
				{"roidBreak1", "data/AsteroidBoom1.wav"},
				{"roidBreak2", "data/AsteroidBoom2.wav"},
				{"roidBreak3", "data/AsteroidBoom3.wav"},
				{"pew","data/shot.mp3"},
				{"muzak", "data/Broderoids.mp3"},
				{"zoom", "data/Thrust.mp3"}
		};
		
		for (String[] noise : defaultSounds) {
			Sound temp = Gdx.audio.newSound(Gdx.files.internal(noise[1]));
			sounds.put(noise[0], temp);
		}
	}
	
	public static Sound get(String key) {
		return sounds.get(key);
	}

	public static void dispose() {
		for (Sound noise : sounds.values()) {
			noise.dispose();
		}
		sounds = null;
	}
}
