import java.util.*;

/**
 * MyClass is a class to handle the assignment 2 functionality of implementing the A* search algorithm for a
 * search space using its cost matrix and heuristic vector.
 */
public class MyClass {
    // Stores the letter(s) corresponding to each index
    public static String[] stateLetters = {"A", "B", "C", "D", "E", "H", "J", "G1", "G2", "G3"};
    // Stores the cheapest paths to each goal state in the format (goal state index, number of cycles)
    private static final HashMap<Integer, Integer> goalStatePathCycles = new HashMap<>();

    /**
     * Runs A* search from a start state to a specific goal state. Uses a cost matrix and heuristic vector
     * to calculate the costs between each state.
     * @param cost_matrix: Array of costs to/from each state.
     * @param heuristic_vector: Array of heuristic costs (h(n) function) for each state.
     * @param startStateIndex: Integer value of the start state's index.
     * @param goalStateIndex: Integer value of the goal state's index.
     * @return Integer array of the path state's indices.
     */
    public static int[] performAStarSearch(int[][] cost_matrix, int[] heuristic_vector, int startStateIndex, int goalStateIndex) {
        // Store node indices to visit and their costs
        PriorityQueue<int[]> openNodes = new PriorityQueue<>(Comparator.comparingInt(node -> node[1]));
        // Store visited nodes
        HashSet<Integer> visitedNodes = new HashSet<>();
        // Store all previously visited states
        HashMap<Integer, Integer> previousStates = new HashMap<>();
        // Store g(n) function results as (state index, path cost) pairs
        HashMap<Integer, Integer> initialPathCosts = new HashMap<>();
        // Store f(n) function results as (state index, estimated cost) pairs
        HashMap<Integer, Integer> estimatedCosts = new HashMap<>();

        // Store start state values
        initialPathCosts.put(startStateIndex, 0);
        estimatedCosts.put(startStateIndex, heuristic_vector[startStateIndex]);
        openNodes.add(new int[]{startStateIndex, estimatedCosts.get(startStateIndex)});

        // Store the number of cycles it took to reach the final goal state
        int numberCycles = 0;

        // Check if there are still open nodes to explore
        while (!openNodes.isEmpty()) {
            numberCycles++;

            // Get node with the lowest estimated cost
            int[] currentNode = openNodes.poll();
            int currentState = currentNode[0];

            // Check if the current state is a goal state
            if (currentState == goalStateIndex) {
                // Store number of cycles to reach this goal state
                goalStatePathCycles.put(currentState, numberCycles);
                return getPathStates(previousStates, currentState);
            }

            // State has been visited, update the visited node list
            visitedNodes.add(currentState);

            // Check neighbour nodes for the next state in the path
            for (int neighbourIndex = 0; neighbourIndex < cost_matrix.length; neighbourIndex++) {
                // Store current path cost (in the current state's column)
                int currentPathCost = cost_matrix[neighbourIndex][currentState];
                if (currentPathCost == 0 || visitedNodes.contains(neighbourIndex)) {
                    // Skip this neighbour node if its value is 0 (there's no direct path) or it's already been visited
                    continue;
                }

                // Store the g(n) path cost from the start state to this current state
                int gPathCost = initialPathCosts.getOrDefault(currentState, Integer.MAX_VALUE) + currentPathCost;
                if (gPathCost < initialPathCosts.getOrDefault(neighbourIndex, Integer.MAX_VALUE)) {
                    previousStates.put(neighbourIndex, currentState);
                    initialPathCosts.put(neighbourIndex, gPathCost);
                    // Calculate heuristic function: f(n) = g(n) + h(n) = path cost + estimated cost
                    int heuristicCost = gPathCost + heuristic_vector[neighbourIndex];
                    estimatedCosts.put(neighbourIndex, heuristicCost);
                    openNodes.add(new int[]{neighbourIndex, heuristicCost});
//                    System.out.println("STATE: " + stateLetters[neighbourIndex] + ", " + gPathCost + " + " + heuristic_vector[neighbourIndex] + " = " + heuristicCost);
                }
            }
        }

        // Return empty path if no path is found
        return new int[0];
    }

    /**
     * Gets all state's indices in the cheapest path.
     * @param previousStates: HashMap of the previously visited states and their costs.
     * @param currentState: Integer value of the current state's index.
     * @return Integer array of the path state's indices.
     */
    private static int[] getPathStates(HashMap<Integer, Integer> previousStates, int currentState) {
        ArrayList<Integer> allPathStates = new ArrayList<>();
        allPathStates.add(currentState);
        // Store all states from the HashMap into an ArrayList
        while (previousStates.containsKey(currentState)) {
            currentState = previousStates.get(currentState);
            allPathStates.add(currentState);
        }
        // Store node states in the reverse order they were stored (so it's in the correct order starting from the initial state)
        int[] path = new int[allPathStates.size()];
        for (int index = 0; index < path.length; index++) {
            path[index] = allPathStates.get(allPathStates.size() - 1 - index);
        }
        return path;
    }

    /**
     * Prints the cheapest path, the goal state, and the number of cycles it took to reach the final goal state.
     * @param cheapestPath: Integer array of the cheapest path state's indices.
     * @param goalStateIndex: Integer value of the goal state's index.
     */
    public static void printCheapestPathStates(int[] cheapestPath, int goalStateIndex) {
        // Print the goal state
        System.out.println("Goal state: " + stateLetters[goalStateIndex]);
        if (cheapestPath.length > 0) {
            int startStateIndex = cheapestPath[0];
            System.out.print("Cheapest path from " + stateLetters[startStateIndex] + " to " + stateLetters[goalStateIndex] + ": ");
            // Print the node states in the path
            for (int currentState: cheapestPath) {
                System.out.print(stateLetters[currentState] + " ");
            }
            // Print the number of cycles
            int numberCycles = goalStatePathCycles.get(goalStateIndex);
            System.out.println("\nNumber of cycles to reach " + stateLetters[goalStateIndex] + ": " + numberCycles + "\n");
        } else {
            // If there's no path
            System.out.println("There was no path found to the goal state: " + stateLetters[goalStateIndex]);
        }
    }

    /**
     * Calculates the cheapest path among all the goal state paths.
     * @param goalStatesList: Integer array of all the goal state's indices.
     */
    public static void calculateOverallCheapestPath(ArrayList<Integer> goalStatesList) {
        // Store the cheapest number of cycles and goal state for a path
        int overallCheapestPath = goalStatePathCycles.get(goalStatesList.getFirst());
        int overallCheapestGoalState = goalStatesList.getFirst();

        // Loop through each goal state
        for (int goalStateIndex: goalStatesList) {
            int currentPathCycle = goalStatePathCycles.get(goalStateIndex);
            // Check if the current cycle to this goal state is the smallest
            if (currentPathCycle < overallCheapestPath) {
                // Store the cycle values
                overallCheapestPath = currentPathCycle;
                overallCheapestGoalState = goalStateIndex;
            }
        }
        // Print cheapest path result
        System.out.println("Overall cheapest path is: " + overallCheapestPath + " for goal state " + stateLetters[overallCheapestGoalState]);
    }

    /**
     * Main method that runs the program, which runs the A * search algorithm on a search space with goal states.
     * For each goal state, it prints the cheapest path, the goal state and the number of cycles. After, it calculates
     * the cheapest path among the goal state's paths.
     * @param args: Command line arguments passed to the program.
     */
    public static void main(String[] args) {
        int[][] cost_matrix = {
                {0,0,0,6,1,0,0,0,0,0},
                {5,0,2,0,0,0,0,0,0,0},
                {9,3,0,0,0,0,0,0,0,0},
                {0,0,1,0,2,0,0,0,0,0},
                {6,0,0,0,0,2,0,0,0,0},
                {0,0,0,7,0,0,0,0,0,0},
                {0,0,0,0,2,0,0,0,0,0},
                {0,9,0,0,0,0,0,0,0,0},
                {0,0,0,5,0,0,0,0,0,0},
                {0,0,0,0,0,8,7,0,0,0}
        };

        int[] heuristic_vector = {5,7,3,4,6,8,5,0,0,0};

        // Identify goal states and save them in a new vector
        ArrayList<Integer> goalStatesList = new ArrayList<>();
        for (int index = 0; index < heuristic_vector.length; index++) {
            // Goal states have a 0 value in the heuristic vector
            if (heuristic_vector[index] == 0) {
                goalStatesList.add(index);
            }
        }
        // Maybe remove array and just use ArrayList?
//        int[] goalStateIndices = new int[goalStatesList.size()];
//        for (int goalIndex = 0; goalIndex < goalStatesList.size(); goalIndex++) {
//           goalStateIndices[goalIndex] = goalStatesList.get(goalIndex);
//        }

        // Write a program to implement the A * search

        // Start state is A
        int startStateIndex = 0;

        // Run A* search for each goal state in the list
        for (int goalIndex: goalStatesList) {
            // Perform A* search and store path result
            int[] cheapestPath = performAStarSearch(cost_matrix, heuristic_vector, startStateIndex, goalIndex);
            // Print the cheapest path, the goal state and the number of cycles
            printCheapestPathStates(cheapestPath, goalIndex);
        }

        // Calculate the cheapest path among the three paths
        calculateOverallCheapestPath(goalStatesList);
    }
}