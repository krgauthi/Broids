package com.Broders.mygdxgame;

import java.io.*;
import java.util.*;

public class ScoresManager {

	private static String path;
	private static File file;
	private static Scanner fileReader;
	private static PrintWriter fileWriter;
	private static String encrypted [][];
	private static String decrypted [][];

	public static void init(BaseGame g) throws FileNotFoundException{
		path = "config/HS.bro";
		file = new File(path);
		encrypted = new String[10][3];
		if(file.exists()){
			fileReader = new Scanner(file);
			for(int i = 0; i < 10; i++){
				for(int j = 0; j < 3; j++){
					encrypted[i][j] = fileReader.next();
				}
			}
			fileReader.close();
			decrypted = new String[10][3];
			decrypt();
		}
		else{
			String newDecrypted [][] = {
					{"1.)", "UberBro", "10000"},
					{"2.)", "AwesomeBro", "9000"},
					{"3.)", "SickBro", "8000"},
					{"4.)", "ChillBro", "7000"},
					{"5.)", "Bro", "6000"},
					{"6.)", "SubstandardBro", "5000"},
					{"7.)", "MediocreBro", "4000"},
					{"8.)", "LesserBro", "3000"},
					{"9.)", "UnBro", "2000"},
					{"10.)", "BroKe", "1000"}};
			decrypted = newDecrypted;
			encrypt();
			writeScores();
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

	public static String[][] getScores(){
		return decrypted;
	}

	public static void writeScores() throws FileNotFoundException{
		fileWriter = new PrintWriter(file);
		for(int i = 0; i < 10; i++){
			fileWriter.println(encrypted[i][0] + " " + encrypted[i][1] + " " + encrypted[i][2]);
		}
		fileWriter.close();
	}

	private static void encrypt(){
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 3; j++){
				String s = "";
				for(int k = 0; k < decrypted[i][j].length(); k++){
					int val = (((int) decrypted[i][j].charAt(k)) + 59);
					if(val > 126) val -= 95;
					s = s.concat(String.valueOf((char) val));
				}
				encrypted[i][j] = s;
			}
		}
	}

	private static void decrypt(){
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 3; j++){
				String s = "";
				for(int k = 0; k < encrypted[i][j].length(); k++){
					int val = (((int) encrypted[i][j].charAt(k)) - 59);
					if(val < 32) val += 95;
					s = s.concat(String.valueOf((char) val));
				}
				decrypted[i][j] = s;
			}
		}
	}
}
