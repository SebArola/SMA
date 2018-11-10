import java.util.Random;

import fr.irit.smac.amak.Environment;
import fr.irit.smac.amak.Scheduling;

/**
 * This class represents the environment of the AMAS which is the world (or at
 * least a part of it)
 *
 */
public class Salle extends Environment {
	public Salle(Object... params) {
		super(Scheduling.DEFAULT, params);
	}

	/**
	 * Areas in the world
	 */
	private Area[][] areas;
	/**
	 * Number of areas in width
	 */
	public final static int WIDTH = 100;
	/**
	 * Number of areas in height
	 */
	public final static int HEIGHT = 100;

	private int minutes;
	private int hour;

	/**
	 * Create the various areas
	 */
	@Override
	public void onInitialization() {

		this.hour = 0;

		areas = new Area[HEIGHT][WIDTH];
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				areas[y][x] = new Area(x, y);
			}
		}
	}

	private float getAmbiantLuminosity() {
		float l;
		float delta = Math.abs(14 - this.hour);
		if (delta >= 7) {
			l = 0.2f;
		} else {
			l = delta / 7;
		}
		return l;
	}
	
	private float getWindowLuminosity() {
		float l;
		Random r = new Random();
		if(r.nextBoolean()) {
			l = this.getAmbiantLuminosity()*0.8f+r.nextFloat()/5;
		}else {
			l = this.getAmbiantLuminosity()*0.8f-r.nextFloat()/5;
		}
		return l;
	}
	
	private float getDoorLuminosity() {
		float l;
		Random r = new Random();
		if(r.nextBoolean()) {
			l = this.getAmbiantLuminosity()*0.25f+r.nextFloat()/5;
		}else {
			l = this.getAmbiantLuminosity()*0.25f-r.nextFloat()/5;
		}
		return l;
	}

	/**
	 * Inform each area at each cycle, 1 cycle = 1 minute
	 */
	@Override
	public void onCycle() {

		this.minutes += 1;
		if (this.minutes == 60) {
			this.hour += 1;
			this.minutes = 0;
		}
		if (this.hour == 24) {
			this.hour = 0;
		}

		System.out.println(this.hour + " heures et " + this.minutes + " minutes");

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				areas[y][x].cycle();
			}
		}
	}

	/**
	 * Getter for the areas
	 * 
	 * @return the areas array
	 */
	public Area[][] getAreas() {
		return areas;
	}

	/**
	 * Get an area at a specific coordinate
	 * 
	 * @param dx the x coordinate
	 * @param dy the y coordinate
	 * @return the area
	 */
	public Area getAreaByPosition(int dx, int dy) {

		if (dx < 0 || dy < 0 || dx >= WIDTH || dy >= HEIGHT)
			return null;
		return areas[dy][dx];
	}
}
