import java.util.ArrayList;

/* Node for game trees
 * Keeps track of game state, and sequence of moves which
 * permutes root state into node state
 */

public class Node implements Piece {
	
	ArrayList<Node> childNodes;
	Gameboard gb;
	int player;
	Move lastMove;
	double utility;
	
	// constructor for root node
	public Node(Gameboard gb, int player) {		
		this.gb = gb;
		this.childNodes = new ArrayList<Node>();
		this.utility = INVALID;
		this.lastMove = null;
		this.player = player;
	}
	
	// constructor for non-root node, lastMove is move which created this node
	// lastPlayer is player colour of parent node
	public Node(Gameboard gb, int lastPlayer, Move lastMove) {
		this.gb = gb;
		this.childNodes = new ArrayList<Node>();
		this.utility = INVALID;
		this.lastMove = lastMove;
		
		if (lastPlayer == WHITE) {
			this.player = BLACK;
		} else {
			this.player = WHITE;
		}
		
	}
	
	// generate all child nodes for some parent node
	public void getChildNodes() {
		getChildJumpNodes();
		getChildPlaceNodes();
	}
	
	// generate child nodes from node corresponding to place moves
	public void getChildPlaceNodes() {
		
		for (int i = 0; i < gb.n; i++) {
			for (int j = 0; j < gb.n; j++) {
				if (gb.board[i * gb.n + j] == EMPTY) {
					int[] rowPositions = {i};
					int[] colPositions = {j};
					
					Move placeMove = new Move(player, true, rowPositions, colPositions);
					Gameboard childBoard = Gameboard.newInstance(gb);
					childBoard.applyMove(placeMove);
					this.childNodes.add(new Node(childBoard, this.player, placeMove));
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
				if (gb.board[i * gb.n + j] == player) {
					rowList = new ArrayList<Integer>();
					colList = new ArrayList<Integer>();
					rowList.add(i);
					colList.add(j);
					getChildJumpNodesFromPos(gb, i, j, rowList, colList);
				}
			}
		}
	}
	
	// recursive function -- DO NOT TOUCH!
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
				childBoard.board[pair.nni * gb.n + pair.nnj] = player;
				if (childBoard.board[pair.ni * gb.n + pair.nj] != player) {
					childBoard.board[pair.ni * gb.n + pair.nj] = DEAD; 
				}
				// add jump node to childNodes
				Node childNode = new Node(childBoard, this.player, jumpMove);
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
		
		return new Move(player, isPlaceMove, rowPositions, colPositions);
	}
	
	public void resetChildren() {
		this.childNodes = new ArrayList<Node>();
	}
	
	// return list of nodes with random gameboards
	// TODO: make boards of different sparsity
	public static ArrayList<Node> getTrainingSet(int size, int n, int player) {
		ArrayList<Node> trainingSet = new ArrayList<Node>();
		for (int i = 0; i < size; i++) {
			Node node = new Node(Gameboard.getRandomBoard(n), player);
			trainingSet.add(node);
		}
		return trainingSet;
	}
	
	public String toString() {
		StringBuffer nodeBuffer = new StringBuffer();
		
		// print node's representation of game state
		nodeBuffer.append("gb:\n");
		nodeBuffer.append(gb.toString());
		
		// print who's turn is next
		switch (player) {
		case WHITE:
			nodeBuffer.append("player: WHITE\n");
			break;
		case BLACK:
			nodeBuffer.append("player: BLACK\n");
			break;
		default:
			nodeBuffer.append("Error: node's \"player\" attribute not set to WHITE or BLACK\n");
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