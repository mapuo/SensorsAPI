package link.mapuo.sensors.model;

import java.security.InvalidParameterException;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Temperature extends SensorModel {
	@XmlElement
	private int temperature;

	public Temperature() {
		// JAXB needs this
	}

	public Temperature(int temperature) {
		setTemperature(temperature);
	}

	public Temperature(UUID uuid, int temperature) {
		super(uuid);
		setTemperature(temperature);
	}

	public int getTemperature() {
		return temperature;
	}
	
	public void setTemperature(int temperature)
	{
		// Validation? -40 >= temperature <= 100
		if (temperature < -40 || temperature > 100) {
			throw new InvalidParameterException("temperature must be between -40 and 100");
		}
		this.temperature = temperature;
	}

	@Override
	public String toString() {
		return "Temperature [getUuid()=" + getUuid() + ", getName()=" + getName() + ", getLocation()=" + getLocation()
				+ ", getTemperature()=" + getTemperature() + "]";
	}

}
