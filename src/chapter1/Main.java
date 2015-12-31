package chapter1;

public class Main {

	public static void main(String[] args) {
		Thread task=new PrimeGenerator();
		task.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!Thread.interrupted()){
			task.interrupt();
		}
		new Thread(()->{});
	}

}
