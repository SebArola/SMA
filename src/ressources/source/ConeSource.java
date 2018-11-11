package ressources.source;

import java.util.List;
import java.util.Random;

import env.Salle;
import ressources.Area;

public abstract class ConeSource extends Source {
	private double ratio;

	public ConeSource(Area position, int maxDistance, Salle env, double ratio) {
		super(position, maxDistance, env);
		this.ratio = ratio;
	}

	private double getNoise() {
		Random r = new Random();
		double noise;
		if (r.nextBoolean()) {
			noise = r.nextDouble() / 10;
		} else {
			noise = -r.nextDouble() / 10;
		}
		return noise;
	}

	private double applyNoise(double value) {
		return Math.max(0, Math.min(value + getNoise(), 1));
	}

	@Override
	public double getLuminosity() {
		return this.applyNoise(env.getAmbiantLuminosity() * this.ratio);
	}

	protected List<Area> getAffectedArea() {
		return env.getAreaByCone(this.position.getX(), this.position.getY(), this.maxDistance, this.getInverse());
	}

	protected abstract boolean getInverse();

}
