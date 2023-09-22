import java.util.Arrays;

public class DistributionSorts {

    /* Destructively sorts ARR using counting sort. Assumes that ARR contains
       only 0, 1, ..., 9. */
    public static void countingSort(int[] arr) {
        // TODO: YOUR CODE HERE
        //first create an array that counts the number of times the object in question is counter
        //we need array from 0 - 10 in order to accomodate for 0-9 in arr
        int [] numOfTimes = new int[10];
        //count the number of times in num of times through for loop iteration at specific location
        for (int i = 0; i < arr.length; i++) {
            numOfTimes[arr[i]] += 1;
        }
        //now change values of arr based upon the numOfTimes
        //for (int i = 0; i < arr.length; i++){
        //double while loop to account for the increasing increments
        int counter = 0;
        int i = 0;
        while (counter != numOfTimes.length) {
            while (numOfTimes[counter] > 0) {
                arr[i] = counter;
                numOfTimes[counter] -= 1;
                i++;
            }
            counter++;
        }
    }

    /* Destructively sorts ARR using LSD radix sort. */
    public static void lsdRadixSort(int[] arr) {
        int maxDigit = mostDigitsIn(arr);
        for (int d = 0; d < maxDigit; d++) {
            countingSortOnDigit(arr, d);
        }
    }

    /* A helper method for radix sort. Modifies ARR to be sorted according to
       DIGIT-th digit. When DIGIT is equal to 0, sort the numbers by the
       rightmost digit of each number. */
    private static void countingSortOnDigit(int[] arr, int digit) {
        // TODO: YOUR CODE HERE
        //all we need this helper to do is diler by that specific digit
        //the actual sort method iterates through each digit
        int [] numOfTimes = new int[10];
        //need another int[] to accomodate for desired changes in arr
        int [] arrHelper = new int[arr.length];
        int denominator = (int) Math.pow(10, digit);
        int reIndexing = 0;
        //need to account for the digit in the location of the power in numOfTimes
        for (int i = 0; i < arr.length; i++) {
            //https://stackoverflow.com/questions/8842504/raising-a-number-to-a-power-in-java
            //https://stackoverflow.com/questions/8071363/calculating-powers-of-integers
            //I didn't know how to raise to the power so I did research on stackoverflow and linked it
            int placeHolder = ((arr[i]/denominator)%10);
            numOfTimes[placeHolder] += 1;
        }
        //need to reindex or else the other numbers in the set won't appear correctly besides the first
        while (reIndexing < numOfTimes.length - 1)  {
            numOfTimes[reIndexing+1] += numOfTimes[reIndexing];
            reIndexing += 1;
        }
        //placing correct numbers into the arrHelper since we are destructively changing arr
        //if we don't put it in arrHelper, then we could run the risk of duplicate numbers
        for (int counterBackward = arr.length -1; counterBackward >= 0; counterBackward--) {
            int placeHolder = ((arr[counterBackward]/denominator)%10);
            arrHelper[numOfTimes[placeHolder]-1] = arr[counterBackward];
            numOfTimes[placeHolder] -= 1;
        }
        //now to destructively copy arrHelper to arr
        int counter = 0;
        while (counter < arrHelper.length) {
            arr[counter] = arrHelper[counter];
            counter += 1;
        }
    }

    /* Returns the largest number of digits that any integer in ARR has. */
    private static int mostDigitsIn(int[] arr) {
        int maxDigitsSoFar = 0;
        for (int num : arr) {
            int numDigits = (int) (Math.log10(num) + 1);
            if (numDigits > maxDigitsSoFar) {
                maxDigitsSoFar = numDigits;
            }
        }
        return maxDigitsSoFar;
    }

    /* Returns a random integer between 0 and 9999. */
    private static int randomInt() {
        return (int) (10000 * Math.random());
    }

    /* Returns a random integer between 0 and 9. */
    private static int randomDigit() {
        return (int) (10 * Math.random());
    }

    private static void runCountingSort(int len) {
        int[] arr1 = new int[len];
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = randomDigit();
        }
        System.out.println("Original array: " + Arrays.toString(arr1));
        countingSort(arr1);
        if (arr1 != null) {
            System.out.println("Should be sorted: " + Arrays.toString(arr1));
        }
    }

    private static void runLSDRadixSort(int len) {
        int[] arr2 = new int[len];
        for (int i = 0; i < arr2.length; i++) {
            arr2[i] = randomInt();
        }
        System.out.println("Original array: " + Arrays.toString(arr2));
        lsdRadixSort(arr2);
        System.out.println("Should be sorted: " + Arrays.toString(arr2));

    }

    public static void main(String[] args) {
        runCountingSort(20);
        runLSDRadixSort(3);
        runLSDRadixSort(30);
    }
}