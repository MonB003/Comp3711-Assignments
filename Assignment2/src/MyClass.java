import java.util.*;

public class MyClass {
    public static String[] stateLetters = {"A", "B", "C", "D", "E", "H", "J", "G1", "G2", "G3"};

    public static boolean isGoalState(int stateIndex, int[] goalStateIndices) {
        for (int goalState: goalStateIndices) {
            if (goalState == stateIndex) {
                System.out.println("State: " + stateLetters[stateIndex] + " is a goal state.");
                return true;
            }
        }
        return false;
    }
//    public static boolean isGoalState(int stateIndex, int[] heuristic_vector) {
//        for (int index = 0; index < heuristic_vector.length; index++) {
//            if ((index == stateIndex) && (heuristic_vector[index] == 0)) {
//                return true;
//            }
//        }
//        return false;
//    }

    public static int[] performAStarSearch(int[][] cost_matrix, int[] heuristic_vector, int startStateIndex, int[] goalStateIndices) {
        // Store node indices to visit and their costs
        PriorityQueue<int[]> openNodes = new PriorityQueue<>(Comparator.comparingInt(node -> node[1]));
        // Store visited nodes
        HashSet<Integer> closedNodes = new HashSet<>();
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

        // Check if there are still open nodes to explore
        while (!openNodes.isEmpty()) {
            // Get node with the lowest estimated cost
            int[] currentNode = openNodes.poll();
            int currentState = currentNode[0];

            // Check if the current state is a goal state
            if (isGoalState(currentState, goalStateIndices)) {
                return getPath(previousStates, currentState);
            }

            // State has been visited
            closedNodes.add(currentState);

            // Check neighbour nodes for the next state in the path
            for (int neighbourIndex = 0; neighbourIndex < cost_matrix.length; neighbourIndex++) {
                // Store current path cost (in the current state's column)
                int currentPathCost = cost_matrix[neighbourIndex][currentState];
                if (currentPathCost == 0 || closedNodes.contains(neighbourIndex)) {
                    // Skip this neighbour node if its value is 0 (there's no direct path) or it's already been visited
                    continue;
                }

                int gPathCost = initialPathCosts.getOrDefault(currentState, Integer.MAX_VALUE) + currentPathCost;
                if (gPathCost < initialPathCosts.getOrDefault(neighbourIndex, Integer.MAX_VALUE)) {
                    previousStates.put(neighbourIndex, currentState);
                    initialPathCosts.put(neighbourIndex, gPathCost);
                    // Calculate heuristic function: f(n) = g(n) + h(n) = path cost + estimated cost
                    int estimateCostSum = gPathCost + heuristic_vector[neighbourIndex];
                    estimatedCosts.put(neighbourIndex, estimateCostSum);
                    openNodes.add(new int[]{neighbourIndex, estimateCostSum});
                }
            }
        }

        // Return empty path if no path is found
        return new int[0];
    }

    private static int[] getPath(HashMap<Integer, Integer> previousStates, int currentState) {
        ArrayList<Integer> allPathStates = new ArrayList<>();
        allPathStates.add(currentState);
        while (previousStates.containsKey(currentState)) {
            currentState = previousStates.get(currentState);
            allPathStates.add(currentState);
        }
        int[] path = new int[allPathStates.size()];
        for (int index = 0; index < path.length; index++) {
            path[index] = allPathStates.get(allPathStates.size() - 1 - index);
        }
        return path;
    }

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
//        int[] goalStateIndices = {7, 8, 9}; // With hardcoded values
        for (int index = 0; index < heuristic_vector.length; index++) {
            // Goal states have a 0 value in the heuristic vector
            if (heuristic_vector[index] == 0) {
                goalStatesList.add(index);
            }
        }
        int[] goalStateIndices = new int[goalStatesList.size()];
        for (int goalIndex = 0; goalIndex < goalStatesList.size(); goalIndex++) {
           goalStateIndices[goalIndex] = goalStatesList.get(goalIndex);
        }

        // Write a program to implement the A * search

        // Start state is A
        int startStateIndex = 0;

        // Perform A* search and store path result
        int[] cheapestPath = performAStarSearch(cost_matrix, heuristic_vector, startStateIndex, goalStateIndices);
        // Print the cheapest path, the goal state and the number of cycles
        if (cheapestPath.length > 0) {
            System.out.println("Cheapest path: ");
            for (int currentState: cheapestPath) {
                System.out.print(stateLetters[currentState] + " ");
            }
            System.out.println();
            System.out.println("Number of cycles: " + cheapestPath.length);
        } else {
            System.out.println("There was no path found to any goal state.");
        }
    }
}