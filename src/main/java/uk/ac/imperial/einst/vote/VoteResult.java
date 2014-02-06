package uk.ac.imperial.einst.vote;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VoteResult {

	final public static String NO_WINNER = "none";

	final Ballot ballot;
	int invalid = 0;
	final Map<Object, Integer> scores = new HashMap<Object, Integer>();

	private int best = 0;
	private Set<Object> leaders = new HashSet<Object>();

	public VoteResult(Ballot ballot) {
		super();
		this.ballot = ballot;
	}

	void setInvalidCount(int invalidVotes) {
		this.invalid = invalidVotes;
	}

	void setScore(Object opt, int score) {
		scores.put(opt, score);
		if (score > best) {
			best = score;
			leaders.clear();
			leaders.add(opt);
		} else if (score == best) {
			leaders.add(opt);
		}
	}

	public Object getWinner() {
		return leaders.size() == 1 ? leaders.iterator().next() : NO_WINNER;
	}

	public Ballot getBallot() {
		return ballot;
	}

	@Override
	public String toString() {
		return "voteResult(" + ballot + ", " + invalid + " invalid, " + scores
				+ ")";
	}

}
