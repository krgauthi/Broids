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
	
	//Eliminated "?" operator for below methods (? true:false is redundant)
	public boolean equals(String dir){
		return this.toString().equals(dir);
	}
	
	public boolean equals(InputDir dir){
		return this.toString().equals(dir.toString());
	}
}
