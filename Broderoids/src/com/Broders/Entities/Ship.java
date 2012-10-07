package com.Broders.Entities;

import com.Broders.Logic.Pos;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ship extends Entities{	
	
	private Boolean thrust;
	
	public Ship(String id, EntityType type) {		
		super(id, type);
		
		float xx = Gdx.graphics.getWidth();
		float yy = Gdx.graphics.getHeight();
		
		this.thrust = false;
		super.setSprite("data/Test.png");
		super.getSprite().setScale(.05f, .05f);
		super.SetPos(new Pos(xx*.5f, yy*.5f));
		super.getSprite().setColor(Color.MAGENTA);
	}
	
	public Boolean getThrust(){
		return this.thrust;
	}

	@Override
	public void Draw(SpriteBatch sb) {
		super.getSprite().draw(sb);
	}
	
}
