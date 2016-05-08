package link.mapuo.sensors.model;

import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SensorModel {
	@XmlElement
	private UUID uuid;

	@XmlElement
	private String name;

	@XmlElement
	private String location;

	public SensorModel() {
	}

	public static SensorModel withRandomUUID() {
		return new SensorModel(UUID.randomUUID());
	}

	public SensorModel(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public void setRandomUUID() {
		setUuid(UUID.randomUUID());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
