package decisionTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import dataRecording.Dataset;
import pacman.game.Constants.STRATEGY;

public class DecisionTree {
	private Node root;
	public Dataset dataset;
	public ArrayList<String> attributeList; // names of attributes
	
	public DecisionTree() {
		dataset = new Dataset(DataSaverLoader.LoadPacManData());
		attributeList = new ArrayList<String>(); // fill with attributes
	}
	
	public void buildDecisionTree() {
		root = generateTree();
	}
	
	public Node generateTree() {
		Node node = null;
		
		if(allTuplesSameClass()) {
			node = new Node(dataset.getTuples()[0].strategy.toString()); // TODO: make node as a leaf node labeled as C.
			return node;
		} else if(attributeList.isEmpty()) {
			node = new Node(getMajorityClass().toString()); // TODO: make node as a leaf node labeled with the majority class in D.
			return node;
		} else {
			
			selectAttribute();
			
			node = new Node(attributeA);
			attributeList.remove(attributeA);
			
			for (String string : attributeList) {
				//TODO: a) separate all tuples in dataset so that attributeA takes the value string, creating the subset Dj
				
				
			}
		}
		
		return node;
	}
	
	private void selectAttribute() {
		
		
	}

	public boolean allTuplesSameClass() {
		STRATEGY strat = dataset.getTuples()[0].strategy;
		// TODO check if every tuple in dataset has the same class C. 
	
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
		String strat = "";
		
		
		
		return strat;
	}
	

}
