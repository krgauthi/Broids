package com.Broders.mygdxgame;

import java.io.*;
import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ScoresManager {

	private static BaseGame game;
	public static String data_path = "data/";
	private static Preferences prefs;
	private static String scores [][] = {
		{"1.)", "", ""},
		{"2.)", "", ""},
		{"3.)", "", ""},
		{"4.)", "", ""},
		{"5.)", "", ""},
		{"6.)", "", ""},
		{"7.)", "", ""},
		{"8.)", "", ""},
		{"9.)", "", ""},
		{"10.)", "", ""}};
	private static String preset [][] = {
		{"UberBro", "10000"},
		{"AwesomeBro", "9000"},
		{"SickBro", "8000"},
		{"ChillBro", "7000"},
		{"Bro", "6000"},
		{"SubstandardBro", "5000"},
		{"MediocreBro", "4000"},
		{"LesserBro", "3000"},
		{"UnBro", "2000"},
		{"BroKe", "1000"}};

	public static void init(BaseGame g){
		game = g;
		prefs = Gdx.app.getPreferences("broids-prefs");
		load();
	}

	public static void addScore(String player, int s){
		for(int i = 0; i < 10; i++){
			if(s > Integer.parseInt(scores[i][2])){
				int temp = Integer.parseInt(scores[i][2]);
				scores[i][2] = String.valueOf(s);
				s = temp;
				String tempP = scores[i][1];
				scores[i][1] = player;
				player = tempP;
			}
		}
		update();
	}

	public static String[][] getScores(){
		return scores;
	}

	public static void load(){
		scores[0][1] = prefs.getString("first",preset[0][0]);
		scores[0][2] = prefs.getString("firstScore",preset[0][1]);
		scores[1][1] = prefs.getString("second",preset[1][0]);
		scores[1][2] = prefs.getString("secondScore",preset[1][1]);
		scores[2][1] = prefs.getString("third",preset[2][0]);
		scores[2][2] = prefs.getString("thirdScore",preset[2][1]);
		scores[3][1] = prefs.getString("fourth",preset[3][0]);
		scores[3][2] = prefs.getString("fourthScore",preset[3][1]);
		scores[4][1] = prefs.getString("fifth",preset[4][0]);
		scores[4][2] = prefs.getString("fifthScore",preset[4][1]);
		scores[5][1] = prefs.getString("sixth",preset[5][0]);
		scores[5][2] = prefs.getString("sixthScore",preset[5][1]);
		scores[6][1] = prefs.getString("seventh",preset[6][0]);
		scores[6][2] = prefs.getString("seventhScore",preset[6][1]);
		scores[7][1] = prefs.getString("eighth",preset[7][0]);
		scores[7][2] = prefs.getString("eighthScore",preset[7][1]);
		scores[8][1] = prefs.getString("nineth",preset[8][0]);
		scores[8][2] = prefs.getString("ninethScore",preset[8][1]);
		scores[9][1] = prefs.getString("tenth",preset[9][0]);
		scores[9][2] = prefs.getString("tenthScore",preset[9][1]);
		update();
	}
	
	public static void update(){
		prefs.putString("first", scores[0][1]);
		prefs.putString("firstScore", scores[0][2]);
		prefs.putString("second", scores[1][1]);
		prefs.putString("secondScore", scores[1][2]);
		prefs.putString("third", scores[2][1]);
		prefs.putString("thirdScore", scores[2][2]);
		prefs.putString("fourth", scores[3][1]);
		prefs.putString("fourthScore", scores[3][2]);
		prefs.putString("fifth", scores[4][1]);
		prefs.putString("fifthScore", scores[4][2]);
		prefs.putString("sixth", scores[5][1]);
		prefs.putString("sixthScore", scores[5][2]);
		prefs.putString("seventh", scores[6][1]);
		prefs.putString("seventhScore", scores[6][2]);
		prefs.putString("eighth", scores[7][1]);
		prefs.putString("eighthScore", scores[7][2]);
		prefs.putString("nineth", scores[8][1]);
		prefs.putString("ninethScore", scores[8][2]);
		prefs.putString("tenth", scores[9][1]);
		prefs.putString("tenthScore", scores[9][2]);
		prefs.flush();
	}
}
