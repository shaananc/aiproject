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
    double score;
    List<InternalMove> moveList;
    
    public ScoutRet(double score, List<InternalMove> moveList){
        this.score = score;
        this.moveList = moveList;
    }
    
    public ScoutRet(){
        this.moveList = new ArrayList<InternalMove>();
        
    }
}
