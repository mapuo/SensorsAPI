package link.mapuo.sensors.emulator;

public class SensorEmulator {

	public static void main(String[] args) {
		ThreadGroup threadGroup = new ThreadGroup("Sensors");
		for (int i = 0; i < 10; i++) {
			new Thread(threadGroup, new SensorRunnable()).start();
		}
	}

}
