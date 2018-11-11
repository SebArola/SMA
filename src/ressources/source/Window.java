package ressources.source;
import env.Salle;
import ressources.Area;

public class Window extends ConeSource {
	
	public Window(Area position, Salle env) {
		super(position, 55, env, 0.8);
	}

	@Override
	protected boolean getInverse() {
		return false;
	}

}
