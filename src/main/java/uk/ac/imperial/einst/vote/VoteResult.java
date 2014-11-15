package uk.ac.imperial.einst.vote;


public class VoteResult {

	final public static String NO_WINNER = "none";

	final Ballot ballot;
	int invalid = 0;
	Object winner = NO_WINNER;

	public VoteResult(Ballot ballot) {
		super();
		this.ballot = ballot;
	}
	
	public VoteResult(Ballot ballot, Object winner, int invalid) {
		this(ballot);
		this.winner = winner;
		this.invalid = invalid;
	}

	void setInvalidCount(int invalidVotes) {
		this.invalid = invalidVotes;
	}

	public Object getWinner() {
		return winner;
	}

	void setWinner(Object winner) {
		this.winner = winner;
	}

	public Ballot getBallot() {
		return ballot;
	}

	@Override
	public String toString() {
		return "voteResult(" + ballot.getIssue().getName() + ", " + invalid
				+ " invalid)";
	}

}
