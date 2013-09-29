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
	int ni;
	int nj;
	int nni;
	int nnj;

	public NeighbourPair(int ni, int nj, int nni, int nnj) {
		this.ni = ni;
		this.nj = nj;
		this.nni = nni;
		this.nnj = nnj;
	}
}