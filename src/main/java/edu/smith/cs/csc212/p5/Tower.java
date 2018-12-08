package edu.smith.cs.csc212.p5;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

import com.sun.media.sound.SoftMixingSourceDataLine;

import me.jjfoley.gfx.IntPoint;

import java.awt.geom.Rectangle2D;

public class Tower implements Comparable<Tower> {
	/**
	 * Target priorities of Towers
	 */
	// TODO fix string array
	// final String[] targetPriorities = new String["first", "last", "strong"];
	/**
	 * Position of tower on board.
	 */
	IntPoint pos;
	/**
	 * World we are in
	 */
	World w;
	/**
	 * If this tower's projectiles are homing missiles
	 */
	boolean isHoming;
	/**
	 * Range of the tower (pixels)
	 */
	float range;
	double coolDown = 3;
	double sumTime = 0;

	/**
	 * Construct the tower. Tower will hang to the lower-right of the given position.
	 * 
	 * @param position
	 */
	public Tower(IntPoint position, World world) {
		System.out.println(position);
		int x = (int) (position.getX() * 60);
		int y = (int) (position.getY() * 60);
		this.pos = new IntPoint(x, y);
		this.w = world;
		this.range = 200;
		System.out.println(this.pos);
	}

	/**
	 * Draw the tower on the world.
	 * 
	 * @param g
	 */
	public void draw(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setColor(new Color(255, 255, 255, 50));
		Shape range = new Ellipse2D.Double(pos.getX() - this.range, pos.getY() - this.range, this.range * 2,
				this.range * 2);
		g2.fill(range);
		g2.setColor(Color.white);
		Shape body = new Rectangle2D.Double(pos.getX() - 15, pos.getY() - 15, 30, 30);
		g2.fill(body);
		g2.dispose();
	}

	/**
	 * Fire a projectile.
	 * 
	 * @param The Enemy that triggered the tower's Fire response
	 */
	public void fireProjectile(Enemy e, Double t) {
		// This will be triggered by something passing into the range of the tower
		// Create new Projectile with
		// Same World the Tower is in
		// the Enemy we pass the Fire method
		// Whether or not the projectile is a homing missile
		// The position of the tower as a starting point
		this.sumTime += t;
		if (this.sumTime > this.coolDown) {
			sumTime = 0;
			Projectile p = new Projectile(this.w, e, false, this.pos);
			w.addProjectile(p);
			System.out.println("projectile");
		}
	}

	@Override
	public int compareTo(Tower t) {
		// If we are the same tower, we are in the same location.
		if (this.equals(t)) {
			return 0;
		}
		// If we are left of t, we are greater.
		if (this.pos.getX() < t.pos.getX()) {
			return 1;
		} else if (this.pos.getX() == t.pos.getX()) {
			// If they are at the same x, the one that appears highest to the user wins.
			if (this.pos.getY() < t.pos.getY()) {
				return 1;
			}
		}
		return -1;
	}

	public void update(double secondsSinceLastUpdate) {

	}

}
