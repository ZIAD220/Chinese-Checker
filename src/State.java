import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class State {
    public Marble [][] board;
    public boolean turn;
    public int heuristic;
    public ArrayList<Pair> player1;
    public ArrayList<Pair> player2;

    public State(Marble[][] board, boolean turn, ArrayList<Pair> player1, ArrayList<Pair> player2) {
        this.board = board;
        this.turn = turn;
        this.heuristic = 0;
        this.player1 = player1;
        this.player2 = player2;
    }
    public State(Marble[][] board, boolean turn) {
        this.board = board;
        this.turn = turn;
        this.heuristic = 0;
    }

    public State(Marble[][] board, boolean turn, int heuristic) {
        this.board = board;
        this.turn = turn;
        this.heuristic = heuristic;
    }

    public State clone()
    {
        Marble [][] cloneBoard = board.clone();
        for (int i=0; i<cloneBoard.length; i++) {
            cloneBoard[i] = cloneBoard[i].clone();
        }
        return new State(cloneBoard, turn, Utils.deepCopyPairs(player1), Utils.deepCopyPairs(player2));
    }

    @Override
    public String toString() {
        String result="";
        for (int i=0;i< board.length;i++){
            for (int j=0;j<board[0].length;j++){
                if(!board[i][j].isValid){
                    result+=" ";
                }
                else if(board[i][j].owner==0){
                    result+="-";
                }
                else if(board[i][j].owner==1){
                    result+="g";
                }
                else if(board[i][j].owner==2){
                    result+="r";
                }
            }
            result+="\n";
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        for (int i=0; i<board.length; i++) {
            Marble[] marbles = board[i];
            for (int j=0; j<marbles.length; j++) {
                if (!marbles[j].equals(state.board[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }
}
