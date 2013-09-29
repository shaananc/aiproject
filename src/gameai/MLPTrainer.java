/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import static gameai.Piece.BLACK;
import static gameai.Piece.WHITE;
import java.io.File;
import org.encog.Encog;
import org.encog.neural.networks.BasicNetwork;
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

        Player MLP1 = (Player) myMLPPlayer;
        Player MLP2 = (Player) myMLPPlayer2;
        Player NP = (Player) new NegaPlayer();
        Player Random = (Player) new RandomPlayer();
        Player MitchP = (Player) new Mbrunton();
        Player Human = (Player) new HumanPlayer();


        int i = 0;
        while (i <= 3000) {
            if (i % 500 == 0) {
                myMLPPlayer.isLearning = false;
                myMLPPlayer2.isLearning = false;


                String w1 = playGame(MLP1, NP, true);
                String w2 = playGame(MLP1, MitchP, true);
                String w3 = playGame(MLP1, Random, true);
                System.out.println("Versus NegaPlayer: " + w1);
                System.out.println("Versus MitchPlayer: " + w2);
                System.out.println("Versus Random: " + w3);
                
                if(w1.equals("White") && w2.equals("White")){
                    break;
                }

                myMLPPlayer2.isLearning = true;
                myMLPPlayer.isLearning = true;

                System.out.println(i);



            } else {
                playGame(MLP1, MLP2, false);
                //playGame(MLP1, NP, false);
                //playGame(MLP1, MitchP, false);
                // playGame(NP, MLP1,false);
                //playGame(MitchP, MLP1,false);
            }




            System.out.flush();
            i++;
        }

        System.out.println(myMLPPlayer.network.dumpWeights());
        EncogDirectoryPersistence.saveObject(new File(FILENAME), myMLPPlayer.network);
        //BasicNetwork network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(FILENAME));
        //System.out.println(network.dumpWeights());
        Encog.getInstance().shutdown();

    }

    public MLPTrainer() {
        try {
            BasicNetwork network = (BasicNetwork) EncogDirectoryPersistence.loadObject(new File(FILENAME));
            myMLPPlayer = new MLPPlayer(n, network);
            System.out.println(myMLPPlayer.network.dumpWeights());

        } catch (Exception e) {
            myMLPPlayer = new MLPPlayer(n);
        }
        myMLPPlayer2 = new MLPPlayer(n, myMLPPlayer.network);
    }

    public String playWithOutput(Player P1, Player P2) {

        lastPlayedMove = new Move();
        int NumberofMoves = 0;
        int boardEmptyPieces = n * n;
        System.out.println("Referee started !");

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
            // P2.printBoard(System.out);

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
                //P2.printBoard(System.out);

            }

        }
        //System.out.println("--------------------------------------");
        //System.out.println("P2 Board is :");
        //P2.printBoard(System.out);
        System.out.println("Board is :");
        P1.printBoard(System.out);

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

        //return winner;

        System.out.println("Player two (Black) indicate winner as: " + P2.getWinner());
        System.out.println("Total Number of Moves Played in the Game: " + NumberofMoves);
        System.out.println("Referee Finished !");
        return winner;
    }

    public String playGame(Player P1, Player P2, boolean showBoard) {

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

        if (showBoard) {
            P1.printBoard(System.out);
            P2.getWinner();
        }
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
        while (true) {
            MLPTrainer trainer = new MLPTrainer();
            trainer.run();
        }
    }
}
