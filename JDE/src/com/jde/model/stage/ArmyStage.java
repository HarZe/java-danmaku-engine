package com.jde.model.stage;

import java.util.ArrayList;
import java.util.Iterator;

import com.jde.model.entity.Entity;
import com.jde.model.entity.bullet.Bullet;
import com.jde.model.entity.enemy.Enemy;
import com.jde.model.entity.player.Player;
import com.jde.model.entity.spawning.Spawner;
import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;
import com.jde.model.physics.collision.HitBox;
import com.jde.model.physics.collision.HitZone;
import com.jde.model.utils.Parser;

public class ArmyStage implements Stage {

	protected int cores = Runtime.getRuntime().availableProcessors();
	
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
		
		// Multi-thread limits & checks
		if (cores < 2)
			cores = 2;
		else if (cores > 8)
			cores = 8;
	}

	@Override
	public void draw() {
		
		player.draw();
		
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
	public boolean colliding(double ms) {
		boolean collision = false;
		
		// Multi-thread collision detection
		ArrayList<Collider> colliders = new ArrayList<Collider>();
		int step = bullets.size() / (cores - 1);
		
		for (int i = 0; i < cores - 1; i++) {
			colliders.add(new Collider(bullets, player.getMovement(), i*step, (i+1)*step, ms));
			colliders.get(i).start();
		}
		colliders.add(new Collider(bullets, player.getMovement(), (cores - 1)*step, bullets.size(), ms));
		colliders.get(cores - 1).start();
		
		for (Enemy e : enemies)
			collision = collision || e.collides(player.getMovement(), ms);
		
		for (Collider c : colliders) {
			try {
				c.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			collision = collision || c.collision();
		}
		
		// TODO: delete this debug lines
		if (collision) {
			System.out.println("Collision, player: " + player.getMovement().getPosition());
			player.getMovement().setPosition(Parser.unvirtualizeCoordinates(new Vertex(0, 100)));
		}
		
		return collision;
	}

	@Override
	public void forward(double ms) {
		
		// TODO: multi-thread it
		
		elapsed += ms;
		
		ArrayList<Bullet> toDeleteBullets = new ArrayList<Bullet>();
		ArrayList<Enemy> toDeleteEnemies = new ArrayList<Enemy>();
		
		// Forward current bullets
		for (Bullet b : bullets) {
			b.forward(ms);
			if (!gameZone.isInside(null, b.getMovement().getPosition()))
				toDeleteBullets.add(b);
		}

		// Forward current enemies
		for (Enemy e : enemies) {		
			
			// Spawn new bullets if shot
			for (Bullet b : e.forwardAndBullets(ms)) {
				b.spawn(elapsed);
				bullets.add(b);
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
			}
		}
		
		for (Bullet b : toDeleteBullets)
			bullets.remove(b);
		
		for (Enemy e : toDeleteEnemies)
			enemies.remove(e);
		
		player.update(ms);
	}
	
	protected class Collider extends Thread {
		
		ArrayList<? extends Entity> entities;
		protected Movement collider;
		protected int start;
		protected int end;
		protected double ms;
		
		protected boolean collision = false;
		
		public Collider(ArrayList<? extends Entity> entities, Movement collider, int start, int end, double ms) {
			this.entities = entities;
			this.collider = collider;
			this.start = start;
			this.end = end;
		}
		
		@Override
		public void run() {
			Iterator<? extends Entity> it = entities.listIterator(start);
			int size = end - start;
			for (int i = 0; i < size && !collision; i++)
				collision = collision || it.next().collides(player.getMovement(), ms);
		}
		
		public boolean collision() {
			return collision;
		}
	}

	@Override
	public boolean isFinished() {
		return false;
	}

}
