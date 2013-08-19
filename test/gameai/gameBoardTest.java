/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author SHAANAN
 */
public class gameBoardTest {

    public gameBoardTest() {
        String test = "- - W W W -\n"
                + "- - W X - -\n"
                + "- - W - - -\n"
                + "- - - - - -\n"
                + "- - - - - -\n"
                + "B - - - - -\n";
        gameBoard gb = new gameBoard(6, test);
        System.out.println(gb);
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testNumJumps() {
        String full = "B W - W - B\n"
                + "W X W B B W\n"
                + "X X W B X B\n"
                + "B X W W X W\n"
                + "X B B B X X\n"
                + "B B B X W W\n";
        gameBoard gb = new gameBoard(6, full);
        System.out.println(gb);
        gb.turn = gameBoard.WHITE;
        List<Move> whiteJumps = gb.getJumpMoves();
        System.out.println("W " + whiteJumps.size());
        gb.printMoves(whiteJumps);
        //gb.turn = gameBoard.BLACK;
        //System.out.println("B " + gb.getJumpMoves().size());
    }
}