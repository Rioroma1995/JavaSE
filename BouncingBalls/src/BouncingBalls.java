import ua.princeton.lib.StdDraw;

public class BouncingBalls {
	public static void main(String[] args) {
		//int n = Integer.parseInt(args[0]);
		int n = 100;
		Ball[] balls = new Ball[n];
		for (int i = 0; i < n; i++)
			balls[i] = new Ball();
		while (true) {
			StdDraw.clear();
			StdDraw.line(0, 0, 1, 0);
			StdDraw.line(0, 0, 0, 1);
			StdDraw.line(1, 0, 1, 1);
			StdDraw.line(0, 1, 1, 1);
			for (int i = 0; i < n; i++) {               
				balls[i].move(0.5);
				balls[i].draw();
			}
			StdDraw.show(50);
		}
	}
}
