package semaphore;

public class Philosopher extends Thread {
	
	private int id;
	private Status status;
	private SharedBoundedStackSemaphore buffer;
	private final Long serviceTime = 1000L;
	
	public Philosopher(SharedBoundedStackSemaphore buffer, int id) {
		this.id = id;
		this.status = Status.THINKING;
		this.buffer = buffer;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				think();
				takeCutlery();
				eat();
				dropCutlery();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
