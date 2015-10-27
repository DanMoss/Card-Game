package cardgame.player;

import java.util.Scanner;

/**
 * A console player input/output system.
 * 
 * @see PlayerIO
 */
class ConsolePlayerIO extends PlayerIO
{
    /**
     * Sole constructor.
     */
    public ConsolePlayerIO() {}
    
    /* (non-Javadoc)
     * @see cardgame.player.PlayerIO#sendMessage(java.lang.String)
     */
    public void sendMessage(String message)
    {
        System.out.println(message);
    }
    
    /** 
     * Accepts an integer input from the {@code ConsolePlayerIO}.
     * <p>
     * Opens a scanner for the console and reads user input. When an integer is
     * entered, it is checked to see if is in the required range. Closes the
     * scanner once a valid integer is found.
     * 
     * @see PlayerIO#chooseInt(int, int)
     * @see java.util.Scanner
     */
    public int chooseInt(int lowerBound, int upperBound)
    {
        Scanner scanner    = new Scanner(System.in);
        String  notAnInt   = "That is not an integer!";
        String  notInRange = "That integer is not in the specified range!";
        boolean validInput;
        int     input;
        
        do {
            // Getting an integer
            while (!scanner.hasNextInt()) {
                sendMessage(notAnInt);
                scanner.next();
            }
            input = scanner.nextInt();
            
            // Checking the integer is in range
            validInput = (input >= lowerBound) && (input < upperBound);
            if (!validInput)
                sendMessage(notInRange);
        } while (!validInput);
        
        scanner.close();
        
        return input;
    }
}
