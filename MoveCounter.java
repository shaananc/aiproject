/* Mitchell Brunton - mbrunton
   Shaanan Cohney   - sncohney
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* MoveCounter objects are used to count the number of legal
   moves possible for a given player on a given gameboard
*/

public class MoveCounter {

	private int n;					// dimension of board
	private Map<Gameboard, Integer> moveCache;		// store Gameboard/numMoves entries for lookup
	
	public MoveCounter(int n) {
		this.n = n;
		this.moveCache = new HashMap<Gameboard, Integer>();
	}
	
	// count number of possible moves "player" could make on Gameboard "gb"
	public int countMoves(Gameboard gb, Cell.State player) {
		
		// if it's cached, look it up, otherwise calculate and store
		if (moveCache.containsKey(gb)) {
			return moveCache.get(gb);
		} else {
			int moves = countPlaceMoves(gb);
			moves += countJumpMoves(gb, player);
			moveCache.put(gb, moves);
			return moves;
		}
	}
	
	// count number of placement moves possible (same for either player)
	public int countPlaceMoves(Gameboard gb) {
		int placeMoves = 0;
		
		for (Cell cell : gb.board){
			if (cell.state == Cell.State.E) {
					placeMoves++;
			}
		}
		
		return placeMoves;
	}
	
	// count number of jump moves "player" could make from Gameboard "gb"
	public int countJumpMoves(Gameboard gb, Cell.State player) {
		int jumpMoves = 0;

		// for each cell..
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				Cell cell = gb.board[i*n + j];
				if (cell.state == player) {
					// if player has piece at "cell", then add number of jump moves player
					// can make from this position to our running total
					jumpMoves += countJumpMovesFromPos(gb, player, new Pos(i, j), 0);
				}
			}
		}
		
		return jumpMoves;
	}

	// recursively calculate the number of jump moves "player" can make from position "pos"
	// in Gameboard "gb"
	public int countJumpMovesFromPos(Gameboard gb, Cell.State player, Pos pos, int depth) {
		// DEBUGGING
		/*
		System.out.println("in countJumpMovesFromPos at pos (" + pos.i + ", " + pos.j + ")");
		System.out.println("depth = " + depth);
		System.out.println("gameboard:");
		System.out.println(gb);
		*/
		
		ArrayList<NeighbourPair> neighbourPairs = gb.getNeighbourPairs(pos);
		int jumpMoves = 0;
		
		for (NeighbourPair neighbourPair : neighbourPairs) {
			Pos neighbour = neighbourPair.neighbour;
			Pos neighboursNeighbour = neighbourPair.neighboursNeighbour;
			int ni = neighbour.i;
			int nj = neighbour.j;
			int nni = neighboursNeighbour.i;
			int nnj = neighboursNeighbour.j;
			
			// if neighbour is occupied, and neighbour's neighbour is empty
			if (gb.board[ni*n + nj].isOccupied()) {
				if (gb.board[nni*n + nnj].state == Cell.State.E) {
					// then place player's piece in neighbour's neighbour's position on a new board
					// and recursively calculate the number of jump moves
					Gameboard child = gb.executeMove(player, new Pos(nni, nnj), new Pos(ni, nj));
					jumpMoves += 1 + countJumpMovesFromPos(child, player, neighboursNeighbour, depth + 1);
				}
			}
		}
		// DEBUGGING
		//System.out.println("jumpMoves from pos (" + pos.i + ", " + pos.j + ") = " + jumpMoves);
		
		return jumpMoves;
	}

	public void storeCache() {
		/* TODO: store cache values locally in XML */
	}
}
