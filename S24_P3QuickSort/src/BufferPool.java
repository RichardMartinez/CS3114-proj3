import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class represents the BufferPool
 * Internally, it holds a list of buffers
 * 
 * @author Richard Martinez
 * 
 * @version 2024-04-12
 */
public class BufferPool {
//    ByteBuffer.wrap(array).getShort()
    
    // 1 buffer = 4096 bytes = 1024 records
    public static final int BLOCK_SIZE_BYTES = 4096;
    
    // List of buffers
    private Buffer[] buffers;
    
    // Number of buffers available
    private int numBuffers;
    
    // RandomAccessFile to read and write
    private RandomAccessFile file;
    
    // TODO: Hold stats variables here
    
    /**
     * Constructor for BufferPool
     * 
     * @param file
     *      The RAF to read and write
     * @param numBuffers
     *      The number of buffers for this pool
     * @throws IOException 
     */
    public BufferPool(RandomAccessFile file, int numBuffers) {
        this.numBuffers = numBuffers;
        this.file = file;
        
        // Init buffer list
        this.buffers = new Buffer[numBuffers];
        
        // Init all with default constructor
        for (int i = 0; i < numBuffers; i++) {
            buffers[i] = new Buffer();
        }
    }
    
    /**
     * Reads the record stored at virtualAddress.
     * The buffer pool will determine which buffer it is in,
     * then act accordingly.
     * 
     * @param record
     *      The 4 byte long array where the record is returned
     * @param virtualAddress
     *      The virtualAddress to read from
     * @throws IOException 
     */
    public void readRecord(byte[] record, int virtualAddress) throws IOException {
        // Determine where the virtualAddress maps to
        int[] mapped = mapVirtualAddress(virtualAddress);
        int blockID = mapped[0];
        int offset = mapped[1];
        
        // Check if buffer with that blockID is in pool
        Buffer buf = findBuffer(blockID);
        
        if (buf == null) {
            // Not in pool
            // Do something about it
            
            // Is there space?
//            if (full) {
//                // NO, we are full
//                // EVICT
//                
//                return;
//            }
            
            // Not in pool, but YES we have space
            
            // Generate new buffer with that blockID
//            Buffer newBuf = new Buffer(blockID);
            
            // Read from the file into the new buffer
//            readFromFile(newBuf);
            
            // Place buffer at front of list
            
            // Read record into record array
            
            
            
            return;
        }
        
        // The block was in the pool
    }
    
    /**
     * Read from the file and place the data inside buf
     * @param buf
     *      The buffer to read into
     * @throws IOException 
     */
    public void readFromFile(Buffer buf) throws IOException {
        // Point RAF to correct starting location
        file.seek(buf.getID() * BLOCK_SIZE_BYTES);
        
        // Read into the buffer data
        file.read(buf.getData());
    }
    
    /**
     * Write the contents of the buffer to the file
     * @param buf
     *      The buffer to write
     * @throws IOException 
     */
    public void writeToFile(Buffer buf) throws IOException {
        // Point RAF to correct starting location
        file.seek(buf.getID() * BLOCK_SIZE_BYTES);
        
        // Write the file
        file.write(buf.getData());
    }
    
    /**
     * Maps a Virtual Address to a block ID and offset within that blocks
     * @param virtualAddress
     */
    public int[] mapVirtualAddress(int virtualAddress) {
        int[] mapped = new int[2];
        
        // Block ID
        mapped[0] = virtualAddress / BLOCK_SIZE_BYTES;
        
        // Block Offset within that block
        mapped[1] = virtualAddress % BLOCK_SIZE_BYTES;
        
        return mapped;
    }
    
    /**
     * If requested buffer is in pool, return it
     * Else return null
     * @param blockID
     *      The ID to check for
     * @return The requested buffer or null
     */
    public Buffer findBuffer(int blockID) {
        for (int i = 0; i < numBuffers; i++) {
            Buffer buf = buffers[i];
            if (buf.getID() == blockID) {
                return buf;
            }
        }
        return null;
    }
    
    /**
     * Place the buffer at the start of the list 
     * @param buf
     *      The new buffer to add
     */
    public void placeFront(Buffer buf) {
        // Shift everything down
        
        // Place at buffers[0]
        
        // Increment active buffers
    }
}  
