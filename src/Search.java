import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;

public class Search
{
    public static final int horizontalGridSize = 10;
    public static final int verticalGridSize = 6;

    public static final char[] input = {'P','X','F','V','W','Y','I','T','Z','U','N','L'};

    // Static UI class to display the board
    public static UI ui = new UI(horizontalGridSize, verticalGridSize, 50);

    // Helper function which starts the brute force algorithm
    public static void search()
    {
        // Initialize an empty board
        int[][] field = new int[horizontalGridSize][verticalGridSize];
		wipeField(field);
		algorithmX(buildMatrix(field));
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

    //TODO replace bruteForce with recursions
    private static void bruteForce( int[][] field){
    	Random random = new Random();
    	boolean solutionFound = false;

    	while (!solutionFound) {
    		solutionFound = true;

      	//Empty board again to find a solution
  			for (int i = 0; i < field.length; i++) {
  				for (int j = 0; j < field[i].length; j++) {
  					field[i][j] = -1;
  				}
  			}

    		//Put all pentominoes with random rotation/inversion on a random position on the board
    		for (int i = 0; i < input.length; i++) {

    			//Choose a pentomino and randomly rotate/inverse it
    			int pentID = characterToID(input[i]);
    			int mutation = random.nextInt(PentominoDatabase.data[pentID].length);
    			int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];

    			//Randomly generate a position to put the pentomino on the board
    			int x;
    			int y;
    			if (horizontalGridSize < pieceToPlace.length) {
    				//this particular rotation of the piece is too long for the field
    				x=-1;
    			} else if (horizontalGridSize == pieceToPlace.length) {
    				//this particular rotation of the piece fits perfectly into the width of the field
    				x = 0;
    			} else {
    				//there are multiple possibilities where to place the piece without leaving the field
    				x = random.nextInt(horizontalGridSize-pieceToPlace.length+1);
    			}

    			if (verticalGridSize < pieceToPlace[0].length) {
    				//this particular rotation of the piece is too high for the field
    				y=-1;
    			} else if (verticalGridSize == pieceToPlace[0].length) {
    				//this particular rotation of the piece fits perfectly into the height of the field
    				y = 0;
    			} else {
    				//there are multiple possibilities where to place the piece without leaving the field
    				y = random.nextInt(verticalGridSize-pieceToPlace[0].length+1);
    			}

    			//If there is a possibility to place the piece on the field, do it
    			if (x >= 0 && y >= 0) {
	    			addPiece(field, pieceToPlace, pentID, x, y);
	    		}
    		}

    		for (int i = 0; i < field.length; i++) {
				for (int j = 0; j < field[i].length; j++) {
					if(field[i][j] == -1) solutionFound = false;
				}
			}

     		//if (solutionFound) {
  			try{
  				Thread.sleep(100);
  				ui.setState(field);
  				System.out.println("Solution found");
  			} catch (InterruptedException ie){
    			//display the field
    			ui.setState(field);
    			System.out.println("Solution found");
    			break;
    		}
    	}
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

    //takes the possibilities matrix and outputs a solution for that matrix
	public static ArrayList<Integer> algorithmX(ArrayList<ArrayList<Integer>> matrix){
		int minC=1000000;
		int sumC=0;
		int indC=0;
		int n=0;
		ArrayList<Integer> sol=new ArrayList<Integer>();

		if(matrix.get(0).size()==0){
			return sol;
		}
		for (int i = 0; i < matrix.get(0).size(); i++) {
			n=0;
			for (int j = 0; j < matrix.size(); j++) {
				sumC+=matrix.get(j).get(i);
				n++;
			}
			if(sumC<minC){
				minC=sumC;
				indC=i;
				System.out.println(sumC+", "+indC);
			}
			sumC=0;
		}

		for (int i = 0; i < matrix.size(); i++) {
			if(matrix.get(i).get(indC)==1){
				sol=new ArrayList<Integer>();
			}
		}
		return sol;
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
	public static ArrayList<ArrayList<Integer>> buildMatrix(int[][] field) {
		int[][] matrix = new int[10000][(horizontalGridSize*verticalGridSize)+12];
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
								matrix[row][12 + onePos[w][0] + (onePos[w][1] * horizontalGridSize)] = 1;
							}
							matrix[row][pentID] = 1;
							wipeField(field);
							row++;
						}
					}
				}
			}
		}

		Integer[][] matrix1=new Integer[matrix.length][matrix[0].length];
		for (int k = 0; k < matrix.length; k++) {
			for (int l = 0; l < matrix[k].length; l++) {
				matrix1[k][l]=matrix[k][l];
			}
		}

		ArrayList<ArrayList<Integer>> list= new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < row; i++) {
			ArrayList<Integer> arl = new ArrayList<Integer>(Arrays.asList(matrix1[i]));
			list.add(arl);
		}
		return list;
	}

    // Main function. Needs to be executed to start the brute force algorithm
    public static void main(String[] args)
    {
        search();
    }
}
