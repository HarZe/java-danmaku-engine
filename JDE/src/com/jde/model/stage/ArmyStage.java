package com.jde.model.stage;

import java.util.ArrayList;

import com.jde.model.entity.bullet.Bullet;
import com.jde.model.entity.enemy.Enemy;
import com.jde.model.entity.player.Player;
import com.jde.model.entity.spawning.Spawner;
import com.jde.model.physics.Vertex;
import com.jde.model.physics.collision.HitBox;
import com.jde.model.physics.collision.HitZone;
import com.jde.model.utils.Parser;

public class ArmyStage implements Stage {

	protected ArrayList<Enemy> enemies;
	protected ArrayList<Bullet> bullets;

	protected Spawner<Enemy> spawner;
	protected HitZone gameZone;
	
	protected Player player;
	
	protected double elapsed = 0;

	public ArmyStage(ArrayList<Enemy> enemiesSpawn, Player player) {
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<Bullet>();

		spawner = new Spawner<Enemy>(0);
		spawner.addSpawnables(enemiesSpawn);
		
		gameZone = new HitBox(new Vertex(223,240), new Vertex(384 + 50, 448 + 50));
		
		this.player = player;
	}

	@Override
	public void draw() {
		
		player.draw();
		
		for (Enemy e : enemies)
			e.draw();
		
		for (Bullet b : bullets)
			b.draw();
		
		player.drawFocus();
		
		// DEBUG
		/*System.out.println("************** NEW FRAME **************");
		for (Enemy e : enemies)
			System.out.println(e);
		
		for (Bullet b : bullets)
			System.out.println(b);*/
	}

	@Override
	public void forward(double ms) {
		boolean collision = false;
		
		elapsed += ms;
		
		ArrayList<Bullet> toDeleteBullets = new ArrayList<Bullet>();
		ArrayList<Enemy> toDeleteEnemies = new ArrayList<Enemy>();
		
		// Forward current bullets
		for (Bullet b : bullets) {
			collision = collision || b.collides(player.getMovement(), ms);
			b.forward(ms);
			if (!gameZone.isInside(null, b.getMovement().getPosition()))
				toDeleteBullets.add(b);
		}

		// Forward current enemies
		for (Enemy e : enemies) {
			// TODO: enemies collision
			
			// Spawn new bullets if shot
			for (Bullet b : e.forwardAndBullets(ms)) {
				b.spawn(elapsed);
				bullets.add(b);
				collision = collision || b.collides(player.getMovement(), ms);
			}
		
			if (!gameZone.isInside(null, e.getMovement().getPosition()))
				toDeleteEnemies.add(e);
		}
				
		// Spawn new enemies
		for (Enemy e : spawner.forward(ms)) {
			enemies.add(e);
			
			// Spawn bullets from them
			for (Bullet b : e.spawnAndBullets(elapsed)) {
				b.spawn(elapsed);
				bullets.add(b);
				collision = collision || b.collides(player.getMovement(), ms);
			}
		}
		
		for (Bullet b : toDeleteBullets)
			bullets.remove(b);
		
		for (Enemy e : toDeleteEnemies)
			enemies.remove(e);
		
		player.update(ms);
		
		if (collision) {
			System.out.println("Collision");
			player.getMovement().setPosition(Parser.unvirtualizeCoordinates(new Vertex(0, 100)));
		}
	}

	@Override
	public boolean isFinished() {
		return false;
	}

}
