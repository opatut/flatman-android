package de.opatut.flatman.data;

public class User {
	public int id;
	public String username;
	public String displayname;
	public String email;
	public int group_id;
	
	public String getAvatarUrl(int size) {
		return DataStorage.API_URL + "/user/" + id + "/avatar/" + size;
	}

	public String getAvatarUrl() {
		return getAvatarUrl(128);
	}

	public static String getAvatarUrl(int id, int size) {
		return DataStorage.API_URL + "/user/" + id + "/avatar/" + size;
	}
}
