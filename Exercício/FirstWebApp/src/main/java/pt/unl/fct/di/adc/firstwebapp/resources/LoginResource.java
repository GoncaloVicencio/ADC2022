package pt.unl.fct.di.adc.firstwebapp.resources;

import java.util.logging.Logger;

//import javax.servlet.http.HttpServletRequest;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;

//import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
//import com.google.cloud.datastore.PathElement;
//import com.google.cloud.datastore.StringValue;
//import com.google.cloud.datastore.Transaction;
import com.google.cloud.datastore.*;
import com.google.gson.Gson;

import pt.unl.fct.di.adc.firstwebapp.util.LoginData;
import pt.unl.fct.di.adc.firstwebapp.util.AuthToken;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class LoginResource {

	/**
	 * A logger object
	 */
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());

	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	private final Gson g = new Gson();
	
	public LoginResource() {

	}

	/*@POST
	@Path("/v2")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response doLogin2(LoginData data, @Context HttpServletRequest request, @Context HttpHeaders headers) {
		
		LOG.fine("Attempt to login user: " + data.username);
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		Key ctrsKey = datastore.newKeyFactory()
				.addAncestors(PathElement.of("User", data.username))
				.setKind("UserStats").newKey("counters");
		
		Key logKey = datastore.allocateId(datastore.newKeyFactory()
				.addAncestors(PathElement.of("User", data.username))
				.setKind("UserLog").newKey());
		
		Transaction txn = datastore.newTransaction();
		
		try {
			Entity user = txn.get(userKey);
			if( user == null) {
				LOG.warning("Failed login attempt fopr username: " + data.username);
				return Response.status(Status.FORBIDDEN).build();
			}
		
			Entity stats = txn.get(ctrsKey);
			if( stats == null) {
				stats = Entity.newBuilder(ctrsKey)
					.set("user_stats_logins", 0L)
					.set("user_stats_failed", 0L)
					.set("user_first_login", Timestamp.now())
					.set("user_last_login", Timestamp.now())
					.build();
			}
			String hashedPWD = (String) user.getString("user_pwd");
		
			if(hashedPWD.equals(DigestUtils.sha512Hex(data.password))) {
			
				Entity log = Entity.newBuilder(logKey)
					.set("user_login_ip", request.getRemoteAddr())
					.set("user_login_host", request.getRemoteHost())
					.set("user_login_latlon", StringValue.newBuilder(headers.getHeaderString("X-AppEngine-CityLatLong"))
							.setExcludeFromIndexes(true).build())
					.set("user_login_city", headers.getHeaderString("X-AppEngine-City"))
					.set("user_login_country", headers.getHeaderString("X-AppEngine-Country"))
					.set("user_login_time", Timestamp.now())
					.build();
				Entity ustats = Entity.newBuilder(ctrsKey)
					.set("user_stats_logins", 1L + stats.getLong("user_stats_logins"))
					.set("user_stats_failed", 0L)
					.set("user_first_login",stats.getTimestamp("user_first_login"))
					.set("user_last_login", Timestamp.now())
					.build();
				txn.put(log, ustats);
				txn.commit();
		
				AuthToken token = new AuthToken(data.username, user.getString("user_role"));
				LOG.info("User '" + data.username + "' logged in sucessfully.");
				return Response.ok(g.toJson(token)).build();
			} else {
				Entity ustats = Entity.newBuilder(ctrsKey)
					.set("user_stats_logins", stats.getLong("user_stats_logins"))
					.set("user_stats_failed", 1L + stats.getLong("user_stats_failed"))
					.set("user_first_login", stats.getTimestamp("user_first_login"))
					.set("user_last_login", stats.getTimestamp("user_last_login"))
					.set("user_last_attempt", Timestamp.now())
					.build();

				txn.put(ustats);
				txn.commit();
				LOG.warning("Wrong password or username: " + data.username);
				return Response.status(Status.FORBIDDEN).build();
			}
		} catch(Exception e) {
			txn.rollback();
			LOG.severe(e.getMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		
		} finally {
			if(txn.isActive()) {
				txn.rollback();
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
		}
		
	}*/
	
	@POST
	@Path("/v3")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response doLogin3(LoginData data) {
		
		LOG.fine("Attempt to login user: " + data.username);
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		Entity user = datastore.get(userKey);
		
		if( user == null) {
			LOG.warning("Failed login attempt for username: " + data.username);
			return Response.status(Status.FORBIDDEN).build();
		} else {
			String hashedPWD = (String) user.getString("user_pwd");
			if(hashedPWD.equals(DigestUtils.sha512Hex(data.password))) {
				AuthToken token = new AuthToken(data.username, user.getString("user_role"));
				LOG.info("User '" + data.username + "' logged in sucessfully.");
				return Response.ok(g.toJson(token)).build();
			} else {
				LOG.warning("Username or Password does not match.");
				return Response.status(Status.FORBIDDEN).build();
			}
		}
	}

}
