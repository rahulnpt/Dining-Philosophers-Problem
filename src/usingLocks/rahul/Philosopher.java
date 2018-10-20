package usingLocks.rahul;

import java.util.Random;

public class Philosopher {
	
	private int id;
	private Chopstick leftChopstick;
	private Chopstick rightChopstick;
	private int eatCounter;
	private Random random;
	private volatile boolean isFull = false;
	
	
	Philosopher(int id,Chopstick leftChopstick,Chopstick rightChopstick){
		this.id = id;
		this.leftChopstick =leftChopstick;
		this.rightChopstick = rightChopstick;
		this.random = new Random();
	}

	public void eat(Philosopher philosopher,State leftState,State rightState) throws InterruptedException {
		if(leftChopstick.pickUp(philosopher, leftState)) {
			if(rightChopstick.pickUp(philosopher, rightState)) {
				System.out.println(this+" is eating now");
				this.eatCounter++;
				Thread.sleep(random.nextInt(1000));
				rightChopstick.putDown(philosopher, rightState);
			}
			leftChopstick.putDown(philosopher, leftState);
		}
	}
	
	public void think() throws InterruptedException {
		System.out.println(this+" is thinking now");
		Thread.sleep(random.nextInt(1000));
	}
	
	@Override
	public String toString() {
		return "Philosopher " + id ;
	}

	public boolean isFull() {
		return isFull;
	}

	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}

	public int getEatingCounter() {
		return this.eatCounter;
	}
	
}
