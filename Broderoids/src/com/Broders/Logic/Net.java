package com.Broders.Logic;

import com.Broders.Entities.*;
import com.Broders.Screens.GameScreen;
import com.Broders.mygdxgame.BaseGame;
import com.Broders.mygdxgame.SoundManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.*;
import com.google.gson.stream.*;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Net extends Thread {
	// Server -> Client communication;
	public static final int FRAME_ERROR = -1;
	public static final int FRAME_GAME_LEAVE = 0;
	public static final int FRAME_GAME_ENTITY_CREATE = 1;
	public static final int FRAME_GAME_ENTITY_MODIFY = 2;
	public static final int FRAME_GAME_ENTITY_REMOVE = 3;
	public static final int FRAME_GAME_COLLISION = 4;
	public static final int FRAME_GAME_PLAYER_CREATE = 5;
	public static final int FRAME_GAME_PLAYER_MODIFY = 6;
	public static final int FRAME_GAME_PLAYER_REMOVE = 7;
	public static final int FRAME_GAME_ROUND_OVER = 8;
	public static final int FRAME_GAME_SYNC = 9;
	// The only game frame without a command equivalent;
	public static final int FRAME_GAME_HOST_CHANGE = 10;

	public static final int FRAME_LOBBY_LIST = 20;
	public static final int FRAME_LOBBY_CREATE = 21;
	public static final int FRAME_LOBBY_JOIN = 22;

	// Client -> Server communication;
	public static final int COMMAND_GAME_LEAVE = 0;
	public static final int COMMAND_GAME_ENTITY_CREATE = 1;
	public static final int COMMAND_GAME_ENTITY_MODIFY = 2;
	public static final int COMMAND_GAME_ENTITY_REMOVE = 3;
	public static final int COMMAND_GAME_COLLISION = 4;
	public static final int COMMAND_GAME_PLAYER_CREATE = 5;
	public static final int COMMAND_GAME_PLAYER_MODIFY = 6;
	public static final int COMMAND_GAME_PLAYER_REMOVE = 7;
	public static final int COMMAND_GAME_ROUND_OVER = 8;

	public static final int COMMAND_LOBBY_LIST = 20;
	public static final int COMMAND_LOBBY_CREATE = 21;
	public static final int COMMAND_LOBBY_JOIN = 22;

	// Entity types
	public static final int ENTITY_ASTEROID = 0;
	public static final int ENTITY_SHIP = 1;
	public static final int ENTITY_BULLET = 2;

	private static BaseGame main;

	// public Semaphore entitiesLock;

	private static Socket s;
	private static JsonWriter out;
	private static JsonStreamParser parser;
	private static Gson g;
	private static ReentrantLock l;

	public static void init(BaseGame game) {
		main = game;

		// Open the network connection
		try {
			g = new Gson();
			s = new Socket("sekhmet.lug.mtu.edu", 9988);
			//s = new Socket("localhost", 9988);
			main.setConnected(true);
			out = new JsonWriter(new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream())));
			parser = new JsonStreamParser(new BufferedReader(
					new InputStreamReader(s.getInputStream())));
		} catch (Exception e) {
			e.printStackTrace();
			main.setConnected(false);
		}

		l = new ReentrantLock();
	}

	public static void leaveGame() {
		JsonObject o = new JsonObject();
		o.addProperty("c", COMMAND_GAME_LEAVE);
		Net.send(o);
	}
	
	public static void modifyPlayer(Player p) {
		JsonObject o = new JsonObject();
		o.addProperty("c", COMMAND_GAME_PLAYER_MODIFY);
		JsonObject d = new JsonObject();
		d.addProperty("i", p.getId());
		//d.addProperty("n", ) // Name
		d.addProperty("s", p.getScore());
		d.addProperty("c", p.getColor().toString());
		//d.addProperty("h", ) // Host
		o.add("d", d);
	}

	public static void createEntity(Entity e) {
		JsonObject o = new JsonObject();
		o.addProperty("c", COMMAND_GAME_ENTITY_CREATE);
		Net.entitySend(o, e);
	}

	public static void modifyEntity(Entity e) {
		JsonObject o = new JsonObject();
		o.addProperty("c", COMMAND_GAME_ENTITY_MODIFY);
		Net.entitySend(o, e);
	}

	public static void removeEntity(Entity e) {
		JsonObject o = new JsonObject();
		o.addProperty("c", COMMAND_GAME_ENTITY_REMOVE);
		o.addProperty("d", e.getId());
		System.out.println(e.getId());
		Net.send(o);
	}

	private static int entityType(Entity e) {
		if (e instanceof Ship) {
			return ENTITY_SHIP;
		} else if (e instanceof Bullet) {
			return ENTITY_BULLET;
		} else {
			return ENTITY_ASTEROID;
		}
	}

	public static void collision(Entity eA, Entity eB) {
		JsonObject o = new JsonObject();
		o.addProperty("c", COMMAND_GAME_ENTITY_REMOVE);
		JsonObject d = new JsonObject();
		d.addProperty("a", eA.getId());
		d.addProperty("b", eB.getId());
		o.add("d", d);
	}

	private static void entitySend(JsonObject o, Entity e) {
		JsonObject d = new JsonObject();
		d.addProperty("t", Net.entityType(e));
		d.addProperty("id", e.getId());
		d.addProperty("x", e.getX());
		d.addProperty("y", e.getY());
		Vector2 linVel = e.getLinearVelocity();
		d.addProperty("xv", linVel.x);
		d.addProperty("yv", linVel.y);
		d.addProperty("a", e.getAngle());
		d.addProperty("av", e.getAngularVelocity());
		d.addProperty("e", e.extra());

		o.add("d", d);
		Net.send(o);
	}

	public static Screen joinGame(String name, String pass) {
		JsonObject o = new JsonObject();
		o.addProperty("c", COMMAND_LOBBY_JOIN);

		JsonObject d = new JsonObject();
		d.addProperty("n", name);
		if (pass != null) {
			d.addProperty("p", pass);
		}
		o.add("d", d);

		Net.send(o);

		JsonElement e = parser.next();
		JsonObject inner = e.getAsJsonObject();
		int c = inner.get("c").getAsInt();

		// TODO: Useful errors
		if (c == FRAME_ERROR) {
			SoundManager.play("error");
			return null;
		} else if (c != FRAME_LOBBY_JOIN) {
			return null;
		}

		JsonObject innerInner = inner.get("d").getAsJsonObject();
		float x = innerInner.get("x").getAsFloat();
		float y = innerInner.get("y").getAsFloat();
		boolean hosting = innerInner.get("h").getAsBoolean();
		int id = innerInner.get("i").getAsInt();


		return new GameScreen(CoreLogic.getGame(), id, x, y, hosting);
	}

	public static Screen newGame(String name, int limit, float x, float y,
			String pass) {
		JsonObject o = new JsonObject();
		o.addProperty("c", COMMAND_LOBBY_CREATE);

		JsonObject d = new JsonObject();
		d.addProperty("n", name);
		d.addProperty("p", pass);
		d.addProperty("l", limit);
		d.addProperty("x", x);
		d.addProperty("y", y);
		o.add("d", d);

		Net.send(o);

		JsonElement e = parser.next();
		JsonObject outer = e.getAsJsonObject();
		int c = outer.get("c").getAsInt();

		// TODO: Useful errors
		if (c == FRAME_ERROR) {
			SoundManager.play("error");
			return null;
		} else if (c != FRAME_LOBBY_JOIN) {
			return null;
		}

		JsonObject inner = outer.get("d").getAsJsonObject();

		// float x = inner.get("x").getAsFloat();
		// float y = inner.get("y").getAsFloat();
		boolean hosting = inner.get("h").getAsBoolean();
		int id = inner.get("i").getAsInt();

		System.out.println(inner);



		return new GameScreen(CoreLogic.getGame(), id, x, y, hosting);
	}

	public static ArrayList<String[]> listGames() {
		JsonObject o = new JsonObject();
		o.addProperty("c", COMMAND_LOBBY_LIST);
		Net.send(o);

		o = parser.next().getAsJsonObject();
		ArrayList<String[]> ret = new ArrayList<String[]>();

		int command = o.get("c").getAsInt();
		if (command == Net.FRAME_ERROR) {
			SoundManager.play("error");
			return ret;
		} else if (command != Net.FRAME_LOBBY_LIST) {
			return ret;
		}

		JsonArray a = o.get("d").getAsJsonArray();
		for (JsonElement e : a) {
			JsonObject inner = e.getAsJsonObject();
			String[] s = new String[5];
			s[0] = inner.get("n").getAsString();
			s[1] = Boolean.toString(inner.get("p").getAsBoolean());
			s[2] = Integer.toString(inner.get("c").getAsInt());
			s[3] = Integer.toString(inner.get("l").getAsInt());
			ret.add(s);
		}

		return ret;
	}

	// TODO: Do we actually want this?
	private static void invalidFrame() throws NetException {
		throw new NetException(0, "Invalid frame recieved");
	}

	public static void lock() {
		Net.l.lock();
	}

	public static void unlock() {
		Net.l.unlock();
	}

	private static void handleErrorFrame(JsonObject o) throws Exception {
		int command = o.get("c").getAsInt();
		if (command != Net.FRAME_ERROR) {
			SoundManager.play("error");
			invalidFrame();
			return;
		}

		String text = o.get("d").getAsString();
		SoundManager.play("error");
		throw new Exception(text);
	}

	public static void handleGame() {
		// TODO: Handle thread.join();
		Net thread = new Net();
		thread.start();
	}


	public static void send(JsonObject o) {
		try {
			Net.g.toJson(o, Net.out);
			Net.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Non static portion begins here */

	public Net() {
	}

	@Override
	public void run() {
		JsonElement element;
		while (parser.hasNext()) {
			try {
				element = parser.next();

				Net.lock();

				// Since we know we have an object,
				// lets do what we need to with it
				JsonObject obj = element.getAsJsonObject();

				JsonElement e;

				e = obj.get("c");
				int frameType = e.getAsInt();
				if (!CoreLogic.getHost()) {
					if (frameType == FRAME_GAME_SYNC) {
						// NOTE: This should be the first thing the client gets,
						// so we can assume that there's nothing else in here.
						// In other words, just add all the objects

						JsonObject inn = obj.get("d").getAsJsonObject();
						JsonObject pla = inn.get("p").getAsJsonObject();
						for (JsonElement pe : pla.getAsJsonArray()) {
							JsonObject o = pe.getAsJsonObject();

							int id = o.get("i").getAsInt();
							String name = o.get("n").getAsString();
							int score = o.get("s").getAsInt();

							CoreLogic.createPlayer(id, name, score);
						}

						JsonObject ento = inn.get("e").getAsJsonObject();
						for (JsonElement ee : ento.getAsJsonArray()) {
							JsonObject o = ee.getAsJsonObject();
							EntityData ed = new EntityData();
							ed.type = o.get("t").getAsInt();
							ed.id = o.get("id").getAsString();
							ed.extra = o.get("e").getAsInt();
							ed.x = o.get("x").getAsFloat();
							ed.y = o.get("y").getAsFloat();
							ed.xv = o.get("xv").getAsFloat();
							ed.yv = o.get("yv").getAsFloat();
							ed.a = o.get("a").getAsFloat();
							ed.av = o.get("av").getAsFloat();

							CoreLogic.createEntity(ed);
						}	
					} else if (frameType == FRAME_GAME_COLLISION) {
						JsonObject o = obj.get("d").getAsJsonObject();
						String A = o.get("a").getAsString();
						Entity eA = CoreLogic.findEntity(A);
						String B = o.get("b").getAsString();
						Entity eB = CoreLogic.findEntity(B);
						CollisionLogic.entityContact(eA, eB);
					} else if (frameType == FRAME_GAME_ROUND_OVER) {
						CoreLogic.setRoundOver();
					}
				}

				if (frameType == FRAME_GAME_PLAYER_CREATE) {
					JsonObject o = obj.get("d").getAsJsonObject();
					int id = o.get("i").getAsInt();
					String name = o.get("n").getAsString();
					int score = o.get("s").getAsInt();

					if (id != CoreLogic.clientId) {
						CoreLogic.createPlayer(id, name, score);
					}
				} else if (frameType == FRAME_GAME_PLAYER_MODIFY) {
					JsonObject o = obj.get("d").getAsJsonObject();
					int id = o.get("i").getAsInt();
					int score = o.get("s").getAsInt();

					Player p = CoreLogic.getPlayer(Integer.toString(id));
					p.setScore(score);
				} else if (frameType == FRAME_GAME_PLAYER_REMOVE) {
					int id = obj.get("d").getAsInt();
					CoreLogic.removePlayer(Integer.toString(id));
				} else if (frameType == FRAME_GAME_ENTITY_CREATE) {
					JsonObject o = obj.get("d").getAsJsonObject();
					EntityData ed = new EntityData();
					ed.type = o.get("t").getAsInt();
					ed.id = o.get("id").getAsString();
					ed.extra = o.get("e").getAsInt();
					ed.x = o.get("x").getAsFloat();
					ed.y = o.get("y").getAsFloat();
					ed.xv = o.get("xv").getAsFloat();
					ed.yv = o.get("yv").getAsFloat();
					ed.a = o.get("a").getAsFloat();
					ed.av = o.get("av").getAsFloat();

					CoreLogic.createEntity(ed);
				} else if (frameType == FRAME_GAME_ENTITY_MODIFY) {
					JsonObject o = obj.get("d").getAsJsonObject();
					String id = o.get("id").getAsString();
					float x = o.get("x").getAsFloat();
					float y = o.get("y").getAsFloat();
					float xv = o.get("xv").getAsFloat();
					float yv = o.get("yv").getAsFloat();
					float a = o.get("a").getAsFloat();
					float av = o.get("av").getAsFloat();

					Entity ent = CoreLogic.findEntity(id);
					String[] idParts = id.split("-");
					if (!idParts[0].equals(Integer.toString(CoreLogic.clientId))
							&& !(CoreLogic.getHost() && idParts[0].equals("1"))) {
						// NOTE: Hacky work around
						if (ent != null) {
							ent.teleport(x, y, a, av, xv, yv);
						}
					}
				} else if (frameType == FRAME_GAME_ENTITY_REMOVE) {
					if (!CoreLogic.getHost()) {
						String data = obj.get("d").getAsString();

						Entity ent = CoreLogic.findEntity(data);

						if (ent.getOwner() != CoreLogic.getLocal() && ent.getOwner() != CoreLogic.getComp()) {
							CoreLogic.removeEntity(ent);
						}
					}
				} else if (frameType == FRAME_GAME_HOST_CHANGE) {
					CoreLogic.setHost(true);
				} else if (frameType == FRAME_GAME_LEAVE) {
					// TODO: Cleanup
					Net.unlock();
					break;
				} else {

				}
				Net.unlock();
			} catch (Exception e) { // Lets just pretend this isn't here, shall we?
				e.printStackTrace();
			}
		}
	}
}
