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
        byte[] record = { (byte)0xAA, (byte)0xBB, (byte)0xCC, (byte)0xDD };
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

        // Test getData
        byte[] data = new byte[4096];
        data = buf.getData();

        assertEquals(data[0], (byte)0xAA);
        assertEquals(data[1], (byte)0xBB);
        assertEquals(data[2], (byte)0xCC);
        assertEquals(data[3], (byte)0xDD);

        for (int i = 4; i < 4096; i++) {
            assertEquals(data[i], 0);
        }

        // Test setData
        byte[] data2 = new byte[4096];
        data2[4] = (byte)10;
        data2[5] = (byte)11;
        data2[6] = (byte)12;
        data2[7] = (byte)13;

        buf.setData(data2);
        data = buf.getData();

        assertEquals(data[0], 0);
        assertEquals(data[1], 0);
        assertEquals(data[2], 0);
        assertEquals(data[3], 0);

        assertEquals(data[4], (byte)10);
        assertEquals(data[5], (byte)11);
        assertEquals(data[6], (byte)12);
        assertEquals(data[7], (byte)13);
    }
}
