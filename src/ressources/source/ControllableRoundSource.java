package ressources.source;

import java.util.List;

import env.Salle;
import ressources.Area;

public class ControllableRoundSource extends Source {

	private double luminosity;

	public ControllableRoundSource(Area position, int maxDistance, Salle env) {
		super(position, maxDistance, env);
	}

	protected List<Area> getAffectedArea() {
		//System.out.println(position);
		//System.out.println("new ampoule");
		for(Area a: env.getAreaAround(position.getX(), position.getY(), this.maxDistance)) {
			//System.out.println(a);
		}
		return env.getAreaAround(position.getX(), position.getY(), this.maxDistance);
	}

	@Override
	public double getLuminosity() {
		return luminosity;
	}

	public void setLuminosity(double luminosity) {
		this.luminosity = luminosity;
	}

}
