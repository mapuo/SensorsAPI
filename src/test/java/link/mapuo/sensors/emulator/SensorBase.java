package link.mapuo.sensors.emulator;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import link.mapuo.sensors.model.SensorModel;

public abstract class SensorBase<T> {
	private static final String[] LOCATIONS = { "basement", "balcony", "rooftop", "living room", "bedroom", "kitchen" };

	protected int randomHmdt() {
		return ThreadLocalRandom.current().nextInt(0, 100);
	}

	protected int randomTemp() {
		return ThreadLocalRandom.current().nextInt(-40, 100);
	}

	protected int randomDelta() {
		return ThreadLocalRandom.current().nextInt(-5, 6);
	}

	protected String randomLocation() {
		return LOCATIONS[ThreadLocalRandom.current().nextInt(LOCATIONS.length)];
	}

	protected T create(T t, Client client, UriBuilder builder) {
		WebTarget createTarget = client.target(builder.build());
		Entity<?> entity = Entity.entity(t, MediaType.APPLICATION_JSON_TYPE);
		Response createResponse = createTarget.request(MediaType.APPLICATION_JSON_TYPE).post(entity);

		URI location = createResponse.getLocation();
		WebTarget readTarget = client.target(location);

		SensorModel readEntity = (SensorModel) readTarget.request(MediaType.APPLICATION_JSON_TYPE).get(t.getClass());
		String[] uuid = location.getPath().split("/");
		readEntity.setUuid(UUID.fromString(uuid[uuid.length - 1]));

		return (T) readEntity;
	}

	public T update(T t, WebTarget target) {
		Entity<?> entity = Entity.entity(t, MediaType.APPLICATION_JSON_TYPE);
		try {
			target.request(MediaType.APPLICATION_JSON_TYPE).put(entity);
		} catch (ResponseProcessingException e) {
			e.printStackTrace();
		} catch (ProcessingException e) {
			e.printStackTrace();
		}

		return t;
	}
}
