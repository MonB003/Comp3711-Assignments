public class MyClass {
    public static void main(String[] args) {
        // Check arguments
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

        // Assignment 1 function
        // Set next location default value
        char next_location = current_location;
        // If all squares are clean, the vacuum cleaner stays in its current location
        boolean allSquaresClean = A_status && B_status && C_status && D_status;
        if (allSquaresClean) {
//            next_location = current_location;
            return; // Or add else check
        }

        // If current location is not clean, vacuum stays in its current location to clean it up

        int location_index = 0;
        char[] letter_indices = {'A', 'B', 'C', 'D'};
        for (int index = 0; index < letter_indices.length; index++) {
            char current_letter = letter_indices[index];
            if (current_location == current_letter) {
                // Set index, add 1 because boolean arguments start at 1
                location_index = index+1;
            }
        }
        boolean current_location_status = Boolean.parseBoolean(args[location_index]);

        if (current_location == 'A') {
            // Check if location is clean
           if (A_status) {
               // Move to B
               next_location = 'B';
           }
        } else if (current_location == 'B') {

        } else if (current_location == 'C') {

        } else if (current_location == 'D') {

        } else {

        }

        System.out.println("\nAction - Next Location = " + next_location);
    }
}
