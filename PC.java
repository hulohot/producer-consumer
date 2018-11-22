import java.util.*;
import java.util.concurrent.Semaphore;

public class PC extends Thread {

    String type;
    Random rand = new Random();
    int randomSeed = 10000;

    PC(String type) {
        this.type = type;
    }

    @Override
    public void run() {
        do {
            double sleepTime = Math.random() * (500) + 1;
            int num = rand.nextInt(randomSeed);
            try {
                if (type.equals("producer")) {
                    Buffer.insert(num);
                    System.out.println("Producer produced: " + num);
                } else {
                    int item = Buffer.remove();
                    System.out.println("\tConsumer Consumed: " + item);
                }
                Thread.sleep((long) sleepTime);

            } catch (Exception e) {
                // TODO: handle exception
            }
        } while (true);
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

        // Create producer/consumer threads
        PC[] producers = new PC[numProducerThreads + 1];
        PC[] consumers = new PC[numConsumerThreads + 1];

        for (int i = 0; i < numProducerThreads; i++) {
            producers[i] = new PC("producer");
            producers[i].start();
        }

        for (int i = 0; i < numConsumerThreads; i++) {
            consumers[i] = new PC("consumer");
            consumers[i].start();
        }

        // Sleep
        try {
            Thread.sleep((long) sleepTime * 1000);
        } catch (Exception e) {
            // TODO: handle exception
        }

        // Exit
        System.exit(0);

    }
}

class Buffer {

    public static final int BUFFER_SIZE = 5;

    public static Semaphore mutex = new Semaphore(1);
    public static Semaphore empty = new Semaphore(BUFFER_SIZE);
    public static Semaphore full = new Semaphore(0);

    public static int count = 0;
    public static int in = 0;
    public static int out = 0;
    public static int[] buffer = new int[BUFFER_SIZE];;

    public Buffer() {
    }

    // producer calls this method
    public static void insert(int item) {
        try {
            full.acquire();
            mutex.acquire();

            // add an item to the buffer
            ++count;
            buffer[in] = item;
            in = (in + 1) % BUFFER_SIZE;

            // if (count == BUFFER_SIZE)
            // System.out.println("Producer Entered " + item + " Buffer FULL");
            // else
            // System.out.println("Producer Entered " + item + " Buffer Size = " + count);

            mutex.release();
            empty.release();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // consumer calls this method
    public static int remove() {
        int item = 0;
        try {
            empty.acquire();
            mutex.acquire();

            // remove an item from the buffer
            --count;
            item = buffer[out];
            out = (out + 1) % BUFFER_SIZE;

            // if (count == 0)
            // System.out.println("Consumer Consumed " + item + " Buffer EMPTY");
            // else
            // System.out.println("Consumer Consumed " + item + " Buffer Size = " + count);

            mutex.release();
            full.release();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return item;
    }

    void print() {

    }
}