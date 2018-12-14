package edu.smith.cs.csc212.p5;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Projectile {
	/**
	 * The World the Projectile exists in.
	 */
	private World w;
	/**
	 * The Enemy the Projectile is fired upon.
	 */
	Enemy e;
	/**
	 * Whether or not the Projectile is a homing projectile.
	 */
	boolean isTracker;
	/**
	 * Location of the tower
	 */
	Point2D towerLocation;
	/**
	 * Projectile's current location
	 */
	Point2D location;
	/**
	 * Projectile's destination.
	 */
	Point2D dest;
	/**
	 * Amount of damage this Projectile will deal to an Enemy.
	 */
	public final float damage = 20;
	/**
	 * Speed, in pixels per second
	 */
	public final float speed = 250;

	private float timeAlive = 0;
	final double towerRange = 5.0;

	/**
	 * Constructor
	 * 
	 * @param world  The World the Projectile exists in.
	 * @param target The Enemy the Projectile is fired upon.
	 * @param homing Whether or not the Projectile is a homing projectile.
	 * @param loc    Where the Projectile fires from.
	 */
	public Projectile(World world, Enemy target, boolean homing, Point2D loc) {
		this.w = world;
		this.e = target;
		this.towerLocation = new Point2D.Double(loc.getX(), loc.getY());
		this.isTracker = homing;
		this.setDestination();

		this.location = new Point2D.Double(loc.getX(), loc.getY());
		// Adds itself to the World's list of projectiles.
		w.addProjectile(this);
	}

	/**
	 * Sets the destination of the projectile.
	 */
	private void setDestination() {
		System.out.println("setDestination called.");
		// if we're a tracker, our destination is the fish
		if (isTracker) {
			System.out.println("This is a tracker! No further work required.");
			this.dest = e.getLocation();
		}

		// if we're not a tracker, we're gonna fire towards the fish but our actual "destination" is the outside edge of the circle
		else {
			// get point of fish
			System.out.println("Tower location: " + towerLocation);
			Point2D ePt = e.getLocation();
			System.out.println("Fish location: " + ePt);
			// (tower) - (fish)
			Point2D vector = new Point2D.Double(ePt.getX() - this.towerLocation.getX(),
					ePt.getY() - this.towerLocation.getY());
			System.out.println("Fish - tower: " + vector);
			// get magnitude
			double magnitude = vector.distance(0, 0);
			System.out.println("Magnitude: " + magnitude);
			// now we have a unit vector
			vector.setLocation(vector.getX() / magnitude, vector.getY() / magnitude);
			System.out.println("Now it's a unit vector: " + vector);
			// multiply by tower range
			vector.setLocation(vector.getX() * this.towerRange, vector.getY() * this.towerRange);
			System.out.println("And now it's the right size: " + vector);
			// add to tower
			vector.setLocation(this.towerLocation.getX() + vector.getX(), this.towerLocation.getY() + vector.getY());
			System.out.println("And now it's centered on the tower again: " + vector);
			// return it
			this.dest = vector;
		}
	}

	/**
	 * Update state of the Projectile based on time since last update.
	 */
	public void update(double t) {
		this.timeAlive += t;
		if (w == null) {
			return;
		}
		// If we are a tracker, point us towards the enemy we're tracking
		if (this.isTracker) {
			setDestination();
		}
		// if we hit our target, do damage and then remove ourselves
		if (this.location.distance(this.dest) < 5) {
			if (this.location.distance(e.getLocation()) < 5) {
				e.takeDamage(50);
			}
			this.w.removeProjectile(this);
			this.w = null;
			return;
		}

		// If we don't have a destination, set it to the current location.
		// setHypot is change in location/seconds * seconds elapsed
		// calculate diff in x and y and hypot
		double dx = this.dest.getX() - this.location.getX();
		double dy = this.dest.getY() - this.location.getY();
		double hypot = Math.hypot(dx, dy);
		// System.out.println("dx = " + dx + "| dy = " + dy + "| hyp = " + hypot);
		// new x is old x plus cos(x/hypot)
		double newX = this.location.getX() + (dx / hypot) * speed * t;
		// new y is old y plus sin(y/hypot)
		double newY = this.location.getY() + (dy / hypot) * speed * t;
		// System.out.println("newx = " + newX + "| newy = " + newY);
		// Set our location to our new location
		if (this.timeAlive > 5) {
			// System.out.println("I'm a long-lasting bullet. Here's my location: " + this.location
			// + ", and here's my destination" + this.dest);
		}
		this.location = new Point2D.Float((float) newX, (float) newY);
	}

	/**
	 * Draw the Projectile on the World
	 */
	public void draw(Graphics2D g) {
		g.setColor(Color.black);
		Shape bullet = new Ellipse2D.Float((float) this.location.getX(), (float) this.location.getY(), 5, 5);
		g.fill(bullet);
	}
}
