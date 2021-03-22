package monitor;

public class MainMonitor {
	
	public static void main(String[] args) {
		
		Integer sync = new Integer(0);
		
		SharedBoundedStackMonitor buffer = new SharedBoundedStackMonitor(5, sync);
	}
}
