import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MoveCounter {

	private int n;					// dimension of board
	private Map<Gameboard, Integer> moveCache;		// store Gameboard/numMoves entries for lookup
	
	public MoveCounter(int n) {
		this.n = n;
		this.moveCache = new HashMap<Gameboard, Integer>();
	}
	
	public int countMoves(Gameboard gb, Cell.State player) {
		if (moveCache.containsKey(gb)) {
			return moveCache.get(gb);
		} else {
			int moves = countPlaceMoves(gb);
			moves += countJumpMoves(gb, player);
			moveCache.put(gb, moves);
			return moves;	
		}
	}
	
	public int countPlaceMoves(Gameboard gb) {
		/* TODO: iterate over empty (non-dead) cells */
		int n = gb.getN();
		int placeMoves = 0;
		
		for (Cell cell : gb.board){
			if (cell.state == Cell.State.E) {
					placeMoves++;
			}
		}
		
		return placeMoves;
	}
	
	public int countJumpMoves(Gameboard gb, Cell.State player) {
		int jumpMoves = 0;
		int n = gb.getN();
		// for each cell..
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				Cell cell = gb.board[i*n + j];
				if (cell.state == player) {
					jumpMoves += countJumpMovesFromPos(gb, player, new Pos(i, j) );
				}
			}
		}	
				
		return jumpMoves;
	}

	public int countJumpMovesFromPos(Gameboard gb, Cell.State player, Pos pos) {
		
		ArrayList<NeighbourPair> neighbourPairs = gb.getNeighbourPairs(pos);
		int jumpMoves = 0;
		
		for (NeighbourPair neighbourPair : neighbourPairs) {
			Pos neighbour = neighbourPair.neighbour;
			Pos neighboursNeighbour = neighbourPair.neighboursNeighbour;
			int ni = neighbour.i;
			int nj = neighbour.j;
			int nni = neighboursNeighbour.i;
			int nnj = neighboursNeighbour.j;
			
			// if neighbour is occupied, and neighbour's neighbour isn't...
			if (gb.board[ni*n + nj].isOccupied()) {
				if (gb.board[nni*n + nnj].state == Cell.State.E) {
					Gameboard child = gb.executeMove(player, new Pos(nni, nnj), new Pos(ni, nj));
					System.out.println(child);
					jumpMoves += 1 + countJumpMovesFromPos(child, player, neighboursNeighbour);
				}
			}
		}
		
		return jumpMoves;
	}

	public void storeCache() {
		/* TODO: store cache values locally in XML datastruct */
	}
}
