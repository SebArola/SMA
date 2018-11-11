package ressources.source;
import java.util.List;
import ressources.Area;
import env.Salle;

public abstract class Source {

	protected Area position;
	protected List<Area> area;
	protected int maxDistance;
	protected Salle env;

	public Source(Area position, int maxDistance, Salle env) {
		this.position = position;
		this.maxDistance = maxDistance;
		this.env = env;
		this.area = this.getAffectedArea();
	}
	
	
	
	protected abstract List<Area> getAffectedArea();
	public abstract double getLuminosity();

	public void cycle() {
		
		double l = this.getLuminosity();
		
		for (Area area : this.area) {

			double distance = Math.hypot(position.getX() - area.getX(), position.getY() - area.getY());
			double ratioDistance = 1 - (distance / maxDistance);
			if (distance > maxDistance) {
				ratioDistance = 0;
			}
			

			/*
			 * assert (ratioDistance >= 0); assert (ratioDistance <= 1); assert (noisedL >=
			 * 0); assert (noisedL <= 1);
			 */

			// System.out.println("lum : " + luminosity + " ratioDistance " + ratioDistance
			// + " noised " + noisedL
			// + " distance " + distance);

			// System.out.println(ratioDistance * noisedL);

			area.addLuminosity(this, ratioDistance * l);
		}
	}
}
