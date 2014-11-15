package uk.ac.imperial.einst.vote;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AutoCountVoteResult extends VoteResult {

	final Map<Object, Integer> scores = new HashMap<Object, Integer>();

	private int best = 0;
	private Set<Object> leaders = new HashSet<Object>();

	final boolean tieBreakRand;

	public AutoCountVoteResult(Ballot ballot) {
		this(ballot, false);
	}

	public AutoCountVoteResult(Ballot ballot, boolean tieBreakRand) {
		super(ballot);
		this.tieBreakRand = tieBreakRand;
	}

	void setScore(Object opt, int score) {
		scores.put(opt, score);
		if (score > best) {
			best = score;
			leaders.clear();
			leaders.add(opt);
		} else if (score == best && best > 0) {
			leaders.add(opt);
		}
	}

	@Override
	public Object getWinner() {
		if (leaders.contains(winner))
			return winner;
		else if (leaders.size() == 1) {
			winner = leaders.iterator().next();
			return winner;
		} else if (tieBreakRand && leaders.size() > 0) {
			int element = new Random().nextInt(leaders.size());
			int i = 0;
			for (Object o : leaders) {
				if (i == element) {
					winner = o;
					return winner;
				}
				i++;
			}
		}
		return NO_WINNER;
	}

	@Override
	public String toString() {
		return "voteResult(" + ballot.getIssue().getName() + ", " + invalid
				+ " invalid, " + scores + ")";
	}
}
