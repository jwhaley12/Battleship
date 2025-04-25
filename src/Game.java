import java.util.HashMap;
import java.util.Scanner;

public class Game {

    public Game(){

    }
    public void start(){
        Board board = new Board();
        board.placeShip();
        board.placeShip();
        board.placeShip();
        board.placeShip();
        board.getBoard();

        Scanner scanner = new Scanner(System.in);
        String guess = "";

        while(true){
            System.out.println("Please enter your guess (y,x): ");
            guess = scanner.nextLine();
            if(guess.equals("Quit")){
                break;
            }
            board.checkGuess(guess);
            if(board.getTotalShips() == 0){
                break;
            }
        }





    }
}
