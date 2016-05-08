package link.mapuo.sensors.model;

import java.util.UUID;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Temperature extends SensorModel {
	@XmlElement
	@NotNull
	@DecimalMin(value = "-40", inclusive = true, message = "Temperature shoud be >= -40")
	@DecimalMax(value = "100", inclusive = true, message = "Temperature shoud be <= 100")
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
		this.temperature = temperature;
	}

	@Override
	public String toString() {
		return "Temperature [getUuid()=" + getUuid() + ", getName()=" + getName() + ", getLocation()=" + getLocation()
				+ ", getTemperature()=" + getTemperature() + "]";
	}

}
