package com.Broders.Logic;

import com.Broders.Entities.*;
import com.Broders.mygdxgame.BaseGame;
import com.google.gson.*;
import com.google.gson.stream.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Net extends Thread {
	// Frames from the server
	public static final int FRAME_ERROR = -1;
	public static final int FRAME_SYNC = 1;

	// Delta commands must be == related InputCommands
	public static final int FRAME_DELTA_UPDATE = 2;
	public static final int FRAME_DELTA_REMOVE = 3;
	public static final int FRAME_DELTA_CREATE = 4;

	// Responses from Lobby Commands
	public static final int FRAME_LIST_RESPONSE = 10;
	public static final int FRAME_CREATE_RESPONSE = 11;
	public static final int FRAME_JOIN_RESPONSE = 12;

	// Commands we will be sending
	public static final int COMMAND_ERROR = -1;

	// Game Commands
	public static final int COMMAND_LEAVE = 1;
	public static final int COMMAND_ENTITY_UPDATE = 2;
	public static final int COMMAND_ENTITY_REMOVE = 3;
	public static final int COMMAND_ENTITY_CREATE = 4;

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
	BaseGame theGame;
	JsonWriter out;
	JsonStreamParser in;
	int state = STATUS_LOBBY;

	public Net(BaseGame game) {
		theGame = game;
	}

	@Override
	public void run() {
		try {
			Gson g = new Gson();
			Socket s;

			s = new Socket("localhost", 9988);
			out = new JsonWriter(new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream())));
			in = new JsonStreamParser(new BufferedReader(new InputStreamReader(
					s.getInputStream())));
			
			while (in.hasNext()) {
				JsonElement element = in.next();
				JsonObject obj = element.getAsJsonObject();

				// So, we have a json object... now what?
				JsonElement e = obj.get("c");
				if (!e.isJsonPrimitive()) {
					// Well, shit. We got some shitty data from the server.
					// Connection must be bad, lets get out of here.
					// TODO: Be less draconian
					break;
				}

				int c = e.getAsInt();
				if (state == STATUS_LOBBY) {
					if (c == FRAME_LIST_RESPONSE) {
						
					} else if (c == FRAME_LIST_RESPONSE) {

					} else if (c == FRAME_LIST_RESPONSE) {

					} else {
						// TODO: Be less draconian
						break;
					}
				} else {
					if (c == FRAME_SYNC) {
						HashMap<String, Entity> entities = new HashMap<String, Entity>();

						// TODO: Don't edit current entity
						e = obj.get("e");
						JsonArray a = e.getAsJsonArray();
						for (JsonElement entityElement : a) {
							Entity toAdd;
							JsonObject entity = entityElement.getAsJsonObject();
							e = entity.get("id");
							if (!e.isJsonPrimitive()) {
								// TODO: Trouble... we should handle this
							}
							String id = e.getAsString();
							
							// If we own it, ignore it
							/*if (id.startsWith(Integer.toString(CoreLogic.getClientId()) + "-")) {
								continue;
							}*/
							
							e = entity.get("t");
							if (!e.isJsonPrimitive()) {
								// TODO: Trouble... we should handle this
							}
							int type = e.getAsInt();
							
							e = entity.get("x");
							if (!e.isJsonPrimitive()) {
								// TODO: Trouble... we should handle this
							}
							float x = e.getAsFloat();
							
							e = entity.get("y");
							if (!e.isJsonPrimitive()) {
								// TODO: Trouble... we should handle this
							}
							float y = e.getAsFloat();
							
							e = entity.get("a");
							if (!e.isJsonPrimitive()) {
								// TODO: Trouble... we should handle this
							}
							float angle = e.getAsFloat();
							
							e = entity.get("av");
							if (!e.isJsonPrimitive()) {
								// TODO: Trouble... we should handle this
							}
							float angVelocity = e.getAsFloat();
							
							e = entity.get("xv");
							if (!e.isJsonPrimitive()) {
								// TODO: Trouble... we should handle this
							}
							float XVelocity = e.getAsFloat();
							
							e = entity.get("yv");
							if (!e.isJsonPrimitive()) {
								// TODO: Trouble... we should handle this
							}
							float YVelocity = e.getAsFloat();
							
							/*if (CoreLogic.entities.containsKey(id)) {
								// Update
								Entity ent = CoreLogic.entities.get(id);
								ent.setPosition(x, y);
								ent.setAngle(angle);
								ent.finalizeBody();
								ent.setAngularVelocity(angVelocity);
								ent.setLinearVelocity(XVelocity, YVelocity);
							} else {
								Entity ent;
								if (type == ENTITY_SHIP) {
									ent = new Ship("ship", CoreLogic.myGame.playerColor);
								} else if (type == ENTITY_ASTEROID) {
									ent = new Asteroid("asteroid", 0, 0);
								} else {
									ent = new Bullet("bullet", 0, 0, 0);
								}
								ent.setPosition(x, y);
								ent.setAngle(angle);
								ent.finalizeBody();
								ent.setAngularVelocity(angVelocity);
								ent.setLinearVelocity(XVelocity, YVelocity);
							}*/
						}
						
						// TODO: Why do we need the unterruptable?
						theGame.entitiesLock.acquireUninterruptibly();
						//CoreLogic.entities = entities;
						theGame.entitiesLock.release();
					} else if (c == FRAME_DELTA_UPDATE) {
						
					} else if (c == FRAME_DELTA_CREATE) {

					} else if (c == FRAME_DELTA_REMOVE) {
						
					} else {
						// TODO: Be less draconian
						break;
					}
				}
			}
			
			// Cave Johnson, we're done here.
			out.close();
			s.close();
			
			in = null;
			out = null;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
