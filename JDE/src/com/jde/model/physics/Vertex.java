package com.jde.model.physics;

public class Vertex {
	
	protected double x;
	protected double y;

	public Vertex() {
		x = y = 0;
	}

	public Vertex(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public Vertex clone() {
		return new Vertex(x,y);
	}
	
	public Vertex add(Vertex v) {
		x += v.getX();
		y += v.getY();
		return this;
	}
	
	public Vertex scale(double s) {
		x *= s;
		y *= s;
		return this;
	}
	
	public Vertex multiply(Vertex v) {
		x *= v.getX();
		y *= v.getY();
		return this;
	}
	
	public Vertex rotate(double a) {
		double px = x;
		x *= px*Math.cos(a) - y*Math.sin(a);
		y *= px*Math.sin(a) + y*Math.cos(a);
		return this;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public static Vertex angle(double a) {
		return new Vertex(Math.cos(a), Math.sin(a));
	}
}
