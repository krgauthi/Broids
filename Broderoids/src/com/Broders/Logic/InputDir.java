package com.Broders.Logic;

public enum InputDir {
	FORWARD ("forward"),
	BACKWARD ("backward"),
	LEFT ("left"),
	RIGHT ("right"),
	NULL ("");
	private String dir;
	
	private InputDir(String dir){
		this.dir = dir;
	}
	
	public String toString(){
		return this.dir;
	}
	
	public boolean equals(String dir){
		return this.toString().equals(dir) ? true : false;
	}
	
	public boolean equals(InputDir dir){
		return this.toString().equals(dir.toString()) ? true : false;
	}
}
