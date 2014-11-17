package com.acton.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.acton.data.DataStore;
import com.acton.models.Permission;
import com.acton.models.Role;
import com.acton.models.User;
import com.acton.services.exception.DataNotFoundException;
import com.acton.services.exception.RolesCannotBeEmpty;
import com.acton.services.exception.UserIdAlreadyExists;

public class Service implements IService {
	
	private DataStore ds = DataStore.getInstance();

	@Override
	public List<String> getPermsissionsByUserId(String userId)
			throws DataNotFoundException {

		User user = getUserById(userId);
		List<String> roles = user.getRoles();
		if (roles == null)
			return new ArrayList<String>();
		Set<String> response = new HashSet<String>();
		for (String roleId : roles) {
			List<String> permissionList = ds.getRole(roleId).getPermissions();
			if (permissionList == null)
				continue;
			for (String permissionId : permissionList) {
				Permission permission = ds.getPermsision(permissionId);
				if (permission == null)
					continue;
				else {
					response.add(permission.getName());
				}
			}
		}
		return new ArrayList<String>(response);
	}

	private User getUserById(String userId) throws DataNotFoundException {
		User user = ds.getUser(userId);
		if (user == null)
			throw new DataNotFoundException();
		return user;
	}

	@Override
	public Boolean checkPermission(String userId, String permissionId) throws DataNotFoundException {
		User user = getUserById(userId);
		List<String> roles = user.getRoles();
		if (roles == null) {
			System.out.println("Role is null!");
			return false;
		}
		for (String roleId : roles) {
			Role role = ds.getRole(roleId);
			if(role == null) continue;
			List<String> permissionList = role.getPermissions();
			if (permissionList == null)
				continue;
			for (String id : permissionList) {
				if (id.equals(permissionId))
					return true;
			}
		}
		return false;	
	}

	@Override
	public Boolean modifyPermissionListForRole(String roleId,HashMap<String,List<String>> permissionMap) throws DataNotFoundException {
		Role role = ds.getRole(roleId);
		if(role == null) throw new DataNotFoundException();
		if(!permissionMap.containsKey("permissions")) throw new DataNotFoundException(); //validate if permission exists
		List<String> list = permissionMap.get("permissions");
		role.setPermissions(list);
		return true;
	}

	@Override
	public Boolean deletePermission(String permissionId) throws DataNotFoundException {
		if(ds.getPermsision(permissionId) == null ) throw new DataNotFoundException();
		ds.permissionStore.remove(permissionId);//remove from roles?
		return true;
	}

	@Override
	public List<String> fetchAllRoles() {
		return new ArrayList<String>(ds.roleStore.keySet());
	}

	@Override
	public List<String> createUser(User user) throws DataNotFoundException, UserIdAlreadyExists, RolesCannotBeEmpty {
		if(ds.getUser(user.getId()) != null) {
			throw new UserIdAlreadyExists();
		}
		if(user.getRoles().size() == 0) {
			throw new RolesCannotBeEmpty();
		}
		for(String role : user.getRoles()) {
			if(ds.getRole(role) == null) throw new DataNotFoundException();
		}
		ds.userStore.put(user.getId(),user);
		return null;
	}


	
	
}
