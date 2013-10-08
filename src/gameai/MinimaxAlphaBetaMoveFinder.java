package gameai;

public class MinimaxAlphaBetaMoveFinder extends MoveFinder {
	
	public MinimaxAlphaBetaMoveFinder(int n, int player) {
		super(n, player);
	}

	@Override
	public Move getMove(GameBoardMitch gb) {
		
		Node root = new Node(gb, player);
		int alpha = - INF;
		int beta = INF;
		
		boolean isMaximising = true;
		double bestUtility = minimax(root, alpha, beta, isMaximising, MAX_DEPTH);
	
		for (Node childNode : root.childNodes) {
			if (childNode.utility == bestUtility) {
					return childNode.lastMove;
			}
		}

		// should never get here
		return null;
	}
	
	public double getTrueUtility(Node node) {
		return minimax(node, -INF, INF, true, MAX_DEPTH);
	}
	
	public double minimax(Node node, double alpha, double beta, boolean isMaximising, int depth) {
		if (node.gb.getWinner() != EMPTY || depth <= 0) {
			node.utility = evaluate(node);
			return node.utility;
		}
		
		node.getChildNodes();
		
		if (isMaximising) {
			for (Node childNode : node.childNodes) {
				alpha = Math.max(alpha, minimax(childNode, alpha, beta, !isMaximising, depth - 1));
				if (beta <= alpha) {
					break;
				}
			}
			node.utility = alpha;
		} else {
			for (Node childNode : node.childNodes){
				beta = Math.min(beta,  minimax(childNode, alpha, beta, !isMaximising, depth - 1));
				if (beta <= alpha) {
					break;
				}
			}
			node.utility = beta;
		}
		
		if (depth < MAX_DEPTH) {
			// not the root node, so can forget about children and conserve space
			node.resetChildren();
		}
		
		return node.utility;
	}
}
