import java.util.ArrayList;
import java.util.Random;

public class Main {
    // Stores the order of provinces and territories
    public static String[] regionsOrder = {"BC", "AB", "SK", "MB", "ON", "QC", "NB", "NS", "PEI", "NL", "NU", "NT", "YT"};
    // Stores all colour options for the map
    public static char[] allColourOptions = {'r', 'b', 'o', 'j'};
    // Stores k colour options for the map based on the k value passed to the program
    public static ArrayList<Character> allColours = new ArrayList<>();

    /**
     * Checks if a state matrix is a goal state.
     * @param currentState: Char array of the current state.
     * @param adjacencyMatrix: Integer array of the adjacency matrix.
     * @return Boolean value of whether this current state is a goal state or not.
     */
    public static boolean isGoalState(char[] currentState, int[][] adjacencyMatrix) {
        // If there are no adjacent regions that share the same colour, this is a goal state
        return calculateHeuristic(currentState, adjacencyMatrix) == 0;
    }

    /**
     * Heuristic function returns the number of adjacent regions that share the same colour.
     * @param currentState: Char array of the current state.
     * @param adjacencyMatrix: Integer array of the adjacency matrix.
     * @return Integer value of the number of adjacent regions that share the same colour.
     */
    public static int calculateHeuristic(char[] currentState, int[][] adjacencyMatrix) {
        int adjacentRegionsCount = 0;
        // Loop through all rows in matrix
        for (int row = 0; row < currentState.length; row++) {
            // Loop through all columns, excluding the first row to avoid double counting values
            for (int column = row + 1; column < currentState.length; column++) {
                // Check if the regions are adjacent, and if these regions have the same colour
                if (adjacencyMatrix[row][column] == 1 && currentState[row] == currentState[column]) {
                    adjacentRegionsCount++;
                }
            }
        }
        return adjacentRegionsCount;
    }

    /**
     * Calculates the cost of the colour units in each region.
     * @param currentState: Char array of the current state.
     * @return Integer value of the total cost for all regions.
     */
    public static int calculateCost(char[] currentState) {
        int totalCost = 0;
        for (char currentColour : currentState) {
            totalCost += getColourCost(currentColour);
        }
        return totalCost;
    }

    /**
     * Gets the cost of a specific colour.
     * @param colour: Character value to get the cost of.
     * @return Integer value of the character's cost.
     */
    public static int getColourCost(char colour) {
        switch (colour) {
            case 'r':
                return 2;
            case 'b':
                return 1;
            case 'o':
                return 3;
            case 'j':
                return 5;
            default:
                return 0;
        }
    }

    /**
     * Successor function changes the colour of a single region and generates a new solution (successor state).
     * @param currentState: Char array of the current state.
     * @return Char array of the successor state.
     */
    public static char[] performSuccessorFunction(char[] currentState) {
        // Create Random object for generating random numbers
        Random randomNumber = new Random();
        // Successor state is a new array to store the currentState with a colour change
        char[] successorState = currentState.clone();
        // Randomly select a single region's index to change the colour of
        int regionIndex = randomNumber.nextInt(currentState.length);

        // Change the region's colour to a colour that's different from its current one
        char currentStateColour = currentState[regionIndex];
        int colourIndex = randomNumber.nextInt(allColours.size());
        char newColour = allColours.get(colourIndex);
        while (newColour == currentStateColour) {
            colourIndex = randomNumber.nextInt(allColours.size());
            newColour = allColours.get(colourIndex);
        }

        // Set the new colour at the region's index
        successorState[regionIndex] = newColour;
        return successorState;
    }

    /**
     * Prints out a state in order.
     * @param state: Char array of the state.
     */
    public static void printStateArray(char[] state) {
        for (char currentColour: state) {
            System.out.print(currentColour + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // Check for invalid number of command line arguments passed
        if (args.length != 1) {
            System.out.println("Error: Invalid number of arguments passed: " + args.length);
            return;
        }

        // Accept the k value (k is the number of colours) as a command line argument
        int k = Integer.parseInt(args[0]);
        System.out.println("K = " + k);
        if (k < 3 || k > 4) {
            System.out.println("No solution when k = " + k);
            return;
        }

        // Store k colours in an ArrayList
        for (int index = 0; index < k; index++) {
            allColours.add(allColourOptions[index]);
        }

        // Adjacency matrix A, where the value is 1 when the region at indices i and j are adjacent.
        int[][] adjacencyMatrix = {
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
                {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0},
                {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1}
        };

        // Array for initial state, S0
        char[] currentState = {'b', 'o', 'o', 'o', 'r', 'r', 'j', 'j', 'j', 'j', 'j', 'j', 'j'};
        System.out.print("Initial state: ");
        printStateArray(currentState);

        System.out.println("IS GOAL STATE: " + isGoalState(currentState, adjacencyMatrix));

        // Core of the hill-climbing search

        // Check if the current state is a goal state
        while (!isGoalState(currentState, adjacencyMatrix)) {
            // Calculate the successor
            char[] successorState = performSuccessorFunction(currentState);

            // Calculate the heuristic
            int currentHeuristic = calculateHeuristic(currentState, adjacencyMatrix);
            int successorHeuristic = calculateHeuristic(successorState, adjacencyMatrix);

            // If the successor state has a lower heuristic value, move to successor
            if (successorHeuristic < currentHeuristic) {
                currentState = successorState;
                System.out.println("Moving to successor state with heuristic: " + successorHeuristic);
            } else {
                // There's no improvement, stop search
                System.out.println("STOPPED SEARCH");
                break;
            }
        }

        // Print out the solution in the correct order and also the cost
        System.out.print("Solution state: ");
        printStateArray(currentState);
        System.out.println("Cost: " + calculateCost(currentState));

        // Example of a goal state
        char[] testGoalState = {'r', 'b', 'o', 'j', 'r', 'b', 'o', 'j', 'r', 'o', 'b', 'j', 'o'};
        if (isGoalState(testGoalState, adjacencyMatrix)) {
            System.out.println("Goal state");
        } else {
            System.out.println("Not goal state");
        }

    }
}