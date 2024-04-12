import java.io.IOException;

/**
 * This class represents a sorter that can run
 * QuickSort on the BufferPool
 * 
 * @author Richard Martinez
 * 
 * @version 2024-04-12
 */
public class Sorter {
    
    public static final int RECORD_SIZE_BYTES = 4;
    
    // The buffer pool to use
    private BufferPool pool;
    
    /**
     * Constructor for Sorter
     * @param pool
     */
    public Sorter(BufferPool pool) {
        this.pool = pool;
    }
    
    /**
     * Recursive quick sort method
     * @param i
     *      Recursive index i
     * @param j
     *      Recursive index j
     * @throws IOException 
     */
    public void sort(int i, int j) throws IOException {
        // Find the pivot
        int indexPivot = findpivot(i, j);
        int indexEnd = j * RECORD_SIZE_BYTES;
        
        // Read pivot and end
        byte[] pivotRecord = new byte[RECORD_SIZE_BYTES];
        pool.readRecord(pivotRecord, indexPivot);
        
        byte[] endRecord = new byte[RECORD_SIZE_BYTES];
        pool.readRecord(endRecord, indexEnd);
        
        // Swap them
        swap(indexPivot, indexEnd);
        
        // TODO: Partition
        
        // At the end, flush the buffer pool?
    }
    
    /**
     * A simple find pivot method
     * @param i
     *      The left index
     * @param j
     *      The right index
     * @return
     *      The pivot index
     */
    public int findpivot(int i, int j) {
        return (i+j)/2;
    }
    
    /**
     * Swap method using virtual addresses
     * @param virtualAddress1
     *      The first address to swap
     * @param virtualAddress2
     *      The second address to swap
     * @throws IOException
     */
    public void swap(int virtualIndex1, int virtualIndex2) throws IOException {
        byte[] record1 = new byte[RECORD_SIZE_BYTES];
        byte[] record2 = new byte[RECORD_SIZE_BYTES];
        
        int virtualAddress1 = virtualIndex1 * RECORD_SIZE_BYTES;
        int virtualAddress2 = virtualIndex2 * RECORD_SIZE_BYTES;
        
        // Read both records
        pool.readRecord(record1, virtualAddress1);
        pool.readRecord(record2, virtualAddress2);
        
        // Write them to opposite addresses
        pool.writeRecord(record1, virtualAddress2);
        pool.writeRecord(record2, virtualAddress1);
    }
    
    
}
