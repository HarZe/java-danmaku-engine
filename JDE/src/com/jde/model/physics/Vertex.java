package com.jde.model.physics;

/**
 * This Vertex class implements a 2D vertex
 * 
 * @author HarZe (David Serrano)
 */
public class Vertex {

	/** X-axis coordinate */
	protected double x;

	/** Y-axis coordinate */
	protected double y;

	/**
	 * @param a
	 *            Angle in radians
	 * @return A unit vector rotated "a" radians: [sin(a), cos(a)]
	 */
	public static Vertex angleToVertex(double a) {
		return new Vertex(Math.cos(a), Math.sin(a));
	}

	/**
	 * Basic constructor, returns the origin vertex: [0, 0]
	 */
	public Vertex() {
		x = y = 0;
	}

	/**
	 * Parametric constructor, returns the vertex [x, y]
	 * 
	 * @param x
	 *            X-axis coordinate
	 * @param y
	 *            Y-axis coordinate
	 */
	public Vertex(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Point-to-point vertex constructor, returns the vertex that goes from
	 * "start" to "end"
	 * 
	 * @param start
	 *            Origin vertex point
	 * @param end
	 *            Destination vertex point
	 */
	public Vertex(Vertex start, Vertex end) {
		x = end.x - start.x;
		y = end.y - start.y;
	}

	/**
	 * @param v
	 *            Vertex to add
	 * @return A reference to itself (calling this method modifies the vertex)
	 */
	public Vertex add(Vertex v) {
		x += v.x;
		y += v.y;
		return this;
	}

	/**
	 * @param a
	 *            First vertex
	 * @param b
	 *            Second vertex
	 * @return A reference to a new Vertex, equal to a.add(b) or b.add(a)
	 */
	public static Vertex add(Vertex a, Vertex b) {
		return new Vertex(a.x + b.x, a.y + b.y);
	}

	/**
	 * @return The rotation angle of the vertex (over the X-axis)
	 */
	public double angle() {
		Vertex uv = unit();
		double acos = Math.acos(uv.x);
		double asin = Math.asin(uv.y);

		if (asin >= 0)
			return Math.toDegrees(acos);
		else
			return 360 - Math.toDegrees(acos);
	}

	/**
	 * Returns a new copy of the vertex
	 */
	public Vertex clone() {
		return new Vertex(x, y);
	}

	/**
	 * @param v
	 *            Second vertex of the cross product
	 * @return The cross product of this vertex with "v", please note that order
	 *         is: this x v
	 */
	public double crossProduct(Vertex v) {
		return x * v.y - y * v.x;
	}

	/**
	 * @param v
	 *            Vertex point to compare with
	 * @return The distance between this vertex and "v"
	 */
	public double distanceTo(Vertex v) {
		return Math.sqrt((v.x - x) * (v.x - x) + (v.y - y) * (v.y - y));
	}

	/**
	 * @return The X-axis coordinate of the vertex
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return The Y-axis coordinate of the vertex
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return The length of the vertex, also the distance to the origin
	 */
	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * @return Returns the left perpendicular vertex: [-y, x]
	 */
	public Vertex normal() {
		return new Vertex(-y, x);
	}

	/**
	 * @return Returns the opposite vertex: [-x, -y]
	 */
	public Vertex opposite() {
		return new Vertex(-x, -y);
	}

	/**
	 * Resizes the vertex to match the given length
	 * 
	 * @param s
	 *            The new length for the vertex
	 * @return A reference to the calling vertex
	 */
	public Vertex resize(double s) {
		double lenght = length();
		x *= s / lenght;
		y *= s / lenght;
		return this;
	}

	/**
	 * Rotates the calling vertex the given angle
	 * 
	 * @param a
	 *            Angle in radians
	 * @return A reference to the calling vertex
	 */
	public Vertex rotate(double a) {
		double px = x;
		x = px * Math.cos(a) - y * Math.sin(a);
		y = px * Math.sin(a) + y * Math.cos(a);
		return this;
	}

	/**
	 * @param v
	 *            Second vertex
	 * @return The scalar product of this vertex with the second one
	 */
	public double scalar(Vertex v) {
		return v.x * x + v.y * y;
	}

	/**
	 * Scales the vector (for ex. scale(3) of [2,2] results in [6,6])
	 * 
	 * @param s
	 *            Scale factor
	 * @return A reference to the calling vertex
	 */
	public Vertex scale(double s) {
		x *= s;
		y *= s;
		return this;
	}

	/**
	 * Sets a new value for the X-axis coordinate
	 * 
	 * @param x
	 *            New "x" value
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Sets a new value for the Y-axis coordinate
	 * 
	 * @param y
	 *            New "y" value
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Subtracts the given vertex to the calling vertex (result is "this - v")
	 * 
	 * @param v
	 *            Vertex to subtract
	 * @return A reference to itself (calling this method modifies the vertex)
	 */
	public Vertex sub(Vertex v) {
		x -= v.x;
		y -= v.y;
		return this;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	/**
	 * @return A new vertex, with length = 1, and the same orientation that the
	 *         calling vertex
	 */
	public Vertex unit() {
		double lenght = length();
		return new Vertex(x / lenght, y / lenght);
	}
}
