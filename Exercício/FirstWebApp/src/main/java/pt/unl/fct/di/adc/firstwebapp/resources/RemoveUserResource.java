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

		if (user == null) {
			LOG.warning("User does not exist: " + data.username);
			return Response.ok("User does not exist: " + data.username).build();
			
		} else {
			if (data.role.equals("SU") || data.tokenUsername.equals(data.username)
					|| ( data.role.equals("GS") && 
							( user.getString("user_role").equals("User") || user.getString("user_role").equals("GBO")) ) 
					|| ( data.role.equals("GBO") && 
							user.getString("user_role").equals("User") ) )	 
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
				
				}finally {
					if(txn.isActive())
						txn.rollback();
				}				
			} else {
				LOG.warning("You do not have permission to take this action.");
				return Response.ok("{You do not have permission to take this action.}").build();
			}
		}
	}
}