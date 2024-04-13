import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * This class represents a sorter that can run
 * QuickSort on the BufferPool
 * 
 * @author Richard Martinez
 * 
 * @version 2024-04-12
 */
public class Sorter {

    // The buffer pool to use
    private BufferPool pool;

    /**
     * Constructor for Sorter
     * 
     * @param pool
     *            The buffer pool for the file
     */
    public Sorter(BufferPool pool) {
        this.pool = pool;
    }


    /**
     * Recursive quick sort method
     * 
     * @param i
     *            Recursive index i
     * @param j
     *            Recursive index j
     * @throws IOException
     */
    public void sort(int i, int j) throws IOException {
        // Optimize with insertion sort here
        // If Length is short enough, use inssort
        if (j - i < 10) {
            insertionSort(i, j);
            return;
        }

        // Find the pivot
        int indexPivot = findpivot(i, j);

        // Swap them
        swap(indexPivot, j);

        // Now, pivot record is at position j or indexEnd

        // Partition
        int k = partition(i, j);

        // Put the pivot back in its place
        swap(k, j);

        // Call sort recursively on left and right sub lists
        if (k - i > 1) {
            // Sort left sub list
            sort(i, k - 1);
        }
        if (j - k > 1) {
            sort(k + 1, j);
        }
    }


    /**
     * Insertion Sort for Optimization
     * 
     * @param i
     *            Left Index
     * @param j
     *            Right Index
     * @throws IOException
     */
    public void insertionSort(int i, int j) throws IOException {
        int length = j - i + 1;

        for (int a = 1; a < length; a++) {
            for (int b = a; (b > 0) && (getKey(i + b) < getKey(i + b
                - 1)); b--) {
                // Swap index b, b-1
                // Index b -> Virtual Index i+b
                swap(i + b, i + b - 1);
            }
        }

    }


    /**
     * A simple find pivot method
     * 
     * @param i
     *            The left index
     * @param j
     *            The right index
     * @return
     *         The pivot index
     * @throws IOException
     */
    public int findpivot(int i, int j) throws IOException {
        return (i + j) / 2;
    }


    /**
     * Swap method using virtual addresses
     * 
     * @param virtualIndex1
     *            The first address to swap
     * @param virtualIndex2
     *            The second address to swap
     * @throws IOException
     */
    public void swap(int virtualIndex1, int virtualIndex2) throws IOException {
        byte[] record1 = new byte[4];
        byte[] record2 = new byte[4];

        // Read both records
        pool.readRecord(record1, virtualIndex1);
        pool.readRecord(record2, virtualIndex2);

        // Write them to opposite addresses
        pool.writeRecord(record1, virtualIndex2);
        pool.writeRecord(record2, virtualIndex1);
    }


    /**
     * Partition the array
     * 
     * @param left
     *            The left index
     * @param right
     *            The right index
     * @return the position of the right sub array
     * @throws IOException
     */
    public int partition(int left, int right) throws IOException {
        // Pivot is at position right, grab it
        short pivotKey = getKey(right);

        // Then decrement right
        right--;

        // Then do partition using comparisons to pivot
        while (left <= right) { // Move inwards until they meet
            // Keep increasing left until you find something larger
            short leftKey = getKey(left);
            while (leftKey < pivotKey) {
                left++;
                leftKey = getKey(left);
            }

            // Keep decreasing right until you find something smaller
            short rightKey = getKey(right);
            while ((right >= left) && (rightKey >= pivotKey)) {
                right--;
                if (right < 0) {
                    break;
                }

                rightKey = getKey(right);
            }

            // if (right > left) -> swap
            if (right > left) {
                swap(left, right);
            }
        }

        // Return the position of the right sub array
        return left;
    }


    /**
     * Return the key of the virtual index as a short for comparison
     * 
     * @param virtualIndex
     *            The index to get
     * 
     * @return a short representing the key
     * @throws IOException
     */
    public short getKey(int virtualIndex) throws IOException {
        byte[] record = new byte[4];
        pool.readRecord(record, virtualIndex);
        return ByteBuffer.wrap(record).getShort();
    }

}
