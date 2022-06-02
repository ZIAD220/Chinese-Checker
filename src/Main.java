import java.util.ArrayList;

public class Main {

    public static void init_board(Marble board[][]) {
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

    public static void init_moves(Marble board[][], ArrayList<Pair> p1, ArrayList<Pair> p2){
       for(int i = 1; i <= 17; i++)
           for(int j = 1; j <= 25; j++)
               if (board[i][j].owner == 1)
                   p1.add(new Pair(i, j));
               else if (board[i][j].owner == 2)
                   p2.add(new Pair(i, j));
    }

    public static void main(String[] args){
        Marble board[][] = new Marble[18][26];
        init_board(board);
        ArrayList<Pair> p1_marbles = new ArrayList<>();
        ArrayList<Pair> p2_marbles = new ArrayList<>();
        init_moves(board, p1_marbles, p2_marbles);
        for(int i = 0; i < p1_marbles.size(); i++)
            System.out.println(p2_marbles.get(i).x + " " + p2_marbles.get(i).y);
    }
}
