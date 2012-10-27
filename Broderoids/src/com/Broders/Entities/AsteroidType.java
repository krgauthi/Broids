package com.Broders.Entities;

import com.Broders.Logic.Settings;

public enum AsteroidType implements Type{

	SMALL ("00", "small", Settings.img_path + "broid.png"),
	MEDIUM ("01", "medium", Settings.img_path + "broid2.png"),
	LARGE ("02", "large", Settings.img_path + "broid3.png");
	
	private String type;
	private String str;
	private String sprite;
		
	private AsteroidType(String type, String str, String sprite){
		this.type = type;
		this.str = str;
		this.sprite = sprite;
	}
	
	public String getTypeValue(){
		return type;
	}
	
	public String getSpritePath(){
		return sprite;
	}
	
	public String toString(){
		return this.str;
	}
	
	public boolean equals(Type type) {
		return type.toString().equals(this.str);
	}	
}
