package gameai;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SHAANAN
 */
public class GameBoardShaanan implements GameBoard {

    protected static final boolean WHITE = false;
    protected static final boolean BLACK = true;
    boolean turn = WHITE;
    BitSet pieces;
    BitSet mask;
    int n;
    int depth;

    public GameBoardShaanan(int n) {

        pieces = new BitSet(n);
        mask = new BitSet(n);
        pieces.set(0, n, false);
        mask.set(0, n, false);
        this.n = n;
        depth = 0;
    }

    public GameBoardShaanan(int n, String boardString) {
        this(n);

        int loc;
        for (loc = 0; loc < n * n; loc++) {
            char t = boardString.charAt(loc);
            if (t == '-') {
            } else if (t == 'B') {
                mask.set(loc);
            } else if (t == 'W') {
                mask.set(loc);
                pieces.set(loc);
            } else if (t == 'X') {
                pieces.set(loc);
            } else {
                System.out.println("Error parsing boardString -- unidentified char:" + t);
                System.exit(0);
            }
        }
    }

    public GameBoardShaanan(InputStream boardStream) {
        Scanner s = new Scanner(boardStream);
        n = s.nextInt();

        pieces = new BitSet(n);
        mask = new BitSet(n);


        int loc = 0;
        while (s.hasNext()) {
            String t = s.next();
            if (t.equals("-")) {
            } else if (t.equals("B")) {
                mask.set(loc);
                pieces.set(loc);
            } else if (t.equals("W")) {
                mask.set(loc);
            } else if (t.equals("X")) {
                pieces.set(loc);
            } else if (t.equals("EOF")) {
                return;
            } else {
                System.out.println("Error parsing boardString -- unidentified char:" + t);
                System.exit(0);
            }
            loc += 1;
        }

        depth = 0;
    }

    public int getWinner() {
        if (!isOver()) {
            return 0;
        }
        int[] n = getNumbers();
        if (n[0] > n[1]) {
            return 1;
        } else if (n[1] > n[0]) {
            return 2;
        } else {
            return 3;
        }
    }

    // 11 black, 10, white, 00, clear, 01 X
    public boolean isWhite(int loc) {
        return (!pieces.get(loc) && mask.get(loc));
    }

    public boolean isBlack(int loc) {
        return (pieces.get(loc) && mask.get(loc));
    }

    public boolean isEmpty(int loc) {
        return (!pieces.get(loc) && !mask.get(loc));
    }

    public boolean isDead(int loc) {
        return (pieces.get(loc) && !mask.get(loc));
    }

    public boolean isMyPiece(int loc) {
        return (mask.get(loc) && (pieces.get(loc) == turn));
    }

    public boolean isEnemyPiece(int loc) {
        return (mask.get(loc) && (pieces.get(loc) != turn));
    }

    public boolean isSameColor(int loc1, int loc2) {
        return (mask.get(loc1) == mask.get(loc2)) && (pieces.get(loc1) == pieces.get(loc2));
    }

    public boolean isFull(int loc) {
        return mask.get(loc);
    }

    public void setWhite(int loc) {
        pieces.clear(loc);
        mask.set(loc);
    }

    public void setBlack(int loc) {
        pieces.set(loc);
        mask.set(loc);
    }

    public void setEmpty(int loc) {
        pieces.clear(loc);
        mask.clear(loc);
    }

    public void setDead(int loc) {
        pieces.set(loc);
        mask.clear(loc);
    }

    public GameBoardShaanan executeMove(InternalMove move) {

        int x = move.x;
        int y = move.y;

        GameBoardShaanan gb = (GameBoardShaanan) this.deepCopy();


        int num = y * n + x;


        /* If the space is already taken, the move is invalid */
        if (gb.mask.get(num)) {
            throw new IllegalArgumentException();
        }


        /* implementing simple placement */

        if (turn == WHITE) {
            gb.setWhite(num);
        } else {
            gb.setBlack(num);
        }

        if (move.jumpedSquare == InternalMove.PLACE) {

            gb.turn = !turn;

        } else if (move.jumpedSquare != -1
                && !(gb.isWhite(move.x + n * move.y) && gb.isWhite(move.jumpedSquare))
                && !(gb.isBlack(move.x + n * move.y) && gb.isBlack(move.jumpedSquare))) {

            gb.setDead(move.jumpedSquare);
        }

        gb.depth = depth + 1;

        return gb;
    }

    public GameBoardShaanan executeMove(Move m) {


        GameBoardShaanan gb = (GameBoardShaanan) this.deepCopy();


        if (m.IsPlaceMove) {
            if (m.P == Move.WHITE) {
                gb.setWhite(m.ColPositions[0] + n * m.RowPositions[0]);
            } else {
                gb.setBlack(m.ColPositions[0] + n * m.RowPositions[0]);
            }
        } else {
            int i;
            for (i = 0; i < m.RowPositions.length - 1; i++) {
                int signx = m.ColPositions[i] < m.ColPositions[i + 1] ? 1 : -1;
                int signy = m.RowPositions[i] < m.RowPositions[i + 1] ? 1 : -1;
                int jumpedx = m.ColPositions[i] + signx * Math.abs(m.ColPositions[i] - m.ColPositions[i + 1]) / 2;
                int jumpedy = m.RowPositions[i] + signy * Math.abs(m.RowPositions[i] - m.RowPositions[i + 1]) / 2;
                int jumpedloc = jumpedx + n * jumpedy;


                if (m.P == Move.WHITE) {
                    gb.setWhite(m.ColPositions[i + 1] + n * m.RowPositions[i + 1]);
                } else {
                    gb.setBlack(m.ColPositions[i + 1] + n * m.RowPositions[i + 1]);
                }


                if (!(gb.isWhite(jumpedloc) && m.P == Move.WHITE)
                        && !(gb.isBlack(jumpedloc) && m.P == Move.BLACK)) {
                    gb.setDead(jumpedloc);
                }


            }
        }

        gb.depth = depth + 1;

        gb.turn = !gb.turn;

        return gb;
    }

    /* TODO: Is it best to return an integer list? 
     * Is this the best algorithm to get moves? */
    /* O(size) */
    public List<InternalMove> getPlaceMoves() {
        List<InternalMove> moves = new ArrayList<InternalMove>();
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                int num = y * n + x;
                if (isEmpty(num)) {
                    InternalMove m = new InternalMove();
                    m.x = x;
                    m.y = y;
                    moves.add(m);
                }
            }
        }
        return moves;
    }

    public int countPlaceMoves() {
        int count = 0;
        for (int loc = 0; loc < n * n; loc += 1) {
            if (isEmpty(loc)) {
                count += 1;
            }
        }
        return count;
    }

    public List<List<InternalMove>> getJumpMoves() {
        List<List<InternalMove>> all_moves = new ArrayList<List<InternalMove>>();

        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                int num = y * n + x;
                /* If the space is populated by your color and is a jump */
                if (isMyPiece(num)) {
                    for (InternalMove move : getSpaceJumps(x, y)) {
                        List<InternalMove> path = new ArrayList<InternalMove>();
                        path.add(move);
                        all_moves.add(path);
                        enumerateJumpTree(move, executeMove(move), path, all_moves);
                    }
                }
            }
        }


        return all_moves;
    }

    public void enumerateJumpTree(InternalMove m, GameBoardShaanan gb, List<InternalMove> path, List<List<InternalMove>> all_moves) {
        //System.out.println("Enumerating Tree From: " + "\n");
        //System.out.println(gb);

        List<InternalMove> possibleMoves = gb.getSpaceJumps(m.x, m.y);
        if (possibleMoves.isEmpty()) {
            return;
        } else {
            for (InternalMove m_prime : possibleMoves) {
                List<InternalMove> path_prime = new ArrayList<InternalMove>(path);
                path_prime.add(m_prime);
                all_moves.add(path_prime);
                GameBoardShaanan gb_prime = gb.executeMove(m_prime);
                //System.out.println("move in jump tree: " + m.x + ":" + m.y + "\n");
                //System.out.println(gb);
                enumerateJumpTree(m_prime, gb_prime, path_prime, all_moves);
            }
        }
    }

    public int countJumpMoves() {
        int count = 0;
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                if (isMyPiece(y * n + x)) {
                    for (InternalMove move : getSpaceJumps(x, y)) {
                        count += 1;
                        count += jumpDFS(move, this);
                    }
                }
            }
        }
        return count;
    }

    public GameBoardShaanan executeCompound(List<InternalMove> moves) {
        GameBoardShaanan gb = this.deepCopy();
        boolean jump = true;
        if (moves.get(0).jumpedSquare == InternalMove.PLACE) {
            jump = false;
        }
        for (int i = 0; i < moves.size(); i++) {
            gb = gb.executeMove(moves.get(i));
        }
        // Flip turns for jump
        if (jump) {
            gb.turn = !gb.turn;
        }

        return gb;
    }

    public int jumpDFS(InternalMove move, GameBoardShaanan gb) {

        int count = 0;

        GameBoardShaanan gb_prime = gb.executeMove(move);
        for (InternalMove move_prime : gb_prime.getSpaceJumps(move.x, move.y)) {
            count += 1;
            count += jumpDFS(move_prime, gb_prime);
        }

        return count;
    }

    public List<Integer> getAdjacent(int loc) {
        List<Integer> l = new LinkedList<Integer>();

        int x = loc % n;
        int y = loc / n;

        int[] dirs = new int[]{1, -1, n, -n, n + 1, n - 1, -n + 1, -n - 1};

        for (int i = 0; i < 8; i++) {
            int candidate = loc + dirs[i];

            // Check square on board
            if (candidate > n * n - 1 || candidate < 0) {
                continue;
            }
            // Left Out of Bounds
            if ((i == 5 || i == 7 || i == 1) && x - 2 < 0) {
                continue;
            }

            // Right Out of Bounds
            if ((i == 4 || i == 6 || i == 0) && x + 2 >= n) {
                continue;
            }

            l.add(candidate);

        }

        return l;

    }

    private List<InternalMove> getSpaceJumps(int x, int y) {
        int[] dirs = new int[]{1, -1, n, -n, n + 1, n - 1, -n + 1, -n - 1};

        int num = y * n + x;

        // Right 0, Left 1, Down 2, Up 3, Down-Right 4, Down-Left 5, Up-Right 6, Up-Left, 7
        //{1, -1, n, -n, n + 1, n - 1, -n + 1, -n - 1};
        List<InternalMove> moves = new ArrayList<InternalMove>();

        for (int i = 0; i < 8; i++) {

            int candidateSpace = num + dirs[i] * 2;
            int jumpedSquare = num + dirs[i];

            // Check square on board
            if (candidateSpace > n * n - 1 || jumpedSquare > n * n - 1
                    || candidateSpace < 0 || jumpedSquare < 0) {
                continue;
            }

            // Left Out of Bounds
            if ((i == 5 || i == 7 || i == 1) && x - 2 < 0) {
                continue;
            }

            // Right Out of Bounds
            if ((i == 4 || i == 6 || i == 0) && x + 2 >= n) {
                continue;
            }

            if (isFull(jumpedSquare) && isEmpty(candidateSpace)) {
                //int movesJump = -2;

                int movesJump = jumpedSquare;


                moves.add(new InternalMove(candidateSpace % n, candidateSpace / n, movesJump));
            }

        }

        return moves;
    }

    /* O(size) printing of the board */
    @Override
    public String toString() {
        String str = "";
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {

                int loc = y * n + x;
                if (isWhite(loc)) {
                    str = str + "W";
                } else if (isBlack(loc)) {
                    str = str + "B";
                } else if (isDead(loc)) {
                    str = str + "X";
                } else {
                    str = str + "-";
                }

                str = str + " ";
            }

            str = str.trim();
            str = str + "\n";
        }
        return str;

    }

    public GameBoardShaanan deepCopy() {
        GameBoardShaanan gb = new GameBoardShaanan(this.n);
        gb.turn = turn;
        gb.mask = (BitSet) mask.clone();
        gb.pieces = (BitSet) pieces.clone();
        return gb;
    }

    public void printMoves(Collection<InternalMove> moves) {
        Queue<InternalMove> toplay = new LinkedList<InternalMove>();
        toplay.addAll(moves);
        while (!toplay.isEmpty()) {
            System.out.println(this.executeMove(toplay.remove()));
        }
    }

    public int getDepth() {
        BitSet over = ((BitSet) pieces.clone());
        over.or(mask);
        return over.cardinality();
    }

    public boolean isOver() {

        return getDepth() >= n * n;
    }

    public int[] getNumbers() {
        int[] ret = new int[2];
        ret[0] = 0;
        ret[1] = 0;
        for (int i = 0; i < n * n; i++) {
            if (isWhite(i)) {
                ret[0] += 1;
            }
            if (isBlack(i)) {
                ret[1] += 1;
            }
        }
        return ret;
    }

    public List<List<InternalMove>> getMoves() {
        List<List<InternalMove>> ret = getJumpMoves();
        for (InternalMove m : getPlaceMoves()) {
            List t = new ArrayList();
            t.add(m);
            ret.add(t);
        }
        return ret;

    }

    public static void run(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String input;
            StringBuilder boardStringBuilder = new StringBuilder();

            int n = new Integer(br.readLine());
            if (n <= 0) {
                System.out.println("Input must begin with positive integer n");
                System.exit(1);
            }

            int i = 0;
            while ((input = br.readLine()) != null) {
                boardStringBuilder.append(input.replace(" ", "").replace("\n", ""));
                i++;
            }
            if (i != n) {
                System.out.println("Must supply n rows of gameboard");
                System.exit(1);
            }

            String boardString = boardStringBuilder.toString();
            if (boardString.length() != n * n) {
                System.out.println("Row of incorrect length supplied");
                System.exit(1);
            }


            GameBoardShaanan gb = new GameBoardShaanan(n, boardString);
            gb.turn = GameBoardShaanan.WHITE;
            System.out.println("W " + gb.countPlaceMoves() + " " + gb.countJumpMoves());
            gb.turn = GameBoardShaanan.BLACK;
            System.out.println("B " + gb.countPlaceMoves() + " " + gb.countJumpMoves());
        } catch (IOException ex) {
            Logger.getLogger(GameBoardShaanan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        run(System.in);
    }
}
