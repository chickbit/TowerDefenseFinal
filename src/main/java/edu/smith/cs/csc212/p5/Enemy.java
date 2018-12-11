package edu.smith.cs.csc212.p5;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

public class Enemy {
	/**
	 * Enemy's current location
	 */
	Point2D location;
	/**
	 * Enemy's destination
	 */
	Point2D destination;
	/**
	 * The path the enemy is walking.
	 */
	List<Tile> destTileList;
	/**
	 * Initial health
	 */
	final float initHealth;
	/**
	 * Current health
	 */
	float health;
	/**
	 * Health to subtract at next Update
	 */
	float healthDebit;
	/**
	 * Speed, in pixels per second
	 */
	float speed; // in pixels per second
	/**
	 * Color
	 */
	Color color;
	/**
	 * Size (doesn't do anything yet.)
	 */
	float size = (float) 0.75;
	// Vars that talk about enemy size
	int fishWidth = 80;
	int fishHeight = 40;
	// width of a tile in the world
	int tileW = 60;
	World w;

	/**
	 * Construct an Enemy
	 * 
	 * @param path  A List of Tiles that the enemy walks on.
	 * @param speed The speed of the enemy.
	 */
	public Enemy(List<Tile> path, float speed) {
		// set speed, color, health
		this.speed = speed;
		this.color = Color.PINK;
		this.initHealth = 100;
		this.healthDebit = 0;
		this.health = 100;

		// this fish works with tiles
		this.destTileList = (LinkedList<Tile>) path;

		// yell if the programmer gives us a crummy path
		if (path.size() < 2) {
			throw new AssertionError("I need at least a start and an end point!");
		}

		// set location to the first point on the path
		this.location = ((LinkedList<Tile>) path).getFirst().getFloatPixelCenter();
		// pop our current location off of the path
		path.remove(0);
		// the new first point is our destination
		this.destination = ((LinkedList<Tile>) path).getFirst().getFloatPixelCenter();
	}

	/**
	 * Draw the Enemy on the Graphics2D g.
	 * 
	 * @param Graphics2D g
	 */
	public void draw(Graphics2D g) {
		double x = this.location.getX();
		double y = this.location.getY();

		Graphics2D g2 = (Graphics2D) g.create();
		// Move the graphics window so we draw on top of our center point

		g2.translate(x - (fishWidth / 2) * size, y - (fishHeight / 2) * size);

		g2.scale(size, size);
		g2.setColor(this.color);

		Shape body = new Ellipse2D.Double(0, 0, 80, 40);
		Shape tail = new Ellipse2D.Double(70, -10, 20, 60);
		Shape eye = new Ellipse2D.Double(25, 10, 10, 10);

		g2.fill(body);

		// draw body outline.
		g2.setColor(Color.black);
		g2.draw(body);

		// draw eye (still black):
		g2.fill(eye);

		// draw tail:
		Color tailColor = color.darker();
		g2.setColor(tailColor);
		g2.fill(tail);

		// draw tail outline.
		g2.setColor(Color.black);
		g2.draw(tail);

		// Draw the health bar on a separate Graphics2D so I can make it
		// fade away when the enemy isn't being hit.

		Graphics2D g3 = (Graphics2D) g.create();
		g3.translate(x - (fishWidth / 2) * size, y - (fishHeight / 2) * size);
		g3.scale(size, size);
		// draw the health bar
		float hungerWidth = (this.initHealth - this.health) / this.initHealth * 60;
		if (hungerWidth <= 0) {
			hungerWidth = 0;
		}
		// The hunger bar is two bars - a background red bar, and a green foreground bar that shrinks as hunger increases
		Shape hungerBarBackground = new Rectangle2D.Double(10, 20 - fishHeight / 2 - 15, 60, 5);
		Shape hungerBar = new Rectangle2D.Double(10, 20 - fishHeight / 2 - 15, 60 - hungerWidth, 5);
		g3.setColor(Color.red);
		g3.fill(hungerBarBackground);
		g3.setColor(Color.green);
		g3.fill(hungerBar);

		g2.dispose();
		g3.dispose();
	}

	/**
	 * Update state of the Enemy based on time since last update.
	 */
	public void update(double t) {
		// Calculate health
		this.health -= this.healthDebit;
		// Reset health debit counter
		this.healthDebit = 0;
		if (this.health <= 0) {

		}
		// TODO If health <= 0, remove the enemy from the World.

		// If we don't have a destination, set it to the current location.
		// This is because I created a constructor for Enemy that doesn't take a location
		// It's possible to have a static enemy.
		if (this.destination.equals(null) && !this.location.equals(null)) {
			this.destination = this.location;
		}
		// If we have a destination and a location, move towards the destination.
		else if (!this.destination.equals(null) && !this.location.equals(null)) {
			// if we are at our destination, get the next destination
			if (this.destination.distance(this.location) < 3) {
				if (destTileList.size() > 1) {
					// pop off current tile
					destTileList.remove(0);
					// get the next one
					this.destination = destTileList.get(0).getFloatPixelCenter();
				}
			}

			// setHypot is change in location/seconds * seconds elapsed
			// calculate diff in x and y and hypot
			double dx = this.destination.getX() - this.location.getX();
			double dy = this.destination.getY() - this.location.getY();
			double hypot = Math.hypot(dx, dy);
			// new x is old x plus cos(x/hypot)
			double newX = this.location.getX() + (dx / hypot) * speed * t;
			// new y is old y plus sin(y/hypot)
			double newY = this.location.getY() + (dy / hypot) * speed * t;
			// Set our location to our new location
			this.location = new Point2D.Float((float) newX, (float) newY);
		}

	}

	/**
	 * The game will hand the Enemy the next point. Enemy will move towards destination by itself.
	 * 
	 * @param nextPt
	 * @return
	 */
	public boolean setNextPoint(Point2D nextPt) {
		// TODO Probably involves throwing some errors if NextPt isn't a float-type
		// Point2D?
		this.destination = nextPt;
		// TODO We should return false if this fails? or if it was the same point. idk.
		return true;
	}

	/**
	 * @return The current location of the Enemy.
	 */
	public Point2D getLocation() {
		return this.location;
	}

	/**
	 * Damage the Enemy
	 * 
	 * @param damage The amount of HP to subtract from the Enemy's total.
	 */
	public void takeDamage(float damage) {
		// Our health-debit bank is updated by the amount of damage
		this.healthDebit += damage;
	}

	/**
	 * @return True if this fish is alive, false if it's dead.
	 */
	public boolean isDead() {
		if (this.health <= 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isAtEnd() {
		// get the end tile
		Tile endTile = this.destTileList.get(destTileList.size() - 1);

		// if our position is within 1 px of the endTile's center, we're at the end.
		if (this.location.distance(endTile.getFloatPixelCenter()) < 1) {
			return true;
		} else {
			return false;
		}
	}

}
