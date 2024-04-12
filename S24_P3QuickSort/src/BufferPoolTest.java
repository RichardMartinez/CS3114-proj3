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
}
