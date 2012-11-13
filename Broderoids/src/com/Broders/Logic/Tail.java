package com.Broders.Logic;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tail extends LinkedList<Pos> {

	private int length;
	private boolean flag = true;
	private Texture btail;
	private Sprite Tailsprite;

	public Tail(int l, Color c) {
		length = l;

		btail = new Texture(Gdx.files.internal("data/bullet.png"));
		Tailsprite = new Sprite(btail);
		Tailsprite.setColor(c);
	}

	public void update() {

		if (flag || this.size() > length) {
			flag = false;
			if (!this.isEmpty())
				this.removeFirst();
		} else {
			flag = true;
		}

	}

	public void draw(SpriteBatch spriteBatch) {

		float xx = Gdx.graphics.getWidth();
		float yy = Gdx.graphics.getHeight();

		for (Pos xy : this) {
			// Tailsprite.setPosition(xy.Getx(),yy-xy.Gety());
			Tailsprite.setPosition((xx * (xy.getX() - .01f)),
					yy - (yy * (xy.getY() + .05f)));
			Tailsprite.draw(spriteBatch);

		}
	}

}