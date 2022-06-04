import jdk.jshell.execution.Util;

import java.util.*;

public class Main {
    public static final int[] bottomCenter = {15, 13};
    public static final int[] topCenter = {3, 13};
    public static int difficultyLevel = 3;
    public static State state = null;
    public static Scanner in = new Scanner(System.in);
    public static void startGame()
    {
        Marble[][] board = new Marble[18][26];
        init_board(board);
        ArrayList<Pair> p1_marbles = new ArrayList<>();
        ArrayList<Pair> p2_marbles = new ArrayList<>();
        init_moves(board, p1_marbles, p2_marbles);
        state=new State(board,false); //computer turn
        state.player1=p2_marbles;
        state.player2=p1_marbles;
//        ArrayList<State> states=new ArrayList<>();
////        for(int i = 0; i < p1_marbles.size(); i++)
////            System.out.println(p1_marbles.get(i).x + " " + p1_marbles.get(i).y);
//        State best = new State(initialState.board, initialState.turn);
//        initialState.heuristic=alphaBeta(initialState,true, difficultyLevel, Integer.MIN_VALUE, Integer.MAX_VALUE, best);
//        System.out.print(best);
    }

    public static void humanPlay() {
        System.out.print("Enter source cell location: ");
        int xs, ys, xd, yd;
        xs = in.nextInt();
        ys = in.nextInt();
        ArrayList<Pair> allMoves = valid_move(state.board, new Pair(xs, ys));
        for(Pair p : allMoves)
            System.out.println(p);
        System.out.print("Enter destination cell location: ");
        xd = in.nextInt();
        yd = in.nextInt();
        if (!allMoves.contains(new Pair(xd, yd))){
            System.out.println("Not valid move");
            humanPlay();
            return;
        }
        Marble temp = state.board[xs][ys];
        state.board[xs][ys] = state.board[xd][yd];
        state.board[xd][yd] = temp;
        state.player1.remove(new Pair(xs,ys));
        state.player1.add(new Pair(xd,yd));
        state.turn = !state.turn;
        ArrayList<Pair> tmp = state.player1;
        state.player1 = state.player2;
        state.player2 = tmp;
    }

    public static void play() {
        //for(int i = 0; i < state.player1.size(); i++)
          //  System.out.println(state.player1.get(i));
        if (state.turn) {
            State best = new State(state.board, state.turn);
            state.heuristic=alphaBeta(state,true, difficultyLevel, Integer.MIN_VALUE, Integer.MAX_VALUE, best);
            state = best;
        }
        else {
            humanPlay();
        }
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

    public static int alphaBeta(State state, boolean atRoot, int depth, int alpha, int beta, State best) {
        if (depth == 0 || getWinner(state) != 0) {
            state.heuristic=evalState(state);
            return evalState(state);
        }
        ArrayList<State> children = new ArrayList<>();
        getChildren(state, children);

        if(state.turn) {
            int maxEval = Integer.MIN_VALUE;
            for(State child : children) {
                int eval = alphaBeta(child, false, depth - 1, alpha, beta, best);
                if (eval > maxEval){
                    maxEval = eval;
                    if (atRoot){
                        best.board = child.board;
                        best.player1 = child.player1;
                        best.player2 = child.player2;
                        best.turn = child.turn;
                        best.heuristic = eval;
                    }

                }
                alpha = Integer.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            state.heuristic=maxEval;
            return maxEval;
        } else {
        int minEval = Integer.MAX_VALUE;
        for(State child : children){
            int eval = alphaBeta(child, false, depth - 1, alpha, beta, best);
            minEval = Integer.min(minEval, eval);
            beta = Integer.min(beta, eval);
            if(beta <= alpha) {
                break;
            }
        }
        state.heuristic=minEval;
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
        //System.out.println("AI Thinking...");
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
            int dx[] = new int[] {-1, 0, 1, +1, +0, -1};
            int dy[] = new int[] {+1, 2, 1, -1, -2, -1};
            for(int j = 0; j < 6; j++){
                int nx = x + dx[j];
                int ny = y + dy[j];
                if (canMove(state.board, nx, ny)){
                    State newState = state.clone();
                    newState.board[x][y] = state.board[nx][ny];
                    newState.board[nx][ny] = state.board[x][y];
                    ArrayList<Pair> player = Utils.deepCopyPairs(currPlayer);
                    player.get(i).y=ny;
                    player.get(i).x=nx;
                    newState.player2=player;
                    newState.player1=Utils.deepCopyPairs(otherPlayer);
                    newState.turn = !state.turn;
                    children.add(newState);
                }
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
        int dx[] = new int[]{0, +0, 2, +2, -2, -2};
        int dy[] = new int[]{4, -4, 2, -2, -2, +2};
        for(int j = 0; j < 6; j++){
            int nx = x + dx[j];
            int ny = y + dy[j];
            if (canHop(state.board, x, y, nx, ny)){
                State newState = state.clone();
                newState.board[x][y] = state.board[nx][ny];
                newState.board[nx][ny] = state.board[x][y];
                ArrayList<Pair> player = Utils.deepCopyPairs(currPlayer);
                player.get(i).x = nx;
                player.get(i).y = ny;
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
    }

    //step and hop
    public static void getChildren(State state, ArrayList<State> children){
        performStep(state,children);
        performHop(state,state, children);
    }


    public static ArrayList<Pair> valid_move(Marble[][] board, Pair from){

        int x = from.x, y = from.y;
        if (x < 0 || x > 17 || y < 0 || y > 25 || board[x][y].owner != 2)
            return new ArrayList<>();
        ArrayList<Pair> allMoves = new ArrayList<>();

        // Checking steps.
        int dx[] = new int[] {-1, 0, 1, +1, +0, -1};
        int dy[] = new int[] {+1, 2, 1, -1, -2, -1};
        for(int i = 0; i < 6; i++){
            int nx = from.x + dx[i];
            int ny = from.y + dy[i];
            if (canMove(board, nx, ny))
                allMoves.add(new Pair(nx, ny));
        }

        // Checking hops (using bfs).
        dx = new int[]{0, +0, 2, +2, -2, -2};
        dy = new int[]{4, -4, 2, -2, -2, +2};
        boolean vis[][] = new boolean[18][26];
        vis[from.x][from.y] = true;
        Queue<Pair> q = new LinkedList<>();
        q.add(from);
        while(!q.isEmpty()){
            int sz = q.size();
            while(sz > 0){
                Pair cur = q.poll();
                for(int i = 0; i < 6; i++){
                    int nx = cur.x + dx[i];
                    int ny = cur.y + dy[i];
                    if (canHop(board, cur.x, cur.y, nx, ny) && !vis[nx][ny]){
                        vis[nx][ny] = true;
                        q.add(new Pair(nx, ny));
                        allMoves.add(new Pair(nx, ny));
                    }
                }
                sz--;
            }
        }
        return allMoves;
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

    public static int getDifficulty(){
        System.out.println("Choose difficulty level: ");
        System.out.println("1- Easy\n2- Medium\n3- Hard");
        int difficulty = in.nextInt();
        if(difficulty==1)
            difficultyLevel=1;
        else if(difficulty==2)
            difficultyLevel=3;
        else if(difficulty==3)
            difficultyLevel=5;
        else {
            System.out.println("Enter proper value (1 - 2 - 3).");
            return getDifficulty();
        }
        return difficultyLevel;
    }

    public static void main(String[] args) {
        getDifficulty();
        System.out.println("Red is your color (r)");
        startGame();
        while (getWinner(state) == 0) {
            System.out.println(state);
            System.out.println();
            play();
        }
        System.out.println(state);
        System.out.println();
        if(getWinner(state)==1)
            System.out.println("Computer wins!!");
        else if(getWinner(state)==2)
            System.out.println("You win!!");
    }
}
