package chapter3.event;

import java.util.concurrent.CountDownLatch;

public class Videoconference implements Runnable {

	private final CountDownLatch controller;
	
	public Videoconference(int number) {
		this.controller=new CountDownLatch(number);
	}
	
	public void arrive(String name){
		System.out.printf("%s has arrived.",name);
		this.controller.countDown();//计数器减1
		System.out.printf("VideoConference: Waiting for %d participants.\n",controller.getCount());
	}
	@Override
	public void run() {
		System.out.printf("VideoConference: Initialization: %d participants.\n",controller.getCount());
		try{
			this.controller.await();//直到计数为0时才唤醒该线程
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		System.out.println("end of all thread,continue to do another thing.");
	}

}
