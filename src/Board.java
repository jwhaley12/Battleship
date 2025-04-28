import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Board {

    private final String[][] board;
    private final HashMap<Integer, Ship> ships;
    private final int boardHeight;
    private final int boardWidth;
    private final HashMap<Coordinate, Integer> coordinatesUsed;
    private int totalShips;
    private HashMap<Coordinate, String> guessesMade;

    public Board(){
        System.out.println("Creating Board...");
        this.ships = new HashMap<>();
        this.coordinatesUsed = new HashMap<>();
        this.guessesMade = new HashMap<>();
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

        Coordinate[] shipCoordinates = null;
        int attemptCounter = 0;
        int i = 0;
        int x = startPosition.getX();
        int y = startPosition.getY();
        String currentDirection = null;

        Ship.Builder builder = new Ship.Builder()
                .startingLocation(startPosition)
                .length(length);



        while(shipCoordinates == null && i < directions.length){
            final int finalY = y;
            final int finalX = x;

            currentDirection = directions[i];
            if(currentDirection.equals("UP") && y - (length - 1) >= 0){

                shipCoordinates = isShipOverlapping(length, (Integer n) -> finalY - n,
                                                                    (Integer n) -> finalX);
            }
            else if(currentDirection.equals("RIGHT") && x + (length - 1) < this.boardWidth){
                shipCoordinates = isShipOverlapping(length, (Integer n) -> finalY,
                                                                    (Integer n) -> finalX + n);
            }
            else if(currentDirection.equals("DOWN") && y + (length - 1) < this.boardHeight){
                shipCoordinates = isShipOverlapping(length, (Integer n) -> finalY + n,
                                                                    (Integer n) -> finalX);
            }
            else if(currentDirection.equals("LEFT") && x - (length - 1) > 0){
                shipCoordinates = isShipOverlapping(length, (Integer n) -> finalY,
                                                                    (Integer n) -> finalX - n);
            }


            if(shipCoordinates != null){
                updateBoardWithShip(shipCoordinates);
                ship = builder.direction(currentDirection).fullLocation(shipCoordinates).id(this.totalShips).Build();
                addShipLocation(ship);
                System.out.println("Successfully placed ship! Direction: " +currentDirection);
            }
            attemptCounter += 1;
            i++;
            //If position does not work, try a different position
            if(i == 4 && shipCoordinates == null){
                System.out.println("Couldn't Get valid position from i 0-3. Retrying...");
                startPosition = getRandomCoordinate();
                x = startPosition.getX();
                y = startPosition.getY();
                i = 0;
                System.out.println("Attempting to place ship of length: " + length + " Start Position: {" + startPosition.getY() + ", " + startPosition.getX() + "}");
            }
            //Attempts at trying to find a location that works.
            if(attemptCounter > 20 && shipCoordinates == null){
                System.out.println("Could not find a position within the amount of time given.");
                throw new RuntimeException();
            }
        }
    }

    private Coordinate[] isShipOverlapping(int length, Function<Integer, Integer> calculateY, Function<Integer, Integer> calculateX){
        Coordinate[] usedCords = new Coordinate[length];

        for(int j = 0; j < length; j++){
            Coordinate coordinate = new Coordinate(calculateY.apply(j), calculateX.apply(j));
            if(this.coordinatesUsed.get(coordinate) != null){
                return null;
            }
            usedCords[j] = coordinate;
        }

        return usedCords;
    }

    private void updateBoardWithShip(Coordinate[] coordinates){

        this.totalShips += 1;
        for (int i = 0; i < coordinates.length; i++) {
            Coordinate currCoordinate = coordinates[i];
            this.coordinatesUsed.put(currCoordinate, totalShips);
            this.board[currCoordinate.getY()][currCoordinate.getX()] = "" + totalShips;
        }

    }

    private void updateBoardWithHitOrMiss(String updateType, Coordinate coordinate){
        if(updateType.equals("Miss")){
            int y = coordinate.getY();
            int x = coordinate.getX();
            this.board[y][x] = "O";
        }
        else if(updateType.equals("Hit")) {
            int y = coordinate.getY();
            int x = coordinate.getX();
            this.board[y][x] = "X";
        }
        else{
            System.out.println("Invalid updateType");
            return;
        }

        this.getBoard();
    }

    public void getBoard(){
        for(int i = 0; i < this.boardHeight; i++){
            System.out.println(Arrays.toString(this.board[i]));
        }
    }

    public String checkGuess(String guess){
        if(guess.length() != 3 || !guess.contains(",")){
            System.out.println("Invalid guess. Please Try again");
            return "Invalid";
        }

        String[] guesses = guess.split(",");
        int x = Integer.parseInt(guesses[1]);
        int y = Integer.parseInt(guesses[0]);

        Coordinate coordinateGuess = new Coordinate(y, x);
        if(guessesMade.get(coordinateGuess) != null){
            System.out.println("Guess already made");
            return "Picked";
        }

        guessesMade.put(coordinateGuess, "t");

        Integer shipID = this.coordinatesUsed.get(coordinateGuess);
        System.out.println(shipID);
        if(shipID == null){
            updateBoardWithHitOrMiss("Miss", coordinateGuess);
            return "Miss";
        }

        Ship ship = this.ships.get(shipID);
        ship.hit(coordinateGuess);
        updateBoardWithHitOrMiss("Hit", coordinateGuess);

        if(ship.isSunk()){
            System.out.println("You sunk ship ID: " + ship.getID());
            this.totalShips -= 1;
            System.out.println("Remaining ships: " + this.totalShips);
        }

        return "Hit";

    }

    public int getTotalShips(){ return this.totalShips;}

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
        this.ships.put(ship.getID(), ship);
    }

}
