package de.opatut.flatman.data;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCategory {
	public int id;
	public String title;
	public int group_id;
	
	public List<ShoppingItem> getItems() {
		List<ShoppingItem> items = new ArrayList<ShoppingItem>();
		for(ShoppingItem item : DataStorage.getInstance().group.shopping_items) {
			if(item.category_id == id) {
				items.add(item);
			}
		}
		return items;
	}
}
