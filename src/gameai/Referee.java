package gameai;

import gameai.Move;
import gameai.Player;

/*   
 *   Referee:
 *      A mediator between two players. It is responsible to initialize 
 *      the players and pass the plays between them and terminates the game. 
 *      It is the responsibility of the players to check whether they have won and
 *      maintain the board state.
 *
 *   @author msalehi
 */


public class Referee implements Piece{

	private static Player P1;
	private static Player P2;
	private static Move lastPlayedMove;
	
	/*
	 * Input arguments: first board size, second path of player1 and third path of player2
	 */
	public static void main(String[] args)
	{
            
		lastPlayedMove = new Move();
		int NumberofMoves = 0;
		int boardEmptyPieces=Integer.valueOf(args[0])*Integer.valueOf(args[0]);
		System.out.println("Referee started !");
		try{
			P1 = (Player)(Class.forName(args[1]).newInstance());
			P2 = (Player)(Class.forName(args[2]).newInstance());
		}
		catch(Exception e){
			System.out.println("Error "+ e.getMessage());
			System.exit(1);
		}
		
		P1.init(Integer.valueOf(args[0]), WHITE);
		P2.init(Integer.valueOf(args[0]), BLACK);
		
		
		while(boardEmptyPieces > 0)
		{
			
			NumberofMoves++;
			lastPlayedMove=P1.makeMove();
			if (lastPlayedMove.IsPlaceMove){  
				System.out.println("Placing to. "+lastPlayedMove.RowPositions[0]+":"+lastPlayedMove.ColPositions[0]+" by "+lastPlayedMove.P);
				boardEmptyPieces--;
			} else
			{
				System.out.println("Jumpping from. "+lastPlayedMove.RowPositions[0]+":"+lastPlayedMove.ColPositions[0]+" by "+lastPlayedMove.P);
				for (int i=1;i<lastPlayedMove.ColPositions.length;i++){
					boardEmptyPieces--;
					System.out.println("To. "+lastPlayedMove.RowPositions[i]+":"+lastPlayedMove.ColPositions[i]);
				}

			}
			P1.printBoard(System.out);
			
			if(P2.opponentMove(lastPlayedMove)<0)
			{
				System.out.println("Exception: Player 2 rejected the move of player 1.");
				P1.printBoard(System.out);
				P2.printBoard(System.out);
				System.exit(1);
			}
			
			if (boardEmptyPieces <= 0) break;
			else{
			NumberofMoves++;	
			lastPlayedMove = P2.makeMove();
			if (lastPlayedMove.IsPlaceMove){  
				System.out.println("Placing to. "+lastPlayedMove.RowPositions[0]+":"+lastPlayedMove.ColPositions[0]+" by "+lastPlayedMove.P);
				boardEmptyPieces--;
			} else
			{
				System.out.println("Jumpping from. "+lastPlayedMove.RowPositions[0]+":"+lastPlayedMove.ColPositions[0]+" by "+lastPlayedMove.P);
				for (int i=1;i<lastPlayedMove.ColPositions.length;i++){
					boardEmptyPieces--;
					System.out.println("To. "+lastPlayedMove.RowPositions[i]+":"+lastPlayedMove.ColPositions[i]);
				}

			}
			P2.printBoard(System.out);
			if(P1.opponentMove(lastPlayedMove)<0)
			{
				System.out.println("Exception: Player 1 rejected the move of player 2.");
				P2.printBoard(System.out);
				P1.printBoard(System.out);
				System.exit(1);
			}
			}
			
		}
		System.out.println("--------------------------------------");
		System.out.println("P2 Board is :");
		P2.printBoard(System.out);
		System.out.println("P1 Board is :");
		P1.printBoard(System.out);
		
		System.out.println("Player one (White) indicate winner as: "+ P1.getWinner());
		System.out.println("Player two (Black) indicate winner as: "+ P2.getWinner());
		System.out.println("Total Number of Moves Played in the Game: "+ NumberofMoves);
		System.out.println("Referee Finished !");
	}
	

}
