package Phase2.Tetris;
import General.PentominoDatabase;

import javax.swing.*;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;

/*TODO
    Implement game class
    Implement board class
    Implement AI class
*/

public class Tetris{
    public static int fieldWidth;
    public static int fieldHeight;
    public static int blocks;
    public static int[][] field;
    public static int[][] tempField;
    private static boolean keys[]=new boolean[65536];
    public static int curPiece;
    public static int curPieceRotation;
    public static int curPos[]=new int[2];
    public static GameWrapper gameWrapper;
    public static boolean upPressed=false;
    public static boolean downPressed=false;
    public static boolean rightPressed=false;
    public static boolean leftPressed=false;
    public static boolean spacePressed=false;
    static boolean newPiece=true;
    public static int score = 0;

    public static void step(){
        checkForNewPiece();
        boolean collided=checkCollision();
        if(!collided){
            if(leftPressed) movePiece(false);
            if(rightPressed) movePiece(true);
            if(upPressed) rotatePiece(false);
            if(downPressed) rotatePiece(true);
            if(spacePressed) dropPiece();
            movePieceDown();
            GameWrapper.ui.setState(tempField);
            GameWrapper.ui.setState(tempField);
        } else {
            System.out.println("collided");
        }
    }

    public static void wipeField(int[][] field){
        for (int i = 0; i < field.length; i++) {
            Arrays.fill(field[i],-1);
        }
    }

    public static void checkForNewPiece(){
        if(newPiece){
            curPiece=getNewPiece();
            int[][] pieceToPlace = General.PentominoDatabase.data[curPiece][curPieceRotation];
            curPos[0]=0;
            curPos[1]=4-pieceToPlace[0].length;
            addPiece();
            newPiece=false;
        }
    }

    public static void addPiece(){
        int[][] pieceToPlace = General.PentominoDatabase.data[curPiece][curPieceRotation];
        for(int i = 0; i < pieceToPlace.length; i++) // loop over x position of pentomino
        {
            for (int j = 0; j < pieceToPlace[i].length; j++) // loop over y position of pentomino
            {
                if (pieceToPlace[i][j] == 1)
                {
                    // Add the ID of the pentomino to the board if the pentomino occupies this square
                    tempField[curPos[0] + i][curPos[1] + j] = curPiece;
                }
            }
        }
    }

    public static void printMatrix(int[][] m) {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                System.out.print(j+" ");
            }
            System.out.println();
        }
    }

    public static int getNewPiece(){ //TODO Max randomize the return between 0 and 11
        int pieceIndex=0;
        int pieceRotation=0; // don't touch!
        return 0;
    }

    public static void rotatePiece(boolean cw){//TODO Lindalee change the pieceRotation variable to the right transformation (check the PentominoDatabase class)
        int pieceRotation=0;
        if(cw) {
            if (curPieceRotation < PentominoDatabase.data[Tetris.curPiece].length - 1) {
                pieceRotation++;
            }
            if (curPieceRotation > PentominoDatabase.data[Tetris.curPiece].length - 1) {
                pieceRotation = 0;
            }
        }
        if(!cw) {
            if (curPieceRotation == 0) {
                pieceRotation = PentominoDatabase.data[Tetris.curPiece].length - 1;
            }
            else {
                pieceRotation = pieceRotation - 1;
            }
        }
        curPieceRotation=pieceRotation;
        int[][] pieceToPlace = PentominoDatabase.data[Tetris.curPiece][Tetris.curPieceRotation];
        Tetris.addPiece();
        Tetris.gameWrapper.ui.setState(Tetris.tempField);
    }

    public static void movePiece(boolean right){//TODO Lindalee change the pieceRotation variable to the right transformation (check the PentominoDatabase class)
        int pieceRotation=0;
        curPieceRotation=pieceRotation;
    }


    public static boolean checkCollision(){//TODO Sam
        int[][] pieceToPlace = General.PentominoDatabase.data[curPiece][curPieceRotation];
        for(int i = 0; i < pieceToPlace.length; i++){ // loop over x position of pentomino
            for (int j = 0; j < pieceToPlace[i].length; j++){ // loop over y position of pentomino
                if (field[curPos[0] + i][curPos[1] + j] != -1){
                    return true;
                }
            }
        }
        return pieceToPlace[0].length+curPos[1]+1>fieldHeight;
    }

    public static void dropPiece(){//TODO Drago Drop piece to the bottom

    }

    public static void movePieceDown(){
        curPos[1]+=1;
        tempField=copyField(field);
        addPiece();
    }

    private void checkRows(){
        //check if there's a full row, if there is use removeRows
    }

    private void removeRows(int row){
        //remove this row, update the score and move all above rows down by the amount of full rows
    }

    private static int[][] copyField(int[][] f0){
        int [][] f1=new int[fieldWidth][fieldHeight];
        for(int i=0;i<fieldWidth;i++){
            for(int j=0;j<fieldHeight;j++){
                f1[i][j]=f0[i][j];
            }
        }
        return f1;
    }

    public static void main(String[] args){
        fieldWidth = 5;
        fieldHeight = 20;
        blocks = 5;
        field = new int[fieldWidth][fieldHeight];
        tempField = new int[fieldWidth][fieldHeight];
        wipeField(field);
        tempField = copyField(field);
        gameWrapper = new GameWrapper(fieldWidth, fieldHeight-5, 50);
        gameWrapper.ui.window.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        leftPressed=true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        rightPressed=true;
                        break;
                    case KeyEvent.VK_UP:
                        upPressed=true;
                        break;
                    case KeyEvent.VK_DOWN:
                        downPressed=true;
                        break;
                    case KeyEvent.VK_SPACE:
                        spacePressed=true;
                        break;
                    case KeyEvent.VK_CONTROL:
                        System.out.println("hello");
                        break;
                }
            }
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        leftPressed=false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        rightPressed=false;
                        break;
                    case KeyEvent.VK_UP:
                        upPressed=false;
                        break;
                    case KeyEvent.VK_DOWN:
                        downPressed=false;
                        break;
                    case KeyEvent.VK_SPACE:
                        spacePressed=false;
                        break;
                }
            }
            public void keyTyped(KeyEvent e) {
            }
        });
        Timer timer = new Timer();
        timer.schedule(new GameTimer(), 0, 500);
    }
}