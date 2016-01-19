package chapter3.phaser;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Leonardo
 *
 */
public class FileSearch implements Runnable {

	private String initPath;
	private String end;
	private List<String> results;
	private Phaser phaser;
	
	public FileSearch(String initPath,String end,Phaser phaser){
		this.initPath=initPath;
		this.end=end;
		this.phaser=phaser;
		this.results=new ArrayList<String>();
	}
	private void directoryProcess(File file) {
		File list[] = file.listFiles();
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				if (list[i].isDirectory()) {
					directoryProcess(list[i]);
				}
				else {
					fileProcess(list[i]);
				}
			}
		}
	}
	private void fileProcess(File file) {
		if (file.getName().endsWith(end)) {
			results.add(file.getAbsolutePath());
		}
	}
	private void filterResults() {
		List<String> newResults=new ArrayList<>();
		long actualDate=new Date().getTime();
		for(String ri:this.results){
			File file=new File(ri);
			long fileDate=file.lastModified();
			if(actualDate-fileDate<TimeUnit.MICROSECONDS.convert(1, TimeUnit.DAYS)){
				newResults.add(ri);
			}
		}
		this.results=newResults;
	}
	private boolean checkResults(){
		if(this.results.isEmpty()){
			System.out.printf("%s: Phase %d: 0 results.\n",Thread.currentThread().getName(),phaser.getPhase());
			System.out.printf("%s: Phase %d: End.\n",Thread.currentThread().getName(),phaser.getPhase());
			phaser.arriveAndDeregister();
			return false;
		}
		else {
			System.out.printf("%s: Phase %d: %d results.\n",Thread.currentThread().getName(),phaser.getPhase(),results.size());
			phaser.arriveAndAwaitAdvance();
			return true;
		}
	}
	private void showInfo(){
		for (int i=0; i<results.size(); i++){
			File file=new File(results.get(i));
			System.out.printf("%s: %s\n",Thread.currentThread().
			getName(),file.getAbsolutePath());
		}
		phaser.arriveAndAwaitAdvance();
	}
	@Override
	public void run() {
		this.phaser.arriveAndAwaitAdvance();
		System.out.printf("%s: Starting.\n",Thread.currentThread().getName());
		File file = new File(initPath);
			if (file.isDirectory()) {
			directoryProcess(file);
		}
		filterResults();
		if(!checkResults()){
			return ;
		}
		showInfo();
		phaser.arriveAndDeregister();
		System.out.printf("%s: Work completed.\n",Thread.
		currentThread().getName());
			
	}

	public static void main(String[] args) {
		Phaser phaser=new Phaser(3);
		FileSearch system=new FileSearch("", "", phaser);
		FileSearch apps=new FileSearch("", "", phaser);
		FileSearch doc=new FileSearch("", "", phaser);
		Thread systemThread=new Thread(system,"System");
		systemThread.start();
		Thread appThread=new Thread(apps,"Apps");
		systemThread.start();
		Thread docThread=new Thread(doc,"Documents");
		systemThread.start();
		try {
			systemThread.join();
			appThread.join();
			docThread.join();
			} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Terminated: "+ phaser.isTerminated());
	}
}
