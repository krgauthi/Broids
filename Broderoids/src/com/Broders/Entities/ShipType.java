package com.Broders.Entities;

import com.Broders.Logic.Settings;

public enum ShipType implements Type{

	CLASSIC ("00", "classic", Settings.img_path + "ship1.png", Settings.img_path + "ship2.png");
	
	private String type;
	private String str;
	private String sprite1;
	private String sprite2;
	
	private ShipType(String type, String str, String sprite1, String sprite2){
		this.type = type;
		this.str = str;
		this.sprite1 = sprite1;
		this.sprite2 = sprite2;
	}
	
	public String getTypeValue(){
		return type;
	}
	
	/**
	 * Do not use.  Ship has two sprites, which must be returned separately.
	 */
	@Deprecated
	public String getSpritePath() {
		return null;
	}
	
	public String getSprite1Path(){
		return sprite1;
	}
	
	public String getSprite2Path(){
		return sprite2;
	}
	
	public String toString(){
		return this.str;
	}
	
	public boolean equals(Type type) {
		return type.toString().equals(this.str);
	}
}
