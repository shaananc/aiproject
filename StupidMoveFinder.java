import csproj.jumper.*;

/* As dumb as they come... finds moves by scanning
 * Gameboard for next possible place move
 */

public class StupidMoveFinder extends MoveFinder {

	public StupidMoveFinder(int n, int player) {
		super(n, player);
	}

	@Override
	public Move getMove(Gameboard gb) {
		
		for (int i = 0; i < gb.n; i++) {
    		for (int j = 0; j < gb.n; j++) {
    			int[] r = {i};
    			int[] c = {j};
    			Move stupidMove = new Move(player, true, r, c);
    			if (gb.isLegalMove(stupidMove)) {
    				gb.applyMove(stupidMove);
    				return stupidMove;
    			}
    		}
    	}
    	return null;
	}

}
