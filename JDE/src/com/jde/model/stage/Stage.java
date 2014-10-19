package com.jde.model.stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.jde.audio.Music;
import com.jde.model.entity.Entity;
import com.jde.model.entity.bullet.Bullet;
import com.jde.model.entity.enemy.Enemy;
import com.jde.model.entity.player.Player;
import com.jde.model.entity.spawning.Spawner;
import com.jde.model.physics.Vertex;
import com.jde.model.physics.collision.HitBox;
import com.jde.model.physics.collision.HitZone;
import com.jde.view.Game;

/**
 * This Stage class is the main engine of the game, it contains the player,
 * bullets and enemies
 * 
 * @author HarZe (David Serrano)
 */
public class Stage {

	/**
	 * This Collider is a thread of a multi-thread collision system
	 * 
	 * @author HarZe (David Serrano)
	 */
	protected class Collider extends Thread {

		/** List of entities to check collision */
		ArrayList<? extends Entity> entities;
		/** Colliding entity */
		protected Entity other;
		/** Start index of the entity list */
		protected int start;
		/** End index of the entity list */
		protected int end;
		/** Milliseconds of forwarding */
		protected double ms;

		/** Result of the checking, true if there has been collision */
		protected boolean collision = false;
		/** Detected collision entity */
		protected Entity collisionEntity = null;

		/**
		 * Full constructor
		 * 
		 * @param entities
		 *            List of entities to check collision
		 * @param other
		 *            Colliding entity
		 * @param start
		 *            Start index of the entity list
		 * @param end
		 *            End index of the entity list
		 * @param ms
		 *            Milliseconds of forwarding
		 */
		public Collider(ArrayList<? extends Entity> entities, Entity other,
				int start, int end, double ms) {
			this.entities = entities;
			this.other = other;
			this.start = start;
			this.end = end;
		}

		/**
		 * @return Returns the entity that is meant to collide with the list of
		 *         entities
		 */
		public Entity collider() {
			return other;
		}

		/**
		 * @return True if there has been collision
		 */
		public boolean collision() {
			return collision;
		}

		/**
		 * @return The entity of the given list which has collided first (if
		 *         any), null otherwise
		 */
		public Entity collisionEntity() {
			return collisionEntity;
		}

		@Override
		public void run() {
			Iterator<? extends Entity> it = entities.listIterator(start);
			int size = end - start;
			for (int i = 0; i < size && !collision; i++) {
				collisionEntity = it.next();
				collision = collisionEntity.collides(other.getMovement(), ms);
			}

			// TODO: add support to detect multiple bullets colliding the enemy
			// in the same frame, not very likely but it can happen
		}
	}

	/** Number of cores */
	protected static int cores = Runtime.getRuntime().availableProcessors();

	/** Despawn border margin limit */
	protected static final int MARGIN = 50;

	/** List of current enemies */
	protected ArrayList<Enemy> enemies;
	/** List of current enemy bullets */
	protected ArrayList<Bullet> enemyBullets;

	/** Enemy spawners */
	protected Spawner<Enemy> enemySpawner;

	/** Game board's hit zone */
	protected HitZone gameZone;
	/** Player */
	protected Player player;

	/** List of current player bullets */
	protected ArrayList<Bullet> playerBullets;

	/** Background music */
	protected Music music;

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
		enemyBullets = new ArrayList<Bullet>();
		playerBullets = new ArrayList<Bullet>();

		enemySpawner = new Spawner<Enemy>(0);
		enemySpawner.addSpawnables(enemiesSpawn);

		gameZone = new HitBox(Game.GAME_BOARD_CENTER, Game.GAME_BOARD_SIZE
				.clone().add(new Vertex(MARGIN, MARGIN)));

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
	 * @return Points earned
	 */
	public double colliding(double ms) {
		
		double points = 0;
		
		// Play music at first call (if there is music to play)
		if (elapsed == 0 && music != null)
			music.play();

		// Multi-thread collision detection
		ArrayList<Enemy> toDeleteEnemies = new ArrayList<Enemy>();
		ArrayList<Bullet> toDeletePlayerBullets = new ArrayList<Bullet>();
		boolean collision = false;
		Collider current;
		int i;

		// Enemy bullets that might collide with player
		ArrayList<Collider> fromEnemy = new ArrayList<Collider>();
		int fromEnemyStep = enemyBullets.size() / (cores - 1);

		// Player bullets that might collide with enemies
		HashMap<Enemy, Collider> fromPlayer = new HashMap<Enemy, Collider>();

		// Invoke collision check of enemy bullets on player
		for (i = 0; i < cores - 1; i++) {
			current = new Collider(enemyBullets, player, i * fromEnemyStep,
					(i + 1) * fromEnemyStep, ms);
			fromEnemy.add(current);
			current.start();
		}
		current = new Collider(enemyBullets, player, (cores - 1)
				* fromEnemyStep, enemyBullets.size(), ms);
		fromEnemy.add(current);
		current.start();

		// Invoke collision check of player and its bullets on enemies
		for (Enemy e : enemies) {
			current = new Collider(playerBullets, e, 0, playerBullets.size(),
					ms);
			fromPlayer.put(e, current);
			current.start();

			// Directly check collision of player with enemy
			collision = collision || e.collides(player.getMovement(), ms);
		}

		// Wait for checking enemy bullets to finish
		for (Collider c : fromEnemy) {
			try {
				c.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			collision = collision || c.collision();
		}

		// Wait for checking ally bullets to finish, and remove defeated enemies
		for (Collider c : fromPlayer.values()) {
			try {
				c.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (c.collision()) {
				Enemy enemyHit = (Enemy) c.collider();
				Bullet bullet = (Bullet) c.collisionEntity();

				enemyHit.setHealth(enemyHit.getHealth() - bullet.getPower());
				toDeletePlayerBullets.add(bullet);
				if (enemyHit.getHealth() <= 0) {
					toDeleteEnemies.add(enemyHit);
					points += enemyHit.getPoints();
				}
			}
		}

		// Delete destroyed enemies
		for (Enemy e : toDeleteEnemies)
			enemies.remove(e);

		// Delete the destroyed bullets (hitting an enemy)
		for (Bullet b : toDeletePlayerBullets)
			playerBullets.remove(b);

		if (collision && player.getRespawnProtectRemaining() <= 0) {
			// Respawning player
			player.setCurrentLifes(player.getCurrentLifes() - 1);
			player.setRespawnProtectRemaining(player.getRespawnProtect());
			playerBullets.clear();
			enemyBullets.clear();

			// Move to respawn point
			player.getMovement().setPosition(
					Game.unvirtualizeCoordinates(new Vertex(0, 100)));
		}

		return points;
	}

	/**
	 * Draws the stage
	 */
	public void draw() {

		player.draw();

		for (Enemy e : enemies)
			e.draw();

		for (Bullet b : playerBullets)
			b.draw();

		for (Bullet b : enemyBullets)
			b.draw();

		player.drawFocus();
	}

	/**
	 * Forward all the entities
	 * 
	 * @param ms
	 *            Milliseconds to forward
	 * @return Points earned
	 */
	public double forward(double ms) {

		// TODO: multi-thread it

		elapsed += ms;

		ArrayList<Bullet> toDeleteEnemyBullets = new ArrayList<Bullet>();
		ArrayList<Bullet> toDeletePlayerBullets = new ArrayList<Bullet>();
		ArrayList<Enemy> toDeleteEnemies = new ArrayList<Enemy>();

		// Forward current bullets
		for (Bullet b : enemyBullets) {
			b.forward(ms);
			if (!gameZone.isInside(null, b.getMovement().getPosition()))
				toDeleteEnemyBullets.add(b);
		}

		// Forward current enemies
		for (Enemy e : enemies) {

			// Spawn new bullets if shot
			for (Bullet b : e.forwardAndBullets(ms)) {
				b.spawn();
				enemyBullets.add(b);
			}

			if (!gameZone.isInside(null, e.getMovement().getPosition()))
				toDeleteEnemies.add(e);
		}

		// Spawn new enemies
		for (Enemy e : enemySpawner.forward(ms)) {
			enemies.add(e);

			// Spawn bullets from them
			for (Bullet b : e.spawnAndBullets(elapsed)) {
				b.spawn();
				enemyBullets.add(b);
			}
		}

		// Spawn new player bullets, and move the player
		for (Bullet b : player.update(ms, elapsed)) {
			b.spawn();
			playerBullets.add(b);
		}

		for (Bullet b : playerBullets) {
			b.forward(ms);
			if (!gameZone.isInside(null, b.getMovement().getPosition()))
				toDeletePlayerBullets.add(b);
		}

		for (Bullet b : toDeleteEnemyBullets)
			enemyBullets.remove(b);

		for (Bullet b : toDeletePlayerBullets)
			playerBullets.remove(b);

		for (Enemy e : toDeleteEnemies)
			enemies.remove(e);
		
		return ms;
	}

	public Music getMusic() {
		return music;
	}

	public boolean isFinished() {
		return false;
	}

	public void setMusic(Music music) {
		this.music = music;
	}

}
