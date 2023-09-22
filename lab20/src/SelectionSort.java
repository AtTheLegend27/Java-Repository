public class SelectionSort {


    /**
     * @param arr
     *
     * Sort the array arr in place using selection sort.
     * The selection sort algorithm is as follows:
     * 1. Find the minimum element in the array.
     * 2. Swap the minimum element with the first element in the array.
     * 3. Repeat steps 1 and 2, but ignoring the first element in the array.
     *
     * This should be an in-place sort, so you shouldn't create any additional arrays.
     */
//    static int trueCounter = 0;
//    static int[] visited;
    public static void sort(int[] arr) {
        // TODO: Implement selection sort
        //OG {6, 3, 5, 7, 2, 1, 4}
        for (int i = 0; i < arr.length; i++) {
            int currentMin = min(arr, i);
            swap(arr, currentMin, i);
        }
    }

    /**
     * @param arr
     * @param start
     * @return the index of the minimum element in the array arr, starting from index start.
     *
     * A suggested helper method that will make it easier for you to implement selection sort.
     */
    public static int min(int[] arr, int start) {
        // TODO: Implement this helper method
        int counter = arr.length -1;
        int currentMinumum = arr[counter];
        int updatedCurrentMin = counter;
        for (int futureCounter = counter - 1; futureCounter > start -1; futureCounter--) {
            if (arr[futureCounter] < currentMinumum) {
                currentMinumum = arr[futureCounter];
                updatedCurrentMin = futureCounter;
                counter --;
            }
            else if (futureCounter == 0){
                break;
            }
            else {
                counter--;
            }
        }
        return updatedCurrentMin;
    }


    /**
     * @param arr
     * @param i
     * @param j
     *
     * Swap the elements at indices i and j in the array arr.
     * A helper method you can use in your implementation of sort.
     */
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i]  = arr[j];
        arr[j]  = temp;

    }

    //check if visited before
//        for (int values = 0; values <= arr.length; values ++) {
//            for (int trueCounters = trueCounter; trueCounters == 0; trueCounters--) {
//                if (visited[trueCounters] == arr[values]) {
//                    arr[values] = 0;
//                } else {
//                    break;
//                }
//            }
//        }
//        //return min
//        int temporaruMin = 0;
//        int consistentCounter = 0;
//        for (int counter = 0; counter <= arr.length; counter ++) {
//            if (arr[consistentCounter] > arr[counter + 1]) {
//                consistentCounter = counter + 1 ;
//                temporaruMin = consistentCounter;
//            }
//            else {
//                break;
//            }
//        }
//        visited[trueCounter] = temporaruMin;
//        trueCounter ++;
//        return temporaruMin;

}

