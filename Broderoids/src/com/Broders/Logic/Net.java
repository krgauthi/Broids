package com.Broders.Logic;

import com.Broders.Entities.*;
import com.Broders.Screens.GameScreen;
import com.Broders.mygdxgame.BaseGame;
import com.badlogic.gdx.Screen;
import com.google.gson.*;
import com.google.gson.stream.*;

import java.net.*;
import java.io.*;
import java.util.*;
//import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Net extends Thread {
	// Frames from the server
	public static final int FRAME_ERROR = -1;
	public static final int FRAME_SYNC = 1;

	// Delta commands must be == related InputCommands
	public static final int FRAME_ENTITY_UPDATE = 2;
	public static final int FRAME_ENTITY_REMOVE = 3;
	public static final int FRAME_ENTITY_CREATE = 4;
	public static final int FRAME_PLAYER_REMOVE = 5;
	public static final int FRAME_PLAYER_CREATE = 6;

	// Responses from Lobby Commands
	public static final int FRAME_LIST_RESPONSE = 10;
	public static final int FRAME_CREATE_RESPONSE = 11;
	public static final int FRAME_JOIN_RESPONSE = 12;
	public static final int FRAME_LEAVE_RESPONSE = 13;

	// Commands we will be sending
	public static final int COMMAND_ERROR = -1;

	// Game Commands
	public static final int COMMAND_LEAVE = 1;
	public static final int COMMAND_ENTITY_UPDATE = 2;
	public static final int COMMAND_ENTITY_REMOVE = 3;
	public static final int COMMAND_ENTITY_CREATE = 4;
	public static final int COMMAND_PLAYER_REMOVE = 5;
	public static final int COMMAND_PLAYER_CREATE = 6;
	public static final int COMMAND_SYNC_REQUEST = 7;

	// Lobby Commands
	public static final int COMMAND_LIST = 10;
	public static final int COMMAND_CREATE = 11;
	public static final int COMMAND_JOIN = 12;

	// Entity types
	public static final int ENTITY_SHIP = 1;
	public static final int ENTITY_ASTEROID = 2;
	public static final int ENTITY_BULLET = 3;

	// State
	public static final int STATUS_LOBBY = 1;
	public static final int STATUS_GAME = 2;

	// Instance vars
	// private static int state = STATUS_LOBBY;

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
			out = new JsonWriter(new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream())));
			parser = new JsonStreamParser(new BufferedReader(
					new InputStreamReader(s.getInputStream())));
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		l = new ReentrantLock();
	}

	private static void invalidFrame() throws NetException {
		throw new NetException(0, "Invalid frame recieved");
	}

	private static void lock() {
		Net.l.lock();
	}

	private static void unlock() {
		Net.l.unlock();
	}

	private static void handleErrorFrame(JsonObject o) throws NetException {
		int command = o.get("c").getAsInt();
		if (command != Net.FRAME_ERROR) {
			invalidFrame();
			return;
		}
		int code = o.get("id").getAsInt();
		String text = o.get("text").getAsString();
		throw new NetException(code, text);
	}

	public static Entity createEntity(JsonObject inner, Player owner) {
		String id = inner.get("id").getAsString();
		int entityType = inner.get("t").getAsInt();
		float xPos = inner.get("x").getAsFloat();
		float yPos = inner.get("y").getAsFloat();
		float xVel = inner.get("xv").getAsFloat();
		float yVel = inner.get("yv").getAsFloat();
		float dPos = inner.get("d").getAsFloat();
		float vPos = inner.get("v").getAsFloat();

		// TODO: entity extra
		Entity ret;
		if (entityType == Net.ENTITY_ASTEROID) {
			ret = new Asteroid(Asteroid.LARGE, id, owner,
					CoreLogic.getGame().gameColor, xPos, yPos);
		} else if (entityType == Net.ENTITY_BULLET) {
			ret = new Bullet(id, owner, dPos, xPos, yPos);
		} else /* if (entityType == Net.ENTITY_SHIP) */{
			ret = new Ship(id, owner, xPos, yPos);
		} /*
		 * else {null ret = new Entity(); // WTF? }
		 */
		ret.teleport(xPos, yPos, dPos, vPos, xVel, yVel);
		return ret;
	}

	public static void handleGame() {
		// TODO: Handle thread.join();
		Net thread = new Net();
		thread.start();
	}

	public static void send(JsonObject o) {
		Net.g.toJson(o, Net.out);
		try {
			Net.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<String[]> listGames() throws NetException {
		JsonObject o = new JsonObject();
		o.addProperty("c", Net.COMMAND_LIST);
		Net.send(o);

		ArrayList<String[]> games = new ArrayList<String[]>();
		JsonElement element = Net.parser.next();
		JsonObject ob = element.getAsJsonObject();
		int command = ob.get("c").getAsInt();
		if (command == Net.FRAME_ERROR) {
			Net.handleErrorFrame(ob);
		} else if (command != Net.FRAME_LIST_RESPONSE) {
			Net.invalidFrame();
		}

		JsonArray a = ob.getAsJsonArray("d");
		if (a == null) {
			Net.invalidFrame();
		}

		for (JsonElement gameElem : a) {
			ob = gameElem.getAsJsonObject();
			String[] temp = new String[4];
			temp[0] = ob.get("n").getAsString();
			temp[1] = Boolean.toString(ob.get("p").getAsInt() == 0);
			temp[2] = Integer.toString(ob.get("c").getAsInt());
			temp[3] = Integer.toString(ob.get("m").getAsInt());

			games.add(temp);

			System.out.println(temp[0]);
		}

		return games;
	}

	public static Screen joinGame(String game, String password) {
		JsonObject o = new JsonObject();
		o.addProperty("c", Net.COMMAND_JOIN);

		JsonObject d = new JsonObject();
		d.addProperty("n", game);
		d.addProperty("p", password);

		o.add("d", d);
		Net.send(o);

		o = Net.parser.next().getAsJsonObject();
		int ret = o.get("c").getAsInt();
		if (ret == Net.FRAME_ERROR) {
			// Trouble
			System.out.println("Error when connecting");
			return null;
		} else if (ret != Net.FRAME_JOIN_RESPONSE) {
			// Trouble
			System.out.println("Invalid frame response when connecting");
			return null;
		}

		d = o.get("d").getAsJsonObject();

		int id = d.get("id").getAsInt();
		float width = d.get("w").getAsFloat();
		float height = d.get("h").getAsFloat();

		System.out.println("Connected");

		return new GameScreen(Net.main, id, width, height, false);
	}

	public static void leaveGame() {
		JsonObject o = new JsonObject();
		o.addProperty("c", Net.COMMAND_LEAVE);
		Net.send(o);
	}

	/* Non static portion begins here */

	public Net() {
	}

	@Override
	public void run() {
		JsonElement element;
		while (parser.hasNext()) {
			element = parser.next();

			// TODO: Make it possible to break out of the loop

			// Since we know we have an object,
			// lets do what we need to with it
			JsonObject obj = element.getAsJsonObject();

			JsonElement e;

			e = obj.get("c"); // Type
			int frameType = e.getAsInt();
			if (frameType == FRAME_SYNC) {
				System.out.println("Sync");

				e = obj.get("t"); // Time
				int gameTime = e.getAsInt();
				System.out.println("gametime =" + gameTime);

				JsonArray eArray = obj.getAsJsonArray("e");

				for (JsonElement elem : eArray) {
					JsonObject inner = elem.getAsJsonObject();

					String id = inner.get("id").getAsString();
					int entityType = inner.get("t").getAsInt();
					float xPos = inner.get("x").getAsFloat();
					float yPos = inner.get("y").getAsFloat();
					float xVel = inner.get("xv").getAsFloat();
					float yVel = inner.get("yv").getAsFloat();
					float dPos = inner.get("d").getAsFloat();
					float vPos = inner.get("v").getAsFloat();

					Net.lock();
					// TODO: Clear people, clear entities, remake
					Net.unlock();
				}
			} else {
				System.out.println("Delta");

				e = obj.get("e");

				JsonObject inner = e.getAsJsonObject();

				String id = inner.get("id").getAsString();
				System.out.println("d.e.id Id-id = " + id);

				if (frameType == FRAME_PLAYER_CREATE) {
					String[] idParts = id.split("-");

					Net.lock();
					Player temp = new Player("Net Player",
							Integer.parseInt(idParts[0]));
					CoreLogic.getPlayersMap().put(
							Integer.toString(temp.getId()), temp);
					Net.unlock();
				} else if (frameType == FRAME_PLAYER_REMOVE) {
					Net.lock();
					Net.unlock();
				} else if (frameType == FRAME_ENTITY_UPDATE) {
					// int entityType = inner.get("t").getAsInt();
					float xPos = inner.get("x").getAsFloat();
					float yPos = inner.get("y").getAsFloat();
					float xVel = inner.get("xv").getAsFloat();
					float yVel = inner.get("yv").getAsFloat();
					float dPos = inner.get("d").getAsFloat();
					float vPos = inner.get("v").getAsFloat();

					// Update here
					Net.lock();
					Entity temp = CoreLogic.findEntity(id);
					temp.teleport(xPos, yPos, dPos, vPos, xVel, yVel);
					Net.unlock();
				} else if (frameType == FRAME_ENTITY_CREATE) {
					// Update here
					Net.lock();
					// TODO: Implement this
					String[] idParts = id.split("-");
					Player p = CoreLogic.getPlayersMap().get(idParts[0]);
					p.getEntitiesMap().put(idParts[1],
							Net.createEntity(inner, p));
					Net.unlock();
				} else if (frameType == FRAME_ENTITY_REMOVE) {
					Net.lock();
					CoreLogic.removeEntity(CoreLogic.findEntity(id));
					Net.unlock();
				} else {
					// Cave Johnson, we're done here.
					break;
				}
			}
		}
	}
}
