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
     * 
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
     * Test the sort method
     * 
     * @throws Exception
     */
    public void testSort() throws Exception {
        int numRecords = 1024;
        sorter.sort(0, numRecords - 1);
        pool.flush();

        assertTrue(CheckFile.check("oneBlock.txt"));
    }


    /**
     * Test one block bin sort
     * 
     * @throws Exception
     */
    public void testBinSortOneBlock() throws Exception {
        String fname = "oneBlock.bin";

        // Generate oneBlock.bin
        FileGenerator fg;
        fg = new FileGenerator(fname, 1);
        fg.setSeed(33333333);
        fg.generateFile(FileType.BINARY);

        RandomAccessFile file = new RandomAccessFile(fname, "rw");
        pool = new BufferPool(file, 1);
        sorter = new Sorter(pool);

        int numRecords = 1024;
        sorter.sort(0, numRecords - 1);
        pool.flush();

        assertTrue(CheckFile.check(fname));
    }


    /**
     * Test multiple blocks
     * 
     * @throws Exception
     */
    public void testASCIISortMoreBlocks() throws Exception {
        String fname = "moreBlocks.txt";
        int blocks = 10;

        // Generate moreBlocks.txt
        FileGenerator fg;
        fg = new FileGenerator(fname, blocks);
        fg.setSeed(33333333);
        fg.generateFile(FileType.ASCII);

        RandomAccessFile file = new RandomAccessFile(fname, "rw");
        pool = new BufferPool(file, 10);
        sorter = new Sorter(pool);

        int numRecords = (int)(file.length() / 4);

        sorter.sort(0, numRecords - 1);
        pool.flush();

        assertTrue(CheckFile.check(fname));

    }

    /**
     * Test 1000 blocks, 10 buffers
     */
// public void testThousandBlocksTenBuffs() throws Exception {
// String fname = "thousandBlocks.txt";
// int blocks = 1000;
// int numBuffers = 10;
//
// // Generate thousandBlocks.txt
// FileGenerator fg;
// fg = new FileGenerator(fname, blocks);
// fg.setSeed(33333333);
// fg.generateFile(FileType.ASCII);
//
// RandomAccessFile file = new RandomAccessFile(fname, "rw");
// pool = new BufferPool(file, numBuffers);
// sorter = new Sorter(pool);
//
// int numRecords = (int) (file.length() / 4);
//
// System.out.println(numRecords);
//
// sorter.sort(0, numRecords - 1);
// pool.flush();
//
// assertTrue(CheckFile.check(fname));
// }

    /**
     * Test higher blocks than buffers
     * 
     * TODO: THROWING STACK OVERFLOW!!!
     * 
     * @throws Exception
     */
// public void testHigherBlocksThanBuffers() throws Exception {
// String fname = "thousandBlocks.txt";
// int blocks = 129;
// int numBuffers = 100;
//
// // Generate thousandBlocks.txt
// FileGenerator fg;
// fg = new FileGenerator(fname, blocks);
// fg.setSeed(33333333);
// fg.generateFile(FileType.ASCII);
//
// RandomAccessFile file = new RandomAccessFile(fname, "rw");
// pool = new BufferPool(file, numBuffers);
// sorter = new Sorter(pool);
//
// int numRecords = (int)(file.length() / 4);
//
// System.out.println(numRecords);
//
// sorter.sort(0, numRecords - 1);
// pool.flush();
//
// assertTrue(CheckFile.check(fname));
// }


    /**
     * Test Insertion sort method
     */
    public void testInsSort() throws Exception {
        String fname = "insertionsort.txt";
        int blocks = 1;
        int numBuffers = 1;

        // Generate thousandBlocks.txt
        FileGenerator fg;
        fg = new FileGenerator(fname, blocks);
        fg.setSeed(33333333);
        fg.generateFile(FileType.ASCII);

        RandomAccessFile file = new RandomAccessFile(fname, "rw");
        pool = new BufferPool(file, numBuffers);
        sorter = new Sorter(pool);

        int numRecords = (int)(file.length() / 4);

        System.out.println(numRecords);

        sorter.insertionSort(0, numRecords - 1);
        pool.flush();

        assertTrue(CheckFile.check(fname));
    }
}
