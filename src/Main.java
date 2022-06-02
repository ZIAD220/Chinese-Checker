import java.util.ArrayList;

public class Main {
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
        getChildren(initialState,states);
        System.out.print(states);
//        for(int i = 0; i < p1_marbles.size(); i++)
//            System.out.println(p2_marbles.get(i).x + " " + p2_marbles.get(i).y);
    }

    public static boolean canMove(Marble [][] board, int x, int y)
    {
        return x > 0 && y < 26 && y > 0 && x < 18 && board[x][y].owner == 0 && board[x][y].isValid;
    }

    public static boolean canHop(Marble [][] board,int xs,int ys, int x, int y)
    {
        int midX=xs+((x-xs)/2);
        int midY=ys+((y-ys)/2);
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
                ArrayList<Pair> player = (ArrayList<Pair>) currPlayer.clone();
                player.get(i).y=y+2;
                newState.player2=player;
                newState.player1=otherPlayer;
                newState.turn = !state.turn;
                children.add(newState);
            }
            if (canMove(state.board, x, y-2)) { // left
                State newState = state.clone();
                newState.board[x][y] = state.board[x][y-2];
                newState.board[x][y-2] = state.board[x][y];
                ArrayList<Pair> player = (ArrayList<Pair>) currPlayer.clone();
                player.get(i).y=y-2;
                newState.player2=player;
                newState.player1=otherPlayer;
                newState.turn = !state.turn;
                children.add(newState);
            }
            if (canMove(state.board, x+1, y+1)) { // down right
                State newState = state.clone();
                newState.board[x][y] = state.board[x+1][y+1];
                newState.board[x+1][y+1] = state.board[x][y];
                ArrayList<Pair> player = (ArrayList<Pair>) currPlayer.clone();
                player.get(i).y=y+1;
                player.get(i).x=x+1;
                newState.player2=player;
                newState.player1=otherPlayer;
                newState.turn = !state.turn;
                children.add(newState);
            }
            if (canMove(state.board, x+1, y-1)) { // down left
                State newState = state.clone();
                newState.board[x][y] = state.board[x+1][y-1];
                newState.board[x+1][y-1] = state.board[x][y];
                ArrayList<Pair> player = (ArrayList<Pair>) currPlayer.clone();
                player.get(i).y=y-1;
                player.get(i).x=x+1;
                newState.player2=player;
                newState.player1=otherPlayer;
                newState.turn = !state.turn;
                children.add(newState);
            }
            if (canMove(state.board, x-1, y-1)) { // up left
                State newState = state.clone();
                newState.board[x][y] = state.board[x-1][y-1];
                newState.board[x-1][y-1] = state.board[x][y];
                ArrayList<Pair> player = (ArrayList<Pair>) currPlayer.clone();
                player.get(i).y=y-1;
                player.get(i).x=x-1;
                newState.player2=player;
                newState.player1=otherPlayer;
                newState.turn = !state.turn;
                children.add(newState);
            }
            if (canMove(state.board, x-1, y+1)) { // up right
                State newState = state.clone();
                newState.board[x][y] = state.board[x-1][y+1];
                newState.board[x-1][y+1] = state.board[x][y];
                ArrayList<Pair> player = (ArrayList<Pair>) currPlayer.clone();
                player.get(i).y=y+1;
                player.get(i).x=x-1;
                newState.player2=player;
                newState.player1=otherPlayer;
                newState.turn = !state.turn;
                children.add(newState);
            }
        }
    }

    public static void performHop(State state, ArrayList<State> children){
        ArrayList<Pair> currPlayer=state.player1;
        for (int i=0;i<currPlayer.size();i++) {
            performSingleHop(state,i,children);
        }
    }

    public static void performSingleHop(State state, int i, ArrayList<State> children){
        ArrayList<Pair> currPlayer=state.player1;
        ArrayList<Pair> otherPlayer=state.player2;
        Pair pair = currPlayer.get(i);
        int x = pair.x;
        int y = pair.y;
        if(canHop(state.board,x,y,x,y+4)) { // hop right
            State newState = state.clone();
            newState.board[x][y] = state.board[x][y + 4];
            newState.board[x][y + 4] = state.board[x][y];
            ArrayList<Pair> player = (ArrayList<Pair>) currPlayer.clone();
            player.get(i).y = y + 4;
            newState.player1 = player;
            performSingleHop(newState, i, children);
            newState.player2 = player;
            newState.player1 = otherPlayer;
            newState.turn = !state.turn;
            if (!children.contains(newState)) {
                children.add(newState);
            }
        }
        if(canHop(state.board,x,y,x,y-4)){ // hop left
            State newState = state.clone();
            newState.board[x][y] = state.board[x][y-4];
            newState.board[x][y-4] = state.board[x][y];
            ArrayList<Pair> player = (ArrayList<Pair>) currPlayer.clone();
            player.get(i).y=y-4;
            newState.player1 = player;
            performSingleHop(newState, i,children);
            newState.player2=player;
            newState.player1=otherPlayer;
            newState.turn = !state.turn;
            if (!children.contains(newState)) {
                children.add(newState);
            }
        }
        if(canHop(state.board,x,y,x+2,y+2)){ // hop down right
            State newState = state.clone();
            newState.board[x][y] = state.board[x+2][y+2];
            newState.board[x+2][y+2] = state.board[x][y];
            ArrayList<Pair> player = (ArrayList<Pair>) currPlayer.clone();
            player.get(i).x=x+2;
            player.get(i).y=y+2;
            newState.player1 = player;
            performSingleHop(newState, i,children);
            newState.player2=player;
            newState.player1=otherPlayer;
            newState.turn = !state.turn;
            if (!children.contains(newState)) {
                children.add(newState);
            }
        }
        if(canHop(state.board,x,y,x+2,y-2)){ // hop down left
            State newState = state.clone();
            newState.board[x][y] = state.board[x+2][y-2];
            newState.board[x+2][y-2] = state.board[x][y];
            ArrayList<Pair> player = (ArrayList<Pair>) currPlayer.clone();
            player.get(i).x=x+2;
            player.get(i).y=y-2;
            newState.player1 = player;
            performSingleHop(newState, i,children);
            newState.player2=player;
            newState.player1=otherPlayer;
            newState.turn = !state.turn;
            if (!children.contains(newState)) {
                children.add(newState);
            }
        }
        if(canHop(state.board,x,y,x-2,y-2)){ // hop up left
            State newState = state.clone();
            newState.board[x][y] = state.board[x-2][y-2];
            newState.board[x-2][y-2] = state.board[x][y];
            ArrayList<Pair> player = (ArrayList<Pair>) currPlayer.clone();
            player.get(i).x=x-2;
            player.get(i).y=y-2;
            newState.player1 = player;
            performSingleHop(newState, i,children);
            newState.player2=player;
            newState.player1=otherPlayer;
            newState.turn = !state.turn;
            if (!children.contains(newState)) {
                children.add(newState);
            }
        }
        if(canHop(state.board,x,y,x-2,y+2)){ // hop up right
            State newState = state.clone();
            newState.board[x][y] = state.board[x-2][y+2];
            newState.board[x-2][y+2] = state.board[x][y];
            ArrayList<Pair> player = (ArrayList<Pair>) currPlayer.clone();
            player.get(i).x=x-2;
            player.get(i).y=y+2;
            newState.player1 = player;
            performSingleHop(newState, i,children);
            newState.player2=player;
            newState.player1=otherPlayer;
            newState.turn = !state.turn;
            if (!children.contains(newState)) {
                children.add(newState);
            }
        }
    }

    //step and hop
    public static void getChildren(State state, ArrayList<State> children){
        performStep(state,children);
        performHop(state, children);
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
        board[9][4] = board[10][3] = board[10][5] = board[11][2] = board[11][4] = board[11][6]
                = board[12][1] = board[12][3] = board[12][5] = board[12][7] = new Marble(0, 0, true);

        // Orange
        board[9][22] = board[10][21] = board[10][23] = board[11][20] = board[11][22] = board[11][24]
                = board[12][19] = board[12][21] = board[12][23] = board[12][25] = new Marble(0, 0, true);

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
