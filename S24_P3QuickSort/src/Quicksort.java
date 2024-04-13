/**
 * TODO: {Project Description Here}
 */

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
        System.out.println("This is working QuickSort!");
        System.out.println("Implement project here");
        
        if (args.length != 3) {
            System.out.println("Invalid number of args");
            return;
        }
        
        // Parse Input Args
        String dataFileName = args[0];
        int numBuffers = Integer.parseInt(args[1]);
        String statFileName = args[2];
        
        // TODO: Run QuickSort on dataFileName
        // TODO: Keep track of stats file
        
        RandomAccessFile file = new RandomAccessFile(dataFileName, "rw");
        BufferPool pool = new BufferPool(file, numBuffers);
        Sorter sorter = new Sorter(pool);
        
        int numRecords = (int) (file.length() / 4);
        sorter.sort(0, numRecords-1);
        pool.flush();
        
    }
    
    
}
