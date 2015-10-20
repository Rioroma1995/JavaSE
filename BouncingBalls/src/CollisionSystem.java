import ua.princeton.lib.StdDraw;

public class CollisionSystem {
	private MinPQ<Event> pq; // the priority queue
	private double t = 0.0; // simulation clock time
	private Particle[] particles; // the array of particles

	public CollisionSystem(Particle[] particles) {
		this.particles = particles;
	}

	private void predict(Particle a) {
		if (a == null)
			return;
		for (int i = 0; i < particles.length; i++) {
			double dt = a.timeToHit(particles[i]);
			if(dt>0)
				pq.insert(new Event(t + dt, a, particles[i]));
		}
		pq.insert(new Event(t + a.timeToHitVerticalWall(), a, null));
		pq.insert(new Event(t + a.timeToHitHorizontalWall(), null, a));
	}

	private void redraw() {
		StdDraw.clear();
		StdDraw.line(0, 0, 1, 0);
		StdDraw.line(0, 0, 0, 1);
		StdDraw.line(1, 0, 1, 1);
		StdDraw.line(0, 1, 1, 1);
		for (int i = 0; i < particles.length; i++) 
			particles[i].draw();
		StdDraw.show(50);
		pq.insert(new Event(t + 2, null, null));
	}

	public void simulate(){
		pq = new MinPQ<Event>();
		for(int i = 0; i < particles.length; i++) 
			predict(particles[i]);
		pq.insert(new Event(0, null, null));
		while(!pq.isEmpty()){
			Event event = pq.delMin();
			if(!event.isValid()) 
				continue;
			Particle a = event.a;
			Particle b = event.b;
			for(int i = 0; i < particles.length; i++)				
				particles[i].move(event.time - t);
			t = event.time;
			if (a != null && b != null)
				a.bounceOff(b);
			else if (a != null && b == null) 
				a.bounceOffVerticalWall();
			else if (a == null && b != null)
				b.bounceOffHorizontalWall();
			else if (a == null && b == null)
				redraw();
			predict(a);
			predict(b);
		}
	}
	private class Event implements Comparable<Event> {
		private double time; // time of event
		private Particle a, b; // particles involved in event
		private int countA, countB; // collision counts for a and b

		public Event(double t, Particle a, Particle b) {
			time = t;
			this.a = a;
			this.b = b;
			if (a != null)
				countA = a.getCount();
			else           
				countA = -1;
			if (b != null)
				countB = b.getCount();
			else           
				countB = -1;
		}

		public int compareTo(Event that) {
			if (this.time - that.time < 0)
				return -1;
			else if (this.time - that.time > 0)
				return 1;
			else 
				return  0;
		}

		public boolean isValid() {
			if (a != null && a.getCount() != countA) 
				return false;
            if (b != null && b.getCount() != countB) 
            	return false;
			return true;
		}
	}
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
