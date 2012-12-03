package com.Broders.Logic;

public class NetException extends Exception {
	int code;
	public NetException(int code, String err) {
		super(err);
		this.code = code;
	}
	
	@Override
	public String toString() {
		return Integer.toString(code) + ": " + this.getMessage();
	}
}
