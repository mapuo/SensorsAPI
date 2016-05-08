package link.mapuo.sensors.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SensorList {
	@XmlElement
	public List<String> uuids;

	public SensorList() {
	}

	public SensorList(List<String> uuids) {
		this.uuids = uuids;
	}
}
