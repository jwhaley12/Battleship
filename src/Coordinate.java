import java.util.Objects;

public class Coordinate {

    private final int[] location = new int[2];

    public Coordinate(int y, int x){
        this.location[0] = y;
        this.location[1] = x;
    }

    public int[] getCoordinate(){ return location;}
    public void setCoordinate(int x, int y){
        this.location[0] = y;
        this.location[1] = x;
    }

    public int getX(){ return this.location[1]; }
    public int getY(){ return this.location[0]; }

    @Override
    public boolean equals(Object obj){
        if(this == obj){ return true; }

        if(obj.getClass() != this.getClass()){ return false; }

        Coordinate objCord = (Coordinate) obj;
        if(objCord.getX() == this.getX() && objCord.getY() == this.getY()){
            return true;
        }

        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.getX(),this.getY());
    }
}
