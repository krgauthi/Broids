package com.Broders.Entities;

import com.Broders.Logic.CoreLogic;
import com.Broders.Logic.Player;
import com.Broders.Logic.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Asteroid extends Entity {

	public static final int LARGE = 0;
	public static final int MEDIUM = 1;
	public static final int SMALL = 2;

	private int type;

	public Asteroid(String type, String id, Player owner, Color c, float x, float y) {
		super(id, owner);

		super.setColor(c);

		// if we implement separate files just uncomment the lines above and
		// comment this line out
		super.setSprite(Settings.data_path + "broid.png");

		FixtureDef fixDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		if (type.equals("small")) {
			this.type = SMALL;
			super.setSize(3.75f);
			shape.setRadius(1.5f);
			fixDef.density = 0.125f;
			super.setPoints(100);
		} else if (type.equals("medium")) {
			this.type = MEDIUM;
			super.setSize(7.5f);
			shape.setRadius(3f);
			fixDef.density = 0.5f;
			super.setPoints(50);
		} else {
			this.type = LARGE;
			super.setSize(15.0f);
			shape.setRadius(6f);
			fixDef.density = 2.0f;
			super.setPoints(20);
		}

		fixDef.shape = shape;
		fixDef.restitution = 0.0f;

		BodyDef bodDef = new BodyDef();
		bodDef.type = BodyType.DynamicBody;
		bodDef.angularDamping = 0.0f;
		bodDef.linearDamping = 0.0f;

		bodDef.position.set(x, y);
		bodDef.angle = (float) (MathUtils.PI * Math.random());
		bodDef.allowSleep = false;
		super.createBody(bodDef, fixDef);

		// sprite
		float meter = Gdx.graphics.getHeight() / CoreLogic.getHeightScreen();

		super.getSprite().setOrigin(meter * (this.getSize() / 2),
				meter * (this.getSize() / 2));
		super.getSprite().setSize(meter * this.getSize(),
				meter * this.getSize());
		super.getSprite().setColor(this.getColor());

		// Set type data
		super.getBody().setUserData(this);
	}

	

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		Asteroid roid1;
		Asteroid roid2;
		float x1;
		float x2;
		float y1;
		float y2;
		float dir;
		if (this.type == LARGE) {
			float temp = (float) (10+Math.random()%10);
			for(int i = 0; i < temp;i++){
				temp = 360/temp;
				
				Dust D = new Dust(CoreLogic.getScratch().nextId(), CoreLogic.getScratch(), (float)(Math.random()%10)+(temp*i), this.getX(), this.getY());
				CoreLogic.getScratch().getEntitiesMap().put(D.getId(), D);
			}
			
			dir = (float) Math.toRadians(this.getAngle());
			x1 = (float) (this.getX() + 7.5 * Math.cos(dir));
			x2 = (float) (this.getX() + 7.5 * Math.cos(dir));
			y1 = (float) (this.getY() - 7.5 * Math.sin(dir));
			y2 = (float) (this.getY() - 7.5 * Math.sin(dir));

			roid1 = new Asteroid("medium", this.owner.nextId(), this.owner, CoreLogic.getGame().gameColor, x1,
					y1);

			float initForce = (float) (4000 + (2000 * Math.random()));
			float x = (float) (initForce * Math.cos(dir));
			float y = (float) (initForce * Math.sin(dir));

			Vector2 f = roid1.getBody().getWorldVector(new Vector2(x, y));
			Vector2 p = roid1.getBody().getWorldPoint(
					roid1.getBody().getLocalCenter());
			roid1.getBody().applyForce(f, p);

			float spin = (float) (300 + (250 * Math.random()));
			if (Math.random() >= 0.5f)
				spin *= -1;

			roid1.getBody().applyTorque(spin);
			CoreLogic.getComp().getEntitiesMap().put(roid1.getId(), roid1);

			roid2 = new Asteroid("medium", this.owner.nextId(), this.owner, CoreLogic.getGame().gameColor, x2,
					y2);

			initForce = (float) (2000 + (1000 * Math.random()));
			x = (float) (initForce * Math.cos(dir));
			y = (float) (initForce * Math.sin(dir));

			f = roid2.getBody().getWorldVector(new Vector2(x, y));
			p = roid2.getBody().getWorldPoint(roid2.getBody().getLocalCenter());
			roid2.getBody().applyForce(f, p);

			spin = (float) (300 + (250 * Math.random()));
			if (Math.random() >= 0.5f)
				spin *= -1;

			roid2.getBody().applyTorque(spin);
			CoreLogic.getComp().getEntitiesMap().put(roid2.getId(), roid2);
		} else if (this.type == MEDIUM) {
			float temp = (float) (5+Math.random()%10);
			for(int i = 0; i < temp;i++){
				temp = 360/temp;

				Dust D = new Dust(CoreLogic.getScratch().nextId(), CoreLogic.getScratch(), (float)(Math.random()%10)+(temp*i), this.getX(), this.getY());
				CoreLogic.getScratch().getEntitiesMap().put(D.getId(), D);
			}
			this.getX();
			
			dir = (float) Math.toRadians(this.getAngle());
			x1 = (float) (this.getX() + 3.75 * Math.cos(dir));
			x2 = (float) (this.getX() + 3.75 * Math.cos(dir));
			y1 = (float) (this.getY() - 3.75 * Math.sin(dir));
			y2 = (float) (this.getY() - 3.75 * Math.sin(dir));

			roid1 = new Asteroid("small", this.owner.nextId(), this.owner, CoreLogic.getGame().gameColor, x1, y1);

			float initForce = (float) (500 + (250 * Math.random()));
			float x = (float) (initForce * Math.cos(Math.random()*2*Math.PI));
			float y = (float) (initForce * Math.sin(Math.random()*2*Math.PI));

			Vector2 f = roid1.getBody().getWorldVector(new Vector2(x, y));
			Vector2 p = roid1.getBody().getWorldPoint(
					roid1.getBody().getLocalCenter());
			roid1.getBody().applyForce(f, p);

			float spin = (float) (300 + (250 * Math.random()));
			if (Math.random() >= 0.5f)
				spin *= -1;

			roid1.getBody().applyTorque(spin);
			CoreLogic.getComp().getEntitiesMap().put(roid1.getId(), roid1);

			roid2 = new Asteroid("small", this.owner.nextId(), this.owner, CoreLogic.getGame().gameColor, x2, y2);

			initForce = (float) (500 + (250 * Math.random()));
			x = (float) (initForce * Math.cos(dir));
			y = (float) (initForce * Math.sin(dir));

			f = roid2.getBody().getWorldVector(new Vector2(x, y));
			p = roid2.getBody().getWorldPoint(roid2.getBody().getLocalCenter());
			roid2.getBody().applyForce(f, p);

			spin = (float) (300 + (250 * Math.random()));
			if (Math.random() >= 0.5f)
				spin *= -1;

			roid2.getBody().applyTorque(spin);
			CoreLogic.getComp().getEntitiesMap().put(roid2.getId(), roid2);
		} else {
			float temp = (float) (3+Math.random()%10);
			for(int i = 0; i < temp;i++){
				temp = 360/temp;

				Dust D = new Dust(CoreLogic.getScratch().nextId(), CoreLogic.getScratch(), (float)(Math.random()%10)+(temp*i), this.getX(), this.getY());
				CoreLogic.getScratch().getEntitiesMap().put(D.getId(), D);
			}
		}
	}
}
