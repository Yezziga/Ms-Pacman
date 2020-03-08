package decisionTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import dataRecording.Dataset;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.STRATEGY;
import pacman.game.Game;

public class DecisionTree extends Controller<MOVE>{
	private Node root;
	public Dataset dataset;
	public ArrayList<String> attributeList; // names of attributes
	public String classDecisionTree;
	
	public DecisionTree() {
		dataset = new Dataset(DataSaverLoader.LoadPacManData());
		attributeList = new ArrayList<String>(); // fill with attributes
	}
	
	public void buildDecisionTree() {
		root = generateTree(dataset, attributeList);
	}
	
	public Node generateTree(Dataset dataset, ArrayList<String> attributeList) {
		// step 1
		Node node = null;
		
		// step 2
		if(allTuplesSameClass()) {
			node = new Node(dataset.getTuples()[0].strategy.toString()); // make node as a leaf node labeled as C.
			return node;
		} 
		// step 3
		else if(attributeList.isEmpty()) {
			node = new Node(getMajorityClass().toString()); // make node as a leaf node labeled with the majority class in D.
			return node;
		} 
		// step 4
		else {
			
			String attributeA = selectAttribute(); // step 4.1
			
			// step 4.2
			node = new Node(attributeA);
			attributeList.remove(attributeA);
			
			HashMap<String, Integer> map = dataset.attributes.get(attributeA);
			
			ArrayList<String> bestAttrValues = new ArrayList<>(map.keySet());
			ArrayList<String> attrList = null;
			// step 4.3 for each value ai in attributeA
			for (String value : bestAttrValues) {
				attrList = (ArrayList<String>) attributeList.clone();
				// a) separate all tuples in dataset so that attributeA takes
				// the value aj, creating subsetDj
				Dataset subsetDj = dataset.getSubset(attributeA, value);
				
				// b) if subsetDj is empty, add a child node to N labeled
				// with the majority class in D(dataset)
				if(subsetDj.dataset.length == 0) {
					Node child = new Node(getMajorityClass());
					node.newChild(value, child);
				} 
				// c) otherwise, add resulting node from calling generateTree() 
				// as a child node to N
				else {
					node.newChild(value, generateTree(subsetDj, attrList));
				}
			}
			
			
		}
		// step 4 return N (node)
		return node;
	}
	
	private String selectAttribute() {
		String str = null;
		float infoD = infoD("strategy");
		
		ArrayList<Float> gainsList = new ArrayList<>();
		
		for (String attribute : attributeList) {
			float infoAD = infoAD(attribute);
			float infoGain = infoD - infoAD;
			gainsList.add(infoGain);			
		}
		
		int highestGainRatioIndex = calculateGainRatio(gainsList);		
		return attributeList.get(highestGainRatioIndex);
	}

	/**
	 * TODO: Ta reda på hur split info räknas ut & vad som faktiskt görs här
	 * Calculates the gain ratio
	 * @param list
	 * @return
	 */
	private int calculateGainRatio(ArrayList<Float> list) {
		float max = 0;
		int indexMax = 0; 
		
		for (int i =0; i<list.size(); i++) {
			if(list.get(i) > max) {
				max = list.get(i);
				indexMax = i;
			}
		}
		return indexMax;
	}
	
	
	private float calcSplitInfo() {
		float res = 0;
		
		
		return res;
	}

	/**
	 * Calculates the expected information (entropy) for each of the attributes
	 * @param attribute
	 * @return
	 */
	private float infoAD(String attribute) {
		float info = 0;
		HashMap<String, Integer> map = dataset.attributes.get(attribute);
		
		ArrayList<String> values = new ArrayList<>(map.keySet());
		for ( String value: values) {
			Dataset dt = dataset.getSubset(attribute, value);
		}
		return info;
	}

	/**
	 * Calculates the expected information (entropy) of the dataset. Gives the average 
	 * amount of info needed to identify the class label of a tuple in dataset
	 * @param attribute
	 * @return
	 */
	private float infoD(String attribute) {
		float infoD = 0;
		
		HashMap<String, Integer> map = dataset.attributes.get(attribute);
		ArrayList<String> stratValues = new ArrayList<>(map.keySet());
		
		for (String value : stratValues) {
			float pi = calculateProbability(map, attribute, value); // pi = non-zero probability that the attribute
																	// belongs to class Ci
			if(pi>0) {
				infoD += (float) (-pi * (Math.log(pi) / Math.log(2))); // pi log2(pi)
				
			} else if(pi==(-1)) {
				break;
			}
		}
		
		return infoD;
	}

	private float calculateProbability(HashMap<String, Integer> map, String attribute, String value) {
		float attrSize = map.get(value);
		float datasetSize = dataset.dataset.length;
		return attrSize / datasetSize;
	}

	public boolean allTuplesSameClass() {
		STRATEGY strat = dataset.getTuples()[0].strategy;
		// check if every tuple in dataset has the same class C. 
	
		for(DataTuple tuple : dataset.getTuples()) {
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
	public String getMajorityClass() {
		HashMap<String, Integer> mapStrategy = dataset.attributes.get(classDecisionTree);
		ArrayList<String> stratValues = new ArrayList<>(mapStrategy.keySet()); // list of the strategy values
		String majorityClass = "NONE";
		int max = 0;
		for (String string : stratValues) {
			// the value's count
			if(max < mapStrategy.get(string)) {
				max = mapStrategy.get(string);
				majorityClass = string;
			}
		}
		
		
		return majorityClass;
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		MOVE pacmanResult;
		
		return null;
	}
	

}
