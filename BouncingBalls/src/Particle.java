import java.awt.Color;

import ua.princeton.lib.StdDraw;
import ua.princeton.lib.StdRandom;

public class Particle {
	private double rx, ry; // position
	private double vx, vy; // velocity
	private final double radius; // radius
	private final double mass; // mass
	private int count; // number of collisions
	
	public Particle() {
		radius = StdRandom.uniform(0.015, 0.03);
		mass = StdRandom.uniform(0.05, 1);
		rx = StdRandom.uniform(0.01+radius, 1-radius);
		ry = StdRandom.uniform(0.01+radius, 1-radius);
		vx = StdRandom.uniform(-0.02, 0.02);
		vy = StdRandom.uniform(-0.02, 0.02);
	}

	public void move(double dt) {
		rx = rx + vx * dt;
		ry = ry + vy * dt;
	}

	public void draw() {
		StdDraw.setPenColor(setColor(count));
		StdDraw.filledCircle(rx, ry, radius);
		StdDraw.setPenColor(Color.BLACK);
	}

	public double timeToHit(Particle that) {
		if (this == that)
			return Double.POSITIVE_INFINITY;
		double dx = that.rx - this.rx, dy = that.ry - this.ry;
		double dvx = that.vx - this.vx, dvy = that.vy - this.vy;
		double dvdr = dx * dvx + dy * dvy;
		if (dvdr > 0)
			return Double.POSITIVE_INFINITY;
		double dvdv = dvx * dvx + dvy * dvy;
		double drdr = dx * dx + dy * dy;
		double sigma = this.radius + that.radius;
		double d = (dvdr * dvdr) - dvdv * (drdr - sigma * sigma);
		if (d < 0)
			return Double.POSITIVE_INFINITY;
		return -(dvdr + Math.sqrt(d)) / dvdv;
	}
    public double timeToHitVerticalWall() {
        if (vx > 0) 
        	return (1.0 - rx - radius) / vx;
        else if (vx < 0) 
        	return (rx - radius) / -vx;  
        else             
        	return Double.POSITIVE_INFINITY;
    }

    public double timeToHitHorizontalWall() {
        if (vy > 0) 
        	return (1.0 - ry - radius) / vy;
        else if (vy < 0) 
        	return (ry - radius) / -vy;
        else             
        	return Double.POSITIVE_INFINITY;
    }
	public void bounceOff(Particle that) {
		double dx = that.rx - this.rx, dy = that.ry - this.ry;
		double dvx = that.vx - this.vx, dvy = that.vy - this.vy;
		double dvdr = dx * dvx + dy * dvy;
		double dist = this.radius + that.radius;
		double J = 2 * this.mass * that.mass * dvdr
				/ ((this.mass + that.mass) * dist);
		double Jx = J * dx / dist;
		double Jy = J * dy / dist;
		this.vx += Jx / this.mass;
		this.vy += Jy / this.mass;
		that.vx -= Jx / that.mass;
		that.vy -= Jy / that.mass;
		this.count++;
		that.count++;
	}

	public void bounceOffVerticalWall() {
		vx = -vx;
		this.count++;
	}

	public void bounceOffHorizontalWall() {
		vy = -vy;
		this.count++;
	}
	public int getCount(){
		return count;		
	}
	private Color setColor(int n){
		n%=12;
		if (n == 0)
			return Color.BLACK;
		else if (n == 1)
			return  Color.BLUE;
		else if (n == 2)
			return  Color.CYAN;
		else if (n == 3)
			return  Color.DARK_GRAY;
		else if (n == 4)
			return  Color.GRAY;
		else if (n == 5)
			return  Color.GREEN;
		else if (n == 6)
			return  Color.LIGHT_GRAY;
		else if (n == 7)
			return  Color.MAGENTA;
		else if (n == 8)
			return  Color.ORANGE;
		else if (n == 9)
			return  Color.PINK;
		else if (n == 10)
			return  Color.RED;
		else
			return  Color.YELLOW;
	}
}
