package gameai;

public class NegascoutMoveFinder extends MoveFinder {

    public NegascoutMoveFinder(int n, int player) {
        super(n, player);
    }

    @Override
    public Move getMove(GameBoardMitch gb) {
        Node root = new Node(gb, player);

        int alpha = -INF;
        int beta = INF;

        ScoutRetMitch ret = negascout(root, alpha, beta, 0);
        return ret.node.lastMove;
        
//        double bestUtility = negascout(root, alpha, beta, MAX_DEPTH);
//
//        for (Node childNode : root.childNodes) {
//            if (childNode.utility == bestUtility) {
//                return childNode.lastMove;
//            }
//        }

        // should never get here
//        System.out.println("FATAL ERROR");
//        System.exit(1);

//        return null;
    }

    public double getTrueUtility(Node node) {
        return negascout(node, -INF, INF, 0).score;
    }

    public ScoutRetMitch negascout(Node node, double alpha, double beta, int depth) {

        double b;
        
        ScoutRetMitch t;
        ScoutRetMitch a = new ScoutRetMitch();
        
        if (node.gb.getWinner() != EMPTY || depth >= MAX_DEPTH) {
             
            return new ScoutRetMitch(evaluate(node),node);
        }

        a.score = alpha;
        b = beta;

        node.getChildNodes();
        boolean isFirst = true;
        for (Node childNode : node.childNodes) {
            // search in null window
            t = negascout(childNode, -b, -a.score, depth + 1);
            t.score = -t.score;
            t.node = childNode;
            
            if ((t.score > a.score) && (t.score < beta) && !isFirst && (depth < MAX_DEPTH - 1)) {
                // initial search failed, continue with regular alpha beta
                childNode.resetChildren();
                a = negascout(childNode, -beta, -t.score, depth + 1);
                a.score = -a.score;
                a.node = childNode;
            }

            if (t.score > a.score) {
                a = t;
            }

            if (a.score >= beta) {
                return a;
            }

            b = a.score + 1;

            isFirst = false;
        }

        

        // remove non-essential children to conserve memory
//
        if (depth < MAX_DEPTH) {
            node.resetChildren();
        }

        return a;
    }
}
