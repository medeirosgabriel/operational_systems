package semaphore;

import java.util.concurrent.Semaphore;

public class SharedBoundedStackSemaphore {
	
	private Semaphore[] philoSem;
	private Philosopher[] philosophers;
	private Semaphore mutex;
	private int size;
	
	public SharedBoundedStackSemaphore(int size) {
		this.size = size;
		this.philoSem = new Semaphore[this.size];
		this.philosophers = new Philosopher[this.size];
		this.mutex = new Semaphore(1);
		this.start(this.size);
	}
	
	private void start(int size) {
		for (int i = 0; i < size; i++) {
			this.philoSem[i] = new Semaphore(0);
		}
		for (int i = 0; i < size; i++) {
			this.philosophers[i] = new Philosopher(this, i);
			this.philosophers[i].start();
		}
	}

	public void take(int id) throws InterruptedException {
		this.mutex.acquire();
		this.philosophers[id].setStatus(Status.HUNGRY);
		System.out.println("Philosopher " + id + " is hungry");
		int right = this.getRight(id);
		int left = this.getLeft(id);
		if (!this.philosophers[right].getStatus().equals(Status.EATING) 
				&& !this.philosophers[left].getStatus().equals(Status.EATING)) {
			System.out.println("Philosopher " + id + " is eating");
			this.philosophers[id].setStatus(Status.EATING);
			this.philoSem[id].release();
		} else {
			System.out.println("Philosopher " + id + " couldn't eat");
		}
		this.mutex.release();
		this.philoSem[id].acquire();
	}
	
	public void drop(int id) throws InterruptedException {
		this.mutex.acquire();
		System.out.println("Philosopher " + id + " is thinking");
		this.philosophers[id].setStatus(Status.THINKING);
		int right = this.getRight(id);
		int left = this.getLeft(id);
		if (this.philosophers[right].getStatus().equals(Status.HUNGRY) 
				&& !this.philosophers[this.getRight(right)].getStatus().equals(Status.EATING)) {
			this.philoSem[right].release();
			this.philosophers[right].setStatus(Status.EATING);
			System.out.println("Philosopher " + right + " is eating");
		}
		if (this.philosophers[left].getStatus().equals(Status.HUNGRY) 
				&& !this.philosophers[this.getLeft(left)].getStatus().equals(Status.EATING)) {
			this.philoSem[left].release();
			this.philosophers[left].setStatus(Status.EATING);
			System.out.println("Philosopher " + left + " is eating");
		}
		this.mutex.release();
	}
	
	private int getLeft(int id) {
		return (id + 1) % this.size;
	}
	
	private int getRight(int id) {
		return id;
	}
}
