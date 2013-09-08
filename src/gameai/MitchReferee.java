package gameai;

/* my implementation of referee class for jumper game
   until one provided by tutor makes an appearance
*/

public class MitchReferee implements Piece {
    
    public static void main(String[] args) {
    	botvsbot();
    	//humanvsbot(); 
    }
    
    public static void botvsbot() {
    	Mbrunton bot1 = new Mbrunton();
    	Mbrunton bot2 = new Mbrunton();
    	
    	final long startTime = System.currentTimeMillis();
    	playgame(bot1, bot2);
    	final long endTime = System.currentTimeMillis();
    	System.out.println("Execution time: " + (endTime - startTime));
    }
    
    public static void humanvsbot() {
    	HumanPlayer human = new HumanPlayer();
    	Mbrunton bot = new Mbrunton();
    	playgame(human, bot);
    }
    
    public static int playgame(Mbrunton p1, Mbrunton p2) {
    	int n = 6;
        p1.init(n, WHITE, new MinimaxAlphaBetaMoveFinder(n, WHITE, 6));
        p2.init(n, BLACK, new MinimaxAlphaBetaMoveFinder(n, BLACK, 6));
        
        System.out.println("Original board:");
        p1.printBoard(System.out);
        
        while (p1.getWinner() == EMPTY) {
        	Move p1Move = p1.makeMove();
        	System.out.println("After player 1's move:");
        	p1.printBoard(System.out);
        	if (p1.getWinner() != EMPTY) {
        		break;
        	}
        	
        	p2.opponentMove(p1Move);
        	Move p2Move = p2.makeMove();
        	System.out.println("After player 2's move: ");
        	p2.printBoard(System.out);
        	p1.opponentMove(p2Move);
        }
        
        int finalState = p1.getWinner();
        switch (finalState) {
        
        case WHITE:
        	System.out.println("PLAYER 1 (WHITE) WINS!");
        	break;
        case BLACK:
        	System.out.println("PLAYER 2 (BLACK) WINS!");
        	break;
        case DEAD:
        	System.out.println("DRAW");
        	break;
        case INVALID:
        	System.out.println("Someone was very very cheeky and made an illegal move!");
        	System.out.println("Invalid game...");
        	break;
        }
        
        return finalState;
    }
}
