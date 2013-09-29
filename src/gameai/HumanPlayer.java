package gameai;

import java.util.Scanner;

/* Implementation of Player interface for a human player
 * Only redefines makeMove method 
 */

//import csproj.jumper.*

public class HumanPlayer extends Mbrunton {
	Scanner in;
    public HumanPlayer() {
    	super();
    	in = new Scanner(System.in);
    }

    @Override
    public int init(int n, int p) {
    	int retVal = super.init(n, p);
    	System.out.println("WELCOME TO JUMPER!");
    	System.out.println("When it's your turn to move, do one of the following:");
    	System.out.println("\tEnter a \"place\" move of form i j, where 0 <= i,j < n");
    	System.out.println("\tEnter a \"jump\" move of form i1 j1 i2 j2 ... ik jk");
    	System.out.println("Have fun :)");
    	System.out.println();
    	return retVal;
    }
    
    @Override
    public Move makeMove() {
    	System.out.println("Enter move:");
    	Move m = null;
    	
    	boolean invalidInput = true;
    	while (invalidInput) {
    		invalidInput = false;
        	String input = in.nextLine();
        	String[] strArray = input.split(" ");
        	
        	if (strArray.length % 2 != 0) {
				System.out.println("Must supply even number of position values!");
				invalidInput = true;
				continue;
        	}
        	
        	int[] rowPositions = new int[strArray.length / 2];
    		int[] colPositions = new int[strArray.length / 2];
    		int k1 = 0;
    		int k2 = 0;
        	for (int i = 0; i < strArray.length; i++) {
        		try {
        			if (i % 2 == 0) {
        				rowPositions[k1] = Integer.parseInt(strArray[i]);
        				k1++;
        			} else {
        				colPositions[k2] = Integer.parseInt(strArray[i]);
        				k2++;
        			}
        		} catch (NumberFormatException e) {
        			System.out.println("Board positions must be integers!");
        			invalidInput = true;
        			continue;
        		}
        	}
    		
    		m = new Move(player, rowPositions.length == 1, rowPositions, colPositions);
    		if (!gb.isLegalMove(m)) {
    			System.out.println("Illegal move!");
    			invalidInput = true;
    		}
    	}
  
    	gb.applyMove(m);
    	return m;
    }
    
}

