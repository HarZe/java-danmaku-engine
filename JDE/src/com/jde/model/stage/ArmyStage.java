package com.jde.model.stage;

import java.util.ArrayList;

import com.jde.model.entity.bullet.Bullet;
import com.jde.model.entity.enemy.Enemy;
import com.jde.model.entity.spawning.Spawner;
import com.jde.model.physics.Vertex;
import com.jde.model.physics.collision.HitBox;
import com.jde.model.physics.collision.HitZone;

public class ArmyStage implements Stage {

	protected ArrayList<Enemy> enemies;
	protected ArrayList<Bullet> bullets;

	protected Spawner<Enemy> spawner;
	protected HitZone gameZone;
	
	protected double elapsed = 0;

	public ArmyStage(ArrayList<Enemy> enemiesSpawn) {
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<Bullet>();

		spawner = new Spawner<Enemy>(0);
		spawner.addSpawnables(enemiesSpawn);
		
		gameZone = new HitBox(new Vertex(223,240), new Vertex(384 + 50, 448 + 50));
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
		
		ArrayList<Bullet> toDeleteBullets = new ArrayList<Bullet>();
		ArrayList<Enemy> toDeleteEnemies = new ArrayList<Enemy>();
		
		// Forward current bullets
		for (Bullet b : bullets) {
			b.forward(ms);
			if (!gameZone.isInside(b.getMovement().getPosition()))
				toDeleteBullets.add(b);
		}

		// Forward current enemies
		for (Enemy e : enemies) {
			// Spawn new bullets if shot
			for (Bullet b : e.forwardAndBullets(ms)) {
				b.spawn(elapsed);
				bullets.add(b);
			}
		
			if (!gameZone.isInside(e.getMovement().getPosition()))
				toDeleteEnemies.add(e);
		}
				
		// Spawn new enemies
		for (Enemy e : spawner.forward(ms)) {
			enemies.add(e);
			
			// Spawn bullets from them
			for (Bullet b : e.spawnAndBullets(elapsed)) {
				b.spawn(elapsed);
				System.out.println(b.spawnTime());
				bullets.add(b);
			}
		}
		
		for (Bullet b : toDeleteBullets)
			bullets.remove(b);
		
		for (Enemy e : toDeleteEnemies)
			enemies.remove(e);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

}
