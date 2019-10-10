import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;



public class SearchDLL
{
    public static final int horizontalGridSize = 6;
    public static final int verticalGridSize = 5;
    public static ArrayList<ArrayList<Boolean>> algXreturn;
    public static ArrayList<ArrayList<Integer>> supMat;
    public static ArrayList<Integer> solutions= new ArrayList<>();
    public static final char[] input = {'W','T','L','I','Y','V'};
    public static ArrayList<String> tempArr= new ArrayList<>();
    public static ArrayList<String> solArr= new ArrayList<>();
    // Static UI class to display the board
    //public static UI ui = new UI(horizontalGridSize, verticalGridSize, 50);

    // Helper function which starts the brute force algorithm
    public static void search()
    {
        // Initialize an empty board
        int[][] field = new int[horizontalGridSize][verticalGridSize];
        wipeField(field);
        dancingWithTheMoonlight(arrToDL(buildMatrix(field)));
    }
    //takes the pentomino character and outputs the unique integer ID for it
    private static int characterToID(char character) {
        int pentID = -1;
        if (character == 'X') {
            pentID = 0;
        } else if (character == 'I') {
            pentID = 1;
        } else if (character == 'Z') {
            pentID = 2;
        } else if (character == 'T') {
            pentID = 3;
        } else if (character == 'U') {
            pentID = 4;
        } else if (character == 'V') {
            pentID = 5;
        } else if (character == 'W') {
            pentID = 6;
        } else if (character == 'Y') {
            pentID = 7;
        } else if (character == 'L') {
            pentID = 8;
        } else if (character == 'P') {
            pentID = 9;
        } else if (character == 'N') {
            pentID = 10;
        } else if (character == 'F') {
            pentID = 11;
        }
        return pentID;
    }

    //TO DO
    public static Node arrToDL(ArrayList<ArrayList<Boolean>> matrix){
        Node header = new Node();
        header.setLeft(header);
        header.setRight(header);
        //creating a working node so that we don't have to track back to header every time
        Node workingNode = header;
        for(int i=0;i<matrix.get(0).size();i++){
            //adding a new column node
            workingNode.setRight(new Node());
            workingNode.getRight().setLeft(workingNode);
            workingNode=workingNode.getRight();
            workingNode.setRight(header);
            //saving it as a column node for reference in the internal for loop
            Node columnNode = workingNode;
            for(int j=0;j<matrix.size();j++){
                if(matrix.get(j).get(i)){
                    //if there is a 1/true in the matrix, adding a new node below the column node and entering it
                    workingNode.setDown(new Node());
                    workingNode.getDown().setUp(workingNode);
                    workingNode=workingNode.getDown();
                    workingNode.setColID(j);
                    workingNode.setRowID(i);
                    workingNode.setColumn(columnNode);
                    workingNode.setDown(columnNode);
                    columnNode.incrementNodeCount();
                    columnNode.setUp(workingNode);
                }
            }
            //getting back to the column node
            workingNode=columnNode;
        }
        return header;
    }

    public static void dancingWithTheMoonlight(Node header){
        ArrayList<Node> solutions=new ArrayList<>();
        if(header.getRight().equals(header)){
            System.out.println(solutions);
            return;
        }
            Node column=getMinColumn(header);
            cover(column);
            Node row=column;
            while(row.hasDown()){
                row=row.getDown();
                solutions.add(row);
                Node right = row;
                while(right.hasRight()){
                    right=right.getRight();
                    right=cover(right);
                }
                dancingWithTheMoonlight(header);
                solutions.remove(solutions.size()-1);
                column=row.getColumn();
                Node left = row;
                while(left.hasLeft()){
                    left=row.getLeft();
                    left=uncover(left);
                }
            }
        column=uncover(column);
    }

    public static Node cover(Node column){
        column.getRight().setLeft(column.getLeft());
        column.getLeft().setRight(column.getRight());
        Node row = column.getDown();
        while(row.hasDown()){
            Node right = row.getRight();
            while(right.hasRight()){
                right.getUp().setDown(right.getDown());
                right.getDown().setUp(right.getUp());
                right=right.getRight();
            }
            row=row.getDown();
        }
        return column;
    }

    public static Node uncover(Node column){
        Node row = column;
        while(row.hasUp()){
            row=row.getUp();
            Node left = row;
            while(left.hasLeft()){
                left=left.getLeft();
                left.getUp().setDown(left);
                left.getDown().setUp(left);
            }
        }
        column.getRight().setLeft(column);
        column.getLeft().setRight(column);
        return column;
    }

    public static Node getMinColumn(Node header){
        int minNum = Integer.MAX_VALUE;
        Node minNode=header;
        Node current=header;
        while(current.hasRight()) {
            if (!current.hasUp() && current.getNodeCount() < minNum) {
                minNum = current.getNodeCount();
                minNode = current;
                current=current.getRight();
            }
        }
        return minNode;
    }
    // Adds a pentomino to the position on the field (overriding current board at that position)
    public static int[][] addPiece(int[][] field, int[][] piece, int pieceID, int x, int y) {
        int[][] matrix = new int[5][2];
        int n=0;
        for(int i = 0; i < piece.length; i++) // loop over x position of pentomino
        {
            for (int j = 0; j < piece[i].length; j++) // loop over y position of pentomino
            {
                if (piece[i][j] == 1)
                {
                    // Add the ID of the pentomino to the board if the pentomino occupies this square
                    field[x + i][y + j] = pieceID;
                    matrix[n][0]=x + i;
                    matrix[n][1]=y + j;
                    n++;
                }
            }
        }

        return matrix;
    }

    //sets all squares in the field to -1
    public static void wipeField(int[][] field){
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = -1;
            }
        }
    }

    //returns a matrix with all possible positions for all pentominoes in the grid
    public static ArrayList<ArrayList<Boolean>> buildMatrix(int[][] field) {
        boolean[][] matrix = new boolean[10000][(horizontalGridSize*verticalGridSize)+12];
        int[][] onePos;
        int pentID = 0;
        int mutation = 0;
        int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];
        int x, y = 0;
        int row = 0;
        int n=0;
        //for every pentomino
        for (int i = 0; i < input.length; i++) {
            pentID = characterToID(input[i]);
            //for every mutation
            for (int j = 0; j < PentominoDatabase.data[pentID].length; j++) {
                mutation = j;
                pieceToPlace = PentominoDatabase.data[pentID][mutation];
                for (int k = 0; k < field.length; k++) {
                    for (int l = 0; l < field[k].length; l++) {
                        x = k;
                        y = l;
                        if (horizontalGridSize < pieceToPlace.length || x > horizontalGridSize - pieceToPlace.length) {
                            //this particular rotation of the piece is too long for the field
                            x = -1;
                        } else if (horizontalGridSize == pieceToPlace.length) {
                            //this particular rotation of the piece fits perfectly into the width of the field
                            x = 0;
                        }

                        if (verticalGridSize < pieceToPlace[0].length || y > verticalGridSize - pieceToPlace[0].length) {
                            //this particular rotation of the piece is too high for the field
                            y = -1;
                        } else if (verticalGridSize == pieceToPlace[0].length) {
                            //this particular rotation of the piece fits perfectly into the height of the field
                            y = 0;
                        }

                        //If there is a possibility to place the piece on the field, do it
                        if (x >= 0 && y >= 0) {
                            onePos = addPiece(field, pieceToPlace, pentID, x, y);
                            for (int w = 0; w < onePos.length; w++) {
                                matrix[row][12 + onePos[w][0] + (onePos[w][1] * horizontalGridSize)] = true;
                            }
                            matrix[row][pentID] = true;
                            wipeField(field);
                            row++;
                        }
                    }
                }
            }
        }

        Boolean[][] matrix1=new Boolean[matrix.length][matrix[0].length];
        for (int k = 0; k < matrix.length; k++) {
            for (int l = 0; l < matrix[k].length; l++) {
                matrix1[k][l]=matrix[k][l];
            }
        }

        ArrayList<ArrayList<Boolean>> list= new ArrayList<>();
        for (int i = 0; i < row; i++) {
            ArrayList<Boolean> arl = new ArrayList<>(Arrays.asList(matrix1[i]));
            list.add(arl);
        }
        return list;
    }

    // Main function. Needs to be executed to start the brute force algorithm
    public static void main(String[] args)
    {
        search();
        return;
        //System.out.println("The array is "+Arrays.toString(algXreturn.toArray()));
        // I don't know how to convert back from the list to a grid representation :(
    }
}
