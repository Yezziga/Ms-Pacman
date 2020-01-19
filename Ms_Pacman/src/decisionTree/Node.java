package decisionTree;

import java.util.ArrayList;
import java.util.TreeMap;

public class Node {
	private String label;
	private TreeMap<String, Node> nodeChildren;
	private ArrayList<String> attributeValues;
	private boolean isLeaf;

	public Node(String attributeLabel) {
		label = attributeLabel;
		nodeChildren = new TreeMap<String, Node>();
		attributeValues = new ArrayList<String>();
		isLeaf = true;
	}
	
	public void newChild(String label, Node node) {
		nodeChildren.put(label, node);
		if(isLeaf) {
			isLeaf = false;
		}
	}
	
	public TreeMap<String, Node> getChildren() {
		return nodeChildren;
	}
	
	public String getLabel() {
		return label;
	}
	
	public boolean isLeaf() {
		return isLeaf;
	}
}
