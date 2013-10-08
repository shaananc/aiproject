package gameai;
import java.util.Random;


public class RandomMoveFinder extends MoveFinder {

	public RandomMoveFinder(int n, int player) {
		super(n, player);
	}
	
	@Override
	public Move getMove(GameBoardMitch gb) {
		Node root = new Node(gb, player);
		root.getChildNodes();
		Random rand = new Random();
		int i = rand.nextInt(root.childNodes.size());
		return root.childNodes.get(i).lastMove;
	}
	
	public double getTrueUtility(Node node) {
		return evaluate(node);
	}
}
