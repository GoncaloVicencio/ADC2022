package pt.unl.fct.di.adc.firstwebapp.resources;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.cloud.datastore.*;

import pt.unl.fct.di.adc.firstwebapp.util.UpdatePasswordData;

@Path("/updatepassword")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class UpdatePasswordResource {

	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());

	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	
	public UpdatePasswordResource() {
		
	}
	
	@POST
	@Path("/v1")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response doUpdatePassword(UpdatePasswordData data) {

		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		Entity user = datastore.get(userKey);

		if (user == null) {
			LOG.warning("User does not exist: " + data.username);
			return Response.status(Status.FORBIDDEN).build();
			
		} else {
			String hashedPWD = (String) user.getString("user_pwd");
			
			if (data.oldPassword.equals(data.password)) {
				LOG.warning("New password is identical to it's old version. Please change passwords.");
				return Response.status(Status.FORBIDDEN).build();
				
			} else if (hashedPWD.equals(DigestUtils.sha512Hex((String) data.oldPassword))){				
				Transaction txn = datastore.newTransaction();
				try {
					Entity newUser = Entity.newBuilder(userKey)
						.set("user_name", user.getString("user_name"))
						.set("user_pwd", DigestUtils.sha512Hex(data.password))
						.set("user_profile_status", user.getString("user_profile_status"))
						.set("user_home_telephone", user.getString("user_home_telephone"))
						.set("user_mobile_phone", user.getString("user_mobile_phone"))
						.set("user_taxID", user.getString("user_taxID"))
						.set("user_role", user.getString("user_role"))
						.set("user_state", user.getString("user_state"))
						.set("user_creation_time", user.getString("user_creation_time")).build();
					txn.put(newUser);				
					LOG.info("User '" + data.username + "' sucessfully changed passwords.");
					txn.commit();
					return Response.ok("{}").build();
				} catch (Exception e) {
					txn.rollback();
					LOG.severe(e.getMessage());
					return Response.status(Status.INTERNAL_SERVER_ERROR).build();
				}
				finally {
					if(txn.isActive())
						txn.rollback();
				}
			} else {
				LOG.warning("Incorrect password, change failed for user: " + data.username);
				return Response.status(Status.FORBIDDEN).build();
			}
		}
	}
}