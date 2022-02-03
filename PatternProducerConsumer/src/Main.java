/*
Это низкоуровневая реализация паттерна ProducerConsumer

Примечание:
Цикл while на проверке используется потому что мы дважды проверяем, когда потоку отдали монитор, точно ли у нас очередь пуста/полна.
в отличие от if, который проверет лишь однажды. и в случае полноты или пустоты у нас выброситься исклчюение
 */

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer();

        Thread thread1 = new Thread(() -> {
            try {
                pc.producer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                pc.consumer();
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
}

class ProducerConsumer {

    private Queue<Integer> q1 = new LinkedList<>();
    private Random random = new Random();
    private final Object lock = new Object(); // по этому объекту синхронизируемся
    private final int LIMIT = 10;


    public void producer() throws InterruptedException {
        while (true) {
            synchronized (lock) {
                while (q1.size() == LIMIT) {
                    lock.wait();        // ждем когда нам отдадут монитор
                }
                q1.add(random.nextInt(100)); // добавляем в очередь значение
                lock.notify();          // отдаем монитор
            }
        }
    }


    public void consumer() throws InterruptedException {
        while (true) {
            synchronized (lock) {
                while (q1.size() == 0) {
                    lock.wait();        // ждем когда нам отдадут монитор
                }
                System.out.println(q1.poll());  // берем и печатем значение из очереди
                lock.notify();          // отдаем монитор
            }
            Thread.sleep(1000);
        }
    }
}
