package edu.smith.cs.csc212.p5;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Level {
	List<Enemy> Enemies = new ArrayList<Enemy>();

	/**
	 * Construct a Level
	 */
	public Level(Point2D p, LinkedList<Point2D> path) {
		// TODO populate the level with better Enemies
		Enemy pathTester = new Enemy(path, 15);
		Enemies.add(pathTester);
		for (int i = 0; i < 2; i++) {
			Enemy e = new Enemy(new Point2D.Double(p.getX(), p.getY()), 15);
			Enemies.add(e);
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
}
