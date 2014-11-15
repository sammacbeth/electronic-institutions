package uk.ac.imperial.einst.vote;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Winner determination method where the most named candidate wins. Only works
 * with single and preference votes. Ties are not resolved, there is simply no
 * winner.
 * 
 * @author Sam Macbeth
 * 
 */
public class Plurality {

	public static String NAME = "plurality";

	public static VoteResult determineWinner(Ballot ballot, Set<Vote> votes) {
		VoteMethod method = ballot.getIssue().getMethod();
		if (method != VoteMethod.SINGLE && method != VoteMethod.PREFERENCE) {
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

		int invalidVotes = 0;
		List<Object> voteValues = new LinkedList<Object>();
		// expand preference votes
		if (method == VoteMethod.PREFERENCE) {
			for (Vote v : votes) {
				if (v.getVote() instanceof Preferences) {
					voteValues.addAll(((Preferences) v.getVote()).getList());
				} else
					invalidVotes++;
			}
		} else {
			for (Vote v : votes) {
				voteValues.add(v.getVote());
			}
		}
		// tally votes
		for (Object val : voteValues) {
			if (optionIdx.containsKey(val)) {
				voteCount[optionIdx.get(val)]++;
			} else {
				invalidVotes++;
			}
		}

		// create results
		AutoCountVoteResult res = new AutoCountVoteResult(ballot);
		res.setInvalidCount(invalidVotes);
		for (int i = 0; i < voteCount.length; i++) {
			Object opt = validOptions[i];
			int count = voteCount[i];
			res.setScore(opt, count);
		}
		return res;
	}
}
