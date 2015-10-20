import ua.princeton.lib.StdDraw;
import ua.princeton.lib.StdRandom;

public class Ball {
	private double rx, ry; // position
	private double vx, vy; // velocity
	private final double radius; // radius
	
	public Ball() {
		radius = StdRandom.uniform(0.015, 0.025);	
		rx = StdRandom.uniform(0.01+radius, 1-radius);
		ry = StdRandom.uniform(0.01+radius, 1-radius);
		vx = StdRandom.uniform(-0.02, 0.02);
		vy = StdRandom.uniform(-0.02, 0.02);
	}

	public void move(double dt) {
		if ((rx + vx * dt < radius) || (rx + vx * dt > 1.0 - radius)) 
			vx = -vx;
		if ((ry + vy * dt < radius) || (ry + vy * dt > 1.0 - radius)) 
			vy = -vy;
		rx = rx + vx * dt;
		ry = ry + vy * dt;
	}

	public void draw() {
		StdDraw.filledCircle(rx, ry, radius);
	}
}
