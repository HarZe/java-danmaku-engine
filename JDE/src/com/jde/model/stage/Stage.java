package com.jde.model.stage;


public interface Stage {
	
	public void draw();
	
	public boolean colliding(double ms);
	
	public void forward(double ms);
	
	public boolean isFinished();

}
