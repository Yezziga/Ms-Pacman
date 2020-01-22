package decisionTree;

import java.util.ArrayList;
import java.util.TreeMap;

public class Node {
	private String label; // closestGhostDist - square/circle. name of node
	private TreeMap<String, Node> nodeChildren; // <attribute value(branch), childNode>
	private boolean isLeaf;

	public Node(String attributeLabel) {
		label = attributeLabel;
		nodeChildren = new TreeMap<String, Node>();
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
