public class Ship {

    private Coordinate[] fullLocation;
    private Coordinate startingLocation;
    private String direction;
    private int length;
    private boolean isSunk;

    private Ship(Builder builder){
        this.startingLocation = builder.startingLocation;
        this.direction = builder.direction;
        this.length = builder.length;

        setFullLocation();
    }

    private void setFullLocation(){

    }

    public static class Builder{
        private Coordinate startingLocation;
        private String direction;
        private int length;

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

        public Ship Build(){
            return new Ship(this);
        }

    }


}
