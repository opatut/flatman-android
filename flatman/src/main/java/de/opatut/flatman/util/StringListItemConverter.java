package de.opatut.flatman.util;

public abstract class StringListItemConverter<T> {
	public StringListAdapter<T> adapter;
	
	public abstract long getItemId(T item);
	public abstract String getText(T item, int id);
}
