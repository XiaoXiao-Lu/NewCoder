package com.kinnon;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Kinnon
 * @create 2022-08-14 22:05
 */

public class BlockingQueueTest {

    public static void main(String[] args) {
        BlockingQueue blockingQueue = new ArrayBlockingQueue(10);
        Thread thread1 = new Thread(new Producer(blockingQueue));
        thread1.setName("生产者");
        Thread thread2 = new Thread(new Comsumer(blockingQueue));
        thread2.setName("消费者1");
        Thread thread3 = new Thread(new Comsumer(blockingQueue));
        thread3.setName("消费者2");
        thread1.start();
        thread2.start();
        thread3.start();
    }
}

class Producer implements Runnable{

    private BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(30);
                queue.put(i);
                System.out.println(Thread.currentThread().getName() + "生产:" + queue.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Comsumer implements Runnable{

    private BlockingQueue<Integer> queue;

    public Comsumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(new Random().nextInt(1000));
                Integer poll = queue.take();
                System.out.println(Thread.currentThread().getName() + "消费:" + queue.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}