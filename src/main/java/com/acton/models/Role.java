package com.acton.models;

import java.util.ArrayList;
import java.util.List;

public class Role {
	String id;
	List<String> permissions = new ArrayList<String>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
}
