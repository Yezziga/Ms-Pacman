package decisionTree;

import java.util.TreeMap;

/**
 * Class represents a node in the decision tree.
 * @author Jessica
 *
 */
public class Node {
    private String classLabel;
    /**
     * attribute value : child node
     */
    private TreeMap<String, Node> nodeChildren;

    private boolean isLeaf;

    /**
     * Constructs a Node object with given label
     * @param label
     */
    public Node(String label) {
        this.classLabel = label;
        this.nodeChildren = new TreeMap<String, Node>();
        this.isLeaf = true;
    }

    /**
     * Adds a child node to the node
     * @param label
     * @param node
     */
    public void addChild(String label, Node node) {
        nodeChildren.put(label, node);
        if(this.isLeaf) {
        	isLeaf = false;
        }
    }

    
    public TreeMap<String, Node> getChildren() {
        return this.nodeChildren;
    }

    public String getLabel() {
        return classLabel;
    }


    public boolean isLeaf() {
        return isLeaf;
    }
}
