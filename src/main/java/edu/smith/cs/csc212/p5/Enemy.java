package edu.smith.cs.csc212.p5;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Enemy {
	Point2D location;
	Point2D destination;
	// TODO fill this out
	List<Point2D> destPoints;
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
	float size = 20;
	// Vars that talk about enemy size
	int fishWidth = 80;
	int fishHeight = 40;
	// width of a tile in the world

	int tileW = 60;

	/**
	 * Construct the enemy.
	 * 
	 * @param startPt Initial position
	 * @param speed   Speed in pixels per second.
	 */
	public Enemy(Point2D startPt, float speed) {
		this.initHealth = 100;
		this.health = 100;
		this.location = new Point2D.Double(startPt.getX(), startPt.getY());
		this.speed = speed;
		this.color = Color.orange;
		this.destination = new Point2D.Float(600, 600);
		this.healthDebit = 0;
		this.destPoints = new LinkedList<Point2D>();
	}

	/**
	 * Construct an enemy that uses the Point2D Path mechanic. Fish created with this constructor will be green.
	 * 
	 * @param path
	 */
	public Enemy(LinkedList<Point2D> path, float speed) {
		if (path.size() < 2) {
			throw new AssertionError("I need at least a start and an end point!");
		}
		this.initHealth = 100;
		this.health = 100;
		this.location = path.getFirst();
		path.remove(0);
		this.speed = speed;
		this.color = Color.green;
		this.destination = path.getFirst();
		this.healthDebit = 0;
		this.destPoints = (LinkedList<Point2D>) path;
	}

	/**
	 * Construct the enemy, but don't give it a location yet.
	 * 
	 * @param speed Speed in pixels per second.
	 */
	public Enemy(float speed) {
		this.initHealth = 100;
		this.health = this.initHealth;
		this.speed = speed;
		// Our enemy is a circle for now!
		// this.shape = new Ellipse2D.Float((float) this.location.getX(), (float)
		// this.location.getY(), this.size,
		// this.size);
		this.color = Color.orange;
		this.healthDebit = 0;
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

		g2.translate(x - fishWidth / 2, y - fishHeight / 2);
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
		g3.translate(x - fishWidth / 2, y - fishHeight / 2);
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
			if (this.destination.distance(this.location) < 3 && destPoints.size() > 1) {
				// pop off the current point
				destPoints.remove(0);
				// get the next one
				this.destination = (Point2D) destPoints.get(0);
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

}
