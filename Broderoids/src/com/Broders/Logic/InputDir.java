package com.Broders.Logic;

public enum InputDir {
	FORWARD ("forward"),
	BACKWARD ("backward"),
	LEFT ("left"),
	RIGHT ("right");
	
	private String dir;
	
	private InputDir(String dir){
		this.dir = dir;
	}
	
	public String toString(){
		return this.dir;
	}
}
