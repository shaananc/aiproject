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

    private static Player P1;
    private static Player P2;
    private static Move lastPlayedMove;
    int n = 5;
    public MLPPlayer myMLPPlayer;
    public MLPPlayer myMLPPlayer2;
    public String FILENAME = "encognn.eg";

    public static void main(String[] args) {
        //test();
        MLPTrainer trainer = new MLPTrainer();

        int i = 0;
        while (true) {
            if (i % 10 == 0) {
//                trainer.myMLPPlayer.pauseLearning();
                trainer.myMLPPlayer.isLearning = false;
//                trainer.myMLPPlayer2.pauseLearning();
                trainer.myMLPPlayer2.isLearning = false;

                trainer.playWithOutput();
                //trainer.myMLPPlayer.MLP.save("mlp.nnet");

//                trainer.myMLPPlayer2.resumeLearning();
                trainer.myMLPPlayer2.isLearning = true;
//                trainer.myMLPPlayer.resumeLearning();
                trainer.myMLPPlayer.isLearning = true;





            } else {

                trainer.playGame();

            }

            if (i % 10 == 0) {
//                File f = new File("mlp.nnet");
//                if (f.exists()) {
//                    f.delete();
//
//                }
                //trainer.myMLPPlayer.MLP.save("mlp.nnet");
                EncogDirectoryPersistence.saveObject(new File("encognn.eg"), trainer.myMLPPlayer.network);
            }
            System.out.println("\nGame number: " + i);
            System.out.flush();
            i++;
        }


    }

    public MLPTrainer() {
        myMLPPlayer = new MLPPlayer(n);
        myMLPPlayer2 = new MLPPlayer(n, myMLPPlayer.network);
    }

    public void playGame() {

        lastPlayedMove = new Move();
        int boardEmptyPieces = n * n;


        P1 = (Player) myMLPPlayer;
        P2 = (Player) myMLPPlayer2;

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

    public void playWithOutput() {

        lastPlayedMove = new Move();
        int NumberofMoves = 0;
        int boardEmptyPieces = n * n;
        System.out.println("Referee started !");

        //P2 = (Player) new NegaPlayer();
        P1 = (Player) myMLPPlayer;
        //P1 = (Player) new Mbrunton();
        P2 = (Player) new NegaPlayer();

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
