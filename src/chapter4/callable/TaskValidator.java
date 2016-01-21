package chapter4.callable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Running multiple tasks and processing the
 * first result. call the executor.invokeAny()
 * <p>Running multiple tasks and processing all
 * the results. call the executor.invokeAll()
 * @author Leonardo
 *
 */
public class TaskValidator implements Callable<String> {

	private UserValidator validator;
	private String user;
	private String password;
	public TaskValidator(UserValidator validator,String user,String password) {
		this.validator=validator;
		this.user=user;
		this.password=password;
	}
	@Override
	public String call() throws Exception {
		if (!validator.validate(user, password)) {
			System.out.printf("%s: The user has not been found\n",validator.getName());
			throw new Exception("Error validating user");
		}
		System.out.printf("%s: The user has been found\n",validator.
				getName());
		return validator.getName();
	}
	public static void main(String[] args) {
		String username="test";
		String password="test";
		UserValidator ldapV=new UserValidator("LDAP");
		UserValidator dbV=new UserValidator("DataBase");
		TaskValidator ldapT=new TaskValidator(ldapV, username, password);
		TaskValidator dbT=new TaskValidator(dbV, username, password);
		
		List<TaskValidator> tasks=new ArrayList<>();
		tasks.add(ldapT);
		tasks.add(dbT);
		ExecutorService executor=(ExecutorService)Executors.newCachedThreadPool();
		String result;
		
		try {
			result=executor.invokeAny(tasks);//关键代码
			System.out.println(result);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		executor.shutdown();
	}

}
