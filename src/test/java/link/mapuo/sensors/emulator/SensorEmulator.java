package link.mapuo.sensors.emulator;

public class SensorEmulator {

	public static void main(String[] args) {
		String apiUrl;
		if (args.length > 0) {
			apiUrl = args[0];
		} else {
			apiUrl = "http://localhost:8080/sensors/api/";
		}
		int numberOfClients;
		if (args.length > 1) {
			numberOfClients = Integer.parseInt(args[1]);
		} else {
			numberOfClients = 10;
		}
		ThreadGroup threadGroup = new ThreadGroup("Sensors");
		for (int i = 0; i < numberOfClients; i++) {
			new Thread(threadGroup, new SensorRunnable(apiUrl)).start();
		}
	}

}
