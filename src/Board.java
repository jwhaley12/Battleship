import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Board {

    private final String[][] board;
    private final ArrayList<Ship> shipLocations;
    private final int boardHeight;
    private final int boardWidth;
    private final HashMap<Coordinate, String> coordinatesUsed;
    private int totalShips;

    public Board(){
        System.out.println("Creating Board...");
        this.shipLocations = new ArrayList<>();
        this.coordinatesUsed = new HashMap<>();
        this.totalShips = 0;
        this.board = new String[][]{
                {"~", "~", "~", "~", "~", "~", "~"},
                {"~", "~", "~", "~", "~", "~", "~"},
                {"~", "~", "~", "~", "~", "~", "~"},
                {"~", "~", "~", "~", "~", "~", "~"},
                {"~", "~", "~", "~", "~", "~", "~"},
                {"~", "~", "~", "~", "~", "~", "~"},
                {"~", "~", "~", "~", "~", "~", "~"}

        };

        this.boardHeight = this.board.length;
        this.boardWidth = this.board[0].length;


    }

    public void placeShip(){
        Ship ship;
        Coordinate startPosition = getRandomCoordinate();
        String[] directions = getScrambledDirections();
        int length = (int)(Math.random() * 3 + 2); //2-4
        System.out.println("Attempting to place ship of length: " + length + " Start Position: {" + startPosition.getY() + ", " + startPosition.getX() + "} Ship Number: " + (this.totalShips + 1));

        boolean isValidPosition = false;
        int attemptCounter = 0;
        int i = 0;
        int x = startPosition.getX();
        int y = startPosition.getY();
        String currentDirection = null;

        Ship.Builder builder = new Ship.Builder()
                .startingLocation(startPosition)
                .length(length);



        while(!isValidPosition && i < directions.length){
            final int finalY = y;
            final int finalX = x;

            currentDirection = directions[i];
            if(currentDirection.equals("UP") && y - (length - 1) >= 0){

                isValidPosition = isShipOverlapping(length, (Integer n) -> finalY - n,
                                                                    (Integer n) -> finalX);
            }
            else if(currentDirection.equals("RIGHT") && x + (length - 1) < this.boardWidth){
                isValidPosition = isShipOverlapping(length, (Integer n) -> finalY,
                                                                    (Integer n) -> finalX + n);
            }
            else if(currentDirection.equals("DOWN") && y + (length - 1) < this.boardHeight){
                isValidPosition = isShipOverlapping(length, (Integer n) -> finalY + n,
                                                                    (Integer n) -> finalX);
            }
            else if(currentDirection.equals("LEFT") && x - (length - 1) > 0){
                isValidPosition = isShipOverlapping(length, (Integer n) -> finalY,
                                                                    (Integer n) -> finalX - n);
            }


            if(isValidPosition){
                ship = builder.direction(currentDirection).Build();
                addShipLocation(ship);
                System.out.println("Successfully placed ship! Direction: " +currentDirection);
            }
            attemptCounter += 1;
            i++;
            //If position does not work, try a different position
            if(i == 4 && !isValidPosition){
                System.out.println("Couldn't Get valid position from i 0-3. Retrying...");
                startPosition = getRandomCoordinate();
                x = startPosition.getX();
                y = startPosition.getY();
                i = 0;
                System.out.println("Attempting to place ship of length: " + length + " Start Position: {" + startPosition.getY() + ", " + startPosition.getX() + "}");
            }
            //Attempts at trying to find a location that works.
            if(attemptCounter > 20 && !isValidPosition){
                System.out.println("Could not find a position within the amount of time given.");
                throw new RuntimeException();
            }
        }
    }

    private boolean isShipOverlapping(int length, Function<Integer, Integer> calculateY, Function<Integer, Integer> calculateX){
        Coordinate[] usedCords = new Coordinate[length];

        for(int j = 0; j < length; j++){
            Coordinate coordinate = new Coordinate(calculateY.apply(j), calculateX.apply(j));
            if(this.coordinatesUsed.get(coordinate) != null){
                return false;
            }
            usedCords[j] = coordinate;
        }

        updateBoard("PlaceShip",usedCords);

        return true;
    }

    private void updateBoard(String updateType, Coordinate[] coordinates){
        if(updateType.equals("PlaceShip")) {
            this.totalShips += 1;
            for (int i = 0; i < coordinates.length; i++) {
                Coordinate currCoordinate = coordinates[i];
                this.coordinatesUsed.put(currCoordinate, "TRUE");
                this.board[currCoordinate.getY()][currCoordinate.getX()] = "" + totalShips;
            }
        }
    }

    public void getBoard(){
        for(int i = 0; i < this.boardHeight; i++){
            System.out.println(Arrays.toString(this.board[i]));
        }
    }

    public void checkGuess(String guess){
        if(guess.length() != 3 || !guess.contains(",")){
            System.out.println("Invalid guess. Please Try again");
            return;
        }

        String[] guesses = guess.split(",");
        int x = Integer.parseInt(guesses[1]);
        int y = Integer.parseInt(guesses[0]);
    }

    private String[] getScrambledDirections(){
        String[] directions = {"UP","RIGHT","LEFT","DOWN"};
        Random random = new Random();

        for(int i = 0; i < directions.length; i++){
            String temp = directions[i];
            int swapPosition = random.nextInt(directions.length);
            directions[i] = directions[swapPosition];
            directions[swapPosition] = temp;

        }

        return directions;
    }

    private Coordinate getRandomCoordinate(){
        int x1 = (int)(Math.random() * 7.0 ); //0-6
        int y1 = (int)(Math.random() * 7.0 ); //0-6
        return new Coordinate(y1, x1);
    }
    private void addShipLocation(Ship ship){
        this.shipLocations.add(ship);
    }

}
