package com.acton.data;

import com.acton.models.Permission;
import com.acton.models.Role;
import com.acton.models.User;

public interface IDataStore {

	public  void initialize();
	public  User getUser(String id);
	public  Role getRole(String id);
	public  Permission getPermsision(String id);
	
}
