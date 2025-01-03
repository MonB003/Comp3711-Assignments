import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

class Subset {
    private final String attributeName;
    private final double entropy;
    private final double informationGain;

    public Subset(String attributeName, double entropy, double informationGain) {
        this.attributeName = attributeName;
        this.entropy = entropy;
        this.informationGain = informationGain;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public double getEntropy() {
        return entropy;
    }

    public double getInformationGain() {
        return informationGain;
    }
}
//class Subset {
//    private final String attributeName;
//    private final double informationGain;
//
//    public Subset(String attributeName, double informationGain) {
//        this.attributeName = attributeName;
//        this.informationGain = informationGain;
//    }
//
//    public String getSubsetName() {
//        return attributeName;
//    }
//
//    public double getInformationGain() {
//        return informationGain;
//    }
//}

class TreeNode {
    String attribute; // The attribute used to split data
    Map<String, TreeNode> children; // Children nodes, keyed by attribute value
    String nodeType; // Classification if it's a leaf node

    public TreeNode(String attribute) {
        this.attribute = attribute;
        this.children = new HashMap<>();
    }

    public TreeNode(String nodeType, boolean isLeaf) {
        this.nodeType = nodeType;
        this.children = null;
    }

    public boolean isLeaf() {
        return nodeType != null;
    }
}

public class QuestionFour {
    private static final HashMap<Integer, String> attributesIndices = new HashMap<>();
    private static final ArrayList<String> allAttributes = new ArrayList<>();
    private static final ArrayList<String[]> allFileData = new ArrayList<>();
//    private static final Set<String> visitedNodes = new HashSet<>();

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

    public static double calculateEntropy(double recordFraction) {
        // Base case: if fraction is 0, entropy is 0
        if (recordFraction == 0) {
            return 0;
        }
        // Calculate entropy: -p(x) log2 p(x)
        double logBase2Result = (Math.log(recordFraction) / Math.log(2));
        double entropyValue = -recordFraction * logBase2Result;
//        System.out.println("Entropy = " + entropyValue);
        return entropyValue;
    }

//    public static double calculateTotalChildEntropy(ArrayList<Double> recordFractions) {
//        // Calculate child entropy: H(X) = −p(x1)log2 p(x1) −p(x2)log2 p(x2) ... −p(xn)log2 p(xn)
//        double childEntropy = 0;
//        // Sum all entropy values
//        for (double currentFraction : recordFractions) {
//            childEntropy += calculateEntropy(currentFraction);
//        }
//
//        System.out.println("Child Entropy = " + childEntropy);
//        return childEntropy;
//    }

    public static double calculateAverageChildrenEntropy(ArrayList<double[]> fractionEntropyPairs, double totalEntries) {
        double averageEntropy = 0;

        // Loop through each set of pairs in the ArrayList
        for (double[] currentPair : fractionEntropyPairs) {
            double currentChildSize = currentPair[0];
            double currentEntropy = currentPair[1];

            // Calculate weighted average entropy by multiplying each fraction by its entropy, and sum the results
            averageEntropy += (currentChildSize/totalEntries) * currentEntropy;
        }

//        System.out.println("Average Children Entropy = " + averageEntropy);
        return averageEntropy;
    }

    public static double calculateSubsetEntropy(ArrayList<String[]> subsetData) {
        // Store the index of the attribute to split on
        int booleanIndex = attributesIndices.size() - 1;
        int yesValueCount = 0;
        int noValueCount = 0;
        double totalValues = subsetData.size();

        // Loop through all data to count yes and no occurrences
        for (String[] currentData : subsetData) {
            String booleanValue = currentData[booleanIndex];
            if (booleanValue.equalsIgnoreCase("yes")) {
                yesValueCount++;
            } else {
                noValueCount++;
            }
        }

        double yesFraction = yesValueCount / totalValues;
        double noFraction = noValueCount / totalValues;

        System.out.println("Yes fraction = " + yesValueCount + "/" + totalValues);
        System.out.println("No fraction = " + noValueCount + "/" + totalValues);

        // Return child entropy
        return (calculateEntropy(yesFraction) + calculateEntropy(noFraction));

//        ArrayList<Double> recordFractions = new ArrayList<>();
//        recordFractions.add(yesFraction);
//        recordFractions.add(noFraction);
//        return calculateTotalChildEntropy(recordFractions);
    }

    public static void printTree(TreeNode node, String prefix) {
        if (node.isLeaf()) {
            System.out.println(prefix + "Leaf: " + node.nodeType);
        } else {
            System.out.println(prefix + "Subset: " + node.attribute);
            for (Map.Entry<String, TreeNode> entry : node.children.entrySet()) {
                printTree(entry.getValue(), prefix + "  " + entry.getKey() + " -> ");
            }
        }
    }

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

    public static Subset getHighestInformationGain(ArrayList<Subset> subsets) {
        Subset highestSubset = subsets.getFirst();
        double highestInformationGain = highestSubset.getInformationGain();
        for (Subset currentSubset : subsets) {
            if (currentSubset.getInformationGain() > highestInformationGain) {
                highestInformationGain = currentSubset.getInformationGain();
            }
        }

        System.out.println("Highest Information Gain is " + highestSubset.getAttributeName() + ": " + highestInformationGain);
        // Return the entropy of the subset with the highest information gain, so it can become the next parent entropy
        return highestSubset;
    }

    public static double calculateParentEntropy() {
        // Calculate entropy of the entire dataset
        System.out.println("Calculations for entire dataset:");

        double parentEntropy = calculateSubsetEntropy(allFileData);
        System.out.println("Dataset Entropy: " + parentEntropy);

        ArrayList<Subset> parentEntropyOptions = new ArrayList<>();
//        ArrayList<Subset> subsetsInfo = new ArrayList<>();

        // Find the first attribute to split on by calculating the information gain for each attribute
        for (int attributeIndex = 0; attributeIndex < attributesIndices.size() - 1; attributeIndex++) {
            System.out.println("-----------------------------");
            String attribute = attributesIndices.get(attributeIndex);
            System.out.println("Subset: " + attribute);

            // Calculate the information gain for this attribute
            HashMap<String, ArrayList<String[]>> subsets = splitSubsetData(allFileData, attributeIndex);
            ArrayList<Subset> subsetsInfo = new ArrayList<>();

            // Loop through each child subset in the attribute
            ArrayList<double[]> sizeEntropyPairs = new ArrayList<>();
            double totalEntries = 0;
            for (String attributeValue : subsets.keySet()) {
                System.out.println("Subset value: " + attributeValue);
                ArrayList<String[]> subset = subsets.get(attributeValue);
                double subsetEntropy = calculateSubsetEntropy(subset);
                System.out.println("Child Entropy = " + subsetEntropy);
                totalEntries += subset.size();
                sizeEntropyPairs.add(new double[]{subset.size(), subsetEntropy});

//                double informationGain = parentEntropy - subsetEntropy;
//                Subset currentSubset = new Subset(attributesIndices.get(attributeIndex), attributeValue, subsetEntropy, informationGain);
//                subsetsInfo.add(currentSubset);
            }
            // Calculate average entropy and info gain
            double averageSubsetEntropy = calculateAverageChildrenEntropy(sizeEntropyPairs, totalEntries);
            System.out.println("AVG ENTROPY: " + averageSubsetEntropy);
            double informationGain = parentEntropy - averageSubsetEntropy;
            System.out.println("INFO GAIN: " + informationGain);
            Subset currentSubset = new Subset(attributesIndices.get(attributeIndex), averageSubsetEntropy, informationGain);
//            subsetsInfo.add(currentSubset);
            parentEntropyOptions.add(currentSubset);

//            Subset nextHighestSubset = getHighestInformationGain(subsetsInfo);
//            parentEntropyOptions.add(nextHighestSubset);
        }

        Subset nextParentSubset = getHighestInformationGain(parentEntropyOptions);
        return nextParentSubset.getEntropy();
    }

    public static void calculateAllSubsetsEntropy(HashMap<String, ArrayList<String[]>> subsets, double parentEntropy) {
        for (String attributeValue : subsets.keySet()) {
            ArrayList<String[]> subset = subsets.get(attributeValue);

            // Calculate entropy for this subset
            double subsetEntropy = calculateSubsetEntropy(subset);

            // Calculate information gain: Information Gain = entropy(parent) – [average entropy(children)]
            double informationGain = parentEntropy - subsetEntropy;

            // Print the subset values along with its entropy and information gain
            System.out.println("Entropy: " + subsetEntropy);
            System.out.println("Information Gain: " + informationGain);
            System.out.println();
        }
    }

    public static void performID3Algorithm() {
        double parentEntropy = calculateParentEntropy();
        System.out.println("FINISHED PARENT ENTROPY");
//        int numSubsets = allFileData.getFirst().length;
//        for (int index = 0; index < numSubsets; index++) {
//            HashMap<String, ArrayList<String[]>> currentSubsetData = splitSubsetData(allFileData, index);
//            calculateAllSubsetsEntropy(currentSubsetData, parentEntropy);
//        }
    }

    public static void main(String[] args) {
        String filename = args[0];
        storeFileData(filename);
        performID3Algorithm();
    }
}