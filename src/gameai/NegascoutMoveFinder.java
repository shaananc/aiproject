package gameai;

public class NegascoutMoveFinder extends MoveFinder {

	public NegascoutMoveFinder(int n, int player) {
		super(n, player);
	}

	@Override
	public Move getMove(GameBoardMitch gb) {
		Node root = new Node(gb, player);
		
		int alpha = - INF;
		int beta = INF;
		
		double bestUtility = negascout(root, alpha, beta, MAX_DEPTH);
	
		for (Node childNode : root.childNodes) {
			if (childNode.utility == bestUtility) {
					return childNode.lastMove;
			}
		}

		// should never get here
		return null;
	}
	
	public double getTrueUtility(Node node) {
		return negascout(node, -INF, INF, MAX_DEPTH);
	}
	
	public double negascout(Node node, double alpha, double beta, int depth) {
		if (node.gb.getWinner() != EMPTY || depth <= 0) {
			node.utility = evaluate(node);
			return node.utility;
		}
		
		node.getChildNodes();
		boolean isFirst = true;
		for (Node childNode : node.childNodes) {
			// search in null window
			double score = -negascout(childNode, -alpha - 1, -alpha, depth - 1);
			if (alpha < score && score < beta && !isFirst) {
				// initial search failed, continue with regular alpha beta
				childNode.resetChildren();
				score = -negascout(childNode, -beta, -alpha, depth - 1);
			}
			alpha = Math.max(alpha, score);
			if (alpha >= beta) {
				break;
			}
			isFirst = false;
		}
		
		node.utility = alpha;	

		// remove non-essential children to conserve memory
		
		if (depth < MAX_DEPTH) {
			node.resetChildren();
		}
		
		return node.utility;
	}

}
