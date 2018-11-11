package env;

import java.util.ArrayList;
import java.util.Random;

import fr.irit.smac.amak.Environment;
import fr.irit.smac.amak.Scheduling;
import ressources.Area;
import ressources.source.Door;
import ressources.source.Window;

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

	private Window[] windows;
	private Door[] doors;

	/**
	 * Number of areas in width
	 */
	public final static int WIDTH = 100;
	/**
	 * Number of areas in height
	 */
	public final static int HEIGHT = 100;

	private int minutes = 30;
	private int hour = 6;

	private float ambiantLuminosity;
	
	public int getHour() {
		return this.hour;
	}

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

		this.windows = new Window[9];
		for (int i = 1; i < 10; i += 1) {
			// this.windows[i - 1] = areas[50][0];
			this.windows[i - 1] = new Window(areas[i * 10][0], this);
		}
		this.doors = new Door[] { new Door(areas[10][99], this), new Door(areas[90][99], this) };

	}
	
	public Door[] getDoors() {
		return this.doors;
	}

	public float getAmbiantLuminosity() {
		return this.ambiantLuminosity;
	}

	public void computeAmbiantLuminosity() {
		float delta = Math.abs(14 - (this.hour + this.minutes / 60f));
		if (delta >= 7) {
			this.ambiantLuminosity = 0.07f;
			System.out.println("nuit");
		} else {
			System.out.println("jour with " + delta);
			this.ambiantLuminosity = (7 - delta) / 7 * 0.2f + 0.07f;
		}
	}

	/*
	 * public float getWindowLuminosity() { return this.getAmbiantLuminosity() *
	 * 0.8f; }
	 * 
	 * public float getDoorLuminosity() { return this.getAmbiantLuminosity() *
	 * 0.25f; }
	 */

	/**
	 * Inform each area at each cycle, 1 cycle = 1 minute
	 */
	@Override
	public void onCycle() {

		// this.resetLuminosity();
		this.computeAmbiantLuminosity();

		this.minutes += 1;
		if (this.minutes == 60) {
			this.hour += 1;
			this.minutes = 0;
		}
		if (this.hour == 24) {
			this.hour = 0;
		}

		System.out.println(this.hour + " heures et " + this.minutes + " minutes avec " + this.getAmbiantLuminosity());

		for (Window window : this.windows) {
			// System.out.println("new window at " + area);
			window.cycle();
			// this.illuminateByCone(area, 55, this.getWindowLuminosity(), false); //
			// return;
		}

		for (Door door : this.doors) {
			door.cycle();
			// System.out.println("new door at " + area);
			// this.illuminateByCone(area, 55, this.getDoorLuminosity(), true);
		}

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				areas[y][x].cycle();
			}
		}

	}

	/*
	 * private void resetLuminosity() { for (int x = 0; x < WIDTH; x++) { for (int y
	 * = 0; y < HEIGHT; y++) { areas[y][x].resetLuminosity(); } }
	 * 
	 * }
	 */

	/*
	 * private void illuminateByCone(Area departure, int maxDistance, float
	 * luminosity, boolean inverse) { for (Area area :
	 * getAreaByCone(departure.getX(), departure.getY(), maxDistance, inverse)) {
	 * 
	 * double distance = Math.hypot(departure.getX() - area.getX(), departure.getY()
	 * - area.getY()); double ratioDistance = 1 - (distance / maxDistance); if
	 * (distance > maxDistance) { ratioDistance = 0; } float noisedL =
	 * applyNoise(luminosity);
	 */

	/*
	 * assert (ratioDistance >= 0); assert (ratioDistance <= 1); assert (noisedL >=
	 * 0); assert (noisedL <= 1);
	 */

	// System.out.println("lum : " + luminosity + " ratioDistance " + ratioDistance
	// + " noised " + noisedL
	// + " distance " + distance);

	// System.out.println(ratioDistance * noisedL);

	/*
	 * area.addLuminosity(departure, ratioDistance * noisedL); } }
	 */

	public ArrayList<Area> getAreaAround(int x, int y, int radius) {
		ArrayList<Area> area_around = new ArrayList<Area>();
		for (int i = x - radius; i < x + radius; i++) {
			for (int j = y - radius; j < y + radius; j++) {
				if (j >= 0 && j < WIDTH && i >= 0 && i < HEIGHT)
					area_around.add(this.areas[j][i]);
			}
		}
		return area_around;
	}

	public ArrayList<Area> getAreaByCone(int x, int y, int maxDistance, boolean inverse) {
		ArrayList<Area> areaCone = new ArrayList<Area>();

		// areaCone.add(this.areas[x][y]);

		for (int i = 0; i < maxDistance; i += 1) {
			for (int j = 0; j <= i; j += 1) {
				int newX = y + j;

				int newY = x + i;
				if (inverse) {
					newY = x - i;
				}
				// System.out.println(newX + "," + newY);
				// System.out.println(this.areas.length);
				if (newX < HEIGHT && newY < WIDTH && newX >= 0 && newY >= 0) {
					// System.out.println(this.areas[newX][newY].getX() + "," +
					// this.areas[newX][newY].getY());
					areaCone.add(this.areas[newX][newY]);
				}

				newX = y - j;
				// System.out.println(j + " vs " + (newX));
				if (j != 0 && newX >= 0 && newY < WIDTH && newY >= 0) {

					// System.out.println(newX + "," + (x - j));

					// System.out.println(this.areas[newX][newY].getX() + "," +
					// this.areas[newX][newY].getY());
					areaCone.add(this.areas[newX][newY]);
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
