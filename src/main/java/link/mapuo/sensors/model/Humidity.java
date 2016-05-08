package link.mapuo.sensors.model;

import java.util.UUID;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Humidity extends Temperature {
	@XmlElement
	@DecimalMin(value = "0",   inclusive = true, message = "Humidity shoud be >= 0"  )
	@DecimalMax(value = "100", inclusive = true, message = "Humidity shoud be <= 100")
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
		this.humidity = humidity;
	}

	@Override
	public String toString() {
		return "Humidity [getUuid()=" + getUuid() + ", getName()=" + getName() + ", getLocation()=" + getLocation()
				+ ", getTemperature()=" + getTemperature() + ", getHumidity()=" + getHumidity() + "]";
	}

}
