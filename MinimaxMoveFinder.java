

public class MinimaxMoveFinder extends MoveFinder {
		
	public MinimaxMoveFinder(int n, int player) {
		super(n, player);
	}

	@Override
	public Move getMove(Gameboard gb) {	
		// turn current Gameboard into a root node for search tree
		Node root = new Node(gb, player);
		
		// call minimax function on root node
		double maxUtility = maxi(root, MAX_DEPTH);
		
		// find child of root with same utility value, and return corresponding move
		for (Node childNode : root.childNodes) {
			if (childNode.utility == maxUtility) {
				return childNode.lastMove;
			}
		}
		
		// should never get here -- just to keep the compiler happy
		return null;
	}
	
	public double getTrueUtility(Node node) {
		return maxi(node, MAX_DEPTH);
	}
	
	public double maxi(Node node, int depth) {
		
		// if we've hit a terminal node, or reached max search depth, return node utility
		if (node.gb.getWinner() != EMPTY || depth <= 0) {
			node.utility = evaluate(node);
			return node.utility;
		}
		
		node.getChildNodes();
		double bestUtility = - INF;
		
		// for each child, find it's utility
		for (Node childNode : node.childNodes) {
			double utility = mini(childNode, depth - 1);
			if (utility > bestUtility) {
				bestUtility = utility;
			}
		}
		
		// set this node's utility as the max of all children's utility
		node.utility = bestUtility;
		return bestUtility;
	}
	
	public double mini(Node node, int depth) {

		if (node.gb.getWinner() != EMPTY || depth <= 0) {
			node.utility = evaluate(node);
			return node.utility;
		}
		
		node.getChildNodes();
		double worstUtility = INF;
		
		for (Node childNode : node.childNodes) {
			double utility = maxi(childNode, depth - 1);
			if (utility < worstUtility) {
				worstUtility = utility;
			}
		}
		
		// this node's utility = min of all it's children
		node.utility = worstUtility;
		return worstUtility;
	}

}
