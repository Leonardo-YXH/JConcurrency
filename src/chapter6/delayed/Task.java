package chapter6.delayed;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

public class Task implements Runnable {

	private int id;
	private DelayQueue<Event> queue;
	public Task(int id,DelayQueue<Event> queue) {
		this.id=id;
		this.queue=queue;
	}
	@Override
	public void run() {
		Date now=new Date();
		Date delay=new Date();
		delay.setTime(now.getTime()+(id*1000));
		System.out.printf("Thread %s: %s\n",id,delay);
		for (int i=0; i<10; i++) {
			Event event=new Event(new Date(delay.getTime()+i*10));
			queue.add(event);
		}
	}

	public static void main(String[] args) {
		int size=5;
		DelayQueue<Event> queue=new DelayQueue<Event>();
		Thread[] threads=new Thread[size];
		for(int i=0;i<size;i++){
			Task task=new Task(i+1, queue);
			threads[i]=new Thread(task);
		}
		for(int i=0;i<size;i++){
			threads[i].start();
		}
		for(int i=0;i<size;i++){
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		do {
			int counter=0;
			Event event;
			do {
				event=queue.poll();
				if (event!=null) counter++;
			} while (event!=null);
			System.out.printf("At %s you have read %d events\n",new
			Date(),counter);
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (queue.size()>0);
	}
}
