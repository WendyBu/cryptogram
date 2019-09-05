package edu.gatech.seclass.crypto6300.utils;

public class Utils {
    private static final String DELIMITER = ",";
    /**
     * This method converts an array of integers into a string.
     * @param array array of integers
     * @return equivalent string
     */
    public static String toString(int[] array){
        if(array == null || array.length == 0)
            return null;

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < array.length; i++){
            sb.append(array[i]);
            if(i < array.length - 1)
                sb.append(DELIMITER);
        }
        return sb.toString();
    }

    /**
     * This method converts a string into an array of integers.
     * @param s string
     * @return array of integers
     */
    public static int[] toArray(String s){
        if(s == null || s.isEmpty())
            return null;

        final String[] splitString = s.split(DELIMITER);
        int length = splitString.length;
        int[] numbers = new int[length];
        for(int i = 0; i < length; i++){
            numbers[i] = Integer.valueOf(splitString[i]);
        }
        return numbers;
    }
}
