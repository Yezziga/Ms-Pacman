package decisionTree;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import dataRecording.Dataset;
import pacman.controllers.Controller;
//bara som exempel, kommer bytas ut
import pacman.controllers.examples.AggressiveGhosts;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.STRATEGY;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class DecisionTree extends Controller<MOVE> {
	
	private Node root;
	private String classDecisionTree;
	public Dataset dataset;
	public List<String> attributeList; // names of attributes
	
	public DecisionTree() {
		classDecisionTree = "strategy";
		dataset = new Dataset(DataSaverLoader.LoadPacManData()); //TODO: fix this error
		attributeList = new ArrayList<String>(dataset.attributes.keySet()); // fill with attributes
		attributeList.remove(classDecisionTree);
	}
	
	public void buildDecisionTree() {
		root = generateTree(dataset, (ArrayList<String>) attributeList);
	}
	
	public Node generateTree(Dataset dataset, ArrayList<String> attributeList) {
		//Step 1
		Node node = null;
		
		//Step 2
		if(allTuplesSameClass(dataset)) {
			node = new Node(dataset.dataset.get(0).strategy.toString());
			return node;
			//Step 3
		} else if(attributeList.isEmpty()) {
			node = new Node(getMajorityClass(dataset).toString());
			return node;
			//step 4
		} else {
			
			AttributeSelection selector = new ID3();
			String majorAttribute = selector.AttributeSelection(dataset, attributeList);
			
			node = new Node(majorAttribute);
			attributeList.remove(majorAttribute);
			
			HashMap<String, Integer> map = dataset.attributes.get(majorAttribute);
			List<String> bestAttributeValues = new ArrayList(map.keySet());
			//selectAttribute();
			
			//node = new Node(attributeA);
			//attributeList.remove(attributeA);
			
			for (String value : bestAttributeValues) {
				
				ArrayList<String> attributeListAux = (ArrayList) attributeList.clone();
				
				Dataset subsetDj = dataset.getSubset(majorAttribute, value);
				
				if(subsetDj.dataset.isEmpty()) {
					Node child = new Node(getMajorityClass(dataset));
					node.newChild(value, child);
				}
				else {
					node.newChild(value, generateTree(subsetDj, attributeListAux));
				}
				
			}
			Node defaultNode = new Node(getMajorityClass(dataset).toString());
			node.newChild("default", defaultNode);
			return node;
		}
	}
	
	private void selectAttribute() {
		
		
	}

	public boolean allTuplesSameClass(Dataset D) {
		STRATEGY strat = D.dataset.get(0).strategy;
	
		for(DataTuple tuple : D.dataset) {
			if(tuple.strategy!=strat) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getMajorityClass(Dataset D) {
		HashMap<String, Integer> mapStrategy = D.attributes.get(classDecisionTree);
		List<String> valuesForStrategy = new ArrayList<>(mapStrategy.keySet());
		String classMajority = "NONE";
		int max = 0;
		for(String value : valuesForStrategy) {
			if(max < mapStrategy.get(value)) {
				max = mapStrategy.get(value);
				classMajority = value;
			}
		}
		return classMajority;
	}
	
	public STRATEGY recursiveSearch(Node node, DataTuple game) {
		STRATEGY strategy = null;
		
		if(node.isLeaf()) {
			strategy = STRATEGY.valueOf(node.getLabel());
		} else {
			String valueNode = game.discretizeAttrValue(node.getLabel());
			TreeMap tree = node.getChildren();
			Node nextNode = (Node) tree.get(valueNode);
			if(nextNode == null) {
				nextNode = (Node) tree.get("default");
			}
			strategy = recursiveSearch(nextNode, game);
		}
		return strategy;
	}
	
	public STRATEGY search(Game game) {
		DataTuple actualGame = new DataTuple(game, null);
		return recursiveSearch(root, actualGame);
	}
	
	@Override //TODO: Fix the pacman logic here
	public MOVE getMove(Game game, long timeDue) {
		MOVE results;
		
		
		
		MOVE nextMove;
		
		switch(search(game)) {
		case EATPILLS:
			int[] pills=game.getPillIndices();
			int[] powerPills = game.getPowerPillIndices();
			
			ArrayList<Integer> targets = new ArrayList<Integer>();
			
			for(int i = 0; i < pills.length; i++) {
				if(game.isPillStillAvailable(i))
					targets.add(pills[i]);
			}
			
		}
	}

}