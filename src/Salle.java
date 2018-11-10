import java.util.ArrayList;
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

	private Area[] windows;
	private Area[] doors;
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
		
		this.windows = new Area[]{areas[0][10], areas[0][20], areas[0][30], areas[0][40], areas[0][50], areas[0][60], areas[0][70], areas[0][80], areas[0][90]};
		this.doors = new Area[]{areas[0][10], areas[0][90]};
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
		return  this.getAmbiantLuminosity() * 0.8f;
	}

	private float getDoorLuminosity() {
		return this.getAmbiantLuminosity() * 0.25f;
	}
	
	private float getNoise() {
		Random r = new Random();
		float noise;
		if (r.nextBoolean()) {
			noise = r.nextFloat() / 5;
		} else {
			noise = - r.nextFloat() / 5;
		}
		return noise;		
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
	
	private void illuminateByCone(Area departure, int maxDistance, float luminosity) {
		for(Area area : getAreaByCone(departure.getX(), departure.getY(), maxDistance)) {
			
		}
	}

	public ArrayList<Area> getAreaAround(int x, int y, int radius) {
		ArrayList<Area> area_around = new ArrayList<Area>();
		for (int i = x - radius; i < x + radius; i++) {
			for (int j = y - radius; j < y + radius; j++) {
				area_around.add(this.areas[i][j]);
			}
		}
		return area_around;
	}

	public ArrayList<Area> getAreaByCone(int x, int y, int maxDistance) {
		ArrayList<Area> areaCone = new ArrayList<Area>();
		for (int i = 0; i < maxDistance; i += 1) {
			for (int j = 0; j < i; j += 1) {
				areaCone.add(this.areas[x + i][y + j]);
				if(j != 0 && y - j>=0) {
					areaCone.add(this.areas[x + i][y - j]);
				}
			}
		}
		return areaCone;
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
