package link.mapuo.sensors.client;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import link.mapuo.sensors.model.Humidity;
import link.mapuo.sensors.model.SensorList;
import link.mapuo.sensors.model.SensorModel;
import link.mapuo.sensors.model.Temperature;

@SuppressWarnings("rawtypes")
public class SensorClient {
	private static final String API_URL = "http://localhost:8080/sensors/api/";
	private static final String TEMP_PATH = "temperature";
	private static final String HMDT_PATH = "humidity";

	private enum Target {
		TEMPERATURE(Temperature.class, UriBuilder.fromUri(API_URL).path(TEMP_PATH)), 
		HUMIDITY(Humidity.class, UriBuilder.fromUri(API_URL).path(HMDT_PATH));

		private Class entityType;
		private UriBuilder uri;

		Target(Class entityType, UriBuilder uri) {
			this.entityType = entityType;
			this.uri = uri;
		}
	}

	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();

		Target[] targets = Target.values();
		for (Target target : targets) {
			URI listUri = target.uri.clone().path("list").build();
			WebTarget webTarget = client.target(listUri);
			List<String> list = getList(webTarget);
			list.forEach((uuid) -> printSensor(client, target.uri.clone(), target.entityType, uuid));
		}
	}

	private static List<String> getList(WebTarget target) {
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		SensorList sensorList = response.readEntity(SensorList.class);
		return sensorList.uuids;
	}

	@SuppressWarnings("unchecked")
	private static void printSensor(Client client, UriBuilder uri, Class entityType, String uuid) {
		WebTarget target = client.target(uri.path("{uuid}").build(uuid));
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		SensorModel entity = (SensorModel) response.readEntity(entityType);
		entity.setUuid(UUID.fromString(uuid));
		System.out.println(entity);
	}

}
