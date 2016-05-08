package link.mapuo.sensors.emulator;

import java.util.concurrent.ThreadLocalRandom;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class SensorRunnable implements Runnable {
	private Sensor<?> sensor;
	private Client client;

	public SensorRunnable() {
		client = ClientBuilder.newClient();
		if (ThreadLocalRandom.current().nextBoolean()) {
			sensor = new TemperatureSensor(client);
		}
		else
		{
			sensor = new HumiditySensor(client);
		}
		System.out.println("[" + Thread.currentThread().getName() + "] Created thread with type " + sensor.getClass() + "!");
	}

	@Override
	public void run() {
		while (true) {
			try {
				long millis = ThreadLocalRandom.current().nextInt(15) * 1000;
				System.out.println("[" + Thread.currentThread().getName() + "] Waiting for " + millis + " millis before resume...");
				Thread.sleep(millis);
			} catch (InterruptedException e) {
			}
			
			System.out.println("[" + Thread.currentThread().getName() + "] Starting update of " + sensor.getValue() + "...");
			sensor.update();
			System.out.println("[" + Thread.currentThread().getName() + "] Updated " + sensor.getValue() + "!");
		}
	}

}
