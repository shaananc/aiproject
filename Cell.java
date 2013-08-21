// Gameboard is composed of Cell objects
// Each cell can be in one of four states

public class Cell {
	public enum State {E, B, W, X};
	public enum Colour {Black, White};
	public State state;
	public Pos pos;
	
	public Cell () {
		state = State.E;
	}
	
	public Cell (State state) {
		this.state = state;
	}
	
	public void placeBlack() {
		state = State.B;
	}
	
	public void placeWhite() {
		state = State.W;
	}
	
	public void killCell() {
		state = State.X;
	}
	
	public Boolean isOccupied() {
		return state == State.B || state == State.W;
	}
}
