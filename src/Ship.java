import java.util.ArrayList;
import java.util.Arrays;

public class Ship {

    private ArrayList<Coordinate> fullLocation;
    private Coordinate startingLocation;
    private String direction;
    private int length;
    private boolean isSunk;
    private int id;

    private Ship(Builder builder){
        this.startingLocation = builder.startingLocation;
        this.direction = builder.direction;
        this.length = builder.length;
        this.id = builder.id;
        this.fullLocation = builder.fullLocation;

    }

    public int getID(){ return this.id;}

    public void hit(Coordinate coordinate){

        fullLocation.remove(coordinate);

        if(fullLocation.isEmpty()){
            this.isSunk = true;
        }
    }

    public boolean isSunk(){ return this.isSunk; }



    public static class Builder{
        private Coordinate startingLocation;
        private String direction;
        private int length;
        private int id;
        private ArrayList<Coordinate> fullLocation;

        public Builder startingLocation(Coordinate startingLocation){
            this.startingLocation = startingLocation;
            return this;
        }

        public Builder direction(String direction){
            this.direction = direction;
            return this;
        }

        public Builder length(int length){
            this.length = length;
            return this;
        }

        public Builder fullLocation(Coordinate[] coordinates){
            this.fullLocation = new ArrayList<>();
            this.fullLocation.addAll(Arrays.asList(coordinates));
            return this;
        }

        public Builder id(int id){
            this.id = id;
            return this;
        }

        public Ship Build(){
            return new Ship(this);
        }

    }


}
