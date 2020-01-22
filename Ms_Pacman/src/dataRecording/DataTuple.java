package dataRecording;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import pacman.game.Constants;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.STRATEGY;
import pacman.game.Game;

public class DataTuple {

	public enum DiscreteTag {
		VERY_LOW, LOW, MEDIUM, HIGH, VERY_HIGH, NONE;

		public static DiscreteTag DiscretizeDouble(double aux) {
			if (aux < 0.1)
				return DiscreteTag.VERY_LOW;
			else if (aux <= 0.3)
				return DiscreteTag.LOW;
			else if (aux <= 0.5)
				return DiscreteTag.MEDIUM;
			else if (aux <= 0.7)
				return DiscreteTag.HIGH;
			else
				return DiscreteTag.VERY_HIGH;
		}
	}

	// ====================
	// ADDED BY JQ
	//

	public Constants.STRATEGY strategy;
	public int closestGhostDist = -1;
	public int closestEdibleGhostDist = -1;
	public int closestPillDist = -1;
	public int closestPowerPillDist = -1;
	public MOVE closestGhostDir;
	public MOVE closestEdibleGhostDir;

	public HashMap<String, String> map; // <attribute, attributeValue>

	// ====================

	public MOVE directionChosen;

	// General game state this - not normalized!
	public int mazeIndex;
	public int currentLevel;
	public int pacmanPosition;
	public int pacmanLivesLeft;
	public int currentScore;
	public int totalGameTime;
	public int currentLevelTime;
	public int numOfPillsLeft;
	public int numOfPowerPillsLeft;

	// Ghost this, dir, dist, edible - BLINKY, INKY, PINKY, SUE
	public boolean isBlinkyEdible = false;
	public boolean isInkyEdible = false;
	public boolean isPinkyEdible = false;
	public boolean isSueEdible = false;

	public int blinkyDist = -1;
	public int inkyDist = -1;
	public int pinkyDist = -1;
	public int sueDist = -1;

	public MOVE blinkyDir;
	public MOVE inkyDir;
	public MOVE pinkyDir;
	public MOVE sueDir;

	// Util data - useful for normalization
	public int numberOfNodesInLevel;
	public int numberOfTotalPillsInLevel;
	public int numberOfTotalPowerPillsInLevel;
	private int maximumDistance = 150;

	public DataTuple(Game game, MOVE move) {
		if (move == MOVE.NEUTRAL) {
			move = game.getPacmanLastMoveMade();
		}

		strategy = game.strategy;

		this.mazeIndex = game.getMazeIndex();
		this.currentLevel = game.getCurrentLevel();
		this.pacmanPosition = game.getPacmanCurrentNodeIndex();
		this.pacmanLivesLeft = game.getPacmanNumberOfLivesRemaining();
		this.currentScore = game.getScore();
		this.totalGameTime = game.getTotalTime();
		this.currentLevelTime = game.getCurrentLevelTime();
		this.numOfPillsLeft = game.getNumberOfActivePills();
		this.numOfPowerPillsLeft = game.getNumberOfActivePowerPills();

		if (game.getGhostLairTime(GHOST.BLINKY) == 0) {
			this.isBlinkyEdible = game.isGhostEdible(GHOST.BLINKY);
			this.blinkyDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
					game.getGhostCurrentNodeIndex(GHOST.BLINKY));
		}

		if (game.getGhostLairTime(GHOST.INKY) == 0) {
			this.isInkyEdible = game.isGhostEdible(GHOST.INKY);
			this.inkyDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
					game.getGhostCurrentNodeIndex(GHOST.INKY));
		}

		if (game.getGhostLairTime(GHOST.PINKY) == 0) {
			this.isPinkyEdible = game.isGhostEdible(GHOST.PINKY);
			this.pinkyDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
					game.getGhostCurrentNodeIndex(GHOST.PINKY));
		}

		if (game.getGhostLairTime(GHOST.SUE) == 0) {
			this.isSueEdible = game.isGhostEdible(GHOST.SUE);
			this.sueDist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
					game.getGhostCurrentNodeIndex(GHOST.SUE));
		}

		this.blinkyDir = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),
				game.getGhostCurrentNodeIndex(GHOST.BLINKY), DM.PATH);
		this.inkyDir = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),
				game.getGhostCurrentNodeIndex(GHOST.INKY), DM.PATH);
		this.pinkyDir = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),
				game.getGhostCurrentNodeIndex(GHOST.PINKY), DM.PATH);
		this.sueDir = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),
				game.getGhostCurrentNodeIndex(GHOST.SUE), DM.PATH);

		this.numberOfNodesInLevel = game.getNumberOfNodes();
		this.numberOfTotalPillsInLevel = game.getNumberOfPills();
		this.numberOfTotalPowerPillsInLevel = game.getNumberOfPowerPills();

		int closestGhostDist = Collections.min(Arrays.asList(blinkyDist, inkyDist, pinkyDist, sueDist));

		// set the closest edible ghost
		if (closestGhostDist == blinkyDist && isBlinkyEdible) {
			closestEdibleGhostDist = blinkyDist;
			closestEdibleGhostDir = blinkyDir;
		} else if (closestGhostDist == inkyDist && isInkyEdible) {
			closestEdibleGhostDist = inkyDist;
			closestEdibleGhostDir = inkyDir;			
		} else if (closestGhostDist == pinkyDist && isPinkyEdible) {
			closestEdibleGhostDist = pinkyDist;
			closestEdibleGhostDir = pinkyDir;
		} else if (closestGhostDist == sueDist && isSueEdible) {
			closestEdibleGhostDist = sueDist;
			closestEdibleGhostDir = sueDir;
		}

		// set the closest non-edible ghost
		if (closestGhostDist == blinkyDist && !isBlinkyEdible) {
			this.closestGhostDist = blinkyDist;
			closestGhostDir = blinkyDir;
		} else if (closestGhostDist == inkyDist && !isInkyEdible) {
			this.closestGhostDist = inkyDist;
			closestGhostDir = inkyDir;
		} else if (closestGhostDist == pinkyDist && !isPinkyEdible) {
			this.closestGhostDist = pinkyDist;
			closestGhostDir = pinkyDir;
		} else if (closestGhostDist == sueDist && !isSueEdible) {
			this.closestGhostDist = sueDist;
			closestGhostDir = sueDir;
		}
		
		closestPowerPillDist = getClosestPowerPillDist(game);
		closestPillDist = getClosestPillDist(game);
	}	

	public DataTuple(String data) {
		String[] dataSplit = data.split(";");

		strategy = Constants.STRATEGY.valueOf(dataSplit[0]);

		this.directionChosen = MOVE.valueOf(dataSplit[1]);

		this.pacmanPosition = Integer.parseInt(dataSplit[2]);
		this.numOfPillsLeft = Integer.parseInt(dataSplit[3]);
		this.numOfPowerPillsLeft = Integer.parseInt(dataSplit[4]);
		closestGhostDir = MOVE.valueOf(dataSplit[5]);
		closestEdibleGhostDir = MOVE.valueOf(dataSplit[6]);
		closestPowerPillDist = Integer.parseInt(dataSplit[7]);
		closestPillDist = Integer.parseInt(dataSplit[8]);
		closestGhostDist = Integer.parseInt(dataSplit[9]);
		closestEdibleGhostDist = Integer.parseInt(dataSplit[10]);

		// this.DirectionChosen = MOVE.valueOf(dataSplit[0]);
		//
		// this.mazeIndex = Integer.parseInt(dataSplit[1]);
		// this.currentLevel = Integer.parseInt(dataSplit[2]); // remove
		// this.pacmanPosition = Integer.parseInt(dataSplit[3]);
		// this.pacmanLivesLeft = Integer.parseInt(dataSplit[4]);
		// this.currentScore = Integer.parseInt(dataSplit[5]); // remove
		// this.totalGameTime = Integer.parseInt(dataSplit[6]); // remove
		// this.currentLevelTime = Integer.parseInt(dataSplit[7]); // remove
		// this.numOfPillsLeft = Integer.parseInt(dataSplit[8]);
		// this.numOfPowerPillsLeft = Integer.parseInt(dataSplit[9]);
		// this.isBlinkyEdible = Boolean.parseBoolean(dataSplit[10]);
		// this.isInkyEdible = Boolean.parseBoolean(dataSplit[11]);
		// this.isPinkyEdible = Boolean.parseBoolean(dataSplit[12]);
		// this.isSueEdible = Boolean.parseBoolean(dataSplit[13]);
		// this.blinkyDist = Integer.parseInt(dataSplit[14]);
		// this.inkyDist = Integer.parseInt(dataSplit[15]);
		// this.pinkyDist = Integer.parseInt(dataSplit[16]);
		// this.sueDist = Integer.parseInt(dataSplit[17]);
		// this.blinkyDir = MOVE.valueOf(dataSplit[18]);
		// this.inkyDir = MOVE.valueOf(dataSplit[19]);
		// this.pinkyDir = MOVE.valueOf(dataSplit[20]);
		// this.sueDir = MOVE.valueOf(dataSplit[21]);
		// this.numberOfNodesInLevel = Integer.parseInt(dataSplit[22]);
		// this.numberOfTotalPillsInLevel = Integer.parseInt(dataSplit[23]);
		// this.numberOfTotalPowerPillsInLevel = Integer.parseInt(dataSplit[24]);
	}

	public String getSaveString() {
		StringBuilder stringbuilder = new StringBuilder();

		stringbuilder.append(strategy + ";");

		stringbuilder.append(this.directionChosen + ";");
		// stringbuilder.append(this.mazeIndex + ";");
		// stringbuilder.append(this.currentLevel + ";");
		stringbuilder.append(this.pacmanPosition + ";");
		// stringbuilder.append(this.pacmanLivesLeft + ";");
		// stringbuilder.append(this.currentScore + ";");
		// stringbuilder.append(this.totalGameTime + ";");
		// stringbuilder.append(this.currentLevelTime + ";");
		stringbuilder.append(this.numOfPillsLeft + ";");
		stringbuilder.append(this.numOfPowerPillsLeft + ";");
		// stringbuilder.append(this.isBlinkyEdible + ";");
		// stringbuilder.append(this.isInkyEdible + ";");
		// stringbuilder.append(this.isPinkyEdible + ";");
		// stringbuilder.append(this.isSueEdible + ";");
		// stringbuilder.append(this.blinkyDist + ";");
		// stringbuilder.append(this.inkyDist + ";");
		// stringbuilder.append(this.pinkyDist + ";");
		// stringbuilder.append(this.sueDist + ";");
//		stringbuilder.append(this.blinkyDir + ";");
//		stringbuilder.append(this.inkyDir + ";");
//		stringbuilder.append(this.pinkyDir + ";");
//		stringbuilder.append(this.sueDir + ";");
		// stringbuilder.append(this.numberOfNodesInLevel + ";");
		// stringbuilder.append(this.numberOfTotalPillsInLevel + ";");
		// stringbuilder.append(this.numberOfTotalPowerPillsInLevel + ";");
		stringbuilder.append(closestGhostDir + ";");
		stringbuilder.append(closestEdibleGhostDir + ";");
		stringbuilder.append(closestGhostDist + ";");
		stringbuilder.append(closestEdibleGhostDist + ";");

		return stringbuilder.toString();
	}
	
	private int getClosestPowerPillDist(Game game) {
		int[] indexArr = game.getActivePowerPillsIndices();
		if (indexArr.length == 0) {
			return -1;
		}
		int[] temp = new int[indexArr.length];

		for (int i = 0; i < indexArr.length; i++) {
			temp[i] = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexArr[i]);
		}
		return Arrays.stream(temp).min().getAsInt();
	}

	private int getClosestPillDist(Game game) {
		int[] indexArr = game.getActivePillsIndices();
		if (indexArr.length == 0) {
			return -1;
		}
		int[] temp = new int[indexArr.length];

		for (int i = 0; i < indexArr.length; i++) {
			temp[i] = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), indexArr[i]);
		}
		return Arrays.stream(temp).min().getAsInt();
	}

	/**
	 * Used to normalize distances. Done via min-max normalization. Supposes that
	 * minimum possible distance is 0. Supposes that the maximum possible distance
	 * is 150.
	 * 
	 * @param dist
	 *            Distance to be normalized
	 * @return Normalized distance
	 */
	public double normalizeDistance(int dist) {
		return ((dist - 0) / (double) (this.maximumDistance - 0)) * (1 - 0) + 0;
	}

	public DiscreteTag discretizeDistance(int dist) {
		if (dist == -1)
			return DiscreteTag.NONE;
		double aux = this.normalizeDistance(dist);
		return DiscreteTag.DiscretizeDouble(aux);
	}

	public double normalizeLevel(int level) {
		return ((level - 0) / (double) (Constants.NUM_MAZES - 0)) * (1 - 0) + 0;
	}

	public double normalizePosition(int position) {
		return ((position - 0) / (double) (this.numberOfNodesInLevel - 0)) * (1 - 0) + 0;
	}

	public DiscreteTag discretizePosition(int pos) {
		double aux = this.normalizePosition(pos);
		return DiscreteTag.DiscretizeDouble(aux);
	}

	public double normalizeBoolean(boolean bool) {
		if (bool) {
			return 1.0;
		} else {
			return 0.0;
		}
	}

	public double normalizeNumberOfPills(int numOfPills) {
		return ((numOfPills - 0) / (double) (this.numberOfTotalPillsInLevel - 0)) * (1 - 0) + 0;
	}

	public DiscreteTag discretizeNumberOfPills(int numOfPills) {
		double aux = this.normalizeNumberOfPills(numOfPills);
		return DiscreteTag.DiscretizeDouble(aux);
	}

	public double normalizeNumberOfPowerPills(int numOfPowerPills) {
		return ((numOfPowerPills - 0) / (double) (this.numberOfTotalPowerPillsInLevel - 0)) * (1 - 0) + 0;
	}

	public DiscreteTag discretizeNumberOfPowerPills(int numOfPowerPills) {
		double aux = this.normalizeNumberOfPowerPills(numOfPowerPills);
		return DiscreteTag.DiscretizeDouble(aux);
	}

	public double normalizeTotalGameTime(int time) {
		return ((time - 0) / (double) (Constants.MAX_TIME - 0)) * (1 - 0) + 0;
	}

	public DiscreteTag discretizeTotalGameTime(int time) {
		double aux = this.normalizeTotalGameTime(time);
		return DiscreteTag.DiscretizeDouble(aux);
	}

	public double normalizeCurrentLevelTime(int time) {
		return ((time - 0) / (double) (Constants.LEVEL_LIMIT - 0)) * (1 - 0) + 0;
	}

	public DiscreteTag discretizeCurrentLevelTime(int time) {
		double aux = this.normalizeCurrentLevelTime(time);
		return DiscreteTag.DiscretizeDouble(aux);
	}

	/**
	 * 
	 * Max score value lifted from highest ranking PacMan controller on PacMan vs
	 * Ghosts website: http://pacman-vs-ghosts.net/controllers/1104
	 * 
	 * @param score
	 * @return
	 */
	public double normalizeCurrentScore(int score) {
		return ((score - 0) / (double) (82180 - 0)) * (1 - 0) + 0;
	}

	public DiscreteTag discretizeCurrentScore(int score) {
		double aux = this.normalizeCurrentScore(score);
		return DiscreteTag.DiscretizeDouble(aux);
	}

	/**
	 * Discretize the given attribute value and returns it as a string.
	 * 
	 * @param attrValue
	 *            the attribute value to discretize
	 * @return a string of the discretized attribute value or null as default.
	 */
	public String discretizeAttrValue(String attrValue) {
		switch (attrValue) {
		case "strategy":
			return strategy.toString();
		case "closestGhostDist":
			return discretizeDistance(closestGhostDist).toString();
		case "closestEdibleGhostDist":
			return discretizeDistance(closestEdibleGhostDist).toString();
		case "closestPowerPillDist":
			return discretizeDistance(closestPowerPillDist).toString();
		case "closestPillDist":
			return discretizeDistance(closestPillDist).toString();
		case "numOfPillsLeft":
			return discretizeNumberOfPills(numOfPillsLeft).toString();
		case "numOfPowerPillsLeft":
			return discretizeNumberOfPowerPills(numOfPowerPillsLeft).toString();

		default:
			return null;
		}

	}

	/**
	 * Creates a hashmap and fills it if it does not exist, else returns the
	 * hashmap.
	 * 
	 * @return the hashmap
	 */
	public HashMap<String, String> getHashMap() {
		if (map == null) {
			map = new HashMap<>();
			// make sure attribute names are correct
			String[] strArr = { "strategy", "closestGhostDist", "closestEdibleGhostDist", "closestPowerPillDist",
					"closestPillDist", "closestEdibleGhostDir", "closestGhostDir", "directionChosen",
					"pacmanPosition", "numOfPillsLeft", "numOfPowerPillsLeft" };

			for (String str : strArr) {
				map.put(str, discretizeAttrValue(str));
			}
		}
		return map;
	}
}
