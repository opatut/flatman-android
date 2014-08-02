package de.opatut.flatman.data;

import de.opatut.flatman.data.DataStorage.DataUpdateErrorListener;
import de.opatut.flatman.data.DataStorage.ReplyStatus;

public class ShoppingItem {
	public int id;
	public String amount;
	public String title;
	public String description;
	public boolean purchased;
	public int category_id;
	public int group_id;

	public void sendStatus(final DataUpdateErrorListener errorListener) {
		new Thread(new Runnable() {
			public void run() {
				DataStorage.getInstance().load("/shopping/item/status/" + id + "/" + (purchased ? "purchased" : "reset"),
						null, ReplyStatus.class, errorListener);
			}
		}).start();
	}
}