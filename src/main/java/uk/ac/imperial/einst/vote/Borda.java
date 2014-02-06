package uk.ac.imperial.einst.vote;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Borda {
	public static String NAME = "borda";

	public static VoteResult determineWinner(Ballot ballot, Set<Vote> votes) {
		if (ballot.getIssue().getMethod() != VoteMethod.RANK_ORDER)
			throw new RuntimeException("Cannot count "
					+ ballot.getIssue().getMethod().name()
					+ " votes with Borda method.");
		final Object[] validOptions = ballot.getOptions();
		// map options to array index
		Map<Object, Integer> optionIdx = new HashMap<Object, Integer>();
		for (int i = 0; i < validOptions.length; i++) {
			optionIdx.put(validOptions[i], i);
		}

		int[] bordaRank = new int[validOptions.length];
		int invalidVotes = 0;
		int n = validOptions.length;
		for (Vote v : votes) {
			if (!(v.getVote() instanceof Preferences)) {
				invalidVotes++;
				continue;
			}
			List<Object> prefs = ((Preferences) v.getVote()).getList();
			// check for duplicate preferences
			if (new HashSet<Object>(prefs).size() != prefs.size()) {
				invalidVotes++;
				continue;
			}
			for (int k = 0; k < prefs.size(); k++) {
				Object opt = prefs.get(k);
				if (optionIdx.containsKey(opt)) {
					bordaRank[optionIdx.get(opt)] += n - k;
				}
			}
		}

		VoteResult res = new VoteResult(ballot);
		res.setInvalidCount(invalidVotes);
		for (int i = 0; i < bordaRank.length; i++) {
			res.setScore(validOptions[i], bordaRank[i]);
		}
		return res;
	}
}
