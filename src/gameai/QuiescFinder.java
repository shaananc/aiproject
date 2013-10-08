/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gameai;

/**
 *
 * @author shaananc
 */
public class QuiescFinder extends MoveFinder {

    public QuiescFinder(int n, int player) {
        super(n, player);
    }

    double Quiesce(double alpha, double beta, Node node) {
        double stand_pat = evaluate(node);
        if (stand_pat >= beta) {
            return beta;
        }
        if (alpha < stand_pat) {
            alpha = stand_pat;
        }

        node.getChildJumpNodes();
        int pd = node.gb.getDensity();

        for (Node newNode : node.childNodes) {

            int cd = newNode.gb.getDensity();
            
            
            if (cd - 3 < pd) {
                continue;
            }

            double score = -Quiesce(-beta, -alpha, newNode);

            if (score >= beta) {
                return beta;
            }
            if (score > alpha) {
                alpha = score;
            }
        }
        return alpha;
    }

    @Override
    public Move getMove(GameBoardMitch gb) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getTrueUtility(Node node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
