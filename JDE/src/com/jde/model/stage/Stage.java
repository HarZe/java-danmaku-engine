package com.jde.model.stage;


public interface Stage {
	
	public void draw();
	
	public void forward(double ms);
	
	public boolean isFinished();

}
