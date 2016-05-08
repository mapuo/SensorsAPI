package link.mapuo.sensors.emulator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import link.mapuo.sensors.model.Temperature;

public class TemperatureSensor extends SensorBase<Temperature> implements Sensor<Temperature> {
	private static final String PATH = "temperature";
	private Temperature temperature;
	private Client client;

	public TemperatureSensor(Client client) {
		this.client = client;
		temperature = new Temperature();
		temperature.setName("Temp");
		temperature.setLocation(randomLocation());
		temperature.setTemperature(randomTemp());
		temperature = create(temperature, client, UriBuilder.fromUri(API_URL).path(PATH));
	}

	@Override
	public Temperature getValue() {
		return temperature;
	}

	public void genDelta() {
		int t = temperature.getTemperature() + randomDelta();
		if (t < -40 || t > 100) {
			t = randomTemp();
		}
		temperature.setTemperature(t);
	}

	@Override
	public Temperature update() {
		genDelta();
		UriBuilder builder = UriBuilder.fromUri(API_URL).path(PATH).path("{uuid}");
		WebTarget target = client.target(builder.build(temperature.getUuid().toString()));
		
		temperature = super.update(temperature, target);
		return temperature;
	}
}
