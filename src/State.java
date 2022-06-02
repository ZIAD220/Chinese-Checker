public class State {
    public Marble [][] board;
    public boolean turn;
    public int heuristic;

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
}
