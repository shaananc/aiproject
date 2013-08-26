/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SHAANAN
 */
public class testGenerator {

    public static void main(String[] args) {
        PrintStream stdout = System.out;
        try {
            System.setOut(new PrintStream("tests/out.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(testGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (int i = 0; i < 100; i = i + 1) {
            GameBoard gb = generateBoard(6);
            
            try {
                PrintWriter out = new PrintWriter(new FileWriter("tests/board"+i+".txt"));
                out.write("6\n"+gb.toString());
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(testGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            GameBoard.run(new ByteArrayInputStream(("6\n"+gb.toString()).getBytes()));
        }
        System.setOut(stdout);
    }

    public static GameBoard generateBoard(int n) {
        GameBoard gb = new GameBoard(n);

        Random gen = new Random();
        // pick random fill size
        int fill = gen.nextInt(n * n * 3);
        for (int i = 0; i <= fill; i += 1) {
            //pick random square
            int loc = gen.nextInt(n * n);
            //pick random state
            int state = gen.nextInt(6);
            switch (state) {
                case 0:
                    break;
                case 1:
                case 2:
                    gb.setBlack(loc);
                    break;
                case 3:
                case 4:
                    gb.setWhite(loc);
                    break;
                case 5:
                    gb.setDead(loc);
                    break;
                default:
                    break;

            }
        }

        return gb;
    }
}
