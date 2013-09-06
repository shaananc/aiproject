import java.util.ArrayList;

/* Node for game trees
 * Keeps track of game state, and sequence of moves which
 * permutes root state into node state
 */

public class Node implements Piece {
	Node parentNode;
	ArrayList<Node> childNodes;

	Gameboard gb;
	int whosTurn;
	Move lastMove;
	int utility;
	
	// default constructor
	public Node(Gameboard gb) {
		this.gb = gb;
		this.childNodes = new ArrayList<Node>();
		this.utility = INVALID;
	}
	
	// constructor for root node
	public Node(Gameboard gb, int whosTurn) {
		this(gb);
		
		this.parentNode = null;
		this.lastMove = null;
		this.whosTurn = whosTurn;
	}
	
	// constructor for non-root node, lastMove is move which created this node
	public Node(Node parentNode, Gameboard gb, Move lastMove) {
		this(gb);
		
		this.parentNode = parentNode;
		this.lastMove = lastMove;
		
		if (parentNode.whosTurn == WHITE) {
			this.whosTurn = BLACK;
		} else {
			this.whosTurn = WHITE;
		}
	}
	
	// generate all child nodes for some parent node
	public void getChildNodes() {
		getChildPlaceNodes();
		getChildJumpNodes();		
	}
	
	// generate child nodes from node corresponding to place moves
	public void getChildPlaceNodes() {
		
		for (int i = 0; i < gb.n; i++) {
			for (int j = 0; j < gb.n; j++) {
				if (gb.board[i * gb.n + j] == EMPTY) {
					int[] rowPositions = {i};
					int[] colPositions = {j};
					
					Move placeMove = new Move(whosTurn, true, rowPositions, colPositions);
					Gameboard childBoard = gb.applyMoveToChildBoard(placeMove);
					this.childNodes.add(new Node(this, childBoard, placeMove));
				}
			}
		}
	}
	
	// generate child nodes corresponding to jump nodes
	public void getChildJumpNodes() {
		
		ArrayList<Integer> rowList;
		ArrayList<Integer> colList;
		
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
	
	// TODO
	public void getChildJumpNodesFromPos(Gameboard newBoard, int i, int j, ArrayList<Integer> rowList, ArrayList<Integer> colList) {

		// DEBUGGING
		/*
		System.out.println("Inside getChildJumpNodesFromPos...");
		System.out.println("newBoard:\n" + newBoard);
		System.out.println("(i, j) = (" + i + ", " + j + ")");
		System.out.println("rowList:\n" + rowList);
		System.out.println("colList:\n" + colList);
		*/
		
		ArrayList<NeighbourPair> neighbourPairs = newBoard.getNeighbourPairs(i, j);
		
		// DEBUGGING
		/*
		System.out.println("neighbourPairs:");
		for (NeighbourPair pair : neighbourPairs) {
			System.out.println("(ni, nj) = (" + pair.ni + ", " + pair.nj + ")");
			System.out.println("(nni, nnj) = (" + pair.nni + ", " + pair.nnj + ")");
		}
		*/
		
		for (NeighbourPair pair : neighbourPairs) {
			int neighbour = newBoard.board[pair.ni * gb.n + pair.nj];
			int neighboursNeighbour = newBoard.board[pair.nni * gb.n + pair.nnj];
			if (neighboursNeighbour == EMPTY && (neighbour == BLACK || neighbour == WHITE)) {
				rowList.add(pair.nni);
				colList.add(pair.nnj);
				Move jumpMove = createMoveFromArrayLists(false, rowList, colList);
				Gameboard childBoard = Gameboard.newInstance(newBoard);
				childBoard.board[pair.nni * gb.n + pair.nnj] = whosTurn;
				if (childBoard.board[pair.ni * gb.n + pair.nj] != whosTurn) {
					childBoard.board[pair.ni * gb.n + pair.nj] = DEAD; 
				}
				
				Node childNode = new Node(this, childBoard, jumpMove);
				this.childNodes.add(childNode);
				getChildJumpNodesFromPos(childBoard, pair.nni, pair.nnj, rowList, colList);
				rowList.remove(rowList.size() - 1);
				colList.remove(colList.size() - 1);
			}
		}		
	}
	
	public Move createMoveFromArrayLists(boolean isPlaceMove, ArrayList<Integer> rowList, ArrayList<Integer> colList) {
		int[] rowPositions = new int[rowList.size()];
		int[] colPositions = new int[colList.size()];
		for (int i = 0; i < rowList.size(); i++) {
			rowPositions[i] = rowList.get(i);
			colPositions[i] = colList.get(i);
		}
		
		return new Move(whosTurn, isPlaceMove, rowPositions, colPositions);
	}
	
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