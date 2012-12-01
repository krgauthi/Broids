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
			Socket s = new Socket("localhost", 9988);

			JsonObject o = new JsonObject();
			o.addProperty("c", COMMAND_JOIN);

			JsonObject od = new JsonObject();
			od.addProperty("n", "broids");
			o.add("d", od);

			JsonWriter out = new JsonWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));
			g.toJson(o, out);
			out.flush();

			System.out.println("And now we listen");

			JsonStreamParser parser = new JsonStreamParser(new BufferedReader(new InputStreamReader(s.getInputStream())));

			JsonElement element;
			while (parser.hasNext()) {
				element = parser.next();

				// Since we know we have an object, lets do what we need to with it
				JsonObject obj = element.getAsJsonObject();

				JsonElement e;

				System.out.println(obj);
				
				e = obj.get("c"); // Type
				int frameType = e.getAsInt();
				if (frameType == FRAME_SYNC) {
					System.out.println("Sync");
					
					e = obj.get("t"); // Type
					int gameType = e.getAsInt();
					System.out.println("gametime =" + gametime)

					JsonArray eArray = e.getAsJsonArray("e");

					for(JsonElement inner : eArray){
						String id = inner.get("id").getAsString();
						System.out.println("d.e.id Id-id = " +id);

						int actionType = inner.get("t").getAsInt();
						System.out.println("ActionType-t = " + actionType);

						int entityType = inner.get("t").getAsInt();
						System.out.println("d.e.t Type-t = " + entityType);

						float xPos = inner.get("x").getAsFloat();
						System.out.println("d.e.x xPos-x = " + xPos);

						float yPos = inner.get("y").getAsFloat();
						System.out.println("d.e.y yPos-y = " + yPos);

						float xVel = inner.get("xv").getAsFloat();
						System.out.println("d.e.x xVel-x = " + xVel);

						float yVel = inner.get("yv").getAsFloat();
						System.out.println("d.e.y yVel-y = " + yVel);

						float dPos = inner.get("d").getAsFloat();
						System.out.println("d.e.d dPos-d = " + dPos);

						float vPos = inner.get("v").getAsFloat();
						System.out.println("d.e.v vPos-v = " + vPos);
					}
				} 

				else {
					System.out.println("Delta");

					JsonObject eArray;
					e = obj.get("e");

					JsonObject inner = e.getAsJsonObject();
				
					String id = inner.get("id").getAsString();
					System.out.println("d.e.id Id-id = " +id);

						if (frameType == FRAME_DELTA_UPDATE || frameType == FRAME_DELTA_CREATE) {
							int actionType = inner.get("t").getAsInt();
							System.out.println("ActionType-t = " + actionType);

							int entityType = inner.get("t").getAsInt();
							System.out.println("d.e.t Type-t = " + entityType);

							float xPos = inner.get("x").getAsFloat();
							System.out.println("d.e.x xPos-x = " + xPos);

							float yPos = inner.get("y").getAsFloat();
							System.out.println("d.e.y yPos-y = " + yPos);

							float xVel = inner.get("xv").getAsFloat();
							System.out.println("d.e.x xVel-x = " + xVel);

							float yVel = inner.get("yv").getAsFloat();
							System.out.println("d.e.y yVel-y = " + yVel);

							float dPos = inner.get("d").getAsFloat();
							System.out.println("d.e.d dPos-d = " + dPos);

							float vPos = inner.get("v").getAsFloat();
							System.out.println("d.e.v vPos-v = " + vPos);

						// Update here
						//Entity temp = CoreLogic.findEntity(id);
						//temp.teleport(null, null, xPos, yPos, dPos, vPos, xVel, yVel);
						} 
						else {
					//CoreLogic.removeEntity(id);
					}
				}
			}
		} catch (UnknownHostException e) {

		} catch (IOException e) {

		}/* catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}*/
		// Cave Johnson, we're done here.
	}
}
