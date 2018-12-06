package edu.smith.cs.csc212.p5;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

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
		this.setDestination();
		this.isTracker = homing;
		this.location = (Float) loc;
		// Adds itself to the World's list of projectiles.
		w.addProjectile(this);
	}

	/**
	 * Sets the destination of the projectile.
	 */
	private void setDestination() {
		this.dest = e.getLocation();
	}

	/**
	 * Update state of the Projectile based on time since last update.
	 */
	public void update(double t) {
		if (w == null) {
			return;
		}
		// If we are a tracker, point us towards the enemy we're tracking
		if (this.isTracker) {
			setDestination();
		}
		// if we hit our target, do damage and then remove ourselves
		if (this.location.distance(this.dest) < 2) {
			e.takeDamage(10);
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
