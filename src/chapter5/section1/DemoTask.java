package chapter5.section1;

import java.util.ArrayList;
import java.util.List;

public class DemoTask implements Dividable<DemoTask> {

	/**
	 * 任务量 
	 */
	private int taskSize;
	/**
	 * 最大承载量
	 */
	private int upSize;
	/**
	 * 分割块
	 */
	private int divNum;
	/**
	 * 任务名
	 */
	private String taskName;
	public DemoTask(int taskSize,int upSize,int divNum,String taskName){
		this.taskSize=taskSize;
		this.upSize=upSize;
		this.divNum=divNum;
		this.taskName=taskName;
	}
	@Override
	public List<DemoTask> divide() {
		int avg=taskSize/divNum;
		int other=taskSize-avg*divNum;
		List<DemoTask> tasks=new ArrayList<DemoTask>();
		for(int i=0;i<divNum;i++){
			int tSize=avg;
			if(i<other){
				tSize=avg+1;
			}
			DemoTask task=new DemoTask(tSize, upSize, divNum, taskName+"_"+i);
			tasks.add(task);
		}
		return tasks;
	}
	/**
	 * 任务是否需要分解
	 * @return
	 */
	public boolean isDividable(){
		
		if(this.taskSize>this.upSize){
			return true;
		}
		return false;
	}
	/**
	 * 执行任务
	 */
	public void doTask(){
		System.out.println(Thread.currentThread().getName()+":doing "+this.taskName);
	}
	
}
