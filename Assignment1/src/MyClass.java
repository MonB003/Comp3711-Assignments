/**
 * MyClass is a class to handle the assignment 1 functionality of evaluating the next location of an action.
 */
public class MyClass {
    // Size stores how many rows and columns the environment has. For this assignment, there are 2 rows and 2 columns.
    private static final int environmentSize = 2;
    // Model based agent stores internal state of the environment
    private static final boolean[][] agentEnvironment = new boolean[environmentSize][environmentSize];
    // Stores the index positions of each letter
    private static final char[][] letterIndices = {{'A', 'B'},{'C', 'D'}};

    /**
     * Stores the command line arguments in the agentEnvironment array.
     * @param A_status: Boolean value of square A's status
     * @param B_status: Boolean value of square B's status
     * @param C_status: Boolean value of square C's status
     * @param D_status: Boolean value of square D's status
     */
    public static void setupEnvironment(boolean A_status, boolean B_status, boolean C_status, boolean D_status) {
        agentEnvironment[0][0] = A_status;
        agentEnvironment[0][1] = B_status;
        agentEnvironment[1][0] = C_status;
        agentEnvironment[1][1] = D_status;
    }

    /**
     * Gets the indices of a character.
     * @param current_location: Character to find the index of.
     * @return Integer array of the character's location indices.
     */
    public static int[] getLocationIndex(char current_location) {
        int[] locationIndex = {0, 0};
        for (int rowIndex = 0; rowIndex < letterIndices.length; ++rowIndex) {
            for (int colIndex = 0; colIndex < letterIndices[rowIndex].length; ++colIndex) {
                char currentLetter = letterIndices[rowIndex][colIndex];
                if (current_location == currentLetter) {
                    // Store the location's row and column indices
                    locationIndex = new int[]{rowIndex, colIndex};
                }
            }
        }
        return locationIndex;
    }

    /**
     * Gets the character at an index.
     * @param rowIndex: Integer value of the array's row index.
     * @param colIndex: Integer value of the array's column index.
     * @return Character at the index.
     */
    public static char getLocationChar(int rowIndex, int colIndex) {
        return letterIndices[rowIndex][colIndex];
    }

    /**
     * Gets the next square horizontal to the current location's square.
     * @param rowIndex: Integer value of the array's row index.
     * @param colIndex: Integer value of the array's column index.
     * @return Integer array of the next location's indices.
     */
    public static int[] getNextHorizontalSquare(int rowIndex, int colIndex) {
        // Move left or right to the next column, use modulus to wrap around for index values
        return new int[]{rowIndex, (colIndex + 1) % environmentSize};
    }

    /**
     * Gets the next square vertical to the current location's square.
     * @param rowIndex: Integer value of the array's row index.
     * @param colIndex: Integer value of the array's column index.
     * @return Integer array of the next location's indices.
     */
    public static int[] getNextVerticalSquare(int rowIndex, int colIndex) {
        // Move up or down to the next row, use modulus to wrap around for index values
        return new int[]{(rowIndex + 1) % environmentSize, colIndex};
    }

    /**
     * Gets the next square diagonal to the current location's square.
     * @param rowIndex: Integer value of the array's row index.
     * @param colIndex: Integer value of the array's column index.
     * @return Integer array of the next location's indices.
     */
    public static int[] getNextDiagonalSquare(int rowIndex, int colIndex) {
        return new int[]{(rowIndex + 1) % environmentSize, (colIndex + 1) % environmentSize};
    }

    /**
     * Main method that runs the program to get the next location of an action.
     * @param args: Command line arguments passed to the program.
     */
    public static void main(String[] args) {
        // Check for invalid argument count
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

        // Store each location's status
        setupEnvironment(A_status, B_status, C_status, D_status);

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
        int[] locationIndex = getLocationIndex(current_location);
        boolean currentLocationStatus = agentEnvironment[locationIndex[0]][locationIndex[1]];

        // If the current location is not clean, the vacuum cleaner stays in its current location to clean it up.
        if (!currentLocationStatus) {
            System.out.println("\nAction - Next Location = " + nextLocation);
            return;
        }

        // Check next horizontal position. Horizontal moves have the highest priority over vertical moves
        int[] nextLocationIndex = getNextHorizontalSquare(locationIndex[0], locationIndex[1]);
        boolean nextLocationStatus = agentEnvironment[nextLocationIndex[0]][nextLocationIndex[1]];
        if (!nextLocationStatus) {
            nextLocation = getLocationChar(nextLocationIndex[0], nextLocationIndex[1]);
            System.out.println("\nAction - Next Location = " + nextLocation);
            return;
        }

        // If next horizontal position is already clean, check next vertical position
        nextLocationIndex = getNextVerticalSquare(locationIndex[0], locationIndex[1]);
        nextLocationStatus = agentEnvironment[nextLocationIndex[0]][nextLocationIndex[1]];
        if (!nextLocationStatus) {
            nextLocation = getLocationChar(nextLocationIndex[0], nextLocationIndex[1]);
            System.out.println("\nAction - Next Location = " + nextLocation);
            return;
        }

        // If next vertical position is already clean, check next diagonal position
        int[] diagonalLocationIndex = getNextDiagonalSquare(locationIndex[0], locationIndex[1]);
        boolean diagonalLocationStatus = agentEnvironment[diagonalLocationIndex[0]][diagonalLocationIndex[1]];
        if (!diagonalLocationStatus) {
            // If diagonal is not clean, move vertically because vacuum cannot move diagonally
            nextLocation = getLocationChar(nextLocationIndex[0], nextLocationIndex[1]);
        }
        System.out.println("\nAction - Next Location = " + nextLocation);
    }
}
