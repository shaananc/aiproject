import java.util.ArrayList;


public class MinimaxMoveFinder extends MoveFinder {

	public static final int MAX_DEPTH = 6;
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
		// DEBUGGING
		System.out.println("In maxi, depth = " + (MAX_DEPTH - depth));
		System.out.println(node.gb + "\n");
		
		// if we've hit a terminal node, or reached max search depth, return node utility
		if (node.gb.getWinner() != EMPTY || depth <= 0) {
			node.utility = evaluator.evaluate(node);
			return node.utility;
		}
		
		// generate all child nodes
		node.childNodes = getChildNodes(node);
		int bestUtility = Integer.MIN_VALUE;
		
		// DEBUGGING
		for (Node childNode : node.childNodes) {
			System.out.println("child:");
			System.out.println(childNode.gb + "\n");
		}
		
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
		// DEBUGGING
		System.out.println("In mini, depth = " + (MAX_DEPTH - depth));
		System.out.println(node.gb + "\n");

		if (node.gb.getWinner() != EMPTY || depth <= 0) {
			node.utility = evaluator.evaluate(node);
			return node.utility;
		}
		
		// for each child, find it's utility
		node.childNodes = getChildNodes(node);
		int worstUtility = Integer.MAX_VALUE;
		
		// DEBUGGING
		for (Node childNode : node.childNodes) {
			System.out.println("child:");
			System.out.println(childNode.gb + "\n");
		}

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
	
	// generate all child nodes for some parent node
	public ArrayList<Node> getChildNodes(Node node) {
		ArrayList<Node> childNodes = new ArrayList<Node>();
		childNodes.addAll(getChildPlaceNodes(node));
		childNodes.addAll(getChildJumpNodes(node));
		
		return childNodes;
	}
	
	// generate child nodes from node corresponding to place moves
	public ArrayList<Node> getChildPlaceNodes(Node node) {
		ArrayList<Node> childPlaceNodes = new ArrayList<Node>();
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (node.gb.board[i * n + j] == EMPTY) {
					int[] rowPositions = {i};
					int[] colPositions = {j};
					
					Move placeMove = new Move(node.whosTurn, true, rowPositions, colPositions);
					Gameboard childBoard = node.gb.applyMoveToChildBoard(placeMove);
					childPlaceNodes.add(new Node(childBoard, placeMove));
				}
			}
		}
		
		return childPlaceNodes;
	}
	
	// generate child nodes corresponding to jump nodes
	public ArrayList<Node> getChildJumpNodes(Node node) {
		ArrayList<Node> childJumpNodes = new ArrayList<Node>();
		
		ArrayList<Integer> rowPositionsList;
		ArrayList<Integer> colPositionsList;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (node.gb.board[i * n + j] == player) {
					rowPositionsList = new ArrayList<Integer>();
					colPositionsList = new ArrayList<Integer>();
					rowPositionsList.add(i);
					colPositionsList.add(j);
					
					childJumpNodes.addAll(getChildJumpNodesFromPos(node, i, j, rowPositionsList, colPositionsList));
				}
			}
		}
		
		return childJumpNodes;
	}
	
	public ArrayList<Node> getChildJumpNodesFromPos(Node node, int i, int j, 
			ArrayList<Integer> rowPositionsList, ArrayList<Integer> colPositionsList) {
		
		ArrayList<Node> childJumpNodes = new ArrayList<Node>();
		ArrayList<NeighbourPair> neighbourPairs = node.gb.getNeighbourPairs(i, j);
		
		for (NeighbourPair pair : neighbourPairs) {
			int neighbour = node.gb.board[pair.ni * n + pair.nj];
			int neighboursNeighbour = node.gb.board[pair.nni * n + pair.nnj];
			if (neighboursNeighbour == EMPTY && (neighbour == BLACK || neighbour == WHITE)) {
				rowPositionsList.add(pair.nni);
				colPositionsList.add(pair.nnj);
				int[] rowPositions = new int[rowPositionsList.size()];
				int[] colPositions = new int[colPositionsList.size()];
				
				for (int k = 0; k < rowPositions.length; k++) {
					rowPositions[k] = rowPositionsList.get(k);
				}
				for (int k = 0; k < colPositions.length; k++) {
					colPositions[k] = colPositionsList.get(k);
				}
				// TODO: node constructor should only invert whosTurn on placeMoves
				Move jumpMove = new Move(node.whosTurn, false, rowPositions, colPositions);
				Gameboard childBoard = node.gb.applyMoveToChildBoard(jumpMove);
				Node childNode = new Node(childBoard, jumpMove);
				childJumpNodes.add( childNode );
				
				// TODO: want to pass deep copy of PositionLists, so subsequent recursive calls
				// don't screw it up for next NeighbourPair
				ArrayList<Integer> rowPositionsListCopy = new ArrayList<Integer>(rowPositionsList.size());
				ArrayList<Integer> colPositionsListCopy = new ArrayList<Integer>(colPositionsList.size());
				for (Integer r : rowPositionsList) rowPositionsListCopy.add(r);
				for (Integer c : colPositionsList) colPositionsListCopy.add(c);
				
				childJumpNodes.addAll( getChildJumpNodesFromPos(node, pair.nni, pair.nnj, 
						rowPositionsListCopy, colPositionsListCopy));
				
				rowPositionsList.remove(rowPositionsList.size() - 1);
				colPositionsList.remove(colPositionsList.size() - 1);
			}
		}
		
		return childJumpNodes;
	}
}
