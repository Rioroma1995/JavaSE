
public class Tester {
	public static void main(String[] args) {
		//int n = Integer.parseInt(args[0]);
		int n = 100;
		Particle[] balls = new Particle[n];
		for (int i = 0; i < n; i++)
			balls[i] = new Particle();
		CollisionSystem cs = new CollisionSystem(balls);
		cs.simulate();
	}
}
