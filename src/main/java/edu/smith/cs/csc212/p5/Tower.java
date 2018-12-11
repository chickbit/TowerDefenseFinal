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
import java.util.List;

public class Tower extends TiledWorldObject {
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
	public Tower(List<Tile> givenTList, World world) {
		super(givenTList, world);
		this.pos = super.getCenter();
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
			Projectile p = new Projectile(super.world, e, false, this.pos);
			super.world.addProjectile(p);
			System.out.println("projectile");
		}
	}

	public void update(double secondsSinceLastUpdate) {

	}

}
