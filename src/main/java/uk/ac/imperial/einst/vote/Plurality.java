package uk.ac.imperial.einst.vote;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Plurality {

	public static String NAME = "plurality";

	public static VoteResult determineWinner(Ballot ballot, Set<Vote> votes) {
		if (ballot.getIssue().getMethod() != VoteMethod.SINGLE_CANDIDATE) {
			throw new RuntimeException("Cannot count "
					+ ballot.getIssue().getMethod().name()
					+ " votes with Plurality method.");
		}
		final Object[] validOptions = ballot.getOptions();
		int[] voteCount = new int[validOptions.length];
		// map options to array index
		Map<Object, Integer> optionIdx = new HashMap<Object, Integer>();
		for (int i = 0; i < validOptions.length; i++) {
			optionIdx.put(validOptions[i], i);
		}
		// tally votes
		int invalidVotes = 0;
		for (Vote v : votes) {
			final Object val = v.getVote();
			if (optionIdx.containsKey(val)) {
				voteCount[optionIdx.get(val)]++;
			} else {
				invalidVotes++;
			}
		}
		// create results
		int best = 0;
		Set<Object> leaders = new HashSet<Object>();

		VoteResult res = new VoteResult(ballot);
		res.setInvalidCount(invalidVotes);
		for (int i = 0; i < voteCount.length; i++) {
			Object opt = validOptions[i];
			int count = voteCount[i];
			res.setScore(opt, count);
			if (count > best) {
				best = count;
				leaders.clear();
				leaders.add(opt);
			} else if (count == best) {
				leaders.add(opt);
			}
		}

		if (leaders.size() == 1)
			// outright winner
			res.setWinner(leaders.iterator().next());
		else
			// tie - no winner
			res.setWinner(null);

		return res;
	}

}
