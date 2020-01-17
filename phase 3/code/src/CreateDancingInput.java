import java.util.ArrayList;

public class CreateDancingInput {
    int[][][][] shapes;
    String type; //Parcels or Pentominoes
    ArrayList<Integer[][][]> placements = new ArrayList<Integer[][][]>();

    int width = (Wrapper.CONTAINER_DEPTH/Wrapper.cellSize);
    int height = (Wrapper.CONTAINER_HEIGHT/Wrapper.cellSize);
    int depth = (Wrapper.CONTAINER_WIDTH/Wrapper.cellSize);

    public CreateDancingInput(String type, int[][][][] shapes) {
        this.type = type;
        this.shapes = shapes;
    }

    public void createPlacements(){
        //For every shape
        for(int[][][] shape : shapes){
            //For every depth layer
            for(int z=0; z < depth; z++){
                //For every y
                for(int y=0; y < height; y++){
                    //For every x
                    for(int x=0; x < width; x++){
                        //Check if it fits
                        if(fits(x, y, z, shape)){
                            int shapeWidth = shape[0][0].length;
                            int shapeHeight = shape[0].length;
                            int shapeDepth = shape.length;

                            int shapeX = 0;
                            int shapeY = 0;
                            int shapeZ = 0;

                            //Get shape inside container
                            Integer[][][] shapeInContainer = new Integer[depth][height][width];

                            //Place shape in container
                            for(int zContainer=0; zContainer < depth; zContainer++){
                                shapeY = 0;
                                System.out.println("zContainer:" + zContainer);

                                //If this is the layer that the shape needs to be placed in
                                if(z>= zContainer && z<(zContainer+shape.length)){
                                    shapeX = 0;

                                    for(int yContainer=0; yContainer < height; yContainer++) {
                                        System.out.println("yContainer:" + yContainer);

                                        //If this is the height that the shape needs to be placed on
                                        if(y>= yContainer && y<(yContainer+shape[0].length)){
                                            for (int xContainer = 0; xContainer < width; xContainer++) {
                                                System.out.println("xContainer:" + xContainer);
                                                //If this is the x that the shape needs to be placed on
                                                if(x>= xContainer && x<(xContainer+shape[0][0].length)){
                                                    System.out.println("Z shape:" + shapeZ);
                                                    System.out.println("Y shape:" + shapeY);
                                                    System.out.println("X shape:" + shapeX);
                                                    if(shape[shapeZ][shapeY][shapeX] == 1){
                                                        shapeInContainer[zContainer][yContainer][xContainer] = 1;
                                                    } else {
                                                        shapeInContainer[zContainer][yContainer][xContainer] = 0;
                                                    }

                                                    ++shapeX;

                                                } else {
                                                    shapeInContainer[zContainer][yContainer][xContainer] = 0;
                                                }
                                            }
                                        } else {
                                            for(int xContainer = 0; x < xContainer; x++){
                                                shapeInContainer[zContainer][yContainer][xContainer] = 0;
                                            }
                                        }
                                    }

                                    ++shapeY;
                                } else {
                                    for(int yContainer = 0; yContainer < height; yContainer++){
                                        for(int xContainer = 0; x < width; x++){
                                            shapeInContainer[zContainer][yContainer][xContainer] = 0;
                                        }
                                    }

                                }

                                ++shapeZ;
                            }

                            //Save it
                            placements.add(shapeInContainer);
                        }
                    }
                }
            }
        }
    }

    public boolean fits(int startX, int startY, int startZ, int[][][] shape){
        int shapeWidth = shape[0][0].length;
        int shapeHeight = shape[0].length;
        int shapeDepth = shape.length;

        //check X
        if(startX+shapeWidth > width){
            return false;
        }

        //check Y
        if(startY+shapeHeight > height){
            return false;
        }

        //check z
        if(startZ+shapeDepth > depth){
            return false;
        }

        return true;
    }

}