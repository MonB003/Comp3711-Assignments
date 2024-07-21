public class Main {
    // Stores the order of provinces and territories
    public static String[] regionsOrder = {"BC", "AB", "SK", "MB", "ON", "QC", "NB", "NS", "PEI", "NL", "NU", "NT", "YT"};

    /**
     * Checks if a state matrix is a goal state.
     * @param currentState: Char array of the current state.
     * @param adjacencyMatrix: Integer array of the adjacency matrix.
     * @return Boolean value of whether this current state is a goal state or not.
     */
    public static boolean isGoalState(char[] currentState, int[][] adjacencyMatrix) {
        for (int row = 0; row < currentState.length; row++) {
            for (int column = 0; column < currentState.length; column++) {
                // Check if the regions are adjacent, and if these regions have the same colour
                if (adjacencyMatrix[row][column] == 1 && currentState[row] == currentState[column]) {
                    // This is not a goal state
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Heuristic function returns the number of adjacent regions that share the same colour.
     * @param currentState: Char array of the current state.
     * @param adjacencyMatrix: Integer array of the adjacency matrix.
     * @return Integer value of the number of adjacent regions that share the same colour.
     */
    public static int calculateHeuristic(char[] currentState, int[][] adjacencyMatrix) {
        int adjacentRegionsCount = 0;
        for (int row = 0; row < currentState.length; row++) {
            for (int column = 0; column < currentState.length; column++) {
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

    public static char[] performSuccessorFunction(char[] currentState) {
        return new char[]{};
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
        if (!isGoalState(currentState, adjacencyMatrix)) {
            System.out.println("Not a goal state");

            currentState = performSuccessorFunction(currentState);
            int currentCost = calculateCost(currentState);
        }
    }
}