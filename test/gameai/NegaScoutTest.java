/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import static gameai.Piece.BLACK;
import static gameai.Piece.WHITE;
import gameai.NegaScout;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author shaananc
 */
public class NegaScoutTest {

    private static Player P1;
    private static Player P2;
    private static Move lastPlayedMove;
    int n = 6;

    public NegaScoutTest() {
    }

   //@Test
    public void trialMove() {
        NegaPlayer P1 = new NegaPlayer();
        P1.init(2, WHITE);
        Move m = P1.makeMove();
        System.out.println(m.RowPositions[0] + ":" + m.ColPositions[0]);
   }

    @Test
    public void test() {

        lastPlayedMove = new Move();
        int NumberofMoves = 0;
        int boardEmptyPieces = n * n;
        System.out.println("Referee started !");

        //P2 = (Player) new NegaPlayer();
        P1 = (Player) new NegaPlayer();
        //P1 = (Player) new Mbrunton();
        P2 = (Player) new Mbrunton();

        P1.init(n, WHITE);
        P2.init(n, BLACK);


        while (boardEmptyPieces > 0) {

            NumberofMoves++;
            lastPlayedMove = P1.makeMove();
            if (lastPlayedMove.IsPlaceMove) {
                System.out.println("Placing to. " + lastPlayedMove.RowPositions[0] + ":" + lastPlayedMove.ColPositions[0] + " by " + lastPlayedMove.P);
                boardEmptyPieces--;
            } else {
                System.out.println("Jumpping from. " + lastPlayedMove.RowPositions[0] + ":" + lastPlayedMove.ColPositions[0] + " by " + lastPlayedMove.P);
                for (int i = 1; i < lastPlayedMove.ColPositions.length; i++) {
                    boardEmptyPieces--;
                    System.out.println("To. " + lastPlayedMove.RowPositions[i] + ":" + lastPlayedMove.ColPositions[i]);
                }

            }




            if (P2.opponentMove(lastPlayedMove) < 0) {
                System.out.println("Exception: Player 2 rejected the move of player 1.");
                P1.printBoard(System.out);
                P2.printBoard(System.out);
                System.exit(1);
            }

            P1.printBoard(System.out);
            P2.printBoard(System.out);

            if (boardEmptyPieces <= 0) {
                break;
            } else {
                NumberofMoves++;
                lastPlayedMove = P2.makeMove();
                if (lastPlayedMove.IsPlaceMove) {
                    System.out.println("Placing to. " + lastPlayedMove.RowPositions[0] + ":" + lastPlayedMove.ColPositions[0] + " by " + lastPlayedMove.P);
                    boardEmptyPieces--;
                } else {
                    System.out.println("Jumping from. " + lastPlayedMove.RowPositions[0] + ":" + lastPlayedMove.ColPositions[0] + " by " + lastPlayedMove.P);
                    for (int i = 1; i < lastPlayedMove.ColPositions.length; i++) {
                        boardEmptyPieces--;
                        System.out.println("To. " + lastPlayedMove.RowPositions[i] + ":" + lastPlayedMove.ColPositions[i]);
                    }

                }

                if (P1.opponentMove(lastPlayedMove) < 0) {
                    System.out.println("Exception: Player 1 rejected the move of player 2.");
                    P2.printBoard(System.out);
                    P1.printBoard(System.out);
                    System.exit(1);
                }

                P1.printBoard(System.out);
                P2.printBoard(System.out);

            }

        }
        System.out.println("--------------------------------------");
        System.out.println("P2 Board is :");
        P2.printBoard(System.out);
        System.out.println("P1 Board is :");
        P1.printBoard(System.out);

        System.out.println("Player one (White) indicate winner as: " + P1.getWinner());
        System.out.println("Player two (Black) indicate winner as: " + P2.getWinner());
        System.out.println("Total Number of Moves Played in the Game: " + NumberofMoves);
        System.out.println("Referee Finished !");
    }
}
