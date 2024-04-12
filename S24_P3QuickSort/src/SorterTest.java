import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import student.TestCase;

/**
 * This class tests the methods of the Sorter class
 * 
 * @author Richard Martinez
 * 
 * @version 2024-04-12
 */
public class SorterTest extends TestCase {

    private Sorter sorter;
    private BufferPool pool;
    
    /**
     * Set up the test object
     * @throws FileNotFoundException 
     */
    public void setUp() throws FileNotFoundException {
        // Generate oneBlock.txt
        FileGenerator fg;
        fg = new FileGenerator("oneBlock.txt", 1);
        fg.setSeed(33333333);
        fg.generateFile(FileType.ASCII);
        
        RandomAccessFile file = new RandomAccessFile("oneBlock.txt", "rw");
        pool = new BufferPool(file, 1);
        sorter = new Sorter(pool);
    }
    
    /**
     * Test the swap method
     * @throws IOException 
     */
    public void testSwap() throws IOException {
        sorter.swap(0, 2);
        pool.flush();
    }
}
