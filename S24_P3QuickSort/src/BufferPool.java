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
    
    // Number of active buffers
    private int activeBuffers;
    
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
     */
    public BufferPool(RandomAccessFile file, int numBuffers) {
        this.numBuffers = numBuffers;
        this.file = file;
        this.activeBuffers = 0;
        
        // Init buffer list
        this.buffers = new Buffer[numBuffers];
        
        // Init all with default constructor
        for (int i = 0; i < numBuffers; i++) {
            buffers[i] = new Buffer();
        }
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
}  
