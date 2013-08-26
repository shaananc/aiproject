/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author SHAANAN
 */
public class GameBoardTest {

    public GameBoardTest() {
        String test = 
                 "- - W W W -\n"
                + "- - W X - -\n"
                + "- - W - - -\n"
                + "- - - - - -\n"
                + "- - - - - -\n"
                + "B - - - - -\n";
        GameBoard gb = new GameBoard(new ByteArrayInputStream(("6\n"+test).getBytes()));
        assertEquals(test,gb.toString());
    
    }



    @Test
    public void testNumJumps() {
        String full = "6\n"
                + "B W - W - B\n"
                + "W X W B B W\n"
                + "X X W B X B\n"
                + "B X W W X W\n"
                + "X B B B X X\n"
                + "B B B X W W\n";
        GameBoard gb = new GameBoard(new ByteArrayInputStream(full.getBytes()));
        
        gb.turn = GameBoard.WHITE;
        assertEquals(4,gb.countJumpMoves());

        gb.turn = GameBoard.BLACK;
        assertEquals(2,gb.countJumpMoves());
        
    }
    
    @Test
    public void testNumPlaces(){
        
    }
}