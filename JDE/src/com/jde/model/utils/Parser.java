package com.jde.model.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jde.model.entity.bullet.Bullet;
import com.jde.model.entity.bullet.Horde;
import com.jde.model.entity.bullet.Wave;
import com.jde.model.entity.enemy.Enemy;
import com.jde.model.physics.Direction;
import com.jde.model.physics.DirectionModifier;
import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;
import com.jde.model.physics.collision.HitBox;
import com.jde.model.physics.collision.HitZone;
import com.jde.model.stage.ArmyStage;
import com.jde.model.stage.Stage;
import com.jde.view.Game;
import com.jde.view.sprites.Sprite;
import com.jde.view.sprites.SpriteSheet;

public class Parser {

	protected HashMap<String, Bullet> bullets;
	protected HashMap<String, Direction> directions;
	protected HashMap<String, DirectionModifier> modifiers;
	protected HashMap<String, Enemy> enemies;
	protected HashMap<String, Horde> hordes;
	protected HashMap<String, Sprite> sprites;
	protected HashMap<String, SpriteSheet> sheets;
	protected HashMap<String, Movement> movements;
	protected HashMap<String, Vertex> vertices;
	protected HashMap<String, Wave> waves;

	protected ArrayList<Enemy> spawns;

	public Parser() {
		bullets = new HashMap<String, Bullet>();
		directions = new HashMap<String, Direction>();
		modifiers = new HashMap<String, DirectionModifier>();
		enemies = new HashMap<String, Enemy>();
		hordes = new HashMap<String, Horde>();
		sprites = new HashMap<String, Sprite>();
		sheets = new HashMap<String, SpriteSheet>();
		movements = new HashMap<String, Movement>();
		vertices = new HashMap<String, Vertex>();
		waves = new HashMap<String, Wave>();

		spawns = new ArrayList<Enemy>();
	}

	public Game parseXML(String filename) {

		// xml parsing
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
		}

		ArrayList<Stage> stages = new ArrayList<Stage>();
		stages.add(new ArmyStage(spawns));
		return new Game(stages);
	}

	protected void parseNodeList(NodeList nodeList) throws Exception {

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE)
				parseNode(node);
		}

	}

	protected void parseNode(Node node) throws Exception {

		switch (node.getNodeName()) {

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

		case "hitbox":
		case "hitbox-ref":
			// TODO
			break;

		case "horde":
		case "horde-ref":
			processHorde(node);
			break;

		case "import":
			processImport(node);
			break;

		case "movement":
		case "movement-ref":
			processMovement(node);
			break;

		case "spawn":
			processSpawn(node);
			break;

		case "sprite":
		case "sprite-ref":
			processSprite(node);
			break;

		case "spritesheet":
			processSpriteSheet(node);
			break;

		case "stage":
		case "stage-ref":
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

	protected Bullet processBullet(Node node) throws Exception {
		Bullet b = null;
		HitZone h = null;
		Sprite s = null;
		Movement m = null;

		if (node.hasAttributes()) {
			NamedNodeMap nodeMap = node.getAttributes();

			if (node.getNodeName().equals("bullet-ref")) {
				if (nodeMap.getNamedItem("ref") != null) {
					String ref = nodeMap.getNamedItem("ref").getNodeValue();

					if (!bullets.containsKey(ref))
						throw new Exception("<bullet> with name:" + ref
								+ " is not declared");

					return bullets.get(ref);
				}
			}

			if (node.hasChildNodes()
					&& node.getChildNodes().getLength() == 3 * 2 + 1)
				for (int i = 1; i < 3 * 2 + 1; i += 2) {
					Node currentNode = node.getChildNodes().item(i);
					if (currentNode.getNodeName().equals("hitbox")
							|| currentNode.getNodeName().equals("hitbox-ref"))
						h = new HitBox(); // TODO
					else if (currentNode.getNodeName().equals("movement")
							|| currentNode.getNodeName().equals("movement-ref"))
						m = processMovement(currentNode);
					else if (currentNode.getNodeName().equals("sprite")
							|| currentNode.getNodeName().equals("sprite-ref"))
						s = processSprite(currentNode);
				}
			else
				throw new Exception(
						"<bullet> requieres: <hitbox>, <movement> and <sprite>");

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				b = new Bullet(s, h, m);
				bullets.put(name, b);
			} else
				throw new Exception("<bullet> with no name declared");

		} else
			throw new Exception("<bullet> with no attributes declared");

		return b;
	}

	protected Direction processDirection(Node node) throws Exception {
		Direction d = null;
		Vertex hv = null;

		if (node.hasAttributes()) {
			NamedNodeMap nodeMap = node.getAttributes();

			if (node.getNodeName().equals("direction-ref")) {
				if (nodeMap.getNamedItem("ref") != null) {
					String ref = nodeMap.getNamedItem("ref").getNodeValue();

					if (!directions.containsKey(ref))
						throw new Exception("<direction> with name:" + ref
								+ " is not declared");

					return directions.get(ref);
				}
			}

			if (node.hasChildNodes()
					&& node.getChildNodes().getLength() == 1 * 2 + 1) {
				for (int i = 1; i < 1 * 2 + 1; i += 2) {
					Node currentNode = node.getChildNodes().item(i);
					if (currentNode.getNodeName().equals("vertex")
							|| currentNode.getNodeName().equals("vertex-ref"))
						hv = unvirtualizeCoordinates(processVertex(currentNode));
				}
				d = new Direction(hv);
			} else {
				double angle = 0;
				if (nodeMap.getNamedItem("angle") != null) {
					angle = Double.parseDouble(nodeMap.getNamedItem(
							"angle").getNodeValue());
				}
				d = new Direction(angle);
			}

			if (nodeMap.getNamedItem("rotation") != null) {
				double rotation = Double.parseDouble(nodeMap.getNamedItem("rotation")
						.getNodeValue());
				d.setRotation(rotation);
			}
			if (nodeMap.getNamedItem("speed") != null) {
				double speed = Double.parseDouble(nodeMap.getNamedItem("speed")
						.getNodeValue());
				d.setSpeed(speed);
			}
			if (nodeMap.getNamedItem("acceleration") != null) {
				double acceleration = Double.parseDouble(nodeMap.getNamedItem(
						"acceleration").getNodeValue());
				d.setAcceleration(acceleration);
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

		} else
			throw new Exception("<direction> with no attributes declared");

		return d;
	}
	
	protected DirectionModifier processDirectionModifier(Node node) throws Exception {
		DirectionModifier d = new DirectionModifier();

		if (node.hasAttributes()) {
			NamedNodeMap nodeMap = node.getAttributes();

			if (node.getNodeName().equals("direction-modifier-ref")) {
				if (nodeMap.getNamedItem("ref") != null) {
					String ref = nodeMap.getNamedItem("ref").getNodeValue();

					if (!modifiers.containsKey(ref))
						throw new Exception("<direction-modifier> with name:" + ref
								+ " is not declared");

					return modifiers.get(ref);
				}
			}

			if (nodeMap.getNamedItem("anglestart") != null) {
				double angleStart = Double.parseDouble(nodeMap.getNamedItem(
						"anglestart").getNodeValue());
				d.setAngleStart(angleStart);
			}
			if (nodeMap.getNamedItem("angleend") != null) {
				double angleEnd = Double.parseDouble(nodeMap.getNamedItem(
						"angleend").getNodeValue());
				d.setAngleEnd(angleEnd);
			}
			if (nodeMap.getNamedItem("rotationstart") != null) {
				double rotationStart = Double.parseDouble(nodeMap.getNamedItem(
						"rotationstart").getNodeValue());
				d.setRotationStart(rotationStart);
			}
			if (nodeMap.getNamedItem("rotationend") != null) {
				double rotationEnd = Double.parseDouble(nodeMap.getNamedItem(
						"rotationend").getNodeValue());
				d.setRotationEnd(rotationEnd);
			}
			if (nodeMap.getNamedItem("speedstart") != null) {
				double speedStart = Double.parseDouble(nodeMap.getNamedItem(
						"speedstart").getNodeValue());
				d.setSpeedStart(speedStart);
			}
			if (nodeMap.getNamedItem("speedend") != null) {
				double speedEnd = Double.parseDouble(nodeMap.getNamedItem(
						"speedend").getNodeValue());
				d.setSpeedEnd(speedEnd);
			}
			if (nodeMap.getNamedItem("accelerationstart") != null) {
				double accelerationStart = Double.parseDouble(nodeMap.getNamedItem(
						"accelerationstart").getNodeValue());
				d.setAccelerationStart(accelerationStart);
			}
			if (nodeMap.getNamedItem("accelerationend") != null) {
				double accelerationEnd = Double.parseDouble(nodeMap.getNamedItem(
						"accelerationend").getNodeValue());
				d.setAccelerationEnd(accelerationEnd);
			}

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				modifiers.put(name, d);
			}

		}

		return d;
	}

	protected Enemy processEnemy(Node node) throws Exception {
		Enemy e = null;
		HitZone hb = null;
		Sprite s = null;
		Movement m = null;
		Horde hd = null;

		if (node.hasAttributes()) {
			NamedNodeMap nodeMap = node.getAttributes();

			if (node.getNodeName().equals("enemy-ref")) {
				if (nodeMap.getNamedItem("ref") != null) {
					String ref = nodeMap.getNamedItem("ref").getNodeValue();

					if (!enemies.containsKey(ref))
						throw new Exception("<enemy> with name:" + ref
								+ " is not declared");

					return enemies.get(ref);
				}
			}

			if (node.hasChildNodes()
					&& node.getChildNodes().getLength() == 4 * 2 + 1)
				for (int i = 1; i < 4 * 2 + 1; i += 2) {
					Node currentNode = node.getChildNodes().item(i);
					if (currentNode.getNodeName().equals("hitbox")
							|| currentNode.getNodeName().equals("hitbox-ref"))
						hb = new HitBox(); // TODO
					else if (currentNode.getNodeName().equals("movement")
							|| currentNode.getNodeName().equals("movement-ref"))
						m = processMovement(currentNode);
					else if (currentNode.getNodeName().equals("sprite")
							|| currentNode.getNodeName().equals("sprite-ref"))
						s = processSprite(currentNode);
					else if (currentNode.getNodeName().equals("horde")
							|| currentNode.getNodeName().equals("horde-ref"))
						hd = processHorde(currentNode);
				}
			else
				throw new Exception(
						"<enemy> requieres: <hitbox>, <movement>, <horde> and <sprite>");

			if (nodeMap.getNamedItem("health") != null) {
				double health = Double.parseDouble(nodeMap.getNamedItem(
						"health").getNodeValue());
				e = new Enemy(s, hb, m, hd, health);
			} else
				throw new Exception("<enemy> with no health declared");

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
	protected void processGame(Node node) throws Exception {

		if (node.hasChildNodes())
			for (int i = 1; i < node.getChildNodes().getLength(); i += 2) {
				Node currentNode = node.getChildNodes().item(i);
				if (currentNode.getNodeName().equals("spawn"))
					processSpawn(currentNode);
				else
					throw new Exception("<game> must contains only <spawn>");
			}
	}

	protected Horde processHorde(Node node) throws Exception {
		Horde h = null;
		ArrayList<Wave> waves = new ArrayList<Wave>();

		if (node.hasAttributes()) {
			NamedNodeMap nodeMap = node.getAttributes();

			if (node.getNodeName().equals("horde-ref")) {
				if (nodeMap.getNamedItem("ref") != null) {
					String ref = nodeMap.getNamedItem("ref").getNodeValue();

					if (!hordes.containsKey(ref))
						throw new Exception("<horde> with name:" + ref
								+ " is not declared");

					return hordes.get(ref);
				}
			}

			if (node.hasChildNodes())
				for (int i = 1; i < node.getChildNodes().getLength(); i += 2) {
					Node currentNode = node.getChildNodes().item(i);
					if (currentNode.getNodeName().equals("wave")
							|| currentNode.getNodeName().equals("wave-ref"))
						waves.add(processWave(currentNode));
				}
			else
				throw new Exception("<horde> requieres <wave>");

			h = new Horde(waves);

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				hordes.put(name, h);
				return h;
			}

		} else
			throw new Exception("<horde> with no attributes declared");

		return h;
	}

	protected void processImport(Node node) throws Exception {
		if (node.hasAttributes()) {
			NamedNodeMap nodeMap = node.getAttributes();

			if (nodeMap.getNamedItem("file") != null) {
				String filename = nodeMap.getNamedItem("file").getNodeValue();

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

			} else
				throw new Exception("<import> with no file declared");

		} else
			throw new Exception("<import> with no attributes declared");
	}

	protected Movement processMovement(Node node) throws Exception {
		Movement m = null;
		Vertex pos = new Vertex();
		ArrayList<Direction> dirs = new ArrayList<Direction>();

		if (node.hasAttributes()) {
			NamedNodeMap nodeMap = node.getAttributes();

			if (node.getNodeName().equals("movement-ref")) {
				if (nodeMap.getNamedItem("ref") != null) {
					String ref = nodeMap.getNamedItem("ref").getNodeValue();

					if (!movements.containsKey(ref))
						throw new Exception("<movement> with name:" + ref
								+ " is not declared");

					return movements.get(ref);
				}
			}

			if (node.hasChildNodes())
				for (int i = 1; i < node.getChildNodes().getLength(); i += 2) {
					Node currentNode = node.getChildNodes().item(i);
					if (currentNode.getNodeName().equals("vertex")
							|| currentNode.getNodeName().equals("vertex-ref"))
						pos = processVertex(currentNode);
					else if (currentNode.getNodeName().equals("direction")
							|| currentNode.getNodeName()
									.equals("direction-ref"))
						dirs.add(processDirection(currentNode));
				}

			if (dirs.size() == 0)
				throw new Exception("<movement> has no directions");

			m = new Movement(pos, dirs);

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				movements.put(name, m);
			}

		} else
			throw new Exception("<movement> with no attributes declared");

		return m;
	}

	// TODO: provisional spawner for enemies
	protected void processSpawn(Node node) throws Exception {
		Enemy e = null;
		Vertex pos = null;

		if (node.hasAttributes()) {
			NamedNodeMap nodeMap = node.getAttributes();

			if (node.hasChildNodes()
					&& node.getChildNodes().getLength() == 2 * 2 + 1)
				for (int i = 1; i < 2 * 2 + 1; i += 2) {
					Node currentNode = node.getChildNodes().item(i);
					if (currentNode.getNodeName().equals("vertex")
							|| currentNode.getNodeName().equals("vertex-ref"))
						pos = processVertex(currentNode);
					else if (currentNode.getNodeName().equals("enemy")
							|| currentNode.getNodeName().equals("enemy-ref"))
						e = processEnemy(currentNode).clone();
				}
			else
				throw new Exception("<spawn> requieres: <vertex> and <enemy>");

			if (nodeMap.getNamedItem("time") != null) {
				double time = Double.parseDouble(nodeMap.getNamedItem("time")
						.getNodeValue());
				e.setSpawnTime(time);
			} else
				throw new Exception("<spawn> with no time declared");

			pos = unvirtualizeCoordinates(pos);
			e.getMovement().getPosition().setX(pos.getX());
			e.getMovement().getPosition().setY(pos.getY());
			spawns.add(e);

		} else
			throw new Exception("<spawn> with no attributes declared");
	}

	protected Sprite processSprite(Node node) throws Exception {
		Sprite s = null;
		SpriteSheet sheet = null;
		double x, y, w, h;
		double scaling = 1;

		if (node.hasAttributes()) {
			NamedNodeMap nodeMap = node.getAttributes();

			if (node.getNodeName().equals("sprite-ref")) {
				if (nodeMap.getNamedItem("ref") != null) {
					String ref = nodeMap.getNamedItem("ref").getNodeValue();

					if (!sprites.containsKey(ref))
						throw new Exception("<sprite> with name:" + ref
								+ " is not declared");

					return sprites.get(ref);
				}
			}

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

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				s = new Sprite(sheet, x, y, w, h, scaling);
				sprites.put(name, s);
				return s;
			} else
				throw new Exception("<sprite> with no name declared");

		} else
			throw new Exception("<sprite> with no attributes declared");
	}

	protected SpriteSheet processSpriteSheet(Node node) throws Exception {
		SpriteSheet s = null;

		if (node.hasAttributes()) {
			NamedNodeMap nodeMap = node.getAttributes();

			String filename = "";

			if (nodeMap.getNamedItem("file") != null)
				filename = nodeMap.getNamedItem("file").getNodeValue();
			else
				throw new Exception("<spritesheet> with no file declared");

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				s = new SpriteSheet(filename);
				sheets.put(name, s);
				return s;
			} else
				throw new Exception("<spritesheet> with no name declared");

		} else
			throw new Exception("<spritesheet> with no attributes declared");
	}

	protected Vertex processVertex(Node node) throws Exception {
		Vertex v = new Vertex();

		if (node.hasAttributes()) {
			NamedNodeMap nodeMap = node.getAttributes();

			if (node.getNodeName().equals("vertex-ref")) {
				if (nodeMap.getNamedItem("ref") != null) {
					String ref = nodeMap.getNamedItem("ref").getNodeValue();

					if (!vertices.containsKey(ref))
						throw new Exception("<vertex> with name:" + ref
								+ " is not declared");

					return vertices.get(ref);
				}
			}

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

		} else
			throw new Exception("<vertex> with no attributes declared");

		return v;
	}

	protected Wave processWave(Node node) throws Exception {
		Wave w = null;
		Bullet b = null;
		Vertex v = null;
		ArrayList<DirectionModifier> mods = new ArrayList<DirectionModifier>();

		if (node.hasAttributes()) {
			NamedNodeMap nodeMap = node.getAttributes();

			if (node.getNodeName().equals("wave-ref")) {
				if (nodeMap.getNamedItem("ref") != null) {
					String ref = nodeMap.getNamedItem("ref").getNodeValue();

					if (!waves.containsKey(ref))
						throw new Exception("<wave> with name:" + ref
								+ " is not declared");

					return waves.get(ref);
				}
			}

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
							|| currentNode.getNodeName().equals("direction-modifier-ref"))
						mods.add(processDirectionModifier(currentNode));
				}

			if (b == null)
				throw new Exception("<wave> has not an unique <bullet>");

			w = new Wave(b);
			w.setModifiers(mods);
			if (v != null)
				w.setSpawnPoint(v);

			if (nodeMap.getNamedItem("timestart") != null) {
				double timeStart = Double.parseDouble(nodeMap.getNamedItem(
						"timestart").getNodeValue());
				w.setTimeStart(timeStart);
			}
			if (nodeMap.getNamedItem("timeend") != null) {
				double timeEnd = Double.parseDouble(nodeMap.getNamedItem(
						"timeend").getNodeValue());
				w.setTimeEnd(timeEnd);
			}
			if (nodeMap.getNamedItem("bullets") != null) {
				int bullets = Integer.parseInt(nodeMap.getNamedItem(
						"bullets").getNodeValue());
				w.setBullets(bullets);
			}
			if (nodeMap.getNamedItem("repeat") != null) {
				if (nodeMap.getNamedItem("repeat").getNodeValue()
						.equalsIgnoreCase("yes"))
					w.setRepeat(true);
			}
			if (nodeMap.getNamedItem("interval") != null) {
				double interval = Double.parseDouble(nodeMap.getNamedItem(
						"interval").getNodeValue());
				w.setInterval(interval);
			}

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				waves.put(name, w);
				return w;
			}

		} else
			throw new Exception("<wave> with no attributes declared");

		return w;
	}

	protected Vertex unvirtualizeCoordinates(Vertex v) {
		return v.add(new Vertex(223, 240));
	}
}
