import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class QuestionFour {
    private static final ArrayList<String[]> allFileData = new ArrayList<>();
    public static void storeFileData(String filename) {
        try {
            // Create objects to read the file
            File file = new File(filename);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferReader = new BufferedReader(fileReader);
            // Store current file line and its data
            String currentLine;
            String[] currentFileData;

            // Read the file line by line
            while ((currentLine = bufferReader.readLine()) != null) {
                // Get line of data
                currentFileData = currentLine.split(",");
                // Store data in the ArrayList
                allFileData.add(currentFileData);

//                // Print data
//                for (String data : currentFileData) {
//                    System.out.print(data + " ");
//                }
//                System.out.println();
            }
            bufferReader.close();
            
        } catch(Exception exception) {
            // If an error occurs, print the error message
            System.out.println("Error: " + exception.getMessage());
        }
    }

    
    public static void main(String[] args) {
        String filename = args[0];
        storeFileData(filename);
    }
}