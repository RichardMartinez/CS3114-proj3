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
        
        // Init buffer list
        this.buffers = new Buffer[numBuffers];
    }
}  
