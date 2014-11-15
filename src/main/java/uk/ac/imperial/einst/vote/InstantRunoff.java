package uk.ac.imperial.einst.vote;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InstantRunoff {

	public static String NAME = "instantrunoff";
	
	public static VoteResult determineWinner(Ballot ballot, Set<Vote> votes) {
		
		VoteMethod method = ballot.getIssue().getMethod();
		if (method != VoteMethod.PREFERENCE) {
			throw new RuntimeException("Cannot count "
					+ ballot.getIssue().getMethod().name()
					+ " votes with Instant Runoff method.");
		}
		final Object[] validOptions = ballot.getOptions();

		Map<Object, Set<Vote>> votePiles = new HashMap<Object, Set<Vote>>();
		Map<Object, Double> voteCount = new HashMap<Object, Double>();
		for(Object o : validOptions) {
			votePiles.put(o, new HashSet<Vote>());
			voteCount.put(o, 0.0);
		}
		
		// rounds
		Set<Object> eliminated = new HashSet<Object>();
		Set<Vote> invalid = new HashSet<Vote>();
		do {
			for(Vote v : votes) {
				if (v.getVote() instanceof Preferences) {
					List<Object> prefs = ((Preferences) v.getVote()).getList();
					for(Object o : prefs) {
						if(!votePiles.containsKey(o)) {
							invalid.add(v);
							break;
						}
						if(!eliminated.contains(o)) {
							// valid candidate still in running
							votePiles.get(o).add(v);
							voteCount.put(o, voteCount.get(o) + v.weight);
							break;
						}
					}
				} else
					invalid.add(v);
			}
			votes.removeAll(invalid);
			// look for winner (> 50% votes)
			double leaderCount = 0;
			double total = 0;
			Set<Object> leaders = new HashSet<Object>();
			Set<Object> last = new HashSet<Object>();
			double lastCount = 0;
			for(Map.Entry<Object, Double> e : voteCount.entrySet()) {
				final double count = e.getValue();
				total += count;
				if(count >= leaderCount) {
					leaderCount = count;
					if(count > leaderCount)
						leaders.clear();
					leaders.add(e.getKey());
				}
				if(last == null || count <= lastCount) {
					lastCount = count;
					if(count < lastCount)
						last.clear();
					last.add(e.getKey());
				}
			}
			if(leaderCount > (total/2)) {
				// outright winner, declare it
				return new VoteResult(ballot, leaders.iterator().next(), invalid.size());
			} else if(validOptions.length - eliminated.size() <= 2) {
				if(validOptions[0] instanceof Integer || validOptions[0] instanceof Double) {
					// mid point result
					double sum = 0;
					int n = 0;
					for(Object o : leaders) {
						sum += Double.parseDouble(o.toString());
						n++;
					}
					double mean = sum/n;
					double bestdiff = -1;
					Object best = null;
					for(Object o : validOptions) {
						double v = Double.parseDouble(o.toString());
						double diff = Math.abs(mean - v);
						if(best == null || diff <= bestdiff) {
							best = o;
							bestdiff = diff;
						}
					}
					return new VoteResult(ballot, best, invalid.size());
				} else {
					return new VoteResult(ballot, leaders.iterator().next(), invalid.size());
				}
			}
			// no outright winner, do runoff
			eliminated.addAll(last);
			votes.clear();
			for(Object o : last) {
				voteCount.remove(o);
				votes.addAll(votePiles.get(o));
			}
		} while(true);
	}
	
}
