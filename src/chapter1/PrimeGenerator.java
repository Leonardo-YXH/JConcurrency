package chapter1;

public class PrimeGenerator extends Thread {

	public void run(){
		long number=1L;
		while(true){
			if(isPrime(number)){
				System.out.printf("Number %d is Prime\n",number);
			}
			if(isInterrupted()){
				System.out.printf("this prime Generator has been Interrupted\n");
				return;
			}
			number++;
			//System.out.println(number);
		}
	}
	private boolean isPrime(long number) {
		if (number <=2) {
			return true;
			}
			for (long i=2; i<number; i++){
			if ((number % i)==0) {
			return false;
			}
			}
			return true;
	}
	

}
