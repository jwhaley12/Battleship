import java.util.HashMap;
import java.util.Scanner;

public class Game {
    private int triesLeft;
    private Board board;
    private Scanner scanner;

    public Game(){
        this.board = new Board();
        this.scanner = new Scanner(System.in);
        this.triesLeft = 5;
    }
    public void start(){
        board.placeShip();
        board.placeShip();
        board.placeShip();
        board.placeShip();
        board.getBoard();

        String guess = "";

        while(true){
            System.out.println("Please enter your guess (y,x): ");
            guess = scanner.nextLine();
            if(guess.equals("Quit")){
                break;
            }
            String result = board.checkGuess(guess);

            switch (result) {
                case "Hit" -> {
                    this.triesLeft = 5;
                    System.out.println("Hit! Tries left: " + this.triesLeft);
                }
                case "Miss" -> {
                    this.triesLeft -= 1;
                    System.out.println("Miss! Tries Left: " + this.triesLeft);
                }
                case "Picked" -> System.out.println("Guess already made. Try again.");
                default -> System.out.println("Invalid Guess. Try again.");
            }

            if(triesLeft == 0){
                int remainingShips = board.getTotalShips();
                System.out.println("You lose! Total remaining ships: " + remainingShips);
                break;
            }

            if(board.getTotalShips() == 0){
                System.out.println("You win!");
                break;
            }
        }





    }
}
