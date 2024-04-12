
/**
 * This class represent a buffer of 4096 bytes or 1024 records.
 * 
 * @author Richard Martinez
 * 
 * @version 2024-04-12
 */
public class Buffer {
    
    // 1 buffer = 4096 bytes = 1024 records
    public static final int BLOCK_SIZE_BYTES = 4096;

    // The 4096 byte long array representing the block
    private byte[] data;
    
    // The dirty bit
    private boolean dirty;
    
    // The block id from the file
    private int id;
    
    /**
     * Constructor for Buffer
     * 
     * @param id
     *      The id to use
     */
    public Buffer(int id) {
        this.setData(new byte[BLOCK_SIZE_BYTES]);
        this.dirty = false;
        this.id = id;
        
        // Fill data with zeros
        for (int i = 0; i < BLOCK_SIZE_BYTES; i++) {
            getData()[i] = 0;
        }
    }
    
    /**
     * Default constructor
     * 
     * Sets ID to -1
     */
    public Buffer() {
        this(-1);
    }
    
    /**
     * Set the record array (must be size 4) to the specified
     * position in the data array
     * 
     * @param record
     *      A byte array of size 4 representing the record
     * @param position
     *      The position or index in the buffer to place it
     */
    public void set(byte[] record, int position) {
        // Don't do any error checking here to save time
        getData()[position] = record[0];
        getData()[position+1] = record[1];
        getData()[position+2] = record[2];
        getData()[position+3] = record[3];
    }
    
    /**
     * Get the record at position and place result in space array
     * 
     * @param space
     *      The array where the result is placed
     * @param position
     *      The position or index in the buffer to read
     */
    public void get(byte[] space, int position) {
        // Don't do any error checking here to save time
        space[0] = getData()[position];
        space[1] = getData()[position+1];
        space[2] = getData()[position+2];
        space[3] = getData()[position+3];
    }
    
    /**
     * Make this buffer dirty
     * 
     * No need for clearDirty method
     */
    public void makeDirty() {
        dirty = true;
    }
    
    /**
     * Returns true if dirty
     * @return true if dirty
     */
    public boolean isDirty() {
        return dirty;
    }
    
    /**
     * Returns the block id
     * @return block id
     */
    public int getID() {
        return this.id;
    }

    /**
     * Returns the data stored in this buffer
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Sets the whole data
     * @param the data
     */
    public void setData(byte[] data) {
        this.data = data;
    }
}
