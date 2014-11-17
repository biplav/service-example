package com.acton.models;

import java.util.ArrayList;
import java.util.List;

public class User {
	
	String id;
	List<String> roles = new ArrayList<String>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
}
