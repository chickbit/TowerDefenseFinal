package edu.smith.cs.csc212.p5;

import java.awt.geom.Point2D;

import me.jjfoley.gfx.IntPoint;

public class Tile {
	/**
	 * Which column of tiles am I in?
	 * The logical x-coordinate of this Tile.
	 * 0 on the left, increasing as we move right.
	 */
	int logicalX;
	/**
	 * Which row of tiles am I in?
	 * The logical y-coordinate of this Tile.
	 * 0 at the top, increasing as we move down.
	 */
	int logicalY;
	/**
	 * Where do I draw myself?
	 * The x-location, in pixels, of the upper-left corner of this Tile.
	 * We assume that the top-left Tile has its upper-left corner at (0 px, 0px)
	 */
	int pixelX;
	/**
	 * Where do I draw myself?
	 * The y-location, in pixels, of the upper-left corner of this Tile.
	 * We assume that the top-left Tile has its upper-left corner at (0 px, 0px)
	 */
	int pixelY;
	/**
	 * The length, in pixels, of a tile's edge.
	 */
	static int edgeLength;
	/**
	 * Is this tile on the enemy-path?
	 */
	boolean isPath;
	/**
	 * Is there a structure on this tile?
	 */
	boolean isBuilt;

	/**
	 * Construct a new, square Tile.
	 * 
	 * @param lx   - the logical x-coordinate of this tile
	 * @param ly   - the logical y-coordinate of this tile
	 * @param el   - the length, in pixels, of one edge of the tile
	 * @param path - is this tile on the enemy-path?
	 */
	public Tile(int lx, int ly, int el, boolean path) {
		this.logicalX = lx;
		this.logicalY = ly;
		edgeLength = el;
		this.isPath = path;
		// Calculate the pixel-location of the tile
		this.pixelX = lx * el;
		this.pixelY = ly * el;
	}

	/**
	 * @return an IntPoint with the pixel-center of the tile.
	 */
	public IntPoint getIntPixelCenter() {
		int newX = pixelX + edgeLength / 2;
		int newY = pixelY + edgeLength / 2;
		IntPoint center = new IntPoint(newX, newY);
		return center;
	}

	/**
	 * @return a Point2D.Float with the pixel-center of the tile.
	 */
	public Point2D getFloatPixelCenter() {
		float newX = pixelX + edgeLength / 2;
		float newY = pixelY + edgeLength / 2;
		Point2D center = new Point2D.Float(newX, newY);
		return center;
	}

	/**
	 * Convert pixel-coordinates to tile-coordinates
	 * 
	 * @param x the pixel x-coordinate
	 * @param y the pixel y-coordinate
	 * @return an IntPoint containing the coordinates of the Tile that holds that point.
	 */
	public static IntPoint pixelsToTile(int x, int y) {
		int newX = x / edgeLength;
		int newY = y / edgeLength;
		IntPoint tileCoordinate = new IntPoint(newX, newY);
		return tileCoordinate;
	}

	/**
	 * Mark the tile as having a structure built on it
	 * 
	 * @return true if we successfully marked the tile as "built upon," false if that failed.
	 */
	public boolean build() {
		// Check if there's anything on this tile
		if (this.isBuildable()) {
			// if we're all clear, mark it as "built"
			this.isBuilt = true;
			return true;
		}
		// if something went wrong, return false;
		return false;
	}

	/**
	 * Note that there's no checking going on here, this is currently just a setter method.
	 * 
	 * @return true if we successfully marked the tile as "no longer built upon," false if that failed.
	 */
	public boolean demolish() {
		// Should we check for isPath here or just marking stuff as no longer built?
		this.isBuilt = false;
		return true;
	}

	/**
	 * Can the player build upon this tile?
	 */
	public boolean isBuildable() {
		// bug prevention: make sure we haven't somehow put a tower on the path.
		builtOnPathCheck();
		// now we do the check for situations that aren't bugs.
		// if it's on the path, we can't build.
		// if it's not on the path and it's not occupied by another building, we can build
		// otherwise, it's not on the path but it's occupied, so we can't build.
		return (!isPath && !isBuilt);
	}

	/**
	 * Throws an error if we have built upon a tile that is part of the path.
	 */
	public void builtOnPathCheck() {
		if (isPath && isBuilt) {
			throw new AssertionError(
					"A tile thinks that it's part of the path AND has a building on it. This is wrong, fix the code!");
		}
	}

	/**
	 * Spit out the Tile as a readable set of logical and pixel coords.
	 */
	public String toString() {
		String logCoords = "Logical coordinates: (" + this.logicalX + ", " + this.logicalY + ")";
		String pixCoords = "Pixel coordinates: (" + this.pixelX + ", " + this.pixelY + ")";
		return logCoords + " | " + pixCoords;

	}
}
