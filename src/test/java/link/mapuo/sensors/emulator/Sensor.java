package link.mapuo.sensors.emulator;

public interface Sensor<T> {

	public T getValue();
	
	public T update();

}
