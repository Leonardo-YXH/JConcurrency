package chapter2.lock;

import java.util.Date;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * read & write lock
 * @author Leonardo
 *
 */
public class PricesInfo {

	private double price1;
	private double price2;
	private ReadWriteLock lock;
	public PricesInfo(){
		this.price1=1;
		this.price2=2;
		this.lock=new ReentrantReadWriteLock();
	}
	
	public double getPrice1() {
		System.out.println("before readlock for price1:"+new Date().toString());
		lock.readLock().lock();
		System.out.println("enter readlock for price1:"+new Date().toString());
		double value=price1;
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lock.readLock().unlock();
		System.out.println("end readlock for price1:"+new Date().toString());
		return value;
	}
	
	public double getPrice2() {
		System.out.println("before readlock for price2:"+new Date().toString());
		lock.readLock().lock();
		System.out.println("enter readlock for price2:"+new Date().toString());
		double value=price2;
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lock.readLock().unlock();
		System.out.println("end readlock for price2:"+new Date().toString());
		return value;
	}
	public double[] getPrices() {
		System.out.println(Thread.currentThread().getName()+"==before readlock for prices:"+new Date().toString());
		lock.readLock().lock();
		System.out.println(Thread.currentThread().getName()+"==enter readlock for prices:"+new Date().toString());
		double value1=price1;
		double value2=price2;
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lock.readLock().unlock();
		System.out.println(Thread.currentThread().getName()+"==end readlock for prices:"+new Date().toString());
		return new double[]{value1,value2};
	}
	
	public void setPrices(double price1, double price2) {
		System.out.println(Thread.currentThread().getName()+"==before writelock for prices:"+new Date().toString());
		lock.writeLock().lock();
		System.out.println(Thread.currentThread().getName()+"==enter writelock for prices:"+new Date().toString());
		this.price1=price1;
		this.price2=price2;
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lock.writeLock().unlock();
		System.out.println(Thread.currentThread().getName()+"==end writelock for prices:"+new Date().toString());
	}
	
	
	
	public static class Reader implements Runnable{
		
		private PricesInfo pricesInfo;
		public Reader (PricesInfo pricesInfo){
			this.pricesInfo=pricesInfo;
		}
		@Override
		public void run() {
			/*for (int i=0; i<10; i++){
				System.out.printf("%s: Price 1: %f\n", Thread.
				currentThread().getName(),pricesInfo.getPrice1());
				System.out.printf("%s: Price 2: %f\n", Thread.
				currentThread().getName(),pricesInfo.getPrice2());
			}*/
			
			//System.out.printf("%s:start getting Price at %s\n", Thread.currentThread().getName(),new Date().toString());
			
			double[] prices=pricesInfo.getPrices();
			System.out.printf("%s: Price 1: %f\n", Thread.currentThread().getName(),prices[0]);
			//System.out.printf("%s: Price 2: %f\n", Thread.currentThread().getName(),prices[1]);
			
			
			//System.out.printf("%s:end getting Price at %s\n", Thread.currentThread().getName(),new Date().toString());
		}
		
	}
	
	public static class Writer implements Runnable{
		
		private PricesInfo pricesInfo;
		public Writer (PricesInfo pricesInfo){
			this.pricesInfo=pricesInfo;
		}
		@Override
		public void run() {
			/*for (int i=0; i<3; i++) {
				System.out.printf("Writer: Attempt to modify the prices.\n");
				pricesInfo.setPrices(Math.random()*10, Math.random()*8);
				System.out.printf("Writer: Prices have been modified.\n");
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}*/
			//System.out.printf("%s:start setting Price at %s\n", Thread.currentThread().getName(),new Date().toString());
			pricesInfo.setPrices(Math.random()*10, Math.random()*8);
			
			//System.out.printf("%s:end setting Price at %s\n", Thread.currentThread().getName(),new Date().toString());
		}
		
	}
	
	public static void main(String[] args) {
		/*PricesInfo pricesInfo=new PricesInfo();
		Reader readers[]=new Reader[5];
		Thread threadsReader[]=new Thread[5];
		for (int i=0; i<5; i++){
			readers[i]=new Reader(pricesInfo);
			threadsReader[i]=new Thread(readers[i]);
		}
		
		Writer writer=new Writer(pricesInfo);
		Thread threadWriter=new Thread(writer);
		for (int i=0; i<5; i++){
			threadsReader[i].start();
		}
		threadWriter.start();*/
		
				
		case1();
		//case2();
		
	}
	public static void case1(){
		//case 1:test read lock
		//可以同时访问
		PricesInfo pricesInfo=new PricesInfo();
		Thread t1=new Thread(new Reader(pricesInfo),"t1");
		Thread t2=new Thread(new Reader(pricesInfo),"t2");
		t1.start();
		t2.start();
		/**
		 * t2:start getting Price at Tue Dec 29 15:55:00 CST 2015
			t1:start getting Price at Tue Dec 29 15:55:00 CST 2015
			t2: Price 1: 1.000000
			t1: Price 1: 1.000000
			t2: Price 2: 2.000000
			t1: Price 2: 2.000000
			t2:end getting Price at Tue Dec 29 15:55:05 CST 2015
			t1:end getting Price at Tue Dec 29 15:55:05 CST 2015
		 */

	}
	/**
	 * case 2:read & writer
	 * 先读后写再读
	 * 读-读 无关
	 * 读-写互斥
	 * 写-写互斥
	 */
	public static void case2(){
		PricesInfo pricesInfo=new PricesInfo();
		Thread t1=new Thread(new Reader(pricesInfo),"t1");
		Thread t3=new Thread(new Reader(pricesInfo),"t3");
		Thread t2=new Thread(new Writer(pricesInfo),"t2");
		t1.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		t2.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		t3.start();
	}
}
