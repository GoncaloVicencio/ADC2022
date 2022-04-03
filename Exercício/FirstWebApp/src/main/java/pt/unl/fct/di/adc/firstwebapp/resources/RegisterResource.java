package pt.unl.fct.di.adc.firstwebapp.resources;

import pt.unl.fct.di.adc.firstwebapp.util.RegisterData;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class RegisterResource {
	
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
		
	public RegisterResource() {
		
	}
	
	/*@POST
	@Path("/v1")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doRegistrationV1(RegisterData data) {
		LOG.fine("Attempt to register user: " + data.username);
		
		//check input data
		if( !data.validRegistration() ) {
			return Response.status(Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();
		}
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		
		Entity user = Entity.newBuilder(userKey).set("user_pwd", DigestUtils.sha512Hex(data.password))
				.set("user_creation_time", Timestamp.now()).build();
		
		datastore.put(user);
		
		LOG.info("User registered " + data.username);
		
		return Response.ok("{}").build();
	}*/
	
	/*
	@GET
	@Path("/{username}")
	public Response createUser(@PathParam("username") String username, ) {
		
		LOG.fine("Attempt to register user: " + data.username);
		
		//check input data
		if( !data.validRegistration() ) {
			return Response.status(Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();
		}
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		
		Entity user = Entity.newBuilder(userKey).set("user_pwd", DigestUtils.sha512Hex(data.password))
				.set("user_creation_time", Timestamp.now()).build();
		
		datastore.put(user);
		
		LOG.info("User registered " + data.username);
		
		return Response.ok("{}").build();
	}
	*/
	/*
	@POST
	@Path("/v3")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doRegistrationV3(RegisterData data) {
		LOG.fine("Attempt to register user: " + data.username);
		
		//check input data
		if( !data.validRegistration() ) {
			return Response.status(Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();
		}		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		
		Entity user = Entity.newBuilder(userKey).set("user_pwd", DigestUtils.sha512Hex(data.password))
				.set("user_creation_time", Timestamp.now()).build();
		
		if (user != null) {
			return Response.status(Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();
		}
		
		datastore.add(user);
		
		LOG.info("User registered " + data.username);
		
		return Response.ok("{}").build();
	}
	*/
	
	@POST
	@Path("/v4")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doRegistrationV4(RegisterData data) {
		LOG.fine("Attempt to register user: " + data.username);
		
		if( !data.validRegistration() ) {
			return Response.status(Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();
		}				
		Transaction txn = datastore.newTransaction();
		try {
			Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
			Entity user = datastore.get(userKey);
			if (user != null) {
				txn.rollback();
				return Response.status(Status.BAD_REQUEST).entity("User already exists.").build();
			} else {
				user = Entity.newBuilder(userKey)
						.set("user_name", data.name)
						.set("user_pwd", DigestUtils.sha512Hex(data.password))
						.set("user_profile_status", data.profile)
						.set("user_home_telephone", data.PhoneNoFixed)
						.set("user_mobile_phone", data.PhoneNoMobile)
						.set("user_taxID", data.NIF)
						.set("user_role", data.role)
						.set("user_state", data.state)
						.set("user_creation_time", Timestamp.now()).build();
				txn.add(user);				
				LOG.info("User registered " + data.username);
				txn.commit();
				return Response.ok("{}").build();
				
			}
		} catch(Exception e) {
			txn.rollback();
			LOG.severe(e.getMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		
		}finally {
			if(txn.isActive()) {
				txn.rollback();
			}
		}		
	}	
	
}
