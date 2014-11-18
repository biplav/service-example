package com.acton;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.acton.models.User;
import com.acton.models.dto.PermissionList;
import com.acton.services.Service;
import com.acton.services.exception.DataNotFoundException;
import com.acton.services.exception.RolesCannotBeEmpty;
import com.acton.services.exception.UserIdAlreadyExists;
import com.google.gson.Gson;

/**
 * Root resource (exposed at "myresource" path)
 */

@Path("/")
public class MyResource {

	Service service = new Service();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("user/{id}")
	public Response getUserPermissions(@PathParam("id") String id) {
		try {
			Gson gson = new Gson();
			List<String> permsissionsByUserId = service
					.getPermsissionsByUserId(id);
			String json = gson.toJson(permsissionsByUserId);
			return Response.ok(json, MediaType.APPLICATION_JSON).build();
		} catch (DataNotFoundException de) {
			return Response.status(Status.NOT_FOUND)
					.entity("User with Id not found").build();
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("checkpermission")
	public Response checkUserPermissions(@QueryParam("userid") String userId,
			@QueryParam("permissionid") String permissionId) {
		if (userId == null || permissionId == null) {
			return Response.status(Status.BAD_REQUEST)
					.entity("userid or permissionid cannot be null").build();
		}
		try {
			if (service.checkPermission(userId, permissionId)) {
				return Response.ok("success").build();
			} else {
				return Response
						.status(Status.FORBIDDEN)
						.entity("User " + userId + " does not have permission "
								+ permissionId).build();
			}

		} catch (DataNotFoundException de) {
			return Response.status(Status.NOT_FOUND)
					.entity("User with Id not found").build();
		} catch (Exception e) {
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("roles")
	public Response fetchAllRoles() {
		Gson gson = new Gson();
		String allRoles = gson.toJson(service.fetchAllRoles());
		return Response.ok(allRoles).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("user")
	public Response createUser(User user) {
		if (user == null || user.getId() == null) {
			return Response.noContent().entity("User id cannot be null.")
					.build();
		}
		try {
			service.createUser(user);
		} catch (DataNotFoundException e) {
			return Response.status(Status.NOT_FOUND).entity("Role not found!")
					.build();
		} catch (UserIdAlreadyExists e) {
			return Response.status(Status.CONFLICT)
					.entity("User id already exists!").build();
		} catch (RolesCannotBeEmpty e) {
			return Response.status(Status.BAD_REQUEST)
					.entity("Role cannot be empty").build();
		}
		return Response.ok().build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("roles/{roleId}")
	public Response modifyRole(@PathParam("roleId") String roleId,
			PermissionList permission) {
		if (permission == null) {
			return Response.noContent()
					.entity("Permission Object cannot be null.").build();
		}
		try {
			service.modifyPermissionListForRole(roleId, permission);
		} catch (DataNotFoundException e) {
			return Response.status(Status.NOT_FOUND).entity("Role not found!")
					.build();
		}
		return Response.ok("success").build();
	}

	@DELETE
	@Path("permission/{permissionId}")
	public Response deletePermission(
			@PathParam("permissionId") String permissionId) {
		try {
			service.deletePermission(permissionId);
			return Response.ok("success").build();
		} catch (DataNotFoundException e) {
			return Response.status(Status.NOT_FOUND)
					.entity("Permission not found!").build();
		}
	}

	@GET
	@Path("extension")
	@Produces("application/x-chrome-extension")
	public Response installExtnesion() {
		try {
			return Response
					.ok()
					.entity(new FileInputStream(
							"src/main/resources/acton-extension.crx")).build();
		} catch (FileNotFoundException e) {
			return Response.status(Status.NOT_FOUND)
					.encoding("Extension not found for your browser").build();
		}

	}
}
