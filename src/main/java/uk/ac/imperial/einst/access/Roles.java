package uk.ac.imperial.einst.access;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Roles {

	static Map<String, Set<String>> SINGLE_ROLESETS = new HashMap<String, Set<String>>();

	public static Set<String> set(String... roles) {
		switch (roles.length) {
		case 0:
			return Collections.emptySet();
		case 1:
			if (!SINGLE_ROLESETS.containsKey(roles[0])) {
				Set<String> rs = new HashSet<String>();
				rs.add(roles[0]);
				SINGLE_ROLESETS.put(roles[0], Collections.unmodifiableSet(rs));
			}
			return SINGLE_ROLESETS.get(roles[0]);
		default:
			Set<String> roleSet = new HashSet<String>();
			roleSet.addAll(Arrays.asList(roles));
			return Collections.unmodifiableSet(roleSet);
		}
	}

	public static Set<String> union(Set<String> s1, Set<String> s2) {
		Set<String> union = new HashSet<String>(s1);
		union.addAll(s2);
		return Collections.unmodifiableSet(union);
	}

	public static Set<String> union(Set<String>... sets) {
		Set<String> union = new HashSet<String>();
		for (Set<String> set : sets) {
			union.addAll(set);
		}
		return Collections.unmodifiableSet(union);
	}

}
