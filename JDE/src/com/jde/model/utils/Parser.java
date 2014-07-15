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
import com.jde.model.entity.enemy.Enemy;
import com.jde.model.physics.Hitbox;
import com.jde.model.physics.Movement;
import com.jde.model.physics.Vertex;
import com.jde.model.stage.ArmyStage;
import com.jde.model.stage.Stage;
import com.jde.view.Game;
import com.jde.view.sprites.Sprite;

public class Parser {

	protected HashMap<String, Bullet> bullets;
	protected HashMap<String, Enemy> enemies;
	protected HashMap<String, Horde> hordes;
	protected HashMap<String, Sprite> sprites;
	protected HashMap<String, Movement> movements;
	protected HashMap<String, Vertex> vertices;

	protected ArrayList<Enemy> spawns;

	public Parser() {
		bullets = new HashMap<String, Bullet>();
		enemies = new HashMap<String, Enemy>();
		hordes = new HashMap<String, Horde>();
		sprites = new HashMap<String, Sprite>();
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
		Hitbox h = null;
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
						h = new Hitbox(); // TODO
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

	protected Enemy processEnemy(Node node) throws Exception {
		Enemy e = null;
		Hitbox hb = null;
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

			if (node.hasChildNodes() && node.getChildNodes().getLength() == 4*2 + 1)
				for (int i = 1; i < 4*2 + 1; i += 2) {
					Node currentNode = node.getChildNodes().item(i);
					if (currentNode.getNodeName().equals("hitbox")
							|| currentNode.getNodeName().equals("hitbox-ref"))
						hb = new Hitbox(); // TODO
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
				if (nodeMap.getNamedItem("repeat").getNodeValue().equalsIgnoreCase("yes"))
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

	protected Movement processMovement(Node node) throws Exception {
		Movement m = new Movement();

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

			if (nodeMap.getNamedItem("speed") != null) {
				double speed = Double.parseDouble(nodeMap.getNamedItem("speed")
						.getNodeValue());
				m.setSpeed(speed);
			}
			if (nodeMap.getNamedItem("angle") != null) {
				double angle = Double.parseDouble(nodeMap.getNamedItem("angle")
						.getNodeValue());
				m.setAngle(angle);
			}
			if (nodeMap.getNamedItem("acceleration") != null) {
				double acceleration = Double.parseDouble(nodeMap.getNamedItem("acceleration")
						.getNodeValue());
				m.setAcceleration(acceleration);
			}

			if (node.hasChildNodes()
					&& node.getChildNodes().getLength() == 1
					&& (node.getFirstChild().getNodeName().equals("vertex") || node
							.getFirstChild().getNodeName().equals("vertex-ref")))
				m.setPosition(processVertex(node.getFirstChild()));

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				movements.put(name, m);
			} else
				throw new Exception("<movement> with no name declared");

		} else
			throw new Exception("<movement> with no attributes declared");

		return m;
	}

	// TODO: provisional spawner for enemies
	protected void processSpawn(Node node) throws Exception {
		Enemy e = null;

		if (node.hasAttributes()) {
			NamedNodeMap nodeMap = node.getAttributes();

			if (node.hasChildNodes()
					&& node.getChildNodes().getLength() == 1*2 + 1
					&& (node.getChildNodes().item(1).getNodeName().equals("enemy") || node
							.getChildNodes().item(1).getNodeName().equals("enemy-ref")))
				e = processEnemy(node.getChildNodes().item(1)).clone();
			else
				throw new Exception("<spawn> has not an unique <enemy>");

			if (nodeMap.getNamedItem("time") != null) {
				double time = Double.parseDouble(nodeMap.getNamedItem("time")
						.getNodeValue());
				e.setSpawnTime(time);
			} else
				throw new Exception("<spawn> with no time declared");

			if (nodeMap.getNamedItem("x") != null) {
				double x = Double.parseDouble(nodeMap.getNamedItem("x")
						.getNodeValue());
				e.getMovement().getPosition().setX(x);
			} else
				throw new Exception("<spawn> with no x-coordinate declared");

			if (nodeMap.getNamedItem("y") != null) {
				double y = Double.parseDouble(nodeMap.getNamedItem("y")
						.getNodeValue());
				e.getMovement().getPosition().setY(y);
			} else
				throw new Exception("<spawn> with no y-coordinate declared");

			spawns.add(e);

		} else
			throw new Exception("<spawn> with no attributes declared");
	}

	protected Sprite processSprite(Node node) throws Exception {
		Sprite s = null;

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

			String image = "";

			if (nodeMap.getNamedItem("image") != null)
				image = nodeMap.getNamedItem("image").getNodeValue();
			else
				throw new Exception("<sprite> with no image declared");

			if (nodeMap.getNamedItem("name") != null) {
				String name = nodeMap.getNamedItem("name").getNodeValue();
				s = new Sprite(image);
				sprites.put(name, s);
				return s;
			} else
				throw new Exception("<sprite> with no name declared");

		} else
			throw new Exception("<sprite> with no attributes declared");
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
			} else
				throw new Exception("<vertex> with no name declared");

		} else
			throw new Exception("<vertex> with no attributes declared");

		return v;
	}
}
