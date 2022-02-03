/*
Это высокоуровневая реализация паттерна ProducerConsumer. deadblock не возникнет. потоки безопастны.
*/

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerHiLVL {
    private static final int limit = 10;
    private static BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(limit);

    public static void main(String[] args) {

        Thread thread1 = new Thread(() -> {
            try {
                producer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                consumer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void producer() throws InterruptedException {
        while (true) {
            blockingQueue.put(new Random().nextInt(100));
        }
    }

    public static void consumer() throws InterruptedException {
        while (true) {
            System.out.println(blockingQueue.take());
            Thread.sleep(100);
        }

    }
}
