package gameai;
import java.util.ArrayList;

/* Mitchell Brunton's implementation of referee class for jumper game
*/

public class TestingStation implements Piece {

	public static void main(String[] args) {
    	int n = 6;
    	BotPlayer p1 = new BotPlayer();
    	BotPlayer p2 = new BotPlayer();
    	
    	p1.init(n, WHITE);
    	p2.init(n, BLACK);
		
    	/* experimentation and optimisation */
		/*
    	p1.setMoveFinder(new MinimaxAlphaBetaMoveFinder(n, WHITE));
    	p2.setMoveFinder(new MinimaxAlphaBetaMoveFinder(n, BLACK));
    	*/
    			
		/*
		p1.setMaxDepth(6);
		p2.setMaxDepth(6);
		*/
		
		
		/* TODO debug
		Gameboard randomBoard = Gameboard.getRandomBoard(n);
		p1.setBoard(randomBoard);
		p2.setBoard(randomBoard);
		*/

		boolean printOutput = true;
		GameResult result = playGame(p1, p2, printOutput);
		
		System.out.println(result);
    }
    
    public static GameResult playGame(BotPlayer p1, BotPlayer p2, boolean printOutput) {
    	
        GameResult result = new GameResult();
        long GameStartTime, playerStartTime;
        long GameEndTime, playerEndTime;
        
        if (printOutput) {
        	System.out.println("Original board:");
        	p1.printBoard(System.out);
        }
        
        GameStartTime = System.currentTimeMillis();
        while (p1.getWinner() == EMPTY) {
        	// player 1's move
        	playerStartTime = System.currentTimeMillis();
        	Move p1Move = p1.makeMove();
        	playerEndTime = System.currentTimeMillis();
        	result.incrementP1Time(playerEndTime - playerStartTime);
        	
        	p2.opponentMove(p1Move);
        	
        	if (printOutput) {
        		System.out.println("After player 1's move:");
        		p1.printBoard(System.out);
        	}
        	
        	if (p1.getWinner() != EMPTY) {
        		break;
        	}
        	
        	// player 2's move
        	playerStartTime = System.currentTimeMillis();
        	Move p2Move = p2.makeMove();
        	playerEndTime = System.currentTimeMillis();
        	result.incrementP2Time(playerEndTime - playerStartTime);
        	
        	p1.opponentMove(p2Move);
        	
        	if (printOutput) {
        		System.out.println("After player 2's move: ");
        		p2.printBoard(System.out);
        	}
        }
        GameEndTime = System.currentTimeMillis();
        result.incrementGameTime(GameEndTime - GameStartTime);
        
        int winner = p1.getWinner();
        result.setWinner(winner, p1.gb);
        
        return result;
    }
}
