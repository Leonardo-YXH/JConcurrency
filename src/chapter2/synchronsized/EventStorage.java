package chapter2.synchronsized;

import java.util.Date;
import java.util.LinkedList;

/**
 * synchronsized
 * @author Leonardo
 *
 */
public class EventStorage {

	private int maxSize;
	private LinkedList<Date> storage;
	public EventStorage(){
		this.maxSize=10000;
		this.storage=new LinkedList<Date>();
	}
	public synchronized void set(){
		while(storage.size()==maxSize){
			try{
				wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		storage.offer(new Date());
		System.out.println("set:"+storage.size());
		notifyAll();
	}
	public synchronized void get(){
		while(storage.size()==0){
			try{
				wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		System.out.println("get:"+storage.size()+storage.poll());
		notifyAll();
	}
	
	/**
	 * 
	 * @author Administrator
	 *
	 */
	public static class Producer implements Runnable{
		private EventStorage storage;
		public Producer(EventStorage storage){
			this.storage=storage;
		}
		@Override
		public void run() {
			for(int i=0;i<100;i++){
				this.storage.set();
				this.storage.set();
			}
		}
		
	}
	/**
	 * 
	 * @author Administrator
	 *
	 */
	public static class Consumer implements Runnable{
		private EventStorage storage;
		public Consumer(EventStorage storage){
			this.storage=storage;
		}
		@Override
		public void run() {
			for(int i=0;i<100;i++){
				this.storage.get();
				//this.storage.get();
			}
		}
		
	}
	
	public static void main(String[] args){
		EventStorage storage=new EventStorage();
		
		Thread a=new Thread(new Producer(storage), "producer");
		Thread b=new Thread(new Consumer(storage), "consumer");
		a.start();
		b.start();
	}
	
}
