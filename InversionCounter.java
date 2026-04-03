/*
    Simple implementation of Divide and Conquer. Will sort any array given in a text file,
    and then relay both the sorted list and number of "swaps" (inversions) done.

    For instructions on how to use this/how to format your input text, please see either the README
    and/or "sample_input.txt"
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Implements a divide-and-conquer algorithm to count the number of inversions in an array while
 * simultaneously sorting it.
 */
public class InversionCounter {

    /**
     * Implements the algorithm recursively.
     * It divides the array into two halves, recursively sorts and counts inversion in each half,
     * and then combines them.
     *
     * @param L     Input array to be sorted.
     * @param temp  Temporary array used during the merge step.
     * @param left  Starting index of the current subarray.
     * @param right Ending index of the current subarray.
     * @return Total number of inversions in the current subarray.
     */
    public static long SortAndCount(int[] L, int[] temp, int left, int right) {
        long invCount = 0;

        // If list L has more than one element (left < right)
        if (left < right) {
            // Divide the list into two halves A and B
            int mid = left + (right - left) / 2;

            // Sort-and-Count(A)
            long rA = SortAndCount(L, temp, left, mid);

            // Sort-and-Count(B)
            long rB = SortAndCount(L, temp, mid + 1, right);

            // Merge-and-Count(A, B)
            long r = MergeAndCount(L, temp, left, mid, right);

            // Combine the counts: r = rA + rB + r
            invCount = rA + rB + r;
        }

        // Base case implicitly returns 0 when left >= right
        return invCount;
    }

    /**
     * Combines two sorted halves of an array into a single sorted subarray and counts the
     * cross-inversions between the two halves.
     *
     * @param L     Input array containing the two sorted halves.
     * @param temp  Temporary array used for merging.
     * @param left  Starting index of the left half (A).
     * @param mid   Ending index of the left half (A).
     * @param right Ending index of the right half (B).
     * @return Number of cross-inversions found during the merge step.
     */
    public static long MergeAndCount(int[] L, int[] temp, int left, int mid, int right) {
        int i = left;      // Starting index for the left subarray A
        int j = mid + 1;   // Starting index for the right subarray B
        int k = left;      // Starting index for the temporary array
        long invCount = 0;

        // Traverse both halves
        while ((i <= mid) && (j <= right)) {
            if (L[i] <= L[j]) {
                temp[k++] = L[i++];
            } else {
                // The number on the left is larger than the number on the right.
                // Because the left half is already sorted, all remaining elements in the left
                // half form an inversion with L[j].
                temp[k++] = L[j++];
                invCount += (mid - i + 1);
            }
        }

        // Copy remaining elements of the left half, if any
        while (i <= mid) {
            temp[k++] = L[i++];
        }

        // Copy remaining elements of the right half, if any
        while (j <= right) {
            temp[k++] = L[j++];
        }

        // Update the sorted elements back into the original array L
        for (i = left; i <= right; i++) {
            L[i] = temp[i];
        }

        return invCount;
    }

    /**
     * Prompts the user for an input file name, reads the file line by line to parse integer
     * arrays, and displays the inversion count/sorted array for each line.
     *
     * @param args Command line arguments for main.
     */
    public static void main(String[] args) {
        // Prompt user for filename
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the input file name: ");
        String fileName = scanner.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                // Parse line into an array of integers
                String[] strTokens = line.split("\\s+");
                int[] L = new int[strTokens.length];

                // Temporary array for merging
                int[] temp = new int[strTokens.length];

                for (int i = 0; i < strTokens.length; i++) {
                    L[i] = Integer.parseInt(strTokens[i]);
                }

                // Call the sorting and counting method
                long totalInversions = SortAndCount(L, temp, 0, L.length - 1);

                // Display the number of inversions and the sorted array
                System.out.print("Inversion count: " + totalInversions + ". Sorted array: ");
                for (int i = 0; i < L.length; i++) {
                    System.out.print(L[i]);
                    if (i < L.length - 1) {
                        System.out.print(" ");
                    }
                }
                System.out.println(".");
            }

            // Error handling (not sure if this was required but just as a safety measure)
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing input. Please ensure the file contains valid integers");
        }

        scanner.close();
    }
}