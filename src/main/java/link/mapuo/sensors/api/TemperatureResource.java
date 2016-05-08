package link.mapuo.sensors.api;

import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.validation.Valid;
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

import link.mapuo.sensors.db.dao.TemperatureDAO;
import link.mapuo.sensors.model.SensorList;
import link.mapuo.sensors.model.Temperature;

@Path("temperature")
public class TemperatureResource {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createSensor(@Context UriInfo info, @Valid Temperature t) {
		DataSource ds = getDataSource();
		try (Connection con = ds.getConnection()) {
			TemperatureDAO dao = new TemperatureDAO(con);
			t.setRandomUUID();
			Temperature temperature = dao.create(t);
			URI path = info.getBaseUriBuilder().path(this.getClass()).path("{uuid}").build(temperature.getUuid());

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
			TemperatureDAO dao = new TemperatureDAO(con);
			List<Temperature> list = dao.findAll();
			List<String> uuids = Lists.transform(list, new Function<Temperature, String>() {
				@Override
				public String apply(Temperature input) {
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
			TemperatureDAO dao = new TemperatureDAO(con);
			Temperature temperature = dao.find_by_id(uuid);

			return Response.ok(temperature).build();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Response.status(Status.NOT_FOUND).build();
	}

	@PUT
	@Path("{sensorid}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response recordValue(@PathParam("sensorid") UUID uuid, @Valid Temperature t) {
		System.out.println("recordValue: " + t);
		DataSource ds = getDataSource();
		try (Connection con = ds.getConnection()) {
			TemperatureDAO dao = new TemperatureDAO(con);
			System.out.println("searching for temperature: " + uuid);
			Temperature temperature = dao.find_by_id(uuid);
			System.out.println("temperature found: " + temperature);
			temperature.setTemperature(t.getTemperature());
			temperature = dao.update(t);

			return Response.ok(temperature).build();
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
			TemperatureDAO dao = new TemperatureDAO(con);
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
