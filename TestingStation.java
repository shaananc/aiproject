import java.util.ArrayList;

/* my implementation of referee class for jumper game
   until one provided by tutor makes an appearance
*/

public class TestingStation implements Piece {
    // TODO TODO TODO: merge BoardEvaluator into MoveFinder!!!!!!!!!!!!
    public static void main(String[] args) {
    	int n = 6;
    	BotPlayer p1 = new BotPlayer();
    	BotPlayer p2 = new BotPlayer();
    	
    	p1.init(n, WHITE);
    	p2.init(n, BLACK);
		
    	/* experimentation and optimisation */
		p1.setMoveFinder(new MinimaxAlphaBetaMoveFinder(n, WHITE));
    	p2.setMoveFinder(new MinimaxAlphaBetaMoveFinder(n, BLACK));
    	
    	p1.setEvaluator(new AdvancedBoardEvaluator(n, WHITE));
		p2.setEvaluator(new AdvancedBoardEvaluator(n, BLACK));
		
		p1.setMaxDepth(5);
		p2.setMaxDepth(5);
		
		ArrayList<Integer> p1Weights = new ArrayList<Integer>();
		ArrayList<Integer> p2Weights = new ArrayList<Integer>();
		
		p1Weights.add(15);
		p1Weights.add(10);
		
		p2Weights.add(40);
		p2Weights.add(10);
		
		p1.moveFinder.evaluator.setWeights(p1Weights);
		p2.moveFinder.evaluator.setWeights(p2Weights);

		//p1.setEvalSwitch(1);
		//p2.setEvalSwitch(1);
	
		//p1.setProjUtilSwitch(1);
		//p2.setProjUtilSwitch(1);

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
