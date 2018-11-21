package ressources;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.ui.VUI;
import fr.irit.smac.amak.ui.drawables.DrawableRectangle;
import ressources.source.Source;

/**
 * An area represents a small part of the world that can be scanned by a drone
 * in one cycle
 *
 */
public class Area {
	/**
	 * The X coordinate of the area
	 */
	private int x;
	/**
	 * The Y coordinate of the area
	 */
	private int y;
	/**
	 * The importance of the area. The more this value is high, the more this area
	 * must be scanned often.
	 */
	private DrawableRectangle drawable;
	private double luminosity = 0;
	private Map<Source, Double> sources;

	/**
	 * Constructor of the area
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 */
	public Area(int x, int y) {
		// Set the position
		this.x = x;
		this.y = y;

		this.sources = new HashMap<Source, Double>();

		drawable = VUI.get().createRectangle(x * 10, y * 10, 10, 10);
		drawable.setLayer(0);
	}
	
	public Map<Source, Double> getSources() {
		return this.sources;
	}

	public void addLuminosity(Source source, double lum) {

		this.sources.put(source, lum);
	}

	private void resetLuminosity() {
		this.luminosity = 0f;
	}

	/**
	 * Getter for the X coordinate
	 * 
	 * @return the x coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Getter for the Y coordinate
	 * 
	 * @return the y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Getter for the amount of time since last scan
	 * 
	 * @return the amount of time since last scan
	 */
	public double getLuminosity() {
		return this.luminosity;
	}

	/**
	 * Update the time since last scan at each cycle
	 */
	public void cycle() {

		this.resetLuminosity();

		for (Map.Entry<Source, Double> entry : this.sources.entrySet()) {
			this.luminosity += entry.getValue();
		}

		this.luminosity = Math.min(this.luminosity, 1f);
		if (this.sources.size() > 1) {
			this.luminosity /= 2;
		}

		try {
			drawable.setColor(new Color((float) luminosity, (float) luminosity, 0f));
		} catch (IllegalArgumentException e) {
			System.out.println((float) luminosity);
			e.printStackTrace();

			// Prints what exception has been thrown
			System.out.println(e);
			System.exit(1);
		}

	}

	public String toString() {
		return this.getX() + "," + this.getY();
	}
}