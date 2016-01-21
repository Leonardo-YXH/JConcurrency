package chapter5.section1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MySchedulePlan extends RecursiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DemoTask job;
	public MySchedulePlan(DemoTask job) {
		this.job=job;
	}
	@Override
	protected void compute() {
		if(job.isDividable()){
			List<DemoTask> subTasks=job.divide();
			List<MySchedulePlan> jobs=new ArrayList<MySchedulePlan>();
			subTasks.forEach(
					t->{MySchedulePlan plan=new MySchedulePlan(t);
						jobs.add(plan);
						//plan.fork();plan.join();
					}
					);
			invokeAll(jobs);
		}
		else{
			job.doTask();
		}

	}

	public static void main(String[] args) {
		DemoTask job=new DemoTask(7, 2, 3, "job");
		MySchedulePlan plan=new MySchedulePlan(job);
		ForkJoinPool pool=new ForkJoinPool();
		pool.invoke(plan);
		//pool.execute(plan);
		//pool.submit(plan);
	}
}
