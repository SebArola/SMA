package ressources.source;
import java.util.List;
import ressources.Area;
import env.Salle;


public class Door extends ConeSource {
	
	public Door(Area position, Salle env) {
		super(position, 35, env, 0.4, true);
	}

}
