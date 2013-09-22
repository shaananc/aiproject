/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import static gameai.Piece.BLACK;
import static gameai.Piece.WHITE;
import java.io.File;
import org.encog.persist.EncogDirectoryPersistence;

/**
 *
 * @author shaananc
 */
public class MLPTrainer {

    private static Move lastPlayedMove;
    int n = 4;
    public MLPPlayer myMLPPlayer;
    public MLPPlayer myMLPPlayer2;
    public String FILENAME = "encognn.eg";

    public void run() {
        MLPTrainer trainer = new MLPTrainer();

        Player MLP1 = (Player) myMLPPlayer;
        Player MLP2 = (Player) myMLPPlayer2;
        Player NP = (Player) new NegaPlayer();
        Player MitchP = (Player) new Mbrunton();


        int i = 1;
        while (i <= 100000) {
            if (i % 1000 == 0) {
                trainer.myMLPPlayer.isLearning = false;
                trainer.myMLPPlayer2.isLearning = false;

                
                System.out.println("Versus NegaPlayer: " + trainer.playWithOutput(MLP1, NP));
                System.out.println("Versus MitchPlayer: " + trainer.playWithOutput(MLP1, MitchP));
                trainer.playWithOutput(MLP1, MitchP);

                trainer.myMLPPlayer2.isLearning = true;
                trainer.myMLPPlayer.isLearning = true;

                System.out.println(i);


            } else {
                trainer.playGame(MLP1, MLP2);
                trainer.playGame(MLP1, NP);
                //trainer.playGame(MLP1, MitchP);
                //trainer.playGame(NP, MLP1);
                //trainer.playGame(MitchP, MLP1);
            }

            if (i % 10 == 0) {
                EncogDirectoryPersistence.saveObject(new File("encognn.eg"), trainer.myMLPPlayer.network);
            }


            System.out.flush();
            i++;
        }


    }

    public MLPTrainer() {
        myMLPPlayer = new MLPPlayer(n);
        myMLPPlayer2 = new MLPPlayer(n, myMLPPlayer.network);
    }

    public void playGame(Player P1, Player P2) {

        lastPlayedMove = new Move();
        int boardEmptyPieces = n * n;

        P1.init(n, WHITE);
        P2.init(n, BLACK);

        while (boardEmptyPieces > 0) {
            lastPlayedMove = P1.makeMove();
            if (lastPlayedMove.IsPlaceMove) {
                boardEmptyPieces--;
            } else {
                for (int i = 1; i < lastPlayedMove.ColPositions.length; i++) {
                    boardEmptyPieces--;
                }
            }


            if (P2.opponentMove(lastPlayedMove) < 0) {

                System.exit(1);
            }



            if (boardEmptyPieces <= 0) {
                break;
            } else {
                lastPlayedMove = P2.makeMove();
                if (lastPlayedMove.IsPlaceMove) {
                    boardEmptyPieces--;
                } else {
                    for (int i = 1; i < lastPlayedMove.ColPositions.length; i++) {
                        boardEmptyPieces--;
                    }
                }

                if (P1.opponentMove(lastPlayedMove) < 0) {
                    System.exit(1);
                }
            }
        }
    }

    public String playWithOutput(Player P1, Player P2) {

        lastPlayedMove = new Move();
        int NumberofMoves = 0;
        int boardEmptyPieces = n * n;
        //System.out.println("Referee started !");

        P1.init(n, WHITE);
        P2.init(n, BLACK);


        while (boardEmptyPieces > 0) {

            NumberofMoves++;
            lastPlayedMove = P1.makeMove();
            if (lastPlayedMove.IsPlaceMove) {
                //System.out.println("Placing to. " + lastPlayedMove.RowPositions[0] + ":" + lastPlayedMove.ColPositions[0] + " by " + lastPlayedMove.P);
                boardEmptyPieces--;
            } else {
                //System.out.println("Jumpping from. " + lastPlayedMove.RowPositions[0] + ":" + lastPlayedMove.ColPositions[0] + " by " + lastPlayedMove.P);
                for (int i = 1; i < lastPlayedMove.ColPositions.length; i++) {
                    boardEmptyPieces--;
                    //System.out.println("To. " + lastPlayedMove.RowPositions[i] + ":" + lastPlayedMove.ColPositions[i]);
                }

            }




            if (P2.opponentMove(lastPlayedMove) < 0) {
                System.out.println("Exception: Player 2 rejected the move of player 1.");
                P1.printBoard(System.out);
                P2.printBoard(System.out);
                System.exit(1);
            }

            //P1.printBoard(System.out);
            // P2.printBoard(System.out);

            if (boardEmptyPieces <= 0) {
                break;
            } else {
                NumberofMoves++;
                lastPlayedMove = P2.makeMove();
                if (lastPlayedMove.IsPlaceMove) {
                    //System.out.println("Placing to. " + lastPlayedMove.RowPositions[0] + ":" + lastPlayedMove.ColPositions[0] + " by " + lastPlayedMove.P);
                    boardEmptyPieces--;
                } else {
                    //System.out.println("Jumping from. " + lastPlayedMove.RowPositions[0] + ":" + lastPlayedMove.ColPositions[0] + " by " + lastPlayedMove.P);
                    for (int i = 1; i < lastPlayedMove.ColPositions.length; i++) {
                        boardEmptyPieces--;
                        //System.out.println("To. " + lastPlayedMove.RowPositions[i] + ":" + lastPlayedMove.ColPositions[i]);
                    }

                }

                if (P1.opponentMove(lastPlayedMove) < 0) {
                    System.out.println("Exception: Player 1 rejected the move of player 2.");
                    P2.printBoard(System.out);
                    P1.printBoard(System.out);
                    System.exit(1);
                }

                //P1.printBoard(System.out);
                //P2.printBoard(System.out);

            }

        }
        //System.out.println("--------------------------------------");
        //System.out.println("P2 Board is :");
        //P2.printBoard(System.out);
        //System.out.println("Board is :");
        //P1.printBoard(System.out);

        String winner = "";


        switch (P1.getWinner()) {
            case 1:
                winner = "White";
                break;
            case 2:
                winner = "Black";
                break;
            case 3:
                winner = "Draw";
                break;
        }

        return winner;
        
        //System.out.println("Player two (Black) indicate winner as: " + P2.getWinner());
        //System.out.println("Total Number of Moves Played in the Game: " + NumberofMoves);
        //System.out.println("Referee Finished !");
    }

    public static void main(String args[]) {
        MLPTrainer trainer = new MLPTrainer();
        trainer.run();
    }
}
