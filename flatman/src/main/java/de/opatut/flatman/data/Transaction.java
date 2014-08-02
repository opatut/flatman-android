package de.opatut.flatman.data;

import java.util.Date;

import de.opatut.flatman.util.Formatter;

public class Transaction {
    public int id;
    public String from_type;
    public String to_type;
    public String extern_name;
    public String reason;
    public int amount;
    public String date;
    public String comment;
    public String type;
    public int author_id;
    public int from_user_id;
    public int to_user_id;

    public User getFromUser() {
        return DataStorage.getInstance().group.getUserById(from_user_id);
    }

    public User getToUser() {
        return DataStorage.getInstance().group.getUserById(to_user_id);
    }

    public User getAuthor() {
        return DataStorage.getInstance().group.getUserById(author_id);
    }

    public Date getDate() {
        return Formatter.parseDate(date);
    }
}
