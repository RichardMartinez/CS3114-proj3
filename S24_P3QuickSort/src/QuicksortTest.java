import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import student.TestCase;

/**
 * @author Richard Martinez
 * @version 2024-03-22
 */
public class QuicksortTest extends TestCase {

    /** Sets up the tests that follow. In general, used for initialization. */
    public void setUp() throws Exception {
        super.setUp();
        systemOut().clearHistory();
    }


    /**
     * Test the file generator
     * 
     * @throws IOException
     */
    public void testFileGen() throws IOException {
        String fname = "threeBlock.txt";
        int blocks = 3;
        FileGenerator fg = new FileGenerator(fname, blocks);
        fg.setSeed(33333333); // a non-random number to make generation
                              // deterministic
        fg.generateFile(FileType.ASCII);

        File f = new File(fname);
        long fileNumBytes = f.length();
        long calcedBytes = blocks * FileGenerator.BYTES_PER_BLOCK;
        assertEquals(calcedBytes, fileNumBytes); // size is correct!

        RandomAccessFile raf = new RandomAccessFile(f, "r");
        short firstKey = raf.readShort(); // reads two bytes
        assertEquals(8273, firstKey); // first key looks like ' Q', translates
                                      // to 8273

        raf.seek(8); // moves to byte 8, which is beginning of third record
        short thirdKey = raf.readShort();
        assertEquals(8261, thirdKey); // third key looks like ' E', translates
                                      // to 8261

        raf.close();
    }


    /**
     * Test the file checker
     * 
     * @throws Exception
     */
    public void testCheckFile() throws Exception {
        assertTrue(CheckFile.check("tinySorted.txt"));

        String fname = "checkme.txt";
        FileGenerator fg = new FileGenerator(fname, 1);
        fg.setSeed(42);
        fg.generateFile(FileType.ASCII);
        // Notice we *re-generate* this file each time the test runs.
        // That file persists after the test is over

        assertFalse(CheckFile.check(fname));

        // hmmm... maybe do some sorting on that file ...
        // then we can do:
        // assertTrue(CheckFile.check(fname));

        String[] args = new String[3];
        args[0] = fname;
        args[1] = "1";
        args[2] = "stats.txt";
        Quicksort.main(args);

        assertTrue(CheckFile.check(fname));
    }


    /**
     * This method is a demonstration of the file generator and file checker
     * functionality. It calles generateFile to create a small binary file. It
     * then calls the file checker to see if it is sorted (presumably not since
     * we don't call a sort method in this test, so we assertFalse).
     *
     * @throws Exception
     *             either a IOException or FileNotFoundException
     */
    public void testSorting() throws Exception {
        String fname = "input.bin";
        FileGenerator fg = new FileGenerator(fname, 1);
        fg.generateFile(FileType.BINARY);

        assertFalse(CheckFile.check(fname)); // file shouldn't be sorted

        String[] args = new String[3];
        args[0] = fname; // the file to be sorted.
        args[1] = "1"; // number of buffers, can impact performance
        args[2] = "stats.txt"; // filename for sorting stats
        Quicksort.main(args);
        // Now the file *should* be sorted, so lets check!

        // TODO: In a real test, the following should work:
        assertTrue(CheckFile.check(fname));
    }


    /**
     * Test sorting 10 blocks with QuickSort
     * 
     * @throws Exception
     */
    public void testSortingTenBlocks() throws Exception {
        String fname = "tenBlocksQS.bin";
        int blocks = 10;
        FileGenerator fg = new FileGenerator(fname, blocks);
        fg.generateFile(FileType.BINARY);

        assertFalse(CheckFile.check(fname));

        String[] args = new String[3];
        args[0] = fname;
        args[1] = "10";
        args[2] = "stats.txt";
        Quicksort.main(args);

        assertTrue(CheckFile.check(fname));
    }


    /**
     * Test sorting 5 blocks with QuickSort
     * 
     * @throws Exception
     */
    public void testSortingFiveBlocksTwoBuffers() throws Exception {
        String fname = "fiveBlocksQS.bin";
        int blocks = 5;
        FileGenerator fg = new FileGenerator(fname, blocks);
        fg.generateFile(FileType.BINARY);

        assertFalse(CheckFile.check(fname));

        String[] args = new String[3];
        args[0] = fname;
        args[1] = "2";
        args[2] = "stats.txt";
        Quicksort.main(args);

        assertTrue(CheckFile.check(fname));
    }

}
