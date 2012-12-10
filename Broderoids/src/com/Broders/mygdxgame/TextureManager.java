package com.Broders.mygdxgame;


import java.util.HashMap;

import com.Broders.Logic.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TextureManager {
	private static HashMap<String, Texture> textures;
	private static HashMap<String, Sprite> sprites;

	public static void init(BaseGame g) {
		BaseGame myGame = g;
		textures = new HashMap<String, Texture>();
		sprites = new HashMap<String, Sprite>();

		if(Settings.getRetro()){
			String[][] defaultTextures = {
					//Hud
					{"healthBar", "data/healthbracket.png"},
					{"healthBlock", "data/healthbar.png"},
					{"shieldBar", "data/shieldbracket.png"},
					{"shieldBlock", "data/shieldbar.png"},
					{"lives", "data/retroShip1.png"},
					{"white", "data/whitebox.png"},
					{"whitePixel", "data/whitepixel.png"},

					// Android only
					{"dPad", "data/leftrightpad.png"},
					{"fireButton", "data/fireButton.png"},
					{"thrustButton", "data/thrustButton.png"},

					//Menu
					{"hostGame","data/bttn_host.png"},
					{"joinGame","data/bttn_join.png"},
					{"refresh","data/bttn_refresh.png"},
					{"singlePlayer","data/bttn_singleplayer.png"},
					{"multiplayer","data/bttn_multiplayer.png"},
					{"highScores","data/bttn_highScore.png"},
					{"mainMenu","data/bttn_mainMenu.png"},
					{"pause","data/bttn_pause.png"},
					{"quit","data/bttn_quit.png"},
					{"settings","data/bttn_settings.png"},
					
					//Title
					{"titleLeft","data/menu_title1.png"},
					{"titleRight","data/menu_title2.png"},

					//Retro Textures
					{"Ship1","data/retroShip1.png"},
					{"Ship2","data/retroShip2.png"},
					{"Broid","data/retroBroid.png"},
					{"bullet","data/bullet.png"},
			};

			for (String[] tex : defaultTextures) {
				loadTexture(tex[0], tex[1]);
				loadSprite(tex[0]);
			}

		}else{
			String[][] defaultTextures = {
					//Hud
					{"healthBar", "data/healthbracket.png"},
					{"healthBlock", "data/healthbar.png"},
					{"shieldBar", "data/shieldbracket.png"},
					{"shieldBlock", "data/shieldbar.png"},
					{"lives", "data/ship1.png"},
					{"white", "data/whitebox.png"},
					{"whitePixel", "data/whitepixel.png"},

					// Android only
					{"dPad", "data/leftrightpad.png"},
					{"fireButton", "data/fireButton.png"},
					{"thrustButton", "data/thrustButton.png"},

					//Menu
					{"hostGame","data/bttn_host.png"},
					{"joinGame","data/bttn_join.png"},
					{"refresh","data/bttn_refresh.png"},
					{"singlePlayer","data/bttn_singleplayer.png"},
					{"multiplayer","data/bttn_multiplayer.png"},
					{"highScores","data/bttn_highScore.png"},
					{"mainMenu","data/bttn_mainMenu.png"},
					{"pause","data/bttn_pause.png"},
					{"quit","data/bttn_quit.png"},
					{"settings","data/bttn_settings.png"},
					{"resume","data/bttn_resume.png"},

					//Title
					{"titleLeft","data/menu_title1.png"},
					{"titleRight","data/menu_title2.png"},
					

					//Game Textures
					{"Ship1","data/ship1.png"},
					{"Ship2","data/ship2.png"},
					{"Broid","data/broid.png"},
					{"bullet","data/bullet.png"},
			};

			for (String[] tex : defaultTextures) {
				loadTexture(tex[0], tex[1]);
				loadSprite(tex[0]);
			}
		}
	}

	public static Texture getTexture(String key) {
		return textures.get(key);
	}

	public static Sprite getSprites(String key) {
		return sprites.get(key);
	}

	public static void loadTexture(String key, String file) {
		Texture temp = new Texture(Gdx.files.internal(file));
		textures.put(key, temp);
	}

	public static void loadSprite(String key) {
		Sprite temp = new Sprite(textures.get(key));
		sprites.put(key, temp);
	}

	public static void destroy() {

	}
}
