package edu.smith.cs.csc212.p5;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import me.jjfoley.gfx.IntPoint;

/**
 * 
 * @author barba
 *
 */
public class World {

	// The World will ask Map for stuff.
	// GameGraphics will never ask Map for stuff. World controls everything.

	// This has the stuff, stores the stuff, adds and removes the stuff.
	Map map;
	List<IntPoint> logicalPath;
	/**
	 * The level number.
	 */
	int levNum = 1;
	Level level;
	List<Enemy> waitingEnemies;
	List<Enemy> walkingEnemies;
	List<Enemy> deadEnemies;
	List<Projectile> projectiles;
	List<Projectile> deadProjectiles;
	HashMap<IntPoint, Tower> towers;
	int height = 500;
	int width = 500;
	double coolDown = 3;
	double sumTime = 0;
	FishHome house;
	FishHome startHouse;

	/**
	 * Constructor
	 */
	public World() {
		// Set up the rocks and path
		this.map = new Map(this);
		// this.fish = new Enemy(new Point2D.Float(0, 0), 10);
		// this.tower = new Tower(new Point2D.Float(500, 500), this);
		this.towers = new HashMap<IntPoint, Tower>();
		this.walkingEnemies = new ArrayList<Enemy>();
		this.deadEnemies = new ArrayList<Enemy>();
		this.waitingEnemies = new ArrayList<Enemy>();
		this.projectiles = new ArrayList<Projectile>();
		this.deadProjectiles = new ArrayList<Projectile>();
		loadMap();
		this.level = new Level(map.getstart(), (LinkedList<Point2D>) map.getPath());
		loadLevel(levNum);
		// plop down a house on the last square

		house = new FishHome(this, logicalPath.get(logicalPath.size() - 1));
		// house = new FishHome(this, new IntPoint(5, 5));
		startHouse = new FishHome(this, logicalPath.get(0));

	}

	/**
	 * Inserts a whole bunch of random towers and saves the LogicalPath to World
	 */
	private void loadMap() {
		this.logicalPath = map.getLogicalPath();
		for (int i = 0; i < 5; i++) {
			IntPoint p = generateRandomTowerCoord();
			IntPoint p2 = new IntPoint((int) p.getX() * 60, (int) p.getY() * 60);
			this.towers.put(p2, new Tower(p, this));
		}
	}

	/**
	 * Loads the enemies
	 * 
	 * @param num
	 */
	private void loadLevel(int num) {
		// Move enemies to world
		this.waitingEnemies = this.level.getEnemies(num);
		this.walkingEnemies.add(waitingEnemies.remove(0));
	}

	/**
	 * Update everything.
	 * 
	 * @param secondsSinceLastUpdate
	 */
	public void update(double secondsSinceLastUpdate) {
		// TODO update
		// What order do we update in???
		// release an enemy every 3 seconds
		this.sumTime += secondsSinceLastUpdate;
		if (this.sumTime > this.coolDown && !waitingEnemies.isEmpty()) {
			sumTime = 0;
			walkingEnemies.add(waitingEnemies.remove(0));
		}

		for (Enemy e : this.walkingEnemies) {
			e.update(secondsSinceLastUpdate);
			for (IntPoint p : this.towers.keySet()) {
				System.out.println("Tower: " + p + " Fish: " + e.getLocation());
				if (Math.hypot(e.location.getX() - p.getX(), e.location.getY() - p.getY()) < this.towers.get(p).range) {
					// System.out.println("FIRE!");
					this.towers.get(p).fireProjectile(e, secondsSinceLastUpdate);
				}
			}
		}

		// TODO how do I update towers if they're not iterable??
		// https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
		for (Tower t : this.towers.values()) {
			t.update(secondsSinceLastUpdate);
		}

		for (Projectile p : this.projectiles) {
			p.update(secondsSinceLastUpdate);
		}

		for (Projectile p : this.deadProjectiles) {
			projectiles.remove(p);
		}

		// this.deadProjectiles.clear();
	}

	/**
	 * Draw the enemies
	 */
	public void drawEnemies(Graphics2D g) {
		// TODO the enemy that is farthest along the path should draw first.
		for (Enemy e : this.walkingEnemies) {
			e.draw(g);
		}
	}

	/**
	 * Draw the towers
	 */
	public void drawTowers(Graphics2D g) {
		for (Tower t : this.towers.values()) {
			t.draw(g);
		}
	}

	/**
	 * Draw everything else
	 */
	public void drawElse(Graphics2D g) {
		// draw projectiles
		for (Projectile p : this.projectiles) {
			p.draw(g);
		}

		// draw house at the end of the path
		house.draw(g);
		startHouse.draw(g);
	}

//HELPER METHODS***************
	/**
	 * 
	 * @return the Background Color
	 */
	public Color getBkgColor() {
		return map.getBkgColor();
	}

	/**
	 * @return the color of the enemy Path
	 */
	public Color getPathColor() {
		return map.getPathColor();
	}

	/**
	 * 
	 * @return the Path for the enemies to follow.
	 */
	public List<Point2D> getPath() {
		return map.getPath();
	}

	/**
	 * @return an IntPoint where it is valid to put a tower.
	 */
	public IntPoint generateRandomTowerCoord() {
		int i = 0;
		IntPoint p;
		do {
			int x = (int) (Math.random() * 600 / 60);
			int y = (int) (Math.random() * 600 / 60);
			p = new IntPoint(x, y);

			i++;
		} while (i < 10 && !isValidTowerCoord(p));

		return p;
	}

	/**
	 * @param p Location for a potential tower.
	 * @return true if the point given is a valid place to put a tower.
	 */
	public boolean isValidTowerCoord(IntPoint p) {
		// if it's on the path, no.
		for (IntPoint path : logicalPath) {
			if (p.equals(path)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Convert a Point2D to its nearest IntPoint equivalent.
	 * 
	 * @param p
	 * @return an IntPoint that is very close to p
	 */
	public IntPoint convertP2DToIntPt(Point2D p) {
		int x = (int) p.getX();
		int y = (int) p.getY();
		return (new IntPoint(x, y));
	}

	/**
	 * Add a Projectile to the world.
	 * 
	 * @param p the Projectile to add.
	 */
	public void addProjectile(Projectile p) {
		this.projectiles.add(p);
	}

	/**
	 * @return the Width of the World
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * @return the height of the World
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * @return the length of a side of a square tile
	 */
	public int getTileSize() {
		return 60;
	}

	/**
	 * Adds a Projectile to the queue of things to be deleted
	 * 
	 * @param p
	 */
	public void removeProjectile(Projectile p) {
		deadProjectiles.add(p);
	}

}
