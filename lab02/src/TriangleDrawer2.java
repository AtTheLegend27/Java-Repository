public class TriangleDrawer2 {
    public static void main(String[] args) {
        int SIZE = 10;
        int row;
        for (row = 0; row < SIZE; row += 1){
            int col;
            for (col = 0; col <= row; col = col + 1) {
                System.out.print('*');
            }
            System.out.println();

        }
    }
}
