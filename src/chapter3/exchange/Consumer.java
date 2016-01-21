package chapter3.exchange;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

public class Consumer implements Runnable {

	private List<String> buffer;
	private final Exchanger<List<String>> exchanger;
	private int num;
	
	public Consumer(List<String> buffer,Exchanger<List<String>> exchanger,int num){
		this.buffer=buffer;
		this.exchanger=exchanger;
		this.num=num;
	}
	@Override
	public void run() {
		int cycle=1;
		for(int i=0;i<num;i++){
			System.out.printf("Consumer: cycle %d\n",cycle);
			
			try {
				buffer=exchanger.exchange(buffer);//交换buffer2-->buffer1
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Consumer: "+buffer.size());
			
			for(int j=0;j<num;j++){
				String message=this.buffer.get(0);
				System.out.printf("Consumer: %s\n",message);
				buffer.remove(0);
			}
			
			cycle++;
		}
	}
	
	public static void main(String[] args){
		List<String> buffer1=new ArrayList<String>();
		List<String> buffer2=new ArrayList<String>();
		Exchanger<List<String>> exchanger=new Exchanger<List<String>>();
	
		int num=1;
		Producer p=new Producer(buffer1, exchanger,num);
		Consumer c=new Consumer(buffer2, exchanger,num);
		
		Thread pt=new Thread(p,"producer");
		Thread ct=new Thread(c,"consumer");
		
		pt.start();
		ct.start();
	}

}
