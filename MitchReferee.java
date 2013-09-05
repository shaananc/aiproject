import csproj.jumper.*;

/* my implementation of referee class for jumper game
   until one provided by tutor makes an appearance
*/

public class MitchReferee implements Piece {
    
    public static void main(String[] args) {
        Mbrunton bot = new Mbrunton();
        HumanPlayer human = new HumanPlayer();
        
        int n = 6;
        bot.init(n, WHITE);
        human.init(n,  BLACK);
        System.out.println("Original board:");
        bot.printBoard(System.out);
        
        while (bot.getWinner() == EMPTY) {
            Move botMove = bot.makeMove();
            System.out.println("After bot's move:");
            bot.printBoard(System.out);
            if (bot.getWinner() != EMPTY) {
                break;
            }
            
            human.opponentMove(botMove);
            Move humanMove = human.makeMove();
            System.out.println("After human's move: ");
            human.printBoard(System.out);
            bot.opponentMove(humanMove);
        }

        int finalState = bot.getWinner();
        switch (finalState) {
        case BLACK:
            System.out.println("BLACK WINS!");
            break;
        case WHITE:
            System.out.println("WHITE WINS!");
            break;
        case DEAD:
            System.out.println("DRAW");
            break;
        case INVALID:
            System.out.println("Someone was very very cheeky and made an illegal move!");
            System.out.println("Invalid game...");
            break;
        }
    }
}
