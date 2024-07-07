import java.util.ArrayList;

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

    public static int[] performAStarSearch(int[][] cost_matrix, int[] heuristic_vector, int startStateIndex, int[] goalStateIndices) {
        // Check if the current state is a goal state
        for (int goalState: goalStateIndices) {
            if (goalState == startStateIndex) {
                // Use this state?
                return new int[]{startStateIndex, 0};
            }
        }

        // Store node indices to visit and their costs
        ArrayList<int[]> openNodes = new ArrayList<>();

        // Check if there are still open nodes to explore
        for (int index = 0; index < cost_matrix[startStateIndex].length; index++) {
            // Store current path cost
            int currentCost = cost_matrix[startStateIndex][index];
            System.out.println("Current cost: " + currentCost);

            // If cost is not 0, there's a path to the current node
            if (currentCost > 0) {
                // Calculate heuristic function: f(n) = g(n) + h(n)
                int costSum = currentCost + heuristic_vector[index];
                int[] indexCost = {index, costSum};
                openNodes.add(indexCost);
            }
        }

        // Find next start node state
        int[] nextStartState = {0, 0};
        for (int[] currentNode: openNodes) {
            if (currentNode[1] > nextStartState[1]) {
                nextStartState = currentNode;
            }
        }
        System.out.println("Next start state: " + stateLetters[nextStartState[0]]);
        return nextStartState;
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
        int[] goalStateIndices = {7, 8, 9};

        // Write a program to implement the A * search
        // Print the cheapest path, the goal state and the number of cycles

        // Start state is A
        int startStateIndex = 0;

        while (!isGoalState(startStateIndex, goalStateIndices)) {
            int[] nextStartState = performAStarSearch(cost_matrix, heuristic_vector, startStateIndex, goalStateIndices);
            startStateIndex = nextStartState[0];
        }

        System.out.println("DONE WHILE LOOP");
    }
}