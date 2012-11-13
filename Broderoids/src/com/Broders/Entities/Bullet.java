package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Settings;
import com.Broders.Logic.Pos;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Bullet extends Entity {

	// private Pos pos;
	// private float dir;
	private float age;
	private static float deathTime = 1f;

	public Bullet(String type, float dir, Vector2 velocity, float x, float y) {
		super(type);
//<<<<<<< HEAD
//
//		//Rinkus' stuff is commented out
//		super.setEnt("bullet");
//		//		
//		//		//FixtureDef & shape
//		//		FixtureDef fixDef = new FixtureDef();
//		//		PolygonShape shape = new PolygonShape();
//		//		shape.setAsBox(0.1f, 0.1f, new Vector2(0f,0f), 0f);
//		//		fixDef.shape = shape;
//		//		fixDef.density = 1f;
//
//		// Fixtures
//		FixtureDef fixDef = new FixtureDef();
//
//		PolygonShape shape = new PolygonShape();
//		shape.setAsBox(0.5f, 0.5f);
//		fixDef.shape = shape;
//		fixDef.density = 1f;
//=======
		super.setEnt("bullet");

		// sprite
		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();

		super.setSize(6f);

		super.setSprite(Settings.data_path + "bullet.png");
		super.getSprite().setOrigin((meter * this.getSize()) / 2,
				(meter * this.getSize()) / 2);
		super.getSprite().setSize(meter * this.getSize(),
				meter * this.getSize());
		super.getSprite().setColor(Color.WHITE);
//>>>>>>> a7fba53dc95ac2bc2178d43557b78112cedf1f77

		//BodyDef
		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.KinematicBody;
		bodDef.linearDamping = 0.0f;
		bodDef.position.set(x, y);
		bodDef.angle = dir;
		bodDef.allowSleep = false;

		//super.createBody(bodDef, fixDef);
		//		
		//		//Create the body
		//		super.createBody(bodDef, fixDef);
		//		super.getBody().setBullet(true);
		//		
		//		//Set the velocity
//				float vX = (float) (20*Math.cos(dir));
//				float vY = (float) (20*Math.sin(dir));
//				super.body.setLinearVelocity(vX, vY);

//		// Set the velocity
//		float vX = (float) (100 * Math.cos(dir));
//		float vY = (float) (100 * Math.sin(dir));		
//		super.body.setLinearVelocity(vX, vY);

//<<<<<<< HEAD
//		//		//sprite
//		//		float meter = Gdx.graphics.getHeight()/CoreLogic.getHeightScreen();			
//		//		
//		//super.setSize(3.5f);
//
//		// sprite
//		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();
//
//		super.setSize(6f);
//
//		super.setSprite(Settings.data_path + "bullet.png");
//		super.getSprite().setOrigin((meter * this.getSize()) / 2,
//				(meter * this.getSize()) / 2);
//		super.getSprite().setSize(meter * this.getSize(),
//				meter * this.getSize());
//		super.getSprite().setColor(Color.WHITE);
//
//
//=======
		FixtureDef fixDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(0.5f);
		fixDef.shape = shape;
		fixDef.density = 1f;

		super.createBody(bodDef, fixDef);
		super.getBody().setBullet(true);

		// Set the velocity
		float vX = (float) (75 * Math.cos(Math.toRadians(dir)));
		float vY = (float) (75 * Math.sin(Math.toRadians(dir)));
		super.body.setLinearVelocity(vX, vY);

		//Set type data
		super.getBody().setUserData(new TypeData("bullet"));
//>>>>>>> a7fba53dc95ac2bc2178d43557b78112cedf1f77
	}

	@Override
	public void Draw(SpriteBatch sb) {
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();

		float x = super.getBody().getPosition().x - (this.getSize() / 2f);
		float y = super.getBody().getPosition().y - (this.getSize() / 2f);

		float posX;
		float posY;

		posX = screenWidth
				* ((x - CoreLogic.getViewPortX()) / CoreLogic.getWidthScreen());
		posY = screenHeight
				* ((y - CoreLogic.getViewPortY()) / CoreLogic.getHeightScreen());

		super.getSprite().setPosition(posX, posY);
		super.getSprite().setRotation(super.getBody().getAngle());
		super.getSprite().draw(sb);
	}

	@Override
	public void update() {
		age += Gdx.graphics.getDeltaTime();

		if (age >= deathTime) {
			CoreLogic.removeEntity(this);
		}

	}

	@Override
	public void destroy() {

	}
}
