package decisionTree;

import java.util.ArrayList;

import dataRecording.DataSaverLoader;
import dataRecording.Dataset;

public class DecisionTree {
	public Dataset dataset;
	public ArrayList<String> listAtt;
	
	public DecisionTree() {
		dataset = new Dataset(DataSaverLoader.LoadPacManData());
		
	}
	
	public Node generateTree(Dataset dataset, ArrayList<String> attributeList) {
		Node node = null;
		
		if(allTuplesSameClass()) {
			node = new Node(); // TODO: make node as a leaf node labeled as C.
			return node;
		} else if(attributeList.isEmpty()) {
			node = new Node(); // TODO: make node as a leaf node labeled with the majority class in D.
			return node;
		} else {
			String attributeA = attributeSelection(dataset, attributeList);
			node = new Node(attributeA);
			listAtt.remove(attributeA);
			
			for (String string : attributeList) {
				//TODO: a) separate all tuples in dataset so that attributeA takes the value string, creating the subset Dj
				
				
			}
		}
		
		return node;
	}
	
	public boolean allTuplesSameClass() {
//		STRATEGY strat = this.dataset.get(0).strategy;
		// TODO check if every tuple in dataset has the same class C. 
		return false;
	}
	
	public String attributeSelection(Dataset dataset, ArrayList<String> attributeList) {
		// TODO: choose the current attribute A.
		return null;
	}
}
