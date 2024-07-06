import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Minesweeper {
    private class MineTile extends JButton {
         int r ;
         int c ;

         public MineTile(int r ,int c){
             this.r = r ;
             this.c = c ;
         }

    }
    JFrame frame = new JFrame("Minesweeper");
        private int tileSize = 70;
        private int numRows = 8;
        private int numCols = 8;
        private int boardWidth = tileSize * numCols;
        private int boardHeight = tileSize * numRows;
    int mineCount = 10;
    MineTile[][] board = new MineTile[numRows][numCols];
    ArrayList<MineTile> mineList;
    Random random = new Random();
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    int tilesClicked = 0;
    boolean gameOver = false ;


    Minesweeper(){

//        frame.setVisible(true);
        frame.setSize(boardWidth , boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        textLabel.setFont(new Font("Arial" , Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper: " + Integer.toString(mineCount));
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);

        frame.add(textPanel , BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows , numCols)); //8x8
       // boardPanel.setBackground(Color.BLUE);
        frame.add(boardPanel);

        for(int i = 0 ; i < numRows; i++) {
            for(int j = 0 ; j < numCols; j++){
                MineTile tile = new MineTile(i , j);
                board[i][j] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0 , 0 , 0 ,0 ));
                tile.setFont(new Font("Arial Unicode MS" , Font.PLAIN , 30));
                //tile.setText("\uD83D\uDCA3");
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e){
                        if(gameOver){
                            return;
                        }
                        MineTile tile = (MineTile) e.getSource();
                        //click
                        if(e.getButton() == MouseEvent.BUTTON1){
                            if(tile.getText() == ""){
                                if(mineList.contains(tile)){
                                    revealMines();
                                }
                                else {
                                    checkMine(tile.r , tile.c);
                                }
                            }
                        }
                        else if(e.getButton() == MouseEvent.BUTTON3){
                            if(tile.getText() == "" && tile.isEnabled()){
                                tile.setText("F");
                            }
                            else if(tile.getText() == "F"){
                                tile.setText("");
                            }
                        }

                    }
                });
                boardPanel.add(tile);

            }
    }
        frame.setVisible(true);
        setMines();
}
    void setMines(){
        mineList = new ArrayList<MineTile>();

//        mineList.add(board[2][2]);
//        mineList.add(board[2][3]);
//        mineList.add(board[5][6]);
//        mineList.add(board[3][4]);
//        mineList.add(board[1][1]);
        int mineLeft = mineCount;
        while(mineLeft > 0) {
            int r = random.nextInt(numRows);
            int c = random.nextInt(numCols);

            MineTile tile = board[r][c];
            if(!mineList.contains(tile)){
                mineList.add(tile);
                mineLeft -= 1;
            }
        }
    }
    void revealMines() {
        for (int i = 0; i < mineList.size(); i++) {
            MineTile tile = mineList.get(i);
            tile.setText("X");
        }
        gameOver = true;
        textLabel.setText("Game Over");
    }
    void checkMine(int r , int c) {
        if(r < 0 ||r >=numRows || c < 0 || c>=numCols){
            return  ;
        }

        MineTile tile = board[r][c];
        if(!tile.isEnabled()){
            return;
        }
        tile.setEnabled(false);
        tilesClicked += 1;
        int minesFound = 0 ;
        //top 3
        minesFound += countMine(r-1 , c-1); // top left
        minesFound += countMine(r-1 , c); // top
        minesFound += countMine(r-1 , c+1); // top right

        //left and right
        minesFound += countMine(r , c-1);
        minesFound += countMine(r , c+1);

        //bottom 3
        minesFound += countMine(r+1 , c-1);
        minesFound += countMine(r+1 , c);
        minesFound += countMine(r+1 , c+1);

        if(minesFound > 0) {
            tile.setText(Integer.toString(minesFound));
        }
        else{
            tile.setText("");

            //top 3
            checkMine(r-1 , c-1); // top left
            checkMine(r-1 , c); // top
            checkMine(r-1 , c+1); // top right

            //left and right
            checkMine(r , c-1);
            checkMine(r , c+1);

            //bottom 3
            checkMine(r+1 , c-1);
            checkMine(r+1 , c);
            checkMine(r+1 , c+1);
        }
        if(tilesClicked == numRows * numCols - mineList.size()){
            gameOver = true;
            textLabel.setText("Mines Cleared!");
        }
    }
    int countMine (int r , int c ){
        if(r < 0 ||r >=numRows || c < 0 || c>=numCols){
            return 0 ;
        }
        if(mineList.contains(board[r][c])){
            return 1;
        }
        return 0;
    }
}
