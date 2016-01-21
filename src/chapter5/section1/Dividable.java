package chapter5.section1;

import java.util.List;
/**
 * 可分解的任务接口
 * @author Leonardo
 *
 * @param <E>
 */
public interface Dividable<E> {

	/**
	 * 将任务分解成多个子任务
	 * @return
	 */
	public List<E> divide();
}
