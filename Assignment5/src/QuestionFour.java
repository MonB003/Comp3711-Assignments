import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * AttributeSubset class represents an attribute in the data file, such as Math or Science.
 */
class AttributeSubset {
    private final String attributeName;
    private final double informationGain;  // Information gain or entropy for child subset

    public AttributeSubset(String attributeName, double informationGain) {
        this.attributeName = attributeName;
        this.informationGain = informationGain;
    }

    /* Getter methods for inputs and outputs */
    public String getAttributeName() {
        return attributeName;
    }

    public double getInformationGain() {
        return informationGain;
    }
}

/**
 * TreeNode class represents a node in the decision tree.
 */
class TreeNode {
    String attribute; // The attribute used to split data
    Map<String, TreeNode> children; // Children nodes, key is attribute value
    String nodeType; // Classification if it's a leaf node

    public TreeNode(String attribute) {
        this.attribute = attribute;
        this.children = new HashMap<>();
    }

    public TreeNode(String nodeType, boolean isLeaf) {
        this.nodeType = nodeType;
        this.children = null;
    }

    /**
     * Returns a boolean value of whether the node is a leaf node.
     * @return True if the node is a leaf node, otherwise false.
     */
    public boolean isLeaf() {
        return nodeType != null;
    }
}

/**
 * QuestionFour class handles the assignment 5 functionality for question 4, such as reading and storing file data,
 * calculating entropy and information gain, finding the next attribute to split on, and building the decision tree.
 */
public class QuestionFour {
    // Stores (index, attribute name) pairs of each attribute
    private static final HashMap<Integer, String> attributesIndices = new HashMap<>();
    // Stores a list of all attribute names listed in the data
    private static final List<String> allAttributes = new ArrayList<>();
    // Stores all data read from the file passed to the program
    private static final ArrayList<String[]> allFileData = new ArrayList<>();

    /**
     * Reads a CSV file, extracts attributes and data, and stores them as String arrays in the allFileData list.
     * @param filename: String of the data filename.
     */
    public static void storeFileData(String filename) {
        try {
            // Create objects to read the file
            File file = new File(filename);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferReader = new BufferedReader(fileReader);
            // Store current file line and its data
            String currentLine;
            String[] currentFileData;

            // Read the first line with the attributes (column names) in the file
            currentLine = bufferReader.readLine();
            currentFileData = currentLine.split(",");
            // Store the attributes and their column index in the file
            for (int columnIndex = 0; columnIndex < currentFileData.length; columnIndex++) {
                String attributeName = currentFileData[columnIndex];
                attributesIndices.put(columnIndex, attributeName);
                allAttributes.add(attributeName);
            }

            // Read the file line by line
            while ((currentLine = bufferReader.readLine()) != null) {
                // Get line of data
                currentFileData = currentLine.split(",");
                // Store data in the ArrayList
                allFileData.add(currentFileData);
            }
            bufferReader.close();

        } catch (Exception exception) {
            // If an error occurs, print the error message
            System.out.println("Error: " + exception.getMessage());
        }
    }

    /**
     * Calculates the weighted average entropy of child nodes based on their size and entropy.
     * @param fractionEntropyPairs: ArrayList that stores double pairs of (size, entropy) values.
     * @param totalEntries: Double value of the total number of entries in the subset.
     * @return Double value of the average entropy of the children.
     */
    public static double calculateAverageChildrenEntropy(ArrayList<double[]> fractionEntropyPairs, double totalEntries) {
        double averageEntropy = 0.0;

        // Loop through each set of pairs in the ArrayList
        for (double[] currentPair : fractionEntropyPairs) {
            double currentChildSize = currentPair[0];
            double currentEntropy = currentPair[1];

            // Calculate weighted average entropy by multiplying each fraction by its entropy, and sum the results
            averageEntropy += (currentChildSize / totalEntries) * currentEntropy;
        }

        System.out.println("Average Children Entropy = " + averageEntropy);
        return averageEntropy;
    }

    /**
     * Calculates the entropy of a subset using "Yes" and "No" class labels.
     * @param subsetData: ArrayList of String arrays that stores the subset data rows.
     * @return Double value of the subset entropy.
     */
    public static double calculateSubsetEntropy(ArrayList<String[]> subsetData) {
        // Store the index of the boolean attribute
        int booleanIndex = attributesIndices.size() - 1;
        // Count occurrences of "Yes" and "No"
        int yesValueCount = 0;
        int noValueCount = 0;
        double totalValues = subsetData.size();

        // Loop through all data to count yes and no occurrences
        for (String[] currentData : subsetData) {
            String booleanValue = currentData[booleanIndex];  // Assuming booleanValue is the last element in the record
            if (booleanValue.equalsIgnoreCase("yes")) {
                yesValueCount++;
            } else {
                noValueCount++;
            }
        }

        // Calculate probabilities based on counts
        double yesFraction = yesValueCount / totalValues;
        double noFraction = noValueCount / totalValues;
        System.out.println("Yes fraction = " + yesValueCount + "/" + totalValues);
        System.out.println("No fraction = " + noValueCount + "/" + totalValues);

        // Calculate child entropy: H(X) = −p(x1)log2 p(x1) −p(x2)log2 p(x2) ... −p(xn)log2 p(xn)
        double entropy = 0.0; // Base case: if fraction is 0, entropy is 0
        if (yesFraction > 0) {
            entropy -= yesFraction * Math.log(yesFraction) / Math.log(2);
        }
        if (noFraction > 0) {
            entropy -= noFraction * Math.log(noFraction) / Math.log(2);
        }

        return entropy;
    }

    /**
     * Recursively prints the decision tree structure.
     * @param node: TreeNode object of the node to print.
     * @param prefix: String value to print before the node.
     */
    public static void printTree(TreeNode node, String prefix) {
        if (node.isLeaf()) {
            System.out.println(prefix + node.nodeType);
        } else {
            System.out.println(prefix + node.attribute);
            for (Map.Entry<String, TreeNode> entry : node.children.entrySet()) {
                printTree(entry.getValue(), prefix + "  " + entry.getKey() + " -> ");
            }
        }
    }

    /**
     * Splits a dataset into subsets based on the values of a specified attribute.
     * @param data: ArrayList of String arrays that stores the file data rows.
     * @param attributeIndex: Integer of the attribute's index in the data list.
     * @return HashMap of (attribute, data) pairs for each attribute and its data.
     */
    public static HashMap<String, ArrayList<String[]>> splitSubsetData(ArrayList<String[]> data, int attributeIndex) {
        // Stores pairs of: attribute name, all data values for that attribute
        HashMap<String, ArrayList<String[]>> splitData = new HashMap<>();

        // Loop through all rows in the dataset
        for (String[] currentData : data) {
            // Get the value of the attribute to split on
            String attributeValue = currentData[attributeIndex];

            // If the value doesn't exist in the map, create a new list for it
            if (!splitData.containsKey(attributeValue)) {
                splitData.put(attributeValue, new ArrayList<>());
            }

            // Add the current row to the appropriate list
            splitData.get(attributeValue).add(currentData);
        }

        return splitData;
    }

    /**
     * Finds the attribute subset with the highest information gain.
     * @param subsets: ArrayList of AttributeSubset objects for each subset.
     * @return AttributeSubset of the subset with the highest information gain.
     */
    public static AttributeSubset getHighestInformationGain(ArrayList<AttributeSubset> subsets) {
        System.out.println("--- Finding highest information gain ---");
        AttributeSubset highestSubset = subsets.getFirst();
        double highestInformationGain = highestSubset.getInformationGain();

        // Loop through all attributes
        for (AttributeSubset currentSubset : subsets) {
            System.out.println("Information gain of " + currentSubset.getAttributeName() + ": " + currentSubset.getInformationGain());
            if (currentSubset.getInformationGain() > highestInformationGain) {
                // Store new highest subset
                highestInformationGain = currentSubset.getInformationGain();
                highestSubset = currentSubset;
            }
        }

        System.out.println("\nHighest information gain is " + highestSubset.getAttributeName() + ": " + highestInformationGain + "\n");
        // Return the subset with the highest information gain, so it can become the next parent entropy of the attribute to split
        return highestSubset;
    }

    /**
     * Calculates the information gain of an attribute by calculating the parent entropy and the average subset entropy.
     * @param data: ArrayList of String arrays that stores the file data rows.
     * @param attributeIndex: Integer of the attribute's index in the data list.
     * @return Double value of the information gain.
     */
    public static double calculateInformationGain(ArrayList<String[]> data, int attributeIndex) {
        System.out.println("Attribute: " + allAttributes.get(attributeIndex));
        double parentEntropy = calculateSubsetEntropy(data);
        System.out.println("Parent Entropy: " + parentEntropy);

        // Split data based on attribute
        HashMap<String, ArrayList<String[]>> subsets = splitSubsetData(data, attributeIndex);

        // Loop through each child subset in the attribute
        ArrayList<double[]> sizeEntropyPairs = new ArrayList<>();
        double totalEntries = 0;
        for (String attributeValue : subsets.keySet()) {
            System.out.println("\nSubset: " + attributeValue);
            ArrayList<String[]> subset = subsets.get(attributeValue);
            double subsetEntropy = calculateSubsetEntropy(subset);
            System.out.println("Entropy: " + subsetEntropy);
            totalEntries += subset.size();
            sizeEntropyPairs.add(new double[]{subset.size(), subsetEntropy});
        }

        // Calculate average entropy and information gain
        double averageSubsetEntropy = calculateAverageChildrenEntropy(sizeEntropyPairs, totalEntries);
        return parentEntropy - averageSubsetEntropy; // Return information gain
    }

    /**
     * Determines the best attribute to split on by calculating the information gain for each attribute.
     * @param subsetData: ArrayList of String arrays that stores the subset data rows.
     * @param remainingAttributes: List of Strings for each attribute that hasn't been split on.
     * @return AttributeSubset object of the subset with the highest information gain, which will be split on next.
     */
    public static AttributeSubset getNextSplitAttribute(ArrayList<String[]> subsetData, List<String> remainingAttributes) {
        ArrayList<AttributeSubset> parentEntropyOptions = new ArrayList<>();

        // Loop through remaining attributes to calculate information gain
        for (String attribute : remainingAttributes) {
            int attributeIndex = getAttributeIndex(attribute);
            if (attribute.equals(allAttributes.getLast())) {
                continue; // Skip the boolean attribute
            }

            // Calculate the information gain for this attribute based on the subset data
            double informationGain = calculateInformationGain(subsetData, attributeIndex);
            System.out.println("Information Gain for " + attribute + ": " + informationGain + "\n");

            // Store the result for this attribute
            AttributeSubset currentSubset = new AttributeSubset(attribute, informationGain);
            parentEntropyOptions.add(currentSubset);
        }

        // Return the best subset with the highest information gain
        return getHighestInformationGain(parentEntropyOptions);
    }

    /**
     * Checks if all data rows have the same class label (yes or no).
     * @param data: ArrayList of String arrays that stores the file data rows.
     * @return String value of the class label if the labels are the same, otherwise return null.
     */
    private static String getClassLabel(ArrayList<String[]> data) {
        String classLabel = null;

        // Check if all data rows have the same class label (yes or no value)
        for (String[] record : data) {
            // Get the label value of the boolean attribute
            String currentLabel = record[record.length - 1];
            if (classLabel == null) {
                classLabel = currentLabel;
            } else if (!classLabel.equals(currentLabel)) {
                return null; // Data contains mixed class labels, not a leaf node
            }
        }

        return classLabel; // Return class label if all examples have the same label
    }

    /**
     * Gets the index of an attribute in the dataset.
     * @param attributeName: String of the attribute name.
     * @return Integer of the attribute's index in the attributesIndices HashMap, otherwise return -1 if not found.
     */
    public static int getAttributeIndex(String attributeName) {
        for (Map.Entry<Integer, String> entry : attributesIndices.entrySet()) {
            if (entry.getValue().equals(attributeName)) {
                return entry.getKey();
            }
        }
        return -1; // Return -1 if the attribute is not found
    }

    /**
     * Determines the majority class label in the dataset by counting occurrences.
     * @param data: ArrayList of String arrays that stores the file data rows.
     * @return String value of the class label with the most occurrences.
     */
    private static String getMajorityClass(ArrayList<String[]> data) {
        HashMap<String, Integer> classCounts = new HashMap<>();

        // Count occurrences of each class label
        for (String[] record : data) {
            String classLabel = record[record.length - 1];
            classCounts.put(classLabel, classCounts.getOrDefault(classLabel, 0) + 1);
        }

        // Find the class with the maximum count
        String majorityClass = null;
        int maxCount = -1;
        for (Map.Entry<String, Integer> entry : classCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                majorityClass = entry.getKey();
            }
        }

        return majorityClass;
    }

    /**
     * Recursively builds a decision tree using the ID3 algorithm, splitting on attributes with the highest
     * information gain until a stopping condition is met.
     * @param data: ArrayList of String arrays that stores the file data rows.
     * @param attributes: List of Strings for each attribute name.
     * @return TreeNode object of the entire tree's root node.
     */
    public static TreeNode buildTree(ArrayList<String[]> data, List<String> attributes) {
        // Base case: if the data is pure or no attributes left, return a leaf node
        String classLabel = getClassLabel(data);
        if (classLabel != null) {
            return new TreeNode(classLabel, true); // Leaf node
        }

        // Base case: if no attributes are left to split on, return a leaf node with the majority class
        if (attributes.isEmpty()) {
            String majorityClass = getMajorityClass(data);
            return new TreeNode(majorityClass, true); // Leaf node
        }

        // Calculate the best attribute to split on
        AttributeSubset bestSubset = getNextSplitAttribute(data, attributes);

        // Create a new TreeNode with the best attribute
        TreeNode node = new TreeNode(bestSubset.getAttributeName());

        // Recursively split the data and add children nodes
        List<String> remainingAttributes = new ArrayList<>(attributes);
        remainingAttributes.remove(bestSubset.getAttributeName());

        System.out.println("--- Splitting on " + bestSubset.getAttributeName() + " ---");
        int nextIndexSplit = getAttributeIndex(bestSubset.getAttributeName());
        HashMap<String, ArrayList<String[]>> splitData = splitSubsetData(data, nextIndexSplit);

        // Recursively build the tree for each subset
        for (Map.Entry<String, ArrayList<String[]>> entry : splitData.entrySet()) {
            String value = entry.getKey();
            ArrayList<String[]> childData = entry.getValue();
            TreeNode childNode = buildTree(childData, remainingAttributes);
            node.children.put(value, childNode); // Add the child node
        }

        return node; // Return the root node
    }

    /**
     * Performs the ID3 algorithm by calling helper methods to build and print the decision tree.
     */
    public static void performID3Algorithm() {
        List<String> remainingAttributes = new ArrayList<>(allAttributes);
        // Run ID3 algorithm and build the decision tree
        TreeNode root = buildTree(allFileData, remainingAttributes);
        // Print the resulting tree
        System.out.println("\n--- Decision Tree Result ---");
        printTree(root, "");
    }

    /**
     * Main method that runs the program, which takes the data filename, stores the data, and calls the method to
     * perform the ID3 algorithm.
     * @param args: Command line arguments passed to the program.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Error: One argument (data filename) must be passed to the program. Number of arguments passed: " + args.length);
            return;
        }
        String filename = args[0];
        storeFileData(filename);
        System.out.println("Performing ID3 algorithm on dataset from: " + filename + "\n");
        performID3Algorithm();
    }
}