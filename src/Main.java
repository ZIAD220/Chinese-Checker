import jdk.jshell.execution.Util;

import java.util.*;

public class Main {
    public static final int[] bottomCenter = {15, 13};
    public static final int[] topCenter = {3, 13};
    public static int dificultyLevel = 3;
    public static void startGame()
    {
        Marble[][] board = new Marble[18][26];
        init_board(board);
        ArrayList<Pair> p1_marbles = new ArrayList<>();
        ArrayList<Pair> p2_marbles = new ArrayList<>();
        init_moves(board, p1_marbles, p2_marbles);
        State initialState=new State(board,true); //computer turn
        initialState.player1=p1_marbles;
        initialState.player2=p2_marbles;
        ArrayList<State> states=new ArrayList<>();
//        for(int i = 0; i < p1_marbles.size(); i++)
//            System.out.println(p1_marbles.get(i).x + " " + p1_marbles.get(i).y);
        getChildren(initialState,states);
//        System.out.print(states);
    }

    public static byte getWinner(State state) {
        Marble[][] board = state.board;
        if (board[14][10].owner == 1 && board[14][12].owner == 1 && board[14][14].owner == 1 && board[14][16].owner == 1 &&
                board[15][11].owner == 1 && board[15][13].owner == 1 && board[15][15].owner == 1 &&
                board[16][12].owner == 1 && board[16][14].owner == 1 && board[17][13].owner == 1) {
            return 1;
        }
        if (board[1][13].owner == 2 &&  board[2][12].owner == 2 && board[2][14].owner == 2 && board[3][11].owner == 2 && board[3][13].owner == 2 &&
                board[3][15].owner == 2 && board[4][10].owner == 2 && board[4][12].owner ==2 && board[4][14].owner == 2 && board[4][16].owner == 2){
            return 2;
        }
        return 0;
    }

    public static int minimax(State state, int depth, int alpha, int beta) {
        if (depth == 0 || getWinner(state) != 0) {
            return evalState(state);
        }
        ArrayList<State> children = new ArrayList<>();
        getChildren(state, children);

        if(state.turn) {
            int maxEval = Integer.MIN_VALUE;
            for(State child : children) {
                int eval = minimax(child, depth - 1, alpha, beta);
                maxEval = Integer.max(maxEval, eval);
                alpha = Integer.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else {
        int minEval = Integer.MAX_VALUE;
        for(State child : children){
            int eval = minimax(child, depth - 1, alpha, beta);
            minEval = Integer.min(minEval, eval);
            beta = Integer.min(beta, eval);
            if(beta <= alpha) {
                break;
            }
        }
        return minEval;
        }
    }

    public static int evalState(State state) {
        if (getWinner(state) == 1) {
            return Integer.MAX_VALUE;
        }
        if (getWinner((state)) == 2) {
            return Integer.MIN_VALUE;
        }
        ArrayList<Pair> computerMarbles = state.turn ? state.player1 : state.player2;
        ArrayList<Pair> humanMarbles = !state.turn ? state.player1 : state.player2;
        double computerDistance = 0, humanDistance = 0;
        for (int i=0; i<computerMarbles.size(); i++) {
            computerDistance += Utils.getEucDistance(computerMarbles.get(i).x, computerMarbles.get(i).y, bottomCenter[0], bottomCenter[1]);
            humanDistance += Utils.getEucDistance(humanMarbles.get(i).x, humanMarbles.get(i).y, topCenter[0], topCenter[1]);
        }
        return (int)Math.round(humanDistance - computerDistance);
    }

    public static boolean canMove(Marble [][] board, int x, int y)
    {
        return x > 0 && y < 26 && y > 0 && x < 18 && board[x][y].owner == 0 && board[x][y].isValid;
    }

    public static boolean canHop(Marble [][] board,int xs,int ys, int x, int y)
    {
        int midX=xs+(x-xs)/2;
        int midY=ys+(y-ys)/2;
        return x > 0 && y < 26 && y > 0 && x < 18 && board[x][y].owner == 0 &&
                board[x][y].isValid && board[midX][midY].owner!=0;
    }

    public static void performStep(State state, ArrayList<State> children)
    {
        ArrayList<Pair> currPlayer=state.player1;
        ArrayList<Pair> otherPlayer=state.player2;
        for (int i=0;i<currPlayer.size();i++)
        {
            Pair pair=currPlayer.get(i);
            int x = pair.x;
            int y = pair.y;
            if (canMove(state.board, x, y+2)) // right
            {
                State newState = state.clone();
                newState.board[x][y] = state.board[x][y+2];
                newState.board[x][y+2] = state.board[x][y];
                ArrayList<Pair> player = Utils.deepCopyPairs(currPlayer);
                player.get(i).y=y+2;
                newState.player2=player;
                newState.player1=Utils.deepCopyPairs(otherPlayer);
                newState.turn = !state.turn;
                children.add(newState);
            }
            if (canMove(state.board, x, y-2)) { // left
                State newState = state.clone();
                newState.board[x][y] = state.board[x][y-2];
                newState.board[x][y-2] = state.board[x][y];
                ArrayList<Pair> player = Utils.deepCopyPairs(currPlayer);
                player.get(i).y=y-2;
                newState.player2=player;
                newState.player1=Utils.deepCopyPairs(otherPlayer);
                newState.turn = !state.turn;
                children.add(newState);
            }
            if (canMove(state.board, x+1, y+1)) { // down right
                State newState = state.clone();
                newState.board[x][y] = state.board[x+1][y+1];
                newState.board[x+1][y+1] = state.board[x][y];
                ArrayList<Pair> player = Utils.deepCopyPairs(currPlayer);
                player.get(i).y=y+1;
                player.get(i).x=x+1;
                newState.player2=player;
                newState.player1=Utils.deepCopyPairs(otherPlayer);
                newState.turn = !state.turn;
                children.add(newState);
            }
            if (canMove(state.board, x+1, y-1)) { // down left
                State newState = state.clone();
                newState.board[x][y] = state.board[x+1][y-1];
                newState.board[x+1][y-1] = state.board[x][y];
                ArrayList<Pair> player = Utils.deepCopyPairs(currPlayer);
                player.get(i).y=y-1;
                player.get(i).x=x+1;
                newState.player2=player;
                newState.player1=Utils.deepCopyPairs(otherPlayer);
                newState.turn = !state.turn;
                children.add(newState);
            }
            if (canMove(state.board, x-1, y-1)) { // up left
                State newState = state.clone();
                newState.board[x][y] = state.board[x-1][y-1];
                newState.board[x-1][y-1] = state.board[x][y];
                ArrayList<Pair> player = Utils.deepCopyPairs(currPlayer);
                player.get(i).y=y-1;
                player.get(i).x=x-1;
                newState.player2=player;
                newState.player1=Utils.deepCopyPairs(otherPlayer);
                newState.turn = !state.turn;
                children.add(newState);
            }
            if (canMove(state.board, x-1, y+1)) { // up right
                State newState = state.clone();
                newState.board[x][y] = state.board[x-1][y+1];
                newState.board[x-1][y+1] = state.board[x][y];
                ArrayList<Pair> player = Utils.deepCopyPairs(currPlayer);
                player.get(i).y=y+1;
                player.get(i).x=x-1;
                newState.player2=player;
                newState.player1=Utils.deepCopyPairs(otherPlayer);
                newState.turn = !state.turn;
                children.add(newState);
            }
//            System.out.println(state.player1.get(i).x+" "+state.player1.get(i).y);
        }
    }

    public static void performHop(State state,State original, ArrayList<State> children){
        ArrayList<Pair> currPlayer=state.player1;
        for (int i=0;i<currPlayer.size();i++) {
            performSingleHop(state, original, i,children);
        }
    }

    public static void performSingleHop(State state,State originalState, int i, ArrayList<State> children) {
        ArrayList<Pair> currPlayer = state.player1;
        ArrayList<Pair> otherPlayer = state.player2;
        Pair pair = currPlayer.get(i);
        int x = pair.x;
        int y = pair.y;
        if (canHop(state.board, x, y, x, y + 4)) { // hop right
            State newState = state.clone();
            newState.board[x][y] = state.board[x][y + 4];
            newState.board[x][y + 4] = state.board[x][y];
            ArrayList<Pair> player = Utils.deepCopyPairs(currPlayer);
            player.get(i).y = y + 4;
            newState.player1 = player;
            State recState = newState.clone();
            newState.player2 = player;
            newState.player1=Utils.deepCopyPairs(otherPlayer);
            newState.turn = !state.turn;
            if (!children.contains(newState) && !newState.equals(originalState)) {
                children.add(newState);
                performSingleHop(recState, originalState, i, children);
            }
        }
        if (canHop(state.board, x, y, x, y - 4)) { // hop left
            State newState = state.clone();
            newState.board[x][y] = state.board[x][y - 4];
            newState.board[x][y - 4] = state.board[x][y];
            ArrayList<Pair> player = Utils.deepCopyPairs(currPlayer);
            player.get(i).y = y - 4;
            newState.player1 = player;
            State recState = newState.clone();
            newState.player2 = player;
            newState.player1=Utils.deepCopyPairs(otherPlayer);
            newState.turn = !state.turn;
            if (!children.contains(newState) && !newState.equals(originalState)) {
                children.add(newState);
                performSingleHop(recState, originalState, i, children);
            }
        }
        if (canHop(state.board, x, y, x + 2, y + 2)) { // hop down right
            State newState = state.clone();
            newState.board[x][y] = state.board[x + 2][y + 2];
            newState.board[x + 2][y + 2] = state.board[x][y];
            ArrayList<Pair> player = Utils.deepCopyPairs(currPlayer);
            player.get(i).x = x + 2;
            player.get(i).y = y + 2;
            newState.player1 = player;
            State recState = newState.clone();
            newState.player2 = player;
            newState.player1=Utils.deepCopyPairs(otherPlayer);
            newState.turn = !state.turn;
            if (!children.contains(newState) && !newState.equals(originalState)) {
                children.add(newState);
                performSingleHop(recState, originalState, i, children);
            }
        }
        if (canHop(state.board, x, y, x + 2, y - 2)) { // hop down left
            State newState = state.clone();
            newState.board[x][y] = state.board[x + 2][y - 2];
            newState.board[x + 2][y - 2] = state.board[x][y];
            ArrayList<Pair> player = Utils.deepCopyPairs(currPlayer);
            player.get(i).x = x + 2;
            player.get(i).y = y - 2;
            newState.player1 = player;
            State recState = newState.clone();
            newState.player2 = player;
            newState.player1=Utils.deepCopyPairs(otherPlayer);
            newState.turn = !state.turn;
            if (!children.contains(newState) && !newState.equals(originalState)) {
                children.add(newState);
                performSingleHop(recState, originalState, i, children);
            }
        }
        if (canHop(state.board, x, y, x - 2, y - 2)) { // hop up left
            State newState = state.clone();
            newState.board[x][y] = state.board[x - 2][y - 2];
            newState.board[x - 2][y - 2] = state.board[x][y];
            ArrayList<Pair> player = Utils.deepCopyPairs(currPlayer);
            player.get(i).x = x - 2;
            player.get(i).y = y - 2;
            newState.player1 = player;
            State recState = newState.clone();
            newState.player2 = player;
            newState.player1=Utils.deepCopyPairs(otherPlayer);
            newState.turn = !state.turn;
            if (!children.contains(newState) && !newState.equals(originalState)) {
                children.add(newState);
                performSingleHop(recState, originalState, i, children);
            }
        }
        if (canHop(state.board, x, y, x - 2, y + 2)) { // hop up right
            State newState = state.clone();
            newState.board[x][y] = state.board[x - 2][y + 2];
            newState.board[x - 2][y + 2] = state.board[x][y];
            ArrayList<Pair> player = Utils.deepCopyPairs(currPlayer);
            player.get(i).x = x - 2;
            player.get(i).y = y + 2;
            newState.player1 = player;
            State recState = newState.clone();
            newState.player2 = player;
            newState.player1=Utils.deepCopyPairs(otherPlayer);
            newState.turn = !state.turn;
            if (!children.contains(newState) && !newState.equals(originalState)) {
                children.add(newState);
                performSingleHop(recState, originalState, i, children);
            }
        }
    }

    //step and hop
    public static void getChildren(State state, ArrayList<State> children){
        performStep(state,children);
        performHop(state,state, children);
    }


    public static boolean valid_move(Marble[][] board, ArrayList<Pair> player, Pair from, Pair to){
        if (!player.contains(from))
            return false;

        // Checking steps.
        int dx[] = new int[] {-1, 0, 1, +1, +0, -1};
        int dy[] = new int[] {+1, 2, 1, -1, -2, -1};
        for(int i = 0; i < 6; i++){
            int nx = from.x + dx[i];
            int ny = from.y + dy[i];
            if (canMove(board, nx, ny) && nx == to.x && ny == to.y)
                return true;
        }

        // Checking hops (using bfs).
        dx = new int[]{0, +0, 2, +2, -2, -2};
        dy = new int[]{4, -4, 2, -2, -2, +2};
        Set<Pair> vis = new HashSet<>(); // Visited array.
        vis.add(from);
        Queue<Pair> q = new LinkedList<>();
        q.add(from);
        while(!q.isEmpty()){
            int sz = q.size();
            while(sz > 0){
                Pair cur = q.poll();
                for(int i = 0; i < 6; i++){
                    int nx = cur.x + dx[i];
                    int ny = cur.y + dy[i];
                    if (canHop(board, cur.x, cur.y, nx, ny) && !vis.contains(new Pair(nx, ny))){
                        if (to.x == nx && to.y == ny)
                            return true;
                        vis.add(new Pair(nx, ny));
                        q.add(new Pair(nx, ny));
                    }
                }
                sz--;
            }
        }
        return false;
    }

    public static void init_board(Marble[][] board) {
        for(int i = 0; i < 18; i++)
            for(int j = 0; j < 26; j++)
                board[i][j] = new Marble();
        // Green
        board[1][13] = board[2][12] = board[2][14] = board[3][11] = board[3][13] = board[3][15]
                = board[4][10] = board[4][12] = board[4][14] = board[4][16] = new Marble(1, 1, true);

        // Red
        board[14][10] = board[14][12] = board[14][14] = board[14][16] = board[15][11] = board[15][13]
                = board[15][15] = board[16][12] = board[16][14] = board[17][13] = new Marble(2, 2, true);

        // Initializing Empty (White) Marbles.

        for(int i = 5, l = 9, r = 17; i <= 9; i++, l--, r++)
            for(int j = l; j <= r; j += 2)
                board[i][j].setValid(true);

        for(int i = 10, l = 6, r = 20; i <= 13; i++, l++, r--)
            for(int j = l; j <= r; j += 2)
                board[i][j].setValid(true);

        // Blue
        board[5][1] = board[5][3] = board[5][5] = board[5][7] = board[6][2] = board[6][4]
                = board[6][6] = board[7][3] = board[7][5] = board[8][4] = new Marble(0, 0, true);

        // Purple
        board[10][4] = board[11][3] = board[11][5] = board[12][2] = board[12][4] = board[12][6]
                = board[13][1] = board[13][3] = board[13][5] = board[13][7] = new Marble(0, 0, true);

        // Orange
        board[10][22] = board[11][21] = board[11][23] = board[12][20] = board[12][22] = board[12][24]
                = board[13][19] = board[13][21] = board[13][23] = board[13][25] = new Marble(0, 0, true);

        // Yellow
        board[5][19] = board[5][21] = board[5][23] = board[5][25] = board[6][20] = board[6][22]
                = board[6][24] = board[7][21] = board[7][23] = board[8][22] = new Marble(0, 0, true);

    }

    public static void init_moves(Marble[][] board, ArrayList<Pair> p1, ArrayList<Pair> p2){
       for(int i = 1; i <= 17; i++)
           for(int j = 1; j <= 25; j++)
               if (board[i][j].owner == 1)
                   p1.add(new Pair(i, j));
               else if (board[i][j].owner == 2)
                   p2.add(new Pair(i, j));
    }

    public static void main(String[] args) {
        startGame();
    }
}
