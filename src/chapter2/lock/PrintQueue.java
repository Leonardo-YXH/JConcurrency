package chapter2.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * lock
 * @author Leonardo
 *
 */
public class PrintQueue {

	private final Lock queueLock=new ReentrantLock();
	public void printJob(Object document){
		System.out.println(Thread.currentThread().getName()+"PrintQueue:before Printing.out of lock");
		//lock the critical section
		queueLock.lock();
		try{
			Long duration=(long) (Math.random()*10000);
			System.out.println(Thread.currentThread().getName()+"PrintQueue:Printing a Job during "+(duration/1000)+" seconds");
			Thread.sleep(duration);
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			queueLock.unlock();
		}
	}
	public static class Job implements Runnable{
		private PrintQueue printQueue;
		public Job(PrintQueue printQueue){
			this.printQueue=printQueue;
		}
		@Override
		public void run() {
			System.out.printf("%s: Going to print a document\n", Thread.
					currentThread().getName());
					printQueue.printJob(new Object());
			System.out.printf("%s: The document has been printed\n",
					Thread.currentThread().getName());
		}   
		
	}
	public static void main(String[] args){
		PrintQueue queue=new PrintQueue();
		Thread[] thread=new Thread[10];
		for(int i=0;i<10;i++){
			thread[i]=new Thread(new Job(queue),"thread-"+i);
		}
		for(int i=0;i<10;i++){
			thread[i].start();
		}
	}
}
