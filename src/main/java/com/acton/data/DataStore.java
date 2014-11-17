package com.acton.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.acton.models.Permission;
import com.acton.models.Role;
import com.acton.models.User;
import com.google.gson.Gson;

public class DataStore implements IDataStore {
	
	public Map<String,User> userStore = new HashMap<String,User>();
	public Map<String,Role> roleStore = new HashMap<String,Role>();
	public Map<String,Permission> permissionStore = new HashMap<String,Permission>();
	
	private static DataStore dataStore;
	
	private DataStore() {
		super();
	}
	
	public static DataStore getInstance() {
		if(dataStore == null) {
			dataStore = new DataStore();
			dataStore.initialize();
			return dataStore;
		}
		return dataStore;
	}

	private void initializeUserStore() {
		List<User> userList = readJson("users.json", User.class);
		for(User user : userList) {
			userStore.put(user.getId(), user);
		}
	}
	
	private void initializeRoleStore() {
		List<Role> roleList = readJson("roles.json", Role.class);
		for(Role role : roleList) {
			roleStore.put(role.getId(), role);
		}
	}
	
	private void initializePermisionStore() {
		List<Permission> permissionsList = (List<Permission>) readJson(
				"permissions.json", Permission.class);
		for(Permission permission : permissionsList) {
			permissionStore.put(permission.getId(),permission);
		}
	}
	
	private <T> List<T> readJson(String filename, final Class<T> type) {
		Gson gson = new Gson();
		try {
			Type listType = new ParameterizedType() {
				public Type[] getActualTypeArguments() {
					return new Type[] { type };
				}

				public Type getRawType() {
					return List.class;
				}

				public Type getOwnerType() {
					return null;
				}
			};
			BufferedReader br = new BufferedReader(new FileReader(
					"src/main/resources/" + filename));
			// convert the json string back to object
			List<T> obj = gson.fromJson(br, listType);
			System.out.println(obj);
			return (List<T>) obj;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void initialize() {
		initializePermisionStore();
		initializeRoleStore();
		initializeUserStore();	
	}

	@Override
	public User getUser(String id) {
		return userStore.get(id);
	}

	@Override
	public Role getRole(String id) {
		return roleStore.get(id);
	}

	@Override
	public Permission getPermsision(String id) {
		return permissionStore.get(id);
	}
	
	public static void main(String[] args) {
		DataStore ds = DataStore.getInstance();
		ds.initialize();
	}

}
