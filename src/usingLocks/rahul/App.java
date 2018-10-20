package usingLocks.rahul;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.IntStream;

public class App {
	
	Philosopher philosopherArray[];
	
	Chopstick chopstickArray[] ;
	
	public static void main(String[] args) throws InterruptedException {
		
		App diningPhilosopherSimulation = new App();
		
		diningPhilosopherSimulation.startApp();
	}
	
	public void startApp() throws InterruptedException {
		ExecutorService executorService = null;
		try {
			executorService = Executors.newFixedThreadPool(Constants.NO_OF_PHILOSOPHERS);
			
			philosopherArray = new Philosopher[Constants.NO_OF_PHILOSOPHERS];
			
			chopstickArray = new Chopstick[Constants.NO_OF_CHOPSTICKS];
			
			IntStream.range(0, chopstickArray.length)
			.forEach(i->{
				chopstickArray[i] = new Chopstick(i+1);
			});
			
			Function<Integer, Chopstick> getAdjacentChopstick = (j)->{
				Chopstick chopStick = j<0?chopstickArray[chopstickArray.length	+j]:chopstickArray[j];
				return chopStick;
			};
			
			for(int i=0;i<philosopherArray.length;i++) {
				philosopherArray[i] = new Philosopher(i+1, getAdjacentChopstick.apply(i-1), getAdjacentChopstick.apply(i));
				executorService.submit(new ThreadJob(philosopherArray[i]));
			};
			
			Thread.sleep(Constants.MAXIMUM_SIMULATION_TIME);
			
			
			IntStream.range(0, philosopherArray.length)
			.forEach(i->{
				philosopherArray[i].setFull(true);
			});
		} finally {
			executorService.shutdown();
			while(!executorService.isTerminated()) {
				System.out.println("############Waiting for all Threads to finish jobs###########");
				Thread.sleep(100);
			};
			
			for(int i=0;i<philosopherArray.length;i++) {
				System.out.println(philosopherArray[i]+" ate "+philosopherArray[i].getEatingCounter()+" times");
			}
		}
		
	}

	
class ThreadJob implements Runnable{
	
	private Philosopher philosopher;
	
	public ThreadJob(Philosopher philosopher) {
		this.philosopher = philosopher;
	}
	
	
	public void run() {
		while(!philosopher.isFull()) {
			try {
				philosopher.think();
				philosopher.eat(philosopher, State.LEFT, State.RIGHT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
}
