package uk.ac.imperial.einst.vote;

import java.util.HashMap;
import java.util.Map;

public class VoteResult {

	final Ballot ballot;
	int invalid = 0;
	final Map<Object, Integer> scores = new HashMap<Object, Integer>();
	Object winner;

	public VoteResult(Ballot ballot) {
		super();
		this.ballot = ballot;
	}

	void setInvalidCount(int invalidVotes) {
		this.invalid = invalidVotes;
	}

	void setScore(Object object, int i) {
		scores.put(object, i);
	}

	public Object getWinner() {
		return winner;
	}

	public Ballot getBallot() {
		return ballot;
	}

	public void setWinner(Object winner) {
		this.winner = winner;
	}

	@Override
	public String toString() {
		return "voteResult(" + ballot + ", " + invalid + " invalid, " + scores
				+ ")";
	}

}
