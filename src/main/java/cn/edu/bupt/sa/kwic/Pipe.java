package cn.edu.bupt.sa.kwic;

import java.io.IOException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Pipe<E> {
    protected Queue<E> pipeBuffer = new LinkedList<E>();

    private boolean canWrite = true;
    private boolean canRead = true;

    /**
     * Write
     * @param object
     * @return
     */
    public synchronized boolean put(E object) {
        if (!canWrite)
            throw new RuntimeException(new IOException("Pipe has been closed"));

        boolean result = pipeBuffer.add(object);
        notify();
        return result;
    }

    /**
     * Read
     * @Param object
     * @return
     */
    public synchronized E get() throws InterruptedException {
        if (!canRead)
            throw new NoSuchElementException("Pipe is closed and empty");

        while (pipeBuffer.isEmpty())
            wait();

        E object = pipeBuffer.remove();
        if (object == null)
            canRead = false;
        return object;
    }

    /**
     * Closed the pipe for write operation and clear buffer.
     */
    public synchronized void close() {
        canWrite = false;
        pipeBuffer.add(null);
        notify();
    }

    /**
     * Open the pipe for read and write operations and clear buffer.
     */
    public synchronized void open() {
        pipeBuffer.clear();
        canWrite = true;
        canRead = true;
        notify();
    }
}