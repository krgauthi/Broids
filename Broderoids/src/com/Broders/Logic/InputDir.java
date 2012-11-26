package com.Broders.Logic;

/**
 * This enum will be used for handling the input direction. Mostly here for now
 * in case any additional values/handling need to be passed with a direction. If
 * that never becomes the case, this can be removed in favor of simply using
 * Strings to denote direction.
 * 
 * @author ntpeters
 * 
 */
public enum InputDir {

	FORWARD ("forward"),
	BACKWARD ("backward"),
	LEFT ("left"),
	RIGHT ("right"),
	SHOOT ("shoot"),
	NOSHOOT ("noshoot"), //Can be used to disable rapid-fire
	NULL ("");

	private String dir;

	/**
	 * Initializes the direction.
	 * 
	 * @param dir
	 *            Direction
	 */
	private InputDir(String dir) {
		this.dir = dir;
	}

	/**
	 * Returns the string value of the direction.
	 * 
	 * @return Direction as a String
	 */
	public String toString() {
		return this.dir;
	}

	/**
	 * Determines the equality between this direction and the given direction.
	 * 
	 * @param dir
	 *            Direction to compare against
	 * @return True if the directions are the same, false otherwise
	 */
	public boolean equals(String dir) {
		return this.toString().equals(dir);
	}

	/**
	 * Determines the equality between this direction and the given direction.
	 * 
	 * @param dir
	 *            Direction to compare against
	 * @return True if the directions are the same, false otherwise
	 */
	public boolean equals(InputDir dir) {
		return this.toString().equals(dir.toString());
	}
}
