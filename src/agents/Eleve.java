package agents;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

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
		drawable.setLayer(1);
		drawable.setColor(Color.RED);
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

	/**
	 * Agent action phase.
	 */
	@Override
	protected void onAct() {

		Salle env = getAmas().getEnvironment();
		
		if(env.getHour() > this.end && this.arrived) {
			this.drawable.hide();
		}

		if (env.getHour() < this.start) {
			this.drawable.hide();
			return;
		} else if (env.getHour() > this.end) {
			this.arrived = false;
			this.place = this.startArea;
		}

		this.drawable.show();

		if (dx == place.getX() && dy == place.getY()) {
			this.arrived = true;
			
		}
		
		if(this.arrived) {
			return;
		}

		Map<Area, Double> distances = new HashMap<Area, Double>();

		/*
		 * double distanceXP = Float.MAX_VALUE; double distanceYM = Float.MAX_VALUE;
		 * double distanceXM = Float.MAX_VALUE; double distanceYP = Float.MAX_VALUE;
		 */

		int x;
		int y;

		x = dx + 1;
		y = dy;
		if (x < 100) {
			distances.put(env.getAreaByPosition(x, y), Math.hypot(x - place.getX(), y - place.getY()));
		}
		x = dx;
		y = dy + 1;
		if (y < 100) {
			distances.put(env.getAreaByPosition(x, y), Math.hypot(x - place.getX(), y - place.getY()));
		}
		x = dx - 1;
		y = dy;
		if (x >= 0) {
			distances.put(env.getAreaByPosition(x, y), Math.hypot(x - place.getX(), y - place.getY()));
		}
		x = dx;
		y = dy - 1;
		if (y >= 0) {
			distances.put(env.getAreaByPosition(x, y), Math.hypot(x - place.getX(), y - place.getY()));
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

	@Override
	protected void onUpdateRender() {
		/*
		 * drawable = VUI.get().createRectangle(dx * 10, dy * 10, 10, 10);
		 * drawable.setLayer(1); drawable.setColor(Color.RED);
		 */

		drawable.move(dx * 10, dy * 10);
	}

	@Override
	protected void onDecide() {
	}

}
