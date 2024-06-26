
/**
 * Project 3 Virtual QuickSort
 * 
 * Compiled using JRE 11 for CS 3114
 * Operating System: Windows 11
 * IDE: Eclipse
 * Date Completed: 04-13-2024
 * Created By: Richard Martinez
 * 
 * In this project, I implemented a QuickSort algorithm
 * that has been modified to use Virtual Memory.
 * This means that from the point of view of the sorter,
 * there is infinite memory. In reality, all requests from
 * the virtual memory are mapped and maintained using a
 * caching data structure called a BufferPool. Using the
 * least recently used technique, the BufferPool is able
 * to provide the illusion of memory to the QuickSort.
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * The class containing the main method.
 *
 * @author Richard Martinez
 * @version 2024-03-22
 */

// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

public class Quicksort {

    /**
     * @param args
     *            Command line parameters. See the project spec!!!
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // This is the main file for the program.
// System.out.println("This is working QuickSort!");
// System.out.println("Implement project here");

// if (args.length != 3) {
// System.out.println("Invalid number of args");
// return;
// }

        // Parse Input Args
        String dataFileName = args[0];
        int numBuffers = Integer.parseInt(args[1]);
        String statFileName = args[2];

        RandomAccessFile file = new RandomAccessFile(dataFileName, "rw");
        BufferPool pool = new BufferPool(file, numBuffers);
        Sorter sorter = new Sorter(pool);

        int numRecords = (int)(file.length() / 4);

        long start = System.currentTimeMillis();
        sorter.sort(0, numRecords - 1);
        pool.flush();
        long finish = System.currentTimeMillis();

        // Write the stats to a file
        // dataFileName
        // cacheHits
        // diskReads
        // diskWrites
        // amount of time
        long runTimeMS = finish - start;
        int cacheHits = pool.getCacheHits();
        int diskReads = pool.getDiskReads();
        int diskWrites = pool.getDiskWrites();

        int numBlocks = numRecords / 1024;

        // Write stats to file
        try {
            FileWriter statsFile = new FileWriter(statFileName, true);

            String output = "Filename: " + dataFileName + "\n" + "Num Blocks: "
                + numBlocks + "\n" + "Num Buffers: " + numBuffers + "\n"
                + "Cache Hits: " + cacheHits + "\n" + "Disk Reads: " + diskReads
                + "\n" + "Disk Writes: " + diskWrites + "\n" + "Run Time: "
                + runTimeMS + " ms \n\n";

            statsFile.write(output);
            statsFile.close();
        }
        catch (Exception e) {
            System.out.println("Error writing stats to file");
        }

    }

}
