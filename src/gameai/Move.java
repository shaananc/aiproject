package gameai;
/*
 *   Move:
 *      Which can be a place or a jump
 *      
 *   @author msalehi
 *   
 */

public class Move implements Piece {
	public int P;
	public boolean IsPlaceMove;
	public int RowPositions[];
	public int ColPositions[];
	
	public Move()
	{
		P = EMPTY;
		IsPlaceMove = true;
		//RowPositions = ;
		//ColPositions = ;		
		
	}
	public Move(int player, boolean ip, int[] r, int[] c)
	{
		P = player;
		IsPlaceMove = ip;
		RowPositions = r;
		ColPositions = c;
		
	}
	
}