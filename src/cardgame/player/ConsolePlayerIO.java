package cardgame.player;

import java.util.Scanner;

class ConsolePlayerIO extends PlayerIO
{
    public ConsolePlayerIO()
    {
    }
    
    public void sendMessage(String message)
    {
        System.out.println(message);
    }
    
    // lowerBound inclusive, upperBound exclusive
    public int chooseInt(int lowerBound, int upperBound)
    {
        Scanner scanner      = new Scanner(System.in);
        String  invalidInput = "Invalid input, please try again";
        boolean validInput;
        int     input;
        
        do {
            while (!scanner.hasNextInt()) {
                sendMessage(invalidInput);
                scanner.next();
            }
            input = scanner.nextInt();
            
            validInput = (input >= lowerBound) && (input < upperBound);
            if (!validInput)
                sendMessage(invalidInput);
            
        } while (!validInput);
        
        scanner.close();
        
        return input;
    }
}