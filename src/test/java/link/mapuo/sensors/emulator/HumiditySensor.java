package link.mapuo.sensors.emulator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import link.mapuo.sensors.model.Humidity;

public class HumiditySensor extends SensorBase<Humidity> implements Sensor<Humidity> {
	private static final String PATH = "humidity";
	private Humidity humidity;
	private Client client;

	public HumiditySensor(Client client) {
		this.client = client;
		int t = randomTemp();
		int h = randomHmdt();
		humidity = new Humidity();
		humidity.setName("Hmdt");
		humidity.setLocation(randomLocation());
		humidity.setTemperature(t);
		humidity.setHumidity(h);
		humidity = create(humidity, client, UriBuilder.fromUri(API_URL).path(PATH));
	}

	@Override
	public Humidity getValue() {
		return humidity;
	}

	public void genDelta() {
		int t = humidity.getTemperature() + randomDelta();
		if (t < -40 || t > 100) {
			t = randomTemp();
		}
		int h = humidity.getHumidity() + randomDelta();
		if (h < 0 || t > 100) {
			t = randomHmdt();
		}
		humidity.setTemperature(t);
		humidity.setHumidity(h);
	}
	
	@Override
	public Humidity update() {
		genDelta();
		UriBuilder builder = UriBuilder.fromUri(API_URL).path(PATH).path("{uuid}");
		WebTarget target = client.target(builder.build(humidity.getUuid().toString()));
		
		humidity = super.update(humidity, target);
		return humidity;
	}
}
