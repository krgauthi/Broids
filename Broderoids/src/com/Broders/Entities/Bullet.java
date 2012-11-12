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
	private static float deathTime = 1.5f;

	public Bullet(String type, float dir, Vector2 velocity, float x, float y) {
		super(type);
		// TODO Write initialization for Bullet body/sprite

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

		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.DynamicBody;
		bodDef.linearDamping = 0.0f;

		bodDef.position.set(x, y);
		bodDef.angle = dir;
		bodDef.allowSleep = false;

		// Fixtures
		FixtureDef fixDef = new FixtureDef();

		CircleShape shape = new CircleShape();
		shape.setRadius(0.5f);
		fixDef.shape = shape;
		fixDef.density = 1f;
		// TODO Add fixtures

		super.createBody(bodDef, fixDef);

		/*Vector2 linV = this.body.getLinearVelocity();
		float angV = this.body.getAngularVelocity();
		float angle = this.body.getAngle();

		CoreLogic.getWorld().destroyBody(this.body);

		this.bodDef.position.set(x, y);
		this.bodDef.angle = angle;
		this.body = CoreLogic.getWorld().createBody(bodDef);
		this.body.createFixture(this.fixDef);
		this.body.setAngularVelocity(angV);
		this.body.setLinearVelocity(linV);*/
		
		// Set the velocity
		float vX = (float) (50 * Math.cos(Math.toRadians(dir)));
		float vY = (float) (50 * Math.sin(Math.toRadians(dir)));
		super.body.setLinearVelocity(vX, vY);
		
		System.out.println(this.getX() + "-" + this.getY());
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
