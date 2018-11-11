package ressources.source;
import ressources.Area;
import env.Salle;


public class Door extends ConeSource {
	
	public Door(Area position, Salle env) {
		super(position, 100, env, 0.4);
	}
	@Override
	protected boolean getInverse() {
		return true;
	}
}
