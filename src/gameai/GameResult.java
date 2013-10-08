package gameai;
import java.util.ArrayList;

/* Class to store the results of a game of jumper */

public class GameResult implements Piece {

	int winner;
	int victoryMetric;	// how much did winner win by
	long p1TimeMillis;
	long p2TimeMillis;
	long gameTimeMillis;
	ArrayList<Long> p1Times;
	ArrayList<Long> p2Times;
	
	public GameResult() {
		winner = EMPTY;
		victoryMetric = 0;
		p1TimeMillis = 0;
		p2TimeMillis = 0;
		gameTimeMillis = 0;
		p1Times = new ArrayList<Long>();
		p2Times = new ArrayList<Long>();
	}
	
	public void incrementP1Time(long millis) {
		p1TimeMillis += millis;
		p1Times.add(millis);
	}
	
	public void incrementP2Time(long millis) {
		p2TimeMillis += millis;
		p2Times.add(millis);
	}
	
	public void incrementGameTime(long millis) {
		gameTimeMillis += millis;
	}
	
	public void setWinner(int winner, GameBoard gb) {
		this.winner = winner;
	}
	
	public void setVictoryMetric(GameBoardMitch finalGb) {
		int numPlayer = 0;
		int numEnemy = 0;
		for (int i = 0; i < finalGb.n; i++) {
			for (int j = 0; j < finalGb.n; j++) {
				int piece = finalGb.board[i * finalGb.n + j];
				if (piece == winner) {
					numPlayer++;
				} else if (piece != EMPTY && piece != DEAD) {
					// piece is enemy piece
					numEnemy++;
				}
			}
		}
		
		victoryMetric = numPlayer - numEnemy;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("GAME RESULT:\n");
		buffer.append("winner: ");
		
		switch (winner) {
		case BLACK:
			buffer.append("BLACK\n");
			break;
		case WHITE:
			buffer.append("WHITE\n");
			break;
		case DEAD:
			buffer.append("DRAW\n");
			break;
		case INVALID:
			buffer.append("INVALID GAME\n");
			return buffer.toString();
		case EMPTY:
			System.out.println("Error: toString() call on non-terminated GameResult object");
			System.exit(1);
		}
		
		buffer.append("victory metric: " + victoryMetric + "\n");
		buffer.append("player 1 time (millis): " + p1TimeMillis + "\n");
		buffer.append("player 2 time (millis): " + p2TimeMillis + "\n");
		buffer.append("player 1 times: ");
		for (Long p1Time : p1Times) {
			buffer.append(p1Time + " ");
		}
		buffer.append("\n");
		buffer.append("player 2 times: ");
		for (Long p2Time : p2Times) {
			buffer.append(p2Time + " ");
		}
		buffer.append("\n");
		buffer.append("total game time (millis): " + gameTimeMillis + "\n");
		return buffer.toString();
	}
}
