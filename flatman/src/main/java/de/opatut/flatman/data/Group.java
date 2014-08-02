package de.opatut.flatman.data;

import java.util.ArrayList;
import java.util.List;

public class Group {
	public int id;
	public String name;
	public String created;
	
	public List<User> members = new ArrayList<User>();
	public List<ShoppingItem> shopping_items = new ArrayList<ShoppingItem>();
	public List<ShoppingCategory> shopping_categories = new ArrayList<ShoppingCategory>();
	public List<Task> tasks = new ArrayList<Task>();
	
	public ShoppingCategory getShoppingCategory(int id) {
		for(ShoppingCategory category : shopping_categories) {
			if(category.id == id) {
				return category;
			}
		}
		return null;
	}
}
