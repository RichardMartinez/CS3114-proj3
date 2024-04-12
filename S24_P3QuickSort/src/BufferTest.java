import student.TestCase;

/**
 * This class tests the methods of the Buffer class
 * 
 * @author Richard Martinez
 * 
 * @version 2024-04-12
 */
public class BufferTest extends TestCase {

    private Buffer buf;
    private Buffer buf2;
    
    /**
     * Set up the test object
     */
    public void setUp() {
        buf = new Buffer(10);
        buf2 = new Buffer();
    }
    
    /**
     * Test the buffer class
     */
    public void testBuffer() {        
        // Test getID
        assertEquals(buf.getID(), 10);
        
        // Test set
        byte[] record = {(byte)0xAA, (byte)0xBB, (byte)0xCC, (byte)0xDD};
        buf.set(record, 0);
        
        // Test get
        byte[] space = new byte[4];
        buf.get(space, 0);
        assertEquals(space[0], (byte)0xAA);
        assertEquals(space[1], (byte)0xBB);
        assertEquals(space[2], (byte)0xCC);
        assertEquals(space[3], (byte)0xDD);
        
        // Test makeDirty
        buf.makeDirty();
        
        // Test isDirty
        assertTrue(buf.isDirty());
        
        // Test default constructor
        assertEquals(buf2.getID(), -1);
    }
}
