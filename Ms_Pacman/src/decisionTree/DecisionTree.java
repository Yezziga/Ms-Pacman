package decisionTree;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import dataRecording.Dataset;
import pacman.controllers.Controller;
import pacman.entries.pacman.MyPacMan;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.STRATEGY;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * The decision tree trained with the ID3 algorithm, with the extension of calculating gain ratio.
 * @author Jessica
 *
 */
public class DecisionTree extends Controller<MOVE> {

	private Node root;
	/**
	 * Label to classify
	 */
	private String classLabel;
	private Dataset dataset;
	/**
	 * Name of attributes
	 */
	private ArrayList<String> listAttibutes;
	public int numberOfCorrectDecisions;
	public int totalDecisions;

	/**
	 * Constructs a decision tree object with the class label as "strategy". Loads a
	 * dataset on which to train and predict from.
	 */
	public DecisionTree() {

		classLabel = "strategy";
		dataset = new Dataset(DataSaverLoader.LoadPacManData());
		listAttibutes = new ArrayList<String>(dataset.getMap().keySet());
		listAttibutes.remove(classLabel);
	}

	public void buildTree() {
		root = generateTree(dataset, listAttibutes);
	}

	/**
	 * Recursive method which generates the decision tree.
	 * 
	 * @param dataset
	 *            the set of data to create a decision tree for
	 * @param attributeList
	 *            the list of attributes
	 * @return
	 */
	public Node generateTree(Dataset dataset, ArrayList<String> attributeList) {

		// Step 1
		Node node = null;

		// Step 2
		if (allTuplesSameClass(dataset)) {
			// make node as a leaf node labelled as C.
			node = new Node(dataset.getTuples()[0].strategy.toString());
			return node;
		}
		// Step 3
		else if (attributeList.isEmpty()) {
			// make node as a leaf node labelled with the majority class in D.
			node = new Node(getMajorityClass(dataset));
			return node;
		}
		// Step 4
		else {
			// Step 4.1 - choose the current attribute A through attribute selection
			String attributeA = attributeSelection(dataset.getTuples(), attributeList);

			// Step 4.2 - label node as attributeA & remove from attributeList
			node = new Node(attributeA);
			attributeList.remove(attributeA);

			// Step 4.3 - for each value in attributeA
			HashMap<String, Integer> map = dataset.getMap().get(attributeA);
			ArrayList<String> attributeAValues = new ArrayList<>(map.keySet());
			for (String attributeValue : attributeAValues) {

				ArrayList<String> attrList = (ArrayList) attributeList.clone();
				// a) separate all tuples in dataset so attributeA takes the value
				// attributeValue (aj)
				// and creates a subset (subsetDj)
				Dataset subsetDj = dataset.getSubDataset(attributeA, attributeValue);

				// b) if the subset is empty, add a child node to N labelled with the
				// majority class in dataset
				if (subsetDj.getTuples().length == 0) {
					Node child = new Node(getMajorityClass(dataset));
					node.addChild(attributeValue, child);
				}
				// c) otherwise, add the resulting node from calling generateTree(dataset,
				// attribute)
				// as a child node to N
				else {
					node.addChild(attributeValue, generateTree(subsetDj, attrList));
				}
			}
			Node defaultNode = new Node(getMajorityClass(dataset));
			node.addChild("default", defaultNode);
			// Step 4.4 - return N.
			return node;
		}
	}

	/**
	 * Checks if all the tuples in the dataset has the same class.
	 * 
	 * @param dataset
	 *            the dataset in which to check
	 * @return true if all tuples have the same class, otherwise false
	 */
	private boolean allTuplesSameClass(Dataset dataset) {
		STRATEGY str = dataset.getTuples()[0].strategy;
		for (DataTuple tuple : dataset.getTuples()) {
			if (tuple.strategy != str) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Finds the class that occurs most in the dataset
	 * 
	 * @param dataset
	 *            the dataset which contains the tuples
	 * @return a string of the attribute value which occurs the most
	 */
	private String getMajorityClass(Dataset dataset) {
		HashMap<String, Integer> mapStrategy = dataset.getMap().get(classLabel);
		List<String> stratValues = new ArrayList<>(mapStrategy.keySet());
		String majorityClass = "NONE";
		int max = 0;
		for (String value : stratValues) {
			if (max < mapStrategy.get(value)) {
				max = mapStrategy.get(value);
				majorityClass = value;
			}
		}

		return majorityClass;
	}

	/**
	 * Traverses from the root and searches for the strategy
	 * @param node
	 * @param game
	 * @return
	 */
	public STRATEGY searchForStrategy(Node node, DataTuple game) {
		STRATEGY strategy = null;
		// set strategy so pacman eats pills
		if (node.isLeaf())
			strategy = STRATEGY.valueOf(node.getLabel());
		//
		else {
			// calculate the value for the node
			String valueNode = game.discretize(node.getLabel());
			TreeMap<String, Node> tree = node.getChildren();
			Node nextNode = (Node) tree.get(valueNode);
			if (nextNode == null) {
				nextNode = (Node) tree.get("default");
			}
			strategy = searchForStrategy(nextNode, game);
		}
		return strategy;
	}

	/**
	 * Creates a datatuple for the playing game
	 * 
	 * @param game
	 * @return
	 */
	public STRATEGY getFinalStrategy(Game game) {
		DataTuple actualGame = new DataTuple(game, null);
		return searchForStrategy(root, actualGame);
	}

	/**
	 * Performs information gain and gain ratio in extension, and returns the
	 * attribute which has the highest gain ratio
	 * 
	 * @param tuples
	 * @param attributes
	 * @return the attribute (name) with the highest gain raitio
	 */
	public String attributeSelection(DataTuple[] tuples, ArrayList<String> attributes) {
		String attribute = attributes.get(0);
		float bestAttribute = 0;
		float currentAttribute = 0;

		for (int i = 0; i < attributes.size(); i++) {
			currentAttribute = informationGain(attributes.get(i));
			if (currentAttribute > bestAttribute) {
				attribute = attributes.get(i);
			}
		}
//		System.out.println("Highest gain ratio: " + attribute);
		return attribute;
	}

	/**
	 * Calculates the information gain and gain ratio and returns the highest
	 * gainRatio
	 * 
	 * @param attribute
	 * @return
	 */
	private float informationGain(String attribute) {
		float gain = 0;
		HashMap<String, Integer> map = dataset.getMap().get(attribute);
		ArrayList<String> attributeValues = new ArrayList<>(map.keySet());
		float infoD = 0;
		float infoAD = 0;
		infoD = calculateEntropy(map, dataset, attribute, attributeValues);
		infoAD = informationSplit(map, attribute, attributeValues);
		gain = infoD - infoAD;
		float gainRatio = calculateGainRatio(gain, infoAD);

		return gainRatio;
	}

	/**
	 * Calculates the gain ratio (GainRatio(A))
	 * 
	 * @param gain
	 *            the information gain
	 * @param infoAD
	 * @return
	 */
	private float calculateGainRatio(float gain, float infoAD) {
		float splitInfo = (float) (infoAD * (Math.log(infoAD) / Math.log(2)));
		float gainRatio = gain / splitInfo;
		return gainRatio;
	}

	/**
	 * Calculates the expected information of the dataset (Info(D)) --> the average
	 * information needed to identify the class label of a tuple in dataset
	 * 
	 * @param attribute
	 *            the class label Ci
	 * @return the expected information of the dataset as a float
	 */
	private float calculateEntropy(HashMap<String, Integer> map, Dataset dataset, String attribute,
			ArrayList<String> attributeValues) {
		float infoD = 0;

		// pi, the non-zero probability that a tuple in dataset belongs to class ci
		float pi = 0;
		for (String nrOfOccurrences : attributeValues) {

			// calculates the probability
			pi = (map.get(nrOfOccurrences) / dataset.getTuples().length);
			if (pi > 0) {
				// pi log2(pi)
				infoD += (float) (-pi * (Math.log(pi) / Math.log(2)));
			} else if (pi == (-1)) {
				break;
			}
		}
		return infoD;
	}

	/**
	 * Calculates the expected information of the attribute when the dataset is
	 * divided in relation to the attribute (InfoA(D))
	 * 
	 * @param map
	 * @param string
	 * @param attributeValues
	 * @return
	 */
	private float informationSplit(HashMap<String, Integer> map, String attribute, ArrayList<String> attributeValues) {
		float infoAD = 0;
		for (String nrOfOccurrences : attributeValues) {
			Dataset dt = dataset.getSubDataset(attribute, nrOfOccurrences);

			float datasetSize = dataset.getTuples().length;
			float dtSize = dt.getTuples().length;

			infoAD += (dtSize / datasetSize) * calculateEntropy(map, dt, "strategy", attributeValues);
		}
		return infoAD;
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		MOVE pacManResult;
		MyPacMan pacman = new MyPacMan();
		pacManResult = pacman.getMove(game, timeDue);

		// Calculates decisionTree-based nextMove.
		MOVE nextMove;
		totalDecisions++;
		switch (getFinalStrategy(game)) {

		case EATPILLS:
			int[] pills = game.getPillIndices();
			int[] powerPills = game.getPowerPillIndices();

			ArrayList<Integer> targets = new ArrayList<Integer>();

			for (int i = 0; i < pills.length; i++)
				if (game.isPillStillAvailable(i))
					targets.add(pills[i]);

			for (int i = 0; i < powerPills.length; i++)
				if (game.isPowerPillStillAvailable(i))
					targets.add(powerPills[i]);

			int[] targetsArray = new int[targets.size()];

			for (int i = 0; i < targetsArray.length; i++)
				targetsArray[i] = targets.get(i);

			nextMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),
					game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), targetsArray,
							Constants.DM.PATH),
					Constants.DM.PATH);
			if (pacManResult == nextMove)
				numberOfCorrectDecisions++;
			else
				 System.err.println("Incorrect prediction: " + nextMove.toString() +  ", pacman chose: "
				 + pacManResult.toString());
				return nextMove;
		case CHASE:
			int minDistGChase = Integer.MAX_VALUE;
			GHOST ghostToChase = null;

			for (GHOST ghost : GHOST.values()) {
				
				int distance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
						game.getGhostCurrentNodeIndex(ghost));
				if (distance < minDistGChase) {
					minDistGChase = distance;
					ghostToChase = ghost;
				}
			}

			nextMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),
					game.getGhostCurrentNodeIndex(ghostToChase), Constants.DM.PATH);
			if (pacManResult == nextMove)
				numberOfCorrectDecisions++;
			return nextMove;
		case FLEE:
			int minDistGRunAway = Integer.MAX_VALUE;
			GHOST ghostToFleeFrom = null;

			for (GHOST ghost : GHOST.values()) {
				int distance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),
						game.getGhostCurrentNodeIndex(ghost));
				if (distance < minDistGRunAway) {
					minDistGRunAway = distance;
					ghostToFleeFrom = ghost;
				}
			}

			nextMove = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(),
					game.getGhostCurrentNodeIndex(ghostToFleeFrom), Constants.DM.PATH);
			if (pacManResult == nextMove)
				numberOfCorrectDecisions++;
			return nextMove;
		default:
			return MOVE.NEUTRAL;
		}
	}
}