package pt.unl.fct.di.adc.firstwebapp.resources;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.google.cloud.datastore.*;

import pt.unl.fct.di.adc.firstwebapp.util.RemoveUserData;

@Path("/removeuser")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class RemoveUserResource {

	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());

	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	
	public RemoveUserResource() {
		
	}
	
	@POST
	@Path("/v1")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response doRemoveUser(RemoveUserData data) {

		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		Entity user = datastore.get(userKey);

		Key userKeyAuth = datastore.newKeyFactory().setKind("User").newKey(data.tokenUsername);
		Entity userAuth = datastore.get(userKeyAuth);
		
		if (user == null) {
			LOG.warning("User does not exist.");
			return Response.status(Status.FORBIDDEN).entity("User does not exist.").build();
			
		} else {
			String u = user.getString("user_role");
			String r = userAuth.getString("user_role");
			if (data.tokenUsername.equals(data.username) || r.equals("SU") || 
					( r.equals("GS") && ( u.equals("User") || u.equals("GBO")) ) || 
					( r.equals("GBO") && u.equals("User") ) )	 
			{
				LOG.info("Removing user:" + data.username);
				
				Transaction txn = datastore.newTransaction();
				try {
					txn.delete(userKey);				
					LOG.info("User '" + data.username + "' sucessfully deleted.");
					txn.commit();
					return Response.ok("User deleted.").build();
					
				} catch(Exception e) {
					txn.rollback();
					LOG.severe(e.getMessage());
					return Response.status(Status.INTERNAL_SERVER_ERROR).build();
				
				} finally {
					if(txn.isActive()) {
						txn.rollback();
					}
				}				
			} else {
				LOG.warning("You do not have permission to take this action.");
				return Response.status(Status.FORBIDDEN).entity("You do not have permission to take this action.").build();
			}
		}
	}
}