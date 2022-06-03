import java.util.ArrayList;

public class Utils {
    public static ArrayList<Pair> deepCopyPairs(ArrayList<Pair> pairs) {
        ArrayList<Pair> result = new ArrayList<>();
        for (Pair pair: pairs) {
            result.add((Pair)pair.clone());
        }
        return result;
    }

    public static double getEucDistance(int xs, int ys, int xd, int yd) {
        return Math.sqrt(Math.pow(xd - xs, 2) + Math.pow(yd-ys, 2));
    }

    public static State cloneState(State state) {
        Marble [][] cloneBoard = state.board.clone();
        for (int i=0; i<cloneBoard.length; i++) {
            for (int j=0; j<cloneBoard[0].length; j++) {
                cloneBoard[i][j] = (Marble) cloneBoard[i][j].clone();
            }
        }
        return new State(cloneBoard, state.turn, Utils.deepCopyPairs(state.player1), Utils.deepCopyPairs(state.player2));
    }
}
