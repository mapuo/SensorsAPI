package link.mapuo.sensors.api;

import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import link.mapuo.sensors.db.dao.HumidityDAO;
import link.mapuo.sensors.model.Humidity;
import link.mapuo.sensors.model.SensorList;

@Path("humidity")
public class HumidityResource {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createSensor(@Context UriInfo info, Humidity h) {
		DataSource ds = getDataSource();
		try (Connection con = ds.getConnection()) {
			HumidityDAO dao = new HumidityDAO(con);
			h.setRandomUUID();
			Humidity humidity = dao.create(h);
			URI path = info.getBaseUriBuilder().path(this.getClass()).path("{uuid}").build(humidity.getUuid());

			return Response.created(path).build();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Response.serverError().build();
	}

	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listSensors() {
		DataSource ds = getDataSource();
		try (Connection con = ds.getConnection()) {
			HumidityDAO dao = new HumidityDAO(con);
			List<Humidity> list = dao.findAll();
			List<String> uuids = Lists.transform(list, new Function<Humidity, String>() {
				@Override
				public String apply(Humidity input) {
					return input.getUuid().toString();
				}
			});
			SensorList sensors = new SensorList(uuids);

			return Response.ok(sensors).build();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Response.serverError().build();
	}

	@GET
	@Path("{sensorid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response showValue(@PathParam("sensorid") UUID uuid) {
		DataSource ds = getDataSource();
		try (Connection con = ds.getConnection()) {
			HumidityDAO dao = new HumidityDAO(con);
			Humidity humidity = dao.find_by_id(uuid);

			return Response.ok(humidity).build();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Response.status(Status.NOT_FOUND).build();
	}

	@PUT
	@Path("{sensorid}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response recordValue(@PathParam("sensorid") UUID uuid, Humidity h) {
		System.out.println("recordValue: " + h);
		DataSource ds = getDataSource();
		try (Connection con = ds.getConnection()) {
			HumidityDAO dao = new HumidityDAO(con);
			System.out.println("searching for humidity: " + uuid);
			Humidity humidity = dao.find_by_id(uuid);
			System.out.println("humidity found: " + humidity);
			humidity.setTemperature(h.getTemperature());
			humidity.setHumidity(h.getHumidity());
			humidity = dao.update(humidity);

			return Response.ok(humidity).build();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Response.status(Status.NOT_FOUND).build();
	}

	@DELETE
	@Path("{sensorid}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response recordValue(@PathParam("sensorid") UUID uuid) {
		DataSource ds = getDataSource();
		try (Connection con = ds.getConnection()) {
			HumidityDAO dao = new HumidityDAO(con);
			dao.delete(uuid);

			return Response.ok().build();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Response.status(Status.NOT_FOUND).build();
	}

	private DataSource getDataSource() {
		try {
			return (DataSource) new InitialContext().lookup("java:comp/env/jdbc/SensorsDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}

		return null;
	}

}
