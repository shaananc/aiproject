/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SHAANAN
 */
public class ScoutRet {
    int score;
    List<Move> moveList;
    
    public ScoutRet(int score, List<Move> moveList){
        this.score = score;
        this.moveList = moveList;
    }
    
    public ScoutRet(){
        this.moveList = new ArrayList<Move>();
        
    }
}
