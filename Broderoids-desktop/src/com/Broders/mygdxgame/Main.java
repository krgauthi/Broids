package com.Broders.mygdxgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Broderoids";
		cfg.useGL20 = false;
		cfg.width = 1024;	//1280, 720
		cfg.height = 576;
		
		new LwjglApplication(new BaseGame(), cfg);
	}
}
