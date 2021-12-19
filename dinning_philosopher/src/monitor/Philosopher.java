package monitor;

public class Philosopher extends Thread {
	
	private int id;
	private Status status;
	private SharedBoundedStackMonitor buffer;
	private final Long serviceTime = 1000L;
	private Integer sync;
	
	public Philosopher(SharedBoundedStackMonitor buffer, int id, Integer sync) {
		this.id = id;
		this.status = Status.THINKING;
		this.buffer = buffer;
		this.sync = sync;
	}
	
	@Override
	public void run() {
		think();
		synchronized (this.sync) {
			while (!this.buffer.testTake(this.id)) {
				try {
					this.sync.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			this.takeCutlery();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		eat();
		try {
			this.dropCutlery();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void takeCutlery() throws InterruptedException {
		this.buffer.take(this.id);
	}
	
	private void dropCutlery() throws InterruptedException {
		this.buffer.drop(this.id);
	}
	
	private void think() {
		try {
            Thread.sleep(serviceTime);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
	}
	
	private void eat() {
		try {
            Thread.sleep(serviceTime);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
