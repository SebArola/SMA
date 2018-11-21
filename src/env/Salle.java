package env;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import fr.irit.smac.amak.Environment;
import fr.irit.smac.amak.Scheduling;
import ressources.Area;
import ressources.source.ControllableRoundSource;
import ressources.source.Door;
import ressources.source.Source;
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
	
	public ArrayList<Area> badAreas;

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
			this.windows[i - 1] = new Window(areas[i * 10][0], this);
		}
		this.doors = new Door[] { new Door(areas[10][99], this), new Door(areas[90][99], this) };
		
		this.badAreas = new ArrayList<Area>();

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
			this.ambiantLuminosity = 0.15f;
			System.out.println("nuit");
		} else {
			System.out.println("jour with " + delta);
			this.ambiantLuminosity = (7 - delta) / 7 * 0.7f + 0.15f;
		}
	}

	/**
	 * Inform each area at each cycle, 1 cycle = 1 minute
	 */
	@Override
	public void onCycle() {
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
			window.cycle();
		}

		for (Door door : this.doors) {
			door.cycle();
		}

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				areas[y][x].cycle();
			}
		}

	}

	

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

		
		int startSize = 7;

		for (int i = 0; i < maxDistance; i += 1) {
			for (int j = 0; j <= i+startSize; j += 1) {
				int newX = y + j;

				int newY = x + i;
				if (inverse) {
					newY = x - i;
				}
				
				if (newX < HEIGHT && newY < WIDTH && newX >= 0 && newY >= 0) {
					
					areaCone.add(this.areas[newX][newY]);
				}

				newX = y - j;
				
				if (j != 0 && newX >= 0 && newY < WIDTH && newY >= 0) {

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

	public void setBadArea(int dx, int dy) {
		Area a = this.getAreaByPosition(dx, dy);
		 Map<Source, Double> sources = a.getSources();
		 for (Source s : sources.keySet()) {
				if(s.getClass() == ControllableRoundSource.class) {
					ControllableRoundSource roundSource = (ControllableRoundSource) s;
					roundSource.setLuminosity(Math.max(0, Math.min(roundSource.getLuminosity()+0.2, 1)));
				}
		 }
	}
}
