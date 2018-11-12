package controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;

import agents.Ampoule;
import agents.Eleve;
import env.Salle;
import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Scheduling;
import fr.irit.smac.amak.ui.MainWindow;
import fr.irit.smac.amak.ui.VUI;
import fr.irit.smac.lxplot.LxPlot;
import ressources.Area;
import ressources.source.Door;

/**
 * This class represents the AMAS
 *
 */
public class DrAmas extends Amas<Salle> {

	private ArrayList<Ampoule> ampoules;
	private ArrayList<Eleve> eleves;

	public final static int NBAMPOULE = 16;
	//public final static int NBAMPOULE = 1;
	public final static int NBELEVES = 30;
	private int totalSum = 0;

	/**
	 * Queue used to compute the sliding window
	 */
	private LinkedList<Double> lastSums = new LinkedList<>();

	/**
	 * Constructor
	 * 
	 * @param env The environment of the AMAS
	 */
	public DrAmas(Salle env) {
		// Set the environment and use manual scheduling
		super(env, Scheduling.DEFAULT);
	}

	/**
	 * Create the agents at random positions
	 */
	@Override
	protected void onInitialAgentsCreation() {

		this.ampoules = new ArrayList<Ampoule>();
		this.eleves = new ArrayList<Eleve>();

		int x = 12;
		int y = 12;
		for (int i = 0; i < NBAMPOULE; i++) {
			if (x > (100 / NBAMPOULE) * NBAMPOULE) {
				x = 12;
				y += 25;
			}
			//System.out.println("new ampoule " + x + "," + y);
			this.ampoules.add(new Ampoule(this, x, y));
			x += 25;
		}

		// this.ampoules.add(new Ampoule(this, 10, 50));
		
		for (int i = 0; i < 3; i += 1) {
			for (int j = 0; j < NBELEVES/6; j += 1) {
				Door departure = environment.getDoors()[new Random().nextInt(environment.getDoors().length)];

				this.eleves.add(new Eleve(this, departure.getX(), departure.getY(),
						environment.getAreaByPosition(10 + 8 * j, 30 + 20 * i)));
			}
			
		}
		
		for (int i = 0; i < 3; i += 1) {
			for (int j = 0; j < NBELEVES/6; j += 1) {
				Door departure = environment.getDoors()[new Random().nextInt(environment.getDoors().length)];

				this.eleves.add(new Eleve(this, departure.getX(), departure.getY(),
						environment.getAreaByPosition(50 + 8 * j, 30 + 20 * i)));
			}
			
		}
		
		
		/*for (int i = 0; i < NBELEVES; i += 1) {

			Door departure = environment.getDoors()[new Random().nextInt(environment.getDoors().length)];
			int j = i%10;

			this.eleves.add(new Eleve(this, departure.getX(), departure.getY(),
					environment.getAreaByPosition(10 + 8 * j, 30 + 7 * j)));
		}*/

	}

	/**
	 * Launch the system
	 * 
	 * @param args Arguments of the problem (not used)
	 */
	public static void main(String[] args) {

		VUI.get().setDefaultView(100, -400, -300);
		DrAmas drAmas = new DrAmas(new Salle());

		new SalleViewer(drAmas);
		/*
		 * MainWindow.addMenuItem("Remove 10 drones", l -> { for (int i = 0; i < 10;
		 * i++) {
		 * drAmas.getAgents().get(drAmas.getEnvironment().getRandom().nextInt(drAmas.
		 * getAgents().size())) .destroy(); } });
		 * /*MainWindow.addMenuItem("Add 10 drones", l -> { for (int i = 0; i < 10; i++)
		 * {
		 * 
		 * new Drone(drAmas, drAmas.getEnvironment().getRandom().nextInt(World.WIDTH),
		 * drAmas.getEnvironment().getRandom().nextInt(World.HEIGHT)); } });
		 */

	}

	/**
	 * At the end of each system cycle, compute the sum and average of area
	 * criticalities and display them
	 */
	@Override
	protected void onSystemCycleEnd() {
		double min = 1;
		double sum = 0;
		int nbCase = 0;
		int countOk = 0;
		Salle env = getEnvironment();
		for (int x = 0; x < env.getAreas()[0].length; x++) {
			for (int y = 0; y < env.getAreas().length; y++) {
				double lum = env.getAreaByPosition(x, y).getLuminosity();
				if (lum >= 0.5) {
					countOk += 1;
				}
				sum += lum;
				nbCase++;
				if (lum < min)
					min = lum;
			}
		}

		this.totalSum += sum;

		LxPlot.getChart("Luminosite").add("Lum moyenne", getCycle() % 1000, sum / nbCase);

		// LxPlot.getChart("Consommation").add("Totale", getCycle() % 1000, totalSum);
		LxPlot.getChart("Consommation").add("Instantannée", getCycle() % 1000, sum);

		sum = 0;
		for (Ampoule a : this.ampoules) {
			sum += a.getLuminosity();
		}

		lastSums.add(sum);
		if (lastSums.size() > 10000)
			lastSums.poll();

//		LxPlot.getChart("Luminosite").add("Lum total", getCycle() % 1000, sum);
//		LxPlot.getChart("Luminosite").add("Lum total moyenne", getCycle() % 1000, average(lastSums));

		LxPlot.getChart("Luminosite").add("Lum ambiante", getCycle() % 1000, env.getAmbiantLuminosity());
		LxPlot.getChart("Luminosite").add("Lum ampoules moyenne", getCycle() % 1000, sum / this.ampoules.size());

		LxPlot.getChart("Area").add("Light >= 0.5", getCycle() % 1000, countOk);
		LxPlot.getChart("Area").add("Light < 0.5", getCycle() % 1000, env.HEIGHT * env.WIDTH - countOk);

	}

	/**
	 * Compute the average of a list
	 * 
	 * @param lastSums2 List on which computing the average
	 * @return the average
	 */
	private double average(LinkedList<Double> lastSums2) {
		OptionalDouble average = lastSums2.stream().mapToDouble(a -> a).average();
		return average.getAsDouble();
	}

	/**
	 * Get agents presents in a specified area
	 * 
	 * @param areaByPosition The specified area
	 * @return the list of drones in this area
	 */
	public Agent[] getAgentsInArea(Area areaByPosition) {
		List<Agent> res = new ArrayList<>();
		for (Agent<?, Salle> agent : agents) {
			/*
			 * if (((Agent) agent).getCurrentArea() == areaByPosition) res.add((Agent)
			 * agent);
			 */
		}
		return res.toArray(new Agent[0]);
	}
}