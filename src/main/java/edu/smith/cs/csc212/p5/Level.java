package edu.smith.cs.csc212.p5;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Level {
	private List<Tile> tilePath;
	List<Enemy> Enemies = new ArrayList<Enemy>();

	/**
	 * Construct a Level
	 */
	public Level(Point2D p, LinkedList<Point2D> P2DPath, LinkedList<Tile> tp) {
		// instantiate
		this.tilePath = new LinkedList<Tile>();

		// copy the tilePath - must do this instead of direct setting
		// else enemies use the list statically and can't reset
		for (Tile t : tp) {
			this.tilePath.add(t);
		}
		// add a bunch of enemies
		for (int i = 0; i < 10; i++) {
			// System.out.println("Tilepath: " + tilePath);
			Enemy tilePathTester = new Enemy(new LinkedList<>(tilePath), 50);
			Enemies.add(tilePathTester);
		}
	}

	/**
	 * @param levelNum The level number
	 * @return a List of Enemies
	 */
	public List<Enemy> getEnemies(int levelNum) {
		// We haven't implemented different levels based on level numbers yet.
		return Enemies;
	}

	/**
	 * @return the List<Tile> for enemies to follow.
	 */
	public List<Tile> getTilePath() {
		return this.tilePath;
	}
}
