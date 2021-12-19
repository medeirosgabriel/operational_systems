package monitor;

public class SharedBoundedStackMonitor {
	
	private Philosopher[] philosophers;
	private int size;
	private Integer sync; 
	
	public SharedBoundedStackMonitor(int size, Integer sync) {
		this.size = size;
		this.philosophers = new Philosopher[this.size];
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
			this.philosophers[id].setStatus(Status.EATING);
			System.out.println(String.format("Philosopher %d is eating - LEFT: %s - RIGHT: %s", id, 
					this.philosophers[this.getLeft(id)].getStatus(),
					this.philosophers[this.getRight(id)].getStatus()));
		} else {
			System.out.println(String.format("Philosopher %d couldn't eat - LEFT: %s - RIGHT: %s", id, 
					this.philosophers[this.getLeft(id)].getStatus(),
					this.philosophers[this.getRight(id)].getStatus()));
		}
	}
	
	synchronized public void drop(int id) throws InterruptedException {
		this.philosophers[id].setStatus(Status.THINKING);
		System.out.println(String.format("Philosopher %d dropped the cutlery and now is thinking", id));
		int right = this.getRight(id);
		int left = this.getLeft(id);
		if (this.philosophers[right].getStatus().equals(Status.HUNGRY) 
				&& !this.philosophers[this.getRight(right)].getStatus().equals(Status.EATING)) {
			this.philosophers[right].setStatus(Status.EATING);
			System.out.println(String.format("Philosopher %d is eating - LEFT: %s - RIGHT: %s", right, 
					this.philosophers[this.getLeft(right)].getStatus(),
					this.philosophers[this.getRight(right)].getStatus()));
			
		}
		if (this.philosophers[left].getStatus().equals(Status.HUNGRY) 
				&& !this.philosophers[this.getLeft(left)].getStatus().equals(Status.EATING)) {
			this.philosophers[left].setStatus(Status.EATING);
			System.out.println(String.format("Philosopher %d is eating - LEFT: %s - RIGHT: %s", left, 
					this.philosophers[this.getLeft(left)].getStatus(),
					this.philosophers[this.getRight(left)].getStatus()));
		}
	}
	
	synchronized public boolean testTake(int id) {
		int right = this.getRight(id);
		int left = this.getLeft(id);
		return !this.philosophers[right].getStatus().equals(Status.EATING) 
				&& !this.philosophers[left].getStatus().equals(Status.EATING);
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
