import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class GUI {
    public static JFrame mainFrame;
    public static JButton[] marbles;
    static int cellSize = 30;
    static int[][] locations = new int[121][2];
    static int clickCout = 0;
    static int selectedMarbleIndex = -1;
    static int targetMarbleIndex = -1;

    public static void startGUI() {
        mainFrame = new JFrame("Chinese Checkers");
        mainFrame.setSize(700, 600);
        mainFrame.setLayout(new GridLayout());
        JPanel panel = new JPanel(new GridLayout(5, 1, 0, 60));
        panel.setSize(700, 600);
        JButton easyButton = new JButton("Easy");
        easyButton.setSize(200, 100);
        JButton mediumButton = new JButton("Medium");
        mediumButton.setSize(200, 100);
        JButton hardButton = new JButton("Hard");
        hardButton.setSize(200, 100);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        panel.setBorder(BorderFactory.createEmptyBorder(100, 50, 0, 50));
        panel.add(easyButton);
        panel.add(mediumButton);
        panel.add(hardButton);
        mainFrame.add(panel);
        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.difficultyLevel = 1;
                startGame();
            }
        });
        mediumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.difficultyLevel = 3;
                startGame();
            }
        });
        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.difficultyLevel = 5;
                startGame();
            }
        });
    }

    public static void startGame() {
        mainFrame.getContentPane().removeAll();
        mainFrame.repaint();
        mainFrame.setLayout(null);
        marbles = new JButton[121];
        initLocations();
        drawBoard();
    }

    public static void drawBoard() {
        System.out.println("YAAAAWW");

        for (int i = 0; i < marbles.length; i++) {
            int[] logicalBoardLocation = mapButtonToLogicalBoar[i];
            int x = logicalBoardLocation[0], y = logicalBoardLocation[1];
            marbles[i] = new RoundButton("");
            marbles[i].setBounds(locations[i][0], locations[i][1], cellSize, cellSize);
            marbles[i].setBackground(Main.state.board[x][y].owner == 0 ? Color.WHITE : Main.state.board[x][y].owner == 1 ? Color.GREEN : Color.RED);
            marbles[i].setActionCommand("" + i);
            marbles[i].addActionListener(actionListener);
            mainFrame.add(marbles[i]);
        }
    }

    public static void updateBoard() {
        System.out.println(Main.state);
        for (int i=0; i<marbles.length; i++) {
            int[] logicalBoardLocation = mapButtonToLogicalBoar[i];
            int x = logicalBoardLocation[0], y = logicalBoardLocation[1];
            marbles[i].setBackground(Main.state.board[x][y].owner == 0 ? Color.WHITE : Main.state.board[x][y].owner == 1 ? Color.GREEN : Color.RED);
        }
    }

    static ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            clickCout++;
            if (clickCout == 1) {
                selectedMarbleIndex = Integer.parseInt(e.getActionCommand());
            }
            else {
                targetMarbleIndex = Integer.parseInt(e.getActionCommand());
                int xs = mapButtonToLogicalBoar[selectedMarbleIndex][0];
                int ys = mapButtonToLogicalBoar[selectedMarbleIndex][1];
                int xd = mapButtonToLogicalBoar[targetMarbleIndex][0];
                int yd = mapButtonToLogicalBoar[targetMarbleIndex][1];
                ArrayList<Pair> allMoves = Main.valid_move(Main.state.board, new Pair(xs, ys));
                if (!allMoves.contains(new Pair(xd, yd))) {
                    System.out.println("Not valid move");
                    selectedMarbleIndex = -1;
                    targetMarbleIndex = -1;
                    clickCout = 0;
                    return;
                }
                Marble temp = Main.state.board[xs][ys];
                Main.state.board[xs][ys] = Main.state.board[xd][yd];
                Main.state.board[xd][yd] = temp;
                Main.state.player1.remove(new Pair(xs,ys));
                Main.state.player1.add(new Pair(xd,yd));
                Main.state.turn = !Main.state.turn;
                ArrayList<Pair> tmp = Main.state.player1;
                Main.state.player1 = Main.state.player2;
                Main.state.player2 = tmp;
                System.out.println(xs + ", " + ys);
                System.out.println(xd + ", " + yd);

                selectedMarbleIndex = -1;
                targetMarbleIndex = -1;
                clickCout = 0;
                updateBoard();
                Main.play();
            }
        }
    };

    public static Color marbleColorDictionary(int i) {
        return i>=0 && i<10 ? Color.GREEN : i>(121-11) && i<121 ? Color.RED : Color.WHITE;
    }

    public static void initLocations() {
        for (int j=0; j< locations.length; j++) {
            locations[j][0] = 0;
            locations[j][1] = 0;
        }
        int middle = 700/2-cellSize;
        locations[0] = new int[]{middle, 20};
        locations[1] = new int[]{middle-cellSize*2/3, 20+cellSize};
        locations[2] = new int[]{middle+cellSize*2/3, 20+cellSize};
        locations[3] = new int[]{middle-cellSize*2*2/3, 20+2*cellSize};
        locations[4] = new int[]{middle, 20+2*cellSize};
        locations[5] = new int[]{middle+cellSize*2*2/3, 20+2*cellSize};
        locations[6] = new int[]{middle-cellSize*3*2/3, 20+3*cellSize};
        locations[7] = new int[]{middle-cellSize*2/3, 20+3*cellSize};
        locations[8] = new int[]{middle+cellSize*2/3, 20+3*cellSize};
        locations[9] = new int[]{middle+cellSize*3*2/3, 20+3*cellSize};
        ////
        locations[10] = new int[]{middle-cellSize*12*2/3, 20+4*cellSize};
        locations[11] = new int[]{middle-cellSize*10*2/3, 20+4*cellSize};
        locations[12] = new int[]{middle-cellSize*8*2/3, 20+4*cellSize};
        locations[13] = new int[]{middle-cellSize*6*2/3, 20+4*cellSize};
        locations[14] = new int[]{middle-cellSize*4*2/3, 20+4*cellSize};
        locations[15] = new int[]{middle-cellSize*2*2/3, 20+4*cellSize};
        locations[16] = new int[]{middle, 20+4*cellSize};
        locations[17] = new int[]{middle+cellSize*2*2/3, 20+4*cellSize};
        locations[18] = new int[]{middle+cellSize*4*2/3, 20+4*cellSize};
        locations[19] = new int[]{middle+cellSize*6*2/3, 20+4*cellSize};
        locations[20] = new int[]{middle+cellSize*8*2/3, 20+4*cellSize};
        locations[21] = new int[]{middle+cellSize*10*2/3, 20+4*cellSize};
        locations[22] = new int[]{middle+cellSize*12*2/3, 20+4*cellSize};
        locations[23] = new int[]{middle-cellSize*11*2/3, 20+5*cellSize};
        locations[24] = new int[]{middle-cellSize*9*2/3, 20+5*cellSize};
        locations[25] = new int[]{middle-cellSize*7*2/3, 20+5*cellSize};
        locations[26] = new int[]{middle-cellSize*5*2/3, 20+5*cellSize};
        locations[27] = new int[]{middle-cellSize*3*2/3, 20+5*cellSize};
        locations[28] = new int[]{middle-cellSize*2/3, 20+5*cellSize};
        locations[29] = new int[]{middle+cellSize*2/3, 20+5*cellSize};
        locations[30] = new int[]{middle+cellSize*3*2/3, 20+5*cellSize};
        locations[31] = new int[]{middle+cellSize*5*2/3, 20+5*cellSize};
        locations[32] = new int[]{middle+cellSize*7*2/3, 20+5*cellSize};
        locations[33] = new int[]{middle+cellSize*9*2/3, 20+5*cellSize};
        locations[34] = new int[]{middle+cellSize*11*2/3, 20+5*cellSize};
        locations[35] = new int[]{middle-cellSize*10*2/3, 20+6*cellSize};
        locations[36] = new int[]{middle-cellSize*8*2/3, 20+6*cellSize};
        locations[37] = new int[]{middle-cellSize*6*2/3, 20+6*cellSize};
        locations[38] = new int[]{middle-cellSize*4*2/3, 20+6*cellSize};
        locations[39] = new int[]{middle-cellSize*2*2/3, 20+6*cellSize};
        locations[40] = new int[]{middle, 20+6*cellSize};
        locations[41] = new int[]{middle+cellSize*2*2/3, 20+6*cellSize};
        locations[42] = new int[]{middle+cellSize*4*2/3, 20+6*cellSize};
        locations[43] = new int[]{middle+cellSize*6*2/3, 20+6*cellSize};
        locations[44] = new int[]{middle+cellSize*8*2/3, 20+6*cellSize};
        locations[45] = new int[]{middle+cellSize*10*2/3, 20+6*cellSize};
        locations[46] = new int[]{middle-cellSize*9*2/3, 20+7*cellSize};
        locations[47] = new int[]{middle-cellSize*7*2/3, 20+7*cellSize};
        locations[48] = new int[]{middle-cellSize*5*2/3, 20+7*cellSize};
        locations[49] = new int[]{middle-cellSize*3*2/3, 20+7*cellSize};
        locations[50] = new int[]{middle-cellSize*2/3, 20+7*cellSize};
        locations[51] = new int[]{middle+cellSize*2/3, 20+7*cellSize};
        locations[52] = new int[]{middle+cellSize*3*2/3, 20+7*cellSize};
        locations[53] = new int[]{middle+cellSize*5*2/3, 20+7*cellSize};
        locations[54] = new int[]{middle+cellSize*7*2/3, 20+7*cellSize};
        locations[55] = new int[]{middle+cellSize*9*2/3, 20+7*cellSize};
        locations[56] = new int[]{middle-cellSize*8*2/3, 20+8*cellSize};
        locations[57] = new int[]{middle-cellSize*6*2/3, 20+8*cellSize};
        locations[58] = new int[]{middle-cellSize*4*2/3, 20+8*cellSize};
        locations[59] = new int[]{middle-cellSize*2*2/3, 20+8*cellSize};
        locations[60] = new int[]{middle, 20+8*cellSize};
        locations[61] = new int[]{middle+cellSize*2*2/3, 20+8*cellSize};
        locations[62] = new int[]{middle+cellSize*4*2/3, 20+8*cellSize};
        locations[63] = new int[]{middle+cellSize*6*2/3, 20+8*cellSize};
        locations[64] = new int[]{middle+cellSize*8*2/3, 20+8*cellSize};
        locations[65] = new int[]{middle-cellSize*9*2/3, 20+9*cellSize};
        locations[66] = new int[]{middle-cellSize*7*2/3, 20+9*cellSize};
        locations[67] = new int[]{middle-cellSize*5*2/3, 20+9*cellSize};
        locations[68] = new int[]{middle-cellSize*3*2/3, 20+9*cellSize};
        locations[69] = new int[]{middle-cellSize*2/3, 20+9*cellSize};
        locations[70] = new int[]{middle+cellSize*2/3, 20+9*cellSize};
        locations[71] = new int[]{middle+cellSize*3*2/3, 20+9*cellSize};
        locations[72] = new int[]{middle+cellSize*5*2/3, 20+9*cellSize};
        locations[73] = new int[]{middle+cellSize*7*2/3, 20+9*cellSize};
        locations[74] = new int[]{middle+cellSize*9*2/3, 20+9*cellSize};
        locations[75] = new int[]{middle-cellSize*10*2/3, 20+10*cellSize};
        locations[76] = new int[]{middle-cellSize*8*2/3, 20+10*cellSize};
        locations[77] = new int[]{middle-cellSize*6*2/3, 20+10*cellSize};
        locations[78] = new int[]{middle-cellSize*4*2/3, 20+10*cellSize};
        locations[79] = new int[]{middle-cellSize*2*2/3, 20+10*cellSize};
        locations[80] = new int[]{middle, 20+10*cellSize};
        locations[81] = new int[]{middle+cellSize*2*2/3, 20+10*cellSize};
        locations[82] = new int[]{middle+cellSize*4*2/3, 20+10*cellSize};
        locations[83] = new int[]{middle+cellSize*6*2/3, 20+10*cellSize};
        locations[84] = new int[]{middle+cellSize*8*2/3, 20+10*cellSize};
        locations[85] = new int[]{middle+cellSize*10*2/3, 20+10*cellSize};
        locations[86] = new int[]{middle-cellSize*11*2/3, 20+11*cellSize};
        locations[87] = new int[]{middle-cellSize*9*2/3, 20+11*cellSize};
        locations[88] = new int[]{middle-cellSize*7*2/3, 20+11*cellSize};
        locations[89] = new int[]{middle-cellSize*5*2/3, 20+11*cellSize};
        locations[90] = new int[]{middle-cellSize*3*2/3, 20+11*cellSize};
        locations[91] = new int[]{middle-cellSize*2/3, 20+11*cellSize};
        locations[92] = new int[]{middle+cellSize*2/3, 20+11*cellSize};
        locations[93] = new int[]{middle+cellSize*3*2/3, 20+11*cellSize};
        locations[94] = new int[]{middle+cellSize*5*2/3, 20+11*cellSize};
        locations[95] = new int[]{middle+cellSize*7*2/3, 20+11*cellSize};
        locations[96] = new int[]{middle+cellSize*9*2/3, 20+11*cellSize};
        locations[97] = new int[]{middle+cellSize*11*2/3, 20+11*cellSize};
        locations[98] = new int[]{middle-cellSize*12*2/3, 20+12*cellSize};
        locations[99] = new int[]{middle-cellSize*10*2/3, 20+12*cellSize};
        locations[100] = new int[]{middle-cellSize*8*2/3, 20+12*cellSize};
        locations[101] = new int[]{middle-cellSize*6*2/3, 20+12*cellSize};
        locations[102] = new int[]{middle-cellSize*4*2/3, 20+12*cellSize};
        locations[103] = new int[]{middle-cellSize*2*2/3, 20+12*cellSize};
        locations[104] = new int[]{middle, 20+12*cellSize};
        locations[105] = new int[]{middle+cellSize*2*2/3, 20+12*cellSize};
        locations[106] = new int[]{middle+cellSize*4*2/3, 20+12*cellSize};
        locations[107] = new int[]{middle+cellSize*6*2/3, 20+12*cellSize};
        locations[108] = new int[]{middle+cellSize*8*2/3, 20+12*cellSize};
        locations[109] = new int[]{middle+cellSize*10*2/3, 20+12*cellSize};
        locations[110] = new int[]{middle+cellSize*12*2/3, 20+12*cellSize};
        //////////////////

        locations[111] = new int[]{middle-cellSize*3*2/3, 20+13*cellSize};
        locations[112] = new int[]{middle-cellSize*2/3, 20+13*cellSize};
        locations[113] = new int[]{middle+cellSize*2/3, 20+13*cellSize};
        locations[114] = new int[]{middle+cellSize*3*2/3, 20+13*cellSize};
        locations[115] = new int[]{middle-cellSize*2*2/3, 20+14*cellSize};
        locations[116] = new int[]{middle, 20+14*cellSize};
        locations[117] = new int[]{middle+cellSize*2*2/3, 20+14*cellSize};
        locations[118] = new int[]{middle-cellSize*2/3, 20+15*cellSize};
        locations[119] = new int[]{middle+cellSize*2/3, 20+15*cellSize};
        locations[120] = new int[]{middle, 20+16*cellSize};
//        for (int j=10; j<23; j++) {
////            *((2/3)*(j>23-10 ? -1 : 1)*(j-10+2)/2)
//            locations[j] = new int[]{middle-cellSize*((j-10+2)/2), 50+4*cellSize};
//        }
    }

    //////////////////////////////////////////////

    private static class RoundButton extends JButton {

        public RoundButton(String label) {
            super(label);

            setBackground(Color.lightGray);
            setFocusable(false);

    /*
     These statements enlarge the button so that it
     becomes a circle rather than an oval.
    */
            Dimension size = getPreferredSize();
            size.width = size.height = Math.max(size.width, size.height);
            setPreferredSize(size);

    /*
     This call causes the JButton not to paint the background.
     This allows us to paint a round background.
    */
            setContentAreaFilled(false);
        }

        protected void paintComponent(Graphics g) {
            if (getModel().isArmed()) {
                g.setColor(Color.gray);
            } else {
                g.setColor(getBackground());
            }
            g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);

            super.paintComponent(g);
        }

        protected void paintBorder(Graphics g) {
            g.setColor(Color.darkGray);
            g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
        }

        // Hit detection.
        Shape shape;

        public boolean contains(int x, int y) {
            // If the button has changed size,  make a new shape object.
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
            }
            return shape.contains(x, y);
        }
    }

    static int[][] mapButtonToLogicalBoar = {
            {1, 13}, {2, 12}, {2, 14}, {3, 11}, {3, 13}, {3, 15}, {4, 10}, {4, 12}, {4, 14}, {4, 16},
            {5, 1}, {5, 3}, {5, 5}, {5, 7}, {5, 9}, {5, 11}, {5, 13}, {5, 15}, {5, 17}, {5, 19}, {5, 21}, {5, 23}, {5, 25},
            {6, 2}, {6, 4}, {6, 6}, {6, 8}, {6, 10}, {6, 12}, {6, 14}, {6, 16}, {6, 18}, {6, 20}, {6, 22}, {6, 24},
            {7, 3}, {7, 5},{7, 7},{7, 9},{7, 11},{7, 13},{7, 15},{7, 17},{7, 19}, {7, 21}, {7, 23},
            {8, 4}, {8, 6}, {8, 8}, {8, 10}, {8, 12}, {8, 14}, {8, 16}, {8, 18}, {8, 20}, {8, 22},
            {9, 5},{9, 7},{9, 9},{9, 11},{9, 13},{9, 15},{9, 17},{9, 19}, {9, 21},
            {10, 4}, {10, 6}, {10, 8}, {10, 10}, {10, 12}, {10, 14}, {10, 16}, {10, 18}, {10, 20}, {10, 22},
            {11, 3}, {11, 5},{11, 7},{11, 9},{11, 11},{11, 13},{11, 15},{11, 17},{11, 19}, {11, 21}, {11, 23},
            {12, 2}, {12, 4}, {12, 6}, {12, 8}, {12, 10}, {12, 12}, {12, 14}, {12, 16}, {12, 18}, {12, 20}, {12, 22}, {12, 24},
            {13, 1}, {13, 3}, {13, 5}, {13, 7}, {13, 9}, {13, 11}, {13, 13}, {13, 15}, {13, 17}, {13, 19}, {13, 21}, {13, 23}, {13, 25},
            {14, 10}, {14, 12}, {14, 14}, {14, 16}, {15, 11}, {15, 13}, {15, 15}, {16, 12}, {16, 14}, {17, 13}
    };
}
