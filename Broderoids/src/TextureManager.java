

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TextureManager {
	private static HashMap<String, Texture> textures;
	
	public static void init() {
		textures = new HashMap<String, Texture>();
		String[][] defaultTextures = {
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
		};
		
		for (String[] tex : defaultTextures) {
			load(tex[0], tex[1]);
		}
	}
	
	public static Texture get(String key) {
		return textures.get(key);
	}
	
	public static void load(String key, String file) {
		Texture temp = new Texture(Gdx.files.internal(file));
		textures.put(key, temp);
	}
	
	public static void destroy() {
		
	}
}
