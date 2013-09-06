

public class MinimaxMoveFinder extends MoveFinder {

	public static final int MAX_DEPTH = 3;
	// TODO: discuss whether MAX_DEPTH should be strictly even
	
	// TODO: prune search space with alpha-beta
	
	// TODO: consider pruning further by only considering longest jump moves
	// from a certain pos (or similar) since otherwise we could have jump {a,b}
	// and jump {a,b,c} as children from n1, then {c} as a child of {a,b} - almost
	// repeating ourselves (modulo opponent's sandwiched move)
	
	// TODO consider:
	// cache of grandchildren of most recent root-child,
	// since next call to getMove will feature one of
	// their gbs
	
	// TODO consider:
	// not explicitly returning int from maxi/mini functions
	// but rather just setting node utility values (maybe less
	// readable though
	
	public MinimaxMoveFinder(int n, int player) {
		super(n, player);
	}

	@Override
	public Move getMove(Gameboard gb) {	
		// turn current Gameboard into a root node for search tree
		Node root = new Node(gb, player);
		
		// call minimax function on root node
		int maxUtility = maxi(root, MAX_DEPTH);
		
		// find child of root with same utility value, and return corresponding move
		for (Node childNode : root.childNodes) {
			if (childNode.utility == maxUtility) {
				return childNode.lastMove;
			}
		}
		
		// should never get here -- just to keep the compiler happy
		return null;
	}
	
	public int maxi(Node node, int depth) {
		
		// if we've hit a terminal node, or reached max search depth, return node utility
		if (node.gb.getWinner() != EMPTY || depth <= 0) {
			node.utility = evaluator.evaluate(node);
			return node.utility;
		}
		
		// generate jump nodes first, and if there are none then look to place nodes
		node.getChildJumpNodes();
		if (node.childNodes.size() == 0) {
			node.getChildPlaceNodes();
		}
		int bestUtility = Integer.MIN_VALUE;
		
		// for each child, find it's utility
		for (Node childNode : node.childNodes) {
			int utility = mini(childNode, depth - 1);
			if (utility > bestUtility) {
				bestUtility = utility;
			}
		}
		
		// set this node's utility as the max of all children's utility
		node.utility = bestUtility;
		return bestUtility;
	}
	
	public int mini(Node node, int depth) {

		if (node.gb.getWinner() != EMPTY || depth <= 0) {
			node.utility = evaluator.evaluate(node);
			return node.utility;
		}
		
		// for each child, find it's utility
		node.getChildJumpNodes();
		if (node.childNodes.size() == 0) {
			node.getChildPlaceNodes();
		}
		int worstUtility = Integer.MAX_VALUE;
		
		for (Node childNode : node.childNodes) {
			int utility = maxi(childNode, depth - 1);
			if (utility < worstUtility) {
				worstUtility = utility;
			}
		}
		
		// this node's utility = min of all it's children
		node.utility = worstUtility;
		return worstUtility;
	}

}
