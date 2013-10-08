package gameai;

/* As dumb as they come... finds moves by scanning
 * Gameboard for next possible place move
 */

public class StupidMoveFinder extends MoveFinder {

	public StupidMoveFinder(int n, int player) {
		super(n, player);
	}

	@Override
	public Move getMove(GameBoardMitch gb) {
		
		for (int i = 0; i < gb.n; i++) {
    		for (int j = 0; j < gb.n; j++) {
    			if (gb.board[i*n + j] == EMPTY) {
    				int[] r = {i};
        			int[] c = {j};
        			return new Move(player, true, r, c);
    			}
    		}
    	}
    	return null;
	}
	
	// TODO
	public double getTrueUtility(Node node) {
		return evaluate(node);
	}

}
