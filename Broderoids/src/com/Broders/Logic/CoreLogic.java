package com.Broders.Logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class CoreLogic {
	private World world;
	
	public void initCore(){
		Vector2 gravity = new Vector2(0, -10f);
		world = new World(gravity, true);
	}
	
	public void execute(double delta, InputDir in){
		
	}
}
