package agents;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import controller.DrAmas;
import env.Salle;
import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.ui.VUI;
import fr.irit.smac.amak.ui.drawables.DrawableRectangle;
import ressources.Area;
import ressources.source.ControllableRoundSource;

/**
 * This class represent an agent of the system DrAmas
 *
 */
public class Ampoule extends Agent<DrAmas, Salle> {
	/**
	 * Current coordinate of the lightbulb
	 */
	private int dx, dy;

	/**
	 * Les zones eclairees par l'ampoule
	 */
	private ArrayList<Area> zoneEclaire;

	/**
	 * Razon de l'eclairage de l'ampoule (aka nombre de case eclaire)
	 */
	private int rayonDeclairage;

	private DrawableRectangle drawable;
	private ControllableRoundSource source;

	/**
	 * Constructor of the light bulb
	 * 
	 * @param amas   The AMAS the light bulb belongs to
	 * @param startX The x coordinate of the light bulb
	 * @param startY The y coordinate of the light bulb
	 */
	public Ampoule(DrAmas amas, int x, int y) {
		super(amas, x, y);

		this.rayonDeclairage = 15;
		//System.out.println(x + "," + y);
		//System.out.println(getAmas().getEnvironment().getAreaByPosition(x, y));
		this.source = new ControllableRoundSource(getAmas().getEnvironment().getAreaByPosition(x, y), rayonDeclairage,
				getAmas().getEnvironment());
		this.zoneEclaire = new ArrayList<Area>();
	}

	@Override
	protected void onInitialization() {

		dx = (int) params[0];
		dy = (int) params[1];
	}

	@Override
	protected void onRenderingInitialization() {
		drawable = VUI.get().createRectangle(dx * 10, dy * 10, 10, 10);
		drawable.setLayer(1);
		drawable.setColor(Color.WHITE);
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

	public void setLuminosity(double luminosity) {
		this.source.setLuminosity(luminosity);
	}

	/**
	 * Agent action phase. Eclaire les zones
	 */
	@Override
	protected void onAct() {
		this.source.cycle();
		// this.zoneEclaire.addAll(getAmas().getEnvironment().getAreaAround(this.dx,
		// this.dy, this.rayonDeclairage));
		/*
		 * for (Area area : this.zoneEclaire) { area.addLuminosity(this,
		 * this.luminosite); //System.out.println(area.getLuminosity()); }
		 */

	}

	@Override
	protected void onUpdateRender() {

	}

	@Override
	protected void onDecide() {
		this.setLuminosity(0.2);
		this.zoneEclaire.addAll(getAmas().getEnvironment().getAreaAround(this.dx,this.dy, this.rayonDeclairage));
		int sum = 0;
		int nbZone = 0;
		for (Area area : this.zoneEclaire) {
			sum += area.getLuminosity();
			nbZone++;
		}
		sum /= nbZone;
		if (sum < 0.5) {
			this.source.setLuminosity(this.source.);
		}else {
			this.source.setLuminosity;
		}
	}
}