/* Mitchell Brunton - mbrunton
   Shaanan Cohney   - sncohney
*/

/* NeighbourPair objects hold two Pos (position) values, one representing
   the neighbour of a given cell, and the other representing that neighbour's
   neighbour, in the same direction. They are used when looking for possible
   jump moves
*/
package gameai;

public class NeighbourPair {
	Pos neighbour;
	Pos neighboursNeighbour;
	
	public NeighbourPair(Pos neighbour, Pos neighboursNeighbour) {
		this.neighbour = neighbour;
		this.neighboursNeighbour = neighboursNeighbour;
	}
}
