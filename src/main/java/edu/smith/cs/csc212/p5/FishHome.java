package edu.smith.cs.csc212.p5;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import me.jjfoley.gfx.IntPoint;

public class FishHome {

	/**
	 * For graphically drawing the roof.
	 */
	final Polygon roof;
	/**
	 * For graphically drawing the house.
	 */
	final Polygon house;
	private World w;
	private int x;
	private int y;

	public FishHome(World world, IntPoint location) {
		this.x = location.x;
		this.y = location.y;
		w = world;
		// I drew this out on graph paper.
		roof = new Polygon();
		roof.addPoint(1, 4);
		roof.addPoint(9, 4);
		roof.addPoint(5, 1);

		house = new Polygon();
		house.addPoint(2, 4);
		house.addPoint(2, 9);
		house.addPoint(3, 9);
		house.addPoint(3, 6);
		house.addPoint(5, 5);
		house.addPoint(7, 6);
		house.addPoint(7, 9);
		house.addPoint(8, 9);
		house.addPoint(8, 4);
	}

	public void draw(Graphics2D g) {
		Graphics2D scale = (Graphics2D) g.create();
		scale.translate(x * w.getTileSize(), y * w.getTileSize());
		scale.scale(6.0, 6.0);
		scale.setColor(Color.black);
		scale.fillPolygon(roof);
		scale.setColor(Color.red);
		scale.fillPolygon(house);
	}

}
