package agents;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import controller.DrAmas;
import env.Salle;
import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.ui.VUI;
import fr.irit.smac.amak.ui.drawables.Drawable;
import fr.irit.smac.amak.ui.drawables.DrawableRectangle;
import ressources.Area;
import ressources.source.Source;

public class Eleve extends Agent<DrAmas, Salle> {

	private int dx, dy;

	private Area place;
	private Area startArea;
	private int start;
	private int end;

	private Drawable drawable;

	private boolean arrived;

	private boolean departure;

	private Drawable drawableMad;

	public Eleve(DrAmas amas, int x, int y, Area place) {
		super(amas, x, y);

		System.out.println("Je vais " + place);
		this.startArea = getAmas().getEnvironment().getAreaByPosition(dx, dy);
		this.place = place;
		this.start = 8;
		this.end = 18;
	}

	@Override
	protected void onInitialization() {

		dx = (int) params[0];
		dy = (int) params[1];
	}

	@Override
	protected void onRenderingInitialization() {
		// drawable = VUI.get().createRectangle(dx * 10, dy * 10, 10, 10);
		drawable = VUI.get().createImage(dx * 10, dy * 10, "data/eleve.png");
		drawableMad = VUI.get().createImage(dx * 10, dy * 10, "data/eleve_mad.png");
		drawableMad.setLayer(1);
		drawable.setLayer(1);
		// drawable.setColor(Color.RED);
		drawable.hide();
		drawableMad.hide();
	}

	/**
	 * Getter for the x coordinate
	 * 
	 * @return the x coordinate
	 */
	public int getX() {
		return dx;
	}

	/**
	 * Getter for the y coordinate
	 * 
	 * @return the y coordinate
	 */

	public int getY() {
		return dy;
	}

	public boolean isPresent() {
		return this.drawable.isVisible();
	}

	private void move(boolean random) {

		Salle env = getAmas().getEnvironment();
		Map<Area, Double> distances = new HashMap<Area, Double>();

		int x;
		int y;

		x = dx + 1;
		y = dy;
		if (x < 100) {
			if (random) {
				distances.put(env.getAreaByPosition(x, y), new Random().nextDouble());
			} else {
				distances.put(env.getAreaByPosition(x, y), Math.hypot(x - place.getX(), y - place.getY()));
			}

		}
		x = dx;
		y = dy + 1;
		if (y < 100) {
			if (random) {
				distances.put(env.getAreaByPosition(x, y), new Random().nextDouble());
			} else {
				distances.put(env.getAreaByPosition(x, y), Math.hypot(x - place.getX(), y - place.getY()));
			}

		}
		x = dx - 1;
		y = dy;
		if (x >= 0) {
			if (random) {
				distances.put(env.getAreaByPosition(x, y), new Random().nextDouble());
			} else {
				distances.put(env.getAreaByPosition(x, y), Math.hypot(x - place.getX(), y - place.getY()));
			}

		}
		x = dx;
		y = dy - 1;
		if (y >= 0) {
			if (random) {
				distances.put(env.getAreaByPosition(x, y), new Random().nextDouble());
			} else {
				distances.put(env.getAreaByPosition(x, y), Math.hypot(x - place.getX(), y - place.getY()));
			}

		}

		Area next = null;
		double min = Double.MAX_VALUE;
		for (Map.Entry<Area, Double> entry : distances.entrySet()) {
			if (entry.getValue() < min) {
				min = entry.getValue();
				next = entry.getKey();
			}
		}

		this.dx = next.getX();
		this.dy = next.getY();
	}

	private boolean isHappy() {
		Salle env = getAmas().getEnvironment();

		int x = 50 + new Random().nextInt() % 12;
		int y = 5 + new Random().nextInt() % 6;
		boolean ret = true;
		
		//System.out.println(x+","+y);

		if (env.getAreaByPosition(x, y).getLuminosity() < 0.5) {
			env.setBadArea(x, y);
			ret = false;
		}

		if (env.getAreaByPosition(dx, dy).getLuminosity() < 0.5) {
			env.setBadArea(dx, dy);
			ret = false;
		}
		if (new Random().nextDouble() > 0.999) {
			env.setBadArea(dx, dy);
			ret = false;
		}
		return ret;
	}

	/**
	 * Agent action phase.
	 */
	@Override
	protected void onAct() {

		Salle env = getAmas().getEnvironment();

		if (this.drawable.isVisible() || this.drawableMad.isVisible()) {
			if (!this.isHappy()) {
				this.drawableMad.show();
				this.drawable.hide();
			} else {
				this.drawableMad.hide();
				this.drawable.show();
			}
		}

		if (env.getHour() < this.start) {
			this.drawable.hide();
			return;
		} else if (env.getHour() >= this.end) {
			this.departure = true;
			this.place = this.startArea;
		} else if (env.getHour() >= 12 && env.getHour() <= 13) {
			this.arrived = false;
			this.move(true);
			this.move(true);
			this.move(true);
			this.move(true);
			return;
		}

		this.drawable.show();

		if (dx == place.getX() && dy == place.getY()) {
			this.arrived = true;
			this.departure = false;
		}

		if (env.getHour() >= this.end && !this.departure) {
			this.drawableMad.hide();
			this.drawable.hide();
		}

		if (this.arrived && !this.departure) {
			return;
		}

		/*
		 * double distanceXP = Float.MAX_VALUE; double distanceYM = Float.MAX_VALUE;
		 * double distanceXM = Float.MAX_VALUE; double distanceYP = Float.MAX_VALUE;
		 */

		this.move(false);
		this.move(false);
		this.move(false);
		this.move(false);

	}

	@Override
	protected void onUpdateRender() {
		/*
		 * drawable = VUI.get().createRectangle(dx * 10, dy * 10, 10, 10);
		 * drawable.setLayer(1); drawable.setColor(Color.RED);
		 */

		drawable.move(dx * 10, dy * 10);
		drawableMad.move(dx * 10, dy * 10);
	}

	@Override
	protected void onDecide() {
	}

}
