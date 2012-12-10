package com.Broders.mygdxgame;

import java.io.*;
import java.util.*;

public class ScoresManager {
	
	private static BaseGame myGame;
	private static String path;
	private static File file;
	private static Scanner fileReader;
	private static PrintWriter fileWriter;
	private static String scores [][];
	private static String decrypted [][];
	
	public static void init(BaseGame g) throws FileNotFoundException{
		myGame = g;
		path = "config/HS.bro";
		file = new File(path);
		scores = new String[10][3];
		if(file.exists()){
			fileReader = new Scanner(file);
			for(int i = 0; i < 10; i++){
				for(int j = 0; j < 3; j++){
					scores[i][j] = fileReader.next();
				}
			}
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
		encrypt();
	}
	
	public static void addScore(String player, int s){
		for(String [] a : scores){
			
		}
	}
	
	public static String[][] getScore(){
		
		return scores;
	}
	
	private static void encrypt(){
		for(String[] a : scores){
			for(String s : a){
				char c [] = s.toCharArray();
				for(int i = 0; i < c.length; i++){
					int temp = Character.getNumericValue(c[i]) + 231;
					if(temp > 255){
						temp = temp - 255;
					}
					c[i] = (char) temp;
				}
				s = String.copyValueOf(c);
			}
		}
	}
	
	private static void decrypt(){
		
		for(String[] a : scores){
			for(String s : a){
				char c [] = s.toCharArray();
				for(int i = 0; i < c.length; i++){
					int temp = Character.getNumericValue(c[i]) - 231;
					if(temp < 255){
						temp = temp + 255;
					}
					c[i] = (char) temp;
				}
				s = String.copyValueOf(c);
			}
		}
	}
}
