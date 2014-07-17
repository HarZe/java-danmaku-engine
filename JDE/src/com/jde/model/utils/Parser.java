package com.jde.model.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jde.model.entity.bullet.Bullet;
import com.jde.model.entity.bullet.Horde;
import com.jde.model.entity.enemy.Enemy;
import com.jde.model.physics.Direction;
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
	protected HashMap<String, Enemy> enemies;
	protected HashMap<String, Horde> hordes;
	protected HashMap<String, Sprite> sprites;
	protected HashMap<String, SpriteSheet> sheets;
	protected HashMap<String, Movement> movements;
	protected HashMap<String, Vertex> vertices;

	protected ArrayList<Enemy> spawns;

	public Parser() {
		bullets = new HashMap<String, Bullet>();
		directions = new HashMap<String, Direction>();
		enemies = new HashMap<String, Enemy>();
		hordes = new HashMap<String, Horde>();
		sprites = new HashMap<String, Sprite>();
		sheets = new HashMap<String, SpriteSheet>();
		movements = new HashMap<String, Movement>();
		vertices = new HashMap<String, Vertex>();

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
		Direction d = new Direction();

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

			if (nodeMap.getNamedItem("speed") != null) {
				double speed = Double.parseDouble(nodeMap.getNamedItem("speed")
						.getNodeValue());
				d.setSpeed(speed);
			}
			if (nodeMap.getNamedItem("angle") != null) {
				double angle = Double.parseDouble(nodeMap.getNamedItem("angle")
						.getNodeValue());
				d.setAngle(angle);
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

			if (node.hasChildNodes()
					&& node.getChildNodes().getLength() == 1 * 2 + 1
					&& (node.getChildNodes().item(1).getNodeName()
							.equals("bullet") || node.getChildNodes().item(1)
							.getNodeName().equals("bullet-ref")))
				h = new Horde(processBullet(node.getChildNodes().item(1)));
			else
				throw new Exception("<horde> has not an unique <bullet>");

			if (nodeMap.getNamedItem("anglestart") != null) {
				double angleStart = Double.parseDouble(nodeMap.getNamedItem(
						"anglestart").getNodeValue());
				h.setAngleStart(angleStart);
			}
			if (nodeMap.getNamedItem("angleend") != null) {
				double angleEnd = Double.parseDouble(nodeMap.getNamedItem(
						"angleend").getNodeValue());
				h.setAngleEnd(angleEnd);
			}
			if (nodeMap.getNamedItem("timestart") != null) {
				double timeStart = Double.parseDouble(nodeMap.getNamedItem(
						"timestart").getNodeValue());
				h.setTimeStart(timeStart);
			}
			if (nodeMap.getNamedItem("timeend") != null) {
				double timeEnd = Double.parseDouble(nodeMap.getNamedItem(
						"timeend").getNodeValue());
				h.setTimeEnd(timeEnd);
			}
			if (nodeMap.getNamedItem("bullets") != null) {
				double bullets = Double.parseDouble(nodeMap.getNamedItem(
						"bullets").getNodeValue());
				h.setBullets(bullets);
			}
			if (nodeMap.getNamedItem("repeat") != null) {
				if (nodeMap.getNamedItem("repeat").getNodeValue()
						.equalsIgnoreCase("yes"))
					h.setRepeat(true);
			}
			if (nodeMap.getNamedItem("interval") != null) {
				double interval = Double.parseDouble(nodeMap.getNamedItem(
						"interval").getNodeValue());
				h.setInterval(interval);
			}

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				hordes.put(name, h);
			} else
				throw new Exception("<movement> with no name declared");

		} else
			throw new Exception("<movement> with no attributes declared");

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
		LinkedList<Direction> dirs = new LinkedList<Direction>();

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
						e = processEnemy(currentNode);
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

	protected Vertex unvirtualizeCoordinates(Vertex v) {
		return v.add(new Vertex(223, 240));
	}
}
