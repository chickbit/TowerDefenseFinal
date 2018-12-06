package edu.smith.cs.csc212.p5;

import java.awt.Color;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.jjfoley.gfx.IntPoint;

/**
 * Everything about the appearance of a specific level map is controlled here.
 * 
 * @author barbara
 *
 */
public class Map {
	// This stores rocks and stuff in a specific configuration
	// Has a path for enemies.
	List<Point2D> path = new LinkedList<Point2D>();
	List<IntPoint> logicalPath = new LinkedList<IntPoint>();
	Color defaultColor = new Color(100, 219, 255);
	Color pathColor = new Color(113, 147, 255);
	Point2D start = new Point2D.Float(300, 300);
	Point2D end = new Point2D.Float(600, 600);
	private World w;

	// probably needs to know its size.
	static final int WIDTH = 960;
	static final int HEIGHT = 640;

	/**
	 * Constructor
	 */
	public Map(World w) {
		// make the map of IntPoints. These are logical grid points.
		// TODO get this to work with LOGICAL_GRID_SIZE
		this.w = w;
		int tw = w.getTileSize();
		for (int i = 1; i < 5; i++) {
			logicalPath.add(new IntPoint(i, 4));
			path.add(new IntPoint(i * tw + 30, 4 * tw + 30));
		}
	}

	/**
	 * @return the List<Point2D> path that enemies follow
	 */
	public List<Point2D> getPath() {
		return this.path;
	}

	public List<IntPoint> getLogicalPath() {
		return this.logicalPath;
	}

	/**
	 * @return the Color to draw the background
	 */
	public Color getBkgColor() {
		return this.defaultColor;
	}

	/**
	 * @return the Color to draw the path
	 */
	public Color getPathColor() {
		return this.pathColor;
	}

	/**
	 * @return the point where enemies start.
	 */
	public Point2D getstart() {
		return this.start;
	}

}
