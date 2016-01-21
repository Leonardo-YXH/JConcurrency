package chapter5.section1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class CountTask extends RecursiveTask<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int start;
	private int end;
	private List<Double> data;
	
	private static final int THRESHOLD=4;
	
	public CountTask(int start,int end,List<Double> data) {
		this.start=start;
		this.end=end;
		this.data=data;
	}

	@Override
	protected Integer compute() {//核心，分解子任务
		int sum=0;
		if(end-start<=THRESHOLD){
			for(int i=start;i<=end;i++){
				sum+=this.data.get(i);
			}
		}
		else{//任务分解成两个子任务(也可以是多个)
			int mid=(start+end)/2;
			CountTask task1=new CountTask(start, mid, data);
			CountTask task2=new CountTask(mid+1, end, data);
			task1.fork();
			task2.fork();
			
			int sum1=task1.join();
			int sum2=task2.join();
			sum=sum1+sum2;
		}
		System.out.println(sum);
		return sum;
	}
	public static void main(String[] args) {
		List<Double> data=new ArrayList<Double>();
		int size=11;
		for(int i=0;i<size;i++){
			data.add(i*2d);
		}
		ForkJoinPool pool=new ForkJoinPool();
		CountTask task=new CountTask(0, size-1, data);
		Future<Integer> rs=pool.submit(task);
		
		try {
			System.out.println(rs.get());//
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//pool.execute(task);用于执行没有返回值的task,比如继承RecursiveAction的task
	}
}
