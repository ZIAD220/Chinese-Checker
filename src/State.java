import java.util.ArrayList;

public class State {
    public Marble [][] board;
    public boolean turn;
    public int heuristic;
    public ArrayList<Pair> player1;
    public ArrayList<Pair> player2;

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
        return new State(board.clone(), turn);
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
}
