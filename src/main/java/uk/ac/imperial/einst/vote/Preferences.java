package uk.ac.imperial.einst.vote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	
	public static Preferences generate(VoteMethod type, Map<Object, ? extends Number> rankings, final boolean asc, int prefCount) {
		List<Map.Entry<Object, ? extends Number>> prefList = new LinkedList<Map.Entry<Object, ? extends Number>>(
				rankings.entrySet());
		Collections.shuffle(prefList);
		Collections
				.sort(prefList,
						new Comparator<Map.Entry<Object, ? extends Number>>() {
							@Override
							public int compare(
									Entry<Object, ? extends Number> o1,
									Entry<Object, ? extends Number> o2) {
								return Double.compare(
										o1.getValue().doubleValue(),
										o2.getValue().doubleValue());
							}
						});
		if(!asc)
			Collections.reverse(prefList);
		if(type == VoteMethod.PREFERENCE) {
			prefList = prefList.subList(0, prefCount);
		}
		Preferences prefs = new Preferences();
		for (Map.Entry<Object, ? extends Number> e : prefList) {
			prefs.addPreference(e.getKey());
		}
		return prefs;
	}

}
