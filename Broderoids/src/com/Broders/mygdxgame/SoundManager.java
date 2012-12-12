package com.Broders.mygdxgame;
import java.util.HashMap;

import com.Broders.Logic.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

	private static HashMap<String, Sound> sounds;
	private static Music music;
	private static float sv;


	public static void init() {
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
		
		music = Gdx.audio.newMusic(Gdx.files.internal("data/broderoids.mp3"));
		music.play();
		music.setLooping(true);
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
		return sounds.get(key).play(sv);
	}

	public static long play(String key, float volume) {
		update();
		return sounds.get(key).play(sv * volume);
	}

	public static long play(String key, float volume, float pitch) {
		update();
		Sound clip = sounds.get(key);
		long id = 0;
		update();

		id = sounds.get(key).play(sv * volume);

		clip.setPitch(id, pitch);
		return id;
	}

	public static void setPitch(String key, long id, float pitch) {
		sounds.get(key).setPitch(id, pitch);
	}

	public static void setVolume(String key, long id, float volume) {
		update();
		sounds.get(key).setVolume(id, volume * sv);
	}

	public static void dispose() {
		for (Sound noise : sounds.values()) {
			noise.stop();
			noise.dispose();
		}
		music.stop();
		music.dispose();
		sounds = null;
	}
	
	public static void setMusicVolume(float volume){
		music.setVolume(volume * 0.1f);
	}

	private static void update() {
		sv = Settings.getSoundVol() * 0.1f;
	}
}
