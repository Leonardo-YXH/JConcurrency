package chapter3.exchange;

import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * 
 * @author Leonardo
 *
 */
public class Producer implements Runnable {

	private List<String> buffer;
	private final Exchanger<List<String>> exchanger;
	private int num;
	
	public Producer(List<String> buffer,Exchanger<List<String>> exchanger,int num){
		this.buffer=buffer;
		this.exchanger=exchanger;
		this.num=num;
	}
	@Override
	public void run() {
		int cycle=1;
		for(int i=0;i<num;i++){
			System.out.printf("Producer: cycle %d\n",cycle);
			for(int j=0;j<num;j++){
				String message="event "+((i*num)+j);
				System.out.printf("Producer: %s\n",message);
				buffer.add(message);
			}
			try {
				buffer=exchanger.exchange(buffer);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Producer: "+buffer.size());
			cycle++;
		}

	}

}
