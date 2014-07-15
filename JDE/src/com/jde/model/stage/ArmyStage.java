package com.jde.model.stage;

import java.util.ArrayList;

import com.jde.model.entity.bullet.Bullet;
import com.jde.model.entity.enemy.Enemy;
import com.jde.model.entity.spawning.Spawner;

public class ArmyStage implements Stage {

	protected ArrayList<Enemy> enemies;
	protected ArrayList<Bullet> bullets;

	protected Spawner<Enemy> spawner;
	
	protected double elapsed = 0;

	public ArmyStage(ArrayList<Enemy> enemiesSpawn) {
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<Bullet>();

		spawner = new Spawner<Enemy>(0);
		spawner.addSpawnables(enemiesSpawn);
	}

	@Override
	public void draw() {
		for (Enemy e : enemies)
			e.draw();
		
		for (Bullet b : bullets)
			b.draw();
		
		// DEBUG
		/*System.out.println("************** NEW FRAME **************");
		for (Enemy e : enemies)
			System.out.println(e);
		
		for (Bullet b : bullets)
			System.out.println(b);*/
	}

	@Override
	public void forward(double ms) {
		elapsed += ms;
		
		// Forward current bullets
		for (Bullet b : bullets)
			b.forward(ms);

		// Forward current enemies
		for (Enemy e : enemies)
			// Spawn new bullets if shot
			for (Bullet b : e.forwardAndBullets(ms)) {
				b.spawn(elapsed);
				bullets.add(b);
			}
				
		// Spawn new enemies
		for (Enemy e : spawner.forward(ms)) {
			enemies.add(e);
			
			// Spawn bullets from them
			for (Bullet b : e.spawnAndBullets(elapsed)) {
				b.spawn(elapsed);
				bullets.add(b);
			}
		}
		
		// TODO: delete out-of-area bullets and enemies
	}

	@Override
	public boolean isFinished() {
		return false;
	}

}
