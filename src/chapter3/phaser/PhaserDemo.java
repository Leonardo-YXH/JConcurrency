package chapter3.phaser;

import java.util.concurrent.Phaser;

public class PhaserDemo implements Runnable {
	private String name;
	private Phaser phaser;
	private int step;
	public PhaserDemo(String name,Phaser phaser,int step){
		this.phaser=phaser;
		this.name=name;
		this.step=step;
	}
	private boolean step1(){
		System.out.println(this.name+" start step1...");
		if(this.step==1){
			this.phaser.arriveAndDeregister();//执行到此结束
			return false;
		}
		else{
			this.phaser.arriveAndAwaitAdvance();//继续下一步骤
		}
		System.out.println(this.name+" end step1...");
		return true;
	}
	private boolean step2(){
		System.out.println(this.name+" start step2...");
		if(this.step==2){
			this.phaser.arriveAndDeregister();//执行到此结束
			return false;
		}
		else{
			this.phaser.arriveAndAwaitAdvance();//继续下一步骤
		}
		System.out.println(this.name+" end step2...");
		return true;
	}
	private boolean step3(){
		System.out.println(this.name+" start step3...");
		if(this.step==3){
			this.phaser.arriveAndDeregister();//执行到此结束
			return false;
		}
		else{
			this.phaser.arriveAndAwaitAdvance();//继续下一步骤
		}
		System.out.println(this.name+" end step3...");
		return true;
	}
	@Override
	public void run() {
		System.out.println("start "+this.name+" at step"+this.step);
		this.phaser.arriveAndAwaitAdvance();
		if(this.step1()){
			if(this.step2()){
				this.step3();
			}
		}
		
		System.out.println("end "+this.name+" at step"+this.step);
	}

	public static void main(String[] args) {
		/**
		 * 各个线程的各步骤单独运行，但每一步之间是同步的，只有大家都执行完第一步才会继续下一步，若其中有的
		 * 线程没有下一步则不管该线程
		 */
		Phaser phaser=new Phaser(3);
		Thread jt=new Thread(new PhaserDemo("jack", phaser, 2),"jack");
		jt.start();
		Thread rt=new Thread(new PhaserDemo("rose", phaser, 2),"rose");
		rt.start();
		Thread lt=new Thread(new PhaserDemo("leo", phaser, 1),"leo");
		lt.start();
		
		try {
			jt.join();
			rt.join();
			lt.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Terminated: "+ phaser.isTerminated());
	}

}
