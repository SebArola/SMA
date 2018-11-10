import java.awt.Color;
import java.util.Random;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.ui.VUI;
import fr.irit.smac.amak.ui.drawables.DrawableRectangle;

/**
 * An area represents a small part of the world that can be scanned by a drone
 * in one cycle
 *
 */
public class Area {
	/**
	 * The amount of time (in cycles) since when the area hasn't been scanned
	 */
	private double timeSinceLastSeen = 1000;
	/**
	 * The X coordinate of the area
	 */
	private int x;
	/**
	 * The Y coordinate of the area
	 */
	private int y;
	/**
	 * The importance of the area. The more this value is high, the more this
	 * area must be scanned often.
	 */
	private DrawableRectangle drawable;
	private double luminosity = 0;
	//private double luminosity = new Random().nextDouble();

	/**
	 * Constructor of the area
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 */
	public Area(int x, int y) {
		// Set the position
		this.x = x;
		this.y = y;		

		drawable = VUI.get().createRectangle(x*10, y*10, 10,10);
		drawable.setLayer(0);
	}
	
	public void setLuminosity(double lum) {
		this.luminosity = lum;
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
	 * This method is called when the drone scans the area
	 * 
	 * @param drone
	 *            The drone which scans the area
	 */
	/*public void seen(Agent agent) {
		nextTimeSinceLastSeen = 0;
	}*/

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

		drawable.setColor(new Color((float) luminosity, (float) luminosity, 0f));
	}

	/**
	 * Manually set a hgh criticality to request a scan on a specific area
	 */
	/*public void setCritical() {
		nextTimeSinceLastSeen  = 1000;
	}*/

	/**
	 * Compute the criticality of the area based on the time since last scan
	 * 
	 * @return the criticality of the area
	 */
	/*public double computeCriticality() {
		return Math.min(timeSinceLastSeen * outdateFactor / 1000, 1);
	}*/
}