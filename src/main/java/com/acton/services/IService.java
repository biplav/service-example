package com.acton.services;

import java.util.List;

import com.acton.models.User;
import com.acton.models.dto.PermissionList;
import com.acton.services.exception.DataNotFoundException;
import com.acton.services.exception.RolesCannotBeEmpty;
import com.acton.services.exception.UserIdAlreadyExists;

public interface IService {
	
	//Given user, return list of names of permissions that this user is entitled to.
	public List<String> getPermsissionsByUserId(String userId) throws DataNotFoundException;
	
	//Given user and permission, return boolean if entitled or not.
	public Boolean checkPermission(String userId,String permssionId) throws DataNotFoundException;

	//Delete a permission
	public Boolean deletePermission(String permissionId) throws DataNotFoundException;
	
	//Fetch all Roles
	public List<String> fetchAllRoles();
	
	//Create a new User
	public List<String> createUser(User user) throws DataNotFoundException, UserIdAlreadyExists, RolesCannotBeEmpty;

	//Modify permissions of a role
	Boolean modifyPermissionListForRole(String roleId,
			PermissionList permissions) throws DataNotFoundException;
		


}
