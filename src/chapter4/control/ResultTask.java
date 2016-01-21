package chapter4.control;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class ResultTask extends FutureTask<String> {

	private String name;
	public ResultTask(Callable<String> callable) {
		super(callable);
		this.name=((ExecutableTask)callable).getName();
	}

	@Override
	protected void done(){
		if(isCancelled()){
			System.out.println(new Date().toString()+"  "+this.name+" Has been canceled");
		}
		else{
			System.out.println(new Date().toString()+"  "+this.name+" Has finished");
		}
	}
	public static void main(String[] args) {
		ExecutorService executor=Executors.newCachedThreadPool();
		ResultTask tasks[]=new ResultTask[5];
		for (int i = 0; i < tasks.length; i++) {
			ExecutableTask executableTask=new ExecutableTask("Task "+i);
			tasks[i]=new ResultTask(executableTask);
			executor.submit(tasks[i]);
		}
		try{
			TimeUnit.SECONDS.sleep(5);
			
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		for (int i = 0; i < tasks.length; i++) {
			boolean rs=tasks[i].cancel(true);//important
			System.out.println(tasks[i].name+" canceled:"+rs);
		}
		for (int i=0; i<tasks.length; i++) {
			try {
				if (!tasks[i].isCancelled()){
					System.out.printf("%s\n",tasks[i].get());
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} 
		}
		executor.shutdown();
	}

}
