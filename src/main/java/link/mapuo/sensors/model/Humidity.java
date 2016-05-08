package link.mapuo.sensors.model;

import java.security.InvalidParameterException;
import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Humidity extends Temperature {
	@XmlElement
	private int humidity;
	
	public Humidity() {
		// JAXB needs this
	}

	public Humidity(int temperature, int humidity) {
		super(temperature);
		this.setHumidity(humidity);
	}
	public Humidity(UUID uuid, int temperature, int humidity) {
		super(uuid, temperature);
		this.setHumidity(humidity);
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		// Validation? 0 >= humidity <= 100
		if (humidity < 0 || humidity > 100) {
			throw new InvalidParameterException("humidity must be between 0 and 100");
		}
		this.humidity = humidity;
	}

	@Override
	public String toString() {
		return "Humidity [getUuid()=" + getUuid() + ", getName()=" + getName() + ", getLocation()=" + getLocation()
				+ ", getTemperature()=" + getTemperature() + ", getHumidity()=" + getHumidity() + "]";
	}

}
