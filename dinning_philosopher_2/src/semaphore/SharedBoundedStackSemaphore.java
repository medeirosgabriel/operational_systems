package semaphore;

import java.util.concurrent.Semaphore;

//  0
//5   1
//4   2
//  3

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
		System.out.println(String.format("Philosopher %d is hungry", id));
		int right = this.getRight(id);
		int left = this.getLeft(id);
		if (!this.philosophers[right].getStatus().equals(Status.EATING) 
				&& !this.philosophers[left].getStatus().equals(Status.EATING)) {
			
			System.out.println(String.format("Philosopher %d is eating - LEFT: %s - RIGHT: %s", id, 
					this.philosophers[this.getLeft(id)].getStatus(),
					this.philosophers[this.getRight(id)].getStatus()));
			this.philosophers[id].setStatus(Status.EATING);
			this.philoSem[id].release();
			
		} else {
			System.out.println(String.format("Philosopher %d couldn't eat - LEFT: %s - RIGHT: %s", id, 
					this.philosophers[this.getLeft(id)].getStatus(),
					this.philosophers[this.getRight(id)].getStatus()));
		}
		this.mutex.release();
		this.philoSem[id].acquire();
	}
	
	public void drop(int id) throws InterruptedException {
		this.mutex.acquire();
		System.out.println(String.format("Philosopher %d dropped the cutlery and now is thinking", id));
		this.philosophers[id].setStatus(Status.THINKING);
		int right = this.getRight(id);
		int left = this.getLeft(id);
		if (this.philosophers[right].getStatus().equals(Status.HUNGRY) 
				&& !this.philosophers[this.getRight(right)].getStatus().equals(Status.EATING)) {
			this.philosophers[right].setStatus(Status.EATING);
			this.philoSem[right].release();
			
			System.out.println(String.format("Philosopher %d is eating - LEFT: %s - RIGHT: %s", right, 
					this.philosophers[this.getLeft(right)].getStatus(),
					this.philosophers[this.getRight(right)].getStatus()));
		}
		
		if (this.philosophers[left].getStatus().equals(Status.HUNGRY) 
				&& !this.philosophers[this.getLeft(left)].getStatus().equals(Status.EATING)) {
			this.philosophers[left].setStatus(Status.EATING);
			this.philoSem[left].release();
			
			System.out.println(String.format("Philosopher %d is eating - LEFT: %s - RIGHT: %s", left, 
					this.philosophers[this.getLeft(left)].getStatus(),
					this.philosophers[this.getRight(left)].getStatus()));
			
		}
		this.mutex.release();
	}
	
	private int getLeft(int id) {
		return (id + 1) % this.size;
	}
	
	private int getRight(int id) {
		if (id == 0) {
			return this.size - 1;
		} else {
			return id - 1;
		}
	}
}
