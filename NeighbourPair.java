// used for when one is looking for possible jump moves, and is considering pairs of cells which are
// both adjacent and 2-away from a given cell

public class NeighbourPair {
	Pos neighbour;
	Pos neighboursNeighbour;
	
	public NeighbourPair(Pos neighbour, Pos neighboursNeighbour) {
		this.neighbour = neighbour;
		this.neighboursNeighbour = neighboursNeighbour;
	}
}
