package gameai;

/**
 *
 * @author SHAANAN
 */

// Todo - Change x,y to a single int
public class InternalMove  {

    protected static final int SELF_JUMP = -1;
    protected static final int PLACE = -2;
    int x, y;
    int jumpedSquare;

    public InternalMove() {
        this.jumpedSquare = PLACE;
    }

    public InternalMove(int x, int y) {
        this.x = x;
        this.y = y;
        this.jumpedSquare = PLACE;
    }

    public InternalMove(int x, int y, int jumpedSquare) {
        this(x,y);
        this.jumpedSquare = jumpedSquare;
    }
    
    
}
