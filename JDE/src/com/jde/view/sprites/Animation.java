package com.jde.view.sprites;

import java.util.ArrayList;

public class Animation {

	protected ArrayList<Sprite> sprites;
	protected ArrayList<Double> durations;
	protected double elapsed = 0;
	protected int current = 0;
	protected boolean repeat = false;
	
	public Animation(ArrayList<Sprite> sprites, ArrayList<Double> durations) {
		this.sprites = sprites;
		this.durations = durations;
	}
	
	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public void forward(double ms) {
		elapsed += ms;
		
		while (current < sprites.size() && durations.get(current) < elapsed)
			current++;
		
		if (current == sprites.size())
			if (repeat) {
				elapsed -= durations.get(current - 1);
				current = 0;
			}
			else
				current--;
	}
	
	public void draw() {
		sprites.get(current).draw();
	}
	
	public Animation clone() {
		Animation a = new Animation(sprites, durations);
		a.setRepeat(repeat);
		return a;
	}
	
}
