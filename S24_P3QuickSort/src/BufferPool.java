import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class represents the BufferPool
 * Internally, it holds a list of buffers
 * 
 * @author Richard Martinez
 * 
 * @version 2024-04-12
 */
public class BufferPool {
    // 1 buffer = 4096 bytes = 1024 records
    /**
     * The size of a block in bytes
     */
    public static final int BLOCK_SIZE_BYTES = 4096;

    /**
     * The size of a record in bytes
     */
    public static final int RECORD_SIZE_BYTES = 4;

    // List of buffers
    private Buffer[] buffers;

    // Number of buffers available
    private int numBuffers;

    // RandomAccessFile to read and write
    private RandomAccessFile file;

    // Stats variables
    private int cacheHits;

    private int diskReads;

    private int diskWrites;

    /**
     * Constructor for BufferPool
     * 
     * @param file
     *            The RAF to read and write
     * @param numBuffers
     *            The number of buffers for this pool
     * @throws IOException
     */
    public BufferPool(RandomAccessFile file, int numBuffers) {
        this.numBuffers = numBuffers;
        this.file = file;

        // Init buffer list
        this.buffers = new Buffer[numBuffers];

        // Init all with default constructor
        for (int i = 0; i < numBuffers; i++) {
            buffers[i] = new Buffer();
        }

        this.cacheHits = 0;
        this.diskReads = 0;
        this.diskWrites = 0;
    }


    /**
     * Reads the record stored at virtualAddress.
     * The buffer pool will determine which buffer it is in,
     * then act accordingly.
     * 
     * @param record
     *            The 4 byte long array where the record is returned
     * @param virtualIndex
     *            The virtual index to read from
     * @throws IOException
     */
    public void readRecord(byte[] record, int virtualIndex) throws IOException {
        // Virtual address should be a multiple of 4
        // Determine where the virtualAddress maps to
        int virtualAddress = virtualIndex * RECORD_SIZE_BYTES;
        int[] mapped = mapVirtualAddress(virtualAddress);
        int blockID = mapped[0];
        int offset = mapped[1];

        // Check if buffer with that blockID is in pool
        Buffer buf = findBuffer(blockID);

        if (buf == null) {
            // Not in pool
            // Do something about it

            // Is there space?
            if (isFull()) {
                // Need to evict to make space, then
                // let below code cover it
                removeLRU();
            }

            // Not in pool, but YES we have space

            // Generate new buffer with that blockID
            Buffer newBuf = new Buffer(blockID);

            // Read from the file into the new buffer
            readFromFile(newBuf);

            // Place buffer at front of list
            placeFront(newBuf);

            // Read record into record array
            newBuf.get(record, offset);
            return;
        }

        // The block was in the pool
        buf.get(record, offset);
        moveToFront(blockID);
        cacheHits++;
    }


    /**
     * Writes the given record into virtual address.
     * Buffer pool will act accordingly.
     * 
     * @param record
     *            The record to write
     * @param virtualIndex
     *            The virtual index to write to
     * @throws IOException
     */
    public void writeRecord(byte[] record, int virtualIndex)
        throws IOException {
        // Virtual address should be a multiple of 4
        // Determine where the virtualAddress maps to
        int virtualAddress = virtualIndex * RECORD_SIZE_BYTES;
        int[] mapped = mapVirtualAddress(virtualAddress);
        int blockID = mapped[0];
        int offset = mapped[1];

        // Check if buffer with that blockID is in pool
        Buffer buf = findBuffer(blockID);

        if (buf == null) {
            // Not in buffer pool

            // Is there space?
            if (isFull()) {
                // Need to evict to make space, then
                // let below code cover it
                removeLRU();
            }

            // Generate new buffer with that blockID
            Buffer newBuf = new Buffer(blockID);

            // Read from the file into the new buffer
            readFromFile(newBuf);

            // Place buffer at front of list
            placeFront(newBuf);

            newBuf.set(record, offset);
            return;
        }

        // It was in the pool
        buf.set(record, offset);
        moveToFront(blockID);
        cacheHits++;
    }


    /**
     * Read from the file and place the data inside buf
     * 
     * @param buf
     *            The buffer to read into
     * @throws IOException
     */
    public void readFromFile(Buffer buf) throws IOException {
        // Point RAF to correct starting location
        file.seek(buf.getID() * BLOCK_SIZE_BYTES);

        // Read into the buffer data
        file.read(buf.getData());

        diskReads++;
    }


    /**
     * Write the contents of the buffer to the file
     * 
     * @param buf
     *            The buffer to write
     * @throws IOException
     */
    public void writeToFile(Buffer buf) throws IOException {
        // Point RAF to correct starting location
        file.seek(buf.getID() * BLOCK_SIZE_BYTES);

        // Write the file
        file.write(buf.getData());

        diskWrites++;
    }


    /**
     * Maps a Virtual Address to a block ID and offset within that blocks
     * 
     * @param virtualAddress
     *            The virtual address to map
     * @return an int array, 0 index is blockID, 1 index is offset
     */
    public int[] mapVirtualAddress(int virtualAddress) {
        int[] mapped = new int[2];

        // Block ID
        mapped[0] = virtualAddress / BLOCK_SIZE_BYTES;

        // Block Offset within that block
        mapped[1] = virtualAddress % BLOCK_SIZE_BYTES;

        return mapped;
    }


    /**
     * If requested buffer is in pool, return it
     * Else return null
     * 
     * @param blockID
     *            The ID to check for
     * @return The requested buffer or null
     */
    public Buffer findBuffer(int blockID) {
        for (int i = 0; i < numBuffers; i++) {
            Buffer buf = buffers[i];
            if (buf.getID() == blockID) {
                return buf;
            }
        }
        return null;
    }


    /**
     * Place the buffer at the start of the list
     * 
     * @param buf
     *            The new buffer to add
     * @throws IOException
     */
    public void placeFront(Buffer buf) throws IOException {
        // Remove LRU
        // No need, assume there is space at this point
        // removeLRU();

        // Shift everything down
        for (int i = numBuffers - 2; i >= 0; i--) {
            buffers[i + 1] = buffers[i];
        }

        // Place at buffers[0]
        buffers[0] = buf;
    }


    /**
     * Removes the least recently used buffer
     * Write to memory if LRU is dirty
     * 
     * @return the LRU buffer
     * @throws IOException
     */
    public Buffer removeLRU() throws IOException {
        // Remove last item, write if dirty
        int indexLRU = numBuffers - 1;
        Buffer bufLRU = buffers[indexLRU];

        if (bufLRU.getID() < 0) {
            // Empty
            // Do nothing
            return bufLRU;
        }

        // It wasn't empty!
        if (bufLRU.isDirty()) {
            writeToFile(bufLRU);
        }
        return bufLRU;
    }


    /**
     * Flush the buffer pool back to the disk
     * 
     * @throws IOException
     */
    public void flush() throws IOException {
        for (int i = 0; i < numBuffers; i++) {
            Buffer buf = buffers[i];
            if (buf.isDirty()) {
                writeToFile(buf);
            }
        }
    }


    /**
     * Returns true if full
     * 
     * @return true if full
     */
    public boolean isFull() {
        // Only full is last buffer is has id >= 0
        int lastBufIndex = numBuffers - 1;
        Buffer lastBuf = buffers[lastBufIndex];

        return lastBuf.getID() >= 0;
    }


    /**
     * Move the buffer with the specified blockID to the
     * top if it exists in the pool
     * 
     * @param blockID
     *            The block ID to move
     */
    public void moveToFront(int blockID) {
        // Find if buffer exists, get its index for shifting
        Buffer buf = null;
        int index;
        for (index = 0; index < numBuffers; index++) {
            if (buffers[index].getID() == blockID) {
                buf = buffers[index];
                break;
            }
        }

        if (buf == null) {
            // Not in the pool
            return;
        }

        // At this point, we have the index and the buffer

        // Shift down everything above it
        for (int i = index - 1; i >= 0; i--) {
            buffers[i + 1] = buffers[i];
        }

        // Place the found buffer at index 0
        buffers[0] = buf;
    }


    /**
     * Get the number of cache hits
     * 
     * @return the number of hits
     */
    public int getCacheHits() {
        return cacheHits;
    }


    /**
     * Get the number of disk reads
     * 
     * @return the number of disk reads
     */
    public int getDiskReads() {
        return diskReads;
    }


    /**
     * Get the number of disk writes
     * 
     * @return the number of disk writes
     */
    public int getDiskWrites() {
        return diskWrites;
    }

}
