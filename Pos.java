// Pos holds a position on a Gameboard
// where (i, j) goes from (0,0) to (n-1, n-1)
// i is row position, j is col position
// rows run from top to bottom, cols run from
// left to right

public class Pos {
	int i;
	int j;
	
	public Pos(int i, int j) {
		this.i = i;
		this.j = j;
	}
}
