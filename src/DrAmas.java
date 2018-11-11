import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Random;

import fr.irit.smac.amak.Agent;
import fr.irit.smac.amak.Amas;
import fr.irit.smac.amak.Scheduling;
import fr.irit.smac.amak.ui.MainWindow;
import fr.irit.smac.amak.ui.VUI;
import fr.irit.smac.lxplot.LxPlot;

/**
 * This class represents the AMAS
 *
 */
public class DrAmas extends Amas<Salle> {

	private ArrayList<Ampoule> ampoules;
	
	public final static int NBAMPOULE = 90;
	/**
	 * Queue used to compute the sliding window
	 */
	private LinkedList<Double> lastSums = new LinkedList<>();

	/**
	 * Constructor
	 * 
	 * @param env
	 *            The environment of the AMAS
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
		int x =1;
		int y =1;
		for(int i = 0 ; i < NBAMPOULE;i++) {
			if (x>(100/NBAMPOULE)*NBAMPOULE) {
				x=1;
				y+=14;
			}
			this.ampoules.add(new Ampoule(this, x, y));
			x+=14;
		}

	}

	/**
	 * Launch the system
	 * 
	 * @param args
	 *            Arguments of the problem (not used)
	 */
	public static void main(String[] args) {

		VUI.get().setDefaultView(100, -400, -300);
		DrAmas drAmas = new DrAmas(new Salle());

		new SalleViewer(drAmas);
		/*MainWindow.addMenuItem("Remove 10 drones", l -> {
			for (int i = 0; i < 10; i++) {
				drAmas.getAgents().get(drAmas.getEnvironment().getRandom().nextInt(drAmas.getAgents().size()))
						.destroy();
			}
		});
		/*MainWindow.addMenuItem("Add 10 drones", l -> {
			for (int i = 0; i < 10; i++) {

				new Drone(drAmas, drAmas.getEnvironment().getRandom().nextInt(World.WIDTH),
						drAmas.getEnvironment().getRandom().nextInt(World.HEIGHT));
			}
		});*/

	}

	/**
	 * At the end of each system cycle, compute the sum and average of area
	 * criticalities and display them
	 */
	@Override
	protected void onSystemCycleEnd() {
		double min = 1;
		double sum = 0;
		for (int x = 0; x < getEnvironment().getAreas()[0].length; x++) {
			for (int y = 0; y < getEnvironment().getAreas().length; y++) {
				double lum = getEnvironment().getAreaByPosition(x, y).getLuminosity();
				sum += lum;
				if (lum < min)
					min = lum;
			}
		}

		lastSums.add(sum);
		if (lastSums.size() > 10000)
			lastSums.poll();

		LxPlot.getChart("Area criticalities").add("Sum", getCycle() % 1000, sum);
		LxPlot.getChart("Area criticalities").add("Sliding average", getCycle() % 1000, average(lastSums));
	}

	/**
	 * Compute the average of a list
	 * 
	 * @param lastSums2
	 *            List on which computing the average
	 * @return the average
	 */
	private double average(LinkedList<Double> lastSums2) {
		OptionalDouble average = lastSums2.stream().mapToDouble(a -> a).average();
		return average.getAsDouble();
	}

	/**
	 * Get agents presents in a specified area
	 * 
	 * @param areaByPosition
	 *            The specified area
	 * @return the list of drones in this area
	 */
	public Agent[] getAgentsInArea(Area areaByPosition) {
		List<Agent> res = new ArrayList<>();
		for (Agent<?, Salle> agent : agents) {
			/*if (((Agent) agent).getCurrentArea() == areaByPosition)
				res.add((Agent) agent);*/
		}
		return res.toArray(new Agent[0]);
	}
}