import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Gbot {
    private static int populationSize = 100;
    private static int tournamentSize = 5;
    private static int parNo = 8;
    private static int gamesN = 30;
    private static double[][] genomes = new double[populationSize][parNo];
    private static double[][] top10 = new double[10][parNo];
    private static int currentGenome = 0;
    private static int generation = 0;
    private static double mutationRate = 0.1;
    private static double mutationStep = 0.01;
    private static double mutationRate2 = 0.05;
    private static double mutationStep2 = 3;
    private static int writerIterator = 0;
    private static BufferedWriter writer;
    private static File file;
    public static int games = 0;
    private static ArrayList<Integer> scores = new ArrayList<Integer>();
    public static int[] bestMoveNext;

    //A: 1.0 x 1.0 x 2.0 B: 1.0 x 1.5 x 2.0 C: 1.5 x 1.5 x 1.5
    public static int[][][][] aShape = new int[][][][]{
            //T
            {{{1, 1, 1}, {0, 1, 0}, {0, 1, 0}}},
            {{{1, 0, 0}, {1, 1, 1}, {1, 0, 0}}},
            {{{0, 1, 0}, {0, 1, 0}, {1, 1, 1}}},
            {{{0, 0, 1}, {1, 1, 1}, {0, 0, 1}}},
            {{{1}, {0}, {0}}, {{1}, {1}, {1}}, {{1}, {0}, {0}}},
            {{{1, 0, 0}}, {{1, 1, 1}}, {{1, 0, 0}}},
            {{{0}, {0}, {1}}, {{1}, {1}, {1}}, {{0}, {0}, {1}}},
            {{{0, 0, 1}}, {{1, 1, 1}}, {{0, 0, 1}}},
            {{{0}, {1}, {0}}, {{0}, {1}, {0}}, {{1}, {1}, {1}}},
            {{{0, 1, 0}}, {{0, 1, 0}}, {{1, 1, 1}}},
            {{{1}, {1}, {1}}, {{0}, {1}, {0}}, {{0}, {1}, {0}}},
            {{{1, 1, 1}}, {{0, 1, 0}}, {{0, 1, 0}}},
            //P
            {{{1, 1}, {1, 1}, {1, 0}}},
            {{{1, 1, 0}, {1, 1, 1}}},
            {{{0, 1}, {1, 1}, {1, 1}}},
            {{{1, 1, 1}, {0, 1, 1}}},
            {{{1, 1}, {1, 1}, {0, 1}}},
            {{{1, 1, 1}, {1, 1, 0}}},
            {{{1, 0}, {1, 1}, {1, 1}}},
            {{{0, 1, 1}, {1, 1, 1}}},
            {{{1}, {1}, {0}}, {{1}, {1}, {1}}},
            {{{1, 1, 0}}, {{1, 1, 1}}},
            {{{0}, {1}, {1}}, {{1}, {1}, {1}}},
            {{{0, 1, 1}}, {{1, 1, 1}}},
            {{{1}, {1}, {1}}, {{1}, {1}, {0}}},
            {{{1, 1, 1}}, {{1, 1, 0}}},
            {{{1}, {1}, {1}}, {{0}, {1}, {1}}},
            {{{1, 1, 1}}, {{0, 1, 1}}},
            {{{1}, {0}}, {{1}, {1}}, {{1}, {1}}},
            {{{1, 0}}, {{1, 1}}, {{1, 1}}},
            {{{0}, {1}}, {{1}, {1}}, {{1}, {1}}},
            {{{0, 1}}, {{1, 1}}, {{1, 1}}},
            {{{1}, {1}}, {{1}, {1}}, {{0}, {1}}},
            {{{1, 1}}, {{1, 1}}, {{0, 1}}},
            {{{1}, {1}}, {{1}, {1}}, {{1}, {0}}},
            {{{1, 1}}, {{1, 1}}, {{1, 0}}},
            //L
            {{{1}, {1}, {1}, {1}}, {{1}, {0}, {0}, {0}}},
            {{{1, 1, 1, 1}}, {{1, 0, 0, 0}}},
            {{{1}, {1}, {1}, {1}}, {{0}, {0}, {0}, {1}}},
            {{{1, 1, 1, 1}}, {{0, 0, 0, 1}}},
            {{{1}, {0}, {0}, {0}}, {{1}, {1}, {1}, {1}}},
            {{{1, 0, 0, 0}}, {{1, 1, 1, 1}}},
            {{{0}, {0}, {0}, {1}}, {{1}, {1}, {1}, {1}}},
            {{{0, 0, 0, 1}}, {{1, 1, 1, 1}}},
            {{{1, 1}, {1, 0}, {1, 0}, {1, 0}}},
            {{{1, 0, 0, 0}, {1, 1, 1, 1}}},
            {{{0, 1}, {0, 1}, {0, 1}, {1, 1}}},
            {{{1, 1, 1, 1}, {0, 0, 0, 1}}},
            {{{1, 1}, {0, 1}, {0, 1}, {0, 1}}},
            {{{1, 1, 1, 1}, {1, 0, 0, 0}}},
            {{{1, 0}, {1, 0}, {1, 0}, {1, 1}}},
            {{{0, 0, 0, 1}, {1, 1, 1, 1}}},
            {{{1}, {0}}, {{1}, {0}}, {{1}, {0}}, {{1}, {1}}},
            {{{1, 0}}, {{1, 0}}, {{1, 0}}, {{1, 1}}},
            {{{0}, {1}}, {{0}, {1}}, {{0}, {1}}, {{1}, {1}}},
            {{{0, 1}}, {{0, 1}}, {{0, 1}}, {{1, 1}}},
            {{{1}, {1}}, {{1}, {0}}, {{1}, {0}}, {{1}, {0}}},
            {{{1, 1}}, {{1, 0}}, {{1, 0}}, {{1, 0}}},
            {{{1}, {1}}, {{0}, {1}}, {{0}, {1}}, {{0}, {1}}},
            {{{1, 1}}, {{0, 1}}, {{0, 1}}, {{0, 1}}},
            //1x1.5x2
            /*{{{1,1,1,1},{1,1,1,1},{1,1,1,1}},{{1,1,1,1},{1,1,1,1},{1,1,1,1}}},
            {{{1,1,1},{1,1,1},{1,1,1},{1,1,1}},{{1,1,1},{1,1,1},{1,1,1},{1,1,1}}},
            {{{1,1},{1,1},{1,1}},{{1,1},{1,1},{1,1}},{{1,1},{1,1},{1,1}},{{1,1},{1,1},{1,1}}},
            {{{1,1,1,1},{1,1,1,1}},{{1,1,1,1},{1,1,1,1}},{{1,1,1,1},{1,1,1,1}}},
            {{{1,1,1},{1,1,1}},{{1,1,1},{1,1,1}},{{1,1,1},{1,1,1}},{{1,1,1},{1,1,1}}},
            {{{1,1},{1,1},{1,1},{1,1}},{{1,1},{1,1},{1,1},{1,1}},{{1,1},{1,1},{1,1},{1,1}}},
            //1x1x2
            {{{1,1,1,1},{1,1,1,1}},{{1,1,1,1},{1,1,1,1}}},
            {{{1,1},{1,1},{1,1},{1,1}},{{1,1},{1,1},{1,1},{1,1}}},
            {{{1,1},{1,1}},{{1,1},{1,1}},{{1,1},{1,1}},{{1,1},{1,1}}},
            //1.5x1.5x1.5
            {{{1,1,1},{1,1,1},{1,1,1}},{{1,1,1},{1,1,1},{1,1,1}},{{1,1,1},{1,1,1},{1,1,1}}}*/
    };

    public static void addSolid(int[][][] solid, int[] coord, int color) { //add the solid to the container at the specified coordinates
        if (!checkCollision(solid, coord)) {
            for (int i = 0; i < solid.length; i++) {
                for (int j = 0; j < solid[i].length; j++) {
                    for (int k = 0; k < solid[i][j].length; k++) {
                        if (solid[i][j][k] == 1) {
                            FX3D.tmpUIInput[coord[0] + i][coord[1] + j][coord[2] + k] = color;
                        }
                    }
                }
            }
        } else {
            System.out.println("Can't place object");
        }
    }

    public static boolean checkCollision(int[][][] solid, int[] coord) {
        if (coord[0] < 0 || coord[1] < 0 || coord[2] < 0) {
            return true;
        } else {
            try {
                for (int i = 0; i < solid.length; i++) { // loop over x position of pentomino
                    for (int j = 0; j < solid[i].length; j++) { // loop over y position of pentomino
                        for (int k = 0; k < solid[i][j].length; k++) {
                            if (solid[i][j][k] == 1) {
                                if (FX3D.UIInput[coord[0] + i][coord[1] + j][coord[2] + k] != 0) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                return true;
            }
        }
        return false;
    }

    public static void test() {
        //getBestMove1();
        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int[] bm = getBestMove();
                //System.out.println(Arrays.toString(bm));

                dropPiece(aShape[bm[0]], new int[]{0, bm[1], bm[2]}, bm[0] + 1);
                System.out.println(getHeight());
            }
        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();

        /*
        try{Thread.sleep(1000);} catch(Exception e){}
        addSolid(aShape[2], new int[]{6, 0, 0}, 3);
        FX3D.updateUI();*/
    }

    public static boolean dropPiece(int[][][] solid, int[] coord, int color) {
        int[] coord1 = coord.clone();
        coord1[0]++;
        while (!checkCollision(solid, coord1)) {
            FX3D.tmpUIInput = FX3D.copyInput(FX3D.UIInput);
            addSolid(solid, coord1, color);
            coord1[0]++;
        }
        FX3D.UIInput = FX3D.copyInput(FX3D.tmpUIInput);
        FX3D.updateUI();
        if (coord1[0] == coord[0] + 1) return false;
        else return true;
    }

    public static int[][][] copyField(int[][][] f0) {
        int x = Wrapper.CONTAINER_WIDTH / Wrapper.cellSize;
        int y = Wrapper.CONTAINER_HEIGHT / Wrapper.cellSize;
        int z = Wrapper.CONTAINER_DEPTH / Wrapper.cellSize;
        int[][][] f1 = new int[x][y][z];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    f1[i][j][k] = f0[i][j][k];
                }
            }
        }
        return f1;
    }

    public static Integer[] arrayCopy(Integer[] old) {
        Integer[] n = new Integer[old.length];
        for (int i = 0; i < old.length; i++) {
            n[i] = old[i];
        }
        return n;
    }

    public static int[] arrayCopy(int[] old) {
        int[] n = new int[old.length];
        for (int i = 0; i < old.length; i++) {
            n[i] = old[i];
        }
        return n;
    }

    public static double[] arrayCopy(double[] old) {
        double[] n = new double[old.length];
        for (int i = 0; i < old.length; i++) {
            n[i] = old[i];
        }
        return n;
    }

    /**
     * Creates the initial population of genomes, each with random genes
     */
    public static void initPopulation() {
        for (int i = 0; i < populationSize; i++) {
            double[] gen = {Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5, Math.random() * 0.5, Math.random() - 0.5, Math.random() - 0.5, 0};
            genomes[i] = gen;
            if (i < top10.length) top10[i] = gen.clone();
        }
        //genomes[0]=new double[]{3.6974782858162163, 0.020477199087742037, -0.23918114942987156, -0.11603246371208442, -1.6252129706895644, -0.6811390204242889, 1.044093597983097, 4160.0};
    }

    /**
     * Plays the game by making the next move (makePlay)
     */
//    public static void Play() {
//        //Tetris.training = false;
//        double []gen1={14.928740819414838, 0.22815685282185916, -0.40505060750400884, 1.3945551853055056, -5.415407771851985, -2.5420017356444644, 1.0560452657245811, 4640.0}; //best individual
//        //double []gen1={2.883322384854915, 0.48480348209139357, -2.7311401426486226, 0.7769037846237963, -4.525753498240361, -4.223996415025829, 1.088329351302793, 3803.3333333333335}; //best individual
//
//        genomes[0] = gen1;
//        currentGenome=0;
//        makePlay(true);
//    }
//
//    /**
//     * Trains the neural network
//     */
//    public static void train() {
//        //Tetris.training = true;
//        LocalDate myObj = LocalDate.now();
//        file = new File("src/resources/topIndividuals"+myObj.toString()+".txt");
//        initPopulation();
//        while(true){
//            //System.out.println(generation);
//            evalPopulation();
//            getNextGen();
//        }
//        /*try {
//            Thread.sleep(100);
//        }
//        catch(InterruptedException ex){
//            Thread.currentThread().interrupt();
//        }*/
//    }

    /**
     * Updates the saved top 10 individuals
     */
    public static void updateTop10() {

        for (int i = 0; i < top10.length; i++) {
            if (genomes[0][7] > top10[i][7]) {
                top10[top10.length - 1] = genomes[0].clone();
                break;
            }
        }
        java.util.Arrays.sort(top10, new java.util.Comparator<double[]>() {
            public int compare(double[] a, double[] b) {
                return Double.compare(b[7], a[7]);
            }
        });

        for (int i = 0; i < top10.length; i++)
            System.out.println(Arrays.toString(top10[i]));
    }

    /**
     * Evaluates the next individual in the population. If there is none, evolves the population
     */
//    private static void evalPopulation() {
//        State s1=new State();
//        for(int i=0;i<populationSize;i++){
//            System.out.print("-");
//            loadState(s1);
//            currentGenome=i;
//            makePlay(false);
//
//        }
//        System.out.println();
//    }

    /**
     * Creates the new population using the best individuals from the last
     */
    private static void getNextGen() {
        generation++;
        //geneSort(genomes);

        updateTop10();

        try {
            writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(Arrays.toString(genomes[0]) + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        for(int i=0;i<5;i++)
//            System.out.println(Arrays.toString(genomes[i]));
        /*for(int i=0;i<genomes.length;i++){
            System.out.println(Arrays.toString(genomes[i]));
        }
        System.out.println();*/
        int k = 0;
        double[][] tempGene = new double[populationSize][parNo];
        int[] parents = new int[2];
        int elites = (int) (genomes.length / 50.0);
        for (int i = 0; i < elites; i++) {
            tempGene[i] = genomes[i];
        }
        for (int i = elites; i < populationSize; i++) {
            parents = tournamentSelection(tournamentSize);
            tempGene[i] = mate(parents[0], parents[1]);
        }
        genomes = tempGene;
    }

    /**
     * Performs the tournament selection for selection of parents
     *
     * @param tSize: size of population to undergo the selection
     * @return: best parents from the tournament selection
     */
    public static int[] tournamentSelection(int tSize) {
        int[] parents = {0, 0};
        double bestScore = 0;
        int bestInd = 0;
        int a = 0;
        for (int i = 0; i < tSize; i++) {
            a = (int) (Math.random() * populationSize);
            if (genomes[a][7] > bestScore) {
                bestScore = genomes[a][7];
                bestInd = a;
            }
        }
        parents[0] = bestInd;
        bestScore = 0;
        bestInd = 0;
        for (int i = 0; i < tSize; i++) {
            a = (int) (Math.random() * populationSize);
            if (genomes[a][7] > bestScore) {
                bestScore = genomes[a][7];
                bestInd = a;
            }
        }
        parents[1] = bestInd;
        //System.out.println(Arrays.toString(parents));
        return parents;
    }

    /**
     * Gets a new child based on two parents (mom and dad)
     *
     * @param mom: female parent
     * @param dad: male parent
     * @return: a child of two individuals
     */
    private static double[] mate(int mom, int dad) {
        int crossover = (int) Math.random() * 6;
        double[] child = new double[parNo];
        for (int i = 0; i < crossover; i++) {
            child[i] = genomes[dad][i];
        }
        for (int i = crossover; i < child.length - 1; i++) {
            child[i] = genomes[mom][i];
        }
        child[7] = 0;

        for (int i = 0; i < child.length - 1; i++) {
            if (Math.random() < mutationRate) {
                double mut = Math.random() * mutationStep * 2.0;
                child[i] = child[i] + mut - mutationStep;
            }
            if (Math.random() < mutationRate2) {
                double mut = Math.random() * mutationStep2 * 2.0;
                child[i] = child[i] + mut - mutationStep2;
            }
            if (Math.random() < mutationRate) {
                child[i] = (genomes[mom][i] + genomes[dad][i]) / 2;
            }
        }
        return child;
    }

    /**
     * Gets a best move
     *
     * @return
     */
    private static int[] getBestMove() {
        int[][][] state0 = copyField(FX3D.UIInput);
        double[] algorithm = new double[6];
        ArrayList<Integer[]> possibleMoves = new ArrayList<Integer[]>();
        Integer[] move = new Integer[4]; //rotation,translation,rating
        for (int a = 0; a < aShape.length; a++) {
            for (int i = 0; i < Wrapper.ACTUAL_CONTAINER_HEIGHT * 2; i++) {
                for (int j = 0; j < Wrapper.ACTUAL_CONTAINER_DEPTH * 2; j++) {
                    if (dropPiece(aShape[a], new int[]{0, i, j}, a + 1)) {
                        int rating = 0;

                        algorithm[0] = getHeight();
                        algorithm[1] = getCompactness();


                        rating += algorithm[0] * -1 + getCompactness()* -1;

                        move[0] = a;
                        move[1] = i;
                        move[2] = j;
                        move[3] = rating;
                        possibleMoves.add(move.clone());

                        FX3D.UIInput = copyField(state0);
                        FX3D.updateUI();
                    }
                }
            }
        }
        FX3D.UIInput = copyField(state0);
        FX3D.tmpUIInput = copyField(state0);
        FX3D.updateUI();

        int maxR = -10000;
        int maxMove = 0;
        for (int i = 0; i < possibleMoves.size(); i++) {
            if (possibleMoves.get(i)[3] > maxR) {
                maxR = possibleMoves.get(i)[3];
                maxMove = i;
            }
        }

        int[] bestMove = new int[4];
        bestMove[0] = possibleMoves.get(maxMove)[0];
        bestMove[1] = possibleMoves.get(maxMove)[1];
        bestMove[2] = possibleMoves.get(maxMove)[2];
        bestMove[3] = possibleMoves.get(maxMove)[3];
        return bestMove;
    }
//        for (int i = 0; i < rot[Tetris.curPiece]; i++) { //for each possible rotation
//            loadState(s1);
//            for (int t = 0; t < Tetris.fieldWidth; t++) {
//                loadState(s1);
//                boolean moved=true;
//                for (int k = 0; k < i; k++) Tetris.rotatePiece(true);
//                for (int k = 0; k < Tetris.fieldWidth; k++){
//                    Tetris.movePiece(false);
//                }
//                for (int k = 0; k < t; k++){
//                    if(!Tetris.movePiece(true)) moved=false;
//                }
//                if(!moved){
//                    break;
//                }
//                int er = Tetris.dropPiece(true);
//                int rating1 = 0;
//                if(er==-2){
//                    rating1-=500;
//                    er=0;
//                }
//                if(er==-1) er=0;
//
//                algorithm[0] = er;
//                algorithm[1] = Math.pow(getHeight(), 1.5);
//                algorithm[2] = getCumHeight();
//                algorithm[3] = getRelHeight();
//                algorithm[4] = getHoles();
//                algorithm[5] = getRoughness();
//
//                rating1 += algorithm[0] * genomes[currentGenome][0];
//                rating1 += algorithm[1] * genomes[currentGenome][1];
//                rating1 += algorithm[2] * genomes[currentGenome][2];
//                rating1 += algorithm[3] * genomes[currentGenome][3];
//                rating1 += algorithm[4] * genomes[currentGenome][4];
//                rating1 += algorithm[5] * genomes[currentGenome][5];
//                if(bestMoveNext!=null && i==bestMoveNext[0] && t==bestMoveNext[1]) rating1*=genomes[currentGenome][6];
//
//                State s2=new State();
//                for (int j = 0; j < rot[Tetris.nextPiece]; j++) { //for each possible rotation
//                    for (int l = 0; l < Tetris.fieldWidth; l++) {
//                        Tetris.field = copyField(s2.oldField);
//                        Tetris.score = s2.oldScore;
//                        Tetris.curPiece = Tetris.nextPiece;
//                        Tetris.curPieceRotation = Tetris.nextRot;
//                        Tetris.curPos = s2.oldPiecePos;
//                        moved=true;
//                        for (int k = 0; k < j; k++) Tetris.rotatePiece(true);
//                        for (int k = 0; k < Tetris.fieldWidth; k++){
//                            Tetris.movePiece(false);
//                        }
//                        for (int k = 0; k < l; k++){
//                            if(!Tetris.movePiece(true)) moved=false;
//                        }
//                        if(!moved){
//                            break;
//                        }
//                        er = Tetris.dropPiece(true);
//                        int rating2 = 0;
//                        if (er == -2) {
//                            rating2 -= 500;
//                            er = 0;
//                        }
//                        if (er == -1) er = 0;
//
//                        algorithm[0] = er;
//                        algorithm[1] = Math.pow(getHeight(), 1.5);
//                        algorithm[2] = getCumHeight();
//                        algorithm[3] = getRelHeight();
//                        algorithm[4] = getHoles();
//                        algorithm[5] = getRoughness();
//
//                        rating2 += algorithm[0] * genomes[currentGenome][0];
//                        rating2 += algorithm[1] * genomes[currentGenome][1];
//                        rating2 += algorithm[2] * genomes[currentGenome][2];
//                        rating2 += algorithm[3] * genomes[currentGenome][3];
//                        rating2 += algorithm[4] * genomes[currentGenome][4];
//                        rating2 += algorithm[5] * genomes[currentGenome][5];
//
//                        move[0] = i;
//                        move[1] = t;
//                        move[2] = j;
//                        move[3] = l;
//                        move[4] = rating1;
//                        move[5] = rating2;
//                        move[6] = rating1 + rating2;
//                        possibleMoves.add(move.clone());
//
//                        try {
//                            Thread.sleep(1000);
//                        }
//                        catch(InterruptedException ex){
//                            Thread.currentThread().interrupt();
//                        }
//                    }
//                }
//            }
//        }
//        loadState(s1);
//
//        int maxR=-10000;
//        int maxMove=0;
//        for (int i = 0; i < possibleMoves.size(); i++) {
//            if(possibleMoves.get(i)[6]>maxR) {
//                maxR=possibleMoves.get(i)[6];
//                maxMove=i;
//            }
//        }
//
//        int[] bestMove=new int[3];
//        bestMove[0]=possibleMoves.get(maxMove)[0];
//        bestMove[1]=possibleMoves.get(maxMove)[1];
//        bestMove[2]=possibleMoves.get(maxMove)[6];
//
//        bestMoveNext=new int[3];
//        bestMoveNext[0]=possibleMoves.get(maxMove)[2];
//        bestMoveNext[1]=possibleMoves.get(maxMove)[3];
//        bestMoveNext[2]=possibleMoves.get(maxMove)[5];
//        return bestMove;
//    }


    /**
     * Makes next move based on the genome
     * @param play: true if supposed to make next move, false otherwise
     */
//    public static void makePlay(boolean play) {
//        if(play){
//            while(true) {
//                int[] bestMove = getBestMove1();
//                for (int i = 0; i < bestMove[0]; i++) Tetris.rotatePiece(true);
//                for (int i = 0; i < Tetris.fieldWidth; i++) Tetris.movePiece(false);
//                for (int i = 0; i < bestMove[1]; i++) Tetris.movePiece(true);
//                int clearedRows = -1;
//                while (clearedRows == -1) {
//                    clearedRows = Tetris.movePieceDown(false);
//                    try {
//                        Thread.sleep(50);
//                    } catch (InterruptedException ex) {
//                        Thread.currentThread().interrupt();
//                    }
//                }
//                if (clearedRows == -2) {
//                    scores.add(Tetris.lastScore);
//                    games++;
//                    //System.out.println(games);
//                }
//            }
//        }else{
//            while(games<gamesN){
//                int[]bestMove=getBestMove1();
//                for (int i = 0; i < bestMove[0]; i++) Tetris.rotatePiece(true);
//                for (int i = 0; i < Tetris.fieldWidth; i++) Tetris.movePiece(false);
//                for (int i = 0; i < bestMove[1]; i++) Tetris.movePiece(true);
//                int clearedRows=-1;
//                while(clearedRows==-1){
//                    clearedRows=Tetris.dropPiece(false);
//            /*try {
//                Thread.sleep(5);
//            }
//            catch(InterruptedException ex){
//                Thread.currentThread().interrupt();
//            }*/
//                }
//                if(clearedRows==-2){
//                    scores.add(Tetris.lastScore);
//                    games++;
//                    //System.out.println(games);
//                }
//            }
//            Integer sum = 0;
//            if(!scores.isEmpty()) {
//                for (Integer mark : scores) {
//                    sum += mark;
//                }
//                double avg=sum.doubleValue() / scores.size();
//                genomes[currentGenome][7]=avg;
//                //System.out.println(avg);
//                scores= new ArrayList<Integer>();
//            }
//        }
//
//        Tetris.wipeField(Tetris.field);
//        Tetris.tempField = copyField(Tetris.field);
//        Tetris.instantiateNewPiece(false);
//        Tetris.start = true;
//        games = 0;
//        bestMoveNext = new int[3];
//    }

    /**
     * Calculates the cumulative height
     *
     * @return: cumulative height
     */
    private static double getCumHeight() {
        int cumHeight = 0;
        /*int [] l = new int [Tetris.fieldWidth];
        for (int i = 0; i < l.length; i++) l[i]=20;
        for (int i = 0; i < Tetris.fieldWidth; i++) {
            for (int j = 0; j < Tetris.fieldHeight; j++) {
                if(Tetris.field[i][j]!=-1){
                    l[i]=20-j;
                    break;
                }
            }
        }
        for(int i=0;i<l.length;i++){
            if(l[i]==20)l[i]=0;
            cumHeight+=l[i];
        }*/
        return cumHeight;
    }

    /**
     * Gets number of holes in the game field
     *
     * @return: number of holes in game field
     */
    private static double getHoles() {
        int holes = 0;/*
        int [] l = new int [Tetris.fieldWidth];
        for (int i = 0; i < l.length; i++) l[i]=20;
        for (int i = 0; i < Tetris.fieldWidth; i++) {
            for (int j = 0; j < Tetris.fieldHeight; j++) {
                if(Tetris.field[i][j]!=-1){
                    l[i]=j;
                    break;
                }
            }
        }
        for(int i=0;i<Tetris.fieldWidth;i++){
            for(int j=l[i];j<Tetris.fieldHeight;j++){
                if(Tetris.field[i][j]==-1) holes++;
            }
        }*/
        return holes;
    }

    /**
     * Gets roughness
     *
     * @return: roughness
     */
    private static double getRoughness() {
        int roughness = 0;/*
        int [] l = new int [Tetris.fieldWidth];
        for (int i = 0; i < l.length; i++) l[i]=20;
        for (int i = 0; i < Tetris.fieldWidth; i++) {
            for (int j = 0; j < Tetris.fieldHeight; j++) {
                if(Tetris.field[i][j]!=-1){
                    l[i]=20-j;
                    break;
                }
            }
        }
        for(int i=0;i<l.length;i++) {
            if (l[i] == 20) l[i] = 0;
        }
        for(int i=0;i<l.length-1;i++){
            roughness+=Math.abs(l[i]-l[i+1]);
        }*/
        return roughness;
    }

    /**
     * Gets relative height
     *
     * @return: relative height
     */
    private static double getRelHeight() {
        int relheight = 0;/*
        int [] l = new int [Tetris.fieldWidth];
        for (int i = 0; i < l.length; i++) l[i]=20;
        for (int i = 0; i < Tetris.fieldWidth; i++) {
            for (int j = 0; j < Tetris.fieldHeight; j++) {
                if(Tetris.field[i][j]!=-1){
                    l[i]=20-j;
                    break;
                }
            }
        }
        for(int i=0;i<l.length;i++) {
            if (l[i] == 20) l[i] = 0;
        }
        int max=0;
        int min=20;
        for(int i=0;i<l.length;i++) {
            if(l[i]>max)max=l[i];
            else if(l[i]<min)min=l[i];
        }
        relheight=(int)(max-min);*/
        return relheight;
    }

    /**
     * Gets height
     *
     * @return height
     */
    private static double getHeight() {
        int x = (int) Math.round(Wrapper.ACTUAL_CONTAINER_WIDTH * 2);
        int y = (int) Math.round(Wrapper.ACTUAL_CONTAINER_HEIGHT * 2);
        int z = (int) Math.round(Wrapper.ACTUAL_CONTAINER_DEPTH * 2);
        int height = 0;
        outerloop:
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    if (FX3D.UIInput[i][j][k] != 0) {
                        height = x - i;
                        break outerloop;
                    }
                }
            }
        }
        return height;
    }


       // Method initally suppied by Sam, could be developed further
        private static double getCompactness() {
        int x = (int)Math.round(Wrapper.ACTUAL_CONTAINER_WIDTH * 2);
        int y = (int) Math.round(Wrapper.ACTUAL_CONTAINER_HEIGHT * 2);
        int z = (int) Math.round(Wrapper.ACTUAL_CONTAINER_DEPTH * 2);
        System.out.println(x);
        int compactness=0;
        int cumDist = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    if (FX3D.UIInput[i][j][k] != 0) {
                        cumDist+=(Math.sqrt(Math.pow((i-x), 2)+Math.pow((j-y), 2)+Math.pow((k-z), 2)));
                    }
                }
            }
        }
        return compactness;
    }

    /*
    Based on https://core.ac.uk/download/pdf/82384325.pdf Section 2: Concepts and definitions

    private static double getCompactness() {
        int x = (int) Math.round(Wrapper.ACTUAL_CONTAINER_WIDTH * 2);
        int y = (int) Math.round(Wrapper.ACTUAL_CONTAINER_HEIGHT * 2);
        int z = (int) Math.round(Wrapper.ACTUAL_CONTAINER_DEPTH * 2);
        int enclosingSurfaceArea = 0;
        int volume = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    if (FX3D.UIInput[i][j][k] != 0) {
                        if(i-1>=0 && FX3D.UIInput[i-1][j][k] == 0){
                                enclosingSurfaceArea++;
                            }
                        if(j-1>=0 && FX3D.UIInput[i][j-1][k] == 0){
                                enclosingSurfaceArea++;
                            }
                        if(k-1>=0 && FX3D.UIInput[i-1][j][k] == 0){
                                enclosingSurfaceArea++;
                            }
                        if(i+1<x && FX3D.UIInput[i-1][j][k] == 0){
                                enclosingSurfaceArea++;
                            }
                        if(j+1<y && FX3D.UIInput[i-1][j][k] == 0){
                                enclosingSurfaceArea++;
                            }
                        if(k+1<x && FX3D.UIInput[i-1][j][k] == 0){
                                enclosingSurfaceArea++;
                            }
                        volume++;
                    }
                }
            }
        }
        return Math.pow(enclosingSurfaceArea,3)/Math.pow(volume,2);
    }*/

    /**
     * Sorts the array of genes
     *
     * @param gen: sorted array of genes
     */
    public static void geneSort(double[][] gen) {
        java.util.Arrays.sort(gen, new java.util.Comparator<double[]>() {
            public int compare(double[] a, double[] b) {
                return Double.compare(b[7], a[7]);
            }
        });
    }
}
