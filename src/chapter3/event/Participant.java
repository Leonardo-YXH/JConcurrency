package chapter3.event;

import java.util.concurrent.TimeUnit;
/**
 * 
 * @author Leonardo
 *
 */
public class Participant implements Runnable {

	private Videoconference conference;
	private String name;
	public Participant(Videoconference conference,String name){
		this.conference=conference;
		this.name=name;
	}
	
	@Override
	public void run() {
		long duration=(long)(Math.random()*10);
		try {
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.conference.arrive(name);//
	}

	public static void main(String[] args){
		int num=3;
		Videoconference conference=new Videoconference(num);
		Thread tc=new Thread(conference);
		tc.start();
		for (int i=0; i<num; i++){
			Participant p=new Participant(conference, "Participant "+i);
			Thread t=new Thread(p);
			t.start();
		}
	}
}
