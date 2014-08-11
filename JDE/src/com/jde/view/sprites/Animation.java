package com.jde.view.sprites;

import java.util.ArrayList;

public class Animation {

	protected ArrayList<Sprite> sprites;
	protected ArrayList<Double> durations;
	protected double elapsed = 0;
	protected int current = 0;
	protected int loopFrom = 0;
	
	public Animation(ArrayList<Sprite> sprites, ArrayList<Double> durations) {
		this.sprites = sprites;
		this.durations = durations;
		this.loopFrom = sprites.size() - 1;
	}

	public int getLoopFrom() {
		return loopFrom;
	}

	public void setLoopFrom(int loopFrom) {
		if (loopFrom < sprites.size() - 1)
			this.loopFrom = loopFrom;
	}

	public void forward(double ms) {
		elapsed += ms;
		
		while (current < sprites.size() && durations.get(current) < elapsed)
			current++;
		
		if (current == sprites.size()) {
			elapsed -= durations.get(current - 1) - durations.get(loopFrom);
			current = loopFrom;
		}
	}
	
	public void draw() {
		sprites.get(current).draw();
	}
	
	public void reset() {
		current = 0;
		elapsed = 0;
	}
	
	public Animation clone() {
		Animation a = new Animation(sprites, durations);
		a.setLoopFrom(loopFrom);
		return a;
	}
	
}
