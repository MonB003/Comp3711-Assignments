import java.util.ArrayList;

public class MyClass {
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

        // Start state is A
        int startStateIndex = 0;

        // Identify goal states and save them in a new vector
        int[] goalStateIndices = {7, 8, 9};

        // Write a program to implement the A * search
        // Print the cheapest path, the goal state and the number of cycles

        // Store node indices to visit and their costs
        ArrayList<int[]> indexCostPairs = new ArrayList<>();

        for (int index = 0; index < cost_matrix[startStateIndex].length; index++) {
            // Store current path cost
            int currentCost = cost_matrix[startStateIndex][index];
            System.out.println("Current cost: " + currentCost);

            // If cost is not 0, there's a path to the current node
            if (currentCost > 0) {
                // Calculate f(n) = g(n) + h(n)
                int costSum = currentCost + heuristic_vector[index];
                int[] indexCost = {index, costSum};
                indexCostPairs.add(indexCost);
            }
        }
    }
}