package gameai;


public class MinimaxAlphaBetaMoveFinder extends MoveFinder {

	public int MAX_DEPTH = 5;
	
	public MinimaxAlphaBetaMoveFinder(int n, int player) {
		super(n, player);
	}
	
	public MinimaxAlphaBetaMoveFinder(int n, int player, int maxDepth) {
		super(n, player);
		MAX_DEPTH = maxDepth;
	}

	@Override
	public Move getMove(GameBoardMitch gb) {
		
		Node root = new Node(gb, player);
		
		int alpha = Integer.MIN_VALUE / 2;
		int beta = Integer.MAX_VALUE / 2;
		
		boolean isMaximising = true;
		int bestUtility = minimax(root, alpha, beta, isMaximising, MAX_DEPTH);
	
		for (Node childNode : root.childNodes) {
			if (childNode.utility == bestUtility) {
					return childNode.lastMove;
			}
		}

		// should never get here
		return null;
	}
	
	public int minimax(Node node, int alpha, int beta, boolean isMaximising, int depth) {
		if (node.gb.getWinner() != EMPTY || depth <= 0) {
			node.utility = evaluator.evaluate(node);
			return node.utility;
		}
		
		// pass isMaximising for move ordering
		node.getChildNodes(isMaximising);
		
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
			node.childNodes = null;
		}
		
		return node.utility;
	}

}
