import java.util.ArrayList;

import fr.irit.smac.amak.Environment;
import fr.irit.smac.amak.Scheduling;

/**
 * This class represents the environment of the AMAS which is the world (or at
 * least a part of it)
 *
 */
public class Salle extends Environment {
	public Salle(Object...params) {
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

	/**
	 * Inform each area at each cycle
	 */
	@Override
	public void onCycle() {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				areas[y][x].cycle();
			}
		}
	}
	
	public ArrayList<Area> getAreaAround(int x, int y, int radius){
		ArrayList<Area> area_around = new ArrayList<Area>();
		for (int i=x-radius; i<x+radius;i++) {
			for (int j=y-radius; j<y+radius;j++) {
				area_around.add(this.areas[i][j]);
			}
		}
		return area_around;
	}
	
	/**
	 * Getter for the areas
	 * @return the areas array
	 */
	public Area[][] getAreas() {
		return areas;
	}

	/**
	 * Get an area at a specific coordinate
	 * 
	 * @param dx
	 *            the x coordinate
	 * @param dy
	 *            the y coordinate
	 * @return the area
	 */
	public Area getAreaByPosition(int dx, int dy) {

		if (dx < 0 || dy < 0 || dx >= WIDTH || dy >= HEIGHT)
			return null;
		return areas[dy][dx];
	}
}
