package chapter3.semaphore;

import java.util.Date;
import java.util.concurrent.Semaphore;
/**
 * A semaphore is a counter that protects the access to one or more shared resources.
 * When a thread wants to access one of these shared resources, first, it must acquire the
 * semaphore. If the internal counter of the semaphore is greater than 0, the semaphore
 * decrements the counter and allows access to the shared resource. A counter bigger than 0
 * means there are free resources that can be used, so the thread can access and use one
 * of them.
 * Otherwise, if the counter of the semaphore is 0, the semaphore puts the thread to sleep until
 * the counter is greater than 0. A value of 0 in the counter means all the shared resources are
 * used by other threads, so the thread that wants to use one of them must wait until one is free.
 * When the thread has finished the use of the shared resource, it must release the semaphore
 * so that the other thread can access the shared resource. That operation increases the
 * internal counter of the semaphore.
 * In this recipe, you will learn how to use the Semaphore class to implement special kinds of
 * semaphores called binary semaphores. These kinds of semaphores protect the access to a
 * unique shared resource, so the internal counter of the semaphore can only take the values
 * 1 or 0. To show how to use it, you are going to implement a print queue that can be used by
 * concurrent tasks to print their jobs. This print queue will be protected by a binary semaphore,
 * so only one thread can print at a time.
 * @author Leonardo
 *
 */
public class PrintQueue {

	private final Semaphore semaphore;
	
	public PrintQueue(){
		this.semaphore=new Semaphore(2);//可用线程资源（同时可以有两个线程访问）
	}
	
	public void printJob(Object document){
		try{
			System.out.printf("%s: PrintQueue: before Printing a Job at %s\n",Thread.currentThread().getName(),new Date().toString());
			
			this.semaphore.acquire();
			long duration=(long)(Math.random()*5);
			System.out.printf("%s: PrintQueue: Printing a Job during %d seconds at %s\n",Thread.currentThread().getName(),duration,new Date().toString());
			Thread.sleep(duration*1000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			this.semaphore.release();
		}
		System.out.printf("%s: PrintQueue: after Printing a Job at %s\n",Thread.currentThread().getName(),new Date().toString());
		
	}
	
	public static class Job implements Runnable{

		private PrintQueue queue;
		public Job(PrintQueue queue){
			this.queue=queue;
		}
		
		@Override
		public void run() {
			this.queue.printJob(new Object());
			
		}
		
	}
	
	public static void main(String[] args) {
		PrintQueue queue=new PrintQueue();
		int tnum=4;
		Thread thread[]=new Thread[tnum];
		for (int i=0; i<tnum; i++){
			thread[i]=new Thread(new Job(queue),"Thread"+i);
		}
		for (int i=0; i<tnum; i++){
			thread[i].start();
		}

	}

}
