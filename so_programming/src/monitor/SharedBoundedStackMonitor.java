package monitor;

import java.util.concurrent.Semaphore;

public class SharedBoundedStackMonitor {
	
	private Philosopher[] philosophers;
	private Semaphore mutex;
	private int size;
	private Integer sync; 
	
	public SharedBoundedStackMonitor(int size, Integer sync) {
		this.size = size;
		this.philosophers = new Philosopher[this.size];
		this.mutex = new Semaphore(1);
		this.sync = sync;
		this.start(this.size);
	}
	
	private void start(int size) {
		for (int i = 0; i < size; i++) {
			this.philosophers[i] = new Philosopher(this, i, sync);
			this.philosophers[i].start();
		}
	}

	synchronized public void take(int id) throws InterruptedException {
		this.philosophers[id].setStatus(Status.HUNGRY);
		System.out.println("Philosopher " + id + " is hungry");
		int right = this.getRight(id);
		int left = this.getLeft(id);
		if (!this.philosophers[right].getStatus().equals(Status.EATING) 
				&& !this.philosophers[left].getStatus().equals(Status.EATING)) {
			System.out.println("Philosopher " + id + " is eating");
			this.philosophers[id].setStatus(Status.EATING);
		} else {
			System.out.println("Philosopher " + id + " couldn't eat");
		}
	}
	
	synchronized public void drop(int id) throws InterruptedException {
		System.out.println("Philosopher " + id + " is thinking");
		this.philosophers[id].setStatus(Status.THINKING);
		int right = this.getRight(id);
		int left = this.getLeft(id);
		if (this.philosophers[right].getStatus().equals(Status.HUNGRY) 
				&& !this.philosophers[this.getRight(right)].getStatus().equals(Status.EATING)) {
			this.philosophers[right].setStatus(Status.EATING);
		}
		if (this.philosophers[left].getStatus().equals(Status.HUNGRY) 
				&& !this.philosophers[this.getLeft(left)].getStatus().equals(Status.EATING)) {
			this.philosophers[left].setStatus(Status.EATING);
		}
	}
	
	public boolean testTake(int id) {
		int right = this.getRight(id);
		int left = this.getLeft(id);
		return !this.philosophers[right].getStatus().equals(Status.EATING) 
				&& !this.philosophers[left].getStatus().equals(Status.EATING);
	}
	
	private int getLeft(int id) {
		return (id + 1) % this.size;
	}
	
	private int getRight(int id) {
		return id;
	}
	
	public Semaphore getMutex() {
		return mutex;
	}
}
