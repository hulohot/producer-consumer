import java.io.Console;
import java.nio.Buffer;
import java.util.concurrent.Semaphore;
import java.util.*;
import java.lang.InterruptedException;

import sun.awt.Mutex;

/*  @author: Ethan Brugger
*   @uaid  : 010773000
*   @class : CSCE 3613
*/

public class producerConsumer {
    BufferItem buffer;
    int sleepTime = 500;
    int maxRandomNum = 10;

    producerConsumer(BufferItem bufferList) {
        buffer = bufferList;
    }

    void producer() {

        int item;

        while (true) {
            try {
                Thread.sleep((long) (Math.random() * sleepTime) + 1);
            } catch (Exception e) {
                System.out.println(e);
            }
            item = (int) (Math.random() * maxRandomNum) + 1;

            if (!buffer.insertItem(item)) {
                System.out.println("Failed to Produce");
            } else {
                buffer.insertItem(item);
                System.out.println("Producer Produced " + item);
            }
        }
    }

    void consumer() {

        int item;

        while (true) {
            try {
                Thread.sleep((long) (Math.random() * sleepTime) + 1);
            } catch (Exception e) {
                System.out.println(e);
            }

            if (buffer.removeItem() == -1) {
                System.out.println("Failed to Consume");
            } else {
                item = buffer.removeItem();
                System.out.println("Consumer Consumed " + item);
            }
        }
    }

    public static void main(String[] args) {
        // Get command line arguments (cli)
        int sleepTime = Integer.parseInt(args[0]);
        int numProducerThreads = Integer.parseInt(args[1]);
        int numConsumerThreads = Integer.parseInt(args[2]);

        // Output cli arguments
        System.out.println("Sleep Time = " + sleepTime);
        System.out.println("Producer threads = " + numProducerThreads);
        System.out.println("Consumer threads = " + numConsumerThreads);

        // Initialize buffer
        BufferItem buffer = new BufferItem();

        // Create producer threads
        producerConsumer producer;
        for (int i = 0; i < numProducerThreads; i++) {
            producer = new producerConsumer(buffer);
            for (int j = 0; j < 100; j++) {
                producer.producer();
            }
        }

        // Create consumer threads
        producerConsumer consumer = new producerConsumer(buffer);
        for (int i = 0; i < numConsumerThreads; i++) {
            consumer = new producerConsumer(buffer);
            for (int j = 0; j < 100; j++) {
                consumer.consumer();
            }
        }

        // Sleep
        try {
            Thread.sleep((long) sleepTime);
        } catch (Exception e) {
            // TODO: handle exception
        }

        // Exit
        System.exit(0);
    }
}

class BufferItem {

    Semaphore empty;
    Semaphore full;
    Semaphore mutex;

    int bufferSize = 5;
    int in; // Next FREE position in buffer
    int out; // Next FULL position in buffer
    Integer[] buffer;

    BufferItem() {
        empty = new Semaphore(bufferSize);
        full = new Semaphore(0);
        mutex = new Semaphore(1);

        buffer = new Integer[bufferSize];
        in = 0;
        out = 0;
    }

    boolean insertItem(int bufferItem) {
        if ((in + 1) % bufferSize == 0)
            return false;
        else {
            buffer[in] = bufferItem;

            if (in < bufferSize - 2)
                in++;
            else
                in = 0;

            return true;
        }
    }

    int removeItem() {
        if (in == out)
            return -1;
        else {
            int item = buffer[out];
            buffer[out] = 0;
            for (int i = 0; i < bufferSize; i++) {
                if (buffer[i] != 0) {
                    out = i;
                    break;
                }
            }
            return item;
        }
    }
}