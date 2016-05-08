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
	private enum Target {
		TEMPERATURE(Temperature.class, "temperature"), 
		HUMIDITY(Humidity.class, "humidity");

		private Class entityType;
		private String path;

		Target(Class entityType, String path) {
			this.entityType = entityType;
			this.path = path;
		}
	}

	public static void main(String[] args) {
		String apiUrl;
		if (args.length > 0) {
			apiUrl = args[0];
		} else {
			apiUrl = "http://localhost:8080/sensors/api/";
		}
		Client client = ClientBuilder.newClient();

		Target[] targets = Target.values();
		for (Target target : targets) {
			UriBuilder baseUri = UriBuilder.fromUri(apiUrl).path(target.path);
			URI listUri = baseUri.clone().path("list").build();
			WebTarget webTarget = client.target(listUri);
			List<String> list = getList(webTarget);
			list.forEach((uuid) -> printSensor(client, baseUri.clone(), target.entityType, uuid));
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
