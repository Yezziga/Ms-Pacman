package decisionTree;

import java.util.Map;

import javax.swing.JFrame;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;


/**
 * This class is used to visualize the generated decision tree. It uses jgraph,
 * which can be found here: https://github.com/jgraph/jgraphx
 * The jgraphx.jar must be added to the project
 * 
 * @author Jessica
 *
 */
public class TreeVisualizer extends JFrame {
private static final long serialVersionUID = -2707712944901661771L;
	
	private mxGraph graph;
	private Object parent;
	private int x;

	public TreeVisualizer(Node node) {
		super("Decision tree");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 1000);

		graph = new mxGraph();
				
		parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {
			Object root = graph.insertVertex(parent, null, node.getLabel(), 400, 20, 80, 30);

			x = 20;

			addChildren(node, root, 1);

		} finally {
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
		
		setVisible(true);
	}

	private void addChildren(Node node, Object parent, int y) {
		for (Map.Entry<String, Node> entry : node.getChildren().entrySet()) {
			String key = entry.getKey();
			Node childNode = entry.getValue();

			Object child = addChild(parent, childNode.getLabel(), key, x, y);

			x += 20;
			
			addChildren(childNode, child, y+1);
			
		}
	}
	
	private Object addChild(Object parent, String label, String edge, int x, int y) {
		Object child = graph.insertVertex(this.parent, null, label, x, 100*y, 80, 30);
		graph.insertEdge(this.parent, null, edge, parent, child);
		return child;
	}

}
