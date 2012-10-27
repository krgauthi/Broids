package com.Broders.Entities;

import com.Broders.Logic.Settings;

public enum BulletType implements Type{

	BASIC ("00", "basic", Settings.img_path + "bullet.png");
	
	private String type;
	private String str;
	private String sprite;
	
	private BulletType(String type, String str, String sprite){
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

	public boolean equals(Type t) {
		return str.toString().equals(this.str);
	}
}
