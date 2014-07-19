package com.jde.model.physics;

import java.util.LinkedList;

public class Movement {

	protected Vertex position;
	protected Direction direction;
	
	protected LinkedList<Direction> directions;
	protected double elapsed = 0;

	public Movement(Vertex position, LinkedList<Direction> directions) {
		this.position = position;
		this.directions = new LinkedList<Direction>();
		
		for (Direction d : directions)
			this.directions.add(d.clone());
		
		direction = this.directions.peek();
	}

	public Vertex getPosition() {
		return position;
	}

	public void setPosition(Vertex position) {
		this.position = position;
	}

	public Direction getDirection() {
		return direction;
	}

	public LinkedList<Direction> getDirections() {
		return directions;
	}

	public void forward(double ms) {
		elapsed += ms;
		
		if (directions.size() > 1 && direction.getDuration() > 0 && direction.getDuration() < elapsed) {
			elapsed -= direction.getDuration();
			forwardAux(ms - elapsed);
			directions.poll();
			direction = directions.peek();
			ms = elapsed;
		}
		
		forwardAux(ms);
	}
	
	private void forwardAux(double ms) {
		direction.setSpeed(direction.getSpeed() + direction.getAcceleration() * ms * 0.001);
		
		if (direction.isHoming())
			direction.setAngle(Vertex.vertexToAngle(direction.getHomingPosition().clone().sub(position)));
		else if (direction.getDuration() > 0)
			direction.setAngle(direction.getAngle() + ((direction.getAngleEnd() - direction.getAngleStart())*ms / direction.getDuration()));
		
		Vertex step = Vertex.angleToVertex(Math.toRadians(direction.getAngle() - 90));
		step.scale(direction.getSpeed() * ms * 0.001);
		position.add(step);
	}

	public Movement clone() {
		return new Movement(position.clone(), directions);
	}

	public String toString() {
		return "Movement. Pos:" + position + "\n" + direction;
	}
}
