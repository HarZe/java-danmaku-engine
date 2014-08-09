package com.jde.model.physics;

public class Vertex {
	
	protected double x;
	protected double y;

	public Vertex() {
		x = y = 0;
	}

	public Vertex(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vertex(Vertex start, Vertex end) {
		x = end.x - start.x;
		y = end.y - start.y;
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
	
	public double lenght() {
		return Math.sqrt(x * x + y * y);
	}
	
	public Vertex add(Vertex v) {
		x += v.x;
		y += v.y;
		return this;
	}
	
	public Vertex sub(Vertex v) {
		x -= v.x;
		y -= v.y;
		return this;
	}
	
	public Vertex scale(double s) {
		x *= s;
		y *= s;
		return this;
	}
	
	public Vertex multiply(Vertex v) {
		x *= v.x;
		y *= v.y;
		return this;
	}
	
	public double scalar(Vertex v) {
		return v.x * x + v.y * y;
	}
	
	public Vertex rotate(double a) {
		double px = x;
		x *= px*Math.cos(a) - y*Math.sin(a);
		y *= px*Math.sin(a) + y*Math.cos(a);
		return this;
	}
	
	public Vertex unit() {
		double lenght = lenght();
		return new Vertex(x / lenght, y / lenght);
	}
	
	public double angle() {
		Vertex uv = unit();
		double acos = Math.acos(uv.x);
		double asin = Math.asin(uv.y);
		
		if (asin >= 0)
			return Math.toDegrees(acos);
		else
			return 360 - Math.toDegrees(acos);
	}
	
	public double distanceTo(Vertex v) {
		return Math.sqrt((v.x - x)*(v.x - x) + (v.y - y)*(v.y - y));
	}
	
	public Vertex normal() {
		return new Vertex(-y, x);
	}
	
	public Vertex opposite() {
		return new Vertex(-x, -y);
	}
	
	public double crossProduct(Vertex v) {
		return x*v.y - y*v.x;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public Vertex clone() {
		return new Vertex(x, y);
	}
	
	public static Vertex angleToVertex(double a) {
		return new Vertex(Math.cos(a), Math.sin(a));
	}
}
