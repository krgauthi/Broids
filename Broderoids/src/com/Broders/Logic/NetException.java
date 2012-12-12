package com.Broders.Logic;

public class NetException extends Exception {
	// Not sure what this is for, but it gets rid of a warning.
	private static final long serialVersionUID = -8839883806819657565L;

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
