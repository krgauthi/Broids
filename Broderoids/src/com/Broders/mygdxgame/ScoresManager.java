package com.Broders.mygdxgame;

import java.io.*;
import java.util.*;

public class ScoresManager {
	
	private static BaseGame myGame;
	private static String path;
	private static File file;
	private static Scanner fileReader;
	private static PrintWriter fileWriter;
	private static String encrypted [][];
	private static String decrypted [][];
	
	public static void init(BaseGame g) throws FileNotFoundException{
		myGame = g;
		path = "config/HS.bro";
		file = new File(path);
		encrypted = new String[10][3];
		decrypted = new String[10][3];
		if(file.exists()){
			fileReader = new Scanner(file);
			for(int i = 0; i < 10; i++){
				for(int j = 0; j < 3; j++){
					encrypted[i][j] = fileReader.next();
				}
			}
			fileReader.close();
			decrypt();
		}
		else{
			addScore("UberBro", 10000);
			addScore("AwesomeBro", 9000);
			addScore("SickBro", 8000);
			addScore("ChillBro", 7000);
			addScore("Bro", 6000);
			addScore("SubstandardBro", 5000);
			addScore("MediocreBro", 4000);
			addScore("LesserBro", 3000);
			addScore("UnBro", 2000);
			addScore("BroKe", 1000);
		}
	}
	
	public static void addScore(String player, int s){
		int temp;
		String tempName;
		for(int i = 0; i < 10; i++){
			if(s > Integer.parseInt(decrypted[i][2])){
				temp = s;
				s = Integer.parseInt(decrypted[i][2]);
				decrypted[i][2] = String.valueOf(temp);
				tempName = player;
				player = decrypted[i][1];
				decrypted[i][1] = tempName;
			}
		}
		encrypt();
	}
	
	public static String[][] getScore(){
		
		return decrypted;
	}
	
	public static void writeScores() throws FileNotFoundException{
		fileWriter = new PrintWriter(file);
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 3; j++){
				fileWriter.println(encrypted[i][0] + " " + encrypted[i][1] + " " + encrypted[i][2]);
			}
		}
	}
	
	private static void encrypt(){
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 3; j++){
				String s = "";
				char word [] = decrypted[i][j].toCharArray();
				for(int k = 0; k < word.length; k++){
					int val = (((int) word[k]) + 231);
					if(val > 255){
						val -= 255;
					}
					char c = (char) val;
					s = s.concat(String.valueOf(c));
				}
				encrypted[i][j] = s;
			}
		}
	}
	
	private static void decrypt(){
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 3; j++){
				String s = "";
				char word [] = encrypted[i][j].toCharArray();
				for(int k = 0; k < word.length; k++){
					int val = (((int) word[k]) - 231);
					if(val < 1){
						val += 255;
					}
					char c = (char) val;
					s = s.concat(String.valueOf(c));
				}
				decrypted[i][j] = s;
			}
		}
	}
}
