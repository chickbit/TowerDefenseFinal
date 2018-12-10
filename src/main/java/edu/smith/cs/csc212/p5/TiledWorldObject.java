package edu.smith.cs.csc212.p5;

import java.awt.Graphics2D;
import java.util.List;

import me.jjfoley.gfx.IntPoint;

/**
 * Inspiration taken from JJ Foley's
 * WorldObject class in the P2 assignment of Smith's Fall 2018 CSC212 Data Structures class.
 * 
 * A TiledWorldObject is an abtract "item" in the game that lives on a Tile.
 * 
 * @author Barb Garrison
 *
 */
public abstract class TiledWorldObject {
	/**
	 * A List of Tiles that this object occupies
	 */
	private List<Tile> tileList;
	/**
	 * The World this object exists in
	 */
	protected World world;

	// constructor tells those Tiles that they've been built on
	public TiledWorldObject(List<Tile> givenTList, World w) {
		this.world = w;
		// loop over all given tiles
		for (Tile t : givenTList) {
			// add each tile to our own tileList
			this.tileList.add(t);
			// tell each tile that it's been built upon.
			// tiles themselves handle error-checking.
			t.build();
		}
	}

	/**
	 * @return an IntPoint that gives the coordinates, in pixels of the TiledWorldObject's center
	 */
	public IntPoint getCenter() {
		int sumX = 0;
		int sumY = 0;

		// add the centers of all the tiles this object sits on
		for (Tile t : tileList) {
			IntPoint c = t.getIntPixelCenter();
			sumX += c.getX();
			sumY += c.getY();
		}

		// average them
		sumX = sumX / tileList.size();
		sumY = sumY / tileList.size();

		// return the average center
		return (new IntPoint(sumX, sumY));
	}

	/**
	 * Draw this TiledWorldObject!
	 * Abstract so child objects must implement it.
	 * 
	 * @param g Graphics2D API.
	 */
	public abstract void draw(Graphics2D g);

	/**
	 * Update this TiledWorldObject!
	 * Abstract so child objects must implement it.
	 * 
	 * @param secondsSinceLastUpdate how many nanoseconds it's been since the object was last updated.
	 */
	public abstract void update(double secondsSinceLastUpdate);

}
