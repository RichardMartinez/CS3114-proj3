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

    private BufferPool pool;
    
    /**
     * Set up the test object
     * @throws IOException 
     */
    public void setUp() throws IOException {
        // TODO: Change this once RAF is working
        RandomAccessFile file = new RandomAccessFile("oneBlock.txt", "rw");
        pool = new BufferPool(file, 1);
    }
    
    /**
     * Test the BufferPool
     */
    public void testBufferPool() {
        int x = 1;
    }
    
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
}
