/**
 * MyClass is a class to handle the assignment 1 functionality of evaluating the next location of an action.
 */
public class MyClass {
    /**
     * Gets the index of a character.
     * @param current_location: Character to find the index of.
     * @return Integer value of the character's location index.
     */
    public static int getLocationIndex(char current_location) {
        int locationIndex = 0;
        char[] letterIndices = {'A', 'B', 'C', 'D'};
        // Start at 1 because boolean arguments start at 1
        for (int index = 1; index <= letterIndices.length; ++index) {
            char currentLetter = letterIndices[index-1];
            if (current_location == currentLetter) {
                // Set index
                locationIndex = index;
            }
        }
        return locationIndex;
    }

    /**
     * Gets the character at an index.
     * @param locationIndex: Integer value of the array index.
     * @return Character at the index.
     */
    public static char getLocationChar(int locationIndex) {
        char[] letterIndices = {'A', 'B', 'C', 'D'};
        // Array indices start at 0, so subtract 1 from the index value
        return letterIndices[locationIndex-1];
    }

    /**
     * Gets the next square horizontal to the current location's square.
     * @param locationIndex: Integer value of the array index.
     * @return Integer value of the next location's index.
     */
    public static int getNextHorizontalSquare(int locationIndex) {
        // If the number is even, move left. If it's odd, move right.
        return (locationIndex % 2 == 0) ? (locationIndex - 1) : (locationIndex + 1);
    }

    /**
     * Gets the next square vertical to the current location's square.
     * @param locationIndex: Integer value of the array index.
     * @return Integer value of the next location's index.
     */
    public static int getNextVerticalSquare(int locationIndex) {
        // Row size is how many squares to move past before getting the next location index. For this assignment, there are 2 squares per row
        int rowSize = 2;
        // Table size is how many squares are in the table. For this assignment, there are 4 squares
        int tableSize = rowSize * rowSize;
        // If adding the row size this index is an index larger than the table, move up. If not, move down.
        return (locationIndex + rowSize) > tableSize ? (locationIndex - rowSize) : (locationIndex + rowSize);
    }

    /**
     * Gets the next square diagonal to the current location's square.
     * @param locationIndex: Integer value of the array index.
     * @return Integer value of the next location's index.
     */
    public static int getNextDiagonalSquare(int locationIndex) {
        switch (locationIndex) {
            case 1:
                return 4;
            case 2:
                return 3;
            case 3:
                return 2;
            case 4:
                return 1;
            default:
                return 0; // If error occurs
        }
    }

    /**
     * Main method that runs the program to get the next location of an action.
     * @param args: Command line arguments passed to the program.
     */
    public static void main(String[] args) {
        // Check arguments
        if (args.length != 5) {
            System.out.println("Invalid number of command line arguments passed: " + args.length);
            return;
        }

        char current_location = args[0].charAt(0);
        // state: true -> clean, false -> not clean
        boolean A_status = Boolean.parseBoolean(args[1]);
        boolean B_status = Boolean.parseBoolean(args[2]);
        boolean C_status = Boolean.parseBoolean(args[3]);
        boolean D_status = Boolean.parseBoolean(args[4]);
        System.out.println("Current Location = " + current_location + "\n" +
                "Square A status = " + A_status + "\n" +
                "Square B status = " + B_status + "\n" +
                "Square C status = " + C_status + "\n" +
                "Square D status = " + D_status + "\n");

        // Assignment 1 function
        // Store next location, set a default value
        char nextLocation = current_location;

        // If all squares are clean, the vacuum cleaner stays in its current location.
        boolean allSquaresClean = A_status && B_status && C_status && D_status;
        if (allSquaresClean) {
            System.out.println("\nAction - Next Location = " + nextLocation);
            return;
        }

        // Get current location info
        int locationIndex = getLocationIndex(current_location);
        boolean currentLocationStatus = Boolean.parseBoolean(args[locationIndex]);

        // If the current location is not clean, the vacuum cleaner stays in its current location to clean it up.
        if (!currentLocationStatus) {
            System.out.println("\nAction - Next Location = " + nextLocation);
            return;
        }

        // Check next horizontal position. Horizontal moves have the highest priority over vertical moves
        int nextLocationIndex = getNextHorizontalSquare(locationIndex);
        boolean nextLocationStatus = Boolean.parseBoolean(args[nextLocationIndex]);
        if (!nextLocationStatus) {
            nextLocation = getLocationChar(nextLocationIndex);
            System.out.println("\nAction - Next Location = " + nextLocation);
            return;
        }

        // If next horizontal position is already clean, check next vertical position
        nextLocationIndex = getNextVerticalSquare(locationIndex);
        nextLocationStatus = Boolean.parseBoolean(args[nextLocationIndex]);
        if (!nextLocationStatus) {
            nextLocation = getLocationChar(nextLocationIndex);
            System.out.println("\nAction - Next Location = " + nextLocation);
            return;
        }

        // If next vertical position is already clean, check next diagonal position
        int diagonalLocationIndex = getNextDiagonalSquare(locationIndex);
        boolean diagonalLocationStatus = Boolean.parseBoolean(args[diagonalLocationIndex]);
        if (!diagonalLocationStatus) {
            // If diagonal is not clean, move vertically because vacuum cannot move diagonally
            nextLocation = getLocationChar(nextLocationIndex);
        }
        System.out.println("\nAction - Next Location = " + nextLocation);
    }
}
