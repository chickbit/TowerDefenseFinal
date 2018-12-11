package edu.smith.cs.csc212.p5;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import me.jjfoley.gfx.GFX;
import me.jjfoley.gfx.IntPoint;
import me.jjfoley.gfx.TextBox;

/*
 * Plan:
 * 
 * Enemies are a list of enemies in the World
 * They draw on the map
 * Tower class with variables
 * Towers are in a map in the World
 * They are a map of IntPoints to Towers
 * Towers draw on the world
 * 
 * E have health bars
 * E have takeDamage method
 * Tower lights up its radius every x seconds
 * 
 * E move along path
 * 
 * If an enemy is within tower radius during pulse it gets minus health
 * 
 */
public class TDGame extends GFX {
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	/**
	 * Game size (visual). Try changing this to 600.
	 */
	public static int VISUAL_GRID_SIZE = 600;
	/**
	 * Game size (logical).
	 */
	public static int LOGICAL_GRID_SIZE = 10;
	/**
	 * The words appear in the top part of the screen.
	 */
	public static int TOP_PART = 50;
	/**
	 * There's a border to make it look pretty (the board is inset by this much).
	 */
	public static int BORDER = 5;
	/**
	 * This TextBox wraps up making fonts and centering text.
	 */
	TextBox gameState = new TextBox("");
	/**
	 * This is a rectangle representing the TOP_PART of the screen.
	 */
	Rectangle2D topRect;
	/**
	 * The World our game is in
	 */
	World world;
	/**
	 * The set of monsters we want to put on the map
	 */
	Level level;
	/**
	 * The number of mobs that can make it to the end of the map before the Player dies
	 */
	int playerLives;
	/**
	 * The amount of money the Player has to spend on towers.
	 */
	int playerMoney;

	/**
	 * Construct a TDGame.
	 * 
	 * @param width  window width
	 * @param height window height
	 */
	public TDGame() {
		// super(VISUAL_GRID_SIZE + BORDER * 2, VISUAL_GRID_SIZE + BORDER * 2 + TOP_PART);
		super(VISUAL_GRID_SIZE, VISUAL_GRID_SIZE);
		this.world = new World();
		// this.level = new Level();
		gameState.color = Color.WHITE;
		gameState.setFont(TextBox.BOLD_FONT);
		gameState.setFontSize(TOP_PART / 3.0);
		topRect = new Rectangle2D.Double(0, 0, getWidth(), TOP_PART);
	}

	/**
	 * Draw everything!
	 * 
	 */
	public void draw(Graphics2D g) {
		// Draw the background
		g.setColor(world.getBkgColor());
		g.fillRect(0, 0, getWidth(), getHeight());
		int w = getTileW();
		int l = getTileH();
		// Draw the path

		for (IntPoint p : world.logicalPathP2D) {
			g.setColor(world.getPathColor());
			g.fillRect((int) p.getX() * w, (int) p.getY() * l, w, l);
		}

		// Draw the objects in the world
		world.drawTowers(g);

		// Draw the grid
		g.setColor(world.getBkgColor().darker().darker());
		for (int h = 0; h < LOGICAL_GRID_SIZE; h++) {
			for (int k = 0; k < LOGICAL_GRID_SIZE; k++) {
				g.drawRect(h * w, l * k, w, l);
			}
		}

		// draw the stuff that's on top of the grid
		world.drawEnemies(g);
		world.drawElse(g);

		// draw mouse hovering
		IntPoint hover = mouseToGame(this.getMouseLocation());
		if (hover != null) {
			g.setColor(new Color(1, 1, 1, 0.5f));
			g.fillRect(hover.x * w, hover.y * l, w, l);
		}

	}

	/**
	 * How big is a tile?
	 * 
	 * @return this returns the tile width.
	 */
	private int getTileW() {
		return WIDTH / LOGICAL_GRID_SIZE;
	}

	/**
	 * How big is a tile?
	 * 
	 * @return this returns the tile height.
	 */
	private int getTileH() {
		return HEIGHT / LOGICAL_GRID_SIZE;
	}

	// We are going to update the logic separately from the drawing.
	@Override
	public void update(double secondsSinceLastUpdate) {
		// do stuff.
		world.update(secondsSinceLastUpdate);
	}

	/**
	 * Convert Mouse coordinates to Grid coordinates.
	 * 
	 * @param mouse maybe a Mouse location (or null).
	 * @return null or the grid coordinates of the Mouse.
	 */
	public IntPoint mouseToGame(IntPoint mouse) {
		if (mouse == null)
			return null;
		int x = mouse.x;
		int y = mouse.y;
		if (x > 0 && x <= VISUAL_GRID_SIZE && y > 0 && y <= VISUAL_GRID_SIZE) {
			int tx = x / getTileW();
			int ty = y / getTileH();
			return new IntPoint(tx, ty);
		}
		return null;
	}

	/**
	 * Play the game!
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("P5 Main Started!");
		// Start the game with the hard-coded width and height.
		GFX app = new TDGame();

		app.start();
	}
}
