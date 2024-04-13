import java.io.IOException;
import java.io.RandomAccessFile;
import student.TestCase;

/**
 * This class tests the methods of the BufferPool class
 * 
 * @author Richard Martinez
 * 
 * @version 2024-04-12
 */
public class BufferPoolTest extends TestCase {
    
    public static final int RECORD_SIZE_BYTES = 4;

    private BufferPool pool;
    
    /**
     * Set up the test object
     * @throws IOException 
     */
    public void setUp() throws IOException {
        // Generate oneBlock.txt
        FileGenerator fg;
        fg = new FileGenerator("oneBlock2.txt", 1);
        fg.setSeed(33333333);
        fg.generateFile(FileType.ASCII);
        
        RandomAccessFile file = new RandomAccessFile("oneBlock2.txt", "rw");
        pool = new BufferPool(file, 1);
    }
    
//    /**
//     * Test the BufferPool
//     */
//    public void testBufferPool() {
//        int x = 1;
//    }
    
    /**
     * Test the mapping method
     */
    public void testMapVirtualAddress() {
        int[] mapped = new int[2];
        int blockID = -1;
        int offset = -1;
        
        mapped = pool.mapVirtualAddress(0);
        blockID = mapped[0];
        offset = mapped[1];
        assertEquals(blockID, 0);
        assertEquals(offset, 0);
        
        mapped = pool.mapVirtualAddress(1024);
        blockID = mapped[0];
        offset = mapped[1];
        assertEquals(blockID, 0);
        assertEquals(offset, 1024);
        
        mapped = pool.mapVirtualAddress(4095);
        blockID = mapped[0];
        offset = mapped[1];
        assertEquals(blockID, 0);
        assertEquals(offset, 4095);
        
        mapped = pool.mapVirtualAddress(4096);
        blockID = mapped[0];
        offset = mapped[1];
        assertEquals(blockID, 1);
        assertEquals(offset, 0);
        
        mapped = pool.mapVirtualAddress(4096+1024);
        blockID = mapped[0];
        offset = mapped[1];
        assertEquals(blockID, 1);
        assertEquals(offset, 1024);
    }
    
    /**
     * Test reading records
     * @throws IOException 
     */
    public void testReadRecord() throws IOException {
        byte[] space = new byte[4];
        
        pool.readRecord(space, 0);
        assertEquals(space[1], 'Q');
        
        pool.readRecord(space, 1);
        assertEquals(space[1], 'W');
        
        pool.readRecord(space, 2);
        assertEquals(space[1], 'E');
        
        pool.readRecord(space, 3);
        assertEquals(space[1], 'S');
    }
    
    
    
}
