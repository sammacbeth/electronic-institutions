package uk.ac.imperial.einst.vote;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Preferences {

	final List<Object> list;

	public Preferences() {
		super();
		this.list = new LinkedList<Object>();
	}

	public Preferences(Object... prefs) {
		super();
		this.list = new ArrayList<Object>(prefs.length);
		for (Object o : prefs) {
			this.list.add(o);
		}
	}

	public void addPreference(Object option) {
		list.add(option);
	}

	public List<Object> getList() {
		return list;
	}

	@Override
	public String toString() {
		return "" + list + "";
	}

}
