public class ArrayOperations {
    /**
     * Delete the value at the given position in the argument array, shifting
     * all the subsequent elements down, and storing a 0 as the last element of
     * the array.
     */
    public static void delete(int[] values, int pos) {
        if (pos < 0 || pos >= values.length) {
            return;
        }
        for (int i = pos; i <= values.length; i++){
            if (i +  1 >= values.length){
                values[i] = 0;
                return;
            } else {
                values[i] = values[i + 1];
                }
            }
    }


    /**
     * Insert newInt at the given position in the argument array, shifting all
     * the subsequent elements up to make room for it. The last element in the
     * argument array is lost.
     */
    public static void insert(int[] values, int pos, int newInt) {
        if (pos < 0 || pos >= values.length) {
            return;
        }
        for (int i = values.length -1 ; i > pos -2; i--){
            if (i > pos){
                values[i] = values[i - 1];
            } else {
                values[pos] = newInt;
                return;
            }
        }}


    /** 
     * Returns a new array consisting of the elements of A followed by the
     *  the elements of B. 
     */
    public static int[] catenate(int[] A, int[] B) {
        int [] total = new int [A.length + B.length];
        System.arraycopy(A, 0, total, 0, A.length);
        System.arraycopy(B, 0, total, A.length, B.length);
        System.out.println(total);

        return total;
    }

}