package chapter2.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * The constructor of the ReentrantLock and ReentrantReadWriteLock classes admits
a boolean parameter named fair that allows you to control the behavior of both classes.
<b>The false value is the default value and it's called the non-fair mode</b>. In this mode, when
there are some threads waiting for a lock (ReentrantLock or ReentrantReadWriteLock)
and the lock has to select one of them to get the access to the critical section, it selects one
without any criteria. The true value is called the fair mode. In this mode, when there are
some threads waiting for a lock (ReentrantLock or ReentrantReadWriteLock) and the
lock has to select one to get access to a critical section, it selects the thread that has been
waiting for the most time. Take into account that the behavior explained previously is only
used with the lock() and unlock() methods. As the tryLock() method doesn't put the
thread to sleep if the Lock interface is used, the fair attribute doesn't affect its functionality.
 * @author Leonardo
 *
 */
public class PrintQueue {

	private final Lock queueLock=new ReentrantLock(true);//default false
	public void printJob(Object document){
		//System.out.println(Thread.currentThread().getName()+"PrintQueue:before Printing.out of lock");
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
			//System.out.printf("%s: Going to print a document\n", Thread.currentThread().getName());
			printQueue.printJob(new Object());
			//System.out.printf("%s: The document has been printed\n",Thread.currentThread().getName());
		}   
		
	}
	public static void main(String[] args){
		PrintQueue queue=new PrintQueue();
		Thread[] thread=new Thread[4];
		for(int i=0;i<4;i++){
			thread[i]=new Thread(new Job(queue),"thread-"+i);
		}
		for(int i=0;i<4;i++){
			thread[i].start();
		}
	}
}
