/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author SHAANAN
 */
public class Move {
    
    public Move(){}
    
    public Move(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public Move(int x, int y, boolean jump){
        this.x = x;
        this.y = y;
        this.jump = jump;
        this.compJumps = new LinkedList();
    }
    
    int x,y;
    boolean jump;
    int jumpedSquare;
    List<Move> compJumps;
}
