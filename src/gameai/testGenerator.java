/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        
        long totalTimeM = 0;
        long totalTimeS = 0;

         
        
        int trials = 1000;
        
        for (int i = 0; i < trials; i = i + 1) {
            GameBoard gb = generateBoard(6);

            try {
                PrintWriter out = new PrintWriter(new FileWriter("tests/board" + i + ".txt"));
                out.write("6\n" + gb.toString());
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(testGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                System.setOut(new PrintStream(new FileOutputStream("tests/outShaanan.txt", true)));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(testGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }

            final long startTimeS = System.nanoTime();
            GameBoard.run(new ByteArrayInputStream(("6\n" + gb.toString()).getBytes()));
            final long endTimeS = System.nanoTime();
            
            totalTimeS += endTimeS-startTimeS;
            
            PartA parta = new PartA();

            try {
                System.setOut(new PrintStream(new FileOutputStream("tests/outMitch.txt", true)));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(testGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            final long startTimeM = System.nanoTime();
            parta.run(new ByteArrayInputStream(("6\n" + gb.toString()).getBytes()));
            final long endTimeM = System.nanoTime();
            totalTimeM += endTimeM-startTimeM;

        }
        System.setOut(stdout);
        
        System.out.println("Average Time for Shaanan: " + totalTimeS/(10e9*trials));
        System.out.println("Average Time for Mitch: " + totalTimeM/(10e9*trials));
        
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
