package com.jde.model.physics;

import java.util.ArrayList;

public class Movement {

	protected Vertex position;
	protected Direction direction;
	
	protected ArrayList<Direction> directions;
	protected int currentDir = 0;
	protected double elapsed = 0;

	public Movement(Vertex position, ArrayList<Direction> directions) {
		this.position = position;
		this.directions = new ArrayList<Direction>();
		
		for (Direction d : directions)
			this.directions.add(d.clone());
		
		direction = this.directions.get(currentDir);
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

	public ArrayList<Direction> getDirections() {
		return directions;
	}

	public void forward(double ms) {
		elapsed += ms;
		
		if (currentDir + 1 < directions.size() && direction.getDuration() > 0 && direction.getDuration() < elapsed) {
			elapsed -= direction.getDuration();
			forwardAux(ms - elapsed);
			currentDir++;
			
			Direction newDir = directions.get(currentDir);
			
			newDir.setAngle(newDir.getAngle() + direction.getAngle());
			newDir.setRotation(newDir.getRotation() + direction.getRotation());
			newDir.setMotion(newDir.getMotion() + direction.getMotion());
			newDir.setSpeed(newDir.getSpeed() + direction.getSpeed());
			newDir.setAcceleration(newDir.getAcceleration() + direction.getAcceleration());
			
			direction = newDir;
			
			ms = elapsed;
		}
		
		forwardAux(ms);
	}
	
	private void forwardAux(double ms) {
		direction.setSpeed(direction.getSpeed() + direction.getAcceleration() * ms * 0.001);
		direction.setRotation(direction.getRotation() + direction.getMotion() * ms * 0.001);
		
		if (direction.isHoming())
			direction.setAngle(Vertex.vertexToAngle(direction.getHomingPosition().clone().sub(position)));
		else if (direction.getDuration() > 0)
			direction.setAngle(direction.getAngle() + (direction.getRotation() * ms * 0.001));
		
		Vertex step = Vertex.angleToVertex(Math.toRadians(direction.getAngle()));
		step.scale(direction.getSpeed() * ms * 0.001);
		position.add(step);
	}
	
	public void applyRandomization() {
		for (Direction d : directions)
			d.applyRandomitazion();
	}

	public Movement clone() {
		return new Movement(position.clone(), directions);
	}

	public String toString() {
		return "Movement. Pos:" + position + "\n" + direction;
	}
}
