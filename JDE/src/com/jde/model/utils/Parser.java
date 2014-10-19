package com.jde.model.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lwjgl.LWJGLException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jde.audio.Music;
import com.jde.audio.SoundEffect;
import com.jde.model.entity.bullet.Bullet;
import com.jde.model.entity.bullet.Wave;
import com.jde.model.entity.enemy.Enemy;
import com.jde.model.entity.player.Player;
import com.jde.model.physics.Direction;
import com.jde.model.physics.DirectionModifier;
import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;
import com.jde.model.physics.collision.HitBody;
import com.jde.model.physics.collision.HitCircle;
import com.jde.model.physics.collision.HitPolygon;
import com.jde.model.physics.collision.HitZone;
import com.jde.model.stage.Stage;
import com.jde.view.Game;
import com.jde.view.hud.HUD;
import com.jde.view.hud.Label;
import com.jde.view.hud.PseudoFont;
import com.jde.view.hud.SpriteLabel;
import com.jde.view.hud.TextLabel;
import com.jde.view.sprites.Animation;
import com.jde.view.sprites.Sprite;
import com.jde.view.sprites.SpriteSheet;

/**
 * This Parser class converts the XML files into playable Game objects
 * 
 * @author HarZe (David Serrano)
 */
public class Parser {

	/** List of imported files to avoid repetition */
	protected HashSet<String> importedFiles;
	/** List of referenceable animations */
	protected HashMap<String, Animation> animations;
	/** List of referenceable bullets */
	protected HashMap<String, Bullet> bullets;
	/** List of referenceable directions */
	protected HashMap<String, Direction> directions;
	/** List of referenceable direction modifiers */
	protected HashMap<String, DirectionModifier> modifiers;
	/** List of referenceable enemies */
	protected HashMap<String, Enemy> enemies;
	/** List of referenceable polygons */
	protected HashMap<String, HitPolygon> polygons;
	/** List of referenceable circles */
	protected HashMap<String, HitCircle> circles;
	/** List of referenceable bodies */
	protected HashMap<String, HitBody> bodies;
	/** List of referenceable HUDs */
	protected HashMap<String, HUD> huds;
	/** List of referenceable labels */
	protected HashMap<String, Label> labels;
	/** List of referenceable sprites */
	protected HashMap<String, Sprite> sprites;
	/** List of referenceable spritesheets */
	protected HashMap<String, SpriteSheet> sheets;
	/** List of referenceable movements */
	protected HashMap<String, Movement> movements;
	/** List of referenceable musics */
	protected HashMap<String, Music> musics;
	/** List of referenceable pseudo-fonts */
	protected HashMap<String, PseudoFont> pseudofonts;
	/** List of referenceable sounds */
	protected HashMap<String, SoundEffect> sounds;
	/** List of referenceable vertices */
	protected HashMap<String, Vertex> vertices;
	/** List of referenceable waves */
	protected HashMap<String, Wave> waves;

	protected ArrayList<Enemy> spawns;
	protected Player player;
	protected Music backgroundMusic;

	protected Vertex playerPos = new Vertex(0, 100);

	protected HUD gameHud = new HUD();

	/**
	 * Default constructor
	 */
	public Parser() {
		importedFiles = new HashSet<String>();

		animations = new HashMap<String, Animation>();
		bullets = new HashMap<String, Bullet>();
		directions = new HashMap<String, Direction>();
		modifiers = new HashMap<String, DirectionModifier>();
		enemies = new HashMap<String, Enemy>();
		polygons = new HashMap<String, HitPolygon>();
		circles = new HashMap<String, HitCircle>();
		bodies = new HashMap<String, HitBody>();
		huds = new HashMap<String, HUD>();
		labels = new HashMap<String, Label>();
		sprites = new HashMap<String, Sprite>();
		sheets = new HashMap<String, SpriteSheet>();
		movements = new HashMap<String, Movement>();
		musics = new HashMap<String, Music>();
		pseudofonts = new HashMap<String, PseudoFont>();
		sounds = new HashMap<String, SoundEffect>();
		vertices = new HashMap<String, Vertex>();
		waves = new HashMap<String, Wave>();

		spawns = new ArrayList<Enemy>();
		player = null;
		backgroundMusic = null;
	}

	/**
	 * This method detects the type of node and process it
	 * 
	 * @param node
	 *            Node to parse
	 * @throws Exception
	 */
	protected void parseNode(Node node) throws Exception {

		switch (node.getNodeName()) {

		case "animation":
		case "animation-ref":
			processAnimation(node);
			break;

		case "bullet":
		case "bullet-ref":
			processBullet(node);
			break;

		case "direction":
		case "direction-ref":
			processDirection(node);
			break;

		case "direction-modifier":
		case "direction-modifier-ref":
			processDirectionModifier(node);
			break;

		case "enemy":
		case "enemy-ref":
			processEnemy(node);
			break;

		case "game":
			processGame(node);
			break;

		case "hit-body":
		case "hit-body-ref":
			processHitBody(node);
			break;

		case "hit-circle":
		case "hit-circle-ref":
			processHitCircle(node);
			break;

		case "hit-polygon":
		case "hit-polygon-ref":
			processHitPolygon(node);
			break;

		case "hud":
			processHud(node);
			break;

		case "import":
			processImport(node);
			break;

		case "movement":
		case "movement-ref":
			processMovement(node);
			break;

		case "music":
		case "music-ref":
			processMusic(node);
			break;

		case "player":
			processPlayer(node);
			break;

		case "pseudo-font":
		case "pseudo-font-ref":
			processPseudoFont(node);
			break;

		case "spawn":
			processSpawn(node);
			break;

		case "sound":
		case "sound-ref":
			processSound(node);
			break;

		case "sprite":
		case "sprite-ref":
			processSprite(node);
			break;

		case "sprite-label":
		case "sprite-label-ref":
			processSpriteLabel(node);
			break;

		case "spritesheet":
			processSpriteSheet(node);
			break;

		case "stage":
		case "stage-ref":
			break;

		case "text-label":
		case "text-label-ref":
			processTextLabel(node);
			break;

		case "vertex":
		case "vertex-ref":
			processVertex(node);
			break;

		case "wave":
		case "wave-ref":
			processWave(node);
			break;

		case "jde-content":
			if (node.hasChildNodes())
				parseNodeList(node.getChildNodes());
			break;

		default:
			throw new Exception("Unknown " + node.getNodeName() + " tag");

		}
	}

	/**
	 * This method parses all the XML nodes in the list
	 * 
	 * @param nodeList
	 *            List of XML nodes
	 * @throws Exception
	 */
	protected void parseNodeList(NodeList nodeList) throws Exception {

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE)
				parseNode(node);
		}

	}

	/**
	 * Full game constructor from initial XML file
	 * 
	 * @param filename
	 *            Main XML file with the <game> tag
	 * @return A ready-to-play instance of Game
	 * @throws LWJGLException
	 * @throws IOException
	 */
	public Game parseXML(String filename) throws IOException, LWJGLException {

		// XML parsing
		try {
			File file = new File(filename);

			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			if (!doc.getDocumentElement().getNodeName()
					.equalsIgnoreCase("jde-content"))
				throw new Exception("XML file " + filename
						+ " is not a jde-content file");

			if (doc.hasChildNodes()) {

				parseNodeList(doc.getChildNodes());

			}

		} catch (Exception e) {
			System.err.println("There has been an error while parsing XML");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		// Resizing hit-bodies
		for (HitBody h : bodies.values())
			h.expand(player.getHitboxRadius());

		// Loading sounds
		for (SoundEffect s : sounds.values())
			s.load();
		for (Music m : musics.values())
			m.load();

		// Creating the game
		ArrayList<Stage> stages = new ArrayList<Stage>();
		Stage testStage = new Stage(spawns, player);
		testStage.setMusic(backgroundMusic);
		stages.add(testStage);
		return new Game(stages, gameHud);
	}

	/**
	 * This method process and <animation> tag, check the wiki to know the
	 * format
	 * 
	 * @param node
	 *            Animation node
	 * @return Generated Animation
	 * @throws Exception
	 */
	protected Animation processAnimation(Node node) throws Exception {

		ArrayList<Sprite> spritesAnim = new ArrayList<Sprite>();
		ArrayList<Double> durationsAnim = new ArrayList<Double>();
		Animation anim = null;

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("animation-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!animations.containsKey(ref))
					throw new Exception("<animation> with name:" + ref
							+ " is not declared");

				return animations.get(ref);
			}
		}

		// Reading the mandatory content
		if (node.hasChildNodes())
			for (int i = 1; i < node.getChildNodes().getLength(); i++) {
				Node currentNode = node.getChildNodes().item(i);

				// Reading frames
				if (currentNode.getNodeName().equals("frame")
						&& currentNode.getChildNodes().getLength() == 2 * 2 + 1) {
					boolean hasSprite = false;
					boolean hasDuration = false;

					for (int j = 1; j < currentNode.getChildNodes().getLength(); j++) {
						Node frameNode = currentNode.getChildNodes().item(j);

						// Reading sprite
						if (frameNode.getNodeName().equals("sprite")
								|| frameNode.getNodeName().equals("sprite-ref")) {
							spritesAnim.add(processSprite(frameNode));
							hasSprite = true;
						}

						// Reading duration
						else if (frameNode.getNodeName().equals("duration")) {
							durationsAnim.add(Double.parseDouble(frameNode
									.getChildNodes().item(0).getNodeValue()));
							hasDuration = true;
						}
					}

					// Checking the <frame> has correct format
					if (!hasSprite || !hasDuration) {
						throw new Exception(
								"<animation>: <frame> must have <sprite> and <duration>");
					}
				}
			}
		else
			throw new Exception("<animation> has not child nodes");

		// Checking there is at least one frame
		if (spritesAnim.size() == 0)
			throw new Exception("<animation> must have at least one <frame>");

		// New animation is generated
		anim = new Animation(spritesAnim, durationsAnim);

		// Checking attributes
		if (node.hasAttributes()) {
			// Checking if it has a reference name to store it
			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				animations.put(name, anim);
			}

			// Checking loopFrom attribute
			if (nodeMap.getNamedItem("loop-from") != null) {
				String loopFrom = nodeMap.getNamedItem("loop-from")
						.getNodeValue();
				anim.setLoopFrom(Integer.parseInt(loopFrom));
			}
		}

		return anim;
	}

	/**
	 * This method process and <bullet> tag, check the wiki to know the format
	 * 
	 * @param node
	 *            Bullet node
	 * @return Generated Bullet
	 * @throws Exception
	 */
	protected Bullet processBullet(Node node) throws Exception {

		Bullet b = null;
		HitBody h = null;
		Animation a = null;
		Movement m = null;
		SoundEffect s = null;

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("bullet-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!bullets.containsKey(ref))
					throw new Exception("<bullet> with name:" + ref
							+ " is not declared");

				return bullets.get(ref);
			}
		}

		// Reading the mandatory content
		boolean hasBody = false;
		boolean hasMov = false;
		boolean hasAnim = false;

		if (node.hasChildNodes()) {

			for (int i = 1; i < node.getChildNodes().getLength(); i += 2) {
				Node currentNode = node.getChildNodes().item(i);

				// Reading hitBody
				if (currentNode.getNodeName().equals("hit-body")
						|| currentNode.getNodeName().equals("hit-body-ref")) {
					h = processHitBody(currentNode);
					hasBody = true;
				}

				// Reading movement
				else if (currentNode.getNodeName().equals("movement")
						|| currentNode.getNodeName().equals("movement-ref")) {
					m = processMovement(currentNode);
					hasMov = true;
				}

				// Reading animation
				else if (currentNode.getNodeName().equals("animation")
						|| currentNode.getNodeName().equals("animation-ref")) {
					a = processAnimation(currentNode);
					hasAnim = true;
				}

				// Reading sound effect
				else if (currentNode.getNodeName().equals("sound")
						|| currentNode.getNodeName().equals("sound-ref"))
					s = processSound(currentNode);
			}
		}

		// Checking
		if (!hasBody || !hasAnim || !hasMov)
			throw new Exception(
					"<bullet> requieres: <hit-body>, <movement> and <animation>");

		// New bullet is generated
		b = new Bullet(a, h, m);
		b.setSpawnSound(s);

		// Checking attributes
		if (node.hasAttributes()) {

			// Optional attribute "power"
			if (nodeMap.getNamedItem("power") != null) {
				Double power = Double.parseDouble(nodeMap.getNamedItem("power")
						.getNodeValue());
				b.setPower(power);
			}

			// Check if it has a reference name to store it
			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				bullets.put(name, b);
			}
		}

		return b;
	}

	/**
	 * This method process and <direction> tag, check the wiki to know the
	 * format
	 * 
	 * @param node
	 *            Direction node
	 * @return Generated Direction
	 * @throws Exception
	 */
	protected Direction processDirection(Node node) throws Exception {

		Direction d = null;
		Vertex hv = null;

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("direction-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!directions.containsKey(ref))
					throw new Exception("<direction> with name:" + ref
							+ " is not declared");

				return directions.get(ref);
			}
		}

		// Reading the mandatory content

		// Homing mode
		if (node.hasChildNodes()
				&& node.getChildNodes().getLength() == 1 * 2 + 1) {
			for (int i = 1; i < 1 * 2 + 1; i += 2) {
				Node currentNode = node.getChildNodes().item(i);

				// Reading homing position
				if (currentNode.getNodeName().equals("vertex")
						|| currentNode.getNodeName().equals("vertex-ref"))
					hv = Game
							.unvirtualizeCoordinates(processVertex(currentNode));
			}
			d = new Direction(hv);
		}

		// Homing (to the player) mode
		else if (nodeMap.getNamedItem("homing-to-player") != null
				&& nodeMap.getNamedItem("homing-to-player").getNodeValue()
						.equalsIgnoreCase("yes")) {
			d = new Direction();
			d.setHoming(true);
			d.setHomingPosition(playerPos);
		}

		// Fixed mode
		else {
			double angle = 0;
			if (nodeMap.getNamedItem("angle") != null) {
				angle = Double.parseDouble(nodeMap.getNamedItem("angle")
						.getNodeValue());
			}
			d = new Direction(angle);
		}

		// Checking attributes
		if (node.hasAttributes()) {

			if (nodeMap.getNamedItem("random-angle-offset") != null) {
				double randomangleoffset = Double.parseDouble(nodeMap
						.getNamedItem("random-angle-offset").getNodeValue());
				d.setRandomAngleOffset(randomangleoffset);
			}

			if (nodeMap.getNamedItem("rotation") != null) {
				double rotation = Double.parseDouble(nodeMap.getNamedItem(
						"rotation").getNodeValue());
				d.setRotation(rotation);
			}

			if (nodeMap.getNamedItem("random-rotation-offset") != null) {
				double randomrotationoffset = Double.parseDouble(nodeMap
						.getNamedItem("random-rotation-offset").getNodeValue());
				d.setRandomRotationOffset(randomrotationoffset);
			}

			if (nodeMap.getNamedItem("motion") != null) {
				double motion = Double.parseDouble(nodeMap.getNamedItem(
						"motion").getNodeValue());
				d.setMotion(motion);
			}

			if (nodeMap.getNamedItem("random-motion-offset") != null) {
				double randommotionoffset = Double.parseDouble(nodeMap
						.getNamedItem("random-motion-offset").getNodeValue());
				d.setRandomMotionOffset(randommotionoffset);
			}

			if (nodeMap.getNamedItem("speed") != null) {
				double speed = Double.parseDouble(nodeMap.getNamedItem("speed")
						.getNodeValue());
				d.setSpeed(speed);
			}

			if (nodeMap.getNamedItem("random-speed-offset") != null) {
				double randomspeedoffset = Double.parseDouble(nodeMap
						.getNamedItem("random-speed-offset").getNodeValue());
				d.setRandomSpeedOffset(randomspeedoffset);
			}
			if (nodeMap.getNamedItem("acceleration") != null) {
				double acceleration = Double.parseDouble(nodeMap.getNamedItem(
						"acceleration").getNodeValue());
				d.setAcceleration(acceleration);
			}

			if (nodeMap.getNamedItem("random-acceleration-offset") != null) {
				double randomaccelerationoffset = Double.parseDouble(nodeMap
						.getNamedItem("random-acceleration-offset")
						.getNodeValue());
				d.setRandomAccelerationOffset(randomaccelerationoffset);
			}

			if (nodeMap.getNamedItem("homing-offset") != null) {
				double homingOffset = Double.parseDouble(nodeMap.getNamedItem(
						"homing-offset").getNodeValue());
				d.setHomingOffset(homingOffset);
			}

			if (nodeMap.getNamedItem("inheritance") != null) {
				if (nodeMap.getNamedItem("inheritance").getNodeValue()
						.equalsIgnoreCase("no"))
					d.setInheritance(false);
			}

			if (nodeMap.getNamedItem("duration") != null) {
				double duration = Double.parseDouble(nodeMap.getNamedItem(
						"duration").getNodeValue());
				d.setDuration(duration);
			}

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				directions.put(name, d);
			}

		}

		return d;
	}

	/**
	 * This method process and <direction-modifier> tag, check the wiki to know
	 * the format
	 * 
	 * @param node
	 *            Direction-modifier node
	 * @return Generated DirectionModifier
	 * @throws Exception
	 */
	protected DirectionModifier processDirectionModifier(Node node)
			throws Exception {

		DirectionModifier d = new DirectionModifier();

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("direction-modifier-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!modifiers.containsKey(ref))
					throw new Exception("<direction-modifier> with name:" + ref
							+ " is not declared");

				return modifiers.get(ref);
			}
		}

		// Checking attributes
		if (node.hasAttributes()) {

			if (nodeMap.getNamedItem("angle-start") != null) {
				double angleStart = Double.parseDouble(nodeMap.getNamedItem(
						"angle-start").getNodeValue());
				d.setAngleStart(angleStart);
			}

			if (nodeMap.getNamedItem("random-angle-offset-start") != null) {
				double randomAngleOffsetStart = Double.parseDouble(nodeMap
						.getNamedItem("random-angle-offset-start")
						.getNodeValue());
				d.setRandomAngleOffsetStart(randomAngleOffsetStart);
			}

			if (nodeMap.getNamedItem("angle-end") != null) {
				double angleEnd = Double.parseDouble(nodeMap.getNamedItem(
						"angle-end").getNodeValue());
				d.setAngleEnd(angleEnd);
			}

			if (nodeMap.getNamedItem("random-angle-offset-end") != null) {
				double randomAngleOffsetEnd = Double
						.parseDouble(nodeMap.getNamedItem(
								"random-angle-offset-end").getNodeValue());
				d.setRandomAngleOffsetEnd(randomAngleOffsetEnd);
			}

			if (nodeMap.getNamedItem("angle-exponent") != null) {
				double angleExponent = Double.parseDouble(nodeMap.getNamedItem(
						"angle-exponent").getNodeValue());
				d.setAngleExponent(angleExponent);
			}

			if (nodeMap.getNamedItem("rotation-start") != null) {
				double rotationStart = Double.parseDouble(nodeMap.getNamedItem(
						"rotation-start").getNodeValue());
				d.setRotationStart(rotationStart);
			}

			if (nodeMap.getNamedItem("random-rotation-offset-start") != null) {
				double randomRotationOffsetStart = Double.parseDouble(nodeMap
						.getNamedItem("random-rotation-offset-start")
						.getNodeValue());
				d.setRandomRotationOffsetStart(randomRotationOffsetStart);
			}

			if (nodeMap.getNamedItem("rotation-end") != null) {
				double rotationEnd = Double.parseDouble(nodeMap.getNamedItem(
						"rotation-end").getNodeValue());
				d.setRotationEnd(rotationEnd);
			}

			if (nodeMap.getNamedItem("random-motion-offset-end") != null) {
				double randomMotionOffsetEnd = Double.parseDouble(nodeMap
						.getNamedItem("random-moation-offset-end")
						.getNodeValue());
				d.setRandomMotionOffsetEnd(randomMotionOffsetEnd);
			}

			if (nodeMap.getNamedItem("rotation-exponent") != null) {
				double rotationExponent = Double.parseDouble(nodeMap
						.getNamedItem("rotation-exponent").getNodeValue());
				d.setRotationExponent(rotationExponent);
			}

			if (nodeMap.getNamedItem("motion-start") != null) {
				double motionStart = Double.parseDouble(nodeMap.getNamedItem(
						"motion-start").getNodeValue());
				d.setMotionStart(motionStart);
			}

			if (nodeMap.getNamedItem("random-motion-offset-start") != null) {
				double randomMotionOffsetStart = Double.parseDouble(nodeMap
						.getNamedItem("random-motion-offset-start")
						.getNodeValue());
				d.setRandomMotionOffsetStart(randomMotionOffsetStart);
			}

			if (nodeMap.getNamedItem("motion-end") != null) {
				double motionEnd = Double.parseDouble(nodeMap.getNamedItem(
						"motion-end").getNodeValue());
				d.setMotionEnd(motionEnd);
			}

			if (nodeMap.getNamedItem("random-rotation-offset-end") != null) {
				double randomRotationOffsetEnd = Double.parseDouble(nodeMap
						.getNamedItem("random-rotation-offset-end")
						.getNodeValue());
				d.setRandomRotationOffsetEnd(randomRotationOffsetEnd);
			}

			if (nodeMap.getNamedItem("motion-exponent") != null) {
				double motionExponent = Double.parseDouble(nodeMap
						.getNamedItem("motion-exponent").getNodeValue());
				d.setMotionExponent(motionExponent);
			}

			if (nodeMap.getNamedItem("speed-start") != null) {
				double speedStart = Double.parseDouble(nodeMap.getNamedItem(
						"speed-start").getNodeValue());
				d.setSpeedStart(speedStart);
			}

			if (nodeMap.getNamedItem("random-speed-offset-start") != null) {
				double randomSpeedOffsetStart = Double.parseDouble(nodeMap
						.getNamedItem("random-speed-offset-start")
						.getNodeValue());
				d.setRandomSpeedOffsetStart(randomSpeedOffsetStart);
			}

			if (nodeMap.getNamedItem("speed-end") != null) {
				double speedEnd = Double.parseDouble(nodeMap.getNamedItem(
						"speed-end").getNodeValue());
				d.setSpeedEnd(speedEnd);
			}

			if (nodeMap.getNamedItem("random-speed-offset-end") != null) {
				double randomSpeedOffsetEnd = Double
						.parseDouble(nodeMap.getNamedItem(
								"random-speed-offset-end").getNodeValue());
				d.setRandomSpeedOffsetEnd(randomSpeedOffsetEnd);
			}

			if (nodeMap.getNamedItem("speed-exponent") != null) {
				double speedExponent = Double.parseDouble(nodeMap.getNamedItem(
						"speed-exponent").getNodeValue());
				d.setSpeedExponent(speedExponent);
			}

			if (nodeMap.getNamedItem("acceleration-start") != null) {
				double accelerationStart = Double.parseDouble(nodeMap
						.getNamedItem("acceleration-start").getNodeValue());
				d.setAccelerationStart(accelerationStart);
			}

			if (nodeMap.getNamedItem("random-acceleration-offset-start") != null) {
				double randomAccelerationOffsetStart = Double
						.parseDouble(nodeMap.getNamedItem(
								"random-acceleration-offset-start")
								.getNodeValue());
				d.setRandomAccelerationOffsetStart(randomAccelerationOffsetStart);
			}

			if (nodeMap.getNamedItem("acceleration-end") != null) {
				double accelerationEnd = Double.parseDouble(nodeMap
						.getNamedItem("acceleration-end").getNodeValue());
				d.setAccelerationEnd(accelerationEnd);
			}

			if (nodeMap.getNamedItem("random-acceleration-offset-end") != null) {
				double randomAccelerationOffsetEnd = Double.parseDouble(nodeMap
						.getNamedItem("random-acceleration-offset-end")
						.getNodeValue());
				d.setRandomAccelerationOffsetEnd(randomAccelerationOffsetEnd);
			}

			if (nodeMap.getNamedItem("acceleration-exponent") != null) {
				double accelerationExponent = Double.parseDouble(nodeMap
						.getNamedItem("acceleration-exponent").getNodeValue());
				d.setAccelerationExponent(accelerationExponent);
			}

			if (nodeMap.getNamedItem("homing-offset-start") != null) {
				double homingOffsetStart = Double.parseDouble(nodeMap
						.getNamedItem("homing-offset-start").getNodeValue());
				d.setHomingOffsetStart(homingOffsetStart);
			}

			if (nodeMap.getNamedItem("homing-offset-end") != null) {
				double homingOffsetEnd = Double.parseDouble(nodeMap
						.getNamedItem("homing-offset-end").getNodeValue());
				d.setHomingOffsetEnd(homingOffsetEnd);
			}

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				modifiers.put(name, d);
			}

		}

		return d;
	}

	/**
	 * This method process and <enemy> tag, check the wiki to know the format
	 * 
	 * @param node
	 *            Enemy node
	 * @return Generated Enemy
	 * @throws Exception
	 */
	protected Enemy processEnemy(Node node) throws Exception {

		Enemy e = null;
		HitBody h = null;
		Animation a = null;
		Movement m = null;
		Wave w = null;
		SoundEffect s = null;

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("enemy-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!enemies.containsKey(ref))
					throw new Exception("<enemy> with name:" + ref
							+ " is not declared");

				return enemies.get(ref);
			}
		}

		// Reading mandatory content
		boolean hasBody = false;
		boolean hasMov = false;
		boolean hasAnim = false;

		if (node.hasChildNodes()) {

			for (int i = 1; i < node.getChildNodes().getLength(); i += 2) {
				Node currentNode = node.getChildNodes().item(i);

				if (currentNode.getNodeName().equals("hit-body")
						|| currentNode.getNodeName().equals("hit-body-ref")) {
					h = processHitBody(currentNode);
					hasBody = true;
				}

				else if (currentNode.getNodeName().equals("movement")
						|| currentNode.getNodeName().equals("movement-ref")) {
					m = processMovement(currentNode);
					hasMov = true;
				}

				else if (currentNode.getNodeName().equals("animation")
						|| currentNode.getNodeName().equals("animation-ref")) {
					a = processAnimation(currentNode);
					hasAnim = true;
				}

				else if (currentNode.getNodeName().equals("wave")
						|| currentNode.getNodeName().equals("wave-ref"))
					w = processWave(currentNode);

				else if (currentNode.getNodeName().equals("sound")
						|| currentNode.getNodeName().equals("sound-ref"))
					s = processSound(currentNode);
			}
		}

		// Checking
		if (!hasBody || !hasAnim || !hasMov)
			throw new Exception(
					"<enemy> requieres: <hit-body>, <movement>, <animation> and may contain an <wave> and/or <sound>");

		// Checking attributes
		if (node.hasAttributes()) {

			if (nodeMap.getNamedItem("health") != null) {
				double health = Double.parseDouble(nodeMap.getNamedItem(
						"health").getNodeValue());

				// New Enemy is created
				e = new Enemy(a, h, m, w, health);
				e.setSpawnSound(s);
			} else
				throw new Exception("<enemy> with no health declared");

			if (nodeMap.getNamedItem("points") != null) {
				double points = Double.parseDouble(nodeMap.getNamedItem(
						"points").getNodeValue());
				e.setPoints(points);
			}

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				enemies.put(name, e);
				return e;
			} else
				throw new Exception("<enemy> with no name declared");

		} else
			throw new Exception("<enemy> with no attributes declared");

	}

	// TODO: incomplete game, just creates one stage with spawns for ArmyStage
	/**
	 * This method process and <game> tag, check the wiki to know the format
	 * 
	 * @param node
	 *            Game node
	 * @throws Exception
	 */
	protected void processGame(Node node) throws Exception {

		boolean hasPlayer = false;
		boolean hasHud = false;

		if (node.hasChildNodes())
			for (int i = 1; i < node.getChildNodes().getLength(); i += 2) {
				Node currentNode = node.getChildNodes().item(i);

				if (currentNode.getNodeName().equals("spawn"))
					processSpawn(currentNode);

				else if (currentNode.getNodeName().equals("player")) {
					processPlayer(currentNode);
					hasPlayer = true;
				}

				else if (currentNode.getNodeName().equals("hud")
						|| currentNode.getNodeName().equals("hud-ref")) {
					gameHud = processHud(currentNode);
					hasHud = true;
				}

				else if (currentNode.getNodeName().equals("music")
						|| currentNode.getNodeName().equals("music-ref")) {
					backgroundMusic = processMusic(currentNode);
				}
			}

		if (!hasPlayer || !hasHud)
			throw new Exception("<game> must contain a <player> and a <hud>");
	}

	/**
	 * This method process and <hit-body> tag, check the wiki to know the format
	 * 
	 * @param node
	 *            Hit-body node
	 * @return Generated HitBody
	 * @throws Exception
	 */
	protected HitBody processHitBody(Node node) throws Exception {

		ArrayList<HitZone> zones = new ArrayList<HitZone>();
		HitBody hitB = null;

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("hit-body-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!bodies.containsKey(ref))
					throw new Exception("<hit-body> with name:" + ref
							+ " is not declared");

				return bodies.get(ref);
			}
		}

		// Reading the mandatory content
		if (node.hasChildNodes())
			for (int i = 1; i < node.getChildNodes().getLength(); i += 2) {
				Node currentNode = node.getChildNodes().item(i);
				if (currentNode.getNodeName().equals("hit-circle")
						|| currentNode.getNodeName().equals("hit-circle-ref"))
					zones.add(processHitCircle(currentNode));
				else if (currentNode.getNodeName().equals("hit-polygon")
						|| currentNode.getNodeName().equals("hit-polygon-ref"))
					zones.add(processHitPolygon(currentNode));
			}

		// Checking that there is at least one "hit zone"
		if (zones.size() == 0)
			throw new Exception(
					"<hit-body> must have at least one <hit-circle> or <hit-polygon>");

		// New HitBody is created
		hitB = new HitBody(zones);

		// Checking attributes
		if (node.hasAttributes()) {

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				bodies.put(name, hitB);
				return hitB;
			}

		}

		return hitB;
	}

	/**
	 * This method process and <hit-circle> tag, check the wiki to know the
	 * format
	 * 
	 * @param node
	 *            Hit-circle node
	 * @return Generated HitCircle
	 * @throws Exception
	 */
	protected HitCircle processHitCircle(Node node) throws Exception {

		HitCircle hitC = new HitCircle(new Vertex(), 0);

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("hit-circle-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!circles.containsKey(ref))
					throw new Exception("<hit-circle> with name:" + ref
							+ " is not declared");

				return circles.get(ref);
			}
		}

		// Reading optional content
		if (node.hasChildNodes()
				&& node.getChildNodes().getLength() == 1 * 2 + 1)
			for (int i = 1; i < 2 * 1 + 1; i += 2) {
				Node currentNode = node.getChildNodes().item(i);
				if (currentNode.getNodeName().equals("vertex")
						|| currentNode.getNodeName().equals("vertex-ref"))
					hitC.setCenter(processVertex(currentNode).clone());
			}

		// Checking attributes
		if (node.hasAttributes()) {

			if (nodeMap.getNamedItem("radius") != null) {
				double radius = Double.parseDouble(nodeMap.getNamedItem(
						"radius").getNodeValue());
				hitC.setRadius(radius);
			} else
				throw new Exception("<hit-circle> with no radius declared");

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				circles.put(name, hitC);
			}

		} else
			throw new Exception("<hit-circle> with no attributes declared");

		return hitC;
	}

	/**
	 * This method process and <hit-polygon> tag, check the wiki to know the
	 * format
	 * 
	 * @param node
	 *            Hit-polygon node
	 * @return Generated HitPolygon
	 * @throws Exception
	 */
	protected HitPolygon processHitPolygon(Node node) throws Exception {

		LinkedList<Vertex> polygonVrtcs = new LinkedList<Vertex>();
		HitPolygon hitP = null;

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("hit-polygon-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!polygons.containsKey(ref))
					throw new Exception("<hit-polygon> with name:" + ref
							+ " is not declared");

				return polygons.get(ref);
			}
		}

		// Reading mandatory content
		if (node.hasChildNodes())
			for (int i = 1; i < node.getChildNodes().getLength(); i += 2) {
				Node currentNode = node.getChildNodes().item(i);
				if (currentNode.getNodeName().equals("vertex")
						|| currentNode.getNodeName().equals("vertex-ref"))
					polygonVrtcs.add(processVertex(currentNode));
			}

		// Checking the polygon has already 3 vertices
		if (polygonVrtcs.size() < 3)
			throw new Exception(
					"<hit-polygon> must have 3 <vertex> or more to be a polygon");

		// New HitPolygon is created
		hitP = new HitPolygon(polygonVrtcs);

		// Checking attributes
		if (node.hasAttributes()) {
			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				polygons.put(name, hitP);
			}
		}

		return hitP;
	}

	/**
	 * This method process and <import> tag, check the wiki to know the format
	 * 
	 * @param node
	 *            Import node
	 * @throws Exception
	 */
	protected void processImport(Node node) throws Exception {

		// Checking attributes
		if (node.hasAttributes()) {
			NamedNodeMap nodeMap = node.getAttributes();

			// Checking "file" attribute
			if (nodeMap.getNamedItem("file") != null) {
				String filename = nodeMap.getNamedItem("file").getNodeValue();

				// Avoid repetition
				if (importedFiles.contains(filename))
					return;
				else
					importedFiles.add(filename);

				// Reading the file
				File file = new File(filename);

				DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				Document doc = dBuilder.parse(file);

				// Checking if it is a JDE XML file
				if (!doc.getDocumentElement().getNodeName()
						.equalsIgnoreCase("jde-content"))
					throw new Exception("XML file " + filename
							+ " is not a jde-content file");

				// Process the XML file content
				if (doc.hasChildNodes()) {
					parseNodeList(doc.getChildNodes());
				}

			} else
				throw new Exception("<import> with no file declared");

		} else
			throw new Exception("<import> with no attributes declared");
	}

	/**
	 * This method process and <hud> tag, check the wiki to know the format
	 * 
	 * @param node
	 *            HUD node
	 * @return Generated HUD
	 * @throws Exception
	 */
	protected HUD processHud(Node node) throws Exception {

		HUD h = null;

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("hud-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!huds.containsKey(ref))
					throw new Exception("<hud> with name:" + ref
							+ " is not declared");

				return huds.get(ref);
			}
		}

		h = new HUD();

		// Reading mandatory content
		if (node.hasChildNodes())
			for (int i = 1; i < node.getChildNodes().getLength(); i += 2) {
				Node currentNode = node.getChildNodes().item(i);
				Label currentLabel = null;

				if (currentNode.getNodeName().equals("sprite-label")
						|| currentNode.getNodeName().equals("sprite-label-ref"))
					currentLabel = processSpriteLabel(currentNode);
				else if (currentNode.getNodeName().equals("text-label")
						|| currentNode.getNodeName().equals("text-label-ref"))
					currentLabel = processTextLabel(currentNode);

				if (currentLabel == null)
					throw new Exception(
							"<hud> must cointain only a list of <sprite-label> or <text-label>");

				h.addLabel(currentLabel.getName(), currentLabel);
			}

		// Checking attributes
		if (node.hasAttributes()) {
			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				huds.put(name, h);
			}
		}

		return h;

	}

	/**
	 * This method process and <movement> tag, check the wiki to know the format
	 * 
	 * @param node
	 *            Movement node
	 * @return Generated Movement
	 * @throws Exception
	 */
	protected Movement processMovement(Node node) throws Exception {

		Movement m = null;
		Vertex pos = new Vertex();
		ArrayList<Direction> dirs = new ArrayList<Direction>();

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("movement-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!movements.containsKey(ref))
					throw new Exception("<movement> with name:" + ref
							+ " is not declared");

				return movements.get(ref);
			}
		}

		// Reading mandatory content
		if (node.hasChildNodes())
			for (int i = 1; i < node.getChildNodes().getLength(); i += 2) {
				Node currentNode = node.getChildNodes().item(i);
				if (currentNode.getNodeName().equals("vertex")
						|| currentNode.getNodeName().equals("vertex-ref"))
					pos = processVertex(currentNode);
				else if (currentNode.getNodeName().equals("direction")
						|| currentNode.getNodeName().equals("direction-ref"))
					dirs.add(processDirection(currentNode));
			}

		// Checking if there is at least one direction
		if (dirs.size() == 0)
			throw new Exception("<movement> has no directions");

		// New Movement is created
		m = new Movement(pos, dirs);

		// Checking attributes
		if (node.hasAttributes()) {

			if (nodeMap.getNamedItem("spin") != null) {
				double spin = Double.parseDouble(nodeMap.getNamedItem("spin")
						.getNodeValue());
				m.setSpin(spin);
			}

			if (nodeMap.getNamedItem("look-at-direction") != null) {
				if (nodeMap.getNamedItem("look-at-direction").getNodeValue()
						.equalsIgnoreCase("no"))
					m.setLookAtMovingDirection(false);
			}

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				movements.put(name, m);
			}
		}

		return m;
	}

	/**
	 * This method process and <music> tag, check the wiki to know the format
	 * 
	 * @param node
	 *            Music node
	 * @return Generated Music
	 * @throws Exception
	 */
	protected Music processMusic(Node node) throws Exception {

		Music m = null;
		String filename = "";

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("music-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!musics.containsKey(ref))
					throw new Exception("<music> with name:" + ref
							+ " is not declared");

				return musics.get(ref);
			}
		}

		// Checking attributes
		if (node.hasAttributes()) {

			if (nodeMap.getNamedItem("file") != null)
				filename = nodeMap.getNamedItem("file").getNodeValue();
			else
				throw new Exception("<music> with no file declared");

			// New Music is created
			m = new Music(filename);

			if (nodeMap.getNamedItem("pitch") != null) {
				Float pitch = Float.parseFloat(nodeMap.getNamedItem("pitch")
						.getNodeValue());
				m.setPitch(pitch);
			}

			if (nodeMap.getNamedItem("gain") != null) {
				Float gain = Float.parseFloat(nodeMap.getNamedItem("gain")
						.getNodeValue());
				m.setGain(gain);
			}

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				musics.put(name, m);
			} else
				throw new Exception("<music> with no name declared");

		} else
			throw new Exception("<music> with no attributes declared");

		return m;
	}

	/**
	 * This method process and <player> tag, check the wiki to know the format
	 * 
	 * @param node
	 *            Player node
	 * @return Generated Player
	 * @throws Exception
	 */
	protected Player processPlayer(Node node) throws Exception {

		ArrayList<Animation> anims = new ArrayList<Animation>();
		Vertex v = new Vertex(0, 100);
		Wave w = null;

		NamedNodeMap nodeMap = node.getAttributes();

		// Reading mandatory content
		boolean hasWave = false;

		if (node.hasChildNodes())
			for (int i = 1; i < node.getChildNodes().getLength(); i += 2) {
				Node currentNode = node.getChildNodes().item(i);

				if (currentNode.getNodeName().equals("vertex")
						|| currentNode.getNodeName().equals("vertex-ref"))
					v = processVertex(currentNode);

				else if (currentNode.getNodeName().equals("animation")
						|| currentNode.getNodeName().equals("animation-ref"))
					anims.add(processAnimation(currentNode));

				else if (currentNode.getNodeName().equals("wave")
						|| currentNode.getNodeName().equals("wave-ref")) {
					w = processWave(currentNode);
					hasWave = true;
				}
			}

		// Checking there are at least 2 main animations and there is a wave
		if (anims.size() < 2 || !hasWave)
			throw new Exception(
					"<player> requieres: <vertex>, <wave> and two <animation>");

		// New Player is created
		v = Game.unvirtualizeCoordinates(v);
		playerPos.setX(v.getX());
		playerPos.setY(v.getY());
		player = new Player(anims.get(0), anims.get(1), playerPos, w);
		if (anims.size() >= 3)
			player.setMovingLeftAnimation(anims.get(2));
		if (anims.size() >= 4)
			player.setMovingRightAnimation(anims.get(3));

		// Checking attributes
		if (node.hasAttributes()) {

			if (nodeMap.getNamedItem("cooldown") != null) {
				double cooldown = Double.parseDouble(nodeMap.getNamedItem(
						"cooldown").getNodeValue());
				player.setBaseCooldown(cooldown);
			}

			if (nodeMap.getNamedItem("hit-size") != null) {
				double hitSize = Double.parseDouble(nodeMap.getNamedItem(
						"hit-size").getNodeValue());
				player.setHitboxRadius(hitSize);
			} else
				throw new Exception("<player> with no hit-size declared");

			if (nodeMap.getNamedItem("lifes") != null) {
				double lifes = Double.parseDouble(nodeMap.getNamedItem("lifes")
						.getNodeValue());
				player.setBaseLifes(lifes);
			}

			if (nodeMap.getNamedItem("respawn-time") != null) {
				double respawnTime = Double.parseDouble(nodeMap.getNamedItem(
						"respawn-time").getNodeValue());
				player.setRespawnProtect(respawnTime);
			}

		} else
			throw new Exception("<player> with no attributes declared");

		return player;
	}

	/**
	 * This method process and <pseudo-font> tag, check the wiki to know the
	 * format
	 * 
	 * @param node
	 *            Pseudo-font node
	 * @return Generated PseudoFont
	 * @throws Exception
	 */
	protected PseudoFont processPseudoFont(Node node) throws Exception {

		PseudoFont f = null;
		String c = null;
		Sprite s = null;

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("pseudo-font-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!pseudofonts.containsKey(ref))
					throw new Exception("<pseudo-font> with name:" + ref
							+ " is not declared");

				return pseudofonts.get(ref);
			}
		}

		f = new PseudoFont();

		// Reading mandatory content
		if (node.hasChildNodes())
			for (int i = 1; i < node.getChildNodes().getLength(); i += 2) {
				Node currentNode = node.getChildNodes().item(i);

				if (currentNode.getNodeName().equals("char")) {
					boolean hasSprite = false;

					// Checking "value" attribute
					if (currentNode.hasAttributes())
						c = currentNode.getAttributes().getNamedItem("value")
								.getNodeValue();
					else
						throw new Exception(
								"<char> declared without 'value' attribute");

					// Reading Sprite
					if (currentNode.hasChildNodes()) {
						for (int j = 1; j < currentNode.getChildNodes()
								.getLength(); j += 2) {
							Node charNode = currentNode.getChildNodes().item(j);

							if (charNode.getNodeName().equals("sprite")
									|| charNode.getNodeName().equals(
											"sprite-ref")) {
								s = processSprite(charNode);
								hasSprite = true;
							}
						}
					} else
						throw new Exception("<char> must contain a <sprite>");

					if (!hasSprite)
						throw new Exception("<char> must contain a <sprite>");

					// Adding the character
					f.addChar(c, s);
				}

			}

		// Checking attributes
		if (node.hasAttributes()) {
			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				pseudofonts.put(name, f);
			}
		}

		return f;
	}

	// TODO: provisional spawner for enemies
	/**
	 * This method process and <spawn> tag, check the wiki to know the format
	 * 
	 * @param node
	 *            Spawn node
	 * @throws Exception
	 */
	protected void processSpawn(Node node) throws Exception {

		Enemy e = null;
		Vertex pos = null;

		NamedNodeMap nodeMap = node.getAttributes();

		// Reading mandatory content
		boolean hasVertex = false;
		boolean hasEnemy = false;

		if (node.hasChildNodes()
				&& node.getChildNodes().getLength() == 2 * 2 + 1)
			for (int i = 1; i < 2 * 2 + 1; i += 2) {
				Node currentNode = node.getChildNodes().item(i);

				if (currentNode.getNodeName().equals("vertex")
						|| currentNode.getNodeName().equals("vertex-ref")) {
					pos = processVertex(currentNode);
					hasVertex = true;
				}

				else if (currentNode.getNodeName().equals("enemy")
						|| currentNode.getNodeName().equals("enemy-ref")) {
					e = processEnemy(currentNode).clone();
					hasEnemy = true;
				}
			}

		if (!hasVertex || !hasEnemy)
			throw new Exception("<spawn> requieres: <vertex> and <enemy>");

		// Checking attributes
		if (node.hasAttributes()) {

			if (nodeMap.getNamedItem("time") != null) {
				double time = Double.parseDouble(nodeMap.getNamedItem("time")
						.getNodeValue());
				e.setSpawnTime(time);
			} else
				throw new Exception("<spawn> with no time declared");

			// New Spawn is created
			pos = Game.unvirtualizeCoordinates(pos);
			e.getMovement().getPosition().setX(pos.getX());
			e.getMovement().getPosition().setY(pos.getY());
			spawns.add(e);

		} else
			throw new Exception("<spawn> with no attributes declared");
	}

	/**
	 * This method process and <sprite> tag, check the wiki to know the format
	 * 
	 * @param node
	 *            Sprite node
	 * @return Generated Sprite
	 * @throws Exception
	 */
	protected Sprite processSprite(Node node) throws Exception {

		Sprite s = null;
		SpriteSheet sheet = null;
		double x, y, w, h;
		double scaling = 1;

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("sprite-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!sprites.containsKey(ref))
					throw new Exception("<sprite> with name:" + ref
							+ " is not declared");

				return sprites.get(ref);
			}
		}

		// Checking attributes
		if (node.hasAttributes()) {

			if (nodeMap.getNamedItem("sheet") != null) {
				String sheetname = nodeMap.getNamedItem("sheet").getNodeValue();
				if (!sheets.containsKey(sheetname))
					throw new Exception("<spritesheet> with name:" + sheetname
							+ " is not declared");
				else
					sheet = sheets.get(sheetname);
			} else
				throw new Exception("<sprite> with no sheet declared");

			if (nodeMap.getNamedItem("x") != null)
				x = Double
						.parseDouble(nodeMap.getNamedItem("x").getNodeValue());
			else
				throw new Exception("<sprite> with no x declared");

			if (nodeMap.getNamedItem("y") != null)
				y = Double
						.parseDouble(nodeMap.getNamedItem("y").getNodeValue());
			else
				throw new Exception("<sprite> with no y declared");

			if (nodeMap.getNamedItem("w") != null)
				w = Double
						.parseDouble(nodeMap.getNamedItem("w").getNodeValue());
			else
				throw new Exception("<sprite> with no w declared");

			if (nodeMap.getNamedItem("h") != null)
				h = Double
						.parseDouble(nodeMap.getNamedItem("h").getNodeValue());
			else
				throw new Exception("<sprite> with no h declared");

			if (nodeMap.getNamedItem("scaling") != null)
				scaling = Double.parseDouble(nodeMap.getNamedItem("scaling")
						.getNodeValue());

			// New Sprite is created
			s = new Sprite(sheet, x, y, w, h, scaling);

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				sprites.put(name, s);
			}

		} else
			throw new Exception("<sprite> with no attributes declared");

		return s;
	}

	/**
	 * This method process and <sprite-label> tag, check the wiki to know the
	 * format
	 * 
	 * @param node
	 *            Sprite-label node
	 * @return Generated SpriteLabel
	 * @throws Exception
	 */
	protected Label processSpriteLabel(Node node) throws Exception {

		Label l = null;
		Sprite s = null;
		Vertex v = null;

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("sprite-label-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!labels.containsKey(ref))
					throw new Exception(
							"<sprite-label> or <text-label> with name:" + ref
									+ " is not declared");

				return labels.get(ref);
			}
		}

		// Reading mandatory content
		boolean hasVertex = false;
		boolean hasSprite = false;

		if (node.hasChildNodes()
				&& node.getChildNodes().getLength() == 2 * 2 + 1)
			for (int i = 1; i < 2 * 2 + 1; i += 2) {
				Node currentNode = node.getChildNodes().item(i);

				if (currentNode.getNodeName().equals("vertex")
						|| currentNode.getNodeName().equals("vertex-ref")) {
					v = processVertex(currentNode);
					hasVertex = true;
				}

				else if (currentNode.getNodeName().equals("sprite")
						|| currentNode.getNodeName().equals("sprite-ref")) {
					s = processSprite(currentNode);
					hasSprite = true;
				}
			}

		if (!hasVertex || !hasSprite)
			throw new Exception(
					"<sprite-label> requieres: <vertex> and <sprite>");

		// Checking attributes
		if (node.hasAttributes()) {

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();

				// New label is created
				l = new SpriteLabel(name, v, s);

				labels.put(name, l);
			} else
				throw new Exception(
						"<sprite-label> must have a 'name' attribute");

			if (nodeMap.getNamedItem("layer") != null) {
				int layer = Integer.parseInt(nodeMap.getNamedItem("layer")
						.getNodeValue());
				l.setLayer(layer);
			}

		} else
			throw new Exception("<sprite-label> with no attributes declared");

		return l;

	}

	/**
	 * This method process and <spritesheet> tag, check the wiki to know the
	 * format
	 * 
	 * @param node
	 *            Spritesheet node
	 * @return Generated SpriteSheet
	 * @throws Exception
	 */
	protected SpriteSheet processSpriteSheet(Node node) throws Exception {

		SpriteSheet s = null;
		String filename = "";

		NamedNodeMap nodeMap = node.getAttributes();

		// Checking attributes
		if (node.hasAttributes()) {

			if (nodeMap.getNamedItem("file") != null)
				filename = nodeMap.getNamedItem("file").getNodeValue();
			else
				throw new Exception("<spritesheet> with no file declared");

			// New SpriteSheet is created
			s = new SpriteSheet(filename);

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				sheets.put(name, s);
			} else
				throw new Exception("<spritesheet> with no name declared");

		} else
			throw new Exception("<spritesheet> with no attributes declared");

		return s;
	}

	/**
	 * This method process and <sound> tag, check the wiki to know the format
	 * 
	 * @param node
	 *            Sound node
	 * @return Generated SoundEffect
	 * @throws Exception
	 */
	protected SoundEffect processSound(Node node) throws Exception {

		SoundEffect s = null;
		String filename = "";

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("sound-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!sounds.containsKey(ref))
					throw new Exception("<sound> with name:" + ref
							+ " is not declared");

				return sounds.get(ref);
			}
		}

		// Checking attributes
		if (node.hasAttributes()) {

			if (nodeMap.getNamedItem("file") != null)
				filename = nodeMap.getNamedItem("file").getNodeValue();
			else
				throw new Exception("<sound> with no file declared");

			// New SoundEffect is created
			s = new SoundEffect(filename);

			if (nodeMap.getNamedItem("pitch") != null) {
				Float pitch = Float.parseFloat(nodeMap.getNamedItem("pitch")
						.getNodeValue());
				s.setPitch(pitch);
			}

			if (nodeMap.getNamedItem("gain") != null) {
				Float gain = Float.parseFloat(nodeMap.getNamedItem("gain")
						.getNodeValue());
				s.setGain(gain);
			}

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				sounds.put(name, s);
			} else
				throw new Exception("<sound> with no name declared");

		} else
			throw new Exception("<sound> with no attributes declared");

		return s;
	}

	/**
	 * This method process and <text-label> tag, check the wiki to know the
	 * format
	 * 
	 * @param node
	 *            Text-label node
	 * @return Generated TextLabel
	 * @throws Exception
	 */
	protected Label processTextLabel(Node node) throws Exception {

		Label l = null;
		String t = "";
		Vertex v = null;
		PseudoFont f = null;

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("text-label-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!labels.containsKey(ref))
					throw new Exception(
							"<sprite-label> or <text-label> with name:" + ref
									+ " is not declared");

				return labels.get(ref);
			}
		}

		// Reading mandatory content
		boolean hasVertex = false;
		boolean hasFont = false;

		if (node.hasChildNodes()
				&& node.getChildNodes().getLength() == 2 * 2 + 1)
			for (int i = 1; i < 2 * 2 + 1; i += 2) {
				Node currentNode = node.getChildNodes().item(i);

				if (currentNode.getNodeName().equals("vertex")
						|| currentNode.getNodeName().equals("vertex-ref")) {
					v = processVertex(currentNode);
					hasVertex = true;
				}

				else if (currentNode.getNodeName().equals("pseudo-font")
						|| currentNode.getNodeName().equals("pseudo-font-ref")) {
					f = processPseudoFont(currentNode);
					hasFont = true;
				}
			}

		if (!hasVertex || !hasFont)
			throw new Exception(
					"<sprite-label> requieres: <vertex> and <pseudo-font>");

		// Checking attributes
		if (node.hasAttributes()) {

			if (nodeMap.getNamedItem("text") != null) {
				t = nodeMap.getNamedItem("text").getNodeValue();
			}

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();

				// New label is created
				l = new TextLabel(name, v, t, f);

				labels.put(name, l);
			}

			if (nodeMap.getNamedItem("layer") != null) {
				int layer = Integer.parseInt(nodeMap.getNamedItem("layer")
						.getNodeValue());
				l.setLayer(layer);
			}

		} else
			throw new Exception("<sprite-label> must have a 'name' attribute");

		return l;

	}

	/**
	 * This method process and <vertex> tag, check the wiki to know the format
	 * 
	 * @param node
	 *            Vertex node
	 * @return Generated Vertex
	 * @throws Exception
	 */
	protected Vertex processVertex(Node node) throws Exception {

		Vertex v = new Vertex();

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("vertex-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!vertices.containsKey(ref))
					throw new Exception("<vertex> with name:" + ref
							+ " is not declared");

				return vertices.get(ref);
			}
		}

		// Checking attributes
		if (node.hasAttributes()) {

			if (nodeMap.getNamedItem("x") != null) {
				double x = Double.parseDouble(nodeMap.getNamedItem("x")
						.getNodeValue());
				v.setX(x);
			}

			if (nodeMap.getNamedItem("y") != null) {
				double y = Double.parseDouble(nodeMap.getNamedItem("y")
						.getNodeValue());
				v.setY(y);
			}

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				vertices.put(name, v);
			}
		}

		return v;
	}

	/**
	 * This method process and <wave> tag, check the wiki to know the format
	 * 
	 * @param node
	 *            Wave node
	 * @return Generated Wave
	 * @throws Exception
	 */
	protected Wave processWave(Node node) throws Exception {

		Wave w = null;
		Bullet b = null;
		Vertex v = null;
		Music m = null;
		SoundEffect s = null;
		ArrayList<DirectionModifier> mods = new ArrayList<DirectionModifier>();
		ArrayList<Wave> subWaves = new ArrayList<Wave>();

		NamedNodeMap nodeMap = node.getAttributes();

		// Check if it is a reference
		if (node.getNodeName().equals("wave-ref")) {
			if (nodeMap.getNamedItem("ref") != null) {
				String ref = nodeMap.getNamedItem("ref").getNodeValue();

				if (!waves.containsKey(ref))
					throw new Exception("<wave> with name:" + ref
							+ " is not declared");

				return waves.get(ref);
			}
		}

		// Reading mandatory content
		if (node.hasChildNodes())
			for (int i = 1; i < node.getChildNodes().getLength(); i += 2) {
				Node currentNode = node.getChildNodes().item(i);

				if (currentNode.getNodeName().equals("vertex")
						|| currentNode.getNodeName().equals("vertex-ref"))
					v = processVertex(currentNode);

				else if (currentNode.getNodeName().equals("bullet")
						|| currentNode.getNodeName().equals("bullet-ref"))
					b = processBullet(currentNode);

				else if (currentNode.getNodeName().equals("direction-modifier")
						|| currentNode.getNodeName().equals(
								"direction-modifier-ref"))
					mods.add(processDirectionModifier(currentNode));

				else if (currentNode.getNodeName().equals("wave")
						|| currentNode.getNodeName().equals("wave-ref"))
					subWaves.add(processWave(currentNode));

				else if (currentNode.getNodeName().equals("music")
						|| currentNode.getNodeName().equals("music-ref"))
					m = processMusic(currentNode);

				else if (currentNode.getNodeName().equals("sound")
						|| currentNode.getNodeName().equals("sound-ref"))
					m = processMusic(currentNode);
			}

		// Checking composed wave has ONLY ONE wave
		if (b == null) {
			if (mods.size() != 1)
				throw new Exception(
						"<wave> has not an unique <bullet> or various <wave> plus a <direction-modifier>");
			w = new Wave(subWaves, mods.get(0));
		}

		// Otherwise, it is a simple wave
		else {
			w = new Wave(b);
			w.setModifiers(mods);
		}

		w.setMusic(m);
		w.setSpawnSound(s);

		// Settting spawn spoint (if any)
		if (v != null)
			w.setSpawnPoint(v);

		// Checking attributes
		if (node.hasAttributes()) {

			if (nodeMap.getNamedItem("time-start") != null) {
				double timeStart = Double.parseDouble(nodeMap.getNamedItem(
						"time-start").getNodeValue());
				w.setTimeStart(timeStart);
			}

			if (nodeMap.getNamedItem("time-end") != null) {
				double timeEnd = Double.parseDouble(nodeMap.getNamedItem(
						"time-end").getNodeValue());
				w.setTimeEnd(timeEnd);
			}

			if (nodeMap.getNamedItem("bullets") != null) {
				int bullets = Integer.parseInt(nodeMap.getNamedItem("bullets")
						.getNodeValue());
				w.setBullets(bullets);
			}

			if (nodeMap.getNamedItem("waves") != null) {
				int waves = Integer.parseInt(nodeMap.getNamedItem("waves")
						.getNodeValue());
				w.setWaves(waves);
			}

			if (nodeMap.getNamedItem("repeat") != null) {
				int repeat = Integer.parseInt(nodeMap.getNamedItem("repeat")
						.getNodeValue());
				w.setRepeat(repeat);
			}

			if (nodeMap.getNamedItem("interval") != null) {
				double interval = Double.parseDouble(nodeMap.getNamedItem(
						"interval").getNodeValue());
				w.setInterval(interval);
			}

			if (nodeMap.getNamedItem("absolute") != null) {
				if (nodeMap.getNamedItem("absolute").getNodeValue()
						.equalsIgnoreCase("yes"))
					w.setAbsolute(true);
			}

			if (nodeMap.getNamedItem("exponent") != null) {
				double exponent = Double.parseDouble(nodeMap.getNamedItem(
						"exponent").getNodeValue());
				w.setTimeExponent(exponent);
			}

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				waves.put(name, w);
			}
		}

		return w;
	}
}
