package Tetris;
import General.PentominoDatabase;

import java.io.IOException;

public class Qbot {

    //Add AI class, with fieldScores variable and findBestPlaceToPlace and updateFieldScores method
    private static int[][] fieldScores;
    //TODO to vague
    private static int bestX = -2;
    private static int bestY = -2;
    private static int bestRotation = -1;
    private static int curPiece = 0;
    //TODO after it works: save the results so far so they can be reused if a position doesn't have a route
    //TODO read advice: 1) Keep track of what positions have been tried and their results (if they were possible at all). This means that you need to save the best location and rotation for the 2 pentominoes that you are trying out.
    // 2) When you finished finding the optimal position to place the first pentomino, check the second pentomino using the new scores that you can get from genRewards. As the field input, use the actually field with the first pentomino
    // added in (as a tmp field).
     /*Qbot tip 3: don't try placing a pentomino in the sky or on a taken cell, find the lowest free cell of each column and try that one. 
    Keep going up until you tried every cell in that column that could possibly be used. Then continue to the next column.
    
    Tip 4: For each of the cells that you try, fit in the pentomino in all it's possible rotations (not mirrors). 
    If none work, save a false in a boolean array that stores the locations that are left to search trough. This is not strictly needed, but can really help you debugging.
    */

    /**
     * Finds best place to place curPiece and nextPiece
     * @return: best place to place current and next piece [xc,yc,rc,xn,yn,rn]
     */
//    public static int[] findBestPlaceToPlaceV2(){
//        int xCurrent = -1;
//        int yCurrent = -1;
//        int rotationCurrent = -1;
//        int xNext = -1;
//        int yNext = -1;
//        int rotationNext = -1;
//        //find the the placement that yields the highest score for the current and the nextPiece
//        int[] temp = mainLoopV2(Tetris.curPiece, Tetris.curPieceRotation);
//        xCurrent = temp[0];
//        yCurrent = temp[1];
//        rotationCurrent = temp[2];
//        genRewards(tempField, Tetris.fieldHeight, Tetris.fieldWidth);
//        temp = mainLoopV2(Tetris.nextPiece, Tetris.nextRot);
//        xNext = temp[0];
//        yNext = temp[1];
//        rotationNext = temp[2];
//        return new int[]{xCurrent,yCurrent,rotationCurrent,xNext,yNext,rotationNext};
//    }

    //TODO What is it still doing here?
    public static void findBestPlaceToPlace() throws IOException {
        System.out.println("PRINT MATRIX");
        Tetris.printMatrix(Tetris.field);

        int cr = Tetris.movePieceDown(false);
        if(cr==-2) return;
        bestX = -1;
        bestY = -1;
        bestRotation = -1;
        curPiece = 0;
        curPiece = Tetris.curPiece;
        bestRotation = Tetris.curPieceRotation;
        genRewards(Tetris.field, Tetris.fieldHeight, Tetris.fieldWidth, Tetris.hiddenRows);
        mainLoop(curPiece,bestRotation);
        if(bestX==-1||bestRotation==-1) {
            return;
        }
        System.out.println(curPiece+" "+bestRotation);
        for(int i=0;i<bestX;i++){
            Tetris.movePiece(true);
        }
        while(Tetris.curPieceRotation!=bestRotation){
            Tetris.rotatePiece(true);
        }
        while(cr==-1){
            cr= Tetris.movePieceDown(false);
            try {
                Thread.sleep(200);
            }
            catch(InterruptedException ex){
                Thread.currentThread().interrupt();
            }
        }
        // genRewards(tempField, Tetris.fieldHeight, Tetris.fieldWidth);
        // mainLoop(Tetris.nextPiece,Tetris.nextRot);
        findBestPlaceToPlace();
    }
    /***
     *
     * @param piece: currently considered piece (curPiece: 1 / nextPiece: 2)
     * @param pieceRotation:  rotation [curPieceRotation / nextRot](just to determine whether piece is flipped)
     */
//    private static int[] mainLoopV2(int piece, int pieceRotation) {
//        int highestScore = 0;
//        int highestRotation = -1;
//        int highestX = -1;
//        int highestY = -1;
//        boolean isMirrored = false;
//        if (pieceRotation > 3) isMirrored = true;
//        int[][][] pieceArr = PentominoDatabase.data[piece];
//        for (int x = 0; x < Tetris.fieldWidth; x++) {
//            for (int y = 0; y < Tetris.fieldHeight; y++) {
//                int[] temp = pLoop(x, y, pieceArr, isMirrored);
//                if (temp[0] > highestScore) {
//                    highestScore = temp[0];
//                    highestRotation = temp[1];
//                    highestX = x;
//                    highestY = y;
//                }
//            }
//        }
//        int[][] pieceToPlace = PentominoDatabase.data[piece][highestRotation];
//        for (int i = 0; i < pieceToPlace.length; i++) { // loop over x position of pentomino
//            for (int j = 0; j < pieceToPlace[i].length; j++) { // loop over y position of pentomino
//                if (pieceToPlace[i][j] == 1) {
//                    // Add the ID of the pentomino to the board if the pentomino occupies this square
//                    tempField[highestX + j][highestY + i] = piece;
//                }
//            }
//        }
//        return new int[]{highestX, highestY, highestRotation};
//    }

    //TODO what is this still doing here?
    private static void mainLoop(int piece, int pieceRotation){
        int highestScore = 0;
        boolean isMirrored = false;
        if(pieceRotation>3) isMirrored = true;
        int[][][] pieceArr = PentominoDatabase.data[piece];
        for (int x = 0; x < Tetris.fieldWidth; x++) {
            for (int y = 0; y < Tetris.fieldHeight; y++) {
                int[] temp = pLoop(x,y,pieceArr,isMirrored);
                if(temp[0]>highestScore){
                    highestScore=temp[0];
                    bestRotation=temp[1];
                    bestX = x;
                    bestY = y;
                }
            }
        }
        int[][] pieceToPlace = PentominoDatabase.data[piece][bestRotation];
        for(int i = 0; i < pieceToPlace.length; i++){ // loop over x position of pentomino
            for (int j = 0; j < pieceToPlace[i].length; j++){ // loop over y position of pentomino
                if (pieceToPlace[i][j] == 1){
                    // Add the ID of the pentomino to the board if the pentomino occupies this square
                    //tempField[bestX + i][bestY + j] = piece;
                }
            }
        }
    }

    /***
     * Loop for a point "P" from mainLoop
     * @param xCoord: x coordinate of point P
     * @param yCoord: y coordinate of point P
     * @param pieceArr
     * @param isMirrored: boolean value, checking if piece is mirrored
     * @return: score for current piece in current point P and the rotation yielding that score
     */
    //TODO what does "Loop for a point "P" from mainLoop" mean?
    private static int[] pLoop(int xCoord, int yCoord, int[][][] pieceArr, boolean isMirrored){
        int[] iterator;
        int highestScoreRotation = -1;
        int highestScore = 0;
        if(isMirrored){
            iterator = new int[]{4,5,6,7};
        }
        else{
            iterator = new int[]{0,1,2,3};
        }
        for(int rotationIndex=0; rotationIndex<iterator.length; rotationIndex++){
            int[][] pieceArr2D = pieceArr[iterator[rotationIndex]];
            int curScore = 0;
            if(xCoord+pieceArr2D.length<fieldScores.length && yCoord+pieceArr2D[0].length < fieldScores[0].length)
            for(int i=0; i<pieceArr2D.length;i++) {
                for (int j = 0; j < pieceArr2D[i].length; j++) {
                    if (pieceArr2D[i][j] != 0) {
                        curScore+=fieldScores[i+xCoord][j+yCoord];
                    }
                }
            }
            if(curScore>highestScore){
                highestScore=curScore;
                highestScoreRotation=iterator[rotationIndex];
            }
        }
        return new int[]{highestScore,highestScoreRotation};
    }

    private static int[][] flipMatrix(int[][] field){
        int[][] flipped = new int[field[0].length][field.length];

        //flip x and y
        for(int i=0; i<field.length; i++){
            for(int j=0; j<field[0].length; j++){
                flipped[j][i] = field[i][j];
            }
        }

        return flipped;
    }


    //TODO let row elimination come before placing the next block
    private static void genRewards(int[][] field, int fieldHeight, int fieldWidth, int fieldPadding) {
        int[][] flipped = flipMatrix(field);

        Tetris.printMatrix(flipped);

        //remove unplayable part of the field
        int[][] tmp = new int[fieldHeight-fieldPadding][fieldWidth];
        for(int i=0; i<fieldHeight-fieldPadding; i++){
            tmp[i] = flipped[i+fieldPadding];
        }

        flipped = tmp;

        Tetris.printMatrix(flipped);

        //flip the field so i is y, j is x and a higher x means something is on the right and not on the left
        //alternative comment: prevent headache
        fieldScores = new int[fieldHeight-fieldPadding][fieldWidth];

        //give scores
        //skip the part of the field that isn't in the playable area
        for (int i = 0; i < fieldScores.length; i++) {
            for (int j = 0; j < fieldScores[0].length; j++) {
                int amountOfBlocksSurrounding = 0;
                int amountOfBlocksInRow = 0;

                //only check field that aren't filled (you don't want to put a score in a cell that has already been taken)
                if (flipped[i][j] == -1) {
                    //check surroundings
                    //check same layer, but exclude the cell itself
                    if (i > 1) {
                        //left
                        if (flipped[i-1][j] != -1) amountOfBlocksSurrounding++;
                    }
                    if (i < fieldWidth - 1) {
                        //right
                        if (flipped[i+1][j] != -1) amountOfBlocksSurrounding++;
                    }

                    //check layer below
                    if (j>1) {

                        //left below
                        if (i > 1) {
                            if (flipped[i-1][j-1] != -1) amountOfBlocksSurrounding++;
                        }

                        //below
                        if (flipped[i][j-1] != -1) amountOfBlocksSurrounding++;

                        //below right
                        if (i < fieldWidth - 1) {
                            if (flipped[i+1][j-1] != -1) amountOfBlocksSurrounding++;
                        }
                    }

                    //get the amount of blocks in this row
                    for (int k = 0; k < fieldWidth; k++) {
                        if (flipped[k][j] != -1) {
                            amountOfBlocksInRow++;
                        }
                    }

                    //update score
                    //multiply the surrounding blocks by the height and add a default 'bonus' based on the layer
                    fieldScores[i][j] = amountOfBlocksSurrounding * (5 * (i + 1)) + 25 * (i) + amountOfBlocksInRow * (3 * (i) + 3) + 1;
                } else {
                    //if the cell is already taken, set it's score to 0
                    fieldScores[i][j] = 0;
                }
            }
        }

//        //debugging
//        //print flipped
//        for (int i = 0; i < fieldHeight-fieldPadding; i++) {
//            for (int j = 0; j < fieldWidth; j++) {
//                System.out.print(String.format("%6d", fieldScores[i][j]));
//            }
//            System.out.println();
//        }
//        System.out.println();

        //flip back because others chose to suffer
        fieldScores = flipMatrix(fieldScores);
    }

    private static int[][] copyField(int[][] f0){
        int [][] f1=new int[Tetris.fieldWidth][Tetris.fieldHeight];
        for(int i = 0; i< Tetris.fieldWidth; i++){
            for(int j = 0; j< Tetris.fieldHeight; j++){
                f1[i][j]=f0[i][j];
            }
        }
        return f1;
    }

    private static void movePieceUp() {
        // Create temporary position; move it up by 1
        int[] temPos = Tetris.arrayCopy(Tetris.curPos);
        temPos[1] -= 1;

        // Create temporary permutation
        int tempPer = Tetris.curPieceRotation;

        // Indicates whether the loop needs to be stopped
        boolean stop = false;

        // Indicates whether the loop is being run for the first time
        boolean initial = true;

        /* A while loop which goes through every single permutation of the current pentomino. It will only stop when either
        of these two situations occurs:
        1) Every single permutation has been tried
        2) The pentomino is able to go up
         */
        while (tempPer < 7 && !stop) {
            if (!checkCollision(PentominoDatabase.data[curPiece][tempPer], Tetris.field, temPos[0], temPos[1])) {
                // Able to move up
                Tetris.curPos[1] -= 1;
                Tetris.tempField = copyField(Tetris.field);
                Tetris.addPiece();
                temPos[1] -= 1;
                // Update permutation
                Tetris.curPieceRotation = tempPer;
                // Stop loop; permutation found
                stop = true;
            } else {
                // In case this is the first time the loop is activated: reset permutation to zero
                if (initial) {
                    tempPer = 0;
                    initial = false;
                } else
                    // Try next permutation
                    tempPer++;
            }
        }
        // if the bot tried all the permutations and it didn't succeed in going up
        if(tempPer==7 && stop){
            // the scores of the temporary position are set to 0 
            fieldScores[temPos[0]][temPos[1]]=0;
            // this is a recursive call that will take the next best position since the scores 
            // of the previous best position are set to 0
            movePieceUp();
        }
    }

    //Try to move the pentomino up one line
    //y is the current y
    //the x and y are the starting point for the pentomino
    //pent = pentomino and it requires a matrix that already has the desired rotation
    //TODO check if it works
    public static boolean checkCollision(int[][] pent, int[][] field, int x, int y) {

        //check if it fits on the field
        if (y + pent.length < Tetris.fieldHeight) {
            //loop over x position of pentomino
            for (int i = 0; i < pent.length; i++) {
                //loop over y position of pentomino
                for (int j = 0; j < pent[i].length; j++) {
                    //if the block has a value in this cell
                    if (pent[i][j] == 1) {
                        //check if the cell in the game field is occupied
                        if (field[x + i][y + j + 1] != -1) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }
}
