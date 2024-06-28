public class Main {
    public static int getNextHorizontalSquare(int locationIndex) {
        // If the number is even, move left. If it's odd, move right.
        return (locationIndex % 2 == 0) ? (locationIndex - 1) : (locationIndex + 1);
    }

    public static int getNextVerticalSquare(int locationIndex) {
        int rowSize = 2;
        return (locationIndex + rowSize) > 4 ? (locationIndex - rowSize) : (locationIndex + rowSize);
    }

    public static int getNextDiagonalSquare(int locationIndex) {
        switch (locationIndex) {
            case 1:
                return 4;
            case 2:
                return 3;
            case 3:
                return 2;
            case 4:
                return 1;
            default:
                return 0; // If error occurs
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello and welcome!");

        for (int i = 1; i <= 4; i++) {
//            System.out.println("Horiz " + i + ": " + getNextHorizontalSquare(i));
//            System.out.println("Vert " + i + ": " + getNextVerticalSquare(i));
            System.out.println("Diag " + i + ": " + getNextDiagonalSquare(i));
        }
    }
}