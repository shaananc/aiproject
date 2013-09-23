import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/* Node for game trees
 * Keeps track of game state, and sequence of moves which
 * permutes root state into node state
 */

public class Node implements Piece {
	ArrayList<Node> childNodes;

	Gameboard gb;
	int whosTurn;
	Move lastMove;
	int utility;
	int projectedUtility;
	public static final boolean ORDERING_NODES = false;
	
	// default constructor
	public Node(Gameboard gb) {
		this.gb = gb;
		this.childNodes = new ArrayList<Node>();
		this.utility = INVALID;
		this.projectedUtility = INVALID;
	}
	
	// constructor for root node
	public Node(Gameboard gb, int whosTurn) {
		this(gb);
		
		this.lastMove = null;
		this.whosTurn = whosTurn;
	}
	
	// constructor for non-root node, lastMove is move which created this node
	public Node(Node parentNode, Gameboard gb, Move lastMove) {
		this(gb);
		this.lastMove = lastMove;
		
		if (parentNode.whosTurn == WHITE) {
			this.whosTurn = BLACK;
		} else {
			this.whosTurn = WHITE;
		}
		
	}
	
	// generate all child nodes for some parent node
	public void getChildNodes() {
		getChildJumpNodes();
		getChildPlaceNodes();
	}
	
	// get child nodes, but order them based on whether we want
	// best nodes first, or worst
	public void getChildNodes(boolean isMaxNode, BoardEvaluator evaluator) {
		getChildNodes();
		
		// NOTE: Finding a projected utility just seems to slow things down...
		// We seem to be better just putting jump nodes first for all nodes
		
		if (ORDERING_NODES) {
			for (Node childNode : childNodes) {
				evaluator.setProjectedUtility(childNode);
			}
			// order nodes based on projected utility
			if (isMaxNode) {
				Collections.sort(childNodes, COMPARE_BEST_FIRST);
			} else {
				Collections.sort(childNodes, COMPARE_WORST_FIRST);
			}
		}
	}
	
	// generate child nodes from node corresponding to place moves
	public void getChildPlaceNodes() {
		
		for (int i = 0; i < gb.n; i++) {
			for (int j = 0; j < gb.n; j++) {
				if (gb.board[i * gb.n + j] == EMPTY) {
					int[] rowPositions = {i};
					int[] colPositions = {j};
					
					Move placeMove = new Move(whosTurn, true, rowPositions, colPositions);
					Gameboard childBoard = Gameboard.newInstance(gb);
					childBoard.applyMove(placeMove);
					this.childNodes.add(new Node(this, childBoard, placeMove));
				}
			}
		}
	}
	
	// generate child nodes corresponding to jump moves
	public void getChildJumpNodes() {
		
		ArrayList<Integer> rowList;
		ArrayList<Integer> colList;
		
		// for each cell, if the cell has a player piece,
		// add all nodes corresponding to jump moves which
		// start at that piece
		for (int i = 0; i < gb.n; i++) {
			for (int j = 0; j < gb.n; j++) {
				if (gb.board[i * gb.n + j] == whosTurn) {
					rowList = new ArrayList<Integer>();
					colList = new ArrayList<Integer>();
					rowList.add(i);
					colList.add(j);
					getChildJumpNodesFromPos(gb, i, j, rowList, colList);
				}
			}
		}
	}
	
	public void getChildJumpNodesFromPos(Gameboard newBoard, int i, int j, ArrayList<Integer> rowList, ArrayList<Integer> colList) {
		
		ArrayList<NeighbourPair> neighbourPairs = newBoard.getNeighbourPairs(i, j);
		
		// for each possible jump location from (i, j)...
		for (NeighbourPair pair : neighbourPairs) {
			int neighbour = newBoard.board[pair.ni * gb.n + pair.nj];
			int neighboursNeighbour = newBoard.board[pair.nni * gb.n + pair.nnj];
			// if we can legally jump there...
			if (neighboursNeighbour == EMPTY && (neighbour == BLACK || neighbour == WHITE)) {
				rowList.add(pair.nni);
				colList.add(pair.nnj);
				// create the jump move which finishes at neighboursNeighbour
				Move jumpMove = createMoveFromArrayLists(false, rowList, colList);
				Gameboard childBoard = Gameboard.newInstance(newBoard);
				// apply that final jump to a new board
				childBoard.board[pair.nni * gb.n + pair.nnj] = whosTurn;
				if (childBoard.board[pair.ni * gb.n + pair.nj] != whosTurn) {
					childBoard.board[pair.ni * gb.n + pair.nj] = DEAD; 
				}
				// add jump node to childNodes
				Node childNode = new Node(this, childBoard, jumpMove);
				this.childNodes.add(childNode);
				// recursively add subsequent jumps
				getChildJumpNodesFromPos(childBoard, pair.nni, pair.nnj, rowList, colList);
				// retract final jump, and continue search with next neighbourPair
				rowList.remove(rowList.size() - 1);
				colList.remove(colList.size() - 1);
			}
		}		
	}
	
	// create a Move object from ArrayLists (used when recursively finding jump moves from a position)
	public Move createMoveFromArrayLists(boolean isPlaceMove, ArrayList<Integer> rowList, ArrayList<Integer> colList) {
		int[] rowPositions = new int[rowList.size()];
		int[] colPositions = new int[colList.size()];
		for (int i = 0; i < rowList.size(); i++) {
			rowPositions[i] = rowList.get(i);
			colPositions[i] = colList.get(i);
		}
		
		return new Move(whosTurn, isPlaceMove, rowPositions, colPositions);
	}
	
	public void resetChildren() {
		this.childNodes = new ArrayList<Node>();
	}
	
	// return list of nodes with random gameboards
	public static ArrayList<Node> getTrainingSet(int size, int n, int whosTurn) {
		ArrayList<Node> trainingSet = new ArrayList<Node>();
		for (int i = 0; i < size; i++) {
			Node node = new Node(Gameboard.getRandomBoard(n), whosTurn);
			trainingSet.add(node);
		}
		return trainingSet;
	}
	
	// use projectedUtility to order nodes
	public static Comparator<Node> COMPARE_BEST_FIRST = new Comparator<Node>() {
		public int compare(Node node1, Node node2) {
			Integer projectedUtility1 = new Integer(node1.projectedUtility);
			Integer projectedUtility2 = new Integer(node2.projectedUtility);
			// reverse the order, since higher projected utility should come first
			return projectedUtility2.compareTo(projectedUtility1);
		}
	};
	
	public static Comparator<Node> COMPARE_WORST_FIRST = new Comparator<Node>() {
		public int compare(Node node1, Node node2) {
			Integer projectedUtility1 = new Integer(node1.projectedUtility);
			Integer projectedUtility2 = new Integer(node2.projectedUtility);
			// lower projected utility should come first
			return projectedUtility1.compareTo(projectedUtility2);
		}
	};
	
	public String toString() {
		StringBuffer nodeBuffer = new StringBuffer();
		
		// print node's representation of game state
		nodeBuffer.append("gb:\n");
		nodeBuffer.append(gb.toString());
		
		// print who's turn is next
		switch (whosTurn) {
		case WHITE:
			nodeBuffer.append("whosTurn: WHITE\n");
			break;
		case BLACK:
			nodeBuffer.append("whosTurn: BLACK\n");
			break;
		default:
			nodeBuffer.append("Error: node's \"whosTurn\" attribute not set to WHITE or BLACK\n");
			break;
		}
		
		if (lastMove == null) {
			nodeBuffer.append("no lastMove... root node");
		} else {
			// print details of move which connects parent node to this node
			nodeBuffer.append("lastMove:\n");
			nodeBuffer.append("\tmove made by: ");
		
			switch (lastMove.P) {
			case WHITE:
				nodeBuffer.append("WHITE\n");
				break;
			case BLACK:
				nodeBuffer.append("BLACK\n");
				break;
			default:
				System.out.println("Error: node's \"lastMove\" attribute not set to WHITE or BLACK");
				break;
			}
			nodeBuffer.append("\tisPlaceMove: " + lastMove.IsPlaceMove);
			nodeBuffer.append("\n\trowPositions: ");
			for (int k = 0; k < lastMove.RowPositions.length; k++) {
				nodeBuffer.append(lastMove.RowPositions[k] + " ");
			}	
			nodeBuffer.append("\n\tcolPositions: ");
			for (int k = 0; k < lastMove.ColPositions.length; k++) {
				nodeBuffer.append(lastMove.ColPositions[k] + " ");
			}
			nodeBuffer.append("\n");
		}
		
		return nodeBuffer.toString();
	}
	
}