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
import com.jde.view.Game;

/**
 * This Stage class is the main engine of the game, it contains the player,
 * bullets and enemies
 * 
 * @author HarZe (David Serrano)
 */
public class Stage {

	/** Number of cores */
	protected static int cores = Runtime.getRuntime().availableProcessors();

	/** List of current enemies */
	protected ArrayList<Enemy> enemies;
	/** List of current bullets */
	protected ArrayList<Bullet> bullets;
	/** Enemy spawners */
	protected Spawner<Enemy> spawner;

	/** Game board's hit zone */
	protected HitZone gameZone;

	/** Player */
	protected Player player;

	/** Elapsed time since the stage started */
	protected double elapsed = 0;

	/**
	 * Basic constructor
	 * 
	 * @param enemiesSpawn
	 *            List of enemies to spawn
	 * @param player
	 *            Player template
	 */
	public Stage(ArrayList<Enemy> enemiesSpawn, Player player) {
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<Bullet>();

		spawner = new Spawner<Enemy>(0);
		spawner.addSpawnables(enemiesSpawn);

		gameZone = new HitBox(Game.GAME_BOARD_CENTER, Game.GAME_BOARD_SIZE
				.clone().add(new Vertex(50, 50)));

		this.player = player;

		// Multi-thread limits & checks
		if (cores < 2)
			cores = 2;
		else if (cores > 8)
			cores = 8;
	}

	/**
	 * This method checks if the player is colliding with any enemy entity
	 * 
	 * @param ms
	 *            Milliseconds to forward
	 * @return True if the player collides
	 */
	public boolean colliding(double ms) {
		boolean collision = false;

		// Multi-thread collision detection
		ArrayList<Collider> colliders = new ArrayList<Collider>();
		int step = bullets.size() / (cores - 1);

		for (int i = 0; i < cores - 1; i++) {
			colliders.add(new Collider(bullets, player.getMovement(), i * step,
					(i + 1) * step, ms));
			colliders.get(i).start();
		}
		colliders.add(new Collider(bullets, player.getMovement(), (cores - 1)
				* step, bullets.size(), ms));
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
			System.out.println("Collision, player: "
					+ player.getMovement().getPosition());
			player.getMovement().setPosition(
					Parser.unvirtualizeCoordinates(new Vertex(0, 100)));
		}

		return collision;
	}

	/**
	 * Draws the stage
	 */
	public void draw() {

		player.draw();

		for (Enemy e : enemies)
			e.draw();

		for (Bullet b : bullets)
			b.draw();

		player.drawFocus();
	}

	/**
	 * Forward all the entities
	 * 
	 * @param ms
	 *            Milliseconds to forward
	 */
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

	public boolean isFinished() {
		return false;
	}

	/**
	 * This Collider is a thread of a multi-thread collision system
	 * 
	 * @author HarZe (David Serrano)
	 */
	protected class Collider extends Thread {

		/** List of entities to check collision */
		ArrayList<? extends Entity> entities;
		/** Movement of the colliding entity */
		protected Movement other;
		/** Start index of the entity list */
		protected int start;
		/** End index of the entity list */
		protected int end;
		/** Milliseconds of forwarding */
		protected double ms;

		/** Result of the checking, true if there has been collision */
		protected boolean collision = false;

		/**
		 * Full constructor
		 * 
		 * @param entities
		 *            List of entities to check collision
		 * @param other
		 *            Movement of the colliding entity
		 * @param start
		 *            Start index of the entity list
		 * @param end
		 *            End index of the entity list
		 * @param ms
		 *            Milliseconds of forwarding
		 */
		public Collider(ArrayList<? extends Entity> entities, Movement other,
				int start, int end, double ms) {
			this.entities = entities;
			this.other = other;
			this.start = start;
			this.end = end;
		}

		/**
		 * @return True if there has been collision
		 */
		public boolean collision() {
			return collision;
		}

		@Override
		public void run() {
			Iterator<? extends Entity> it = entities.listIterator(start);
			int size = end - start;
			for (int i = 0; i < size && !collision; i++)
				collision = collision
						|| it.next().collides(player.getMovement(), ms);
		}
	}

}
